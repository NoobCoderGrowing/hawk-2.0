package writer;
import directory.Directory;
import document.Document;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import util.DataOutput;
import util.WrapInt;
import util.WrapLong;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

@Data
@Slf4j
public class IndexWriter {

    /** indexWriter configuration
     * Configurable object:
     *  Analyzer
     *  Thread count used to do indexing
     *  Maximum memory allowed to do indexing
     *  **/
    private final IndexConfig config;

    private final Directory directory;

    private ThreadPoolExecutor threadPoolExecutor;

    private volatile HashMap<ByteReference, Pair<byte[], int[]>> fdm;

//    stored doc fields
    private volatile List<Pair<Integer, byte[][]>> fdt;

//     in-mem inverted index
    private volatile HashMap<FieldTermPair, List<int[]>> ivt;

    private AtomicLong bytesUsed;

    // lock for flush and reset byteUsed
    private ReentrantLock fdmLock;
    private ReentrantLock fdtLock;

    private ReentrantLock ivtLock;

    //documentID, increase linearly from 0 since every time an indexWriter is opened
    private AtomicInteger docIDAllocator;

    private LinkedList<Future> futures;




    public IndexWriter(IndexConfig config, Directory directory) {
        this.config = config;
        this.directory = directory;
        this.fdm = new HashMap<>();
        this.fdt = new ArrayList<>();
        this.ivt = new HashMap<>();
        this.bytesUsed = new AtomicLong(0);
        this.fdmLock = new ReentrantLock();
        this.fdtLock = new ReentrantLock();
        this.ivtLock = new ReentrantLock();
        this.docIDAllocator = new AtomicInteger(0);
        this.threadPoolExecutor =  new ThreadPoolExecutor( config.getIndexerThreadNum(),
                config.getIndexerThreadNum(), 0, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());
        this.futures = new LinkedList<>();

    }

// indexing is by default multithreaded.
    public synchronized void addDoc(Document doc){
        Future<?> future = threadPoolExecutor.submit(new DocWriter(docIDAllocator, doc, fdt, ivt, bytesUsed,
                fdmLock,fdtLock,ivtLock, directory, config, fdm));
        futures.add(future);
    }

    public synchronized void addDocs(List<Document> docs){
        for (int i = 0; i < docs.size(); i++) {
            Future<?> future = threadPoolExecutor.submit(new DocWriter(docIDAllocator, docs.get(i), fdt, ivt, bytesUsed,
                    fdmLock,fdtLock,ivtLock, directory, config, fdm));
            futures.add(future);
        }
    }

    // must call after all addDoc
    public synchronized void commit(){
        int i = 1;
        log.info("start generating inverted index");
        long start = System.currentTimeMillis();
        while (!futures.isEmpty()) {
            try {
                futures.poll().get();
//                log.info("commit ===>>> future linked list polled " + (i++) + " task");
            } catch (InterruptedException e) {
                log.info("met InterruptedException during commit ");
            } catch (ExecutionException e) {
                log.error("met ExecutionException during commit");
            }
        }
        long end = System.currentTimeMillis();
        log.info("end of generating inverted index");
        log.info("generating inverted index takes " + (end-start) + " milliseconds");
        threadPoolExecutor.shutdown();
        log.info("index writer thread pool shutdown");
        flush();
    }

