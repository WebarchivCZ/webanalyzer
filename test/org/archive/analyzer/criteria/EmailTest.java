/*
 * EmailTest.java
 * JUnit based test
 *
 * Created on August 30, 2007, 5:46 PM
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
public class EmailTest extends TestCase {
    
    public EmailTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    /**
     * Test of search method, of class org.archive.analyzer.criteria.Email.
     */
    public void testSearch() {
        System.out.println("search");
        
        String line = "Emaily," +
                "email@prvy.cz asd@asd.asd, lkajsdfs@aldjadsf.com,asdf@adsf.cz";
        Email instance = Email.getInstance();
        
        instance.search(line);
        System.out.println(instance.statisticsInPercent());
    }    
}
