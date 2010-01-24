/*
 * URLFormater.java
 *
 * Created on August 5, 2007, 2:04 PM
 *
 * Trieda, ktora prefotrmatuje subor s odkazmi (URL napr. zo seznam.cz").
 * Odkazy sa preformatuju do tvaru, v ktorom sa budu lepsie vyhladavat pomocou
 * indexu LUCENE. Odstrani sa prefix "http", odstrania sa lomitka v URL,
 * odstranie sa bodky (napr: ahoj.html/images/bla.jpg) sa preformatuje
 * na tvar ahojhtmlimagesblajpg. Odstani sa aj prefix www.
 * Prehladavanie by malo byt rychlejsie.
 * Budu sa ignorovat odkazy, ktore oskazuju na domenu.cz, tie budem
 * kontrolovat v inej triede.
 *
 *
 */

package org.archive.analyzer.lucene;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import org.archive.analyzer.dictionary.Dictionary;

/**
 *
 * @author praso
 */
public class URLFormater {
   
    /* Url v retazci. */
    private static String urlString;
    
    /* Index otaznika ? v metode formatUrlForLucene. */
    private static int indexOfQuestionMark;
    
    /* Index dvojbodky : v metode formatUrlForLucene. */
    private static int indexOfDoubleDot;
    
    /** Creates a new instance of URLFormater */
    public URLFormater() {
    }

    /**
     * Metoda main, prerobit na vstupne argumenty, (vstupny subor, vystupny
     * prefiltrovany subor).
     */
    public static void main(String[] args) {
        // Subor s odkazmi, ktory sa ma upravit.
        try {
            // Input
            String fileName = "_anal/lucene_odkazy/cz/non_filtered_forbiden_urls/porn.cz";
            File file = new File(fileName);
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(isr);
            
            // Output
            String outputFile = "_anal/lucene_odkazy/cz/filtered_forbiden_urls/porn.cz";
            File filteredFile = new File(outputFile);
            FileOutputStream fileOutputStream = new FileOutputStream(filteredFile);
            OutputStreamWriter osw = new OutputStreamWriter(fileOutputStream);
            BufferedWriter bufferedWriter = new BufferedWriter(osw);
            
            // Odstranenie duplicitnych odkazov pomocou instancie Dictionary, 
            // ktora pomocou TreeSetu zoradi url a odstrani duplicity.
            Dictionary dictionary = new Dictionary();
            
            String line = null;
            int i = 0;
            while ((line = bufferedReader.readLine()) != null) {
                if (!line.contains(".cz")) {
                    // upravit a zapisat do noveho suboru.
                    line = formatURLforLucene(line);
                    i++;
                    dictionary.addWord(line);
                    
                    System.out.println(i + ". " + line);
                }
            }
            
            // Odstranit duplicity.
            for (String url : dictionary.getWords()) {
                bufferedWriter.write(url);
                bufferedWriter.newLine();
            }
            
            // input streams
            bufferedReader.close();
            isr.close();
            fileInputStream.close();
            // output streams
            bufferedWriter.close();
            osw.close();
            fileOutputStream.close();
            
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        
    }
    
    /**
     * Staticka metoda, ktora preformatuje dany link, predany ako retazec do 
     * podoby potrebnej pre vyhladavanie v Indexoch pomocou LUCENE. Odstrania
     * sa nepotrebne znaky ako su : lomitko, bodka a prefixy "http://" a 
     * "www", dvojbodka, ? otazniky aj s parametrami URL. Ponecha sa len 
     * "cisty" HOST.
     *
     * @param urlLink   link, ktory sa preformatuje na vyhladadavanie s LUCENE.
     * @return formatedLink   preformatovany link pre vyhladadavanie s LUCENE.
     */
    public static String formatURLforLucene(String urlLink) {
        // TODO odstranit pomlcku ak je na zaciatku retazcka napr. "-ccom",
        // takto ju LUCENE nenajde lebo ju ignoruje, prekontrolovat ci ignoruje
        // aj nejake ine znaky.
        urlLink = urlLink.replaceAll("http:\\/\\/|www|\\/|\\.", "");
        if (urlLink.startsWith("-")) {
            urlLink = urlLink.substring(1);
        }
        indexOfQuestionMark = urlLink.indexOf('?');
        if (indexOfQuestionMark != -1) {
            urlLink = urlLink.substring(0, indexOfQuestionMark);
        }
        indexOfDoubleDot = urlLink.indexOf(':');
        if (indexOfDoubleDot != -1) {
            urlLink = urlLink.substring(0, indexOfDoubleDot);
        }
        return urlLink;
    }
    
}
