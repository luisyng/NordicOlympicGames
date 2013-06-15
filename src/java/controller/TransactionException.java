/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

/**
 *
 * @author Albert Yera
 */
public class TransactionException extends Exception {

    public static final String ROLLBACK ="ROLLBACK";
    public static final String PERSIST="PERSIST";
    
    /**
     * Creates a new instance of <code>TransactionException</code> without detail message.
     */
    public TransactionException() {
    }

    /**
     * Constructs an instance of <code>TransactionException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public TransactionException(String msg) {
        super(msg);
    }
}
