/*
 * This class is responsible for removing words that occurring in another wordlist.
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
import java.util.HashSet;

/**
 *
 * @author ivlcek
 */
public class WordlistFilter {

    @SuppressWarnings("empty-statement")
    private static HashSet<String> fillSet(HashSet<String> set, String input) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        FileInputStream fis = new FileInputStream(input);
        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
        BufferedReader br = new BufferedReader(isr);
        String line;
        while ((line = br.readLine()) != null) {
            set.add(line);
        }

        br.close();
        isr.close();
        fis.close();

        return set;
    }

    private static void writeToFile(HashSet<String> set, String output) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        FileOutputStream fos = new FileOutputStream(output);
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
        BufferedWriter br = new BufferedWriter(osw);

        for (String line : set) {
            br.write(line);
            br.newLine();
        }

        br.close();
        osw.close();
        fos.close();
    }

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        int i = 1;
        while (i < 23) {
            String filter = "_anal/wordlists/wordlist.cz.ok";
            String filterBy = "_anal/wordlists/w/w/lower" + i + ".gz";
            HashSet<String> filterSet = new HashSet<String>();
            HashSet<String> filterBySet = new HashSet<String>();
            filterBySet = fillSet(filterBySet, filterBy);
            filterSet = fillSet(filterSet, filter);

            for (String item : filterBySet) {
                if (filterSet.remove(item.toLowerCase())) {
                    System.out.println(item);
                }
            }

            writeToFile(filterSet, filter);
            i++;
        }
    }
}
