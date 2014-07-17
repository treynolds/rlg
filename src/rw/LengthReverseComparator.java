/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rw;

/**
 *
 * @author Ted
 */
public class LengthReverseComparator implements java.util.Comparator{
    public int compare(Object obj1, Object obj2){
        String obs1 = obj1+"";
        String obs2 = obj2+"";
        if (obs1.length() < obs2.length()){
            return 1;
        } else if (obs1.length() > obs2.length()){
            return -1;
        } else {
            return 0;
        }
    }
}
