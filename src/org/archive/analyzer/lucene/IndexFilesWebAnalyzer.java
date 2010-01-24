/*
 * IndexFilesWebAnalyzer.java
 *
 * Created on August 12, 2007, 2:40 PM
 *
 * Trieda IndexFilesWebAbalyzer je nahradou za triedu IndexFiles z baliku
 * org.apache.lucene.demo. Bude mat staticku metodu, ktora bude mat jediny
 * ucel, vytvorit indexy pre dane subory a ulozit ich do zvoleneho adresara,
 * z ktoreho sa tieto indexove subory nacitaju a pouziju pri vyhladavani.
 * Hlavna metoda main pozaduje dva vstupne argumenty v poli args.
 * 1. relativna cesta k suborom na indexaciu
 * 2. adresar do ktoreho sa indexy ulozia.
 *
 * Zatial som tuto triedu pouzil iba v testovacej triede CreateIndexTest.
 *
 */

package org.archive.analyzer.lucene;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;


/**
 *
 * @author praso
 */
public class IndexFilesWebAnalyzer {
    
    /**
     * Sukromy konstruktor.
     */
    private IndexFilesWebAnalyzer() {
    }
    
//    private static final File INDEX_DIR = new File("index_webanalyzer");
    
    /**
     * Metoda main. Pozaduje dva vstupne argumenty v poli args. Metoda
     * vytvori indexy do adresara "index_webanalyzer", dalsia cesta z
     * defaultneho adresara je definovana vstupnym argumentom pre umiestnenie
     * indexov.
     * 1. relativna cesta k suborom na indexaciu
     * 2. adresar do ktoreho sa indexy ulozia.
     */
    public static void main(String[] args) {
        String usage = "java org.archive.analyzer.lucene.IndexFilesWebAnalyzer" +
                "1.arg relativna cesta k suborom na indexaciu" +
                "2.arg index_webanalyzer/ + adresar do ktoreho sa indexy ulozia.";
        if (args.length == 0) {
            System.err.println("usage: " + usage);
            System.exit(1);
        }
        
        // adresar so subormi, ktore sa maju indexovat.
        final File docDir = new File(args[0]);
        if (!docDir.exists() || !docDir.canRead()) {
            System.out.println("Document directory '" + docDir.getAbsolutePath() + "'doesn't exists or is not readable, please check the path");
            System.exit(1);
        }
        
        Date start = new Date();
        String resultIndexDir = args[1];
        try {
            IndexWriter writer = new IndexWriter(resultIndexDir, new StandardAnalyzer(), true);
            System.out.println("Indexing to directory '" + resultIndexDir + "'...");
            indexDocs(writer, docDir);
            System.out.println("Optimizing...");
            writer.optimize();
            writer.close();
            
            Date end = new Date();
            System.out.println(end.getTime() - start.getTime() + " total miliseconds");
            
        } catch (IOException ioe) {
            System.out.println(" caught a " + ioe.getClass() +
                    "\n with message: " + ioe.getMessage());
        }
    }
    
    static void indexDocs(IndexWriter writer, File file) throws IOException {
        // do not try to index files that cannot be read
        if (file.canRead()) {
            if (file.canRead()) {
                if (file.isDirectory()) {
                    String[] files = file.list();
                    // an IO error could occur
                    if (files != null) {
                        for (int i = 0; i < files.length; i++) {
                            indexDocs(writer, new File(file, files[i]));
                        }
                    }
                } else {
                    System.out.println("adding " + file);
                    try {
                        writer.addDocument(FileDocument.Document(file));
                    }
                    // at least on windows, some temporary files raise this exception with an "access denied" message
                    // checking if the file can be read doesn't help
                    catch (FileNotFoundException fnfe) {
                        ;
                    }
                }
            }
        }
    }
    
}