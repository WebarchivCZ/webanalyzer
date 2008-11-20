/*
 * This class is responsible for removing duplicate items in filtered wordlist
 */
package cz.webarchiv.webanalyzer.dictionary.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.TreeSet;

/**
 *
 * @author ivlcek
 */
public class DuplicateChecker {

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        String input = "_anal/wordlists/wordlist.cz.ok";
        String output = "_anal/wordlists/wordlist.cz.ok";
        TreeSet<String> words = new TreeSet<String>();
        FileInputStream fileReader = new FileInputStream(input);
        InputStreamReader isr = new InputStreamReader(fileReader, "UTF-8");
        BufferedReader br = new BufferedReader(isr);

        String line;
        while ((line = br.readLine()) != null) {
            words.add(line);
        }

        br.close();
        isr.close();
        fileReader.close();

        FileOutputStream fos = new FileOutputStream(output);
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
        BufferedWriter bw = new BufferedWriter(osw);

        for (String w : words) {
            bw.write(w);
            bw.newLine();
        }

        bw.close();
        osw.close();
        fos.close();

    }
}
