/*
 * CitiesDictLoader.java
 *
 * Created on September 9, 2007, 1:29 PM
 *
 * Trieda, ktora si nahra obsah slovnika, ktory obsahuje nazvy ceskych miest
 * Vyhladavanie bude pomocou binarneho vyhladavania podobne ako pri 
 * Words a ForbiddenWords. Instanciu tejto triedy by mala pouzivat metoda, ktora
 * nacita dane slovo (nazov mesta) a vrati ho volanej metode. Mala by potom 
 * restartovat citac na nulu, aby pri nacitani dalsieho slova mohla preskocit
 * urcity pocet bytov, ktory sa ziska pomocou indexu nahrateho v pameti v jvm.
 *
 */

package org.archive.analyzer.dictionary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.log4j.Logger;

/**
 *
 * @author praso
 */
public class CitiesDictLoader {
    
    public final Logger log4j = Logger.getLogger(CitiesDictLoader.class);
    
    /* Jedina instancia tejto triedy. */
    private static final CitiesDictLoader INSTANCE = new CitiesDictLoader();
    
    /**
     * Staticka tovarna metoda, ktora ziska odkaz na jedinu instanciu tejto 
     * triedy.
     *
     * @return instance     jedina instancia tejto triedy.
     */
    public static CitiesDictLoader getInstance() {
        return INSTANCE;
    }
    
    /* Streamy na citanie zo slovnika. */
    private InputStream inputStream;
    private InputStreamReader inputStreamReader;
    private BufferedReader bufferedReader;
    private String city;
    
    /** 
     * Private constructor creates a new instance of CitiesDictLoader 
     */
    private CitiesDictLoader() {
    }
    
    /**
     * Metoda, ktora otvori stramy na cintanie zo slovnika. Cestu k suboru si 
     * doplni podl apredaneho parametru language napr. "cz" , "sk" ...
     *
     * @param language      jazyk, ktory sa pouzije na otvorenie toho spravneho
     *                      slovnika.
     */
    public void openStreams(String language) {
        try {
            File dictCitiesFile = new File("_anal/dictionaries/" + language +
                    "/cities/dict_cities.csv");
            int fileLengthInBytes = (int) dictCitiesFile.length();
            inputStream = new FileInputStream(dictCitiesFile);
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            if (!bufferedReader.markSupported()) {
                log4j.error("stream of CitiesDictLoader cannot be marked, " +
                        "dictCitiesFile=" + dictCitiesFile);
            }
            bufferedReader.mark(fileLengthInBytes);
        } catch (FileNotFoundException fnfe) {
            log4j.error("openStream language=" + language, fnfe.getCause());
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            log4j.error("openStreams language=" + language, ioe.getCause());
            ioe.printStackTrace();
        }
    }
    
    /**
     * Metoda, ktora uzavrie streamy.
     */
    public void closeStreams() {
        try {
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
        } catch (IOException ioe) {
            log4j.error("closeStreams", ioe.getCause());
            ioe.printStackTrace();
        }
    }
    
    /**
     * Metoda, ktora bude nacitavat slovo podla predaneho parametru skipBytes,
     * co je pocet bytov, ktore ma preskocit pri nacitanvani slova.
     *
     * @param skipBytes     pocet bytov, ktore inputstream preskoci.
     * @return city         mest, ktore sa precitalo zo slovnika na x-tom byte.
     */
    public String loadWord(int skipBytes) {
        if (skipBytes < 0) 
            throw new IllegalArgumentException("skipBytes is less than 0");
        try {
            bufferedReader.skip(skipBytes);
            // Moze nacitat null ak vyhodi readLine null.
            city = bufferedReader.readLine();
            bufferedReader.reset();
        } catch (IOException ioe) {
            log4j.error("loadWord skipBytes=" + skipBytes + " , " + 
                    ioe.getMessage());
            ioe.printStackTrace();
        }
        return city;
    }
    
}
