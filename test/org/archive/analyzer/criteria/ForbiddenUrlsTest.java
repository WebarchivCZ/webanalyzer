/*
 * ForbiddenUrlsTest.java
 * JUnit based test
 *
 * Created on August 13, 2007, 6:28 PM
 */

package org.archive.analyzer.criteria;

import java.util.ArrayList;
import junit.framework.*;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
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
public class ForbiddenUrlsTest extends TestCase {
    
    public ForbiddenUrlsTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    /**
     * Test of search method, of class org.archive.analyzer.ForbiddenUrls.
     */
    public void testSearch() {
        System.out.println("search");
        
        List links = new ArrayList();
        ForbiddenUrls instance = ForbiddenUrls.getInstance();
        instance.inicializeLuceneIndex("cz");
        
        links.add("www.porno.cz");
        links.add("porncom");
        links.add("porn_stockings"); //ee
        links.add("eroticcom");
        links.add("porn-stockingscom");
        links.add("porncom");
        links.add("pornvideoscom");
        links.add("sexcom");
        
        instance.search(links);
        System.out.println(instance.statisticsInPercent());
        instance.closeLuceneIndexSearcher();
    }
    
}
