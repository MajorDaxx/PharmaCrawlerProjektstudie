/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Crawler;

import ComClass.Product;
import ComClass.Request;
import DataModul.DB.DatabaseConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lasse
 */
public abstract class Crawler {

    static final Logger LOG = Logger.getLogger(Crawler.class.getName());

    static Map<String, Class<? extends Crawler>> crawlers = getCrawler_init();

    private static Map<String, Class<? extends Crawler>> getCrawler_init() {
        try {
            List<String> crawlerNames = DatabaseConnection.getDbCon().getCrawlerNames();
            Map<String, Class<? extends Crawler>> init = new HashMap<>();
            crawlerNames.forEach(new Consumer<String>() {
                @Override
                public void accept(String crawlerName) {
                    try {
                        String[] s = crawlerName.split("[.]");
                        init.put(s[s.length - 1], (Class<? extends Crawler>) Class.forName("Crawler." + crawlerName));
                    } catch (ClassNotFoundException ex) {
                        LOG.log(Level.SEVERE, crawlerName + "Konnte nicht geladen werden", ex);
                    }
                }
            });
            return init;
        } catch (DatabaseConnection.NoConnectionException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static Crawler getCrawler(String s) throws NoSuchCrawlerException {

        if (crawlers.isEmpty()) {
            crawlers = getCrawler_init();
        }
        try {
            //Mapping für verscheiene Schreibweisen / Namen für die 
            //Selbe Klasse einbauen 
            return crawlers.get(s).newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            LOG.log(Level.SEVERE, null, ex);
        } catch (ExceptionInInitializerError err) {
            LOG.log(Level.SEVERE, null, err);
        }
        throw new NoSuchCrawlerException();
    }

    protected Request req;

    public Crawler() {

    }

    public abstract String getClassName();

    public abstract boolean isAbletoCrawl(URL url);

    public abstract Product crawln(Product a, URL url);

    public static class InvalidHost extends Exception {

    }

    public static class NoSuchCrawlerException extends Exception {

        public NoSuchCrawlerException() {
        }
    }
}
