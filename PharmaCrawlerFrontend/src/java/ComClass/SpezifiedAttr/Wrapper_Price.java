/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ComClass.SpezifiedAttr;

import static ComClass.SpezifiedAttr.WrapperOutput.LOG;
import com.mysql.jdbc.Blob;
import java.sql.SQLException;
import java.util.logging.Level;

/**
 *
 * @author lasse
 */
public class Wrapper_Price extends WrapperOutput<Double> {

    public Wrapper_Price(Comparison_attr attr) {
        super(attr);
    }

    @Override
    protected Double getVal(Object obj) {

        if (obj instanceof Blob) {
            try {
                Blob blob = (Blob) obj;
                byte[] bdata = blob.getBytes(1, (int) blob.length());
                obj = new Double(new String(bdata));
            } catch (SQLException ex) {
                //Exception weitergeben
                LOG.log(Level.SEVERE, null, ex);
            }
        }
        if(obj instanceof String){
            obj =  new Double((String) obj);
        }
//        if(result>=0){
//            
//        }
        return (Double) obj;
    }

}
