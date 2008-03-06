/*
 * CitiesIndexLoader.java
 *
 * Created on September 9, 2007, 2:43 PM
 *
 * Trieda, ktora bude z indexoveho suboru nacitavat indexu. Bude ich ukladat do 
 * kolekcie v usporiadanej kolekcii ArrayList<Integer>, aby mohla pristupovat
 * pomocou tejto kolekcie k danym prvkom podla pozicie v kolekcii.
 *
 */

package org.archive.analyzer.dictionary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author praso
 */
public class CitiesIndexLoader {
    
    private final Logger log4j = Logger.getLogger(CitiesIndexLoader.class);
    
    /* Jedina instancia tejto triedy. */
    private static final CitiesIndexLoader INSTANCE = new CitiesIndexLoader();
    
    /* Zoznam, kde su ulozene indexy v poradi v akom sa nacitaju z indexu. */
    private List<Integer> indexes = new ArrayList<Integer>();
    
    /* Integer, ktory predstavuje poziciu elementu v zozname indexes. */
    private Integer position;
    
    /* Streamy na citanie zo slovnika. */
    private InputStream inputStream;
    private InputStreamReader inputStreamReader;
    private BufferedReader bufferedReader;
    private String word;
    
    /**
     * Staticka tovarna metoda, ktora vrati odkaz na jedinu instanciu 
     * tejto triedy.
     *
     * @return jedina instancia tejto triedy.
     */
    public static CitiesIndexLoader getInstance() {
        return INSTANCE;
    }
    
    /** 
     * Private constructor creates a new instance of CitiesIndexLoader 
     */
    private CitiesIndexLoader() {
    }
    
    /**
     * Metoda, ktora nainicializuje potrebne streamy pre nacitanie indexoveho
     * suboru. Predany parameter je "cz" alebo "sk" atd.
     *
     * @param language      jazyk, ktorym sa dotvori cesta k spravnemu indexu.
     */
    public void initialize(String language) {
        log4j.debug("initialize language=" + language);
        try {
            File index = new File("_anal/dictionaries/" + language + 
                    "/cities/index_cities.csv");
            inputStream = new FileInputStream(index);
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Integer integer = new Integer(line);
                indexes.add(integer);
            }
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
        } catch (IOException ioe) {
            log4j.error("initialize language=" + language, ioe.getCause());
            ioe.printStackTrace();
        }
    }
    
    /**
     * Metoda, ktora vrati kolekciu indexes ako nemodifikovatelnu kolekciu list.
     *
     * @return indexes nemodifikovatelna kolekcia list.
     */
    public Collection<Integer> getIndexes() {
        return Collections.unmodifiableList(indexes);
    }
    
    /**
     * Metoda, ktora vrati dany prvok zo zoznamu indexes podla indexu predaneho
     * ako parameter. Ak index , ktory predavame ako parameter je vecsi ako 
     * velkost zoznamu indexoc, kta sa vyhodi vynimka, ktora je osetrena v try 
     * catch bloku.
     *
     * @param index     pozicia elementu v zozname.
     * @return element  element na danej pozicii v zozname indexex.
     */
    public Integer getElement(int index) {
        try {
            position = indexes.get(index);
        } catch (IndexOutOfBoundsException ioobe) {
            // Do nothing.
        }
        return position;
    }
    
    /**
     * Metoda, ktora vrati pocet elementov v zozname indexes.
     *
     * @return int      pocet elementov v zozname.
     */
    public int getSize() {
        return indexes.size();
    }
}
