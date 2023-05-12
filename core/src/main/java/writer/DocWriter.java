package writer;
import directory.Directory;
import document.Document;
import field.DoubleField;
import field.Field;
import field.StringField;
import util.*;
import hawk.segment.core.Term;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Data
public class DocWriter implements Runnable {

    private volatile AtomicInteger docIDAllocator;

    private Document doc;

    private volatile HashMap<ByteReference, Pair<byte[], int[]>> fdm;

    private volatile HashMap<FieldTermPair, List<int[]>> ivt;
    private volatile List<Pair<Integer, byte[][]>> fdt;

    private AtomicLong bytesUsed;

    private ReentrantLock fdmLock;
    private ReentrantLock fdtLock;

    private ReentrantLock ivtLock;


    private Directory directory;

    private IndexConfig config;


    public DocWriter(AtomicInteger docIDAllocator, Document doc, List fdt, HashMap<FieldTermPair,
            List<int[]>> ivt, AtomicLong bytesUsed, ReentrantLock fdmLock,ReentrantLock fdtLock,ReentrantLock ivtLock, Directory directory,
                     IndexConfig config, HashMap<ByteReference, Pair<byte[], int[]>> fdm) {
        this.docIDAllocator = docIDAllocator;
        this.doc = doc;
        this.fdt = fdt;
        this.ivt = ivt;
        this.bytesUsed = bytesUsed;
        this.fdmLock = fdmLock;
        this.fdtLock = fdtLock;
        this.ivtLock = ivtLock;
        this.directory = directory;
        this.config = config;
        this.fdm = fdm;
    }

    @Override
    public void run() {
        WrapLong bytesCurDoc = new WrapLong(0);
        // parallel tokenization here
        Pair<HashMap<ByteReference, Pair<byte[], Integer>>, byte[][]>  storedRet = processStoredFields(doc, bytesCurDoc);
        HashMap<ByteReference, Pair<byte[], Integer>> docFDM = storedRet.getLeft();
        byte[][] docFDT = storedRet.getRight();
        HashMap<FieldTermPair, int[]> docIVT = processIndexedFields(doc, bytesCurDoc);
        //assembling should be protected
        int docID = docIDAllocator.getAndAdd(1);
        // assemble memory index
        assembleFDT(docFDT, docID);
        assembleFDM(docFDM);
        assembleIVT(docIVT, docID);
        bytesUsed.addAndGet(bytesCurDoc.getValue() + 8); //8bytes for 2 docID in FDM and IVT
    }

    public void assembleFDT(byte[][] docFDT, int docID){
        fdtLock.lock();
        fdt.add(new Pair<>(docID, docFDT));
        fdtLock.unlock();
    }
    // doc fdm key: filed name; value1:field type, value2: field value length
    // global fdm key: field name; value left: field type; value right1: field value length, value right2: doc count
    public void assembleFDM(HashMap<ByteReference, Pair<byte[], Integer>> docFDM){
        fdmLock.lock();
        for (Map.Entry<ByteReference, Pair<byte[], Integer>> entry : docFDM.entrySet()){
            Pair<byte[], int[]> pair = fdm.putIfAbsent(entry.getKey(), new Pair<>(entry.getValue().getLeft(),
                    new int[]{entry.getValue().getRight(), 1}));
            if (pair != null){
                int filedLengthSum = pair.getRight()[0];
                filedLengthSum += entry.getValue().getRight();
                int docCount = pair.getRight()[1] + 1;
                pair.setRight(new int[]{filedLengthSum, docCount});
            }
        }
        fdmLock.unlock();
    }


    //key: field term pair; value: doc frequency, field value length
    public void assembleIVT(HashMap<FieldTermPair, int[]> docIVT, int docID){
        ivtLock.lock();
        for (Map.Entry<FieldTermPair, int[] > entry : docIVT.entrySet()) {
            FieldTermPair fieldTermPair = entry.getKey();
            //assemble ivt
            int[] IDFreqLength = new int[]{docID, entry.getValue()[0], entry.getValue()[1]};
            List<int[]> value = new ArrayList<>();
            value.add(IDFreqLength);
            List<int[]> oldVal = ivt.putIfAbsent(fieldTermPair, value);
            if(oldVal != null){ // if already a posting exists, concatenates old and new
                oldVal.add(IDFreqLength);
            }
        }
        ivtLock.unlock();
    }

