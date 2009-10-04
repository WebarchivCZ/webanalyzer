/*
 * This manager establishes connection with external DB and stores stats
 * information into it. 
 */
package cz.webarchiv.webanalyzer.multithread.managers;

import cz.webarchiv.webanalyzer.multithread.WebAnalyzerProperties;
import cz.webarchiv.webanalyzer.multithread.analyzer.ProcessedCrawlURI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
public class DBStatsManager implements IManager {

    private static final Logger log4j = Logger.getLogger(DBStatsManager.class);
    private static final DBStatsManager INSTANCE = new DBStatsManager();
    /* Signle connection instance to access DB by all ToeThreads. */
    private Connection connection;
    /* Prepared statement for inserting stats into DB. */
    private PreparedStatement insertStats;
    private final String sqlInsert = "INSERT INTO " +
            WebAnalyzerProperties.getInstance().getDbTable() +
            " (URL, ContentType, ReachedPoints, GeoIpAll, GeoIpValid, " +
            "DictAll, DictValid, HtmlTagAll, HtmlTagValid, " +
            "EmailAll, EmailValid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    /* Prepared statement to reset autoincrement in table. */
    private PreparedStatement resetAutoincrement;
    private final String resetAutoincrementSQL =
            "alter table " + WebAnalyzerProperties.getInstance().getDbTable() +
            " auto_increment = 1";
    /* Prepared statement to create table. */
    private PreparedStatement createTable;
    private final String sqlCreateTable =
            "create table if not exists " +
            WebAnalyzerProperties.getInstance().getDbTable() +  " (" +
                "ID BIGINT UNSIGNED NOT NULL AUTO_INCREMENT," +
                "URL VARCHAR(500)," +
                "ContentType VARCHAR(50)," +
                "ReachedPoints BIGINT UNSIGNED," +
                "GeoIpAll BIGINT," +
                "GeoIpValid BIGINT," +
                "DictAll BIGINT," +
                "DictValid BIGINT," +
                "HtmlTagAll BIGINT," +
                "HtmlTagValid BIGINT," +
                "EmailAll BIGINT," +
                "EmailValid BIGINT," +
                "PRIMARY KEY (ID)" +
            ")";
    /* Prepared statement to drop table. */
    private PreparedStatement dropTable;
    private String sqlDropTable = "drop table " +
            WebAnalyzerProperties.getInstance().getDbTable();
    
    /**
     * Returns singleton instance of this object
     * @return
     */
    public static DBStatsManager getInstance() {
        return INSTANCE;
    }

    /**
     * Private constructor
     */
    private DBStatsManager() {
        // empty
    }

    /**
     * Method that initializes manager. Establises connection to database
     * and compiles the prepared statement. Before this it gets all
     * the necessary information from webanalyzer.properties.
     *
     * @throws Exception
     */
    public void init() throws Exception {
        try {
            log4j.debug("initDBStatsManager");
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(
                    WebAnalyzerProperties.getInstance().getDbURL(),
                    WebAnalyzerProperties.getInstance().getDbUsername(),
                    WebAnalyzerProperties.getInstance().getDbPassword());
            if (!connection.isClosed()) {
                log4j.debug("Successfully connected to " +
                        "MySQL server using TCP/IP...");
            }
            // every sql command is commited automaticaly as it is executed
            connection.setAutoCommit(true);
            // drop table if webanalyzer.properties says so
            if (WebAnalyzerProperties.getInstance().getDbDropTable() == 1) {
                dropTable = connection.prepareStatement(sqlDropTable);
                dropTable.executeUpdate();
            }
            // create table if webanalyzer.properties says so
            if (WebAnalyzerProperties.getInstance().getDbTableCreate() == 1) {
                createTable = connection.prepareStatement(sqlCreateTable);
                createTable.executeUpdate();
            }
            // reset autoincrement if webanalyzer.properties says so
            if (WebAnalyzerProperties.getInstance().getDbResetAutoincrement() == 1) {
                resetAutoincrement = connection.prepareStatement(resetAutoincrementSQL);
                resetAutoincrement.executeUpdate();
            }
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
     * Method that closes the manager. It releases the database connection.
     *
     * @throws Exception
     */
    public void close() throws Exception {
        try {
            log4j.debug("closeDBStatsManager");
            connection.close();
        } catch (SQLException sqle) {
            log4j.fatal("closeDBAccessManagerTest, " + sqle.getCause());
            throw new SQLException("closeDBAccessManagerTest, DB error when " +
                    "closing connection");
        }
    }

    /**
     * Method inserts statistics data into defined table. All arguments are
     * filled from the ProcessedCrawlURI curi object. Those arguments that
     * are supposed to be null have value -1 by default. If value in table is
     * -1 it means that this searcher was not required for analysis in
     * webanalyzer.properties.
     *
     */
    public synchronized void insertStats(ProcessedCrawlURI curi) {
        try {
            log4j.debug("insertStats curi=" + curi.toString());
            log4j.debug("insertStats thread=" + Thread.currentThread().getName());
            // ID is generated automatically by my sql AUTO_INCREMENT function
            // if URL is too long for database column store only first 500 characters
            if (curi.getUrlName().length() > 500) {
                insertStats.setString(1, curi.getUrlName().substring(0, 499));
            } else {
                insertStats.setString(1, curi.getUrlName());
            }
            insertStats.setString(2, curi.getContentType());
            insertStats.setLong(3, curi.getReachedPoints());
            insertStats.setLong(4, curi.getGeoIpAll());
            insertStats.setLong(5, curi.getGeoIpValid());
            insertStats.setLong(6, curi.getDictAll());
            insertStats.setLong(7, curi.getDictValid());
            insertStats.setLong(8, curi.getHtmlTagAll());
            insertStats.setLong(9, curi.getHtmlTagValid());
            insertStats.setLong(10, curi.getEmailAll());
            insertStats.setLong(11, curi.getEmailValid());
            insertStats.executeUpdate();
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(DBStatsManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
