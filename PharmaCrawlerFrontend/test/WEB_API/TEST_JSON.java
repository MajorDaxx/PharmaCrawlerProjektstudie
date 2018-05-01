/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WEB_API;

import java.awt.BorderLayout;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author lasse
 */
public class TEST_JSON {

    int a;
    int b;
    byte c;

    public static void main(String[] args) throws IOException, SQLException {
//        Field[] f_ = TEST_JSON.class.getDeclaredFields();
//        for (Field f:TEST_JSON.class.getDeclaredFields()){
//            System.out.println(f.getType().);
//        }
//    
 File f = new File("../LOG/LOG_PHARMA_"+Thread.currentThread().toString()+".log");
        System.out.println(f.getAbsoluteFile());
 f.mkdirs();
 f.createNewFile();
        Driver d = new com.mysql.jdbc.Driver();
        DriverManager.registerDriver(d);

        Connection con;
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/webcrawler?verifyServerCertificate=false&useSSL=true", "admin", "admin");

        Statement stm = con.createStatement();

        String sql = "Select * from websitescrawler";
        stm.closeOnCompletion();
        ResultSet rS = stm.executeQuery(sql);

        List<String> s = new LinkedList<>();

        boolean last;
        rS.beforeFirst();

        while (rS.next()) {
//            for (int i = 1; i <= 1; i++) {
//                System.out.print(rS.getString(i) + "\t");
//            }

            s.add(rS.getString(1));
        }

        s.forEach((item_) -> {
            System.out.println(item_);
        });

    }

}
