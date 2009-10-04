/*
 * Class needs some modifications to run properly !!!.
 * 
 * This class is just for testing purposes. I would like to test whether it's
 * good to have separated thread that holds the connection to DB and executes
 * queries or DB connection is held by first thread that initializes webanalyzer
 * managers and each toe thread uses the synchronized method to execute DB
 * queries.
 *
 * This class holds connection in first thread that initializes
 * DbAccessManagerand executes and queries are executed by the toe thread that
 * accesses it.
 */
package cz.webarchiv.webanalyzer.multithread.testobj;

import cz.webarchiv.webanalyzer.multithread.managers.IManager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLDataException;
import java.sql.SQLException;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
public class DBAccessManagerTest implements IManager {

    private static final Logger log4j = Logger.getLogger(
            DBAccessManagerTest.class);
    private static final DBAccessManagerTest INSTANCE = new DBAccessManagerTest();
    /* Signle connection instance to access DB by all ToeThreads. */
    private Connection connection;
    /* Prepared statement for inserting stats into DB. */
    private PreparedStatement insertStats;

    public static DBAccessManagerTest getInstance() {
        return INSTANCE;
    }

    private final String sqlInsert = "INSERT INTO webarchiv_stats_tb " +
            "(ID, URL, ContentType, ReachedPoints, GeoIpAll, GeoIpValid, " +
            "DictAll, DictValid, HtmlTagAll, HtmlTagValid, " +
            "EmailAll, EmailValid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    /**
     * Singleton
     */
    private DBAccessManagerTest() {
        //empty
    }

    /**
     * Method establishes connection with the database. If there is any
     * problem it throws exception that is caught by Heritrix.
     * 
     * @throws Exception
     */
    public void init() throws Exception {
        try {
            log4j.debug("initDBAccessManagerTets");
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection("jdbc:mysql://raptor.webarchiv.cz/vlcek",
                    "vlcek", "ivanvlcek");
            if (!connection.isClosed()) {
                log4j.debug("Successfully connected to " +
                        "MySQL server using TCP/IP...");
            }
            // every sql command is commited automaticaly as it is executed
            connection.setAutoCommit(true);
            insertStats = connection.prepareStatement(sqlInsert);

        } catch (InstantiationException ie) {
            log4j.fatal("initDBAccessManagerTest " + ie.getCause());
            throw new InstantiationException("initDBAccessManagerTest, " +
                    "class or its nullary constructor is not accessible");
        } catch (IllegalAccessException iae) {
            log4j.fatal("initDBAccessManagerTest " + iae.getCause());
            throw new IllegalAccessException("initDBAccessManagerTest, " +
                    "class represents an abstract class, an interface, an " +
                    "array class, a primitive type, or void; or if the class " +
                    "has no nullary constructor; or if the instantiation " +
                    "fails for some other reason.");
        } catch (SQLDataException sqlde) {
            log4j.fatal("initDBAccessManagerTest " + sqlde.getCause());
            throw new SQLDataException("initDBAccessManagerTest, " +
                    "DB error occured when establishing connection or " +
                    "when creating prepared statement");
        }
    }

    /**
     * Closes the DB connection.
     * @throws Exception
     */
    public void close() throws Exception {
        try {
            log4j.debug("closeDbAcccessManagerTest");
            connection.close();
        } catch (SQLException sqle) {
            log4j.fatal("closeDBAccessManagerTest, " + sqle.getCause());
            throw new SQLException("closeDBAccessManagerTest, DB error when " +
                    "closing connection");
        }
    }

    /**
     * SQL update method
     *
     * This method will be called by many threads so it should provide
     * synchronized access. !!!
     *
     * @param uriStatistics string holds uri statistics to be inserted into DB
     */
    public synchronized void insertStatistics(DBURITempStats dBURITempStats) {
    // todo create synchronized access to this method
    // todo instead of simple arguments use URIStatistics object
    // todo get the name of the thread accessing this method
        try {
        log4j.debug("insertStatistics, " + dBURITempStats);
        insertStats.setLong(1, dBURITempStats.getId());
        insertStats.setURL(2, dBURITempStats.getUrl());
        insertStats.setString(3, dBURITempStats.getContentType());
        insertStats.setLong(4, dBURITempStats.getReachedPoints());
        insertStats.setLong(5, dBURITempStats.getGeoIpAll());
        insertStats.setLong(6, dBURITempStats.getGeoIpValid());
        insertStats.setLong(7, dBURITempStats.getDictAll());
        insertStats.setLong(8, dBURITempStats.getDictValid());
        insertStats.setLong(9, dBURITempStats.getHtmlTagAll());
        insertStats.setLong(10, dBURITempStats.getHtmlTagValid());
        insertStats.setLong(11, dBURITempStats.getEmailAll());
        insertStats.setLong(12, dBURITempStats.getEmailValid());
        insertStats.executeUpdate();

        log4j.debug("Thread waiting: " + Thread.currentThread().getName());
        for (int i = 0; i<9000; i++) {
            // do nothing just wait
            System.out.print(".");
        }
        System.out.println(".");
        log4j.debug("Thread waiting finished: " + Thread.currentThread().getName());
        } catch (SQLException sqle) {
            log4j.warn("insertStatistics, " + sqle.getCause());
        }
    }
}
