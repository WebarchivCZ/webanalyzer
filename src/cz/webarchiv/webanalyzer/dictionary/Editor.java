/*
 * This class reads the input file, runs some filter for each read line from 
 * input and the filtered line is than written to the output file.
 */
package cz.webarchiv.webanalyzer.dictionary;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author praso
 */
class Editor {

    private static final Logger log4j = Logger.getLogger(Editor.class);

    /**
     * Main method that reads input file.
     * 
     */
    protected void edit(String input, IFilter iFilter, String output) {
        OutputStreamWriter out = null;
        FileInputStream fis = null;
        {
            try {
                log4j.debug("edit input=" + input + ", iFilter=" + iFilter.getName() + ", output=" + output);
                
                // read file
                fis = new FileInputStream(input);
                InputStreamReader in = new InputStreamReader(fis, "UTF-8");
                BufferedReader br = new BufferedReader(in);
                // write file
                FileOutputStream fos = new FileOutputStream(output);
                out = new OutputStreamWriter(fos, "UTF-8");
                BufferedWriter bw = new BufferedWriter(out);
                
                // reading
                String line;
                String word;
                
                // sort
                Set<Word> sort = new TreeSet<Word>();
                
                while((line = br.readLine()) != null) {
                    // filtering starts here ===================================
//                    String[] parts = line.split("\\s");
//                    word = parts[0].toLowerCase();
//                    if (word.matches(".*\\d.*")) {
//                        continue;
//                    }
//                    if (word.length() < 3) {
//                        continue;
//                    }
//                    
//                    if(word.contains("&")) {  
//                        continue;
//                    }
//                
//                    // writing
//                    bw.write(word);
//                    bw.newLine();
                    // filtering ends here =====================================
                  
                    // DANGER !!!
                    // I use this method for two purposes : filtering and sorting
                    // I always have to comment right block of commands. read the source code carfully
                    // sorting starts here ==================================================
                    Word w = new Word();
                    w.setWord(line);
                    sort.add(w);
                }
                
                // write sort
                for (Word w : sort) {
                    bw.write(w.getWord());
                    bw.newLine();
                }
                // sorting ends here=====================================================
                
            } catch (FileNotFoundException ex) {
                java.util.logging.Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
                java.util.logging.Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ex);
            } catch(IOException ioe) {
                java.util.logging.Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ioe);
            }
                finally {
                try {
                    out.close();
                    fis.close();
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
