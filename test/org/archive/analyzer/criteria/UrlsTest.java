/*
 * UrlsTest.java
 * JUnit based test
 *
 * Created on August 8, 2007, 9:13 PM
 */

package org.archive.analyzer.criteria;

import java.util.ArrayList;
import junit.framework.*;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.archive.analyzer.lucene.URLFormater;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.FilterIndexReader;
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
public class UrlsTest extends TestCase {
    
    public UrlsTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    /**
     * Test of getInstance method, of class org.archive.analyzer.Urls.
     */
    
    /**
     * Test of search method, of class org.archive.analyzer.Urls.
     */
    public void testSearch() {
        System.out.println("_anal/lucene_index/cz/urls_index");
        
        List links = new ArrayList<String>();
        Urls instance = Urls.getInstance();
        // Metod, predam len retazec napr. "cz","sk", atd. Ta si pomocou neho 
        // dotvori cestu k vytvorenym indexom .
        instance.inicializeLuceneIndex("cz");
        // naformatovane odkazy.
        links.add("http://www.1-2-3-ubytovanie.sk");
        links.add("http://evangeline-lillyhzikcomczech");
        links.add("http://www.20518823438");
        links.add("http://www.geocitiescomwelt_cz");
        links.add("http://www.forumstredoasiatinfoportalphp");
        links.add("fake-odkaz-nenajde-sacz");
        links.add("1954willys.net?aram=hornoskjasldja>?lkajs.kjd");
        
        instance.search(links);
        System.out.println(instance.statisticsInPercent());
        instance.closeLuceneIndexSearcher();
               
    }
    
}
