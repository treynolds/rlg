package rw;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.*;
/**
 *
 * @author Ted
 */
public class RwDictionary {
    private Vector<RwDictionaryEntry> words = new Vector<RwDictionaryEntry>();
    public void addEntry(RwDictionaryEntry s){
        words.add(s);
    }
    
    public RwDictionaryEntry getEntryAt(int a){
        return words.elementAt(a);
    }
    
    public void setDefinitonAt(String def, int a){
        words.elementAt(a).setDefinition(def);
    }
    
    public void setMeaningAt(String mean, int a){
        words.elementAt(a).setMeaning(mean);
    }

    public boolean contains(RwDictionaryEntry entry){
        boolean contains=false;
        if(words.contains(entry)){
            contains=true;
        }
        return contains;
    }

    public String getDefiniton(String meaning){
        String definition="";
        RwDictionaryEntry b;
        for(int a=0;a<words.size();a++){
            b=(RwDictionaryEntry)words.elementAt(a);
            if(b.getMeaning().equals(meaning)){
                definition = b.getDefinition();
            }
        }
        return definition;
    }

    public boolean setDefinition(RwDictionaryEntry entry){
        boolean defset=false;
        for(int a=0;a<words.size();a++){
            if(words.elementAt(a).equals(entry)){
                words.elementAt(a).setDefinition(entry.getDefinition());
                defset=true;
            }
        }
        return defset;
    }

    public String getMeaning(String definition){
        String meaning="";
        RwDictionaryEntry b;
        for(int a=0;a<words.size();a++){
            b=(RwDictionaryEntry)words.elementAt(a);
            if(b.getDefinition().equals(definition)){
                meaning=b.getMeaning();
            }
        }
        return meaning;
    }

    public boolean setMeaning(RwDictionaryEntry entry){
        boolean meanset = false;
        for(int a=0;a<words.size();a++){
            if(words.elementAt(a).getDefinition().equals(entry.getDefinition())){
                words.elementAt(a).setMeaning(entry.getMeaning());
                meanset=true;
            }
        }
        return meanset;
    }

    public RwDictionaryEntry findFromDefinition(String definition){
        int low = 0;
        int high = words.size()-1;
        int mid = (high + low) / 2 ;
        //System.out.println(definition);
        while (low <= high){
            mid = (high + low)/2;
            //System.out.println(mid);
            if(definition.compareTo(words.elementAt(mid).getDefinition()) < 0){
                high = mid - 1;
            }
            else if(definition.compareTo(words.elementAt(mid).getDefinition()) > 0)
            {
                low = mid + 1;
            }
            else {
                return words.elementAt(mid);
            }
        }
        return words.elementAt(mid);
    }

    public RwDictionaryEntry findRootFromDefinition(String definition){
        RwDictionaryEntry rde = new RwDictionaryEntry();
        RwDictionaryEntry rwd = new RwDictionaryEntry();
        rde = findFromDefinition(definition);
        rwd = rde;
        //System.out.println(rde +" --> "+definition);
        int location = indexOf(rde);
        int lookBack = 0;
        int lookForward = 0;
        while(lookBack < 11 && !rwd.getRoot().equals("r")){
            rwd=getEntryAt(location - lookBack++);
        }
        if(!rwd.getRoot().equals("r")){
            while(lookForward < 11 && !rwd.getRoot().equals("r")){
                rwd=getEntryAt(location + lookForward++);
            }
        }
        if(rwd.getRoot().equals("r")){
            rde=rwd;
            return rde;
        } else {
            return null;
        }
    }

    public RwDictionaryEntry find(RwDictionaryEntry rde){
        int p = words.indexOf(rde);
        if (p>0){
            return words.elementAt(words.indexOf(rde));
        } else {
            return null;
        }
    }

    public int indexOf(RwDictionaryEntry rde){
        return words.indexOf(rde);
    }

    public void clear(){
        words.clear();
    }

    public int size(){
        return words.size();
    }
}
