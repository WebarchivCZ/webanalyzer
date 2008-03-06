/*
 * FilterTextTest.java
 * JUnit based test
 *
 * Created on September 2, 2007, 11:44 PM
 */

package org.archive.analyzer;

import junit.framework.*;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;

/**
 *
 * @author praso
 */
public class FilterTextTest extends TestCase {
    
    public FilterTextTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    /**
     * Test of getFilteredText method, of class org.archive.analyzer.FilterText.
     */
    public void testGetFilteredText() {
        System.out.println("getFilteredText");
        
        String nonfiltered =
                "qwe qwe1 qwe0 qwe- qwe_ qwe qwe 1qwe " +
                "qwe+ qweľ qweš qweč qweť qwež qweý qweá qweí qweé qwe- qweú " +
                "qweä qweň qweô qweř qweě qweď qweĺ qweů qwech &qwe &nbsp " +
                "alllsd.adsd lkjsd.s.s...sdasdas._009ad.80sdsad nbsp";
        

        FilterText instance = FilterText.getInstance();
        
        String expResult = "";
        String result = instance.getFilteredText(nonfiltered);
        System.out.println(result);
        
        String expected = "qwe qwe- qwe qwe qweľ qweš qweč qweť qwež qweý qweá qweí qweé qwe- qweú qweä qweň qweô qweř qweě qweď qweĺ qweů qwech ";
    }
    
}
