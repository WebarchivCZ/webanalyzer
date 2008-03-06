/*
 * CreateIndexTest.java
 *
 * Created on August 12, 2007, 3:40 PM
 *
 * Testovacia trieda. Testuje tried CreateIndex 
 */

package org.archive.analyzer.criteria;

import junit.framework.TestCase;
import org.archive.analyzer.lucene.IndexFilesWebAnalyzer;

/**
 *
 * @author praso
 */
public class CreateIndexTest extends TestCase {
    
    public CreateIndexTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    // LEN ZATIAL METODA, KTORA SA STARA O VYTVARANIE INDEXOV, PRIDE NIEKDE INDE
    // !!!!!!!!!!!!!!
    public void testCreateIndex() {
        System.out.println("create index");
        
        String[] args = new String[2];
        // umiestnenie suborov, ktore sa maju naindexovat.
        args[0] = "_anal/lucene_odkazy/cz/filtered_dict";
        // Umiestnenie vytvorenych indexov.
        args[1] = "_anal/lucene_index/cz/dict_index";
        // Spustenie main metody na vytvorenie indexov.
        IndexFilesWebAnalyzer.main(args);
    }
    
}
