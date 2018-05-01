/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebApi;

import ComClass.SpezifiedAttr.Comparison_attr;
import ComClass.RequestSet;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author lasse
 */
@WebServlet(urlPatterns = {"/PZN_JSON"})
public class JSON_API extends HttpServlet {

    static void init_invoke() {

    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

//        //Gibt die Anfrage weiter an die JSON API! Zeigt die Aantwort als 
//        DataModul.DataApi.getApi().get(request);
//
//        response.setContentType("text/plain;charset=UTF-8");
//        try (PrintWriter out = response.getWriter()) {
//            /* TODO output your page here. You may use following sample code. */
//
//        }
    }

    @Override
    public void init() throws ServletException {
        try {
            super.init(); //To change body of generated methods, choose Tools | Templates.

            File f = new File("../logs/PHARMACRAWLERLOG/LOG_PHARMA_" + Thread.currentThread().toString() + ".log");
            if (!f.exists()) {
                f.createNewFile();
            }
            Logger.getLogger("").addHandler(new FileHandler(f.getAbsolutePath(), true));
            Logger.getLogger("").setLevel(Level.ALL);
            Logger.getLogger("").severe("LOG ist am Start");
        } catch (IOException ex) {
            Logger.getLogger(JSON_API.class.getName()).log(Level.SEVERE,
                    "Feheler beim initialisieren des LOG Files", ex);
        } catch (SecurityException ex) {
            Logger.getLogger(JSON_API.class.getName()).log(Level.SEVERE,
                    "Sicherheits Feheler beim initialisieren des LOG Files", ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            //Requests req = Requests.getRequest(request.getReader());
            //Response r =DataModul.DataApi.getApi().get(req);
//                
            BufferedReader br = new BufferedReader(request.getReader());

            out.write("GET");
            String s;
            while ((s = br.readLine()) != null) {
                out.write(s);
            }

            out.flush();
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
//            Requests req = Requests.getRequest(request.getReader());
//            ResponseSet r =DataModul.DataApi.getApi().get(req);
            try {
                out.print("JSON wird noch nicht unterstützt");
                //out.append(parseRequest(request).toString());
//                RequestSet reqSet = RequestSet.parseRequest(request);
//                DataModul.DataApi.getApi().get(reqSet);
//                out.append("<html>");
//                out.append("<body>");
//                for (Comparison_Artikel result : responseSet.getResults()) {
//                    out.append("<div class = " + result.getTotal() + ">");
//                    for (Comparison_attr attr : result.getAttr()) {
//                        out.append("<p class =" + attr.getRating() + ">");
//                        out.append(attr.getName());
//                        out.append(attr.getVal1().toString());
//                        out.append("</p>");
//                        if (attr.getVal2() != null) {
//                            out.append(attr.getVal2().toString());
//                        }
//                        out.append("</p>");
//                    }
//                    out.append("</div>");
//                }
//                out.append("</body>").append("</html>");
                
            } catch (Exception | Error ex) {
                out.println("Fehler bei der Anfrage Aufgetreten");
                ex.printStackTrace(out);
            }

        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
