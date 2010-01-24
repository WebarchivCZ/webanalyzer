/*
 * This class is responsible for filtering the original source with wordlist
 * included in it. It creates wordlist of certain language, but it may contain
 * very short words, for example, the lenght of words is less or equal 3. It 
 * can also contain duplicate words that has to be removed from wordlist.
 */

package cz.webarchiv.webanalyzer.dictionary.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import org.apache.log4j.Logger;

/**
 *
 * @author ivlcek
 */
public class Filter {
    
    private static final Logger log4j = Logger.getLogger(Filter.class);
    
    /**
     * Constructor
     */
    public Filter() {
        // nothing
    }
    
    /**
     * Method to filter out irrelevant data from input source
     * 
     * @param name of the input file
     * @return output file
     */
    private void filter(String input, String output) {
        log4j.debug("filter input=" + input + ", output=" + output);
        log4j.debug("encoding=" + System.getProperty("file.encoding"));
        try {
            // read input file
            FileInputStream fis = new FileInputStream(input);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
//            InputStreamReader isr = new InputStreamReader(fis, "ISO-8859-2"); // cz wordlist is ISO-8859-2
            BufferedReader br = new BufferedReader(isr);
            // write to output file
            FileOutputStream fos = new FileOutputStream(output);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
    
            String line;
            String word;
            
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s");
                for (int i = 0; i<parts.length; i++) {
                word = parts[i].toLowerCase(); 
                while (word.matches("\\W.*")) {
                    word = word.substring(1);
                }
                while (word.matches("[^\\W]+\\W")) {
                    word = word.substring(0, word.length() - 1);
                }
                
                // set index of array
//                word = parts[0].toLowerCase();

                // this is for wordlist.en
//                if (word.equals("@")) {
//                    word = parts[3].toLowerCase();
//                }
                
                if (word.length() <= 3) {
                    log4j.info("discarding word=" + word);
                    continue;
                }                
                
                // sk abeceda  aáäbcčdďeéfghiíjklĺľmnňoóôpqrŕsštťuúvwxyýzž
                // cz abeceda  aábcčdďeéěfghiíjklmnňoópqrřsštťuúůvwxyýzž
                // en alpahbet abcdefghijklmnopqrstuvwxyz
                if (word.matches(".*[^abcdefghijklmnopqrstuvwxyz-].*")) {
                    log4j.info("discarding word=" + word);
                    continue;
                }
                
                if(word.matches(".*([\\w])\\1\\1.*")) {
                    log4j.info("discarding word=" + word);
                    continue;
                }
                
                bw.write(word);
                bw.newLine();
            }
            }
            
            bw.flush();
            bw.close();
            br.close();
            
        } catch (FileNotFoundException ex) {
            log4j.error("filter", ex.getCause());
        } catch (UnsupportedEncodingException uee) {
            log4j.error("filter", uee.getCause());
        } catch (IOException ioe) {
            log4j.error("filter", ioe.getCause());
        }
    }
    
    /**
     * Main
     * 
     */
    public static void main(String[] args) {
        Filter filter = new Filter();
        String input = "_anal/wordlists/htmlcolor";
        filter.filter(input, input + ".f");
    }

}
