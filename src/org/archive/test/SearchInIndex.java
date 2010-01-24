/*
 * SearchInIndex.java
 *
 * Created on August 2, 2007, 9:40 PM
 *
 * Trieda sluzi len ako prve demo ako sa vyhladava pomocou LUCENE.
 * Funkcionalita je pouzita v triedach : Urls, ForbidenUrls
 */

package org.archive.test;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;


/**
 *
 * @author praso
 */
public class SearchInIndex {
    
    private static Logger log4j = Logger.getLogger(SearchInIndex.class);
    
    /** Creates a new instance of SearchInIndex */
    public SearchInIndex() {
    }
    
    public static void main(String[] args) {
        log4j.debug("=========================SearchInIndex.main()===========");
        // Parameter pride zo vstupu args. Nastavim indexovy subor.
        String index = "index";
        // Queries dotazy na vyhladavanie.
        String queryWord = "manifesok";
        // Field neviem este co to znamena.
        String field ="contents";
        try {
            IndexReader reader = IndexReader.open(index);
            Searcher searcher = new IndexSearcher(reader);
            Analyzer analyzer = new StandardAnalyzer();
            
            QueryParser parser = new QueryParser(field, analyzer);
            // Blok potialo sa moze nainicializovat len raz a potom sa len 
            // zadavaju dotazy na hladanie.
            
            // Odstrani z query white spaces.
            queryWord = queryWord.trim();
            
            Query query = parser.parse(queryWord);
            System.out.println("Searching for : " + query.toString(field));
            
            Hits hits = searcher.search(query);
            
            log4j.debug(hits.length() + " total marching documents");
            System.out.println(hits.length() + " total marching documents");
            
            reader.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            log4j.error("PROBLEM PRI SearchInIndex");
        }
        
        
        
    }
    
}
