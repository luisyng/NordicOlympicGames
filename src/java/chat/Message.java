package chat;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import javax.servlet.http.HttpServletRequest;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * Representation of a chat message
 */
public class Message {
    
    private final static String FROM = "f";
    private final static String TO = "t";
    private final static String MESSAGE = "m";
    private final static String NUMBER = "n";
    
    private String from;
    private String to;
    private String message;
    private int number;

    /**
     * 
     * @param request <code>HttpServletRequest<code/> containing information about the sender, receiver and the content of the message 
     */
    public Message(HttpServletRequest request) {
        this(request.getParameter("from"), request.getParameter("to"), 
                request.getParameter("message"), 1);
    }
    /**
     * Represents a chat message
     * @param from username of the sender
     * @param to username of the receiver
     * @param message content of the message
     * @param number not used for now. Could be used to order messages with some kind of logic order, or validation.
     */
    public Message(String from, String to, String message, int number) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.number = number;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * 
     * @return message representation in JSON, in order to communicate with the chat javascript running in the client.
     * @throws JSONException 
     */
    public JSONObject toJSON() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put(FROM, from);
        jo.put(TO, to);
        jo.put(MESSAGE, message);
        jo.put(NUMBER, number);
        return jo;     
    }

    @Override
    public String toString() {
        return "Message{" + "from=" + from + ", to=" + to 
                + ", message=" + message + ", number=" + number + '}';
    } 
}
