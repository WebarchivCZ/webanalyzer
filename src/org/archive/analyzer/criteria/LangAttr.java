/*
 * LangAttr.java
 *
 * Created on Utorok, 2007, marec 20, 11:30
 *
 * Ak najde attribut lang tak ziska jeho hodnotu (napr. cs) a tuto hodnotu
 * vrati v rerazci metodou getLangAttribute, asi bude lepsie tento reg vyraz
 * skontrolovat v ramici inej triedy, aby bola mensia rezia. Tj. doplnit nejaky
 * reg.vyraz o skupinu v zatvorkach, ktora najde atribut lang. To ale necham na
 * neskor.
 *
 */

package org.archive.analyzer.criteria;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.security.auth.callback.LanguageCallback;
import org.apache.log4j.Logger;
import org.archive.analyzer.*;

/**
 *
 * @author Ivan Vlcek
 */
public class LangAttr {
    
    private final Logger log4j = Logger.getLogger(LangAttr.class);
    
    /* Jedina instancia emailu */
    private static final LangAttr INSTANCE = new LangAttr();
    
    /* Konstanta, ktora sa pricita k bodom stranky ak sa najde atribut lang. */
    private static final int LANG_ATTR_VALUE = 1;
    
    /* Odkaz na jedinacika Counter, pouzijem jeho metodu inkrement. */
    private static final Counter COUNTER = Counter.getInstance();
    
    /* Regexp na hladanie vsetkych atributov lang v html texte. */
    private String langAttr = "lang ?= ?[\"|']? ?([a-z]){2} ?[\"|']?";
    
    /* Vzor pre regexp. */
    private Pattern langAttrPattern;
    
    /* Pocet vsetkych analyzovanych URL. */
    private int numberOfAllAnalyzedObjects;
    
    /* Pocet validnych najdenych atributov. */
    private int numberOfValidFoundedObjects;
    
    /* Vypisat na vustup najdene objekty? .*/
    private boolean sout;
    
    /* Najdeny lang atribut. */
    private String foundLangAttr = null;
    
    /* Language ktoreho atributy sa budu vyhladavat.*/
    private String languageParam = null;
    
    /**
     * Staticka tovarna metoda, ktora vrati odkaz na jedinu instanciu triedy
     * LangAttr.
     *
     * @return jedina instancia tejto triedy LangAttr.
     */
    public static LangAttr getInstance() {
        return INSTANCE;
    }
    
    /**
     * Metoda, ktora nainicializuje triedu LangAttr podla predaneho 
     * parametra language.
     *
     * @param language      jazyk, ktoreho analyzu robime.
     */
    public void inicializeLangAttr(String language) {
        if (language.equals("cz")) {
            languageParam = "cs";
        } else if (language.equals("sk")) {
            languageParam = "sk";
        } 
    }
    
    /**
     * Konstruktor nastavi a skompiluje vzor lanng atributu.
     */
    private LangAttr() {
        langAttrPattern = Pattern.compile(langAttr, Pattern.CASE_INSENSITIVE);
    }
    
    /**
     * Metoda bude vyhladavat atributy lang v predanom texte.
     * Ak najde atribut "lang=cs", tak inkrementuje Counter.
     *
     * @param line   text, ktory sa bude prehladavat.
     */
    public void search(String line) {
//        log4j.debug("LANG_ATTR.search line=line");
        if (line == null)
            throw new NullPointerException("line is null.");
        
        Matcher matcher = langAttrPattern.matcher(line);
        while (matcher.find()) {
            foundLangAttr = matcher.group();
            numberOfAllAnalyzedObjects++;
            if (foundLangAttr.indexOf(languageParam.toLowerCase()) != -1 ||
                    foundLangAttr.indexOf(languageParam.toUpperCase()) != -1) 
            {
                COUNTER.incrementPointsByValue(LANG_ATTR_VALUE);
                numberOfValidFoundedObjects++;
            }
            // TODO nastavit spravne regexp pre najdenie vsetkych atributov a
            // z nich hladat len tie ceske alebo slovenske atd.
            
            if (sout)
                System.out.println("LangAttr founded attribute : " +
                        foundLangAttr);
        }
//        log4j.debug("LANG_ATTR.search finised");
    }
    
    /**
     * Metoda, ktora vracia percenta vyskyty validnych objetov, cize atributov
     * lang, ktore su validne pre danu krajinu. Napriklad pre Cesko
     * lang="cz"
     *
     * @return      percento vyskytu validnych objektov.
     */
    public String statisticsInPercent() {
        if (numberOfAllAnalyzedObjects == 0) {
            return "LANG ATTRIBUTE NOT FOUND !";
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