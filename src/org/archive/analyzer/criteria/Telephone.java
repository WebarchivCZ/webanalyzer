/*
 * Telephone.java
 *
 * Created on Štvrtok, 2007, jún 21, 10:26
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.archive.analyzer.criteria;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.archive.analyzer.*;

/**
 *
 * @author Ivan Vlcek
 */
public class Telephone {
    
    private final Logger log4j = Logger.getLogger(Telephone.class);
    
    /* Jedina instancia triedy Telephone jedinacik. */
    private static final Telephone INSTANCE = new Telephone();
    
    /* Konstanta, ktora sa pricita k bodom stranky ak sa najde telefon. */
    private static final int TELEPHONE_VALUE = 1;
    
    /* Odakz na jedinacika, ktory bude inkrementovat cz hodnotu stranky. */
    private static final Counter COUNTER = Counter.getInstance();
    
    /* Regexp na hladanie telefonnych cisel. */
    private final String TELEPHONE_REGEXP =
            "([\\+|0]{1,2} ?[0-9]{3}[- ]?[0-9]{3}[- ]?[0-9]{3}[- ]?[0-9]{3})";
    
    private final String TEST = "^[\\+]?[()\\/0-9\\. -]{9,}$";
    
    /* Vzor pre regexp. */
    private Pattern telephonePattern;
    
    /* Mnozina najdenych telefonov. */
    private HashSet<String> hsTelephones = new HashSet<String>();
    
    /* Najdeny telefon. */
    private String foundTelephone;
    
    /* Pocet vsetkych analyzovanych telefonych cisel. */
    private int numberOfAllAnalyzedObjects;
    
    /* Pocet validnych najdenych telefonnych cisel. */
    private int numberOfValidFoundedObjects;
    
    /* Vypisat na vustup najdene objekty? .*/
    private boolean sout;
    
    /* Parameter, ktory udava rozpoznavacie cislo pre danu krajinu. */
    private String telephoneParam = null;
    
    /**
     * Staticka metoda, ktora vrati odkaz na jedinacika Telephone
     *
     * @return instancia jedinacika Telephode.
     */
    public static Telephone getInstance() {
        return INSTANCE;
    }
    
    /**
     * Metoda, ktora inicializuje instanciu, podla predaneho paramtru language.
     * Pomocou neho vyberie ta vhodna tel.pripona pre danu krajinu.
     *
     * @param language      jazyk, ktory urci ake tel. cisla sa maju hladat.
     */
    public void inicialize(String language) {
        if (language.equals("cz")) {
            telephoneParam = "";
        }
    }
    
    /**
     * Konstruktor, ktory nastavi a skompiluje vzor pre regexp.
     */
    private Telephone() {
        telephonePattern = Pattern.compile(TELEPHONE_REGEXP);
    }
    
    /**
     * Vymaze mnozinu najdenych telefonov hsTelephones.
     */
    private void clearHSTelephones() {
        hsTelephones.clear();
    }
    
    /**
     * Metoda bude vyhladavat telefonne cisla v texte, ktory je predany
     * ako parameter.
     *
     * @param line   text, ktory sa bude prehladavat.
     */
    public void search(String line) {
//        log4j.debug("TELEPHONE.search line=line");
        Matcher matcher = telephonePattern.matcher(line);
        while (matcher.find()) {
            numberOfAllAnalyzedObjects++;
            foundTelephone = matcher.group();
            // TODO dorobit vyhladavanie vsetkych tel. cisel a urcit podla 
            // predaneho jazyka, ktore z nich su platne v krajine urcenej
            // jazykom.
            if (foundTelephone.startsWith("+420") ||
                    foundTelephone.startsWith("+ 420") ||
                    foundTelephone.startsWith("00 420") ||
                    foundTelephone.startsWith("00420")) {
                numberOfValidFoundedObjects++;
                COUNTER.incrementPointsByValue(TELEPHONE_VALUE);
                
                if (sout)
                    System.out.println("Telephone founded telephone : " +
                            foundTelephone);
            }
        }
//        log4j.debug("TELEPHONE.search finished");
    }
    
    /**
     * Metoda, ktora vracia percenta vyskytu validnych objektov, cize
     * telefonnych cisel.
     *
     * @return      percento vyskytu validnych objektov, telefonnych cisel.
     */
    public String statisticsInPercent() {
        if (numberOfAllAnalyzedObjects == 0) {
            return "TELEPOHONE NOT FOUND !";
        }
        float percent;
        percent = numberOfValidFoundedObjects*100 / numberOfAllAnalyzedObjects;
        Float percentFloat = new Float(percent);
        return percentFloat.toString();
    }
    
    /**
     * Nastavuje vypisovanie najdenych objektov na vystup.
     *
     * @param sout      vypisat na standard output ?
     */
    public void setSout(boolean sout) {
        this.sout = sout;
    }
    
}
