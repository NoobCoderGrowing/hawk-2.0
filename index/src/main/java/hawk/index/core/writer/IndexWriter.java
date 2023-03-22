package hawk.index.core.writer;
import hawk.index.core.directory.Directory;
import hawk.index.core.document.Document;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
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
    private final IndexWriterConfig config;

    private final Directory directory;

    private ThreadPoolExecutor threadPoolExecutor;

    // in-mem inverted index
    private volatile HashMap<FieldTermPair, int[]> ivt;

    //stored doc fields
    private volatile HashMap<Integer, byte[][]> fdt;

    // calculation of byte used in fdt and ivt
    private volatile Long bytesUsed;

    // lock for flush and reset byteUsed
    private ReentrantLock ramUsageLock;

    //documentID, increase linearly from 0 since every time an indexWriter is opened
    private AtomicInteger docIDAllocator;

    private List<Future> futures;

    

    public IndexWriter(IndexWriterConfig config, Directory directory) {
        this.config = config;
        this.directory = directory;
        this.ivt = new HashMap<>();
        this.fdt = new HashMap<>();
        this.bytesUsed = new Long(0);
        this.ramUsageLock = new ReentrantLock();
        this.docIDAllocator = new AtomicInteger(0);
        this.threadPoolExecutor =  new ThreadPoolExecutor( config.getIndexerThreadNum(),
                config.getIndexerThreadNum(), 0, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());
        this.futures = new ArrayList<>();
    }

// indexing is by default multithreaded. User specified multi-thread-indexing leads to unpredictable outcome.
    public void addDoc(Document doc){
        Future<?> future = threadPoolExecutor.submit(new DocWriter(docIDAllocator, doc, fdt, ivt, bytesUsed,
                config.getMaxRamUsage(), ramUsageLock, config.getAnalyzer()));
        futures.add(future);
    }


    public void commit(){
        for (int i = 0; i < futures.size(); i++) {
            try {
                futures.get(i).get();
            } catch (InterruptedException e) {
                log.info("wait task " + i + "successful");
            } catch (ExecutionException e) {
                log.error("something wrong with task " + i);
            }
        }
        threadPoolExecutor.shutdown();
        if(ivt.size() != 0){
            DocWriter lastDocWriter = new DocWriter(docIDAllocator, null, fdt, ivt, bytesUsed, config.getMaxRamUsage(),
                    ramUsageLock, config.getAnalyzer());
            lastDocWriter.flush();
        }
        directory.updateSegInfo();
    }

    public static void main(String[] args) {
    }
}
