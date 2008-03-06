/*
 * ForbiddenDictLoader.java
 *
 * Created on August 18, 2007, 3:31 PM
 *
 * Trieda, ktora je podobna ako DictLoader. Pri prehladavani textu
 * analyzovanej stranky sa bude brat slovo po slove a budu sa tieto slova
 * prehladavat v DictLoader aj v ForiddenDictLoader. DictLoader bude hladat
 * slova daneho jazyka, napr. cz a ForbiddenDictLoader bude hladat slova,
 * ktore zvolime ako zakazane a ak sa najde tak odpocita body analyzovanej
 * stranke.
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
public class ForbiddenDictLoader {
    
    private static Logger log4j = Logger.getLogger(ForbiddenDictLoader.class);
    
    /* Jedina instancia tejto triedy. */
    private static final ForbiddenDictLoader INSTANCE = new ForbiddenDictLoader();
    
    /* Streamy na citanie zo slovnika. */
    private InputStream inputStream;
    private InputStreamReader inputStreamReader;
    private BufferedReader bufferedReader;
    String word;
    
    /**
     * Metoda, ktora vrati odkaz na jedinu instanciu tejto triedy,
     *
     * @return   jedina instancia tejto triedy.
     */
    public static ForbiddenDictLoader getInstance() {
        return INSTANCE;
    }
    
    /**
     * Metoda, ktora otvori streamy na citanie zo slovnika. Cestu k suboru si
     * doplni podla predaneho parametru language ("cz", "sk", ...).
     *
     * @param language      jazyk, ktory sa pouzije na otvorenie toho spravneho
     *                      suboru
     *
     */
    public void openStreams(String language) {
        try {
            // TODO napravit nahravanie spravneho slovnika so zakazanymi slovami
            File forbiddenDictFile = new File("_anal/dictionaries/" + language +
                    "/forbidden_dictionary/dict.csv");
            int fileLengthInBytes = (int) forbiddenDictFile.length();
            inputStream = new FileInputStream(forbiddenDictFile);
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            if (!(bufferedReader.markSupported())) {
                log4j.error("stream of ForbiddenDictLoader cannot be marked, " +
                        "forbiddenDictFile=" + forbiddenDictFile);
            }
            bufferedReader.mark(fileLengthInBytes);
        } catch (FileNotFoundException fnfe) {
            log4j.error("openStreams language=" + language, fnfe.getCause());
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            log4j.error("openStrems language=" + language, ioe.getCause());
            ioe.printStackTrace();
        }
    }
    
    /**
     * Metoda na uzavretie streamov.
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
     * Sukromny konstruktor.
     */
    private ForbiddenDictLoader() {
    }
    
    /**
     * Metoda, ktora bude nacitavat slovo podla predaneho paramteru skipBytes,
     * co je pocet bytov, ktrore ma preskocit pri nacitavani slova. V ramci
     * rychlosti programu nevyhadzujem vynimku IllegalArgumentException ak sa
     * preda parameter skipBytes < 0.
     *
     * @param skipBytes     pocet bytov, ktore inputStream preskoci.
     * @return              slovo, ktore sa precitalo zo slovnika na x-tom byte
     */
    public String loadWord(int skipBytes) {
        if (skipBytes < 0)
            throw new IllegalArgumentException("skipBytes is less than 0");
        try {
            bufferedReader.skip(skipBytes);
            word = bufferedReader.readLine();
            bufferedReader.reset();
        } catch (IOException ioe) {
            log4j.error("loadWord skipBytes=" + skipBytes + " , ",
                    ioe.getCause());
            ioe.printStackTrace();
        }
        
        return word;
    }
    
}
