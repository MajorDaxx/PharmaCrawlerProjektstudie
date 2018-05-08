/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataModul.DB;

import ComClass.SpezifiedAttr.Comparison_attr;
import ComClass.Product;
import ComClass.Request;
import ComClass.RequestSet;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lasse
 */
public class DatabaseConnection {

    private static final Logger LOG = Logger.getLogger(DatabaseConnection.class.getName());

    private static final DatabaseConnection DB_CON = new DatabaseConnection();

    public static DatabaseConnection getDbCon() throws NoConnectionException {
        if (DB_CON != null) {
            return DB_CON;
        } else {
            throw new NoConnectionException();
        }

    }

    //-----------------------------------------------------------------------  
    Connection con;

    private DatabaseConnection() {
        try {
            Driver d = new com.mysql.jdbc.Driver();
            DriverManager.registerDriver(d);
            //Use Poperties class and File to ban config from code
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/webcrawler?verifyServerCertificate=false&useSSL=true", "admin", "admin");
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Keine Datenbankberbindung Möglich", ex);
        }

    }

    public void executeInsert(int run_ID, String artikel_ID, Product a) throws SQLException {

        String sql = "Insert Into webcrawler.product (Run_ID,Artikel_ID,Attribute,Value) Values(" + run_ID + ",'" + artikel_ID + "',?,?)";

        for (Comparison_attr comparison_attr : a.getAttr()) {
            if (comparison_attr.getVal2() != null) {
                PreparedStatement prepStm = con.prepareStatement(sql);
                prepStm.setString(1, comparison_attr.getName());
                prepStm.setObject(2, comparison_attr.getVal2());
                prepStm.execute();
            }
        }

    }

    public ResultSet selectPznExits(Request req) throws SQLException {
        //PrepareStatment
        Statement stm = con.createStatement();

        String sql = "SELECT * FROM webcrawler_results.artikel "
                //+ "WHERE PZN = '" + req.getPzn() + "'"
                + "and Host ='" + req.getHosts() + "'"
                + "and Date ='" + req.getDate() + ")";
        stm.execute(sql);

        return stm.getResultSet();
    }

    public List<String> getCrawlerNames() {
        try {
            Statement stm = con.createStatement();

            String sql = "Select * from webcrawler.websitescrawler";
            stm.closeOnCompletion();
            ResultSet rS = stm.executeQuery(sql);

            List<String> s = new LinkedList<>();

            boolean last;
            rS.beforeFirst();
            while (rS.next()) {
                s.add(rS.getString(1));
            }

            return s;

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);

        }
        return null;

    }

    /**
     * Das Matching Verfahren zu einem Product kann weiter verfeinert Werden
     *
     * @param reqSet
     */
    public void checkRequestSet(RequestSet reqSet) {
        try {

            String prpSQLInsert
                    = "Insert Into webcrawler.request (Date,Crawler,Host) Values (?,?,?);";
            PreparedStatement prepStmInsert;

            for (Request request : reqSet) {

                int id = ckeckRequest(request);

                if (id >= 0) {
                    request.setID(id);
                    Product dbAnswer = getArtikel(id, request.getArtikel().getField("pzn").getVal1().toString());
                    //Antwort auf den Request geben
                    request.getArtikel().setAnswer(dbAnswer);
                } else {
                    //Hinzufügen der Abfrage
                    prepStmInsert = con.prepareStatement(prpSQLInsert);

                    prepStmInsert.setString(1, request.getDate());
                    prepStmInsert.setString(2, request.getCrawler().getClassName());
                    prepStmInsert.setString(3, request.getHosts().getHost());
                    prepStmInsert.execute();

                    //Der Systeminternen Abfrage die ID zuordnen
                    request.setID(ckeckRequest(request));
                }

            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Fehler bei der DB Abfrage des Request Set "
                    + "aufgetreten", ex);
        }

    }

    /**
     * Überprüfen ob es bereit einmal eine Abfrage wie diese Gab
     *
     * @param request
     * @return die ID des Request oder -1 wenn es noch keine ID gab
     * @throws SQLException
     */
    private int ckeckRequest(Request request) throws SQLException {
        String prpSQLQuery
                = "Select Run_ID from webcrawler.request where date =? and Crawler = ? and Host = ?";
        PreparedStatement prepStmQuery;

        prepStmQuery = con.prepareStatement(prpSQLQuery);

        prepStmQuery.setString(1, request.getDate());
        prepStmQuery.setString(2, request.getCrawler().getClassName());
        prepStmQuery.setString(3, request.getHosts().getHost());
        ResultSet resSet = prepStmQuery.executeQuery();

        while (resSet.next()) {
            if (resSet.first()) {
                return resSet.getInt(1);
            } else {
                return -1;
            }
        }
        return -1;
    }

    private Product getArtikel(int id, String pzn) throws SQLException {

        Product answer = new Product();
        String prepStm_getArtikel_SQL = "SELECT * FROM webcrawler.product where Run_ID = ? and Artikel_ID=?";
        PreparedStatement prepStm_getArtikel = con.prepareStatement(prepStm_getArtikel_SQL);

        prepStm_getArtikel.setInt(1, id);
        prepStm_getArtikel.setString(2, pzn);

        ResultSet resSet = prepStm_getArtikel.executeQuery();

        while (resSet.next()) {

            //BLOB wird als Object eingelesen
            answer.setField(resSet.getString("Attribute"), resSet.getBlob("Value"));
        }

        return answer;
    }

    public static class NoConnectionException extends Exception {

    }

}
