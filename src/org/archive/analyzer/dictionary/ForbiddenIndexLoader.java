/*
 * ForbiddenIndexLoader.java
 *
 * Created on August 18, 2007, 3:31 PM
 *
 * Trieda ktora bude z indexoveho suboru nacitavat indexy. Bude ich ukladat
 * v usporiadanej kolekcii. Obdoba triedy IndexLoader.
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
public class ForbiddenIndexLoader {
    
    private static final Logger log4j = 
            Logger.getLogger(ForbiddenIndexLoader.class);    

    /* Jedina instancia tejto triedy. */
    private static final ForbiddenIndexLoader INSTANCE = 
            new ForbiddenIndexLoader();
    
    /* Zoznam, kde su ulozene indexy v poradi v akom sa nacitaju z indexu. */
    private static List<Integer> indexes = new ArrayList<Integer>();
    
    /* Integer, ktory predstavuje poziciu elementu zozname indexes. */
    private Integer position;
    
    /* Streamy na citanie z indexoveho suboru. */
    private InputStream inputStream;
    private InputStreamReader inputStreamReader;
    private BufferedReader bufferedReader;
    private String word;
    
    /**
     * Metoda, ktora vrati jedinu instanciu tejto triedy.
     * 
     * @return      jedina instancia tejto triedy.
     */
    public static ForbiddenIndexLoader getInstance() {
        return INSTANCE;
    }
    
    /** 
     * Sukromny konstruktor.
     */
    private ForbiddenIndexLoader() {
    }    
    
    /**
     * Metoda, ktora nainicializuje potrebne streamy pre nacitanie indexoveho
     * suboru. Predany parameter je "cz", "sk".
     *
     * @param language   jazyk, ktorym sa dotvori cesta k indexu
     */
    public void initialize(String language) {
        log4j.debug("initializes ForbiddenIndexLoader");
        try {
            File forbiddenIndex = new File("_anal/dictionaries/" + language + 
                    "/forbidden_dictionary/index.csv");
            inputStream = new FileInputStream(forbiddenIndex);
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Integer integer = new Integer(line);
                indexes.add(integer);
            }
            inputStream.close();
        } catch (IOException ioe) {
            log4j.error("openStreams", ioe.getCause());
            ioe.printStackTrace();
        }
    }
    
    /**
     * Metoda vrati kolekciu indexes ako nemodifikovatelnu kolekciu list.
     *
     * @param indexes       nemodifikovatelna kolekcia list.
     */
    public Collection<Integer> getIndexes() {
        return Collections.unmodifiableList(indexes);
    }
    
    /**
     * Metoda vrati dany prvok zoznamu indexes podla indexu predaneho ako 
     * parameter. Ak index, ktory predavame ako parameter je vecsi ako velkost
     * zoznamu indexov, tak sa vyhodi vynimka, ktora je osetrena v try catch
     * bloku.
     *
     * @param index     pozicia elementu v zozname.
     * @return          element na danej pozicii v zozname indexes.
     */
    public Integer getElement(int index) {
        try {
            position = indexes.get(index);
        } catch (IndexOutOfBoundsException iofbe) {
//            log4j.warn("getElement index=" + index, iofbe.getCause());
        }
        
        return position;
    }
    
    /**
     * Metoda, ktora vrati pocet elementov v zozname indexes.
     *
     * @return      pocet elementov v zozname indexes.
     */
    public int getSize() {
        return indexes.size();
    }
    
}
