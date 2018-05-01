/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataModul;

import ComClass.Request;
import ComClass.RequestSet;
import DataModul.DB.DatabaseConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lasse
 */
public class DataApi {

    static final Logger LOG = Logger.getLogger(DataApi.class.getName());

    private static final DataApi sigelton = new DataApi();

    public static DataApi getApi() {
        return sigelton;
    }

    /**
     * Managing der Bearbeitung eines Request:
     *
     * 1. Prüfen ob in DB 2. Prüfen ob Crawler läuft 3. Crawler starten
     *
     * @param requestSet
     */
    public void get(RequestSet requestSet) throws InterruptedException {

        LOG.severe(requestSet.toString());

        /**
         * Ergebnisse in der datenbank suchen
         */
        try {
            DatabaseConnection.getDbCon().checkRequestSet(requestSet);
        } catch (DatabaseConnection.NoConnectionException ex) {
            LOG.log(Level.SEVERE, "Keine Verbindung zur Datenbank möglich", ex);
        }

        /**
         * Ergebnisse die nicht in der DB gefunden wurden im Web suchen
         */
        synchronized (requestSet) {
            for (Request thi : requestSet) {
                if (!thi.isAnswerd()) {
                    requestSet.addCrawlingRequest(thi);
                    thi.start();
                }
            }
            //requestSet.wait();
        }
        /**
         * Analyse der Sets und umwandlung in ein Response Set
         */
        
        synchronized (requestSet) {

            for (Request thi : requestSet) {
                if (thi.isAnswerd()) {
                   new Analyzer.Analyser().analyser(thi.getArtikel());
                }
            }
            //requestSet.wait();
        }

        

//        ResponseSet res = new ResponseSet();
//        try {
//            //Überprüfen ob in DB vorhanden
//            DatabaseConnection dbCon = DatabaseConnection.getDbCon();
//
//            ResultSet result = dbCon.selectPznExits(null);
//
//            if (result.first()) {
//                //Artikel a = new Product(PZN.getPZN(result.getString("PZN")));
//                //res.add(a);
//            }
//
//        } catch (DatabaseConnection.NoConnectionException ex) {
//            Logger.getLogger(DataApi.class.getName()).log(Level.SEVERE, "Keine DB überprüfung möglich", ex);
////        } catch (PZN.UnvalidPzn ex) {
////            Logger.getLogger(DataApi.class.getName()).log(Level.SEVERE, "Invalid PZN in DB", ex);
//        } catch (SQLException ex) {
//            Logger.getLogger(DataApi.class.getName()).log(Level.SEVERE, "SQL Error in Read", ex);
//        }
//        
//        return null;
    }

}
