package demo;

import directory.MMapDirectory;
import document.Document;
import field.DoubleField;
import field.Field;
import field.StringField;
import hawk.segment.core.anlyzer.Analyzer;
import hawk.segment.core.anlyzer.NShortestPathAnalyzer;
import lombok.extern.slf4j.Slf4j;
import writer.IndexConfig;
import writer.IndexWriter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;


@Slf4j
public class WriteIndexFromFile {

    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        MMapDirectory mMapDirectory = new MMapDirectory(Paths.get("/opt/index/shard4"));
        Analyzer analyzer = new NShortestPathAnalyzer(1);
        IndexConfig indexConfig = new IndexConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(indexConfig, mMapDirectory);
        URL resource = ClassLoader.getSystemResource("goods-short.csv");
        String path = resource.getPath();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        String line;
        int id = 1;
        while ((line = bufferedReader.readLine()) != null){
            String[] strings = line.split("\t");
            String title = strings[1];
            float price = Float.parseFloat(strings[2]);
            double doublePrice = (double) price;
            Document document = new Document();
            StringField idField = new StringField("id", Integer.toString(id++), Field.Tokenized.NO, Field.Stored.YES);
            StringField stringField = new StringField("title", title, Field.Tokenized.YES, Field.Stored.YES);
            DoubleField doubleField = new DoubleField("price", doublePrice, Field.Tokenized.YES, Field.Stored.YES);
            document.add(idField);
            document.add(stringField);
            document.add(doubleField);
            indexWriter.addDoc(document);
        }
        bufferedReader.close();
        indexWriter.commit();
        long end = System.currentTimeMillis();
        log.info("total takes " + (end-start) + " milliseconds");
    }
}
