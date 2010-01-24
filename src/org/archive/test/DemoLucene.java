/*
 * DemoLucene.java
 *
 * Created on July 31, 2007, 7:59 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.archive.test;

//import org.apache.lucene.


import org.apache.lucene.demo.*;

/**
 *
 * @author praso
 */
public class DemoLucene {
    
    /** Creates a new instance of DemoLucene */
    public DemoLucene() {
    }
    
    public static void indexFile(String[] args) {
        IndexFiles.main(args);
        
    }
    
    public static void search(String[] args) {
        try {
            SearchFiles.main(args);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        String []args2 = new String[1];
        args2[0] = "/home/praso/devel/projects/netbeans/WebAnalyzer/data";
        indexFile(args2);
        String[] pole = new String[2];
        pole[0] = "-index /home/praso/devel/projects/netbeans/WebAnalyzer/index";
        pole[1] = "-queries http://matheasweb.ic.cz/";
        search(pole);
    }
    
}
