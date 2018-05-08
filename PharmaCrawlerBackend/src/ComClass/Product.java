/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ComClass;

import ComClass.SpezifiedAttr.Comparison_attr;
import ComClass.SpezifiedAttr.WrapperString;
import ComClass.SpezifiedAttr.Wrapper_Price;
import com.mysql.jdbc.Blob;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Ergänzt durch Eigenschaften!
 *
 * @author lasse
 */
public class Product {

    private static final Logger LOG = Logger.getLogger(Product.class.getName());

    Map<String, Comparison_attr> attr = new HashMap<>();

    Rating rating;

    public Product() {
    }

    public String asJSON() {

        return "{" + toString() + "}";

    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public Collection<Comparison_attr> getAttr() {
        return attr.values();
    }

    /**
     * Konsitensprüfungen einfügen. Nur bestimmte fielname <-> value
     * Kombinationen zulassen
     *
     * @param filedname
     * @param value
     */
    public void setField(String filedname, Object value) {
        attr.put(filedname, new Comparison_attr(filedname, value));
    }

    public Double getPrice() {

        try {
            return  new Wrapper_Price(attr.get("price")).getVal1();

        } catch (ClassCastException ex) {

            return Double.parseDouble((String) attr.get("price").getVal1());

        }
    }

    /**
     * @param s
     * @return 
     */
    public Comparison_attr getField(String s) {
        
        
        return attr.get(s);
    }

    /**
     * Das übergebene Prouct wird als Antwort auf dieses Product verstanden
     * sodass die Attribute Werte von der ANtwort (val1) an die Attribute von
     * Val2 gespielt werden
     *
     * @param p
     */
    public void setAnswer(Product p) {
        for (Comparison_attr value : p.attr.values()) {
            if (attr.containsKey(value.getName())) {
                attr.get(value.getName()).setVal2(value.getVal1());
            } else {
                attr.put(value.getName(), new Comparison_attr(value.getName(), value.getVal1(), true));
            }
        }
    }

    public boolean hasAnswer() {

        for (Comparison_attr value : attr.values()) {
            if (value.isCompleted()) {
                return true;
            }
        }
        return false;
        //Wenn ein Attribut eine Antwort hat
    }

    public Set<String> getFieldKeys() {
        return attr.keySet();
    }

    public Rating getTotal() {
        return rating;
    }

    @Override
    public String toString() {
        StringBuilder sB = new StringBuilder();

        attr.entrySet().forEach((entry) -> {
            Object obj = entry.getValue();
            if (obj instanceof Blob) {
                try {
                    Blob blob = (Blob) obj;
                    byte[] bdata = blob.getBytes(1, (int) blob.length());
                    obj = new String(bdata);
                } catch (SQLException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                }
            }
            sB.append(entry.getKey()).append("-->").append(obj.toString()).append(";");
        });
        return sB.toString();
    }
}
