// Indexer.java
package com.dwaipayan.cosinesimilarity;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;


/**
 * @author Dwaipayan
 */
public class Indexer {

    private final File sourceDirectory;
    private final File indexDirectory;
    private static String fieldName;

    public Indexer() {
        this.sourceDirectory = new File(Configuration.SOURCE_DIRECTORY_TO_INDEX);
        this.indexDirectory = new File(Configuration.INDEX_DIRECTORY);
        fieldName = Configuration.FIELD_CONTENT;
    }

    /**
     */
    public void index() throws CorruptIndexException,
            LockObtainFailedException, IOException {
        Directory dir = FSDirectory.open(indexDirectory);
        Analyzer analyzer = new StandardAnalyzer(StandardAnalyzer.STOP_WORDS_SET);  // using stop words
        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_10_2, analyzer);

        if (indexDirectory.exists()) {
            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        } else {
            // Add new documents to an existing index:
            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        }

        IndexWriter writer = new IndexWriter(dir, iwc);
        for (File f : sourceDirectory.listFiles()) {
            Document doc = new Document();
            FieldType fieldType = new FieldType();
            fieldType.setIndexed(true);
            fieldType.setIndexOptions(FieldInfo.IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
            fieldType.setStored(true);
            fieldType.setStoreTermVectors(true);
            fieldType.setTokenized(true);
            Field contentField = new Field(fieldName, getAllText(f), fieldType);
            doc.add(contentField);
            writer.addDocument(doc);
        }
        writer.close();
    }

    /**
     * Method to get all the texts of the text file.
     * Lucene cannot create the term vetors and tokens for reader class.
     * You have to index its text values to the index.
     * It would be more better if this was in another class.
     * I am lazy you know all.
     * @param f
     * @return
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public String getAllText(File f) throws FileNotFoundException, IOException {
        String textFileContent = "";

        for (String line : Files.readAllLines(Paths.get(f.getAbsolutePath()))) {
            textFileContent += line;
        }
        return textFileContent;
    }
}