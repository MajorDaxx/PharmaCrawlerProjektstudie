/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Crawler.SingleCrawler;

import ComClass.Product;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author lasse
 */
public class Crawler_DOC extends Single_Crawler {

    @Override
    public Product crawln(Product refArtikel, URL url) {
        try {
            Product answer = new Product();
            answer.setField("pzn", refArtikel.getField("pzn").getVal1().toString());
            URL target = new URL(url, "/" + refArtikel.getField("pzn").getVal1().toString());
            HttpURLConnection con = (HttpURLConnection) target.openConnection();
            con.setInstanceFollowRedirects(true);
            con.connect();

//            for (Entry headerField : con.getHeaderFields().entrySet()) {
//                System.out.println(headerField.getKey() + ":" + headerField.getValue());
//            }
            Document doc = Jsoup.parse(con.getInputStream(), "UTF-8", "https://docmorris.de");
            Elements elms = doc.select("div.product-description");

            if (elms.size() != 1) {
                throw new Error("Website kann nicht gelesen werden");
            }
            Element elm = elms.first();

            answer.setField("name", elm.select("h1").text());
            Element elmsTable = elm.select("table.product-detail-table").first().child(0);
            //Händisch wird der entsprechende Wert mit dem Namen für das Feld 
            String fieldname = null;
            for (Element child : elmsTable.children()) {

                switch (child.child(0).text().replaceAll(":", "")) {
                    case "Packungsgröße":
                        fieldname = "packing_size";
                        break;

                    case "Darreichungsform":
                        fieldname = "dosage_form";
                        break;
                    case "Verordnungsart":
                        fieldname = "order_type";
                        break;
                    case "PZN":
                        fieldname = "pzn";
                        break;
                    case "Anbiete":
                        fieldname = "provider";
                        break;
                    default:
                        fieldname = child.child(0).text();
                        break;
                }
                answer.setField(fieldname.toLowerCase(),
                        child.child(1).text());

            }

            answer.setField("price", Double.parseDouble(doc.select("td.price").first().text().replace("€", "").replace(",", ".")));
            downloadPicture("foto1", refArtikel, url);
            
            return answer;

        } catch (MalformedURLException ex) {
            Logger.getLogger(Crawler_DOC.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) { 
            Logger.getLogger(Crawler_DOC.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    
    private void downloadPicture(String name,Product refArtikel, URL url) throws IOException{
        InputStream in = new BufferedInputStream(url.openStream());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int n = 0;
        while (-1 != (n = in.read(buf))) {
            out.write(buf, 0, n);
        }
        out.close();
        in.close();
        byte[] response = out.toByteArray();

        refArtikel.setField(name, response);
        
    }
}
