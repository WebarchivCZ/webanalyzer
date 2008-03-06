/*
 * CitiesTest.java
 * JUnit based test
 *
 * Created on September 9, 2007, 3:54 PM
 */

package org.archive.analyzer.criteria;

import java.util.StringTokenizer;
import junit.framework.*;
import org.archive.analyzer.dictionary.CitiesDictLoader;
import org.archive.analyzer.dictionary.CitiesIndexLoader;

/**
 *
 * @author praso
 */
public class CitiesTest extends TestCase {
    
    public CitiesTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    /**
     * Test of statisticsInPercent method, of class org.archive.analyzer.criteria.Cities.
     */
    public void testSearch() {
        System.out.println("search");

        String text = " mesto brno praha Blava Opava Krumel Olomouc Martin";
        
        CitiesIndexLoader.getInstance().initialize("cz");
        CitiesDictLoader.getInstance().openStreams("cz");
        Cities instance = Cities.getInstance();
        
        StringTokenizer stringTokenizer= new StringTokenizer(text);
        while (stringTokenizer.hasMoreTokens()) {
            instance.search(stringTokenizer.nextToken());
        }
        
        System.out.println(instance.statisticsInPercent());
        CitiesDictLoader.getInstance().closeStreams();
    }    
}
