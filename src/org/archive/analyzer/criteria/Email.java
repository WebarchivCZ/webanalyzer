/*
 * Email.java
 *
 * Created on Utorok, 2007, marec 20, 1:48
 *
 * Trieda Email bude v predanom texte typu String hladat emaily podla regexpu.
 * Ak najde email, tak inkrementuje jedinacika Counter.
 */

package org.archive.analyzer.criteria;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.archive.analyzer.*;
import org.apache.log4j.Logger;

/**
 *
 * @author Ivan Vlcek
 */
public class Email {
    
    private final Logger log4j = Logger.getLogger(Email.class);
    
    /* Jedina instancia emailu */
    private static final Email INSTANCE = new Email();
    
    /* Konstanta ktora sa pricita k bodom stranky ak sa najde email. */
    private static final int EMAIL_VALUE = 1;
    
    /* Odkaz na jedinacika Counter, pouzijem jeho metodu inkrement. */
    private static final Counter COUNTER = Counter.getInstance();
    
    /* Regexp na hladanie ceskych emailov. */
    private final String EMAIL_REGEXP =
            "([a-z0-9._-]+@[a-z0-9.-]+\\.[a-z]{2,4} )";
    
    /* Vzor pre regexp. */
    private Pattern emailPattern;
    
//    /* Mnozina emailov najdenych na stranke. */
//    private HashSet<String> hsEmails = new HashSet<String>();
    
    /* Najdeny email. */
    private String foundEmail = null;
    
    /* Pocet vsetkych analyzovanych emailov. */
    private int numberOfAllAnalyzedObjects;
    
    /* Pocet validnych najdenych emailov. */
    private int numberOfValidFoundedObjects;
    
    /* Vypisat na vustup najdene objekty? .*/
    private boolean sout;
    
    /* Jazyk, podla ktoreho analyzujeme stranku, podla neho sa budu robit
     * statistiky, vsetkych najdenych emailov ku vsetkym ktore z nich su
     * emaily zadaneho jazyka. */
    private String language = null;
    
    /**
     * Staticka tovarna metoda, ktora vrati odkaz na jedinu instanciu triedy
     * Email.
     *
     * @return email   jedina instancia tejto triedy.
     */
    public static Email getInstance() {
        return INSTANCE;
    }
    
    /**
     * Metoda, ktora nainicializuje instanciu tejto triedy podla parametru
     * language, ktory definuje jazyk, podla ktoreho analyzujeme stranku.
     *
     * @param language      jazyk, podla ktoreho analzujeme stranku.
     */
    public void inicialize(String language) {
        this.language = language;
    }
    
    /**
     * Konstruktor nastavi a skompiluje vzor email pattern.
     */
    private Email() {
        emailPattern = Pattern.compile(EMAIL_REGEXP, Pattern.CASE_INSENSITIVE);
    }
    
//    /**
//     * Vymaze mnozinu hsEmails.
//     */
//    private void clearHSEmails() {
//        hsEmails.clear();
//    }
    
    /**
     * Metoda bude vyhladavat emaily v predanom texte. Ak najde email, tak
     * inkrementuje Counter. Nastavena aby inkrementovala vzdy novy najdeny
     * email.
     *
     * @param line   text, ktory sa bude prehladavat.
     */
    public void search(String line) {
//        log4j.debug("EMAIL.search line=line zatim");
        if (line == null)
            throw new NullPointerException("line is null.");
        Matcher matcher = emailPattern.matcher(line);
        while (matcher.find()) {
            foundEmail = matcher.group();
            numberOfAllAnalyzedObjects++;
            if (foundEmail.endsWith(language.toLowerCase()) ||
                    foundEmail.endsWith(language.toUpperCase())) {
                COUNTER.incrementPointsByValue(EMAIL_VALUE);
                numberOfValidFoundedObjects++;
                
                if (sout)
                    System.out.println("Email " + foundEmail);
            }
        }
//        log4j.debug("EMAIL finished.");
    }
    
    /**
     * Metoda, ktora vracia percenta vyskytu validnych objektov, cize emailov,
     * ktore su ceske.
     *
     * @return      precento vyskytu validnych objektov, emailov.
     */
    public String statisticsInPercent() {
        if (numberOfAllAnalyzedObjects == 0) {
            return "EMAIL NOT FOUND !";
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