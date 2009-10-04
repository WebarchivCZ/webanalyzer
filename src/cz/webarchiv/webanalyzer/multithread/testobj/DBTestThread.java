/*
 * This thread should be used only for testing purposes for DBTest object.
 * Threads are supposed to simulate ToeThreads in Heritrix.
 */
package cz.webarchiv.webanalyzer.multithread.testobj;

import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
public class DBTestThread extends Thread {

    private static final Logger log4j = Logger.getLogger(
            DBTestThread.class);
    private DBURITempStats dBURITempStats;
    private static final DBAccessManagerTest dBAccessManagerTest =
            DBAccessManagerTest.getInstance();

    public void run() {
        log4j.debug("run uri=" + dBURITempStats);
        log4j.debug("Thread.name=" + Thread.currentThread().getName());
        processURI(dBURITempStats);
    }

    private void processURI(DBURITempStats dBURITempStats) {
        log4j.debug("processURI uri=" + dBURITempStats);
        dBAccessManagerTest.insertStatistics(dBURITempStats);
    }

    public void setURI(DBURITempStats dBURITempStats) {
        this.dBURITempStats = dBURITempStats;
    }
}
