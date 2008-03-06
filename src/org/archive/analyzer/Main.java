/*
 * Main.java
 *
 * Created on September 3, 2007, 11:26 PM
 *
 * Trieda Main na spustanie modulu. Na testovanie pomocou profilera.
 *
 */

package org.archive.analyzer;

/**
 *
 * @author praso
 */
public class Main {
    
    /** Creates a new instance of Main */
    public Main() {
    }
    
    public static void main(String[] args) {
        WebAnalyzer webAnalyzer = WebAnalyzer.getInstance();
        
//        String uri2 = "http://en.wikipedia.org/wiki/Web_portal";
//        String uri3 = "http://www.ironmaiden.com";
//        String uri =  "http://www.seznam.cz";
//        String uri4 = "http://www.praso.webzdarma.cz";
//        String uri5 = "http://codex.wordpress.org/Pages";
//        String uri6 = "http://www.is.muni.cz";
//        
//
//        String[] uris = {uri5, uri2, uri3, uri4, uri6, uri };
//        boolean result;
//        for (int i=0; i<uris.length; i++) {
//            result = webAnalyzer.isInScope(uris[i]);
//            System.out.println(uris[i] + " " + result);
//        }
        
        
        
        boolean expResult = false;
//        boolean result = webAnalyzer.isInScope(args[0]);
        boolean result = webAnalyzer.isInScope("http://is.muni.cz");

        // Problemove URL
//        http://zena-in.cz
//        http://zena-in.cz/pocasi/default.asp      //Stranka, kde mi nefungoval download metoda read()
//        http://anoweb.cz              //Stranka, kde mi nefungovalo kodovanie.
//        http://zajazdy.sk/      // nefungovalo kodovanie stranka ma nastavene charset="UTF-8"
//        http://palcobrokers.sk/robots.txt       // robots.txt zamrzol
    }
    
}
