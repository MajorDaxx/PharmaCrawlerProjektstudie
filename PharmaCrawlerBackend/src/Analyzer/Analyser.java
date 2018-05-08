/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analyzer;

import ComClass.Product;
import ComClass.SpezifiedAttr.Comparison_attr;
import ComClass.Rating;
import ComClass.SpezifiedAttr.WrapperString;
import ComClass.SpezifiedAttr.Wrapper_Price;
import com.wcohen.ss.ScaledLevenstein;
import java.util.HashSet;
import java.util.Set;


/**
 *
 * @author lasse
 */
public class Analyser {

    public Analyser() {
    }

    public void analyser(Product artR) {
        ScaledLevenstein lev = new ScaledLevenstein();

        Rating rating;
        Double stringDistance;
        Set<String> keys = new HashSet<>();
        artR.setRating(Rating.GOOD);
        for (Comparison_attr attr : artR.getAttr()) {
            if (attr.isCompleted()) {
                rating = Rating.OK;
                switch (attr.getName()) {
                    case "price":
                        switch (Double.compare(new Wrapper_Price(attr).getVal1(), new Wrapper_Price(attr).getVal2())) {
                            case -1:
                                rating = Rating.GOOD;
                                break;
                            case 0:
                                rating = Rating.OK;
                                break;
                            case 1:
                                rating = Rating.BAD;
                                break;
                        }
                        break;
                    case "name":
                        stringDistance = lev.score(
                                new WrapperString(attr).getVal1(),
                                new WrapperString(attr).getVal2());
                        if (stringDistance > 0.7) {
                            rating = Rating.GOOD;
                        }
                        if (stringDistance < 0.3) {
                            rating = Rating.BAD;
                        }
                        attr.setStringDistance(stringDistance);
                        break;
                    default:
                        break;

                }
                attr.setRating(rating);
            }
        }

    }

}
