package chat;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import chat.Message;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;

/**
 *
 * @author luis
 */
@WebServlet(name = "ChatServlet", urlPatterns = {"/chat/*"})
public class ChatServlet extends HttpServlet {

    private Map<String, List<Message>> map;

    public ChatServlet() {
        super();
        map = new HashMap<>();
    }

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo().substring(1);
        //System.out.println("Action: " + action);

        switch (action) {
            case "send":
                Message m = new Message(request);
                List<Message> receiverList = map.get(m.getTo());

                // If there is no list, we create it
                if (receiverList == null) {
                    receiverList = new ArrayList<>();
                    map.put(m.getTo(), receiverList);
                }

                // Add the message to the list
                receiverList.add(m);
                break;
            case "heartbeat":
                // Get the list of received messages
                String userName = request.getParameter("user");
                List<Message> userList = map.get(userName);
               // System.out.println(userName);

                // Create response
                response.setContentType("text/json;charset=UTF-8");
                PrintWriter out = response.getWriter();

                try {
                    // To json
                    JSONArray ja = new JSONArray();
                    if (userList != null) {
                        for (Message msg : userList) {
                            ja.put(msg.toJSON());
                        }
                        userList.clear();
                    }                   
                    out.println(ja);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                } finally {
                    out.close();
                }
                break;
        }
    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
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
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
