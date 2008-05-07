/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.webarchiv.webanalyzer.multithread.analyzer.util;

import cz.webarchiv.webanalyzer.multithread.analyzer.util.FilterText;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;

/**
 *
 * @author praso
 */
public class FilterTextTest extends TestCase {
    
    public FilterTextTest(String testName) {
        super(testName);
    }            

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getFilteredText method, of class FilterText.
     */
    public void testGetFilteredText() {
        System.out.println("getFilteredText");
        String nonfiltered = "";
        FilterText instance = null;
        String expResult = "";
        String result = instance.getFilteredText(nonfiltered);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of withoutWhiteSpaces method, of class FilterText.
     */
    public void testWithoutWhiteSpaces() {
        System.out.println("withoutWhiteSpaces");
        String text = "";
        FilterText instance = null;
        String expResult = "";
        String result = instance.withoutWhiteSpaces(text);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeTags method, of class FilterText.
     */
    public void testRemoveTags() {
        System.out.println("removeTags");
//        String text = "ahoj <tag sdf=sdf , sdf=df sdf=kjdsf>toto chcem</tote nie>toto ano";
        String text = readFile();
        FilterText instance = FilterText.getInstance();
        String expResult = "";
        String result = instance.removeTags(text);
        System.out.println(result);
//        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }
    
    private static String readFile() {
        FileReader fr = null;
        try {
            fr = new FileReader("_anal/testURI.txt");
            BufferedReader br = new BufferedReader(fr);
            String line = null;
            StringBuffer sb = new StringBuffer();
            while((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException ex) {
            Logger.getLogger(FilterTextTest.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fr.close();
            } catch (IOException ex) {
                Logger.getLogger(FilterTextTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

}
