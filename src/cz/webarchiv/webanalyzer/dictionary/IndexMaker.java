/*
 * This class will create index for defined list of words.
 */

package cz.webarchiv.webanalyzer.dictionary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author praso
 */
public class IndexMaker {
    
    public void run(String dictionary, String index) {
        FileReader fr = null;
        BufferedReader br = null;
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            // read dictionary
            fr = new FileReader(dictionary);
            br = new BufferedReader(fr);            
            // write to index
            fw = new FileWriter(index);
            bw = new BufferedWriter(fw);
            // main function
            String line = null;
            long indexKey = 0l;
            while ((line = br.readLine()) != null) {
                bw.write(Long.toString(indexKey));
                bw.newLine();
                indexKey = indexKey + line.length() + 1;
            }
            // WARNING !!! don't forget to delete the last line in index file.
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(IndexMaker.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ioe) {
            Logger.getLogger(IndexMaker.class.getName()).log(Level.SEVERE, null, ioe);
        } finally {
            try {
                br.close();
                fr.close();
                bw.close();
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(IndexMaker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void main(String[] args) {
        IndexMaker indexMaker = new IndexMaker();
        indexMaker.run("_anal/korpus_cz_2.txt", "_anal/index_korpus_cz.txt");
    }

}
