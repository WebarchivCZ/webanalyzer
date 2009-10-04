/*
 * This will create 200 threads and for each thread uriStatistic instance.
 * Before creating 200 thread a first thread will initialize DBAccessManagerTest
 * what means that DB connection will be created. Then ToeThreads will be
 * inserting statistics into DB.
 */
package cz.webarchiv.webanalyzer.multithread.testobj;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
public class DBTest {

    private static final Logger log4j = Logger.getLogger(DBTest.class);
    private static final DBAccessManagerTest DB_ACCESS_MANAGER_TEST =
            DBAccessManagerTest.getInstance();

    /**
     * Initialize all DBAccessManager = create connection to DB.
     */
    public void init() throws Exception {
        try {
            DB_ACCESS_MANAGER_TEST.init();
        } catch (Exception ex) {
            log4j.fatal("init, " + ex.getCause());
        }
    }

    /**
     * finalize all managers especialy DBAccessManagerTest.
     */
    public void close() throws Exception {
        try {
            DB_ACCESS_MANAGER_TEST.close();
        } catch (Exception ex) {
            log4j.fatal("init " + ex.getCause());
        }
    }

    /**
     * Run the simulation of Heritrix. Create 200 ToeThreads that will use
     * DBAccessManagerTest method called insertStatistics().
     */
    @SuppressWarnings("static-access")
    public void run() throws InterruptedException, MalformedURLException {

        for (int j = 0; j < 1; j++) {
            List<DBTestThread> dbThreads = new ArrayList<DBTestThread>();
            for (int i = 0; i < 50; i++) {
                DBTestThread dBTestThread = new DBTestThread();
                dBTestThread.setURI(generateURI(i));
                dbThreads.add(dBTestThread);
            }
            // start threads
            for (DBTestThread toe : dbThreads) {
                toe.start();
                log4j.debug("Thread started: " + Thread.currentThread().getName());
            }
            // join threads
            for (DBTestThread toe : dbThreads) {
                toe.join();
            }
        }
        log4j.debug("Thread is going to end: " + Thread.currentThread().getName());
    }

    /**
     * Generate URIstats, load uris from file.
     * @param args
     * @throws Exception
     */
    private DBURITempStats generateURI(int i) throws MalformedURLException {
        DBURITempStats uriStats = new DBURITempStats();
        uriStats.setId(i);
        uriStats.setUrl(new URL("https://is.muni.cz/auth1234567890,'':\"/,./\\;][=-0`~!@#$%^&*()_+}{\\\":?><°12345678990%ˇ(/)!\"_:?;+ľščťžýáíé=´äúň§ôň-.,"));
        uriStats.setContentType("text/html");
        uriStats.setReachedPoints(i);
        uriStats.setGeoIpAll(0);
        uriStats.setGeoIpValid(0);
        uriStats.setDictAll(i);
        uriStats.setDictValid(i);
        uriStats.setHtmlTagAll(i);
        uriStats.setHtmlTagValid(i);
        uriStats.setEmailAll(i);
        uriStats.setEmailValid(i);
        return uriStats;
    }

    public static void main(String[] args) throws Exception {
        DBTest dBTest = new DBTest();
        dBTest.init();
        dBTest.run();
        dBTest.close();
    }
}