    public void flush(){
        log.info("start flushing");
        Path[] files = directory.generateSegFiles();
        int docBase = directory.getSegmentInfo().getPreMaxID();
        Path fdtPath = files[0];
        Path fdxPath = files[1];
        Path timPath = files[2];
        Path frqPath = files[3];
        Path fdmPath = files[4];

        long start = System.currentTimeMillis();
        // sort fdt
        Collections.sort(fdt, (o1, o2) -> {
            Integer a = (Integer) o1.getLeft();
            Integer b = (Integer) o2.getLeft();
            return  a - b;
        });
        // sort fdm (by field lexicographically)
        ArrayList<Map.Entry<ByteReference, Pair<byte[], int[]>>> fdmList = new ArrayList<>(fdm.entrySet());
        sortFDM(fdmList);
        // there is no need to sort fdt
        // sort ivt ( sort field first and then term lexicographically)
        List<Map.Entry<FieldTermPair, List<int[]>>> ivtList = new ArrayList<>(ivt.entrySet());
        sortIVTList(ivtList);
        long end = System.currentTimeMillis();
        log.info("sorting takes " + (end - start) + " milliseconds");

        start = System.currentTimeMillis();
        //posting is already sorted
        flushStored(fdtPath, fdxPath, docBase);
        flushIndexed(timPath, frqPath, fdmPath, ivtList, fdmList, docBase);
        directory.updateSegInfo(docIDAllocator.get() + docBase, 1);
        end = System.currentTimeMillis();
        log.info("flush to disk takes " + (end - start) + " milliseconds");

        start = System.currentTimeMillis();
        mergetest(docBase);
        end = System.currentTimeMillis();
        log.info("merge index takes " + (end - start) + " milliseconds");
    }

    public void sortFDM(ArrayList<Map.Entry<ByteReference, Pair<byte[], int[]>>> fdmList){
        Collections.sort(fdmList, (a, b) -> {
            byte[] aField = a.getKey().getBytes();
            byte[] bField = b.getKey().getBytes();
            for (int i = 0; i < aField.length && i < bField.length; i++) {
                int aFieldByte = (aField[i] & 0xff);
                int bFieldByte = (bField[i] & 0xff);
                if(aFieldByte != bFieldByte) {
                    return aFieldByte - bFieldByte;
                }
            }
            return aField.length - bField.length;
        });
    }

    public void sortIVTList(List<Map.Entry<FieldTermPair, List<int[]>>> ivtList){
        // sort FieldTermPair
        Collections.sort(ivtList,(a, b)->{
            FieldTermPair aP = a.getKey();
            FieldTermPair bP = b.getKey();
            byte[] aField = aP.getField();
            byte[] aTerm = aP.getTerm();
            byte[] bField = bP.getField();
            byte[] bTerm = bP.getTerm();
            for (int i = 0; i < aField.length && i < bField.length; i++) {
                int aFieldByte = (aField[i] & 0xff);
                int bFieldByte = (bField[i] & 0xff);
                if(aFieldByte != bFieldByte) {
                    return aFieldByte - bFieldByte;
                }
            }
            if(aField.length != bField.length){
                return aField.length - bField.length;
            }else{
                for (int i = 0; i < aTerm.length && i < bTerm.length; i++) {
                    int aTermByte = (aTerm[i] & 0xff);
                    int bTermByte = (bTerm[i] & 0xff);
                    if(aTermByte != bTermByte) {
                        return aTermByte - bTermByte;
                    }
                }
                return aTerm.length - bTerm.length;
            }
        });
        //sort posting
        for (int i = 0; i < ivtList.size(); i++) {
            List<int[]> posting = ivtList.get(i).getValue();
            Collections.sort(posting, (a, b)->{
                return a[0] - b[0];
            });
        }
    }

    public void flushIndexed(Path timPath, Path frqPath, Path fdmPath, List<Map.Entry<FieldTermPair, List<int[]>>>
            ivtList, ArrayList<Map.Entry<ByteReference, Pair<byte[], int[]>>> fdmList, int docBase){
        try {
            FileChannel timChannel = new RandomAccessFile(timPath.toAbsolutePath().toString(),
                    "rw").getChannel();
            FileChannel frqChannel = new RandomAccessFile(frqPath.toAbsolutePath().toString(),
                    "rw").getChannel();
            FileChannel fdmChannel = new RandomAccessFile(fdmPath.toAbsolutePath().toString(),
                    "rw").getChannel();
            // write fdm
            writeFDM(fdmChannel, fdmList);
            WrapLong frqPos = new WrapLong(0);
            WrapLong timPos = new WrapLong(0);
            log.info("start writing tim and frq");
            for (int i = 0; i < ivtList.size(); i++) { // write .tim and .frq
                FieldTermPair fieldTermPair = ivtList.get(i).getKey();
                List<int[]> posting = ivtList.get(i).getValue();
                writeTIM(timChannel, fieldTermPair, timPos, frqPos);
                writeFRQ(frqChannel, posting, frqPos, docBase);
            }
            log.info("end of writing tim and frq");
            timChannel.force(false);
            frqChannel.force(false);
            fdmChannel.force(false);
            timChannel.close();
            frqChannel.close();
            fdmChannel.close();
        } catch (FileNotFoundException e) {
            log.error(".tim / .frq / .fdm file not found");
            System.exit(1);
        } catch (IOException e) {
            log.error("force flush .tim / .frq / .fdm file errored");
        }
    }


