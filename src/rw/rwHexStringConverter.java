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
public class rwHexStringConverter {

    private static String HEX_VALUES = "0123456789ABCDEF";

    public static String convertTo(String input){
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

    public static String convertFrom(String input){
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
}
