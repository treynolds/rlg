/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rw;

/**
 *
 * @author tedr
 */
public class rwB64C {

    public static String encode(String raw){
        String enc = "";
        try {
            enc = javax.xml.bind.DatatypeConverter.printBase64Binary(raw.getBytes("UTF-8"));
        } catch (java.io.UnsupportedEncodingException ex){
            enc = javax.xml.bind.DatatypeConverter.printBase64Binary(raw.getBytes());
        }
        return enc;
    }

    public static String decode(String raw){
        return new String(javax.xml.bind.DatatypeConverter.parseBase64Binary(raw));
    }
}
