/*
 * FilterText.java
 *
 * Created on September 2, 2007, 7:02 PM
 *
 * Trieda, ktora bude filtrovat text podla toho aky jayzk sme zvolili.
 * Bude to jedinaci, ktory bude mat staticku metodu, ktora vytvori pole
 * bajtov z abecedy, ktora bude predstavovat vsetky pismena, ktore mozu
 * tvorit slova vo vsetkych jazykoch. To je preto aby sa pri pocitani slov
 * pocitali len skutocne slova z roznych jazykov a v nich sa budu vyhladavat
 * slova zadaneho jazyka, napr. "cz". Podla toho sa vypocita uspesnost v
 * percentach. Odfitrluje aj entity ako je nbsp atd.
 */

package cz.webarchiv.webanalyzer.multithread.analyzer.util;

import org.archive.analyzer.*;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

/**
 *
 * @author praso
 */
public class FilterText {
    
    private final Logger log4j = Logger.getLogger(FilterText.class);
    
    /* Platne pismena pre rozne jazyky, aj znak pred znakovymi entitami. */
    private final String LETTERS =
            "aábcčdďeéěfghiíjklmnňoópqrřsštťuúůvwxyýzžĺľäô";
    
    /* Znakove entity, nepovazuju sa za slova. */
    private String[] ENTITIES = {
        "nbsp"
    };
    
    /* Jedina instancia tejto triedy. */
    private static final FilterText INSTANCE = new FilterText();
    
    /* List bajtov, ktory sa nastavi pri vytvarani instancie tejto instancie. */
    private List listOfBytes = new ArrayList();
    
    /* List znakovych entit, ktory sa nastavi pri vytvarani tejto instancie. */
    private List listOfEntities = new ArrayList();
    
    /**
     * Staticka tovarna metoda, ktora vrati odkaz na jedinu instanciu tejto
     * triedy.
     *
     * @return      jedina instancia tejto triedy.
     */
    public static FilterText getInstance() {
        return INSTANCE;
    }
    

    /**
     * Metoda, ktora naplni list validnych pismen.
     */
    private void fillListOfBytes() {
        try {
            for (int i = 0; i < LETTERS.getBytes("utf-8").length; i++) {
                listOfBytes.add(LETTERS.getBytes("utf-8")[i]);
            }
        } catch (UnsupportedEncodingException ex) {
            log4j.error("FilterText set List of Bytes");
            ex.printStackTrace();
        }
    }
    
    /**
     * Metoda, ktora naplni list znakovych entit, ktore sa napovazuju za 
     * validne slova.
     */
    private void fillListOfEntities() {
        for (int i = 0; i < ENTITIES.length; i++) {
            listOfEntities.add(ENTITIES[i]);
        }
    }
    
    /**
     * Sukromny konstruktor, nastavi list bajtov, ktory predstavuje vsetky
     * platne pismena, roznych cudzich jazykov. Dalej nastavi entity, ktore
     * sa nepovauju za validne slova.
     */
    private FilterText() {
        fillListOfBytes();
        fillListOfEntities();
    }
    
    /**
     * Metoda, ktora prefiltruje predany neprefiltrovany text. Prefiltrovany
     * text bude sluzit na vyhladavanie v slovnikoch. Vrati prefiltrovany
     * text alebo null, ak dojde k vynimke UnsupportedEncodingException.
     *
     * @param nonfiltered       neprefiltrovany text
     * @return prefiltrovany text
     */
    public String getFilteredText(String nonfiltered) {
        try {
            StringBuffer stringBuffer = new StringBuffer();
            StringTokenizer stringTokenizer = 
                    new StringTokenizer(nonfiltered.toLowerCase());
            String word;
            while (stringTokenizer.hasMoreTokens()) {
                word = stringTokenizer.nextToken();
                boolean belongs = true;
                byte[] wordBytes = word.getBytes("utf-8");
                for (int i = 0; i < wordBytes.length; i++) {
                    if (!listOfBytes.contains(wordBytes[i])) {
                        belongs = false;
                        break;
                    }
                }
                if (listOfEntities.contains(word)) {
                    belongs = false;
                }
//                System.out.println(word + " " + (patri ? "patri" : "nepatri"));
                if (belongs) {
                    stringBuffer.append(word).append(" ");
                }
            }
            return stringBuffer.toString();
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    /**
     * Metoda, ktora z predaneho retazca s bielymi medzerami vrati retazec s
     * jednou bielou medzerou medzi slovami.
     *
     * @param text   text s white spaces.
     * @return   text s jednou bielou medzerou medzi slovami.
     */
    public String withoutWhiteSpaces(String text) {
        if (text == null)
            throw new NullPointerException("text si null.");
        
        //Split search string into individual terms.
        Pattern pattern = Pattern.compile("[\\s]+|\\.|!|_|:|\\?|&|\\$|\\(|\\)|/|,|;|\\\\|\"|\\{|\\}|\\[|\\]|=");
        String[] terms = pattern.split(text);
        //Check to see if each term matches.
        StringBuffer pageBuffer = new StringBuffer();
        for (int i = 0; i < terms.length; i++) {
            pageBuffer.append(terms[i] + " ");
        }
        return pageBuffer.toString();
    }
    
    /**
     * Metoda, ktora z predaneho textu odstrani vsetky tagy, ktore su uzavrene
     * v znackach &lt; a &gt; vrati len cisty text. Odstrani aj javascript.
     *
     * @param text   retazec s tagovacimi znackami, ktore treba odstranit.
     * @return   text bez tagov.
     */
    public String removeTags(String text) {
        if (text == null)
            throw new NullPointerException("text is null.");
        // TODO otestovat chovanie novej metody
//        filteredText = text.replaceAll("\\<script.*?\\</script>", " ");
//        return filteredText.replaceAll("\\<.*?\\>", " ");
        return text.replaceAll("\\<script.*?\\</script>", " ").replaceAll("\\<.*?\\>", " ");
    }
    
    
    
}
