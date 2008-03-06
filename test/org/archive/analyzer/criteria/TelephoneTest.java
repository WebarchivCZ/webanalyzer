/*
 * TelephoneTest.java
 * JUnit based test
 *
 * Created on August 29, 2007, 11:19 PM
 */

package org.archive.analyzer.criteria;

import junit.framework.*;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author praso
 */
public class TelephoneTest extends TestCase {
    
    public TelephoneTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public void testSearch() {
        System.out.println("search");
        
        String line = "telefon 776 920 666 ," +
                " +420 774 920 666 , " +
                "00420 772 777 777 ," +
                "00 123 123 123 123 ," +
                "123456789101010 123123123123123";
        Telephone instance = Telephone.getInstance();
        instance.search(line);
        System.out.println(instance.statisticsInPercent());
        
    }
}
