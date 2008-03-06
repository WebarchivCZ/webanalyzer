/*
 * Counter.java
 *
 * Created on Streda, 2007, j�n 13, 19:19
 *
 * Counter pocita body pre URL. Metodu ktora inkrementuje points je volana
 * jedinacikmi Words, Phones... Ked points bude &gt CONTRY_BORDER tak sa zavola
 * metoda stop jedinacka crawler.
 */
 
package org.archive.analyzer;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Ivan Vlcek
 */
public class Counter {
    
    /* Konstanta ktora vytvori jedinu instanciu Countera. */
    private static final Counter INSTANCE = new Counter();
    
    /* Odkaz na jedinacka crawler aby som mohol zavolat jeho metodu stop. */
    private static final Crawler CRAWLER = Crawler.getInstance();    
    
    /* Hranicna hodnota stranky, pri jej prekroceni je URL v oblasti zaujmu */
    private final int COUNTER_BORDER = 10;
    
    /* Instancia AtomicInteger points, ktore sa budu inkrementovat. */
    private AtomicInteger points = new AtomicInteger(0);    
    
    /**
     * Staticka tovarna metoda, ktora poskytne odkaz na jedinu instanciu
     * tejto triedy Counter.
     *
     * @return counter   jedina instancia tejto triedy Counter.
     */
    public static Counter getInstance() {
        return INSTANCE;
    }
    
    /**
     * Jedinacik Conter bude pocitat points.
     */
    private Counter() {
    }
    
    /**
     * Metoda, ktora inkrementuje AtomicInterger counter. Ak counter pesiahne
     * hodnoty COUNTER_BORDER, tak sa zavola metoda stop 
     * aby skoncil crawling povodnej URL a zaroven sa nastavi premenna points
     * na nulu. 
     */
    public void incrementByOne() {
        if (points.incrementAndGet() > COUNTER_BORDER) {
            // Zastavim crawler.
            CRAWLER.stop();                       
        }
    } 
    
    /**
     * Metoda, ktora inkrementuje AtomicInteger counter, podla hodnoty
     * predanej ako parameter.
     *
     * @param value   hodnota, ktora sa pricita k bodom stranky.
     */
    public void incrementByParam(int value) {
        if (points.addAndGet(value) > COUNTER_BORDER) {
            // Zastavim Crawler
            CRAWLER.stop();
        }
    }
    
    /**
     * Metoda, ktora inkrementuje pocet ziskanych bodov. Premenna typu 
     * AtomicInteger sa inkrementuje o 1.
     */
    public void incrementPoints() {
        points.incrementAndGet();
    }
    
    /**
     * Metoda, ktora inkremenuje pocet ziskanych bodov o hodnotu predanu ako 
     * paramter.
     *
     * @param value hodnota, o ktoru sa zvacsi pocet ziskanych bodov
     */
    public void incrementPointsByValue(int value) {
        points.addAndGet(value);
    }

    /**
     * Metoda, ktora dekrementuje AtomicInteger counter, podla hodnoty
     * predanej ako parameter.
     *
     * @param value   hodnota, krora sa pricita k bodom stranky.
     */
    public void decrementByParam(int value) {
        points.addAndGet(-value);
    }
    
    /**
     * Metoda, ktora porovna predanu hodnotu ako paratemer s hodnotou
     * premennej points, ktora sa inkrementovala pri kazdej najdenej hladanej
     * hodnote.
     *
     * @param czechBorder hranica bodov predstavujuca cesku stranku.
     * @return true prave vtedy, ked points je vecsia alebo rovna czechBorder
     */
    public boolean reachedCzechBorder(int czechBorder) {
        return points.get() >= czechBorder;
    }
    
    /**
     * Metoda, ktora vrati pocet aktualne nazbieranych bodov.
     *
     * @return pocet aktualne nazbieranych bodov
     */
    public int getReachedPoints() {
        return points.get();
    }
    
    /** 
     * Metoda, ktora nastavi citac bodov na nulu. Resetuje pre dalsie 
     * crawlovanie.
     */
    public void resetPoints() {
        points.set(0);
    }
}
