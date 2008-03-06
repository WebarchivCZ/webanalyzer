/*
 * CreateIndex.java
 *
 * Created on August 5, 2007, 5:27 PM
 *
 * Funkcionalita tejto triedy je presunuta do triedy
 * org.archive.analyzer.lucene.IndexFilesWebAnalyzer, ktoru som si upravil 
 * podla vlastnych potrieb.
 *
 * Trieda, ktrora vytvori index pre LUCENE. Ten sa bude pouzivat na
 * prehladavanie pomocou LUCENE. Neskor dorobit vstupne parametre pre
 * cestu atd. Pred vytvorenim indexu musim subory preformatovat pomocou
 * URLFormater.
 */

package org.archive.test;

import org.apache.lucene.demo.IndexFiles;
import org.apache.lucene.demo.IndexFiles;

/**
 *
 * @author praso
 */
public class CreateIndex {
    
    /** Creates a new instance of CreateIndex */
    public CreateIndex() {
    }
    
    public static void main(String[] args) {
        // Vytvori index zo suborov zadaneho adresara.
        String[] args2 = new String[1];
        args2[0] = "_anal/lucene_odkazy/cz";
        IndexFiles.main(args2);
    }
    
}
