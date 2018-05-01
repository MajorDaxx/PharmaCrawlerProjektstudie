/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ComClass;

import Crawler.Crawler;
import DataModul.DB.DatabaseConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Beschreibt eine Anfrage durch die Weboberfläche
 *
 * @author lasse
 */
public class Request extends Thread {

    //Die eindeutige ID dieses Request aus der DB
    int id;

    //Datum des Datensatz
    private String date;

    /**
     *
     */
    private Product artikel;

    /**
     * Crawler die für diese Anfrage genutz werden sollen
     */
    private Crawler crawler;

    /**
     * Websites die Gecrawlt werden
     */
    private URL host;

    /**
     * Das zugehörige Request Set
     */
    private RequestSet reqSet;

    /**
     *
     * @param date
     * @param artikel
     * @param crawlers
     * @param host
     * @param reqSet
     */
    public Request(String date, Product artikel, Crawler crawlers, URL host, RequestSet reqSet) {
        this.date = date;
        this.artikel = artikel;
        this.crawler = crawlers;
        this.reqSet = reqSet;
        this.host = host;
    }

    public String getDate() {
        return date;
    }

    public Product getArtikel() {
        return artikel;
    }

    public URL getHosts() {
        return host;
    }

    public Crawler getCrawler() {
        return crawler;
    }

    @Override
    public String toString() {
        StringBuilder sB = new StringBuilder();
        //sB.append(super.toString()); //To change body of generated methods, choose Tools | Templates.
        sB.append("Date:").append(date).append("_")
                .append("Artikel: ").append(artikel.asJSON()).append("_")
                .append("Crawler:").append(crawler.getClassName()).append("_")
                .append("Host: ").append(host);
        return sB.toString();
    }

    public void setID(int id) {
        this.id = id;
    }

    public boolean isAnswerd() {
        return artikel.hasAnswer();
    }

    @Override
    public synchronized void start() {
        super.start(); //To change body of generated methods, choose Tools | Templates.
        
        artikel.setAnswer(crawler.crawln(artikel, host));
        
        reqSet.requestFine(this);
        
        try {
            DataModul.DB.DatabaseConnection.getDbCon().executeInsert(id, (String) artikel.getField("pzn").getVal1(),artikel);
        } catch (DatabaseConnection.NoConnectionException | SQLException ex) {
            Logger.getLogger(Request.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
