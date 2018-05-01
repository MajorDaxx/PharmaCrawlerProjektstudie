package WebApi;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import ComClass.SpezifiedAttr.Comparison_attr;
import ComClass.Product;
import ComClass.Request;
import ComClass.RequestSet;
import ComClass.SpezifiedAttr.WrapperString;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jdk.management.resource.internal.WrapInstrumentation;

/**
 *
 * @author lasse
 */
@WebServlet(urlPatterns = {"/HTTP_API"})
public class HTTP_API extends HttpServlet {

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

        //Gibt die Anfrage weiter an die JSON API! Zeigt die Antwort als 
        //Evtl. einbauen eine Wartebildschirm
        JSON_API json_api = new JSON_API();

        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet NewServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet NewServlet at " + request.getContextPath() + "</h1>");

            out.println("</body>");
            out.println("</html>");
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
        processRequest(request, response);
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
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
//            Requests req = Requests.getRequest(request.getReader());
//            ResponseSet r =DataModul.DataApi.getApi().get(req);
            try {

                //out.append(parseRequest(request).toString());
                RequestSet reqSet = RequestSet.parseRequest(request);
                DataModul.DataApi.getApi().get(reqSet);
                out.append("<html>")
                        .append("<head>")
                        .append("<link rel=\"stylesheet\" href=\"style.css\" type=\"text/css\"/>")
                        .append("</head>")
                        .append("<body>")
                        .append("<h1>")
                        .append("Resultat Seite !!!")
                        .append("</h1>")
                        
                        ;
                for (Request request12 : reqSet) {
                    Product result = request12.getArtikel();
                    out.append("<div class = \"" + result.getTotal() + " Product\">")
                            
                            .append("<table>");
                          out.append("<tr><th>Attribut</th><th>Ref_Product</th><th>Result Product</th></td>");
                    result.getAttr().forEach((Comparison_attr attr) -> {
                        out.append("<tr class="+attr.getRating()+">")
                                .append("<th>")
                                .append(attr.getName())
                                .append("</th>")
                                .append("<td>")
                                .append(attr.getVal1() != null ? new WrapperString(attr).getVal1() : "notFound")
                                .append("</td>")
                                .append("<td>")
                                .append(attr.getVal2() != null ? new WrapperString(attr).getVal2() : "notFound")
                                .append("</td>")
                                .append("<td>")
                                .append(attr.getStringDistance()!=null? attr.getStringDistance().toString():"")
                                .append("</td>");
                    });
                    out.append("</table>").append("</div>");
                }
                out.append("</body>");
                out.print("</html>");
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
