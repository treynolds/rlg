/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * RwTranslationDialog.java
 *
 * Created on Apr 29, 2011, 8:19:30 AM
 */

package rw;

import java.awt.Color;
import java.awt.Font;
import java.awt.print.PrinterException;
import javax.swing.JTextPane;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.util.HashMap;
import javax.swing.JOptionPane;


/**
 *
 * @author Ted
 */
public class RwTranslationDialog extends javax.swing.JDialog {

    /** Creates new form RwTranslationDialog */
    public RwTranslationDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(parent);
        originalTextPane.setText("None Yet...");
        translationTextPane.setText("None Yet...");
        this.parent = (RandomWords)parent;
        contractions.put("aren\'t", "are not");
        contractions.put("can\'t","cannot");
        contractions.put("couldn't","could not");
        contractions.put("didn't","did not");
        contractions.put("don\'t","do not");
        contractions.put("hadn\'t","had not");
        contractions.put("hasn\'t","has not");
        contractions.put("haven\'t","have not");
        contractions.put("he\'d","he would");
        contractions.put("he\'ll","he will");
        contractions.put("he\'s","he is");
        contractions.put("i\'d","i would");
        contractions.put("i\'ll","i will");
        contractions.put("i\'m","i am");
        contractions.put("i\'ve","i have");
        contractions.put("isn\'t","is not");
        contractions.put("it\'s","it is");
        contractions.put("let\'s","let us");
        contractions.put("mightn\'t","might not");
        contractions.put("musn\'t","must not");
        contractions.put("shan\'t","shall not");
        contractions.put("she\'d","she would");
        contractions.put("she\'s","she is");
        contractions.put("shouldn\'t","should not");
        contractions.put("that\'s","that is");
        contractions.put("there\'s","there is");
        contractions.put("they\'d","they would");
        contractions.put("they\'ll","they will");
        contractions.put("they\'re","they are");
        contractions.put("they\'ve","they have");
        contractions.put("we\'d","we would");
        contractions.put("we\'re","we are");
        contractions.put("we\'ve","we have");
        contractions.put("weren\'t","were not");
        contractions.put("what\'ll","what will");
        contractions.put("what\'re","what are");
        contractions.put("what\'s","what is");
        contractions.put("what\'ve","what have");
        contractions.put("where\'s","where is");
        contractions.put("who\'ll","who will");
        contractions.put("who\'re","who are");
        contractions.put("who\'s","who is");
        contractions.put("who\'ve","who have");
        contractions.put("won\'t","will not");
        contractions.put("wouldn\'t","would not");
        contractions.put("you\'d","you had");
        contractions.put("you\'ll","you will");
        contractions.put("you\'re","you are");
        contractions.put("you\'ve","you have");
        try{
            setJTextPaneFont(translationTextPane, new Font((String)((RandomWords)parent).getWritingSystem().get("Font"),Font.PLAIN,30),Color.black);
        }
        catch(NullPointerException npe){

        }
        punctuationMap = (HashMap)((RandomWords)parent).getWritingSystem().get("Punctuation");
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        contractions = new java.util.HashMap();
        originalLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        originalTextPane = new javax.swing.JTextPane();
        translationLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        translationTextPane = new javax.swing.JTextPane();
        translateButton = new javax.swing.JButton();
        transliterateButton = new javax.swing.JButton();
        doneButton = new javax.swing.JButton();
        PrintButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Translate Text");
        setMinimumSize(new java.awt.Dimension(640, 640));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        originalLabel.setText("Original");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.05;
        gridBagConstraints.insets = new java.awt.Insets(11, 10, 0, 0);
        getContentPane().add(originalLabel, gridBagConstraints);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(196, 80));
        jScrollPane1.setViewportView(originalTextPane);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(6, 10, 0, 10);
        getContentPane().add(jScrollPane1, gridBagConstraints);

        translationLabel.setText("Translation: Pronunciation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.05;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        getContentPane().add(translationLabel, gridBagConstraints);

        jScrollPane2.setViewportView(translationTextPane);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(6, 10, 0, 10);
        getContentPane().add(jScrollPane2, gridBagConstraints);

        translateButton.setText("Translate");
        translateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                translateButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.05;
        gridBagConstraints.insets = new java.awt.Insets(6, 10, 11, 0);
        getContentPane().add(translateButton, gridBagConstraints);

        transliterateButton.setText("Transliterate");
        transliterateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transliterateButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.05;
        gridBagConstraints.insets = new java.awt.Insets(6, 10, 11, 0);
        getContentPane().add(transliterateButton, gridBagConstraints);

        doneButton.setText("Done");
        doneButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doneButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.weighty = 0.05;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        getContentPane().add(doneButton, gridBagConstraints);

        PrintButton.setText("Print...");
        PrintButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PrintButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.05;
        gridBagConstraints.insets = new java.awt.Insets(6, 10, 11, 0);
        getContentPane().add(PrintButton, gridBagConstraints);

        jLabel1.setText("Translation: Written");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.05;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        getContentPane().add(jLabel1, gridBagConstraints);

        jScrollPane3.setViewportView(jTextPane1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        getContentPane().add(jScrollPane3, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void doneButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doneButtonActionPerformed
        dispose();
    }//GEN-LAST:event_doneButtonActionPerformed

    private void translateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_translateButtonActionPerformed
        System.out.println();
        String[] original = originalTextPane.getText().split(" ");
        StringBuffer translated = new StringBuffer();
        StringBuffer pronunciation = new StringBuffer();
        RwDictionary dictionary = ((RandomWords)parent).getDictionary();
        RwDictionaryEntry rwe = new RwDictionaryEntry();
        for (int increment = 0; increment < original.length; increment ++){
            String s = original[increment];
            String t = s.toLowerCase();
            s = s.toLowerCase().replaceAll("\\W", "");
            if (s.length() == 0){
                continue;
            }
            if (t.length() == 1 && isPunctuation(t.charAt(0))){
                String t2 = punctuationMap.get(t) + (punctuationMap.get(" ") + "");
                translated.append(t2);
                pronunciation.append(t2);
                continue;
            }
            if(isNumber(t)){
                translated.append(t);
                translated.append(" ");
                pronunciation.append(t);
                pronunciation.append(" ");
                continue;
            }
            int b = t.length() >= 3 ? 2 : t.length();
            // copy over leading punctuation
            for(int a = 0; a < b; a ++){
                if(isPunctuation(t.charAt(a))){
                    String z = punctuationMap.get(t.charAt(a)+"")+"";
                    translated.append(z);
                    pronunciation.append(z);
                }
            }
            //expand contractions
            if(contractions.containsKey(t)){
                String[] u = ((String)contractions.get(t)).split(" ");
                rwe = dictionary.findFromDefinition(u[0]);
                System.out.print(rwe.getDefinition() + " ");
                String u2= rwe.getWrittenForm() + (punctuationMap.get(" ")+"");
                String u3= rwe.getMeaning() + " ";
                translated.append(u2);
                pronunciation.append(u3);
                rwe = dictionary.findFromDefinition(u[1]);
                System.out.print(rwe.getDefinition() + " ");
                translated.append(rwe.getWrittenForm());
                pronunciation.append(rwe.getMeaning());
                continue;
            }
            rwe = dictionary.findFromDefinition(s);
            System.out.print(rwe.getDefinition() + " ");
            translated.append(rwe.getWrittenForm());
            pronunciation.append(rwe.getMeaning());
            if (t.length() == 1){
                translated.append(punctuationMap.get(" "));
                pronunciation.append(" ");
                continue;
            }
            //copy over trailing punctuation
            for(int a = 2; a > 0; a --){
                if(isPunctuation(t.charAt(t.length() - a))){
                    String z = punctuationMap.get(t.charAt(t.length() - a)+"")+"";
                    translated.append(z);
                    pronunciation.append(t.charAt(t.length()-a)+"");
                }
            }
            translated.append((punctuationMap.get(" ")+""));
            pronunciation.append(" ");
        }
        translationTextPane.setText(translated.toString().trim());
        jTextPane1.setText(pronunciation.toString());
    }//GEN-LAST:event_translateButtonActionPerformed

    private void transliterateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transliterateButtonActionPerformed
        //System.out.println("Transliterating");
        String[] original = originalTextPane.getText().split(" ");
        StringBuffer translated = new StringBuffer();
        RwDictionary dictionary = ((RandomWords)parent).getDictionary();
        RwDictionaryEntry rwe = new RwDictionaryEntry();
        for (int increment = 0; increment < original.length; increment ++){
            String s = original[increment];
            String t = s.toLowerCase();
            s = s.toLowerCase().replaceAll("\\W", "");
            if (s.length() == 0){
                continue;
            }
            if (t.length() == 1 && isPunctuation(t.charAt(0))){
                String t2 = punctuationMap.get(t) + (punctuationMap.get(" ") + "");
                translated.append(t2);
                continue;
            }
            if(isNumber(t)){
                translated.append(t);
                translated.append(" ");
                continue;
            }
            int b = t.length() >= 3 ? 2 : t.length();
            // copy over leading punctuation
            for(int a = 0; a < b; a ++){
                if(isPunctuation(t.charAt(a))){
                    String z = punctuationMap.get(t.charAt(a)+"")+"";
                    translated.append(z);
                }
            }
            //expand contractions
            if(contractions.containsKey(t)){
                String[] u = ((String)contractions.get(t)).split(" ");
                rwe = dictionary.findFromDefinition(u[0]);
                String u2= rwe.getWrittenForm() + (punctuationMap.get(" ")+"");
                translated.append(u2);
                rwe = dictionary.findFromDefinition(u[1]);
                translated.append(rwe.getTransliteration());
                continue;
            }
            rwe = dictionary.findFromDefinition(s);
            translated.append(rwe.getTransliteration());
            if (t.length() == 1){
                translated.append(punctuationMap.get(" "));
                continue;
            }
            //copy over trailing punctuation
            for(int a = 2; a > 0; a --){
                if(isPunctuation(t.charAt(t.length() - a))){
                    String z = punctuationMap.get(t.charAt(t.length() - a)+"")+"";
                    translated.append(z);
                }
            }
            translated.append((punctuationMap.get(" ")+""));
        }
        translationTextPane.setText(translated.toString().trim());
        /*HashMap phonemes = (HashMap)((RandomWords)parent).getWritingSystem().get("Phonemes");
        HashMap glyphUses = (HashMap)((RandomWords)parent).getWritingSystem().get("GlyphUseMap");
        HashMap punct = (HashMap)((RandomWords)parent).getWritingSystem().get("Punctuation");
        int glyphsPerLetter = 0;
        if(((HashMap)((RandomWords)parent).getWritingSystem()).containsKey("GlyphsPerLetter")){
            glyphsPerLetter = Integer.parseInt(((RandomWords)parent).getWritingSystem().get("GlyphsPerLetter") + "");
        }
        if(((RandomWords)parent).getWritingSystem().get("System").equals("Alphabet")){
            String orig = originalTextPane.getText();
            String trans = "";
            int ucaseVal = 0;
            int lcaseVal = 0;
            int initVal = 0;
            int medVal = 0;
            int finVal = 0;
            int isoVal = 0;
            for (int q = 0; q < glyphsPerLetter; q++) {
                if (glyphUses.get("Glyph_" + (q + 1)).equals("Upper")) {
                    ucaseVal = q;
                }
                if (glyphUses.get("Glyph_" + (q + 1)).equals("Lower")) {
                    lcaseVal = q;
                }
                if (glyphUses.get("Glyph_" + (q + 1)).equals("Initial")) {
                    initVal = q;
                }
                if (glyphUses.get("Glyph_" + (q + 1)).equals("Medial")) {
                    medVal = q;
                }
                if (glyphUses.get("Glyph_" + (q + 1)).equals("Final")) {
                    finVal = q;
                }
                if (glyphsPerLetter > 3
                        && glyphUses.get("Glyph_" + (q + 1)).equals("Isolated")) {
                    isoVal = q;
                }
            }
            char key;
            boolean initial = true;
            String peekAhead = "";
            for (int a = 0; a < orig.length(); a ++){
                key = orig.charAt(a);
                String skey = (key+"").toLowerCase();
                if((a+1)<orig.length()){
                    peekAhead=orig.charAt(a+1)+"";
                }
                String[] lets = ((StringBuilder)phonemes.get(skey)+"").split(" ");
                if(isPunctuation(key)){
                    trans += punct.get(key+"");
                } else if(Character.isUpperCase(key) && glyphUses.containsValue("Upper")){
                    trans += lets[ucaseVal];
                } else if (Character.isLowerCase(key) && glyphUses.containsValue("Lower")){
                    trans += lets[lcaseVal];
                } else if (initial && (peekAhead.equals(" ") || isPunctuation(peekAhead.charAt(0)) || a == orig.length())){
                    trans += lets[isoVal];
                    initial = true;
                } else if (initial){
                    initial = false;
                    trans += lets[initVal];
                } else if (peekAhead.equals(" ") || isPunctuation(peekAhead.charAt(0)) || a == orig.length()){
                    trans += lets[finVal];
                    initial = true;
                } else {
                    trans += lets[medVal];
                }
            }
            translationTextPane.setText(trans);
        }
        if(((RandomWords)parent).getWritingSystem().get("System").equals("Abjad")){

        }
        if(((RandomWords)parent).getWritingSystem().get("System").equals("Abugida")){
            String orig = originalTextPane.getText();
            StringBuilder trans = new StringBuilder();
            for (int a = 0; a < orig.length(); a ++){
                String key = orig.charAt(a)+"";
                key=key.toLowerCase();
                if(isPunctuation(key.charAt(0))){
                    trans.append(punct.get(key));
                } else {
                    trans.append((phonemes.get(key)+"").trim());
                }
            }
            translationTextPane.setText(trans.toString());
        }
        if(((RandomWords)parent).getWritingSystem().get("System").equals("Syllabary")){

        }
        if(((RandomWords)parent).getWritingSystem().get("System").equals("Multigraphic")){
            String orig = originalTextPane.getText();
            int gpg = Integer.parseInt(((RandomWords)parent).getWritingSystem().get("GraphemesPerGlyph") + "");
            boolean enders = phonemes.containsKey("Enders");
            int grapheme = 0;
            StringBuilder trans = new StringBuilder();
            for (int a = 0; a < orig.length(); a++){
                String key = (orig.charAt(a)+"").toLowerCase();
                if(isPunctuation(key.charAt(0))){
                    if(grapheme < gpg - 1 && enders){
                        System.out.print(grapheme);
                        String[] g = (phonemes.get("Enders")+"").split(" ");
                        trans.append(g[(gpg-1)-grapheme]);
                    }
                    trans.append(punct.get(key));
                    grapheme = 0;
                } else {
                    String[] glyph = (phonemes.get(key)+"").split(" ");
                    trans.append(glyph[grapheme]);
                    grapheme++;
                    if(grapheme >= gpg){
                        grapheme = 0;
                    }
                }
            }
            translationTextPane.setText(trans.toString());
        }*/
    }//GEN-LAST:event_transliterateButtonActionPerformed

    private void PrintButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PrintButtonActionPerformed
        try {
            boolean complete = translationTextPane.print();
            if(complete){
                JOptionPane.showMessageDialog(parent, "Done Printing", "Printer", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(parent, "Printing", "Printer", JOptionPane.ERROR_MESSAGE);
            }
        }
        catch (PrinterException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
    }//GEN-LAST:event_PrintButtonActionPerformed

    private boolean isPunctuation(char c){
        return !Character.isLetter(c);
    }

    private boolean isNumber(String s){
        boolean isnum = true;
        try{
            Double.parseDouble(s);
        }
        catch(NumberFormatException e){
            isnum = false;
        }
        return isnum;
    }
    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                RwTranslationDialog dialog = new RwTranslationDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    /**
     * Utility method for setting the font and color of a JTextPane. The
     * result is roughly equivalent to calling setFont(...) and
     * setForeground(...) on an AWT TextArea.
     */
    public static void setJTextPaneFont(JTextPane jtp, Font font, Color c) {
        // Start with the current input attributes for the JTextPane. This
        // should ensure that we do not wipe out any existing attributes
        // (such as alignment or other paragraph attributes) currently
        // set on the text area.
        MutableAttributeSet attrs = jtp.getInputAttributes();

        // Set the font family, size, and style, based on properties of
        // the Font object. Note that JTextPane supports a number of
        // character attributes beyond those supported by the Font class.
        // For example, underline, strike-through, super- and sub-script.
        StyleConstants.setFontFamily(attrs, font.getFamily());
        StyleConstants.setFontSize(attrs, font.getSize());
        StyleConstants.setItalic(attrs, (font.getStyle() & Font.ITALIC) != 0);
        StyleConstants.setBold(attrs, (font.getStyle() & Font.BOLD) != 0);

        // Set the font color
        StyleConstants.setForeground(attrs, c);

        // Retrieve the pane's document object
        StyledDocument doc = jtp.getStyledDocument();

        // Replace the style for the entire document. We exceed the length
        // of the document by 1 so that text entered at the end of the
        // document uses the attributes.
        doc.setCharacterAttributes(0, doc.getLength() + 1, attrs, false);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton PrintButton;
    private java.util.HashMap contractions;
    private javax.swing.JButton doneButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JLabel originalLabel;
    private javax.swing.JTextPane originalTextPane;
    private javax.swing.JButton translateButton;
    private javax.swing.JLabel translationLabel;
    private javax.swing.JTextPane translationTextPane;
    private javax.swing.JButton transliterateButton;
    // End of variables declaration//GEN-END:variables
    private RandomWords parent;
    private HashMap punctuationMap = new HashMap();
}
