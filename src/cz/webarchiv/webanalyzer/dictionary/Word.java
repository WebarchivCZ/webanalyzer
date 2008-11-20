/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.webarchiv.webanalyzer.dictionary;

/**
 *
 * @author praso
 */
class Word implements java.lang.Comparable<Word> {

    private String word;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof Word) {
            Word w = (Word) o;
            return getWord().equals(w.getWord());
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return this.getWord().hashCode();
    }

    public int compareTo(Word o) {
        return this.word.compareTo(o.getWord());
    }
}
