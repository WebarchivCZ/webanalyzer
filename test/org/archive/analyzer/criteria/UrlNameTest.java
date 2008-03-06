/*
 * UrlNameTest.java
 * JUnit based test
 *
 * Created on July 22, 2007, 8:51 AM
 */

package org.archive.analyzer.criteria;

import java.net.MalformedURLException;
import junit.framework.*;
import java.net.URL;

/**
 *
 * @author praso
 */
public class UrlNameTest extends TestCase {

    public UrlNameTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    /**
     * Test of search method, of class org.archive.analyzer.UrlName.
     */
    public void testSearch() throws MalformedURLException {
        System.out.println("search");   
        
        UrlName urlName = UrlName.getInstance();
        
        urlName.openStreams();
        URL url = new URL("http://www.AHOJ.cz/cesta/brno.jpg");        
        
        urlName.search(url);
        System.out.println(urlName.statisticsInPercent());
        
        urlName.closeStreams();
        
    }
    
}
