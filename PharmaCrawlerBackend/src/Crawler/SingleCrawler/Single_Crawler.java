/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Crawler.SingleCrawler;

import ComClass.Product;
import ComClass.Request;
import java.net.URL;

/**
 *
 * @author lasse
 */
public class Single_Crawler extends Crawler.Crawler {

    public Single_Crawler() {

    }

    @Override
    public boolean isAbletoCrawl(URL url) {
        return "www.docmorris.de".equals(url.getHost());
    }

    @Override
    public String getClassName() {
        return "Single_Crawler";
    }

    @Override
    public Product crawln(Product a, URL url) {
        switch (url.getHost()) {
            case "www.docmorris.de":
                return new Crawler_DOC().crawln(a, url);  
            default:
                throw new Error("ungültiger Host");
        }

    }

 
}
