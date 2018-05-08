/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ComClass.SpezifiedAttr;

import ComClass.Rating;
import java.util.logging.Logger;


/**
 *
 * @author lasse
 */
public class Comparison_attr {

    static final Logger LOG = Logger.getLogger(Comparison_attr.class.getName());

    protected String name;
    protected Object val1;
    protected Object val2;
    protected Rating rating;
    //Matching bei Strings
    protected Double stringDistance;

    public Comparison_attr(String name, Object val1) {
        this.name = name;
        this.val1 = val1;
    }

    /**
     * Letztes Attribut identifiziert nur das mit diesem Constructor nur der
     * zweiter wert befüllt wird
     *
     * @param name
     * @param val2
     * @param answer
     */
    public Comparison_attr(String name, Object val2, boolean answer) {
        this.name = name;
        this.val2 = val2;
    }

    public void setVal2(Object val2) {
        this.val2 = val2;
    }

    public Double getStringDistance() {
        return stringDistance;
    }

    public void setStringDistance(Double stringDistance) {
        this.stringDistance = stringDistance;
    }
    

    //----------------------------------------------------
    public String getName() {
        return name;
    }

    public Rating getRating() {
        return rating;
    }

    public boolean isCompleted() {
        return val2 != null && val1 != null && val1 != "" && val2 != "";
    }

    public Object getVal1() {
        return val1;
    }

    public Object getVal2() {
        return val2;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }
}
