package rw;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.*;
import javax.swing.JOptionPane;
/**
 *
 * @author Ted
 */
public class RwDictionaryEntry {
    private String definition;
    private String meaning;
    private String pos;
    private String root;
    private int syllableCount;
    private Vector syllableBreaks = new Vector();
    private String writtenForm;
    private String pttag;
    private int frequency;
    private String transliteration;

    public RwDictionaryEntry(String[] stuff){
        pos = stuff[0];
        definition = stuff[1];
        root = stuff[2];
        meaning = "";
        if(stuff.length > 4){
            syllableCount = Integer.parseInt(stuff[3]);
        }
        if(stuff.length > 5){
            pttag = stuff[4];
            frequency = Integer.parseInt(stuff[5]);
        }
    }

    public RwDictionaryEntry(){

    }

    public void setPtTag(String tag){
        pttag = tag;
    }

    public void setFrequency(int freq){
        frequency  = freq;
    }

    public int getFrequency(){
        return frequency;
    }

    public String getPtTag(){
        return pttag;
    }

    public void setRoot(String rt){
        root = rt;
    }

    public void setPos(String pos){
        this.pos=pos;
    }

    public String getPos(){
        return pos;
    }

    public String getRoot(){
        return root;
    }

    public int getSyllableCount(){
        return syllableCount;
    }

    public void setSyllableCount(int syllables){
        syllableCount = syllables;
    }

    public void setSyllableCount(String syllables){
        syllableCount = Integer.parseInt(syllables);
    }

    public void setSyllableBreak(int syllableBreak){
        syllableBreaks.addElement(syllableBreak);
    }

    public void insertSyllableBreak(int position){
        syllableBreaks.insertElementAt(position, 0);
    }

    public int getLastSyllableBreak(){
        return ((Integer)syllableBreaks.elementAt(syllableBreaks.size()-1)).intValue();
    }

    public int getFirstSyllableBreak(){
        return ((Integer)syllableBreaks.elementAt(1)).intValue();
    }

    public void setDefinition(String define){
        definition = define;
    }

    public void setMeaning(String mean){
        meaning = mean;
    }

    public String getMeaning(){
        return meaning;
    }

    public String getDefinition(){
        return definition;
    }

    public void setWrittenForm(String wf){
        writtenForm = wf;
    }
    public String getWrittenForm(){
        return writtenForm;
    }

    public boolean equals(RwDictionaryEntry rde){
        if(this.meaning.equals(rde.getMeaning())){
            System.out.println("matched!!!");
            return true;
        } else {
            System.out.println("not matched!!!");
            return false;
        }
    }

    public void setTransliteration(String trans){
        transliteration = trans;
    }

    public String getTransliteration(){
        return transliteration;
    }

