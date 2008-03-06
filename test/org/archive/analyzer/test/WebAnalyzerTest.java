/*
 * WebAnalyzerTest.java
 * JUnit based test
 *
 * Created on Streda, 2007, j�n 13, 22:57
 */

package org.archive.analyzer.test;

import junit.framework.*;
import org.archive.analyzer.*;

/**
 *
 * @author Ivan Vlcek
 */
public class WebAnalyzerTest extends TestCase {
    
    public WebAnalyzerTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    /**
     * Test of isInScope method, of class org.archive.analyzer.WebAnalyzer.
     */
    public void testIsInScope() {
        System.out.println("testIsInScope...");
        
       WebAnalyzer webAnalyzer = WebAnalyzer.getInstance();          
        
//        String uri2 = "http://ticketpro.cz";
//        String uri3 = "http://www.ironmaiden.com";
//        String uri =  "http://www.altas.sk";
//        String uri4 = "http://e-broker.cz";
//        String uri5 = "http://praso.webzdarma.cz/";
//        String uri6 = "http://is.muni.cz";
//        
//        String[] uris = {uri5, uri2, uri3, uri4, uri6, uri };
//        boolean result;
//        for (int i=0; i<uris.length; i++) {
//            result = webAnalyzer.isInScope(uris[i]);
//            System.out.println(uris[i] + " " + result);
//        }
       
       
       
        boolean expResult = false;
        boolean result = webAnalyzer.isInScope("http://www.seznam.cz");
       
       
       
       
//        System.out.println(result + "http://praso.webzdarma.cz");
//        result = webAnalyzer.isInScope("http://atlas.sk");
//        System.out.println(result + "http://atlas.sk");
//        result = webAnalyzer.isInScope("http://www.hcc.cz");
//        System.out.println(result + "http://www.hcc.cz");
//        assertEquals(expResult, result);
        
    }
    
}
