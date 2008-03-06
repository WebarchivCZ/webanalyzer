/*
 * Dictionary.java
 *
 * Created on �tvrtok, 2007, apr�l 19, 23:43
 *
 * Trieda slovnik, ktora bude obsahovat slovicka, ktore sa nacitaju z 
 * nejakeho suboru slovnika. Slovicka budu ulozene v kolekcii.
 *
 */

package org.archive.analyzer.dictionary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 * @author Ivan Vlcek
 */
public class Dictionary {
    
    /* Slovicka budu ulozene v usporiadanej mnozine TreeSet. */
    private SortedSet<String> words;
    
    /** 
     * Slovicka mozu byt ulozene v kolekcii ArrayList, kde su ulozene presne 
     * podla poradia vkladania. Pouzivam pre index k slovniku, kde su kluce 
     * usporiadane presne podla poradia v ktorom su vlozene.
     */
    private List<String> wordsList = new ArrayList<String>();
    
    
    /** Konstruktor inicializuje words */
    public Dictionary() {
         words = new TreeSet<String>();
    }
    
    /**
     * Vrati usporiadanu mnozinu TreeSet<String>() ako usporiadanu kolekciu.
     *
     * @return Collection<String>   usporiadana kolekcia slov.
     */
    public Collection<String> getWords() {
        return Collections.unmodifiableSortedSet(words);
    }
    
    /** 
     * Prida do usporiadaneho slovnika slovo. Prevedie predane slovo na male
     * pismena.
     *
     * @param word      slovo, ktore sa prida do slovnika.
     */
    public void addWord(String word) {
        String lowWord = word.toLowerCase();
        words.add(lowWord);
    }
    
    /**
     * Odstrani slovo zo slovnika.
     *
     * @param word      slovo, ktore sa ma odstranit.
     * @return true     ak sa slovo podarilo odstranit.
     */
    public boolean removeWord(String word) {
        return words.remove(word);
    }
    
    /**
     * Metoda, ktora vrati true, ak slovnik obsahuje slovo predane ako 
     * parameter.
     *
     * @param word      slovo, o ktorom zistujem ci je v slovniku.
     * @return          true ak sa slovo nachadza v slovniku.
     */
    public boolean containsWord(String word) {
        return words.contains(word);
    }
    
    /**
     * Metoda, ktora vymaze obsah slovnika.
     */
    public void clear() {
        words.clear();
    }
    
    /**
     * Vypise obsah slvnika na standardny vystup.
     */
    public void writeWordsToOutput() {
        for (String word : words) {
            System.out.println(word);
        }
    }
    
    /**
     * Ulozi slovo do usporiadanej kolekcie ArrayList<String>(), kde sa ulozia
     * v presnom poradi ako tam boli vlozene.
     *
     * @param word      slovo
     */
    public void addWordToList(String word) {
        wordsList.add(word);
    }
    
    /**
     * Vrati usporiadany list podla poradia vlozenia ArrayList<String>() ako 
     * nemodifikovatelnu kolekciu.
     *
     * @return  Collection<String> neusporiadana kolekcia slov.
     */
    public Collection<String> getListWords() {
        return Collections.unmodifiableList(wordsList);
    }
    
    /**
     * Vyprazdni usporiadany kolekciu ArrayList<String>().
     */
    public void clearList() {
        wordsList.clear();
    }            
}