/*
 * ForbiddenUrlNameTest.java
 * JUnit based test
 *
 * Created on August 20, 2007, 8:22 PM
 */

package org.archive.analyzer.criteria;

import java.net.MalformedURLException;
import junit.framework.*;
import java.net.URL;

/**
 *
 * @author praso
 */
public class ForbiddenUrlNameTest extends TestCase {
    
    public ForbiddenUrlNameTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    /**
     * Test of search method, of class org.archive.analyzer.ForbiddenUrlName.
     */
    public void testSearch() throws MalformedURLException {
        System.out.println("search");
        
        URL url = new URL("http://www.AHOJ.cz/cesta/brno.jpg");
        ForbiddenUrlName instance = new ForbiddenUrlName();
        instance.openStreams();
        
        instance.search(url);
        System.out.println(instance.statisticsInPercent());
        
        instance.closeStreams();
        
    }
}
