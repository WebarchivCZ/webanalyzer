/*
 * IndexLoader.java
 *
 * Created on Sobota, 2007, apr�l 28, 13:44
 *
 * Trieda, ktora bude z indexoveho suboru nacitavat indexy. Bude ich ukladat
 * v usporiadanej kolekcii ArrayList<Integer>, aby mohla pristupovat pomocou
 * tejto kolekcie k danym prvkom podla pozicie v kolekcii ( pozicia je dana
 * riadkom, kde je index umiestneny ). Mal by som potom upravit tuto triedu, tak
 * aby neuchovavala indexy indexoveho suboru v kolekcii( velka spotreba pamete),
 * ale mal by som zo suboru citat len tie riadky, ktore potrebujem ( neviem ci
 * je na to trieda, ktora mi nacita len ten riadok, ktory potrebujem aby
 * nemusela prechadzat vsetky riadky. Trieda je jedinacik, pricom konstruktor
 * nacita obsah suboru a potom stream uzavrie. Pri upravovani na ine jazyky
 * prerobit na finalnu triedu. Inicializovat spravny subor s indexom.
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
 * @author Ivan Vlcek
 */
public class IndexLoader {
    
    private static final Logger log4j = Logger.getLogger(IndexLoader.class);
    
    /* Jedina instancia tejto triedy. */
    private static final IndexLoader INSTANCE = new IndexLoader();
    
    /* Zoznam, kde su ulozene indexy v poradi v akom sa nacitaju z indexu. */
    private List<Integer> indexes = new ArrayList<Integer>();
    
    /* Integer, ktory predstavuje poziciu elementu v zozname indexes */
    private Integer position;
    
    /* Streamy na citanie z indexoveho suboru. */
    private InputStream inputStream;
    private InputStreamReader inputStreamReader;
    private BufferedReader bufferedReader;
    private String word;
    
    /**
     * Metoda, ktora vrati jedinu instanciu tejto triedy.
     *
     * @return jedina instancia tejto triedy,
     */
    public static IndexLoader getInstance() {
        return INSTANCE;
    }
    
    /**
     * Sukromny konstruktor.
     */
    private IndexLoader() {
    }
    
    /**
     * Metoda, ktora nainicializuje potrebne streamy pre nacitanie indexoveho
     * suboru. Predany parameter je "cz" alebo "sk", atd.
     *
     * @param language      jazyk, ktorym sa dotvori cesta k spravnemu indexu
     */
    public void initialize(String language) {
        log4j.debug("initiliaze language=" + language);
        try {
            File index = new File("_anal/dictionaries/" + language +
                    "/dictionary/index.csv");
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
            log4j.error("initialize lanuage=" + language, ioe.getCause());
            ioe.printStackTrace();
        }
    }
    
    /**
     * Metoda vrati kolekciu indexes ako nemodifikovatelnu kolekciu list.
     *
     * @return indexes  nemodifikovatelna kolekcia list.
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
     * @param index      pozicia elementu v zozname.
     * @return element   element na danej pozicii v zozname indexes.
     */
    public Integer getElement(int index) {
        try {
            position = indexes.get(index);
        } catch(IndexOutOfBoundsException iofbe) {
//            log4j.warn("getElement index=" + index, iofbe.getCause());
        }
        return position;
    }
    
    /**
     * Metoda vrati pocet elementov v zozname indexes.
     *
     * @return int      pocet elementov v zozname.
     */
    public int getSize() {
        return indexes.size();
    }
    
    /**
     * Metoda vypise kolekciu indexes na standardny vystup.
     */
    public void writeIndexes() {
        System.out.println("Vypis zoznamu indexes.");
        for(Integer i : indexes) {
            System.out.println(i);
        }
    }
}