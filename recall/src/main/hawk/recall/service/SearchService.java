package hawk.recall.service;

import directory.Directory;
import directory.MMapDirectory;
import document.Document;
import field.DoubleField;
import hawk.segment.core.anlyzer.Analyzer;
import hawk.segment.core.anlyzer.NShortestPathAnalyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import reader.DirectoryReader;
import search.*;
import util.DateUtil;
import writer.IndexConfig;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class SearchService {

    private Searcher searchEngine;

    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    @Autowired
    Environment env;

    @Value("${base.path}")
    private String basePath;

    public void initDemoSearchEngine(){
        String shard = env.getProperty("spring.cloud.nacos.discovery.metadata.shard");
        String indexPath = basePath.concat("/").concat("/").concat(shard);
        Directory directory = MMapDirectory.open(Paths.get(indexPath));
        DirectoryReader directoryReader = DirectoryReader.open(directory);
        Analyzer analyzer = new NShortestPathAnalyzer(1);
        IndexConfig indexConfig = new IndexConfig(analyzer);
        Searcher searcher = new Searcher(directoryReader, indexConfig);
        this.searchEngine = searcher;
    }

    public void initSearchEngine(){
        String dateStr = DateUtil.getDateStr();
        String shard = env.getProperty("spring.cloud.nacos.discovery.metadata.shard");
        String indexPath = basePath.concat("/").concat(dateStr).concat("/").concat(shard);
        Directory directory = MMapDirectory.open(Paths.get(indexPath));
        DirectoryReader directoryReader = DirectoryReader.open(directory);
        Analyzer analyzer = new NShortestPathAnalyzer(1);
        IndexConfig indexConfig = new IndexConfig(analyzer);
        Searcher searcher = new Searcher(directoryReader, indexConfig);
        this.searchEngine = searcher;
    }

    public List<Document> search(String query){
        List<Document> result = new ArrayList<>();
        Query strQuery = new StringQuery("title", query);
        readWriteLock.readLock().lock(); // allow concurrent read, but disallow reading while switching engine
        ScoreDoc[] hits = this.searchEngine.search(strQuery, 1000);
        for (int i = 0; i < hits.length; i++) {
            Document doc = this.searchEngine.doc(hits[i]);
            result.add(doc);
        }
        readWriteLock.readLock().unlock();
        return result;
    }

    public List<Document> rangeSearch(double left, double right){
        List<Document> result = new ArrayList<>();
        Query rangeQuery = new NumericRangeQuery("price", left, right);
        readWriteLock.readLock().lock(); // allow concurrent read, but disallow reading while switching engine
        ScoreDoc[] hits = this.searchEngine.search(rangeQuery, 1000);
        for (int i = 0; i < hits.length; i++) {
            Document doc = this.searchEngine.doc(hits[i]);
            result.add(doc);
        }
        readWriteLock.readLock().unlock();

        Collections.sort(result,(a, b)->{
            double priceA = ((DoubleField) a.getFieldMap().get("price")).getValue();
            double priceB = ((DoubleField)b.getFieldMap().get("price")).getValue();
            return Double.compare(priceA,priceB);
        });

        result = result.subList(0,Math.min(1000, result.size()));
        return result;
    }



    public synchronized void switchSearchEngine(){
        // init a new search engine
        Directory directory = MMapDirectory.open(Paths.get(basePath));
        DirectoryReader directoryReader = DirectoryReader.open(directory);
        Analyzer analyzer = new NShortestPathAnalyzer(1);
        IndexConfig indexConfig = new IndexConfig(analyzer);
        Searcher newSearcher = new Searcher(directoryReader, indexConfig);
        // replace the old with the new
        readWriteLock.writeLock().lock();
        Searcher oldSearcher = this.searchEngine;
        this.searchEngine = newSearcher;
        readWriteLock.writeLock().unlock();
        oldSearcher.close();
    }

    public Boolean pullIndex(String tempStamp){
        return null;
    }


}
