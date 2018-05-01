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
public abstract class WrapperOutput<T> {
    static final Logger LOG = Logger.getLogger(WrapperOutput.class.getName());
    
    Comparison_attr attr;

    public WrapperOutput(Comparison_attr attr) {
        this.attr = attr;
    }

    public T getVal1() {
        return getVal(attr.val1);
    }

    public T getVal2() {
        return  getVal(attr.val2);
    }
    
    protected abstract T getVal(Object obj);
}