    public void writeFDM(FileChannel fc, ArrayList<Map.Entry<ByteReference, Pair<byte[], int[]>>> fdmList){
        WrapLong pos = new WrapLong(0);
        log.info("start writting fdm");
        for (int i = 0; i < fdmList.size(); i++) {
            byte[] field = fdmList.get(i).getKey().getBytes();
            byte type = fdmList.get(i).getValue().getLeft()[0];
            int fieldLengthSum = fdmList.get(i).getValue().getRight()[0];
            int docCount = fdmList.get(i).getValue().getRight()[1];
            int length = field.length;
            DataOutput.writeInt(length, fc, pos);
            DataOutput.writeBytes(field, fc, pos);
            DataOutput.writeByte(type, fc, pos);
            DataOutput.writeInt(fieldLengthSum,fc,pos);
            DataOutput.writeInt(docCount, fc, pos);
        }
        log.info("end of writting fdm");
    }

    public void writeTIM(FileChannel fc, FieldTermPair fieldTermPair, WrapLong timPos, WrapLong frqPos){
        byte[] field = fieldTermPair.getField();
        byte[] term = fieldTermPair.getTerm();
//        log.info("tim writing ==> filed name is " + new String(field) + ", term is " + new String(term) +
//                ", frq offset is " + frqPos.getValue());
        DataOutput.writeInt(field.length, fc, timPos);
        DataOutput.writeBytes(field, fc, timPos);
        DataOutput.writeInt(term.length, fc, timPos);
        DataOutput.writeBytes(term, fc, timPos);
        DataOutput.writeVLong(frqPos, fc, timPos);
    }

    public void writeFRQ(FileChannel fc, List<int[]> posting, WrapLong frqPos, int docBase){
        int length = posting.size();
//        log.info("frq writing ==> " + "posting length is " + length);
        DataOutput.writeVInt(length, fc, frqPos);
        for (int i = 0; i < length; i++) {
//            log.info("frq writing ==> " + "doc id is " + posting[i][0] + ", frequency is " + posting[i][1] +
//                    ", field value length is " + posting[i][2]);
            DataOutput.writeVInt(posting.get(i)[0] + docBase, fc, frqPos);
            DataOutput.writeVInt(posting.get(i)[1], fc, frqPos);
            DataOutput.writeVInt(posting.get(i)[2], fc, frqPos);
        }
    }

