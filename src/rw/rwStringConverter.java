/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rw;

import java.io.UnsupportedEncodingException;

/**
 *
 * @author tedr
 */
public class rwStringConverter {

    private static String HEX_VALUES = "0123456789ABCDEF";

    public static String convertToHex(String input){
        StringBuilder sb = new StringBuilder();
        try{
            byte bs[] = input.getBytes("UTF-8");
            for( byte b:bs ){
                sb.append(HEX_VALUES.charAt((b & 0xF0) >> 4)).append(HEX_VALUES.charAt((b & 0x0F)));
            }
        }
        catch(UnsupportedEncodingException ex){
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return sb.toString();
    }

    public static String convertFromHex(String input){
        //System.out.println(input);
        byte bs[] = new byte[input.length()/2];
        for(int c = 0; c < input.length() - 1; c += 2){
            bs[c/2] = (byte)((HEX_VALUES.indexOf(input.charAt(c)) * 16) +
                    HEX_VALUES.indexOf(input.charAt(c + 1)));
        }
        String output = "";
        try{
            output = new String(bs, 0, bs.length, "UTF-8");
        }
        catch(UnsupportedEncodingException ex){
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return output;
    }

     public static String convertTo64(String raw){
        String enc = "";
        try {
            enc = javax.xml.bind.DatatypeConverter.printBase64Binary(raw.getBytes("UTF-8"));
        } catch (java.io.UnsupportedEncodingException ex){
            enc = javax.xml.bind.DatatypeConverter.printBase64Binary(raw.getBytes());
        }
        return enc;
    }

    public static String convertFrom64(String raw){
        return new String(javax.xml.bind.DatatypeConverter.parseBase64Binary(raw));
    }

    public static String convertTo264(String input){
        return convertTo64(convertTo64(input));
    }

    public static String convertFrom264(String input){
        return convertFrom64(convertFrom64(input));
    }

    public static void main(String args[]){
        System.out.println(convertTo64(args[0]));
        System.out.println(convertTo264(args[0]));
        System.out.println(convertFrom64(convertTo264(args[0])));
        System.out.println(convertFrom264(convertTo264(args[0])));
    }

}
