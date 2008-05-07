/*
 * This class reads the input file, runs some filter for each read line from 
 * input and the filtered line is than written to the output file.
 */
package cz.webarchiv.webanalyzer.dictionary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
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

                while ((line = br.readLine()) != null) {
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
            } catch (IOException ioe) {
                java.util.logging.Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ioe);
            } finally {
                try {
                    out.close();
                    fis.close();
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * This method reads the input file and adds each line from the file to
     * collection. This collection is then returned. This is convenient to load
     * collection of words from file.
     * 
     * @param inputFile file from which the lines are loaded into collection
     * @return collection of lines from input file
     */
    public Collection<String> loadCollectionFromFile(String inputFile) {
        BufferedReader bufferedReader;
        try {
            FileReader fileReader = new FileReader(inputFile);
            bufferedReader = new BufferedReader(fileReader);
            String line;
            List<String> lines = new ArrayList<String>();
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
            bufferedReader.close();
            fileReader.close();
            return lines;
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }
    
    /**
     * This method writes elements from given collection to file. Each element 
     * is written in one line.
     * 
     * @param collection collection that will be written to file
     * @param outputFile file to which the collection will be written
     */
    public void writeCollectionToFile(Collection<String> collection, 
            String outputFile) {
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        try {
            fileWriter = new FileWriter(outputFile);
            bufferedWriter = new BufferedWriter(fileWriter);
            for(String element : collection) {
                bufferedWriter.write(element);
                bufferedWriter.newLine();
            }
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bufferedWriter.close();
                fileWriter.close();
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * This method filters out all elements from given collection, that occures
     * in file given as second parameter. Filtered collection is returned.
     * 
     * @param collection collection that will be filtered
     * @param file file from which the elements will be loaded to filter out the 
     * same elements that occures in collection
     */
    public Collection<String> filterCollectionByFile(Collection collection, 
            String file) {
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            String line;
            while((line = bufferedReader.readLine()) != null) {
                // edit line to desired form
                if((line = gainRelevantItemFromLine(line)) == null) {
                    continue;
                }
                System.out.println(line);
                // remove edited line from collection if present
                if (collection.contains(line)) {
                    collection.remove(line);
                }
            }
            return Collections.unmodifiableCollection(collection);
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bufferedReader.close();
                fileReader.close();
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
    /**
     * This method edits line given as parameter. You can gain from given line 
     * only relevant information e.g. line="word 123 other unrelevant bullshits"
     * You are only interested in first word. 
     * 
     * @param line line from which only relevant information is gained
     * @return item this is relevant item from line, you're interested in
     */
    public String gainRelevantItemFromLine(String line) {
        String item = null;
        item = (line.split("\\s"))[1];
        if (item.matches(".*[\\W|\\d|-|_].*")) {
            return null;
        }
        return item.toLowerCase();
    }
    
}