    public String generateMeaning(String syllables, Vector vowels,
            Vector consonants, String pos, HashMap rules, String original, int maxSylls){
        String word="";
        syllableBreaks.clear();
        Vector rule = (Vector)rules.get(pos);
        ArrayList syllablePatterns = new ArrayList();
        syllablePatterns.addAll(Arrays.asList(syllables.split(" ")));
        boolean eachWord = syllablePatterns.get(syllablePatterns.size()-1).equals("0");
        syllablePatterns.remove(syllablePatterns.size()-1);
        boolean syllableSelected = false;
        String syllable = "";
        //if(word.length()==0)
        {
            int f = getFrequency();
            int numberOfSyllables = maxSylls;
            if (f > 10000){
                numberOfSyllables -= 3;
                if(numberOfSyllables < 1){
                    numberOfSyllables = 1;
                }
            }
            if (f > 1000 && f < 10000){
                numberOfSyllables -= 2;
                if(numberOfSyllables < 2){
                    numberOfSyllables = 2;
                }
            }
            if (f > 100 && f < 1000){
                numberOfSyllables -= 1;
                if(numberOfSyllables < 3){
                    numberOfSyllables = 3;
                }
            }
            //int numberOfSyllables=(int)(Math.random()*maxSylls)+1;
            for(int ns = 0; ns < numberOfSyllables; ns++){
                setSyllableBreak(word.length());
                if(!syllableSelected){
                    int sylpat = ((int)(Math.random()*syllablePatterns.size()));
                    syllable = (String)syllablePatterns.get(sylpat);
                    if(eachWord){
                        syllableSelected = true;
                    }
                }
                for(int a = 0; a < syllable.length(); a++){
                    if(syllable.charAt(a) == 'C'){
                        int b=(int)(Math.random()*consonants.size());
                        word += consonants.elementAt(b);
                    }
                    if(syllable.charAt(a) == 'V'){
                        int b=(int)(Math.random()*vowels.size());
                        word += vowels.elementAt(b);
                    }
                    if(syllable.charAt(a) == '['){
                        if(syllable.indexOf(']',a) >= 0){
                            int z = syllable.indexOf(']',a);
                            String[] choosables= syllable.substring(a + 1, z).split(",");
                            int b = (int)(Math.random()*choosables.length);
                            word += choosables[b];
                            a = z;
                        } else {
                            JOptionPane.showMessageDialog(null, "Illegal Syllable Structure\n expected ]", "Oops!",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                        a = syllable.indexOf(']',a);
                    }
                    if(syllable.charAt(a) == '{'){
                        if(syllable.indexOf('}',a) > 0){
                            char let=syllable.charAt(a + 1);
                            if(word.charAt(word.length()-1) == let){
                                int z = syllable.indexOf('}',a);
                                String[] choosables = syllable.substring(syllable.indexOf(":", a + 1) + 1, z).split(",");
                                int x = (int)(Math.random()*choosables.length);
                                word += choosables[x];
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Illegal Syllable Structure\n expected }", "Oops!",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                       a= syllable.indexOf('}',a);
                    }
                    if(syllable.charAt(a) == '('){
                        int percent = 50;
                        String g=syllable.substring(a,syllable.indexOf(')',a));
                        if(g.indexOf('.')>0){
                            percent=(int)(Float.parseFloat(g.substring(g.indexOf('.')))*100);
                        }
                        if(syllable.indexOf(')',a) > 0){
                            if(syllable.charAt(a+1) == 'C'){
                                int pct = (int)(Math.random()*100);
                                int letter = (int)(Math.random()*consonants.size());
                                if(pct <= percent){
                                    word += consonants.get(letter);
                                }
                            }
                            if(syllable.charAt(a+1) == 'V'){
                                int x = (int)(Math.random()*100);
                                int y = (int)(Math.random()*vowels.size());
                                if(x <= percent){
                                   word += vowels.get(y);
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Illegal Syllable Structure\n expected )", "Oops!",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                        //System.out.println(a);
                        a= syllable.indexOf(')',a);
                    }
                    char z =syllable.charAt(a);
                    if(Character.isLowerCase(z) || isPunct(z))
                        word += syllable.charAt(a);
                }
            }
            if(((Boolean)rule.elementAt(0)).booleanValue()){
                int exception = (int)((Math.random()*100) + 1);
                if (exception > ((Integer)rule.elementAt(2)).intValue()){
                    //System.out.println(syllableBreaks.size());
                    if(((Boolean)rule.elementAt(3)).booleanValue()){
                        if(((Boolean)rule.elementAt(4)).booleanValue()){
                            insertSyllableBreak(rule.elementAt(1).toString().length());
                            word = rule.elementAt(1) + word;
                        } else {
                            if (syllableBreaks.size()<=1){
                                insertSyllableBreak(word.length());
                                word = rule.elementAt(1) + word;
                            } else {
                                word = (String)rule.elementAt(1) + word.substring(getFirstSyllableBreak());
                            }
                        }
                    } else {
                        if(((Boolean)rule.elementAt(4)).booleanValue()){
                            setSyllableBreak(word.length());
                            word = word + rule.elementAt(1);
                        } else {
                            if (syllableBreaks.size()<=1){
                                setSyllableBreak(word.length());
                                word = word + rule.elementAt(1);
                            } else {
                                word = word.substring(0,getLastSyllableBreak()) + (String)rule.elementAt(1);
                            }
                        }
                    }
                } else {
                    //System.out.println("Exception Found: " + exception + " " + word + " unchanged, for "+original);
                }
            }
        }
        /*if(this.pttag.equals("nnp") || this.pttag.equals("nnps")){
            word = this.definition;
            //System.out.println(this.pttag + " Setting word to " + word);
        }//*/
        return word;
    }

    public String applyPosRule(RwDictionaryEntry rde, HashMap rules){
        String word =rde.getMeaning();
        String partOfSpeech = rde.getPos();
        Vector rule = (Vector)rules.get(partOfSpeech);
        if(((Boolean)rule.elementAt(0)).booleanValue()){
            int exception = (int)(Math.random()*100);
            if (exception >= ((Integer)rule.elementAt(2)).intValue()){
                if(((Boolean)rule.elementAt(3)).booleanValue()){
                    if(((Boolean)rule.elementAt(4)).booleanValue()){
                        word = rule.elementAt(1) + word;
                    } else {
                        if (syllableBreaks.size()<2){
                            word = rule.elementAt(1) + word;
                        } else {
                            word = (String)rule.elementAt(1) + word.substring(getFirstSyllableBreak());
                        }
                    }
                } else {
                    if(((Boolean)rule.elementAt(4)).booleanValue()){
                        word = word + rule.elementAt(1);
                    } else {
                        if (syllableBreaks.size()<2){
                            word = word + rule.elementAt(1);
                        } else {
                            word = word.substring(0,getLastSyllableBreak()) + (String)rule.elementAt(1);
                        }
                    }
                }
            } else {
                System.out.println("Exception Found: " + exception);
            }
        }
        return word;
    }

    public boolean isPunct(char z){
        String punct="!\"#$%&\'*+,-./:;<=>?`abcdefghijklmnopqrstuvwxyz";
        int a = punct.indexOf(z);
        return (a >= 0);
    }

    public String toString(){
        return definition + ": " + meaning + ", " + pos + ", " +  root + ", " + syllableCount + ", " + rwHexStringConverter.convertTo(syllableBreaks.toString()) + ", " +
                writtenForm + ", " + pttag + ", " + frequency + ", " + transliteration;
    }

    public String toDefString(){
        return pos + ", " + definition + ", " + root + ", " + (int)(definition.length()/2) + ", " + pttag + ", " + frequency;
    }

    public String toHexCodedString(){
        String z=definition + ": " + meaning + ", " + pos + ", " +  root + ", " + syllableCount + ", " + rwHexStringConverter.convertTo(syllableBreaks.toString()) + ", " +
                writtenForm + ", " + pttag + ", " + frequency + ", " + transliteration;
        z=rwHexStringConverter.convertTo(z);
        return z;
    }
}
