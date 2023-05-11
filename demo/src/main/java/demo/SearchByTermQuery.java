package demo;

import com.alibaba.fastjson.JSON;
import directory.Directory;
import directory.MMapDirectory;
import document.Document;
import reader.DirectoryReader;
import search.*;
import writer.IndexConfig;
import hawk.segment.core.anlyzer.Analyzer;
import hawk.segment.core.anlyzer.NShortestPathAnalyzer;

import java.io.IOException;
import java.nio.file.Paths;

public class SearchByTermQuery {

    public static void main(String[] args) throws IOException {
        Directory directory = MMapDirectory.open(Paths.get("/opt/index/shard4"));
        DirectoryReader directoryReader = DirectoryReader.open(directory);
        Analyzer analyzer = new NShortestPathAnalyzer(1);
        IndexConfig indexConfig = new IndexConfig(analyzer);
        Searcher searcher = new Searcher(directoryReader, indexConfig);
        Query query = new TermQuery("title", "剃须刀");
        ScoreDoc[] hits = searcher.search(query, 10);
        for (int i = 0; i < hits.length; i++) {
            Document doc = searcher.doc(hits[i]);
            System.out.println(JSON.toJSONString(doc));
        }

        searcher.close();
    }
}
