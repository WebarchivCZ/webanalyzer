/*
 * ToeTestObject, ktory sluzi na testovanie zdialania resources medzi vlaknami
 * heritrixu Toes. Kazdy Toe object si vytvori vlastny ToeTestObject, ktory
 * bude pocitat ziskane body. Na konci hlavnej funkcnej metody, bude overenie,
 * ci ziskany pocet bodov odpovoveda, skutocnosti, ktoru si nasimulujem.
 */
package org.archive.analyzer;

import java.util.Random;
import org.apache.log4j.Logger;
import org.archive.analyzer.util.RandomUtil;

/**
 *
 * @author praso
 */
public class ToeTestObject {

    private static final Logger log4j = Logger.getLogger(ToeTestObject.class);
    /* Premenna, v ktorej je ulozeny pocet bodov pre spracovavau crawlURI. */
    private int points = 0;

    /**
     * Staticka metoda, ktora vytvara instanciu objektu ToeThreadObject
     * @return nova instancia ojektu ToeThreadObject
     */
    public static ToeTestObject createToeTestObject() {
        return new ToeTestObject();
    }

    /**
     * Private constructor.
     */
    private ToeTestObject() {
        log4j.debug("ivlcek ToeTestObject initialized by thread=" +
                Thread.currentThread().getName());
    // private
    }

    /**
     * Hlavna funkcna metoda, ktora vykonava test s nasimulovanou skutocnostou
     * ziskavania bodov points, pre danu CrawlURI.
     * @param curi crawlURI, pre ktoru sa vykonava test
     * @return true ak vysledok testu odpoveda nasimulovanej skutocnosti
     */
    public boolean runCrawl(String curi) {
        log4j.debug("ivlcek runCrawl curi=" + curi + ", by thread=" +
                Thread.currentThread().getName());
                
        boolean pointsEquals;
        Random aRandom = new Random();
        wait(RandomUtil.generateRandomInt(0, 10, aRandom) * 10000000);
        points++;
        wait(RandomUtil.generateRandomInt(0, 10, aRandom) * 10000000);
        points++;
        wait(RandomUtil.generateRandomInt(0, 10, aRandom) * 10000000);
        pointsEquals = points == 2;
        log4j.debug("=====================================================" +
                "=========================================================");
        log4j.debug("ivlcek runCrawl curi=" + curi + ", by thread=" +
                Thread.currentThread().getName() + ", points=" + points +
                " , pointsEquals=" + pointsEquals);
        log4j.debug("=====================================================" +
                "=========================================================");
        return pointsEquals;
    }

    private void wait(int iterations) {
//        for (int i = 0; i < iterations; i++) {
//            // wait
//        }
    }
}
