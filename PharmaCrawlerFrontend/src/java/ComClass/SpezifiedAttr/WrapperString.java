/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ComClass.SpezifiedAttr;

import com.mysql.jdbc.Blob;
import java.sql.SQLException;
import java.util.logging.Level;

/**
 *
 * @author lasse
 */
public class WrapperString extends WrapperOutput<String> {

    public WrapperString(Comparison_attr attr) {
        super(attr);
    }

    @Override
    protected String getVal(Object obj) {
        if (obj instanceof Blob) {
            try {
                Blob blob = (Blob) obj;
                byte[] bdata = blob.getBytes(1, (int) blob.length());
                return new String(bdata);
            } catch (SQLException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
        if(obj instanceof Double){
            obj = String.valueOf(obj);
        }
        
        return (String) obj.toString();
        
    }

}
