/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.webarchiv.webanalyzer.multithread.testobj;

import cz.webarchiv.webanalyzer.multithread.WebAnalyzer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author praso
 */
public class TestOneThreadWebAnalyzer {
    
    public static void main(String[] args) {
        String urlName = "http://cs.wikipedia.org/wiki/Portál:Obsah";
        String urlContent = readFile();
        Set<String> urlOutlinks = new HashSet<String>();
        String contentType = "";
        
        try {
            WebAnalyzer.getInstance().initialize();
            WebAnalyzer.getInstance().run(urlName, urlContent, urlOutlinks, contentType);
            WebAnalyzer.getInstance().close();
        } catch (Exception ex) {
            Logger.getLogger(TestOneThreadWebAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            Logger.getLogger(TestOneThreadWebAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fr.close();
            } catch (IOException ex) {
                Logger.getLogger(TestOneThreadWebAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
}
