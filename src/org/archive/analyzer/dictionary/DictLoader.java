/*
 * DictLoader.java
 *
 * Created on Piatok, 2007, apr�l 27, 19:02
 *
 * Trieda, ktora bude zo slovnika nacitavat slova. Slovo nacita preskocenim
 * bytov a nasledneho nacitania pomocou metody readLine(). Instancia tejto
 * triedy by sa mala vytvorit len raz, ( navrhovy vzor jedninacik ) a pouzivat
 * by sa mala jej metoda, ktora nacita dane slovo a vrati ho volanej metode.
 * Mala by potom vzdy resetovat citac na nulu. Aby pri dalsom slove mohla
 * preskocit urcity pocet bytov.
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
 * @author Ivan Vlcek
 */
public class DictLoader {
    
    private static Logger log4j = Logger.getLogger(DictLoader.class);
    
    /* Jednina instancia tejto triedy. */
    private static final DictLoader INSTANCE = new DictLoader();
    
    /* Streamy na citanie zo slovnika. */
    private InputStream inputStream;
    private InputStreamReader inputStreamReader;
    private BufferedReader bufferedReader;
    private String word;
    
    /**
     * Meotda, ktora vrati odkaz na jedinu instanciu tejto triedy.
     *
     * @ return jedina instancia tejto triedy.
     */
    public static DictLoader getInstance() {
        return INSTANCE;
    }
    
//    /**
//     * Metoda, ktora otvori streamy na citanie zo slovnika.
//     */
//    void openStreams() {
//        try {
////            File dictFile = new File(getClass().getResource("filteredCzech.csv").toString());
//
//            // Dlzka bytov, ktore moze nacitat vstupny prud tj. velkost suboru.
//            int startMark = 300000;
////        int startMark = (int)dictFile.length();
////            System.out.println("URL zdroja ==========================================");
////            System.out.println(getClass().getResource("filteredCzech.csv").toString());
//            is = getClass().getResourceAsStream("filteredCzech.csv");
//            isr = new InputStreamReader(is);
//            br = new BufferedReader(isr);
//            // Kontrola markovania.
//            if (!(br.markSupported()))
//                System.err.println("Stream DictLoaderu nieje znackovatelny");
//            // Nastavenie marku na zaciatok streamu.
//            br.mark(startMark);
//        } catch (IOException ioe) {
//            log4j.error("openStreams " + ioe.getMessage());
//            ioe.printStackTrace();
//        }
//    }
    
    /**
     * Metoda, ktora otvori streamy na citanie zo slovnika. Cestu k suboru si
     * doplni podla predaneho parametru language napr. "cz", "sk"...
     *
     * @param language      jazyk, ktory sa pouzije na otvorenie toho spravneho
     *                      slovnika
     */
    public void openStreams(String language) {
        try {
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
            log4j.error("openStream language=" + language, fnfe.getCause());
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            log4j.error("openStreams language=" + language, ioe.getCause());
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
     * Mal by som vyvorit jedninacika. Predam mu subor, v ktorom bude
     * vyhladavat slova. Maxpocet Bytov, ktore moze prud nacitat, inak vyhodi
     * vynimku invalidMark pri resetovani streamu. Je to
     * vlastne velkost suboru slovnika.
     *
     * ??? toto uz netreba riesit pretoze nahravam subory priamo a nie z .sjaru
     *
     * @param dictFile      subor v ktorom je slovnik.
     */
    private DictLoader() {
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
    public String loadWord(int skipBytes) {
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
            ioe.printStackTrace();
        }
        
        return word;
    }
    
}
