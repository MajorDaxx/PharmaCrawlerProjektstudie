/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ComClass;

import Crawler.Crawler;
import Crawler.SingleCrawler.Single_Crawler;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

/**
 * Klasse die Mehrer Anfragen Bündelt und wenn alle bearbeitet sind eine Meldung
 * an die DataAPI gibt und die Bearbeitung fortzusetzen.
 *
 * Implementiereung von SET???
 *
 * @author lasse
 */
public class RequestSet implements Set<Request> {

    Set<Request> request;
    Set<Request> crawlingRequest;

    public RequestSet() {
        crawlingRequest = new HashSet<>();
        request = new HashSet<>();
    }

    @Override
    public String toString() {
        StringBuilder sB = new StringBuilder();
        sB.append(super.toString()); //To change body of generated methods, choose Tools | Templates.
        request.forEach((request1) -> {
            sB.append(request1).append("\n");
        });
        return sB.toString();
    }

    protected synchronized void requestFine(Request req) {
        crawlingRequest.remove(req);
        if (crawlingRequest.isEmpty()) {
            this.notifyAll();
        }
    }

    public void addCrawlingRequest(Request thi) {
        crawlingRequest.add(thi);
    }

    //Nur um die Klasse als Set in forEach schleifen nutzen zu können
    @Override
    public int size() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Iterator<Request> iterator() {
        return request.iterator();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean add(Request e) {
        return request.add(e);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean addAll(Collection<? extends Request> c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static RequestSet parseRequest(HttpServletRequest request) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, Crawler.NoSuchCrawlerException {

        List<String> crawler = new LinkedList<>();
        List<URL> url = new LinkedList<>();
        String date = null;

        Map<String, Product> artikel_map = new HashMap<>();
        Enumeration<String> param = request.getParameterNames();

        Product apped_artikel;
        while (param.hasMoreElements()) {
            String paramName = param.nextElement();
            switch (paramName) {
                case "url":
                    for (String url_string : request.getParameterValues(paramName)) {
                        if (url_string.length() > 0) {
                            url.add(new URL(url_string));
                        }
                    }
                    break;
                case "Crawler":
                    crawler.addAll(Arrays.asList(request.getParameterValues(paramName)));
                    break;
                case "date":
                    date = request.getParameter(paramName);
                    break;
                default:
                    String[] paramValue = paramName.split("_");
                    apped_artikel = artikel_map.get(paramValue[1]);
                    if (apped_artikel == null) {
                        apped_artikel = new Product();
                        artikel_map.put(paramValue[1], apped_artikel);
                    }
                    Object obj = request.getParameter(paramName);
                    if (obj != null) {
                        apped_artikel.setField(paramValue[0],
                                obj);
                    }
            }
        }
        List<Product> list_artikel = new LinkedList<>();
        list_artikel.addAll(artikel_map.values());
        RequestBuilder requestBuilder = new RequestBuilder(crawler, url, date,
                list_artikel);

        //RequestSet s = new RequestSet();
        return requestBuilder.build();
    }

    /**
     *
     * @author lasse
     */
    public static class RequestBuilder {

        static final Logger LOG = Logger.getLogger(RequestBuilder.class.getName());

        final List<String> crawler;
        final List<URL> url;
        final String date;
        final List<Product> artikel;

        public RequestBuilder(List<String> crawler, List<URL> url, String date, List<Product> artikel) {
            this.crawler = crawler;
            this.url = url;
            if (date == null) {
                //Ersetzen durch das Aktuelle Datum Calendar.newInstance() etc.
                this.date = "2018-03-03";
            } else {
                this.date = date;
            }

            this.artikel = artikel;
        }

        public RequestSet build() throws Crawler.NoSuchCrawlerException {

            RequestSet reqSet = new RequestSet();

            // Erstellung aller validen abfrage Objekte und zusammenfassen 
            //in einer abfragemenge
            for (String s_crawler_Name : crawler) {
                //Crawler aClass = new Single_Crawler();
                Crawler aClass = Crawler.getCrawler(s_crawler_Name);
                url.stream()
                        .filter((url1) -> (aClass.isAbletoCrawl(url1)))
                        .forEachOrdered((url1) -> {
                            artikel.forEach((product) -> {
                                reqSet.add(new Request(date, product, aClass, url1, reqSet));
                            });
                        });
            }
            return reqSet;
        }

    }

}