    public Pair<HashMap<ByteReference, Pair<byte[], Integer>>, byte[][]> processStoredFields(Document doc, WrapLong bytesCurDoc) {
        HashMap<ByteReference, Pair<byte[], Integer>> docFDM = new HashMap<>();
        byte[][] bytePool = new byte[10][];
        HashMap<String, Field> fieldMap = doc.getFieldMap();
        int i = 0;
        for (Map.Entry<String, Field> entry : fieldMap.entrySet()) {
            Field field = entry.getValue();
            if (field.isStored() == Field.Stored.YES) {
                byte[] fieldBytes = field.customSerialize();
                if (bytePool.length < i + 1) {
                    bytePool = ArrayUtil.bytePoolGrow(bytePool);
                }
                bytePool[i] = fieldBytes;
                bytesCurDoc.setValue(bytesCurDoc.getValue() + fieldBytes.length);
            }
            i++;
            // doc fdm
            byte[] fieldName = field.serializeName();
            byte fieldType = getFieldType(field);
            int filedLength = 0;
            if(field instanceof StringField){
                filedLength = ((StringField) field).getValue().length();
            }else if(field instanceof  DoubleField){
                filedLength = 1;
            }

            assembleFieldTypeMap(docFDM, fieldName, new byte[]{fieldType},filedLength, bytesCurDoc);
        }
        Pair<HashMap<ByteReference, Pair<byte[], Integer>>, byte[][]> ret = new Pair<>(docFDM, bytePool);
        return ret;
    }

    public HashMap<FieldTermPair, int[]> processIndexedFields(Document doc, WrapLong bytesCurDoc){
        HashMap<FieldTermPair, int[]> fieldTermMap = new HashMap<>();
        HashMap<String, Field> fieldMap = doc.getFieldMap();
        for (Map.Entry<String, Field> entry : fieldMap.entrySet()) {
            Field field = entry.getValue();
            if(field.isTokenized() == Field.Tokenized.YES){
                processIndexedField(field, fieldTermMap, bytesCurDoc);
            }
        }
        return fieldTermMap;
    }

    public byte getFieldType(Field field){
        byte termType = 0b00000000;
        if(field.isStored() == Field.Stored.YES){
            termType |= 0b00000001;
        }
        if(field.isTokenized() == Field.Tokenized.YES){
            termType |= 0b00000010;
        }
        if(field instanceof DoubleField){
            termType |= 0b00000100;
        } else if (field instanceof StringField) {
            termType |= 0b00001000;
        }
        return termType;
    }

    public void assembleFieldTypeMap(HashMap<ByteReference, Pair<byte[], Integer>> fieldTypeMap, byte[] fieldName, byte[] type,
                                     int fieldLength, WrapLong bytesCurDoc){
        Pair ret = fieldTypeMap.putIfAbsent(new ByteReference(fieldName), new Pair<>(type, fieldLength));
        if(ret != null){
            bytesCurDoc.setValue(bytesCurDoc.getValue() + fieldName.length + type.length + 4);
        }
    }


    public void assembleFieldTermMap(HashMap<FieldTermPair, int[]> fieldTermMap, byte[] filedName, byte[] filedValue,
                                     WrapLong bytesCurDoc, int fieldLength){
        FieldTermPair fieldTermPair = new FieldTermPair(filedName, filedValue);
        int[] preValue = fieldTermMap.putIfAbsent(fieldTermPair, new int[]{1, fieldLength});
        if(preValue != null){
            fieldTermMap.put(fieldTermPair, new int[]{preValue[0] + 1, fieldLength});
        }else{// 8 bytes from docID, frequency and fieldLength
            bytesCurDoc.setValue(bytesCurDoc.getValue() + filedName.length + filedValue.length + 8);
        }
    }

    public void processIndexedField(Field field, HashMap<FieldTermPair, int[]> fieldTermMap,
                                    WrapLong bytesCurDoc){
        byte[] filedName = field.serializeName();
        int filedLength = 0;
        if (field instanceof StringField){
            // since analyzer returns position information, terms in same field
            //with same value may be identified as different terms as their positions differ
            HashSet<Term> termSet = config.getAnalyzer().anlyze(((StringField) field).getValue(),
                    ((StringField) field).getName());
            for (Term t : termSet) {
                byte[] filedValue = t.getValue().getBytes(StandardCharsets.UTF_8);
                filedLength = ((StringField) field).getValue().length();
                assembleFieldTermMap(fieldTermMap,filedName,filedValue,bytesCurDoc, filedLength);
            }
        } else if (field instanceof DoubleField) {
            double value = ((DoubleField) field).getValue();
            long sortableLong = NumberUtil.double2SortableLong(value);// double to sort
            String[] prefixString = NumberUtil.long2PrefixString(sortableLong,config.getPrecisionStep());
            filedLength = 1;
            for (int i = 0; i < prefixString.length; i++) {
                assembleFieldTermMap(fieldTermMap, filedName,prefixString[i].getBytes(StandardCharsets.UTF_8), bytesCurDoc, filedLength);
            }
        }
    }

}
