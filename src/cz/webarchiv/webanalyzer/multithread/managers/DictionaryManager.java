/*
 * Manager that initializes all needed streams, files, and so on...
 */
package cz.webarchiv.webanalyzer.multithread.managers;

import cz.webarchiv.webanalyzer.multithread.WebAnalyzerProperties;
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
public class DictionaryManager {

    private static final Logger log4j =
            Logger.getLogger(DictionaryManager.class);
    private static final DictionaryManager INSTANCE = new DictionaryManager();
    
    /* Property set according to the property file. */
    private String language = WebAnalyzerProperties.getInstance().
            getDictionaryManagerLanguage();
    
    private InputStream inputStream;
    private InputStreamReader inputStreamReader;
    private BufferedReader bufferedReader;
    private String word;

    public static DictionaryManager getInstance() {
        return INSTANCE;
    }

    /**
     * Singleton.
     */
    private DictionaryManager() {
    // 
    }

    /**
     * Method that initializes all needed files and stream, usually stored in
     * _anal direcotry.
     */
    public void initDictionaryManager() throws Exception {
        try {
            log4j.debug("initDicitionaryManager");
            File dictFile = new File("_anal/dictionaries/" + language +
                    "/dictionary/dict.csv");
            int fileLengthInBytes = (int) dictFile.length();
            inputStream = new FileInputStream(dictFile);
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            if (!(bufferedReader.markSupported())) {
                log4j.error("stream of DictLoader cannot be marked, " +
                        "dictFile=" + dictFile);
            }
            bufferedReader.mark(fileLengthInBytes);
        } catch (FileNotFoundException fnfe) {
            log4j.error("initDictionaryManager language=" + language, fnfe.getCause());
            throw new FileNotFoundException("initDictionaryManager, file not found");
        } catch (IOException ioe) {
            log4j.error("initDictionaryManager language=" + language, ioe.getCause());
            throw new IOException("initDictionaryManager I/O exception");
        }
    }

    /**
     * Method, that closes all used files and streams.
     */
    public void closeDictionaryManager() throws Exception {
        try {
            log4j.debug("closeDictionaryManager");
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
        } catch (IOException ioe) {
            log4j.error("closeDictionaryManager can't close streams");
            throw new IOException("closeDictionaryManager");
        }
    }
    
    /**
     * Metoda, ktora bude nacitavat slovo podla predaneho parametru skipBytes,
     * co je pocet bytov, ktore ma preskocit pri nacitavani slova. V ramci
     * rychlosti programu nevyhadzujem vynimku IllegalArgumentException ak sa
     * preda parameter skipBytes < 0.
     *
     * @param skipBytes     pocet bytov, ktore inputstream preskoci.
     * @return word         slovo, ktore sa precitalo zo slovnika na x-tom byte.
     */
    public synchronized String loadWord(int skipBytes) {
        if (skipBytes < 0)
            throw new IllegalArgumentException("skipBytes is less than 0");
        try {
            bufferedReader.skip(skipBytes);
            // Moze nacitat null ak vyhodi readLine null. Osetri.
            word = bufferedReader.readLine();
            bufferedReader.reset();
        } catch (IOException ioe) {
            log4j.error("loadWord skipBytes=" + skipBytes + ", " +
                    ioe.getMessage());
        }
        return word;
    }
}
