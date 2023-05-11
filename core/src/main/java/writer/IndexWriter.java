package writer;
import directory.Directory;
import document.Document;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

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

    // in-mem inverted index
    private volatile HashMap<FieldTermPair, int[][]> ivt;

    //stored doc fields
    private volatile List<Pair> fdt;

    // calculation of byte used in fdt and ivt
    private AtomicLong bytesUsed;

    // lock for flush and reset byteUsed
    private ReentrantLock ramUsageLock;

    //documentID, increase linearly from 0 since every time an indexWriter is opened
    private AtomicInteger docIDAllocator;
//    private ConcurrentLinkedQueue<Future> futures;

    private LinkedList<Future> futures;

    private HashMap<ByteReference, Pair<byte[], int[]>> fdm;


    public IndexWriter(IndexConfig config, Directory directory) {
        this.config = config;
        this.directory = directory;
        this.ivt = new HashMap<>();
        this.fdt = new ArrayList<>();
        this.bytesUsed = new AtomicLong(0);
        this.ramUsageLock = new ReentrantLock();
        this.docIDAllocator = new AtomicInteger(0);
        this.threadPoolExecutor =  new ThreadPoolExecutor( config.getIndexerThreadNum(),
                config.getIndexerThreadNum(), 0, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());
        this.futures = new LinkedList<>();
        this.fdm = new HashMap<>();
    }

// indexing is by default multithreaded.
    public synchronized void addDoc(Document doc){
        Future<?> future = threadPoolExecutor.submit(new DocWriter(docIDAllocator, doc, fdt, ivt, bytesUsed,
                config.getMaxRamUsage(), ramUsageLock, directory, config, fdm));
        futures.add(future);
    }

    // must call after all addDoc
    public synchronized void commit(){
        int i = 1;
        while (!futures.isEmpty()) {
            try {
                futures.poll().get();
                log.info("commit ===>>> future linked list polled " + (i++) + " task");
            } catch (InterruptedException e) {
                log.info("met InterruptedException during commit ");
            } catch (ExecutionException e) {
                log.error("met ExecutionException during commit");
            }
        }
        threadPoolExecutor.shutdown();
        log.info("commit thread pool shutdown");
        if(ivt.size() != 0 || fdt.size() != 0) {
            DocWriter lastDocWriter = new DocWriter(docIDAllocator, null, fdt, ivt, bytesUsed, config.getMaxRamUsage(),
                    ramUsageLock, directory, config, fdm);
            lastDocWriter.flush();
        }
    }
}
