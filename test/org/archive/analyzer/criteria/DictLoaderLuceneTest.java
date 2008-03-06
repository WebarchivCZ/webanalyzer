/*
 * DictLoaderLuceneTest.java
 *
 * Created on August 17, 2007, 12:22 AM
 *
 * Tato trieda sluzila len ako testovanie vyhladavania v slovniku pomocou
 * LUCENE. Problemy boli s diakritikou.
 */

package org.archive.analyzer.criteria;

import java.util.ArrayList;
import junit.framework.*;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.archive.analyzer.dictionary.DictLoaderLucene;
import org.archive.analyzer.lucene.URLFormater;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Hits;

/**
 *
 * @author praso
 */
public class DictLoaderLuceneTest extends TestCase {
    
    /**
     * Creates a new instance of DictLoaderLuceneTest
     */
    public DictLoaderLuceneTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    /**
     * Test of search method, of class org.archive.analyzer.DictLoaderLucene.
     */
    public void testSearch() {
        System.out.println("search");
        
        String text = "dovednost dovézt abandonovat abdominální mahátma " +
                "slučitelný zakašlání zrušitelný žďár žvýkačka";
        
//        Lucene niektore slova nenajde, najme tie s diakritikou, dorobit text 
//                na hladanie v povodnom dictloadere
        
        DictLoaderLucene instance = DictLoaderLucene.getInstance();
        instance.inicializeLuceneIndex("cz");
        instance.search(text);
        instance.closeLuceneIndexSearcher();
    }
    
}
