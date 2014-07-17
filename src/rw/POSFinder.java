/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rw;

import java.io.*;
import java.util.*;

/**
 *
 * @author Ted
 */
public class POSFinder {

    public static void main(String[] args){
        HashMap hm = new HashMap();
        String[] initials={"V","i","t","N","p","o","h","r","A","v","P","C","!","D","I"};
        String[] values={"Verb","Verb","Verb","Noun","Noun","Noun","Noun","Pronoun","Adjective",
            "Adverb","Preposition","Conjunction","Interjection","Article","Article"};
        HashMap pos = new HashMap();
        for(int a=0; a<initials.length;a++){
            pos.put(initials[a], values[a]);
        }
        try {
            BufferedReader in = new BufferedReader(new FileReader("C:\\reynpos2.csv"));
            PrintWriter out = new PrintWriter(new FileWriter("C:\\RwDictionary.csv"));
            String line;
            String g;
            do{
               line = in.readLine();
               String[] stuff=line.split(",");
               hm.put(stuff[1], stuff[0]);
               g=stuff[1];
            }while(!g.equals("***"));
            in.close();
            System.out.println("done reading main file");
            in = new BufferedReader(new FileReader("C:\\diktionary2.csv"));
            line = "";
            g = "";
            do{
                line = in.readLine();
                line = line.replaceAll("\"", "");
                if (hm.containsKey(line)){
                    System.out.println(line  + " POS = " + hm.get(line));
                    String q = " ";
                    String p = " ";
                    for(int z=0;z<((String)hm.get(line)).length();z++){
                        q="\""+pos.get(((String)hm.get(line)).substring(z, z+1))+"\",\""+line+"\",";
                        if(!p.equals(q)){
                            System.out.println(q);
                            out.println(q);
                            p=q;
                        }
                    }
                } else {
                    System.out.println("oops " +line);
                }
            }while(!line.equals("true"));
            in.close();
            out.close();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

}
