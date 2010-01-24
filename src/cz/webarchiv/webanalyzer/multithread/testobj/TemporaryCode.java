/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.webarchiv.webanalyzer.multithread.testobj;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Administrator
 */
public class TemporaryCode {

    public static void main(String[] args) {

        Connection con = null;

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection("jdbc:mysql://raptor.webarchiv.cz/vlcek",
                    "vlcek", "ivanvlcek");

            if (!con.isClosed()) {
                System.out.println("Successfully connected to " +
                        "MySQL server using TCP/IP...");
            }

            PreparedStatement insertStats = con.prepareStatement(
                    "INSERT INTO webarchiv_stats_tb (id, url, test) VALUES (?, ?, ?)");
            int resultOfUpdate;
            for (int i = 0; i < 1000; i++) {
                insertStats.setInt(1, i);
                insertStats.setString(2, "test" + i);
                insertStats.setString(3, "ur");
                resultOfUpdate = insertStats.executeUpdate();
                System.out.println("result of update =" + resultOfUpdate);
            }


        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
            }
        }
    }
}
