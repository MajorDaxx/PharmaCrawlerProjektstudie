/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Crawler.SingleCrawler;

import ComClass.Product;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author lasse
 */
public class Crawler_shop extends Single_Crawler {

    @Override
    public Product crawln(Product refArtikel, URL url) {
        try {
            Product answer = new Product();
            answer.setField("pzn", refArtikel.getField("pzn").getVal1());
            URL target = new URL(url, "/search");
            HttpURLConnection con = (HttpURLConnection) target.openConnection();
            con.addRequestProperty("query", refArtikel.getField("pzn").getVal1().toString());
            con.setInstanceFollowRedirects(true);
            con.connect();
//            for (Entry headerField : con.getHeaderFields().entrySet()) {
//                System.out.println(headerField.getKey() + ":" + headerField.getValue());
//            }
            Document doc = Jsoup.parse(con.getInputStream(), "UTF-8", "https://docmorris.de");

            return answer;

        } catch (MalformedURLException ex) {
            Logger.getLogger(Crawler_DOC.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Crawler_DOC.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