    public void flushStored(Path fdtPath, Path fdxPath, int docBase){
        try {
            FileChannel fdtChannel = new RandomAccessFile(fdtPath.toAbsolutePath().toString(), "rw").getChannel();
            FileChannel fdxChannel = new RandomAccessFile(fdxPath.toAbsolutePath().toString(), "rw").getChannel();
            // get compression config
            byte[] buffer = new byte[config.getBlocSize()];
            int maxCompressedLength = config.getCompressor().maxCompressedLength(buffer.length);
            byte[] compressedBuffer = new byte[maxCompressedLength];
            WrapInt bufferPos = new WrapInt(0);
            WrapLong fdtPos = new WrapLong(0);
            WrapLong fdxPos = new WrapLong(0);
            log.info("start writing fdx and fdt");
            if(fdt.size()>0){
                int blockStartID = fdt.get(0).getLeft() + docBase;
                for (int i = 0; i < fdt.size(); i++) {
                    int docID = fdt.get(i).getLeft() + docBase;
                    byte[][] data = (byte[][]) fdt.get(i).getRight();
                    if(!insertBlock(docID, data, buffer, bufferPos)){ // if buffer is full, write to disk
                        writeFDX(fdxChannel, blockStartID, fdtPos, fdxPos);
                        writeCompressedBloc(buffer, compressedBuffer, maxCompressedLength, fdtChannel, fdtPos,
                                bufferPos);

                        insertBlock(docID, data, buffer, bufferPos);
                        blockStartID = docID;
                    }
                }
                // last write
                if(bufferPos.getValue() > 0){
                    writeFDX(fdxChannel, blockStartID, fdtPos, fdxPos);
                    writeCompressedBloc(buffer, compressedBuffer, maxCompressedLength, fdtChannel, fdtPos,
                            bufferPos);
                }
            }

            log.info("end of writing fdx and fdt");
            //close channel
            fdxChannel.force(false);
            fdtChannel.force(false);
            fdtChannel.close();
            fdtChannel.close();
        } catch (FileNotFoundException e) {
            log.error("fdt or fdx file has not been generated");
            System.exit(1);
        } catch (IOException e) {
            log.error("sth wrong when close fdx or fdx file channel");
        }
    }

    // write fdt into a buffer of 16kb
    // return false if the buffer can't fit
    public boolean insertBlock(int docID, byte[][] data, byte[] buffer, WrapInt pos){
        int remains = buffer.length - pos.getValue(); // calculate bytes left in the buffer
        int need = 0;
        int notEmpty = 0;
        for (int i = 0; i < data.length; i++) {
            if(data[i]!=null){
                need += data[i].length;
                notEmpty ++;
            }else{
                break;
            }
        }

        if(need + 10 <= remains){ // 10 bytes for docID and field count
            // write docID
            DataOutput.writeVInt(docID, buffer, pos);
            // write field count
            DataOutput.writeVInt(notEmpty, buffer, pos);
            for (int i = 0; i < notEmpty; i++) {
                //write each field
                DataOutput.writeBytes(data[i], buffer, pos);
            }
            return true;
        }
        return false;
    }

    public void writeFDX(FileChannel fc, int docID, WrapLong fdtPos, WrapLong fdxPos){
//        log.info("fdx writing ===> " + "docID is " + docID + ", fdt offset is " + fdtPos.getValue());
        DataOutput.writeVInt(docID, fc, fdxPos);
        DataOutput.writeVLong(fdtPos, fc, fdxPos);
    }

    public void writeCompressedBloc(byte[] buffer, byte[] compressedBuffer, int maxCompressedLength, FileChannel fdtChannel,
                                    WrapLong fdtPos, WrapInt bufferPos){
        //compress
        int compressedLength = config.getCompressor().compress(buffer, 0, buffer.length, compressedBuffer,
                0, maxCompressedLength);
        //write to .fdt
        ByteBuffer byteBuffer = ByteBuffer.wrap(compressedBuffer, 0, compressedLength);
        DataOutput.writeBytes(byteBuffer, fdtChannel, fdtPos);
        // clear buffer and buffer pos
        Arrays.fill(buffer, (byte) 0);
        bufferPos.setValue(0);
        Arrays.fill(compressedBuffer, (byte) 0);
    }

    public void mergetest(int docBase){
        int segCount = directory.getSegmentInfo().getSegCount();
        if(segCount > 1) {
            log.info("merge start now," + " cur segment count is " + segCount + ", cur file number is " +
                    directory.getFiles().size() + ", cur maxDocID is " + directory.getSegmentInfo().getPreMaxID());
            IndexMerger indexMerger = new IndexMerger(directory, config, docIDAllocator.get(), docBase);
            indexMerger.merge();
            log.info("merge end now," + "cur segment count is " + directory.getSegmentInfo().getSegCount() +
                    ", cur file number is " + directory.getFiles().size() + ", cur maxDocID is " +
                    directory.getSegmentInfo().getPreMaxID());
        }
    }
}
