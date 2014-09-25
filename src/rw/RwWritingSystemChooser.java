/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * RwWritingSystemChooser.java
 *
 * Created on Feb 5, 2011, 3:51:50 PM
 */
package rw;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.*;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.helpers.AttributesImpl;
import org.w3c.dom.NodeList;
import org.xml.sax.*;
import javax.xml.xpath.*;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Ted
 */
public class RwWritingSystemChooser extends javax.swing.JDialog {

    /** Creates new form RwWritingSystemChooser */
    public RwWritingSystemChooser(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(parent);
    }

    public RwWritingSystemChooser(java.awt.Frame parent, String title, boolean modal) {
        super(parent, title, modal);
        initComponents();
        setLocationRelativeTo(parent);
    }

    public RwWritingSystemChooser(Frame parent, String title, boolean modal, Vector vowels,
            Vector consonants, HashMap writingSystem) {
        super(parent, title, modal);
        initComponents();
        directionalityButton.setVisible(false);
        abjadDirectionalityButton.setVisible(false);
        abugidaDirectionalityButton.setVisible(false);
        syllabaryDirectionalityButton.setVisible(false);
        setLocationRelativeTo(parent);
        vows = vowels;
        cons = consonants;
        setupSystem(vowels, consonants, writingSystem);
    }

    public void setupSystem(Vector vowels,
            Vector consonants, HashMap writingSystem) {
        int pos = 1;
        Vector vowls = new Vector();
        Vector consnts = new Vector();
        for (int v = 0; v < vowels.size(); v++) {
            vowls.add(new String(vowels.elementAt(v) + ""));
        }
        for (int v = 0; v < consonants.size(); v++) {
            consnts.add(new String(consonants.elementAt(v) + ""));
        }
        String sound = vowels.elementAt(0) + "";
        while (pos < vowls.size()) {
            if (vowls.elementAt(pos).equals(sound)) {
                vowls.remove(pos);
            } else {
                sound = vowls.elementAt(pos) + "";
                pos++;
            }
        }
        pos = 1;
        sound = consnts.elementAt(0) + "";
        while (pos < consnts.size()) {
            if (consnts.elementAt(pos).equals(sound)) {
                consnts.remove(pos);
            } else {
                sound = consnts.elementAt(pos) + "";
                pos++;
            }
        }
        DefaultTableModel dtm4 = (DefaultTableModel) syllabaryTable.getModel();
        String fontname = (String) writingSystem.get("Font");
        Font defaultFont = new Font("LCS-ConstructorII", 0, 14);
        syllabaryTable.setFont(defaultFont);
        alphaTable.setFont(defaultFont);
        abjadTable.setFont(defaultFont);
        abugidaTable.setFont(defaultFont);

        dtm4.setColumnCount(0);
        dtm4.setRowCount(0);
        Vector sconsonants = new Vector(consnts);
        if (!sconsonants.contains("NC")) {
            sconsonants.add("NC");
        }
        dtm4.addColumn(" ", sconsonants);
        vowelNumCombo.removeAllItems();
        consonantNumCombo.removeAllItems();
        for (int a = 0; a < vowls.size(); a++) {
            dtm4.addColumn(vowls.elementAt(a));
            vowelNumCombo.addItem(vowls.elementAt(a));
        }
        dtm4.addColumn("NV");
        vowelNumCombo.addItem("NV");
        for (int a = 0; a < consnts.size(); a++) {
            consonantNumCombo.addItem(consnts.elementAt(a));
        }
        consonantNumCombo.addItem("NC");
        DefaultTableModel dtm = (DefaultTableModel) alphaTable.getModel();
        DefaultTableModel dtm1 = (DefaultTableModel) abjadTable.getModel();
        DefaultTableModel dtm2 = (DefaultTableModel) abugidaTable.getModel();
        DefaultTableModel dtm3 = (DefaultTableModel) multigraphicTable.getModel();
        DefaultTableModel dtm5 = (DefaultTableModel) punctuationTable.getModel();
        DefaultTableModel dtm6 = (DefaultTableModel) presetsTable.getModel();
        dtm.setRowCount(0);
        dtm1.setRowCount(0);
        dtm2.setRowCount(0);
        dtm3.setRowCount(0);
        Vector ds = new Vector();
        ds.addAll(vowls);
        ds.addAll(consnts);
        Object[] ob = ds.toArray();
        Arrays.sort(ob);
        dtm.setRowCount(ob.length);
        dtm1.setRowCount(ob.length);
        dtm2.setRowCount(ob.length);
        dtm3.setRowCount(ob.length);
        HashMap phonemes = (HashMap) writingSystem.get("Phonemes");
        System.out.println(phonemes + "");
        for (int a = 0; a < ob.length; a++) {
            dtm.setValueAt(ob[a], a, 0);
            dtm1.setValueAt(ob[a], a, 0);
            dtm2.setValueAt(ob[a], a, 0);
            dtm3.setValueAt(ob[a], a, 0);
        }
        alphaTable.getColumnModel().getColumn(1).setCellRenderer(tcr2);
        abjadTable.getColumnModel().getColumn(1).setCellRenderer(tcr2);
        abugidaTable.getColumnModel().getColumn(1).setCellRenderer(tcr2);
        multigraphicTable.getColumnModel().getColumn(1).setCellRenderer(tcr2);
        multigraphicTable.getColumnModel().getColumn(2).setCellRenderer(tcr2);
        punctuationTable.getColumnModel().getColumn(1).setCellRenderer(tcr2);
        Vector tabs = new Vector();
        tabs.add("Alphabet");
        tabs.add("Abjad");
        tabs.add("Abugida");
        tabs.add("Syllabary");
        tabs.add("Multigraphic");
        writingSystemPane.setSelectedIndex(tabs.indexOf(writingSystem.get("System")));
        if (writingSystemPane.getSelectedIndex() == 0) {  // Alphabet
            consonants.remove("NV");
            vowels.remove("NC");
            phonemes.remove("NC");
            phonemes.remove("NV");
            fontField.setText(fontname);
            if (writingSystem.get("FontType").equals("borrowed")) {
                borrowedCharsRadio.setSelected(true);
            } else {
                customFontRadio.setSelected(true);
                fontField.setEnabled(true);
                fontField.setEditable(true);
                chooseAlphabetFontButton.setEnabled(true);
            }
            alphaGlyphsPerLetterSpinner.setValue(Integer.parseInt(writingSystem.get("GlyphsPerLetter") + ""));
            for (int a = 0; a < phonemes.size(); a++) {
                String[] z = (phonemes.get(ob[a]) + "").split(" ");
                for (int b = 0; b < z.length; b++) {
                    dtm.setValueAt(z[b], a, b + 1);
                }
            }
            tcr2.setFontName(writingSystem.get("Font").toString());
            HashMap gum = (HashMap) writingSystem.get("GlyphUseMap");
            int ng = Integer.parseInt(writingSystem.get("GlyphsPerLetter") + "");
            for (int nga = ng; nga > 0; nga--) {
                alphaLetterGlyphNumberCombo.setSelectedItem("Glyph_" + nga);
                alphaLetterUsedInCombo.setSelectedItem(gum.get("Glyph_" + nga));
            }
            capitalizationCombo.setSelectedItem((String) writingSystem.get("CapRule"));
            characterLabel.setFont(new Font(writingSystem.get("Font") + "", Font.PLAIN, 30));
        } else if (writingSystemPane.getSelectedIndex() == 1) {  // Abjad
            abjadFontField.setText(fontname);
            if (writingSystem.get("FontType").equals("borrowed")) {
                borrowedAbjadFontRadio.setSelected(true);
            } else {
                customAbjadFontRadio.setSelected(true);
                abjadFontField.setEnabled(true);
                abjadFontField.setEditable(true);
                chooseAbjadFontButton.setEnabled(true);
                abjadCharLabel.setFont(new Font(writingSystem.get("Font") + "", Font.PLAIN, 30));
            }
            abjadGlyphsPerConsonantSpinner.setValue(writingSystem.get("GlyphsPerLetter"));
            for (int a = 0; a < phonemes.size(); a++) {
                String[] z = (phonemes.get(ob[a]) + "").split(" ");
                for (int b = 0; b < z.length; b++) {
                    dtm1.setValueAt(z[b], a, b + 1);
                }
            }
            tcr2.setFontName(writingSystem.get("Font").toString());
            boolean b = true;
            //showVowelsCheck.setSelected(b);
        } else if (writingSystemPane.getSelectedIndex() == 2) {  // Abugida
            abugidaFontField.setText(fontname);
            if (writingSystem.get("FontType").equals("borrowed")) {
                abugidaBorrowedFontRadio.setSelected(true);
            } else {
                abugidaCustomFontRadio.setSelected(true);
                abugidaFontField.setEnabled(true);
                abugidaFontField.setEditable(true);
                abugidaChooseFontButton.setEnabled(true);
                tcr2.setFontName(writingSystem.get("Font").toString());
                abugidaCharLabel.setFont(new Font(writingSystem.get("Font") + "", Font.PLAIN, 30));
            }
            int pns = phonemes.size();
            if (phonemes.containsKey("VowelCarrier")) {
                vowelCarrierCheck.setSelected(true);
                dtm2.setRowCount(dtm2.getRowCount() + 1);
                dtm2.setValueAt("VowelCarrier", dtm2.getRowCount() - 1, 0);
                String vc = (phonemes.get("VowelCarrier") + "");
                dtm2.setValueAt(vc, dtm2.getRowCount() - 1, 1);
                pns--;
            }
            if (phonemes.containsKey("NoVowel")) {
                noVowelCheckbox.setSelected(true);
                dtm2.setRowCount(dtm2.getRowCount() + 1);
                dtm2.setValueAt("NoVowel", dtm2.getRowCount() - 1, 0);
                String vc = (phonemes.get("NoVowel") + "");
                dtm2.setValueAt(vc, dtm2.getRowCount() - 1, 1);
                pns--;
            }
            for (int a = 0; a < pns; a++) {
                String p = (phonemes.get(ob[a])).toString();
                dtm2.setValueAt(p, a, 1);
            }

            tcr2.setFontName(writingSystem.get("Font").toString());
        } else if (writingSystemPane.getSelectedIndex() == 3) {  // syllabary
            syllabaryCustomFontField.setText(fontname);
            if (writingSystem.get("FontType").equals("borrowed")) {
                syllabaryCustomFontCheck.setSelected(false);
            } else {
                syllabaryCustomFontCheck.setSelected(true);
                tcr2.setFontName(writingSystem.get("Font").toString());
                visibleCharLabel.setFont(new Font(writingSystem.get("Font").toString(), Font.PLAIN, 30));
            }
            vowls.add("NV");
            consnts.add("NC");
            HashMap syllables = (HashMap) writingSystem.get("Syllables");
            Object[] keys = syllables.keySet().toArray();
            Arrays.sort(keys);
            Object[] vwls = vowls.toArray();
            Object[] cnsnts = consnts.toArray();
            Arrays.sort(vwls, new LengthReverseComparator());
            Arrays.sort(cnsnts, new LengthReverseComparator());
            for (int s = 0; s < keys.length; s++) {
                String sylb = syllables.get(keys[s]).toString();
                String key = keys[s].toString();
                int vl = -1;
                int cn = -1;
                for (int c = 0; c < cnsnts.length; c++) {
                    if (key.indexOf(cnsnts[c].toString()) >= 0) {
                        cn = consnts.indexOf(cnsnts[c].toString());
                        break;
                    }
                }
                for (int v = 0; v < vwls.length; v++) {
                    if (key.indexOf(vwls[v].toString()) >= 0) {
                        vl = vowls.indexOf(vwls[v].toString());
                        break;
                    }
                }
                dtm4.setValueAt(sylb, cn, vl + 1);
            }

        } else if (writingSystemPane.getSelectedIndex() == 4) {  // multigraphic
            multiCustomFontField.setText(fontname);
            if (writingSystem.get("FontType").equals("borrowed")) {
                multiBorrowedRadio.setSelected(true);
            } else {
                multiCustomRadio.setSelected(true);
                multiCustomFontField.setEnabled(true);
                multiCustomFontField.setEditable(true);
                multiChooseCustomFontButton.setEnabled(true);
                tcr2.setFontName(writingSystem.get("Font") + "");
                multiCharacterLabel.setFont(new Font(writingSystem.get("Font") + "", Font.PLAIN, 30));
            }
            int pns = phonemes.size();
            if (phonemes.containsKey("Enders")) {
                multiUseEndersCheckbox.setSelected(true);
                dtm3.setRowCount(dtm3.getRowCount() + 1);
                pns--;
            }
            graphemesPerGlyphSpinner.setValue(Integer.parseInt(writingSystem.get("GraphemesPerGlyph") + ""));
            for (int a = 0; a < pns; a++) {
                String[] z = (phonemes.get(ob[a]) + "").split(" ");
                for (int b = 0; b < z.length; b++) {
                    dtm3.setValueAt(z[b], a, b + 1);
                }
            }
            dtm3.setValueAt("Enders", dtm3.getRowCount() - 1, 0);
            String[] ends = (phonemes.get("Enders") + "").split(" ");
            for (int a = 0; a < ends.length; a++) {
                dtm3.setValueAt(ends[a], dtm3.getRowCount() - 1, a + 1);
            }
        }
        HashMap punct = (HashMap) writingSystem.get("Punctuation");
        Object[] punctKeys = punct.keySet().toArray();
        Arrays.sort(punctKeys);
        for (int p = 0; p < punctKeys.length; p++) {
            punctuationTable.setValueAt(punct.get(punctKeys[p] + ""), p, 1);
        }
        punctCharacterLabel.setFont(new Font(writingSystem.get("Font") + "", 1, 20));
        presets = new Vector();
        RwCellRenderer2 tcr3 = new RwCellRenderer2();
        tcr3.setFontName("LCS-ConstructorII");
        presetsTable.getColumnModel().getColumn(3).setCellRenderer(tcr3);
        try {
            BufferedReader in = new BufferedReader(new FileReader("./lcs-ws-presets.csv"));
            String s = in.readLine();
            dtm6.setRowCount(0);
            String[] ss = new String[4];
            int b = 0;
            while (!(s = in.readLine()).equals("End,,,,,,,,,,,,")) {
                String[] sar = s.split(",");
                presets.add(sar);
                b++;
                ss[0] = b + "";
                ss[1] = sar[0];
                ss[2] = sar[1];
                int base = Integer.parseInt(sar[2], 16);
                char q;
                int cvalue = base;
                ss[3] = rwHexStringConverter.convertFrom(sar[11]);
                dtm6.addRow(ss);
            }
            in.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Oops!", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Oops!", JOptionPane.ERROR_MESSAGE);
        }
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

        mainButtonGroup = new javax.swing.ButtonGroup();
        alphabetButtonGroup = new javax.swing.ButtonGroup();
        writingSystemLayout = new java.awt.CardLayout();
        abjadFontButtonGroup = new javax.swing.ButtonGroup();
        diacriticsButtonGroup = new javax.swing.ButtonGroup();
        tcr2 = new rw.RwCellRenderer2();
        abugidaFontButtonGroup = new javax.swing.ButtonGroup();
        abugidaDiacriticsButtonGroup = new javax.swing.ButtonGroup();
        alphaGlyphUseMap = new java.util.HashMap();
        abjadGlyphUseMap = new java.util.HashMap();
        writingSystem = new java.util.HashMap();
        multigraphicFontGroup = new javax.swing.ButtonGroup();
        presets = new java.util.Vector();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        writingSystemPane = new javax.swing.JTabbedPane();
        alphabetCard = new javax.swing.JPanel();
        fontLabel = new javax.swing.JLabel();
        borrowedCharsRadio = new javax.swing.JRadioButton();
        customFontRadio = new javax.swing.JRadioButton();
        fontField = new javax.swing.JTextField();
        chooseAlphabetFontButton = new javax.swing.JButton();
        alphaGlyphsPerLetterLabel = new javax.swing.JLabel();
        alphaGlyphsPerLetterSpinner = new javax.swing.JSpinner();
        alphaLetterGlyphNumberCombo = new javax.swing.JComboBox();
        alphaLetterUsedInLabel = new javax.swing.JLabel();
        alphaLetterUsedInCombo = new javax.swing.JComboBox();
        capitalizationLabel = new javax.swing.JLabel();
        capitalizationCombo = new javax.swing.JComboBox();
        alphaScroller = new javax.swing.JScrollPane();
        alphaTable = new javax.swing.JTable();
        upCharButton = new javax.swing.JButton();
        alphaLeftColumnButton = new javax.swing.JButton();
        setAlphaCharButton = new javax.swing.JButton();
        alphaRightColumnButton = new javax.swing.JButton();
        downCharButton = new javax.swing.JButton();
        characterLabelPanel = new javax.swing.JPanel();
        characterLabel = new javax.swing.JLabel();
        downPageButton = new javax.swing.JButton();
        downButton = new javax.swing.JButton();
        upButton = new javax.swing.JButton();
        upPageButton = new javax.swing.JButton();
        alphaJumpToLabel = new javax.swing.JLabel();
        alphaJumpToField = new javax.swing.JTextField();
        alphaCurrentVLabel = new javax.swing.JLabel();
        autofillAlphabetButton = new javax.swing.JButton();
        directionalityButton = new javax.swing.JToggleButton();
        abjadCard = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        borrowedAbjadFontRadio = new javax.swing.JRadioButton();
        customAbjadFontRadio = new javax.swing.JRadioButton();
        abjadFontField = new javax.swing.JTextField();
        chooseAbjadFontButton = new javax.swing.JButton();
        abjadGlyphsPerConsonantLabel = new javax.swing.JLabel();
        abjadGlyphsPerConsonantSpinner = new javax.swing.JSpinner();
        abjadConsonantGlyphNumberCombo = new javax.swing.JComboBox();
        abjadConsonantUsedInLabel = new javax.swing.JLabel();
        abjadConsonantUsedInCombo = new javax.swing.JComboBox();
        abjadScroller = new javax.swing.JScrollPane();
        abjadTable = new javax.swing.JTable();
        upCharAbjadButton = new javax.swing.JButton();
        abjadLeftColumnButton = new javax.swing.JButton();
        setAbjadCharButton = new javax.swing.JButton();
        abjadRightColumnButton = new javax.swing.JButton();
        downCharAbjadButton = new javax.swing.JButton();
        downPageAbjadButton = new javax.swing.JButton();
        downAbjadCharButton = new javax.swing.JButton();
        upAbjadCharButton = new javax.swing.JButton();
        upAbjadPageButton = new javax.swing.JButton();
        abjadCharLabelPanel = new javax.swing.JPanel();
        abjadCharLabel = new javax.swing.JLabel();
        abjadJumpToLabel = new javax.swing.JLabel();
        abjadJumpToField = new javax.swing.JTextField();
        abjadCurrentVLabel = new javax.swing.JLabel();
        autofillAbjadButton = new javax.swing.JButton();
        abjadVowelCarrierCheck = new javax.swing.JCheckBox();
        abjadDirectionalityButton = new javax.swing.JToggleButton();
        abugidaCard = new javax.swing.JPanel();
        abugidaFontLabel = new javax.swing.JLabel();
        abugidaBorrowedFontRadio = new javax.swing.JRadioButton();
        abugidaCustomFontRadio = new javax.swing.JRadioButton();
        abugidaFontField = new javax.swing.JTextField();
        abugidaChooseFontButton = new javax.swing.JButton();
        abugidaScroller = new javax.swing.JScrollPane();
        abugidaTable = new javax.swing.JTable();
        abugidaUpCharButton = new javax.swing.JButton();
        setAbugidaCharButton = new javax.swing.JButton();
        abugidaDownCharButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        abugidaCharLabel = new javax.swing.JLabel();
        downPageAbugidaButton = new javax.swing.JButton();
        downCharAbugidaButton = new javax.swing.JButton();
        upCharAbugidaButton = new javax.swing.JButton();
        upPageAbugidaButton = new javax.swing.JButton();
        abugidaJumpToLabel = new javax.swing.JLabel();
        abugidaJumpToField = new javax.swing.JTextField();
        abugidaCurrentVLabel = new javax.swing.JLabel();
        abugidaAutoFillButton = new javax.swing.JButton();
        vowelCarrierCheck = new javax.swing.JCheckBox();
        noVowelCheckbox = new javax.swing.JCheckBox();
        abugidaDirectionalityButton = new javax.swing.JToggleButton();
        syllabaryCard = new javax.swing.JPanel();
        syllabaryScroller = new javax.swing.JScrollPane();
        syllabaryTable = new javax.swing.JTable();
        setSyllableButton = new javax.swing.JButton();
        syllabaryDownPageButton = new javax.swing.JButton();
        syllabaryDownCharButton = new javax.swing.JButton();
        syllabaryUpCharButton = new javax.swing.JButton();
        syllabaryUpPageButton = new javax.swing.JButton();
        consonantNumCombo = new javax.swing.JComboBox();
        vowelNumCombo = new javax.swing.JComboBox();
        consonantNumLabel = new javax.swing.JLabel();
        vowelNumLabel = new javax.swing.JLabel();
        syllabaryChooseCustomFontButton = new javax.swing.JButton();
        syllabaryCustomFontCheck = new javax.swing.JCheckBox();
        syllabaryCustomFontField = new javax.swing.JTextField();
        jumpToField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        syllabaryCharValueLabel = new javax.swing.JLabel();
        jumpToLabel = new javax.swing.JLabel();
        autoFillSyllabaryButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        visibleCharLabel = new javax.swing.JLabel();
        syllabaryDirectionalityButton = new javax.swing.JToggleButton();
        multigraphicCard = new javax.swing.JPanel();
        multigraphicFontLabel = new javax.swing.JLabel();
        multiBorrowedRadio = new javax.swing.JRadioButton();
        multiCustomRadio = new javax.swing.JRadioButton();
        multiCustomFontField = new javax.swing.JTextField();
        multiChooseCustomFontButton = new javax.swing.JButton();
        graphemesPerGlyphLabel = new javax.swing.JLabel();
        graphemesPerGlyphSpinner = new javax.swing.JSpinner();
        multiUseEndersCheckbox = new javax.swing.JCheckBox();
        multigraphicScroller = new javax.swing.JScrollPane();
        multigraphicTable = new javax.swing.JTable();
        multiUpGlyphButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        multiCharacterLabel = new javax.swing.JLabel();
        multiLeftGlyphButton = new javax.swing.JButton();
        multiSetGlyphButton = new javax.swing.JButton();
        multiRightGlyphButton = new javax.swing.JButton();
        multiDownPageButton = new javax.swing.JButton();
        multiDownCharButton = new javax.swing.JButton();
        multiUpCharButton = new javax.swing.JButton();
        multiUpPageButton = new javax.swing.JButton();
        multiDownButton = new javax.swing.JButton();
        multiJumpToLabel = new javax.swing.JLabel();
        multiJumpToField = new javax.swing.JTextField();
        currentHexValueLabel = new javax.swing.JLabel();
        multiAutoFillButton = new javax.swing.JButton();
        punctuationCard = new javax.swing.JPanel();
        punctUpGlyphButton = new javax.swing.JButton();
        punctCharPanel = new javax.swing.JPanel();
        punctCharacterLabel = new javax.swing.JLabel();
        multiDownButton1 = new javax.swing.JButton();
        punctSetGlyphButton = new javax.swing.JButton();
        punctiDownPageButton = new javax.swing.JButton();
        punctDownCharButton = new javax.swing.JButton();
        punctUpCharButton = new javax.swing.JButton();
        punctUpPageButton = new javax.swing.JButton();
        punctAutoFillButton = new javax.swing.JButton();
        punctCurrentHexValueLabel = new javax.swing.JLabel();
        punctJumpToField = new javax.swing.JTextField();
        punctJumpToLabel = new javax.swing.JLabel();
        punctuationScroller = new javax.swing.JScrollPane();
        punctuationTable = new javax.swing.JTable();
        punctSpacerPanel = new javax.swing.JPanel();
        punctSpacerPanel1 = new javax.swing.JPanel();
        Presets = new javax.swing.JPanel();
        PresetsScroller = new javax.swing.JScrollPane();
        presetsTable = new javax.swing.JTable();
        useSystemButton = new javax.swing.JButton();
        userDefdCard = new javax.swing.JPanel();
        userDefdScroller = new javax.swing.JScrollPane();
        userDefdTable = new javax.swing.JTable();
        browse4WsButton = new javax.swing.JButton();
        loadWsButton = new javax.swing.JButton();
        saveWsButton = new javax.swing.JButton();

        tcr2.setText("rwCellRenderer21"); // NOI18N

        alphaGlyphUseMap.put("Glyph_1","Lower");

        abjadGlyphUseMap.put("Glyph_1","Initial");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        okButton.setText("Ok"); // NOI18N
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel"); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        writingSystemPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                writingSystemPaneStateChanged(evt);
            }
        });

        alphabetCard.setMaximumSize(new java.awt.Dimension(5000, 5000));
        alphabetCard.setMinimumSize(new java.awt.Dimension(610, 484));
        alphabetCard.setName("alphabetCard"); // NOI18N
        alphabetCard.setPreferredSize(new java.awt.Dimension(610, 484));
        alphabetCard.setLayout(new java.awt.GridBagLayout());

        fontLabel.setText("Font"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 10;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        alphabetCard.add(fontLabel, gridBagConstraints);

        alphabetButtonGroup.add(borrowedCharsRadio);
        borrowedCharsRadio.setSelected(true);
        borrowedCharsRadio.setText("Borrowed"); // NOI18N
        borrowedCharsRadio.setMaximumSize(new java.awt.Dimension(80, 23));
        borrowedCharsRadio.setMinimumSize(new java.awt.Dimension(80, 23));
        borrowedCharsRadio.setPreferredSize(new java.awt.Dimension(80, 23));
        borrowedCharsRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrowedCharsRadioActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        alphabetCard.add(borrowedCharsRadio, gridBagConstraints);

        alphabetButtonGroup.add(customFontRadio);
        customFontRadio.setText("Custom"); // NOI18N
        customFontRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customFontRadioActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 20;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 10;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        alphabetCard.add(customFontRadio, gridBagConstraints);

        fontField.setEditable(false);
        fontField.setText("LCS-ConstructorII"); // NOI18N
        fontField.setEnabled(false);
        fontField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fontFieldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 30;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 30;
        gridBagConstraints.gridheight = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.4;
        gridBagConstraints.weighty = 0.1;
        alphabetCard.add(fontField, gridBagConstraints);

        chooseAlphabetFontButton.setText("Choose"); // NOI18N
        chooseAlphabetFontButton.setEnabled(false);
        chooseAlphabetFontButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chooseAlphabetFontButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 60;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = 10;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        alphabetCard.add(chooseAlphabetFontButton, gridBagConstraints);

        alphaGlyphsPerLetterLabel.setText("Glyphs/Letter"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        alphabetCard.add(alphaGlyphsPerLetterLabel, gridBagConstraints);

        alphaGlyphsPerLetterSpinner.setModel(new javax.swing.SpinnerNumberModel(1, 1, 4, 1));
        alphaGlyphsPerLetterSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                alphaGlyphsPerLetterSpinnerStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 10;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        alphabetCard.add(alphaGlyphsPerLetterSpinner, gridBagConstraints);

        alphaLetterGlyphNumberCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Glyph_1", "Glyph_2", "Glyph_3", "Glyph_4" }));
        alphaLetterGlyphNumberCombo.setEnabled(false);
        alphaLetterGlyphNumberCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                alphaLetterGlyphNumberComboActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 20;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 10;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        alphabetCard.add(alphaLetterGlyphNumberCombo, gridBagConstraints);

        alphaLetterUsedInLabel.setText("Used in"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 30;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 10;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        alphabetCard.add(alphaLetterUsedInLabel, gridBagConstraints);

        alphaLetterUsedInCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Initial", "Medial", "Final", "Isolated", "Upper", "Lower" }));
        alphaLetterUsedInCombo.setEnabled(false);
        alphaLetterUsedInCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                alphaLetterUsedInComboActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 40;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 10;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        alphabetCard.add(alphaLetterUsedInCombo, gridBagConstraints);

        capitalizationLabel.setText("Capitalization Rule"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 50;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 10;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        alphabetCard.add(capitalizationLabel, gridBagConstraints);

        capitalizationCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "None", "Nouns", "Sentence", "Word" }));
        capitalizationCombo.setEnabled(false);
        capitalizationCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                capitalizationComboActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 60;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = 10;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        alphabetCard.add(capitalizationCombo, gridBagConstraints);

        alphaTable.setAutoCreateRowSorter(true);
        alphaTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Pronunciation", "Glyph 1"
            }
        ));
        alphaTable.setRowHeight(24);
        alphaScroller.setViewportView(alphaTable);
        alphaTable.getColumnModel().getColumn(0).setCellRenderer(tcr2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = 30;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        alphabetCard.add(alphaScroller, gridBagConstraints);

        upCharButton.setFont(new java.awt.Font("LCS-ConstructorII", 0, 18));
        upCharButton.setText("⬆"); // NOI18N
        upCharButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        upCharButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upCharButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 60;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        alphabetCard.add(upCharButton, gridBagConstraints);

        alphaLeftColumnButton.setFont(new java.awt.Font("LCS-ConstructorII", 0, 18));
        alphaLeftColumnButton.setText("⬅"); // NOI18N
        alphaLeftColumnButton.setEnabled(false);
        alphaLeftColumnButton.setMargin(new java.awt.Insets(0, 4, 0, 4));
        alphaLeftColumnButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                alphaLeftColumnButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 70;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        alphabetCard.add(alphaLeftColumnButton, gridBagConstraints);

        setAlphaCharButton.setText("Set"); // NOI18N
        setAlphaCharButton.setMargin(new java.awt.Insets(6, 4, 6, 4));
        setAlphaCharButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setAlphaCharButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 70;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        alphabetCard.add(setAlphaCharButton, gridBagConstraints);

        alphaRightColumnButton.setFont(new java.awt.Font("LCS-ConstructorII", 0, 18));
        alphaRightColumnButton.setText("➡"); // NOI18N
        alphaRightColumnButton.setEnabled(false);
        alphaRightColumnButton.setMargin(new java.awt.Insets(0, 4, 0, 4));
        alphaRightColumnButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                alphaRightColumnButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 20;
        gridBagConstraints.gridy = 70;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        alphabetCard.add(alphaRightColumnButton, gridBagConstraints);

        downCharButton.setFont(new java.awt.Font("LCS-ConstructorII", 0, 18));
        downCharButton.setText("⬇"); // NOI18N
        downCharButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        downCharButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downCharButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 80;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        alphabetCard.add(downCharButton, gridBagConstraints);

        characterLabelPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        characterLabelPanel.setMaximumSize(new java.awt.Dimension(94, 30));
        characterLabelPanel.setMinimumSize(new java.awt.Dimension(94, 30));
        characterLabelPanel.setPreferredSize(new java.awt.Dimension(94, 30));
        characterLabelPanel.setLayout(new java.awt.GridBagLayout());

        characterLabel.setFont(new java.awt.Font("LCS-ConstructorII", 0, 30));
        characterLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        characterLabel.setText("XXAXX"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(98, 2, 99, 0);
        characterLabelPanel.add(characterLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 40;
        gridBagConstraints.gridy = 60;
        gridBagConstraints.gridwidth = 20;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 20, 0, 3);
        alphabetCard.add(characterLabelPanel, gridBagConstraints);

        downPageButton.setFont(new java.awt.Font("LCS-ConstructorII", 0, 18));
        downPageButton.setText("⬅"); // NOI18N
        downPageButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        downPageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downPageButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 40;
        gridBagConstraints.gridy = 70;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        alphabetCard.add(downPageButton, gridBagConstraints);

        downButton.setFont(new java.awt.Font("LCS-ConstructorII", 0, 18));
        downButton.setText("⇐"); // NOI18N
        downButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        downButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 45;
        gridBagConstraints.gridy = 70;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        alphabetCard.add(downButton, gridBagConstraints);

        upButton.setFont(new java.awt.Font("LCS-ConstructorII", 0, 18));
        upButton.setText("⇒"); // NOI18N
        upButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        upButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 50;
        gridBagConstraints.gridy = 70;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.05;
        gridBagConstraints.weighty = 0.1;
        alphabetCard.add(upButton, gridBagConstraints);

        upPageButton.setFont(new java.awt.Font("LCS-ConstructorII", 0, 18));
        upPageButton.setText("➡"); // NOI18N
        upPageButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        upPageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upPageButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 55;
        gridBagConstraints.gridy = 70;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.05;
        gridBagConstraints.weighty = 0.1;
        alphabetCard.add(upPageButton, gridBagConstraints);

        alphaJumpToLabel.setText("Jump to"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 40;
        gridBagConstraints.gridy = 80;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.05;
        gridBagConstraints.weighty = 0.1;
        alphabetCard.add(alphaJumpToLabel, gridBagConstraints);

        alphaJumpToField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                alphaJumpToFieldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 45;
        gridBagConstraints.gridy = 80;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.05;
        gridBagConstraints.weighty = 0.1;
        alphabetCard.add(alphaJumpToField, gridBagConstraints);

        alphaCurrentVLabel.setText("41"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 50;
        gridBagConstraints.gridy = 80;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.05;
        gridBagConstraints.weighty = 0.1;
        alphabetCard.add(alphaCurrentVLabel, gridBagConstraints);

        autofillAlphabetButton.setText("Auto Fill"); // NOI18N
        autofillAlphabetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autofillAlphabetButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 55;
        gridBagConstraints.gridy = 80;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        alphabetCard.add(autofillAlphabetButton, gridBagConstraints);

        directionalityButton.setFont(new java.awt.Font("LCS-ConstructorII", 0, 12));
        directionalityButton.setText("L ➡ R");
        directionalityButton.setMargin(new java.awt.Insets(0, 2, 0, 2));
        directionalityButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                directionalityButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 60;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 0);
        alphabetCard.add(directionalityButton, gridBagConstraints);

        writingSystemPane.addTab("Alphabet", alphabetCard);

        abjadCard.setMinimumSize(new java.awt.Dimension(610, 484));
        abjadCard.setName("abjadCard"); // NOI18N
        abjadCard.setPreferredSize(new java.awt.Dimension(610, 484));
        abjadCard.setLayout(new java.awt.GridBagLayout());

        jLabel2.setText("Font"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 10;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        abjadCard.add(jLabel2, gridBagConstraints);

        abjadFontButtonGroup.add(borrowedAbjadFontRadio);
        borrowedAbjadFontRadio.setSelected(true);
        borrowedAbjadFontRadio.setText("Borrowed"); // NOI18N
        borrowedAbjadFontRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrowedAbjadFontRadioActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        abjadCard.add(borrowedAbjadFontRadio, gridBagConstraints);

        abjadFontButtonGroup.add(customAbjadFontRadio);
        customAbjadFontRadio.setText("Custom"); // NOI18N
        customAbjadFontRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customAbjadFontRadioActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 20;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 10;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        abjadCard.add(customAbjadFontRadio, gridBagConstraints);

        abjadFontField.setText("LCS-ConstructorII"); // NOI18N
        abjadFontField.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 30;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 30;
        gridBagConstraints.gridheight = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.4;
        gridBagConstraints.weighty = 0.1;
        abjadCard.add(abjadFontField, gridBagConstraints);

        chooseAbjadFontButton.setText("Choose"); // NOI18N
        chooseAbjadFontButton.setEnabled(false);
        chooseAbjadFontButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chooseAbjadFontButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 60;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = 10;
        abjadCard.add(chooseAbjadFontButton, gridBagConstraints);

        abjadGlyphsPerConsonantLabel.setText("Glyphs/Letter"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 10;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        abjadCard.add(abjadGlyphsPerConsonantLabel, gridBagConstraints);

        abjadGlyphsPerConsonantSpinner.setModel(new javax.swing.SpinnerNumberModel(1, 1, 4, 1));
        abjadGlyphsPerConsonantSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                abjadGlyphsPerConsonantSpinnerStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 10;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        abjadCard.add(abjadGlyphsPerConsonantSpinner, gridBagConstraints);

        abjadConsonantGlyphNumberCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Glyph_1", "Glyph_2", "Glyph_3", "Glyph_4" }));
        abjadConsonantGlyphNumberCombo.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 20;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 10;
        abjadCard.add(abjadConsonantGlyphNumberCombo, gridBagConstraints);

        abjadConsonantUsedInLabel.setText("Used in"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 30;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 10;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        abjadCard.add(abjadConsonantUsedInLabel, gridBagConstraints);

        abjadConsonantUsedInCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Initial", "Ini/Med", "Medial", "Med/Fin", "Final", "Isolated", "Upper", "Lower" }));
        abjadConsonantUsedInCombo.setEnabled(false);
        abjadConsonantUsedInCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abjadConsonantUsedInComboActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 40;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 10;
        abjadCard.add(abjadConsonantUsedInCombo, gridBagConstraints);

        abjadTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Pronunciation", "Glyph 1"
            }
        ));
        abjadTable.setRowHeight(24);
        abjadScroller.setViewportView(abjadTable);
        abjadTable.getColumnModel().getColumn(0).setCellRenderer(tcr2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = 40;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.4;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        abjadCard.add(abjadScroller, gridBagConstraints);

        upCharAbjadButton.setFont(new java.awt.Font("LCS-ConstructorII", 0, 18));
        upCharAbjadButton.setText("⬆"); // NOI18N
        upCharAbjadButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        upCharAbjadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upCharAbjadButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 60;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        abjadCard.add(upCharAbjadButton, gridBagConstraints);

        abjadLeftColumnButton.setFont(new java.awt.Font("LCS-ConstructorII", 0, 18));
        abjadLeftColumnButton.setText("⬅"); // NOI18N
        abjadLeftColumnButton.setEnabled(false);
        abjadLeftColumnButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        abjadLeftColumnButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abjadLeftColumnButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 70;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        abjadCard.add(abjadLeftColumnButton, gridBagConstraints);

        setAbjadCharButton.setText("Set"); // NOI18N
        setAbjadCharButton.setMargin(new java.awt.Insets(6, 4, 6, 4));
        setAbjadCharButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setAbjadCharButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 70;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.weightx = 0.05;
        gridBagConstraints.weighty = 0.05;
        abjadCard.add(setAbjadCharButton, gridBagConstraints);

        abjadRightColumnButton.setFont(new java.awt.Font("LCS-ConstructorII", 0, 18));
        abjadRightColumnButton.setText("➡"); // NOI18N
        abjadRightColumnButton.setEnabled(false);
        abjadRightColumnButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        abjadRightColumnButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abjadRightColumnButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 20;
        gridBagConstraints.gridy = 70;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.05;
        gridBagConstraints.weighty = 0.05;
        abjadCard.add(abjadRightColumnButton, gridBagConstraints);

        downCharAbjadButton.setFont(new java.awt.Font("LCS-ConstructorII", 0, 18));
        downCharAbjadButton.setText("⬇"); // NOI18N
        downCharAbjadButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        downCharAbjadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downCharAbjadButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 80;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        abjadCard.add(downCharAbjadButton, gridBagConstraints);

        downPageAbjadButton.setFont(new java.awt.Font("LCS-ConstructorII", 0, 18));
        downPageAbjadButton.setText("⬅"); // NOI18N
        downPageAbjadButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        downPageAbjadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downPageAbjadButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 40;
        gridBagConstraints.gridy = 70;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        abjadCard.add(downPageAbjadButton, gridBagConstraints);

        downAbjadCharButton.setFont(new java.awt.Font("LCS-ConstructorII", 0, 18));
        downAbjadCharButton.setText("⇐"); // NOI18N
        downAbjadCharButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        downAbjadCharButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downAbjadCharButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 45;
        gridBagConstraints.gridy = 70;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        abjadCard.add(downAbjadCharButton, gridBagConstraints);

        upAbjadCharButton.setFont(new java.awt.Font("LCS-ConstructorII", 0, 18));
        upAbjadCharButton.setText("⇒"); // NOI18N
        upAbjadCharButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        upAbjadCharButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upAbjadCharButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 50;
        gridBagConstraints.gridy = 70;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.05;
        gridBagConstraints.weighty = 0.01;
        abjadCard.add(upAbjadCharButton, gridBagConstraints);

        upAbjadPageButton.setFont(new java.awt.Font("LCS-ConstructorII", 0, 18));
        upAbjadPageButton.setText("➡"); // NOI18N
        upAbjadPageButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        upAbjadPageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upAbjadPageButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 55;
        gridBagConstraints.gridy = 70;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.05;
        gridBagConstraints.weighty = 0.1;
        abjadCard.add(upAbjadPageButton, gridBagConstraints);

        abjadCharLabelPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        abjadCharLabelPanel.setMinimumSize(new java.awt.Dimension(94, 30));
        abjadCharLabelPanel.setPreferredSize(new java.awt.Dimension(94, 30));

        abjadCharLabel.setFont(new java.awt.Font("LCS-ConstructorII", 0, 30));
        abjadCharLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        abjadCharLabel.setText("XXAXX"); // NOI18N

        javax.swing.GroupLayout abjadCharLabelPanelLayout = new javax.swing.GroupLayout(abjadCharLabelPanel);
        abjadCharLabelPanel.setLayout(abjadCharLabelPanelLayout);
        abjadCharLabelPanelLayout.setHorizontalGroup(
            abjadCharLabelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(abjadCharLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
        );
        abjadCharLabelPanelLayout.setVerticalGroup(
            abjadCharLabelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(abjadCharLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 40;
        gridBagConstraints.gridy = 60;
        gridBagConstraints.gridwidth = 20;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 20, 0, 3);
        abjadCard.add(abjadCharLabelPanel, gridBagConstraints);

        abjadJumpToLabel.setText("Jump to"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 40;
        gridBagConstraints.gridy = 80;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.05;
        gridBagConstraints.weighty = 0.1;
        abjadCard.add(abjadJumpToLabel, gridBagConstraints);

        abjadJumpToField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abjadJumpToFieldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 45;
        gridBagConstraints.gridy = 80;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.05;
        gridBagConstraints.weighty = 0.1;
        abjadCard.add(abjadJumpToField, gridBagConstraints);

        abjadCurrentVLabel.setText("41"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 50;
        gridBagConstraints.gridy = 80;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.05;
        gridBagConstraints.weighty = 0.1;
        abjadCard.add(abjadCurrentVLabel, gridBagConstraints);

        autofillAbjadButton.setText("Auto Fill"); // NOI18N
        autofillAbjadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autofillAbjadButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 55;
        gridBagConstraints.gridy = 80;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        abjadCard.add(autofillAbjadButton, gridBagConstraints);

        abjadVowelCarrierCheck.setText("Vowel Carrier"); // NOI18N
        abjadVowelCarrierCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abjadVowelCarrierCheckActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 50;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 10;
        abjadCard.add(abjadVowelCarrierCheck, gridBagConstraints);

        abjadDirectionalityButton.setFont(new java.awt.Font("LCS-ConstructorII", 0, 14));
        abjadDirectionalityButton.setText("L ➡ R");
        abjadDirectionalityButton.setMargin(new java.awt.Insets(0, 2, 0, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 60;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 0);
        abjadCard.add(abjadDirectionalityButton, gridBagConstraints);

        writingSystemPane.addTab("Abjad", abjadCard);

        abugidaCard.setMaximumSize(new java.awt.Dimension(610, 484));
        abugidaCard.setMinimumSize(new java.awt.Dimension(610, 484));
        abugidaCard.setName("abugidaCard"); // NOI18N
        abugidaCard.setPreferredSize(new java.awt.Dimension(610, 484));
        abugidaCard.setLayout(new java.awt.GridBagLayout());

        abugidaFontLabel.setText("Font"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 10;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        abugidaCard.add(abugidaFontLabel, gridBagConstraints);

        abugidaFontButtonGroup.add(abugidaBorrowedFontRadio);
        abugidaBorrowedFontRadio.setSelected(true);
        abugidaBorrowedFontRadio.setText("Borrowed"); // NOI18N
        abugidaBorrowedFontRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abugidaBorrowedFontRadioActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        abugidaCard.add(abugidaBorrowedFontRadio, gridBagConstraints);

        abugidaFontButtonGroup.add(abugidaCustomFontRadio);
        abugidaCustomFontRadio.setText("Custom"); // NOI18N
        abugidaCustomFontRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abugidaCustomFontRadioActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 20;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 10;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        abugidaCard.add(abugidaCustomFontRadio, gridBagConstraints);

        abugidaFontField.setText("LCS-ConstructorII"); // NOI18N
        abugidaFontField.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 30;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 30;
        gridBagConstraints.gridheight = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.4;
        gridBagConstraints.weighty = 0.1;
        abugidaCard.add(abugidaFontField, gridBagConstraints);

        abugidaChooseFontButton.setText("Choose"); // NOI18N
        abugidaChooseFontButton.setEnabled(false);
        abugidaChooseFontButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abugidaChooseFontButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 60;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = 10;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        abugidaCard.add(abugidaChooseFontButton, gridBagConstraints);

        abugidaTable.setAutoCreateRowSorter(true);
        abugidaTable.setFont(new java.awt.Font("LCS-ConstructorII", 0, 18));
        abugidaTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Pronunciation", "Written"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        abugidaTable.setRowHeight(24);
        abugidaScroller.setViewportView(abugidaTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = 40;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.35;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        abugidaCard.add(abugidaScroller, gridBagConstraints);

        abugidaUpCharButton.setFont(new java.awt.Font("LCS-ConstructorII", 0, 18));
        abugidaUpCharButton.setText("⬆"); // NOI18N
        abugidaUpCharButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        abugidaUpCharButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abugidaUpCharButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 60;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        abugidaCard.add(abugidaUpCharButton, gridBagConstraints);

        setAbugidaCharButton.setText("Set"); // NOI18N
        setAbugidaCharButton.setMargin(new java.awt.Insets(6, 4, 6, 4));
        setAbugidaCharButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setAbugidaCharButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 70;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        abugidaCard.add(setAbugidaCharButton, gridBagConstraints);

        abugidaDownCharButton.setFont(new java.awt.Font("LCS-ConstructorII", 0, 18));
        abugidaDownCharButton.setText("⬇"); // NOI18N
        abugidaDownCharButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        abugidaDownCharButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abugidaDownCharButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 80;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        abugidaCard.add(abugidaDownCharButton, gridBagConstraints);

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel1.setMaximumSize(new java.awt.Dimension(94, 30));
        jPanel1.setMinimumSize(new java.awt.Dimension(94, 30));
        jPanel1.setPreferredSize(new java.awt.Dimension(94, 30));

        abugidaCharLabel.setFont(new java.awt.Font("LCS-ConstructorII", 0, 30));
        abugidaCharLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        abugidaCharLabel.setText("XXAXX"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(abugidaCharLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(abugidaCharLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 40;
        gridBagConstraints.gridy = 60;
        gridBagConstraints.gridwidth = 20;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 20, 0, 3);
        abugidaCard.add(jPanel1, gridBagConstraints);

        downPageAbugidaButton.setFont(new java.awt.Font("LCS-ConstructorII", 0, 18));
        downPageAbugidaButton.setText("⬅"); // NOI18N
        downPageAbugidaButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        downPageAbugidaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downPageAbugidaButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 40;
        gridBagConstraints.gridy = 70;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        abugidaCard.add(downPageAbugidaButton, gridBagConstraints);

        downCharAbugidaButton.setFont(new java.awt.Font("LCS-ConstructorII", 0, 18));
        downCharAbugidaButton.setText("⇐"); // NOI18N
        downCharAbugidaButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        downCharAbugidaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downCharAbugidaButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 45;
        gridBagConstraints.gridy = 70;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        abugidaCard.add(downCharAbugidaButton, gridBagConstraints);

        upCharAbugidaButton.setFont(new java.awt.Font("LCS-ConstructorII", 0, 18));
        upCharAbugidaButton.setText("⇒"); // NOI18N
        upCharAbugidaButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        upCharAbugidaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upCharAbugidaButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 50;
        gridBagConstraints.gridy = 70;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        abugidaCard.add(upCharAbugidaButton, gridBagConstraints);

        upPageAbugidaButton.setFont(new java.awt.Font("LCS-ConstructorII", 0, 18));
        upPageAbugidaButton.setText("➡"); // NOI18N
        upPageAbugidaButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        upPageAbugidaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upPageAbugidaButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 55;
        gridBagConstraints.gridy = 70;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        abugidaCard.add(upPageAbugidaButton, gridBagConstraints);

        abugidaJumpToLabel.setText("Jump to"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 40;
        gridBagConstraints.gridy = 80;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.05;
        gridBagConstraints.weighty = 0.1;
        abugidaCard.add(abugidaJumpToLabel, gridBagConstraints);

        abugidaJumpToField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abugidaJumpToFieldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 45;
        gridBagConstraints.gridy = 80;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.05;
        gridBagConstraints.weighty = 0.1;
        abugidaCard.add(abugidaJumpToField, gridBagConstraints);

        abugidaCurrentVLabel.setText("41"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 50;
        gridBagConstraints.gridy = 80;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        abugidaCard.add(abugidaCurrentVLabel, gridBagConstraints);

        abugidaAutoFillButton.setText("Auto Fill"); // NOI18N
        abugidaAutoFillButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abugidaAutoFillButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 55;
        gridBagConstraints.gridy = 80;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        abugidaCard.add(abugidaAutoFillButton, gridBagConstraints);

        vowelCarrierCheck.setText("Vowel Carrier"); // NOI18N
        vowelCarrierCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vowelCarrierCheckActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 20;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 0.05;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        abugidaCard.add(vowelCarrierCheck, gridBagConstraints);

        noVowelCheckbox.setText("No Vowel"); // NOI18N
        noVowelCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noVowelCheckboxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 20;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 0.05;
        abugidaCard.add(noVowelCheckbox, gridBagConstraints);

        abugidaDirectionalityButton.setFont(new java.awt.Font("LCS-ConstructorII", 0, 14));
        abugidaDirectionalityButton.setText("L ➡ R");
        abugidaDirectionalityButton.setMargin(new java.awt.Insets(0, 2, 0, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 60;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 0);
        abugidaCard.add(abugidaDirectionalityButton, gridBagConstraints);

        writingSystemPane.addTab("Abugida", abugidaCard);

        syllabaryCard.setName("syllabaryCard"); // NOI18N

        syllabaryTable.setAutoCreateRowSorter(true);
        syllabaryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Pronunciation", "Written"
            }
        ));
        syllabaryTable.setRowHeight(25);
        syllabaryScroller.setViewportView(syllabaryTable);
        syllabaryTable.getColumnModel().getColumn(0).setCellRenderer(tcr2);
        syllabaryTable.getColumnModel().getColumn(1).setCellRenderer(tcr2);

        setSyllableButton.setText("Set @"); // NOI18N
        setSyllableButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setSyllableButtonActionPerformed(evt);
            }
        });

        syllabaryDownPageButton.setText("<<"); // NOI18N
        syllabaryDownPageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                syllabaryDownPageButtonActionPerformed(evt);
            }
        });

        syllabaryDownCharButton.setText("<"); // NOI18N
        syllabaryDownCharButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                syllabaryDownCharButtonActionPerformed(evt);
            }
        });

        syllabaryUpCharButton.setText(">"); // NOI18N
        syllabaryUpCharButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                syllabaryUpCharButtonActionPerformed(evt);
            }
        });

        syllabaryUpPageButton.setText(">>"); // NOI18N
        syllabaryUpPageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                syllabaryUpPageButtonActionPerformed(evt);
            }
        });

        consonantNumCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", " " }));

        vowelNumCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4" }));

        consonantNumLabel.setText("Consonant"); // NOI18N

        vowelNumLabel.setText("Vowel"); // NOI18N

        syllabaryChooseCustomFontButton.setText("Choose"); // NOI18N
        syllabaryChooseCustomFontButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                syllabaryChooseCustomFontButtonActionPerformed(evt);
            }
        });

        syllabaryCustomFontCheck.setText("Custom Font"); // NOI18N
        syllabaryCustomFontCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                syllabaryCustomFontCheckActionPerformed(evt);
            }
        });

        syllabaryCustomFontField.setText("LCS-ConstructorII"); // NOI18N
        syllabaryCustomFontField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                syllabaryCustomFontFieldActionPerformed(evt);
            }
        });

        jumpToField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jumpToFieldActionPerformed(evt);
            }
        });

        jLabel1.setText("Syllable Parts "); // NOI18N

        syllabaryCharValueLabel.setText("41"); // NOI18N

        jumpToLabel.setText("Jump To"); // NOI18N

        autoFillSyllabaryButton.setText("Auto Fill"); // NOI18N
        autoFillSyllabaryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoFillSyllabaryButtonActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        visibleCharLabel.setFont(new java.awt.Font("LCS-ConstructorII", 0, 30));
        visibleCharLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        visibleCharLabel.setText("XXAXX"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(visibleCharLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(visibleCharLabel)
        );

        syllabaryDirectionalityButton.setFont(new java.awt.Font("LCS-ConstructorII", 0, 14));
        syllabaryDirectionalityButton.setText("L ➡ R");
        syllabaryDirectionalityButton.setMargin(new java.awt.Insets(0, 2, 0, 2));

        javax.swing.GroupLayout syllabaryCardLayout = new javax.swing.GroupLayout(syllabaryCard);
        syllabaryCard.setLayout(syllabaryCardLayout);
        syllabaryCardLayout.setHorizontalGroup(
            syllabaryCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(syllabaryCardLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(syllabaryCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(syllabaryScroller, javax.swing.GroupLayout.DEFAULT_SIZE, 578, Short.MAX_VALUE)
                    .addGroup(syllabaryCardLayout.createSequentialGroup()
                        .addGroup(syllabaryCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(syllabaryCardLayout.createSequentialGroup()
                                .addGroup(syllabaryCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, syllabaryCardLayout.createSequentialGroup()
                                        .addGap(8, 8, 8)
                                        .addComponent(setSyllableButton)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(syllabaryCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(consonantNumLabel)
                                            .addComponent(consonantNumCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(syllabaryCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(vowelNumCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(vowelNumLabel))
                                        .addGap(36, 36, 36))
                                    .addGroup(syllabaryCardLayout.createSequentialGroup()
                                        .addComponent(syllabaryDirectionalityButton)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel1)
                                        .addGap(18, 18, 18)))
                                .addComponent(syllabaryCustomFontCheck))
                            .addComponent(autoFillSyllabaryButton, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(syllabaryCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(syllabaryCardLayout.createSequentialGroup()
                                .addComponent(syllabaryCustomFontField, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(syllabaryChooseCustomFontButton))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, syllabaryCardLayout.createSequentialGroup()
                                .addGroup(syllabaryCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(syllabaryCardLayout.createSequentialGroup()
                                        .addComponent(syllabaryDownPageButton)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(syllabaryCardLayout.createSequentialGroup()
                                        .addComponent(jumpToLabel)
                                        .addGap(18, 18, 18)))
                                .addGroup(syllabaryCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(syllabaryCardLayout.createSequentialGroup()
                                        .addComponent(syllabaryDownCharButton)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(syllabaryUpCharButton))
                                    .addComponent(jumpToField, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(6, 6, 6)
                                .addGroup(syllabaryCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(syllabaryCardLayout.createSequentialGroup()
                                        .addComponent(syllabaryCharValueLabel)
                                        .addGap(27, 27, 27))
                                    .addComponent(syllabaryUpPageButton))))))
                .addContainerGap())
        );
        syllabaryCardLayout.setVerticalGroup(
            syllabaryCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(syllabaryCardLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(syllabaryScroller, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(syllabaryCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(syllabaryCustomFontCheck)
                    .addComponent(syllabaryCustomFontField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(syllabaryChooseCustomFontButton)
                    .addComponent(syllabaryDirectionalityButton)
                    .addComponent(jLabel1))
                .addGap(6, 6, 6)
                .addGroup(syllabaryCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(syllabaryCardLayout.createSequentialGroup()
                        .addGroup(syllabaryCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(consonantNumLabel)
                            .addComponent(vowelNumLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(syllabaryCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(consonantNumCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(setSyllableButton)
                            .addComponent(vowelNumCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(autoFillSyllabaryButton))
                    .addGroup(syllabaryCardLayout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17)
                        .addGroup(syllabaryCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(syllabaryDownCharButton)
                            .addComponent(syllabaryUpCharButton)
                            .addComponent(syllabaryUpPageButton)
                            .addComponent(syllabaryDownPageButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(syllabaryCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jumpToField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(syllabaryCharValueLabel)
                            .addComponent(jumpToLabel))))
                .addGap(139, 139, 139))
        );

        writingSystemPane.addTab("Syllabary", syllabaryCard);

        multigraphicCard.setLayout(new java.awt.GridBagLayout());

        multigraphicFontLabel.setText("Font"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        multigraphicCard.add(multigraphicFontLabel, gridBagConstraints);

        multigraphicFontGroup.add(multiBorrowedRadio);
        multiBorrowedRadio.setSelected(true);
        multiBorrowedRadio.setText("Borrowed"); // NOI18N
        multiBorrowedRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                multiBorrowedRadioActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        multigraphicCard.add(multiBorrowedRadio, gridBagConstraints);

        multigraphicFontGroup.add(multiCustomRadio);
        multiCustomRadio.setText("Custom"); // NOI18N
        multiCustomRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                multiCustomRadioActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        multigraphicCard.add(multiCustomRadio, gridBagConstraints);

        multiCustomFontField.setText("LCS-ConstructorII"); // NOI18N
        multiCustomFontField.setEnabled(false);
        multiCustomFontField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                multiCustomFontFieldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.6;
        gridBagConstraints.weighty = 0.1;
        multigraphicCard.add(multiCustomFontField, gridBagConstraints);

        multiChooseCustomFontButton.setText("Choose ..."); // NOI18N
        multiChooseCustomFontButton.setEnabled(false);
        multiChooseCustomFontButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                multiChooseCustomFontButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        multigraphicCard.add(multiChooseCustomFontButton, gridBagConstraints);

        graphemesPerGlyphLabel.setText("Graphemes / Glyph"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        multigraphicCard.add(graphemesPerGlyphLabel, gridBagConstraints);

        graphemesPerGlyphSpinner.setModel(new javax.swing.SpinnerNumberModel(2, 2, 4, 1));
        graphemesPerGlyphSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                graphemesPerGlyphSpinnerStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.weighty = 0.1;
        multigraphicCard.add(graphemesPerGlyphSpinner, gridBagConstraints);

        multiUseEndersCheckbox.setText("Use Enders"); // NOI18N
        multiUseEndersCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                multiUseEndersCheckboxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        multigraphicCard.add(multiUseEndersCheckbox, gridBagConstraints);

        multigraphicTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Sound", "Grapheme 1", "Grapheme 2"
            }
        ));
        multigraphicTable.setRowHeight(24);
        multigraphicScroller.setViewportView(multigraphicTable);
        multigraphicTable.getColumnModel().getColumn(2).setHeaderValue("Grapheme 2");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.4;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        multigraphicCard.add(multigraphicScroller, gridBagConstraints);

        multiUpGlyphButton.setFont(new java.awt.Font("LCS-ConstructorII", 0, 18));
        multiUpGlyphButton.setText("⬆"); // NOI18N
        multiUpGlyphButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        multiUpGlyphButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                multiUpGlyphButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        multigraphicCard.add(multiUpGlyphButton, gridBagConstraints);

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel3.setLayout(new java.awt.BorderLayout());

        multiCharacterLabel.setFont(new java.awt.Font("LCS-ConstructorII", 0, 36));
        multiCharacterLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        multiCharacterLabel.setText("XXAXX"); // NOI18N
        jPanel3.add(multiCharacterLabel, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 0.05;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 0);
        multigraphicCard.add(jPanel3, gridBagConstraints);

        multiLeftGlyphButton.setFont(new java.awt.Font("LCS-ConstructorII", 0, 18));
        multiLeftGlyphButton.setText("⬅"); // NOI18N
        multiLeftGlyphButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        multiLeftGlyphButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                multiLeftGlyphButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        multigraphicCard.add(multiLeftGlyphButton, gridBagConstraints);

        multiSetGlyphButton.setText("Set"); // NOI18N
        multiSetGlyphButton.setMargin(new java.awt.Insets(2, 5, 2, 5));
        multiSetGlyphButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                multiSetGlyphButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        multigraphicCard.add(multiSetGlyphButton, gridBagConstraints);

        multiRightGlyphButton.setFont(new java.awt.Font("LCS-ConstructorII", 0, 18));
        multiRightGlyphButton.setText("➡"); // NOI18N
        multiRightGlyphButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        multiRightGlyphButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                multiRightGlyphButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        multigraphicCard.add(multiRightGlyphButton, gridBagConstraints);

        multiDownPageButton.setFont(new java.awt.Font("LCS-ConstructorII", 0, 18));
        multiDownPageButton.setText("⬅"); // NOI18N
        multiDownPageButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        multiDownPageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                multiDownPageButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.05;
        gridBagConstraints.weighty = 0.1;
        multigraphicCard.add(multiDownPageButton, gridBagConstraints);

        multiDownCharButton.setFont(new java.awt.Font("LCS-ConstructorII", 0, 18));
        multiDownCharButton.setText("⇐"); // NOI18N
        multiDownCharButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        multiDownCharButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                multiDownCharButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.05;
        gridBagConstraints.weighty = 0.1;
        multigraphicCard.add(multiDownCharButton, gridBagConstraints);

        multiUpCharButton.setFont(new java.awt.Font("LCS-ConstructorII", 0, 18));
        multiUpCharButton.setText("⇒"); // NOI18N
        multiUpCharButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        multiUpCharButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                multiUpCharButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.05;
        gridBagConstraints.weighty = 0.1;
        multigraphicCard.add(multiUpCharButton, gridBagConstraints);

        multiUpPageButton.setFont(new java.awt.Font("LCS-ConstructorII", 0, 18));
        multiUpPageButton.setText("➡"); // NOI18N
        multiUpPageButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        multiUpPageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                multiUpPageButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.05;
        gridBagConstraints.weighty = 0.1;
        multigraphicCard.add(multiUpPageButton, gridBagConstraints);

        multiDownButton.setFont(new java.awt.Font("LCS-ConstructorII", 0, 18));
        multiDownButton.setText("⬇"); // NOI18N
        multiDownButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        multiDownButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                multiDownButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        multigraphicCard.add(multiDownButton, gridBagConstraints);

        multiJumpToLabel.setText("Jump To"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.05;
        gridBagConstraints.weighty = 0.1;
        multigraphicCard.add(multiJumpToLabel, gridBagConstraints);

        multiJumpToField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                multiJumpToFieldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.05;
        gridBagConstraints.weighty = 0.1;
        multigraphicCard.add(multiJumpToField, gridBagConstraints);

        currentHexValueLabel.setText("41"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.weightx = 0.05;
        gridBagConstraints.weighty = 0.1;
        multigraphicCard.add(currentHexValueLabel, gridBagConstraints);

        multiAutoFillButton.setText("Auto Fill"); // NOI18N
        multiAutoFillButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        multiAutoFillButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                multiAutoFillButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.05;
        gridBagConstraints.weighty = 0.1;
        multigraphicCard.add(multiAutoFillButton, gridBagConstraints);

        writingSystemPane.addTab("Multigraphic", multigraphicCard);

        punctuationCard.setLayout(new java.awt.GridBagLayout());

        punctUpGlyphButton.setFont(new java.awt.Font("LCS-ConstructorII", 0, 18));
        punctUpGlyphButton.setText("⬆"); // NOI18N
        punctUpGlyphButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        punctUpGlyphButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                punctUpGlyphButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        punctuationCard.add(punctUpGlyphButton, gridBagConstraints);

        punctCharPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        punctCharPanel.setLayout(new java.awt.BorderLayout());

        punctCharacterLabel.setFont(new java.awt.Font("LCS-ConstructorII", 0, 36));
        punctCharacterLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        punctCharacterLabel.setText("XXAXX"); // NOI18N
        punctCharPanel.add(punctCharacterLabel, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 0.05;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 0);
        punctuationCard.add(punctCharPanel, gridBagConstraints);

        multiDownButton1.setFont(new java.awt.Font("LCS-ConstructorII", 0, 18));
        multiDownButton1.setText("⬇"); // NOI18N
        multiDownButton1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        multiDownButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                multiDownButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        punctuationCard.add(multiDownButton1, gridBagConstraints);

        punctSetGlyphButton.setText("Set"); // NOI18N
        punctSetGlyphButton.setMargin(new java.awt.Insets(2, 5, 2, 5));
        punctSetGlyphButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                punctSetGlyphButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        punctuationCard.add(punctSetGlyphButton, gridBagConstraints);

        punctiDownPageButton.setFont(new java.awt.Font("LCS-ConstructorII", 0, 18));
        punctiDownPageButton.setText("⬅"); // NOI18N
        punctiDownPageButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        punctiDownPageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                punctiDownPageButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.05;
        gridBagConstraints.weighty = 0.1;
        punctuationCard.add(punctiDownPageButton, gridBagConstraints);

        punctDownCharButton.setFont(new java.awt.Font("LCS-ConstructorII", 0, 18));
        punctDownCharButton.setText("⇐"); // NOI18N
        punctDownCharButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        punctDownCharButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                punctDownCharButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.05;
        gridBagConstraints.weighty = 0.1;
        punctuationCard.add(punctDownCharButton, gridBagConstraints);

        punctUpCharButton.setFont(new java.awt.Font("LCS-ConstructorII", 0, 18));
        punctUpCharButton.setText("⇒"); // NOI18N
        punctUpCharButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        punctUpCharButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                punctUpCharButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.05;
        gridBagConstraints.weighty = 0.1;
        punctuationCard.add(punctUpCharButton, gridBagConstraints);

        punctUpPageButton.setFont(new java.awt.Font("LCS-ConstructorII", 0, 18));
        punctUpPageButton.setText("➡"); // NOI18N
        punctUpPageButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        punctUpPageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                punctUpPageButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.05;
        gridBagConstraints.weighty = 0.1;
        punctuationCard.add(punctUpPageButton, gridBagConstraints);

        punctAutoFillButton.setText("Auto Fill"); // NOI18N
        punctAutoFillButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        punctAutoFillButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                punctAutoFillButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.05;
        gridBagConstraints.weighty = 0.1;
        punctuationCard.add(punctAutoFillButton, gridBagConstraints);

        punctCurrentHexValueLabel.setText("41"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.weightx = 0.05;
        gridBagConstraints.weighty = 0.1;
        punctuationCard.add(punctCurrentHexValueLabel, gridBagConstraints);

        punctJumpToField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                punctJumpToFieldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.05;
        gridBagConstraints.weighty = 0.1;
        punctuationCard.add(punctJumpToField, gridBagConstraints);

        punctJumpToLabel.setText("Jump To"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.05;
        gridBagConstraints.weighty = 0.1;
        punctuationCard.add(punctJumpToLabel, gridBagConstraints);

        punctuationTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {" ", null},
                {"!", null},
                {"\"", null},
                {"#", null},
                {"$", null},
                {"%", null},
                {"&", null},
                {"'", null},
                {"(", null},
                {")", null},
                {"*", null},
                {"+", null},
                {",", null},
                {"-", null},
                {".", null},
                {"/", null},
                {"0", null},
                {"1", null},
                {"2", null},
                {"3", null},
                {"4", null},
                {"5", null},
                {"6", null},
                {"7", null},
                {"8", null},
                {"9", null},
                {":", null},
                {";", null},
                {"<", null},
                {"=", null},
                {">", null},
                {"?", null},
                {"@", null},
                {"[", null},
                {"\\", null},
                    {"]", null},
                    {"^", null},
                    {"_", null},
                    {"`", null},
                    {"{", null},
                    {"|", null},
                    {"}", null},
                    {"~", null}
                },
                new String [] {
                    "Latin8", "Glyph"
                }
            ) {
                boolean[] canEdit = new boolean [] {
                    false, true
                };

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            });
            punctuationTable.setRowHeight(24);
            punctuationScroller.setViewportView(punctuationTable);

            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 2;
            gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
            gridBagConstraints.gridheight = 4;
            gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.weighty = 0.4;
            gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
            punctuationCard.add(punctuationScroller, gridBagConstraints);

            javax.swing.GroupLayout punctSpacerPanelLayout = new javax.swing.GroupLayout(punctSpacerPanel);
            punctSpacerPanel.setLayout(punctSpacerPanelLayout);
            punctSpacerPanelLayout.setHorizontalGroup(
                punctSpacerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 0, Short.MAX_VALUE)
            );
            punctSpacerPanelLayout.setVerticalGroup(
                punctSpacerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 0, Short.MAX_VALUE)
            );

            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 9;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
            gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints.weightx = 0.2;
            gridBagConstraints.weighty = 0.0010;
            punctuationCard.add(punctSpacerPanel, gridBagConstraints);

            javax.swing.GroupLayout punctSpacerPanel1Layout = new javax.swing.GroupLayout(punctSpacerPanel1);
            punctSpacerPanel1.setLayout(punctSpacerPanel1Layout);
            punctSpacerPanel1Layout.setHorizontalGroup(
                punctSpacerPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 0, Short.MAX_VALUE)
            );
            punctSpacerPanel1Layout.setVerticalGroup(
                punctSpacerPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 0, Short.MAX_VALUE)
            );

            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.gridwidth = 4;
            gridBagConstraints.weightx = 0.4;
            gridBagConstraints.weighty = 0.0010;
            punctuationCard.add(punctSpacerPanel1, gridBagConstraints);

            writingSystemPane.addTab("Punctuation & Numerals", punctuationCard);

            Presets.setLayout(new java.awt.GridBagLayout());

            presetsTable.setAutoCreateRowSorter(true);
            presetsTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null}
                },
                new String [] {
                    "#", "Name", "Writing System Type", "Sample Text"
                }
            ) {
                Class[] types = new Class [] {
                    java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
                };
                boolean[] canEdit = new boolean [] {
                    true, false, false, true
                };

                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            });
            presetsTable.setRowHeight(30);
            presetsTable.getTableHeader().setReorderingAllowed(false);
            PresetsScroller.setViewportView(presetsTable);
            presetsTable.getColumnModel().getColumn(0).setMinWidth(30);
            presetsTable.getColumnModel().getColumn(0).setPreferredWidth(30);
            presetsTable.getColumnModel().getColumn(0).setMaxWidth(30);

            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.weighty = 0.9;
            gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 10);
            Presets.add(PresetsScroller, gridBagConstraints);

            useSystemButton.setText("Use"); // NOI18N
            useSystemButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    useSystemButtonActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 1;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.weighty = 0.1;
            gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 0);
            Presets.add(useSystemButton, gridBagConstraints);

            writingSystemPane.addTab("Presets", Presets);

            userDefdTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null}
                },
                new String [] {
                    "Name", "Type", "Sample Text"
                }
            ));
            userDefdTable.setRowHeight(30);
            userDefdScroller.setViewportView(userDefdTable);

            browse4WsButton.setText("Browse ...");
            browse4WsButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    browse4WsButtonActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout userDefdCardLayout = new javax.swing.GroupLayout(userDefdCard);
            userDefdCard.setLayout(userDefdCardLayout);
            userDefdCardLayout.setHorizontalGroup(
                userDefdCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(userDefdCardLayout.createSequentialGroup()
                    .addGroup(userDefdCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(userDefdCardLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(userDefdScroller, javax.swing.GroupLayout.DEFAULT_SIZE, 578, Short.MAX_VALUE))
                        .addGroup(userDefdCardLayout.createSequentialGroup()
                            .addGap(249, 249, 249)
                            .addComponent(browse4WsButton)))
                    .addContainerGap())
            );
            userDefdCardLayout.setVerticalGroup(
                userDefdCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(userDefdCardLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(userDefdScroller, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(browse4WsButton)
                    .addContainerGap(42, Short.MAX_VALUE))
            );

            writingSystemPane.addTab("User Defined", userDefdCard);

            loadWsButton.setText("Load ...");
            loadWsButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    loadWsButtonActionPerformed(evt);
                }
            });

            saveWsButton.setText("Save As ...");
            saveWsButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    saveWsButtonActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(writingSystemPane, javax.swing.GroupLayout.PREFERRED_SIZE, 603, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(okButton)
                            .addGap(67, 67, 67)
                            .addComponent(loadWsButton)
                            .addGap(34, 34, 34)
                            .addComponent(saveWsButton)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cancelButton)))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(writingSystemPane, javax.swing.GroupLayout.PREFERRED_SIZE, 409, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(okButton)
                        .addComponent(cancelButton)
                        .addComponent(loadWsButton)
                        .addComponent(saveWsButton))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            pack();
        }// </editor-fold>//GEN-END:initComponents

    private void borrowedCharsRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrowedCharsRadioActionPerformed
        chooseAlphabetFontButton.setEnabled(false);
        fontField.setEnabled(false);
    }//GEN-LAST:event_borrowedCharsRadioActionPerformed

    private void syllabaryDownCharButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_syllabaryDownCharButtonActionPerformed
        int c = visibleCharLabel.getText().codePointAt(2);
        --c;
        if (c < 32) {
            c = 32;
        }
        updateSyllabaryChar(c);
    }//GEN-LAST:event_syllabaryDownCharButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        setVisible(false);
        chosenOption = CANCEL_OPTION;
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void syllabaryCustomFontCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_syllabaryCustomFontCheckActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_syllabaryCustomFontCheckActionPerformed

    private void customFontRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customFontRadioActionPerformed
        chooseAlphabetFontButton.setEnabled(true);
        fontField.setEnabled(true);
    }//GEN-LAST:event_customFontRadioActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        int cs = 0;
        boolean punctSet = true;
        if (punctuationTable.getValueAt(0, 1) == null) {
            JOptionPane jop = new JOptionPane();
            cs = jop.showConfirmDialog(this, "Punctuation is not set.\nDo you wish to set it?", "Oops!", JOptionPane.INFORMATION_MESSAGE);
            punctSet = false;
        }
        if (punctSet == true) {
            this.setVisible(false);
            System.out.println(writingSystem);
            chosenOption = OK_OPTION;
            punctuationSet = true;
        }
        if (cs == JOptionPane.NO_OPTION && punctSet == false) {
            this.setVisible(false);
            chosenOption = OK_OPTION;
        }

    }//GEN-LAST:event_okButtonActionPerformed

    private void chooseAlphabetFontButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chooseAlphabetFontButtonActionPerformed
        RwJFontChooser rwf = new RwJFontChooser(new JFrame());
        rwf.setVisible(true);
        Font daFont = rwf.getSelectedFont();
        if (daFont != null) {
            fontField.setText(daFont.getFontName());
            tcr2.setFontName(daFont.getFontName());
            for (int a = 0; a < alphaTable.getRowCount(); a++) {
                alphaTable.setValueAt(alphaTable.getValueAt(a, 0), a, 1);
            }
            characterLabel.setFont(daFont);
            punctCharacterLabel.setFont(daFont);
        }
    }//GEN-LAST:event_chooseAlphabetFontButtonActionPerformed

    private void syllabaryChooseCustomFontButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_syllabaryChooseCustomFontButtonActionPerformed
        RwJFontChooser rwf = new RwJFontChooser(new JFrame());
        rwf.setVisible(true);
        Font daFont = rwf.getSelectedFont();
        if (daFont != null) {
            syllabaryCustomFontField.setText(daFont.getFontName());
            visibleCharLabel.setFont(daFont);
            punctCharacterLabel.setFont(daFont);
            RwCellRenderer2 rcr = new RwCellRenderer2(daFont);
            for (int a = 0; a < syllabaryTable.getColumnCount(); a++) {
                syllabaryTable.getColumnModel().getColumn(a).setCellRenderer(rcr);
            }
        }
    }//GEN-LAST:event_syllabaryChooseCustomFontButtonActionPerformed

    private void setSyllableButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setSyllableButtonActionPerformed
        int row = consonantNumCombo.getSelectedIndex();
        int col = vowelNumCombo.getSelectedIndex();
        syllabaryTable.setValueAt(new String(Character.toChars(visibleCharLabel.getText().codePointAt(2))), row, col + 1);
    }//GEN-LAST:event_setSyllableButtonActionPerformed

    private void syllabaryUpCharButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_syllabaryUpCharButtonActionPerformed
        int c = visibleCharLabel.getText().codePointAt(2);
        ++c;
        updateSyllabaryChar(c);
    }//GEN-LAST:event_syllabaryUpCharButtonActionPerformed

    private void syllabaryDownPageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_syllabaryDownPageButtonActionPerformed
        int c = visibleCharLabel.getText().codePointAt(2);
        c = c - 256;
        if (c < 32) {
            c = 32;
        }
        updateSyllabaryChar(c);
    }//GEN-LAST:event_syllabaryDownPageButtonActionPerformed

    private void syllabaryUpPageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_syllabaryUpPageButtonActionPerformed
        int c = visibleCharLabel.getText().codePointAt(2);
        c = c + 256;
        updateSyllabaryChar(c);
    }//GEN-LAST:event_syllabaryUpPageButtonActionPerformed

    private void jumpToFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jumpToFieldActionPerformed
        String jumpTo = jumpToField.getText();
        int loc = Integer.parseInt(jumpTo, 16);
        updateSyllabaryChar(loc);
    }//GEN-LAST:event_jumpToFieldActionPerformed

    private void autoFillSyllabaryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoFillSyllabaryButtonActionPerformed
        Object o = syllabaryTable.getValueAt(0, 1);
        if (o == null) {
            JOptionPane.showMessageDialog(this, "Cannot start with null!", "Oops!", JOptionPane.ERROR_MESSAGE);
        } else {
            String s = o + "";
            int c = s.codePointAt(0);
            for (int row = 0; row < syllabaryTable.getRowCount(); row++) {
                for (int col = 1; col < syllabaryTable.getColumnCount(); col++) {
                    syllabaryTable.setValueAt(new String(Character.toChars(c)), row, col);
                    c++;
                }
            }
        }
    }//GEN-LAST:event_autoFillSyllabaryButtonActionPerformed

    private void upCharAbjadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upCharAbjadButtonActionPerformed
        int selRow = abjadTable.getSelectedRow();
        if (selRow - 1 >= 0) {
            abjadTable.setRowSelectionInterval(selRow - 1, selRow - 1);
            abjadTable.scrollRectToVisible(abjadTable.getCellRect(selRow - 1, 0, true));
        } else {
            selRow = abjadTable.getRowCount() - 1;
            abjadTable.setRowSelectionInterval(selRow, selRow);
            abjadTable.scrollRectToVisible(abjadTable.getCellRect(selRow, 0, true));
        }
    }//GEN-LAST:event_upCharAbjadButtonActionPerformed

    private void downCharAbjadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downCharAbjadButtonActionPerformed
        int selRow = abjadTable.getSelectedRow();
        if (selRow + 1 < abjadTable.getRowCount()) {
            abjadTable.setRowSelectionInterval(selRow + 1, selRow + 1);
            abjadTable.scrollRectToVisible(abjadTable.getCellRect(selRow + 1, 0, true));
        } else {
            selRow = 0;
            abjadTable.setRowSelectionInterval(selRow, selRow);
            abjadTable.scrollRectToVisible(abjadTable.getCellRect(selRow, 0, true));
        }
    }//GEN-LAST:event_downCharAbjadButtonActionPerformed

    private void downPageAbjadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downPageAbjadButtonActionPerformed
        int c = abjadCharLabel.getText().codePointAt(2);
        c -= 256;
        if (c < 20) {
            c = 20;
        }
        updateAbjadChar(c);
    }//GEN-LAST:event_downPageAbjadButtonActionPerformed

    private void downAbjadCharButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downAbjadCharButtonActionPerformed
        int c = abjadCharLabel.getText().codePointAt(2);
        c--;
        if (c < 32) {
            c = 32;
        }
        updateAbjadChar(c);
    }//GEN-LAST:event_downAbjadCharButtonActionPerformed

    private void upAbjadCharButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upAbjadCharButtonActionPerformed
        int c = abjadCharLabel.getText().codePointAt(2);
        c++;
        updateAbjadChar(c);
    }//GEN-LAST:event_upAbjadCharButtonActionPerformed

    private void upAbjadPageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upAbjadPageButtonActionPerformed
        int c = abjadCharLabel.getText().codePointAt(2);
        c += 256;
        updateAbjadChar(c);
    }//GEN-LAST:event_upAbjadPageButtonActionPerformed

    private void abugidaCustomFontRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_abugidaCustomFontRadioActionPerformed
        abugidaChooseFontButton.setEnabled(true);
        abugidaFontField.setEnabled(true);
    }//GEN-LAST:event_abugidaCustomFontRadioActionPerformed

    private void upCharButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upCharButtonActionPerformed
        int selRow = alphaTable.getSelectedRow();
        if (selRow - 1 >= 0) {
            alphaTable.setRowSelectionInterval(selRow - 1, selRow - 1);
            alphaTable.scrollRectToVisible(alphaTable.getCellRect(selRow - 1, 0, true));
        } else {
            selRow = alphaTable.getRowCount() - 1;
            alphaTable.setRowSelectionInterval(selRow, selRow);
            alphaTable.scrollRectToVisible(alphaTable.getCellRect(selRow, 0, true));
        }
    }//GEN-LAST:event_upCharButtonActionPerformed

    private void downCharButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downCharButtonActionPerformed
        int selRow = alphaTable.getSelectedRow();
        if (selRow + 1 < alphaTable.getRowCount()) {
            alphaTable.setRowSelectionInterval(selRow + 1, selRow + 1);
            alphaTable.scrollRectToVisible(alphaTable.getCellRect(selRow + 1, 0, true));
        } else {
            selRow = 0;
            alphaTable.setRowSelectionInterval(selRow, selRow);
            alphaTable.scrollRectToVisible(alphaTable.getCellRect(selRow, 0, true));
        }
    }//GEN-LAST:event_downCharButtonActionPerformed

    private void downButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downButtonActionPerformed
        int c = characterLabel.getText().codePointAt(2);
        c--;
        if (c < 32) {
            c = 32;
        }
        updateAlphaChar(c);
    }//GEN-LAST:event_downButtonActionPerformed

    private void upButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upButtonActionPerformed
        int c = characterLabel.getText().codePointAt(2);
        c++;
        updateAlphaChar(c);
    }//GEN-LAST:event_upButtonActionPerformed

    private void downPageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downPageButtonActionPerformed
        int c = characterLabel.getText().codePointAt(2);
        c -= 256;
        if (c < 32) {
            c = 32;
        }
        updateAlphaChar(c);
    }//GEN-LAST:event_downPageButtonActionPerformed

    private void upPageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upPageButtonActionPerformed
        int c = characterLabel.getText().codePointAt(2);
        c += 256;
        updateAlphaChar(c);
    }//GEN-LAST:event_upPageButtonActionPerformed

    private void alphaJumpToFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_alphaJumpToFieldActionPerformed
        int v = (int) Integer.parseInt(alphaJumpToField.getText(), 16);
        updateAlphaChar(v);
    }//GEN-LAST:event_alphaJumpToFieldActionPerformed

    private void setAlphaCharButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setAlphaCharButtonActionPerformed
        if (alphaTable.getSelectedColumn() < 1) {
            alphaTable.setColumnSelectionInterval(1, 1);
        }
        try {
            int v = characterLabel.getText().codePointAt(2);
            alphaTable.setValueAt(new String(Character.toChars(v)), alphaTable.getSelectedRow(), alphaTable.getSelectedColumn());
        } catch (IndexOutOfBoundsException ex) {
            JOptionPane.showMessageDialog(this, "Please choose a place to set it!", null,
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_setAlphaCharButtonActionPerformed

    private void chooseAbjadFontButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chooseAbjadFontButtonActionPerformed
        RwJFontChooser rwf = new RwJFontChooser(new JFrame());
        rwf.setVisible(true);
        Font daFont = rwf.getSelectedFont();
        if (daFont != null) {
            abjadFontField.setText(daFont.getFontName());
            tcr2.setFontName(daFont.getFontName());
            for (int a = 0; a < abjadTable.getRowCount(); a++) {
                abjadTable.setValueAt(abjadTable.getValueAt(a, 0), a, 1);
            }
            abjadCharLabel.setFont(daFont);
            punctCharacterLabel.setFont(daFont);
        }
    }//GEN-LAST:event_chooseAbjadFontButtonActionPerformed

    private void autofillAlphabetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autofillAlphabetButtonActionPerformed
        if (alphaTable.getSelectedColumn() < 1) {
            alphaTable.setColumnSelectionInterval(1, 1);
        }
        Object[] o = new Object[alphaTable.getColumnCount()];
        for (int z = 0; z < alphaTable.getColumnCount(); z++) {
            o[z] = alphaTable.getValueAt(0, z);
        }
        if (o[0] == null) {
            JOptionPane.showMessageDialog(this, "Cannot start with null!", "Oops!", JOptionPane.ERROR_MESSAGE);
        } else {
            for (int z = 1; z < alphaTable.getColumnCount(); z++) {
                String s = o[z] + "";
                int c = s.codePointAt(0);
                for (int row = 0; row < alphaTable.getRowCount(); row++) {
                    alphaTable.setValueAt(new String(Character.toChars(c)), row, z);
                    c++;
                }
            }
        }
    }//GEN-LAST:event_autofillAlphabetButtonActionPerformed

    private void autofillAbjadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autofillAbjadButtonActionPerformed
        if (abjadTable.getSelectedColumn() < 1) {
            abjadTable.setColumnSelectionInterval(1, 1);
        }
        Object[] o = new Object[abjadTable.getColumnCount()];
        for (int z = 0; z < abjadTable.getColumnCount(); z++) {
            o[z] = abjadTable.getValueAt(0, z);
        }
        if (o[0] == null) {
            JOptionPane.showMessageDialog(this, "Cannot start with null!", "Oops!", JOptionPane.ERROR_MESSAGE);
        } else {
            for (int z = 1; z < abjadTable.getColumnCount(); z++) {
                String s = o[z] + "";
                int c = s.codePointAt(0);
                for (int row = 0; row < abjadTable.getRowCount(); row++) {
                    abjadTable.setValueAt(new String(Character.toChars(c)), row, z);
                    c++;
                }
            }
        }
    }//GEN-LAST:event_autofillAbjadButtonActionPerformed

    private void setAbjadCharButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setAbjadCharButtonActionPerformed
        if (abjadTable.getSelectedColumn() < 1) {
            abjadTable.setColumnSelectionInterval(1, 1);
        }
        try {
            int v = abjadCharLabel.getText().codePointAt(2);
            abjadTable.setValueAt(new String(Character.toChars(v)), abjadTable.getSelectedRow(), abjadTable.getSelectedColumn());
        } catch (IndexOutOfBoundsException ex) {
            JOptionPane.showMessageDialog(this, "Please choose a place to set it!", null,
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_setAbjadCharButtonActionPerformed

    private void abjadJumpToFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_abjadJumpToFieldActionPerformed
        String jumpTo = abjadJumpToField.getText();
        int loc = Integer.parseInt(jumpTo, 16);
        updateAbjadChar(loc);
    }//GEN-LAST:event_abjadJumpToFieldActionPerformed

    private void borrowedAbjadFontRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrowedAbjadFontRadioActionPerformed
        chooseAbjadFontButton.setEnabled(false);
        abjadFontField.setEnabled(false);
    }//GEN-LAST:event_borrowedAbjadFontRadioActionPerformed

    private void customAbjadFontRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customAbjadFontRadioActionPerformed
        chooseAbjadFontButton.setEnabled(true);
        abjadFontField.setEnabled(true);
    }//GEN-LAST:event_customAbjadFontRadioActionPerformed

    private void abugidaChooseFontButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_abugidaChooseFontButtonActionPerformed
        RwJFontChooser rwf = new RwJFontChooser(new JFrame());
        rwf.setVisible(true);
        Font daFont = rwf.getSelectedFont();
        if (daFont != null) {
            abugidaFontField.setText(daFont.getFontName());
            tcr2.setFontName(daFont.getFontName());
            for (int a = 0; a < abugidaTable.getRowCount(); a++) {
                if (abugidaTable.getValueAt(a, 1) == null) {
                    abugidaTable.setValueAt(abugidaTable.getValueAt(a, 0), a, 1);
                }
            }
            abugidaCharLabel.setFont(daFont);
            punctCharacterLabel.setFont(daFont);
        }
    }//GEN-LAST:event_abugidaChooseFontButtonActionPerformed

    private void abugidaUpCharButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_abugidaUpCharButtonActionPerformed
        int selRow = abugidaTable.getSelectedRow();
        if (selRow - 1 >= 0) {
            abugidaTable.setRowSelectionInterval(selRow - 1, selRow - 1);
            abugidaTable.scrollRectToVisible(abugidaTable.getCellRect(selRow - 1, 0, true));
        } else {
            selRow = abugidaTable.getRowCount() - 1;
            abugidaTable.setRowSelectionInterval(selRow, selRow);
            abugidaTable.scrollRectToVisible(abugidaTable.getCellRect(selRow, 0, true));
        }
    }//GEN-LAST:event_abugidaUpCharButtonActionPerformed

    private void abugidaDownCharButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_abugidaDownCharButtonActionPerformed
        int selRow = abugidaTable.getSelectedRow();
        if (selRow + 1 < abugidaTable.getRowCount()) {
            abugidaTable.setRowSelectionInterval(selRow + 1, selRow + 1);
            abugidaTable.scrollRectToVisible(abugidaTable.getCellRect(selRow + 1, 0, true));
        } else {
            selRow = 0;
            abugidaTable.setRowSelectionInterval(selRow, selRow);
            abugidaTable.scrollRectToVisible(abugidaTable.getCellRect(selRow, 0, true));
        }
    }//GEN-LAST:event_abugidaDownCharButtonActionPerformed

    private void setAbugidaCharButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setAbugidaCharButtonActionPerformed
        try {
            int v = abugidaCharLabel.getText().codePointAt(2);
            abugidaTable.setValueAt(new String(Character.toChars(v)), abugidaTable.getSelectedRow(), 1);
        } catch (IndexOutOfBoundsException ex) {
            JOptionPane.showMessageDialog(this, "Please choose a place to set it!", null,
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_setAbugidaCharButtonActionPerformed

    private void downPageAbugidaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downPageAbugidaButtonActionPerformed
        int c = abugidaCharLabel.getText().codePointAt(2);
        c -= 256;
        if (c < 32) {
            c = 32;
        }
        updateAbugidaChar(c);
    }//GEN-LAST:event_downPageAbugidaButtonActionPerformed

    private void downCharAbugidaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downCharAbugidaButtonActionPerformed
        int c = abugidaCharLabel.getText().codePointAt(2);
        c--;
        if (c < 32) {
            c = 32;
        }
        updateAbugidaChar(c);
    }//GEN-LAST:event_downCharAbugidaButtonActionPerformed

    private void upCharAbugidaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upCharAbugidaButtonActionPerformed
        int c = abugidaCharLabel.getText().codePointAt(2);
        c++;
        updateAbugidaChar(c);
    }//GEN-LAST:event_upCharAbugidaButtonActionPerformed

    private void upPageAbugidaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upPageAbugidaButtonActionPerformed
        int c = abugidaCharLabel.getText().codePointAt(2);
        c += 256;
        updateAbugidaChar(c);
    }//GEN-LAST:event_upPageAbugidaButtonActionPerformed

    private void abugidaAutoFillButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_abugidaAutoFillButtonActionPerformed
        Object o = abugidaTable.getValueAt(0, 1);
        if (o == null) {
            JOptionPane.showMessageDialog(this, "Cannot start with null!", "Oops!", JOptionPane.ERROR_MESSAGE);
        } else {
            String s = o + "";
            int c = s.codePointAt(0);
            for (int row = 0; row < abugidaTable.getRowCount(); row++) {
                abugidaTable.setValueAt(new String(Character.toChars(c)), row, 1);
                c++;
            }
        }
    }//GEN-LAST:event_abugidaAutoFillButtonActionPerformed

    private void abugidaJumpToFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_abugidaJumpToFieldActionPerformed
        int v = Integer.parseInt(abugidaJumpToField.getText(), 16);
        updateAbugidaChar(v);
    }//GEN-LAST:event_abugidaJumpToFieldActionPerformed

    private void abugidaBorrowedFontRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_abugidaBorrowedFontRadioActionPerformed
        abugidaChooseFontButton.setEnabled(false);
        abugidaFontField.setEnabled(false);
    }//GEN-LAST:event_abugidaBorrowedFontRadioActionPerformed

    private void alphaGlyphsPerLetterSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_alphaGlyphsPerLetterSpinnerStateChanged
        int numberOfGlyphs = ((Integer) alphaGlyphsPerLetterSpinner.getValue()).intValue();
        if (numberOfGlyphs > 1) {
            alphaLetterGlyphNumberCombo.setEnabled(true);
            alphaLetterUsedInCombo.setEnabled(true);
            alphaLeftColumnButton.setEnabled(true);
            alphaRightColumnButton.setEnabled(true);
        } else {
            alphaLetterGlyphNumberCombo.setEnabled(false);
            alphaLetterUsedInCombo.setEnabled(false);
            alphaLeftColumnButton.setEnabled(false);
            alphaRightColumnButton.setEnabled(false);
        }
        if (alphaTable.getColumnCount() != numberOfGlyphs + 1) {
            DefaultTableModel defMod = (DefaultTableModel) alphaTable.getModel();
            defMod.setColumnCount(2);
            for (int a = 1; a < numberOfGlyphs; a++) {
                defMod.addColumn("Glyph " + (a + 1));
            }
            for (int a = 0; a <= numberOfGlyphs; a++) {
                alphaTable.getColumnModel().getColumn(a).setCellRenderer(tcr2);
            }
        }
    }//GEN-LAST:event_alphaGlyphsPerLetterSpinnerStateChanged

    private void alphaLeftColumnButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_alphaLeftColumnButtonActionPerformed
        if (alphaTable.getSelectedColumn() < 1) {
            alphaTable.setColumnSelectionInterval(1, 1);
        }
        System.out.println(alphaTable.getSelectedColumn());
        if (alphaTable.getSelectedColumn() > 1) {
            alphaTable.setColumnSelectionInterval(alphaTable.getSelectedColumn() - 1, alphaTable.getSelectedColumn() - 1);
        }
    }//GEN-LAST:event_alphaLeftColumnButtonActionPerformed

    private void alphaRightColumnButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_alphaRightColumnButtonActionPerformed
        if (alphaTable.getSelectedColumn() < 1) {
            alphaTable.setColumnSelectionInterval(1, 1);
        }
        System.out.println(alphaTable.getSelectedColumn());
        if (alphaTable.getSelectedColumn() < alphaTable.getColumnCount() - 1) {
            alphaTable.setColumnSelectionInterval(alphaTable.getSelectedColumn() + 1, alphaTable.getSelectedColumn() + 1);
        }
    }//GEN-LAST:event_alphaRightColumnButtonActionPerformed

    private void alphaLetterGlyphNumberComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_alphaLetterGlyphNumberComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_alphaLetterGlyphNumberComboActionPerformed

    private void alphaLetterUsedInComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_alphaLetterUsedInComboActionPerformed
        String g1 = (String) alphaLetterGlyphNumberCombo.getSelectedItem();
        String use = (String) alphaLetterUsedInCombo.getSelectedItem();
        alphaGlyphUseMap.put(g1, use);
        if (alphaGlyphUseMap.containsValue("Upper")) {
            capitalizationCombo.setEnabled(true);
        } else {
            capitalizationCombo.setEnabled(false);
        }
        //System.out.println(alphaGlyphUseMap);
    }//GEN-LAST:event_alphaLetterUsedInComboActionPerformed

    private void abjadLeftColumnButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_abjadLeftColumnButtonActionPerformed
        if (abjadTable.getSelectedColumn() < 1) {
            abjadTable.setColumnSelectionInterval(1, 1);
        }
        if (abjadTable.getSelectedColumn() > 1) {
            abjadTable.setColumnSelectionInterval(abjadTable.getSelectedColumn() - 1, abjadTable.getSelectedColumn() - 1);
        }
    }//GEN-LAST:event_abjadLeftColumnButtonActionPerformed

    private void abjadRightColumnButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_abjadRightColumnButtonActionPerformed
        if (abjadTable.getSelectedColumn() < 1) {
            abjadTable.setColumnSelectionInterval(1, 1);
        }
        if (abjadTable.getSelectedColumn() < abjadTable.getColumnCount() - 1) {
            abjadTable.setColumnSelectionInterval(abjadTable.getSelectedColumn() + 1, abjadTable.getSelectedColumn() + 1);
        }
    }//GEN-LAST:event_abjadRightColumnButtonActionPerformed

    private void abjadGlyphsPerConsonantSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_abjadGlyphsPerConsonantSpinnerStateChanged
        int numberOfGlyphs = ((Integer) abjadGlyphsPerConsonantSpinner.getValue()).intValue();
        if (numberOfGlyphs > 1) {
            abjadConsonantGlyphNumberCombo.setEnabled(true);
            abjadConsonantUsedInCombo.setEnabled(true);
            abjadLeftColumnButton.setEnabled(true);
            abjadRightColumnButton.setEnabled(true);
        } else {
            abjadConsonantGlyphNumberCombo.setEnabled(false);
            abjadConsonantUsedInCombo.setEnabled(false);
            abjadLeftColumnButton.setEnabled(false);
            abjadRightColumnButton.setEnabled(false);
        }
        if (abjadTable.getColumnCount() != numberOfGlyphs + 1) {
            DefaultTableModel defMod = (DefaultTableModel) abjadTable.getModel();
            defMod.setColumnCount(2);
            for (int a = 1; a < numberOfGlyphs; a++) {
                defMod.addColumn("Glyph " + (a + 1));
            }
            for (int a = 0; a <= numberOfGlyphs; a++) {
                abjadTable.getColumnModel().getColumn(a).setCellRenderer(tcr2);
            }
        }
    }//GEN-LAST:event_abjadGlyphsPerConsonantSpinnerStateChanged

    private void abjadConsonantUsedInComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_abjadConsonantUsedInComboActionPerformed
        String g1 = (String) abjadConsonantGlyphNumberCombo.getSelectedItem();
        String use = (String) abjadConsonantUsedInCombo.getSelectedItem();
        abjadGlyphUseMap.put(g1, use);
        System.out.println(abjadGlyphUseMap);
    }//GEN-LAST:event_abjadConsonantUsedInComboActionPerformed

    private void capitalizationComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_capitalizationComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_capitalizationComboActionPerformed

    private void multiCustomFontFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_multiCustomFontFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_multiCustomFontFieldActionPerformed

    private void multiUpGlyphButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_multiUpGlyphButtonActionPerformed
        int selRow = multigraphicTable.getSelectedRow();
        if (selRow - 1 >= 0) {
            multigraphicTable.setRowSelectionInterval(selRow - 1, selRow - 1);
            multigraphicTable.scrollRectToVisible(multigraphicTable.getCellRect(selRow - 1, 0, true));
        } else {
            selRow = multigraphicTable.getRowCount() - 1;
            multigraphicTable.setRowSelectionInterval(selRow, selRow);
            multigraphicTable.scrollRectToVisible(multigraphicTable.getCellRect(selRow, 0, true));
        }
    }//GEN-LAST:event_multiUpGlyphButtonActionPerformed

    private void multiDownButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_multiDownButtonActionPerformed
        int selRow = multigraphicTable.getSelectedRow();
        if (selRow + 1 < multigraphicTable.getRowCount()) {
            multigraphicTable.setRowSelectionInterval(selRow + 1, selRow + 1);
            multigraphicTable.scrollRectToVisible(multigraphicTable.getCellRect(selRow + 1, 0, true));
        } else {
            selRow = 0;
            multigraphicTable.setRowSelectionInterval(selRow, selRow);
            multigraphicTable.scrollRectToVisible(multigraphicTable.getCellRect(selRow, 0, true));
        }
    }//GEN-LAST:event_multiDownButtonActionPerformed

    private void multiBorrowedRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_multiBorrowedRadioActionPerformed
        multiCustomFontField.setEnabled(false);
        multiChooseCustomFontButton.setEnabled(false);
    }//GEN-LAST:event_multiBorrowedRadioActionPerformed

    private void multiDownPageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_multiDownPageButtonActionPerformed
        int c = multiCharacterLabel.getText().codePointAt(2);
        c -= 256;
        if (c < 32) {
            c = 32;
        }
        updateMultiChar(c);
    }//GEN-LAST:event_multiDownPageButtonActionPerformed

    private void multiCustomRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_multiCustomRadioActionPerformed
        multiCustomFontField.setEnabled(true);
        multiChooseCustomFontButton.setEnabled(true);
    }//GEN-LAST:event_multiCustomRadioActionPerformed

    private void multiChooseCustomFontButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_multiChooseCustomFontButtonActionPerformed
        RwJFontChooser rwf = new RwJFontChooser(new JFrame());
        rwf.setVisible(true);
        Font daFont = rwf.getSelectedFont();
        if (daFont != null) {
            multiCustomFontField.setText(daFont.getFontName());
            tcr2.setFontName(daFont.getFontName());
            for (int b = 1; b < multigraphicTable.getColumnCount(); b++) {
                char q = (multigraphicTable.getValueAt(0, b) + "").charAt(0);
                for (int a = 0; a < multigraphicTable.getRowCount(); a++) {
                    multigraphicTable.setValueAt(((char) (q + a)) + "", a, b);
                }
            }
            multiCharacterLabel.setFont(daFont);
            punctCharacterLabel.setFont(daFont);
        }
    }//GEN-LAST:event_multiChooseCustomFontButtonActionPerformed

    private void multiUpCharButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_multiUpCharButtonActionPerformed
        int c = multiCharacterLabel.getText().codePointAt(2);
        c++;
        updateMultiChar(c);
    }//GEN-LAST:event_multiUpCharButtonActionPerformed

    private void graphemesPerGlyphSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_graphemesPerGlyphSpinnerStateChanged
        int numberOfGlyphs = ((Integer) graphemesPerGlyphSpinner.getValue()).intValue();
        if (multigraphicTable.getColumnCount() != numberOfGlyphs + 1) {
            DefaultTableModel defMod = (DefaultTableModel) multigraphicTable.getModel();
            defMod.setColumnCount(3);
            for (int a = 2; a < numberOfGlyphs; a++) {
                defMod.addColumn("Grapheme " + (a + 1));
            }
            for (int a = 0; a <= numberOfGlyphs; a++) {
                multigraphicTable.getColumnModel().getColumn(a).setCellRenderer(tcr2);
            }
        }
    }//GEN-LAST:event_graphemesPerGlyphSpinnerStateChanged

    private void multiDownCharButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_multiDownCharButtonActionPerformed
        int c = multiCharacterLabel.getText().codePointAt(2);
        c--;
        if (c < 32) {
            c = 32;
        }
        updateMultiChar(c);
    }//GEN-LAST:event_multiDownCharButtonActionPerformed

    private void multiUpPageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_multiUpPageButtonActionPerformed
        int c = multiCharacterLabel.getText().codePointAt(2);
        c += 256;
        updateMultiChar(c);
    }//GEN-LAST:event_multiUpPageButtonActionPerformed

    private void multiLeftGlyphButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_multiLeftGlyphButtonActionPerformed
        if (multigraphicTable.getSelectedColumn() < 1) {
            multigraphicTable.setColumnSelectionInterval(1, 1);
        }
        System.out.println(multigraphicTable.getSelectedColumn());
        if (multigraphicTable.getSelectedColumn() > 1) {
            multigraphicTable.setColumnSelectionInterval(multigraphicTable.getSelectedColumn() - 1, multigraphicTable.getSelectedColumn() - 1);
        }
    }//GEN-LAST:event_multiLeftGlyphButtonActionPerformed

    private void multiRightGlyphButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_multiRightGlyphButtonActionPerformed
        if (multigraphicTable.getSelectedColumn() < 1) {
            multigraphicTable.setColumnSelectionInterval(1, 1);
        }
        System.out.println(multigraphicTable.getSelectedColumn());
        if (multigraphicTable.getSelectedColumn() < multigraphicTable.getColumnCount() - 1) {
            multigraphicTable.setColumnSelectionInterval(multigraphicTable.getSelectedColumn() + 1, multigraphicTable.getSelectedColumn() + 1);
        }
    }//GEN-LAST:event_multiRightGlyphButtonActionPerformed

    private void multiSetGlyphButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_multiSetGlyphButtonActionPerformed
        if (multigraphicTable.getSelectedColumn() < 1) {
            multigraphicTable.setColumnSelectionInterval(1, 1);
        }
        try {
            int v = multiCharacterLabel.getText().codePointAt(2);
            multigraphicTable.setValueAt(new String(Character.toChars(v)), multigraphicTable.getSelectedRow(), multigraphicTable.getSelectedColumn());
        } catch (IndexOutOfBoundsException ex) {
            JOptionPane.showMessageDialog(this, "Please choose a place to set it!", null,
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_multiSetGlyphButtonActionPerformed

    private void multiAutoFillButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_multiAutoFillButtonActionPerformed
        if (multigraphicTable.getSelectedColumn() < 1) {
            multigraphicTable.setColumnSelectionInterval(1, 1);
        }
        Object[] o = new Object[multigraphicTable.getColumnCount()];
        for (int z = 0; z < multigraphicTable.getColumnCount(); z++) {
            o[z] = multigraphicTable.getValueAt(0, z);
        }
        if (o[0] == null) {
            JOptionPane.showMessageDialog(this, "Cannot start with null!", "Oops!", JOptionPane.ERROR_MESSAGE);
        } else {
            for (int z = 1; z < multigraphicTable.getColumnCount(); z++) {
                String s = o[z] + "";
                int c = s.codePointAt(0);
                for (int row = 0; row < multigraphicTable.getRowCount(); row++) {
                    multigraphicTable.setValueAt(new String(Character.toChars(c)), row, z);
                    c++;
                }
            }
        }
    }//GEN-LAST:event_multiAutoFillButtonActionPerformed

    private void multiUseEndersCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_multiUseEndersCheckboxActionPerformed
        if (multiUseEndersCheckbox.isSelected()) {
            DefaultTableModel model = (DefaultTableModel) multigraphicTable.getModel();
            HashMap ws = getWritingSystem();
            System.out.println(ws);
            HashMap phs = (HashMap) ws.get("Phonemes");
            if (!phs.containsKey("Phonemes")) {
                model.addRow(new String[]{"Enders", "", ""});
            }
        }
        if (!multiUseEndersCheckbox.isSelected()) {
            DefaultTableModel model = (DefaultTableModel) multigraphicTable.getModel();
            int rc = model.getRowCount();
            model.setRowCount(--rc);
        }
    }//GEN-LAST:event_multiUseEndersCheckboxActionPerformed

    private void multiJumpToFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_multiJumpToFieldActionPerformed
        int v = Integer.parseInt(multiJumpToField.getText(), 16);
        updateMultiChar(v);
    }//GEN-LAST:event_multiJumpToFieldActionPerformed

    private void fontFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fontFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fontFieldActionPerformed

    private void punctUpGlyphButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_punctUpGlyphButtonActionPerformed
        int selRow = punctuationTable.getSelectedRow();
        if (selRow - 1 >= 0) {
            punctuationTable.setRowSelectionInterval(selRow - 1, selRow - 1);
            punctuationTable.scrollRectToVisible(punctuationTable.getCellRect(selRow - 1, 0, true));
        } else {
            selRow = punctuationTable.getRowCount() - 1;
            punctuationTable.setRowSelectionInterval(selRow, selRow);
            punctuationTable.scrollRectToVisible(punctuationTable.getCellRect(selRow, 0, true));
        }
    }//GEN-LAST:event_punctUpGlyphButtonActionPerformed

    private void multiDownButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_multiDownButton1ActionPerformed
        int selRow = punctuationTable.getSelectedRow();
        if (selRow + 1 < punctuationTable.getRowCount()) {
            punctuationTable.setRowSelectionInterval(selRow + 1, selRow + 1);
            punctuationTable.scrollRectToVisible(punctuationTable.getCellRect(selRow + 1, 0, true));
        } else {
            selRow = 0;
            punctuationTable.setRowSelectionInterval(selRow, selRow);
            punctuationTable.scrollRectToVisible(punctuationTable.getCellRect(selRow, 0, true));
        }
    }//GEN-LAST:event_multiDownButton1ActionPerformed

    private void punctSetGlyphButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_punctSetGlyphButtonActionPerformed
        if (punctuationTable.getSelectedColumn() < 1) {
            punctuationTable.setColumnSelectionInterval(1, 1);
        }
        try {
            int v = punctCharacterLabel.getText().codePointAt(2);
            punctuationTable.setValueAt(new String(Character.toChars(v)) + "", punctuationTable.getSelectedRow(), punctuationTable.getSelectedColumn());
        } catch (IndexOutOfBoundsException ex) {
            JOptionPane.showMessageDialog(this, "Please choose a place to set it!", null,
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_punctSetGlyphButtonActionPerformed

    private void punctiDownPageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_punctiDownPageButtonActionPerformed
        int c = punctCharacterLabel.getText().codePointAt(2);
        c -= 256;
        if (c < 32) {
            c = 32;
        }
        updatePunctChar(c);
    }//GEN-LAST:event_punctiDownPageButtonActionPerformed

    private void punctDownCharButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_punctDownCharButtonActionPerformed
        int c = punctCharacterLabel.getText().codePointAt(2);
        c--;
        if (c < 32) {
            c = 32;
        }
        updatePunctChar(c);
    }//GEN-LAST:event_punctDownCharButtonActionPerformed

    private void punctUpCharButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_punctUpCharButtonActionPerformed
        int c = punctCharacterLabel.getText().codePointAt(2);
        c++;
        updatePunctChar(c);
    }//GEN-LAST:event_punctUpCharButtonActionPerformed

    private void punctUpPageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_punctUpPageButtonActionPerformed
        int c = punctCharacterLabel.getText().codePointAt(2);
        c += 256;
        updatePunctChar(c);
    }//GEN-LAST:event_punctUpPageButtonActionPerformed

    private void punctAutoFillButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_punctAutoFillButtonActionPerformed
        Object o = punctuationTable.getValueAt(0, 1);
        if (o == null) {
            JOptionPane.showMessageDialog(this, "Can not start with null!", "Oops!", JOptionPane.ERROR_MESSAGE);
        } else {
            String s = o + "";
            int q = ((String) punctuationTable.getValueAt(0, 1)).codePointAt(0);
            for (int row = 0; row < punctuationTable.getRowCount(); row++) {
                int c = ((String) punctuationTable.getValueAt(row, 0)).codePointAt(0) - 32;
                punctuationTable.setValueAt(new String(Character.toChars(c + q)), row, 1);
            }
        }
    }//GEN-LAST:event_punctAutoFillButtonActionPerformed

    private void punctJumpToFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_punctJumpToFieldActionPerformed
        int v = Integer.parseInt(punctJumpToField.getText(), 16);
        updatePunctChar(v);
    }//GEN-LAST:event_punctJumpToFieldActionPerformed

    @SuppressWarnings("CallToThreadDumpStack")
    private void writingSystemPaneStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_writingSystemPaneStateChanged
        if (writingSystemPane.getSelectedIndex() < 5) {
            writingSystem.put("System", writingSystemPane.getTitleAt(writingSystemPane.getSelectedIndex()));
        }
        System.out.println(writingSystem.get("System"));
        if (writingSystemPane.getSelectedIndex() == 7) {
            File here = new File(".");
            File[] wsList = here.listFiles(new rwWsFileFilter());
            DefaultTableModel dtm = (DefaultTableModel) userDefdTable.getModel();
            //userDefdTable.removeColumn(userDefdTable.getColumn("Sample Text"));
            dtm.setRowCount(0);
            for (int a = 0; a < wsList.length; a++) {
                try {
                    InputSource ins = new InputSource(wsList[a].getPath());
                    XPath xpath = XPathFactory.newInstance().newXPath();
                    NodeList list = (NodeList) xpath.evaluate("//System/text()", ins, XPathConstants.NODESET);
                    dtm.addRow(new String[]{wsList[a].getName().substring(0, wsList[a].getName().length() - 4), list.item(0).getNodeValue(), wsList[a].getCanonicalPath()});
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }//GEN-LAST:event_writingSystemPaneStateChanged

    private void syllabaryCustomFontFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_syllabaryCustomFontFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_syllabaryCustomFontFieldActionPerformed

    private void vowelCarrierCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vowelCarrierCheckActionPerformed
        if (vowelCarrierCheck.isSelected()) {
            DefaultTableModel model = (DefaultTableModel) abugidaTable.getModel();
            model.addRow(new String[]{"VowelCarrier", "", ""});
        }
        if (!vowelCarrierCheck.isSelected()) {
            DefaultTableModel model = (DefaultTableModel) abugidaTable.getModel();
            int rc = model.getRowCount();
            model.setRowCount(--rc);
        }
    }//GEN-LAST:event_vowelCarrierCheckActionPerformed

    private void noVowelCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noVowelCheckboxActionPerformed
        if (noVowelCheckbox.isSelected()) {
            DefaultTableModel model = (DefaultTableModel) abugidaTable.getModel();
            model.addRow(new String[]{"NoVowel", "", ""});
        }
        if (!noVowelCheckbox.isSelected()) {
            DefaultTableModel model = (DefaultTableModel) abugidaTable.getModel();
            int rc = model.getRowCount();
            model.setRowCount(--rc);
        }
    }//GEN-LAST:event_noVowelCheckboxActionPerformed

    private void abjadVowelCarrierCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_abjadVowelCarrierCheckActionPerformed
        if (abjadVowelCarrierCheck.isSelected()) {
            DefaultTableModel model = (DefaultTableModel) abjadTable.getModel();
            model.addRow(new String[]{"VowelCarrier", "", ""});
        }
        if (!abjadVowelCarrierCheck.isSelected()) {
            DefaultTableModel model = (DefaultTableModel) abjadTable.getModel();
            int rc = model.getRowCount();
            model.setRowCount(--rc);
        }
    }//GEN-LAST:event_abjadVowelCarrierCheckActionPerformed

    private void useSystemButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_useSystemButtonActionPerformed
        String tabs = "Alphabet    Abjad       Abugida     Syllabary   Multigraphic";
        String[] writeSyst = (String[]) presets.elementAt(presetsTable.getSelectedRow());
        for (int a = 0; a < writeSyst.length; a++) {
            System.out.println(writeSyst[a]);
        }
        if (writeSyst[1].equals("Alphabet")) {
            String glyphuses = "IMFSUL";
            alphaGlyphsPerLetterSpinner.setValue(writeSyst[10].length());
            for (int a = 1; a < writeSyst[10].length() + 1; a++) {
                alphaTable.setValueAt(new String(Character.toChars(Integer.parseInt(writeSyst[3 + a], 16))), 0, a);
                alphaLetterGlyphNumberCombo.setSelectedIndex(a - 1);
                alphaLetterUsedInCombo.setSelectedIndex(glyphuses.indexOf(writeSyst[10].charAt(a - 1)));
            }
            if (alphaGlyphUseMap.containsValue("Upper")) {
                capitalizationCombo.setSelectedIndex(2);
            }
            borrowedCharsRadio.setSelected(true);
            punctuationTable.setValueAt(new String(Character.toChars(Integer.parseInt(writeSyst[3], 16))), 0, 1);
            punctAutoFillButtonActionPerformed(evt);
            autofillAlphabetButtonActionPerformed(evt);
            if (writeSyst[writeSyst.length - 1].equals("rtl")) {
                directionalityButton.setSelected(true);
            }
        }
        if (writeSyst[1].equals("Abjad")) {
            String glyphuses = "IiMmFSUL";
            abjadGlyphsPerConsonantSpinner.setValue(writeSyst[10].length());
            for (int a = 1; a < writeSyst[10].length() + 1; a++) {
                abjadTable.setValueAt(new String(Character.toChars(Integer.parseInt(writeSyst[3 + a], 16))), 0, a);
                abjadConsonantGlyphNumberCombo.setSelectedIndex(a - 1);
                abjadConsonantUsedInCombo.setSelectedIndex(glyphuses.indexOf(writeSyst[10].charAt(a - 1)));
            }
            borrowedAbjadFontRadio.setSelected(true);
            punctuationTable.setValueAt(new String(Character.toChars(Integer.parseInt(writeSyst[3], 16))), 0, 1);
            punctAutoFillButtonActionPerformed(evt);
            autofillAbjadButtonActionPerformed(evt);
        }
        if (writeSyst[1].equals("Abugida")) {
            abugidaTable.setValueAt(new String(Character.toChars(Integer.parseInt(writeSyst[4], 16))), 0, 1);
            punctuationTable.setValueAt(new String(Character.toChars(Integer.parseInt(writeSyst[3], 16))), 0, 1);
            punctAutoFillButtonActionPerformed(evt);
            abugidaAutoFillButtonActionPerformed(evt);
        }
        if (writeSyst[1].equals("Syllabary")) {
            syllabaryTable.setValueAt(new String(Character.toChars(Integer.parseInt(writeSyst[2], 16))), 0, 1);
            syllabaryCustomFontCheck.setSelected(false);
            autoFillSyllabaryButtonActionPerformed(evt);
        }
        if (writeSyst[1].equals("Multigraphic")) {
            graphemesPerGlyphSpinner.setValue(writeSyst[10].length());
            for (int a = 1; a < writeSyst[10].length() + 1; a++) {
                multigraphicTable.setValueAt(new String(Character.toChars(Integer.parseInt(writeSyst[3 + a], 16))), 0, a);
            }
            multiUseEndersCheckbox.setSelected(true);
            punctuationTable.setValueAt(new String(Character.toChars(Integer.parseInt(writeSyst[3], 16))), 0, 1);
            punctAutoFillButtonActionPerformed(evt);
            multiUseEndersCheckboxActionPerformed(evt);
            multiAutoFillButtonActionPerformed(evt);
        }
        writingSystemPane.setSelectedIndex(tabs.indexOf(writeSyst[1]) / 12);
    }//GEN-LAST:event_useSystemButtonActionPerformed

    private void directionalityButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_directionalityButtonActionPerformed
        if (directionalityButton.isSelected()) {
            directionalityButton.setText("L ⬅ R");
            writingSystem.put("Directionality", "rtl");
        } else {
            directionalityButton.setText("L ➡ R");
            writingSystem.put("Directionality", "ltr");
        }
    }//GEN-LAST:event_directionalityButtonActionPerformed

    @SuppressWarnings("CallToThreadDumpStack")
    private void loadWsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadWsButtonActionPerformed
        if (wsFile == null) {
            wsFile = new File(".");
        }
        if (writingSystemPane.getSelectedIndex() != 7) {
            writingSystemPane.setSelectedIndex(7);
            File here = new File(".");
            File[] wsList = here.listFiles(new rwWsFileFilter());
            DefaultTableModel dtm = (DefaultTableModel) userDefdTable.getModel();
            //removeColumn(userDefdTable.getColumn("Sample Text"));
            dtm.setRowCount(0);
            for (int a = 0; a < wsList.length; a++) {
                try {
                    InputSource ins = new InputSource(wsList[a].getPath());
                    XPath xpath = XPathFactory.newInstance().newXPath();
                    NodeList list = (NodeList) xpath.evaluate("//System/text()", ins, XPathConstants.NODESET);
                    dtm.addRow(new String[]{wsList[a].getName().substring(0, wsList[a].getName().length() - 4), list.item(0).getNodeValue(), wsList[a].getCanonicalPath()});
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            //userDefdTable.addColumn(userDefdTable.getColumn("Sample Text"));
            if (wsFile.getPath() == ".") {
                wsFile = new File(userDefdTable.getValueAt(userDefdTable.getSelectedRow(), 2) + "");
            }
            SAXParserFactory factory = SAXParserFactory.newInstance();
            try {
                SAXParser saxParser = factory.newSAXParser();
                DefaultHandler handler = new DefaultHandler() {

                    boolean numberOfLettersTag = false;
                    boolean capRuleTag = false;
                    boolean phonemeTag = false;
                    boolean phonemesTag = false;
                    boolean fontTag = false;
                    boolean fontTypeTag = false;
                    boolean glyphUseTag = false;
                    boolean numberGlyphsTag = false;
                    boolean vowelsShownTag = false;
                    boolean systemTag = false;
                    boolean pkey = false;
                    boolean pvalue = false;
                    boolean numberGraphemesTag = false;
                    boolean punctTag = false;
                    boolean glyph1Tag = false;
                    boolean glyph2Tag = false;
                    boolean glyph3Tag = false;
                    boolean glyph4Tag = false;
                    boolean syllablesTag = false;
                    boolean syllableTag = false;
                    boolean skeyTag = false;
                    boolean svalueTag = false;
                    boolean vowelsTag = false;
                    boolean consonantsTag = false;
                    HashMap phnms = new HashMap();
                    HashMap sylbs = new HashMap();
                    String key = "";
                    String value = "";
                    String skey = "";
                    String svalue = "";

                    public void startElement(String uri, String localName, String qName,
                            Attributes attributes) throws SAXException {
                        if (qName.equalsIgnoreCase("NumberOfLetters")) {
                            numberOfLettersTag = true;
                        }
                        if (qName.equalsIgnoreCase("CapRule")) {
                            capRuleTag = true;
                        }
                        if (qName.equalsIgnoreCase("Phonemes")) {
                            phonemeTag = true;
                        }
                        if (qName.equalsIgnoreCase("pkey")) {
                            pkey = true;
                        }
                        if (qName.equalsIgnoreCase("Font")) {
                            fontTag = true;
                        }
                        if (qName.equalsIgnoreCase("FontType")) {
                            fontTypeTag = true;
                        }
                        if (qName.equalsIgnoreCase("GlyphUseMap")) {
                            glyphUseTag = true;
                        }
                        if (qName.equalsIgnoreCase("Glyph_1")) {
                            glyph1Tag = true;
                        }
                        if (qName.equalsIgnoreCase("Glyph_2")) {
                            glyph2Tag = true;
                        }
                        if (qName.equalsIgnoreCase("Glyph_3")) {
                            glyph3Tag = true;
                        }
                        if (qName.equalsIgnoreCase("Glyph_4")) {
                            glyph4Tag = true;
                        }
                        if (qName.equalsIgnoreCase("GlyphsPerLetter")) {
                            numberGlyphsTag = true;
                        }
                        if (qName.equalsIgnoreCase("System")) {
                            systemTag = true;
                        }
                        if (qName.equalsIgnoreCase("pvalue")) {
                            pvalue = true;
                        }
                        if (qName.equalsIgnoreCase("phonemes")) {
                            phonemesTag = true;
                        }
                        if (qName.equalsIgnoreCase("graphemesperglyph")) {
                            numberGraphemesTag = true;
                        }
                        if (qName.equalsIgnoreCase("Punctuation")) {
                            punctTag = true;
                        }
                        if (qName.equalsIgnoreCase("Syllables")) {
                            syllablesTag = true;
                        }
                        if (qName.equalsIgnoreCase("skey")) {
                            skeyTag = true;
                        }
                        if (qName.equalsIgnoreCase("svalue")) {
                            svalueTag = true;
                        }
                        if (qName.equalsIgnoreCase("Vowels")) {
                            vowelsTag = true;
                        }
                        if (qName.equalsIgnoreCase("Consonants")) {
                            consonantsTag = true;
                        }
                    }

                    public void endElement(String uri, String localName, String qName)
                            throws SAXException {
                        //System.out.print("E:");
                    }

                    public void characters(char ch[], int start, int length) throws SAXException {
                        if (numberOfLettersTag) {
                            writingSystem.put("NumberOfLetters", new String(ch, start, length));
                            numberOfLettersTag = false;
                        }
                        if (capRuleTag) {
                            writingSystem.put("CapRule", new String(ch, start, length));
                            capRuleTag = false;
                        }
                        if (phonemesTag) {
                            writingSystem.put("Phonemes", phnms);
                            phonemesTag = false;
                        }
                        if (pkey) {
                            String coded = new String(ch, start, length);
                            //key = ((char)Integer.parseInt(coded,16))+"";
                            key = rwStringConverter.convertFrom64(coded);
                            pkey = false;
                        }
                        if (pvalue) {
                            String coded = new String(ch, start, length);
                            value = rwStringConverter.convertFromHex(coded);
                            //System.out.println(value + " for " + key);
                            phnms.put(key, value);
                            pvalue = false;
                        }
                        if (fontTag) {
                            String f = new String(ch, start, length);
                            writingSystem.put("Font", f);
                            fontTag = false;
                        }
                        if (fontTypeTag) {
                            String f = new String(ch, start, length);
                            writingSystem.put("FontType", f);
                            fontTypeTag = false;
                        }
                        if (systemTag) {
                            String syst = new String(ch, start, length);
                            writingSystem.put("System", syst);
                            systemTag = false;
                        }
                        if (numberGlyphsTag) {
                            writingSystem.put("GlyphsPerLetter", new String(ch, start, length));
                            numberGlyphsTag = false;
                        }
                        if (numberGraphemesTag) {
                            writingSystem.put("GraphemesPerGlyph", new String(ch, start, length));
                            numberGraphemesTag = false;
                        }
                        if (punctTag) {
                            String punct = new String(ch, start, length);
                            HashMap punctHash = new HashMap();
                            for (String keyValue : punct.split(" *, *")) {
                                String[] pairs = keyValue.split(" *e *", 2);
                                punctHash.put(rwStringConverter.convertFromHex(pairs[0]), pairs.length == 1 ? "" : rwStringConverter.convertFromHex(pairs[1]));
                            }
                            writingSystem.put("Punctuation", punctHash);
                            punctTag = false;
                        }
                        if (glyph1Tag) {
                            HashMap gum = ((HashMap) writingSystem.get("GlyphUseMap"));
                            System.out.println(gum);
                            gum.put("Glyph_1", new String(ch, start, length));
                            writingSystem.put("GlyphUseMap", gum);
                            glyph1Tag = false;
                        }
                        if (glyph2Tag) {
                            HashMap gum = ((HashMap) writingSystem.get("GlyphUseMap"));
                            gum.put("Glyph_2", new String(ch, start, length));
                            writingSystem.put("GlyphUseMap", gum);
                            glyph2Tag = false;
                        }
                        if (glyph3Tag) {
                            HashMap gum = ((HashMap) writingSystem.get("GlyphUseMap"));
                            gum.put("Glyph_3", new String(ch, start, length));
                            writingSystem.put("GlyphUseMap", gum);
                            glyph3Tag = false;
                        }
                        if (glyph4Tag) {
                            HashMap gum = ((HashMap) writingSystem.get("GlyphUseMap"));
                            gum.put("Glyph_4", new String(ch, start, length));
                            writingSystem.put("GlyphUseMap", gum);
                            glyph4Tag = false;
                        }
                        if (glyphUseTag) {
                            HashMap gum = new HashMap();
                            writingSystem.put("GlyphUseMap", gum);
                            glyphUseTag = false;
                        }
                        if (syllablesTag) {
                            writingSystem.put("Syllables", sylbs);
                            System.out.println(sylbs);
                            syllablesTag = false;
                        }
                        if (skeyTag) {
                            skey = new String(ch, start, length);
                            skeyTag = false;
                        }
                        if (svalueTag) {
                            String coded = new String(ch, start, length);
                            svalue = rwStringConverter.convertFromHex(coded);
                            sylbs.put(skey, svalue);
                            svalueTag = false;
                        }
                        if (vowelsTag) {
                            vows.clear();
                            String v = new String(ch, start, length);
                            String[] va = v.split(",");
                            for (int a = 0; a < va.length; a++) {
                                if (va[a].contains(" ")) {
                                    String[] vb = va[a].split(" ");
                                    String vc = "";
                                    for (int b = 0; b < vb.length; b++) {
                                        vc += (char) Integer.parseInt(vb[b], 16);
                                    }
                                } else {
                                    vows.add(((char) Integer.parseInt(va[a], 16)) + "");
                                }
                            }
                            vowelsTag = false;
                        }
                        if (consonantsTag) {
                            cons.clear();
                            String v = new String(ch, start, length);
                            String[] va = v.split(",");
                            for (int a = 0; a < va.length; a++) {
                                if (va[a].contains(" ")) {
                                    String[] vb = va[a].split(" ");
                                    String vc = "";
                                    for (int b = 0; b < vb.length; b++) {
                                        vc += (char) Integer.parseInt(vb[b], 16);
                                    }
                                    cons.add(vc);
                                } else {
                                    cons.add(((char) Integer.parseInt(va[a], 16)) + "");
                                }
                            }
                            consonantsTag = false;
                        }
                    }

                    public void endDocument() {
                        wsLoaded = true;
                    }
                };
                saxParser.parse(wsFile, handler);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            setupSystem(vows, cons, writingSystem);
        }
    }//GEN-LAST:event_loadWsButtonActionPerformed

    private void saveWsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveWsButtonActionPerformed
        writingSystem = getWritingSystem();
        JFileChooser jfc = new JFileChooser(".");
        String wsName = "";
        int chosenOption = jfc.showSaveDialog(this);
        if (chosenOption != JFileChooser.CANCEL_OPTION) {
            wsName = jfc.getSelectedFile().getPath();
            if (wsName.endsWith(".rws")) {
                wsName = wsName.substring(0, wsName.length() - 4);
                setTitle("Random Language Generator " + jfc.getSelectedFile().getName());
            }
        }
        try {
            String fileName = wsName;
            if (!fileName.endsWith(".rws")) {
                fileName += ".rws";
            }
            PrintWriter out = new PrintWriter(new File(fileName));
            StreamResult streamResult = new StreamResult(out);
            SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
            TransformerHandler hd = tf.newTransformerHandler();
            Transformer serializer = hd.getTransformer();
            serializer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            serializer.setOutputProperty(OutputKeys.INDENT, "Yes");
            serializer.setOutputProperty(OutputKeys.STANDALONE, "Yes");
            hd.setResult(streamResult);
            hd.startDocument();
            AttributesImpl atts = new AttributesImpl();
            hd.startElement("", "", "WritingSystem", atts);
            hd.startElement("", "", "System", atts);
            String s = (String) writingSystem.get("System");
            hd.characters(s.toCharArray(), 0, s.length());
            hd.endElement("", "", "System");
            hd.startElement("", "", "FontType", atts);
            String ft = (String) writingSystem.get("FontType");
            hd.characters(ft.toCharArray(), 0, ft.length());
            hd.endElement("", "", "FontType");
            hd.startElement("", "", "Font", atts);
            ft = (String) writingSystem.get("Font");
            hd.characters(ft.toCharArray(), 0, ft.length());
            hd.endElement("", "", "Font");
            hd.startElement("", "", "Vowels", atts);
            String daVowels = "";
            for (int a = 0; a < vows.size(); a++) {
                String uc = "";
                String ub = "";
                for (int b = 0; b < ((String) vows.elementAt(a)).length(); b++) {
                    uc = Integer.toHexString((int) Character.codePointAt((CharSequence) vows.elementAt(a), b));
                    if (ub.length() > 0) {
                        ub += " ";
                    }
                    ub += uc;
                }
                daVowels += ub;
                if (a < vows.size() - 1) {
                    daVowels += ",";
                }
            }
            hd.startCDATA();
            hd.characters(daVowels.toCharArray(), 0, daVowels.length());
            hd.endCDATA();
            hd.endElement("", "", "Vowels");
            hd.startElement("", "", "Consonants", atts);
            String cnsnts = "";
            for (int a = 0; a < cons.size(); a++) {
                String uc = "";
                String ub = "";
                for (int b = 0; b < ((String) cons.elementAt(a)).length(); b++) {
                    uc = Integer.toHexString((int) Character.codePointAt((CharSequence) cons.elementAt(a), b));
                    if (ub.length() > 0) {
                        ub += " ";
                    }
                    ub += uc;
                }
                cnsnts += ub;
                if (a < cons.size() - 1) {
                    cnsnts += ",";
                }
            }
            hd.startCDATA();
            hd.characters(cnsnts.toCharArray(), 0, cnsnts.length());
            hd.endCDATA();
            hd.endElement("", "", "Consonants");
            if (!s.equals("System")) {
                hd.startElement("", "", "NumberOfLetters", atts);
                String nl = writingSystem.get("NumberOfLetters") + "";
                hd.characters(nl.toCharArray(), 0, nl.length());
                hd.endElement("", "", "NumberOfLetters");
                String nc = "0";
                String sq = (String) writingSystem.get("System");
                if (sq.equals("Alphabet") || sq.equals("Abjad")) {
                    hd.startElement("", "", "GlyphsPerLetter", atts);
                    nc = writingSystem.get("GlyphsPerLetter") + "";
                    hd.characters(nc.toCharArray(), 0, nc.length());
                    hd.endElement("", "", "GlyphsPerLetter");
                }
                if (sq.equals("Multigraphic")) {
                    hd.startElement("", "", "GraphemesPerGlyph", atts);
                    nc = writingSystem.get("GraphemesPerGlyph") + "";
                    hd.characters(nc.toCharArray(), 0, nc.length());
                    hd.endElement("", "", "GraphemesPerGlyph");
                }
                if (((HashMap) writingSystem).containsKey("GlyphUseMap")) {
                    hd.startElement("", "", "GlyphUseMap", atts);
                    HashMap gum = (HashMap) writingSystem.get("GlyphUseMap");
                    for (int numGlyphs = 0; numGlyphs < Integer.parseInt(nc); numGlyphs++) {
                        hd.startElement("", "", "Glyph_" + (numGlyphs + 1), atts);
                        String gp = (String) gum.get("Glyph_" + (numGlyphs + 1));
                        hd.characters(gp.toCharArray(), 0, gp.length());
                        hd.endElement("", "", "Glyph_" + (numGlyphs + 1));
                    }
                    hd.endElement("", "", "GlyphUseMap");
                }
                if (s.equals("Alphabet")) {
                    hd.startElement("", "", "CapRule", atts);
                    String cr = writingSystem.get("CapRule") + "";
                    hd.characters(cr.toCharArray(), 0, cr.length());
                    hd.endElement("", "", "CapRule");
                }
                if (((HashMap) writingSystem).containsKey("Phonemes")) {
                    HashMap phns = (HashMap) writingSystem.get("Phonemes");
                    hd.startElement("", "", "Phonemes", atts);
                    Object[] keys = phns.keySet().toArray();
                    for (int phn = 0; phn < keys.length; phn++) {
                        hd.startElement("", "", "Phoneme", atts);
                        hd.startElement("", "", "pkey", atts);
                        String hky = rwStringConverter.convertTo64(keys[phn] + "");
                        hd.startCDATA();
                        hd.characters(hky.toCharArray(), 0, hky.length());
                        hd.endCDATA();
                        hd.endElement("", "", "pkey");
                        hd.startElement("", "", "pvalue", atts);
                        String phone = phns.get(keys[phn]).toString();
                        byte[] bs = phone.getBytes("UTF-8");
                        String hexes = "0123456789ABCDEF";
                        StringBuilder sb = new StringBuilder(2 * bs.length);
                        for (byte b : bs) {
                            sb.append(hexes.charAt((b & 0xF0) >> 4)).append(hexes.charAt((b & 0x0F)));
                        }
                        hd.startCDATA();
                        hd.characters(sb.toString().toCharArray(), 0, sb.toString().length());
                        hd.endCDATA();
                        hd.endElement("", "", "pvalue");
                        hd.endElement("", "", "Phoneme");
                    }
                    hd.endElement("", "", "Phonemes");
                }
            }
            if (s.equals("Abjad")) {
                hd.startElement("", "", "VowelsShown", atts);
                String vs = writingSystem.get("VowelsShown") + "";
                hd.characters(vs.toCharArray(), 0, vs.length());
                hd.endElement("", "", "VowelsShown");
            }
            if (s.equals("Abugida")) {
            }
            if (s.equals("Syllabary")) {
                hd.startElement("", "", "NumberOfSyllables", atts);
                String ns = writingSystem.get("NumberOfSyllables") + "";
                hd.characters(ns.toCharArray(), 0, ns.length());
                hd.endElement("", "", "NumberOfSyllables");
                hd.startElement("", "", "Syllables", atts);
                HashMap sylbls = (HashMap) writingSystem.get("Syllables");
                Object[] keys = sylbls.keySet().toArray();
                for (int sc = 0; sc < (Integer.parseInt(ns)); sc++) {
                    hd.startElement("", "", "Syllable", atts);
                    hd.startElement("", "", "skey", atts);
                    String ky = keys[sc].toString();
                    System.out.println(ky + " " + sc);
                    hd.characters(ky.toCharArray(), 0, ky.length());
                    hd.endElement("", "", "skey");
                    hd.startElement("", "", "svalue", atts);
                    String syllable = sylbls.get(ky).toString();
                    byte[] bs = syllable.getBytes("UTF-8");
                    String hexes = "0123456789ABCDEF";
                    StringBuilder sb = new StringBuilder(2 * bs.length);
                    for (byte b : bs) {
                        sb.append(hexes.charAt((b & 0xF0) >> 4)).append(hexes.charAt((b & 0x0F)));
                    }
                    //String vl = sylbls.get(ky).toString();
                    hd.characters(sb.toString().toCharArray(), 0, sb.toString().length());
                    hd.endElement("", "", "svalue");
                    hd.endElement("", "", "Syllable");
                }
                hd.endElement("", "", "Syllables");
            }
            if (s.equals("Multigraphic")) {
            }
            hd.startElement("", "", "Punctuation", atts);
            HashMap punct = (HashMap) writingSystem.get("Punctuation");
            System.out.println(writingSystem);
            String pm = "";
            for (Object key : punct.keySet()) {
                pm += rwStringConverter.convertToHex(key + "") + "e"
                        + rwStringConverter.convertToHex(punct.get(key) + "") + ", ";
            }
            pm = pm.substring(0, pm.length() - 2);
            //pm = rwStringConverter.convertTo64(pm);
            hd.startCDATA();
            hd.characters(pm.toCharArray(), 0, pm.length());
            hd.endCDATA();
            hd.endElement("", "", "Punctuation");
            hd.endElement("", "", "WritingSystem");
            hd.endDocument();
            out.close();
        } catch (Exception ex) {
            System.out.println(ex);
            ex.printStackTrace();
        }
    }//GEN-LAST:event_saveWsButtonActionPerformed

    private void browse4WsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browse4WsButtonActionPerformed
        JFileChooser jfc = new JFileChooser(".");
        FileNameExtensionFilter fnef = new FileNameExtensionFilter("Random Languange Generator Writing System files", "rws");
        jfc.addChoosableFileFilter(fnef);
        int chosenOption = jfc.showOpenDialog(this);
        if (chosenOption != JFileChooser.CANCEL_OPTION) {
            wsFile = jfc.getSelectedFile();
            loadWsButtonActionPerformed(evt);
        }
    }//GEN-LAST:event_browse4WsButtonActionPerformed

    public void updateAlphaChar(int s) {
        String ss = new String(Character.toChars(s));
        characterLabel.setText("XX" + ss + "XX");
        alphaCurrentVLabel.setText(Integer.toHexString((int) s));
    }

    public void updateSyllabaryChar(int s) {
        String ss = new String(Character.toChars(s));
        visibleCharLabel.setText("XX" + ss + "XX");
        syllabaryCharValueLabel.setText(Integer.toHexString((int) s));
    }

    public void updateAbjadChar(int s) {
        String ss = new String(Character.toChars(s));
        abjadCharLabel.setText("XX" + ss + "XX");
        abjadCurrentVLabel.setText(Integer.toHexString((int) s));
    }

    public void updateAbugidaChar(int s) {
        String ss = new String(Character.toChars(s));
        abugidaCharLabel.setText("XX" + ss + "XX");
        abugidaCurrentVLabel.setText(Integer.toHexString((int) s));
    }

    public void updateMultiChar(int s) {
        String ss = new String(Character.toChars(s));
        multiCharacterLabel.setText("XX" + ss + "XX");
        currentHexValueLabel.setText(Integer.toHexString((int) s));
    }

    public void updatePunctChar(int s) {
        String ss = new String(Character.toChars(s));
        punctCharacterLabel.setText("XX" + ss + "XX");
        punctCurrentHexValueLabel.setText(Integer.toHexString((int) s));
    }

    public HashMap getWritingSystem() {
        if (wsLoaded) {
            return writingSystem;
        } else {
            HashMap ws = new HashMap();
            HashMap phonemes = new HashMap();
            String writeSyst = (String) writingSystem.get("System");
            if (directionalityButton.isSelected()) {
                ws.put("Directionality", "rtl");
            } else {
                ws.put("Directionality", "ltr");
            }
            if (writeSyst.equals("Alphabet")) {
                ws.put("System", "Alphabet");
                ws.put("GlyphsPerLetter", alphaGlyphsPerLetterSpinner.getValue());
                ws.put("GlyphUseMap", alphaGlyphUseMap);
                ws.put("NumberOfLetters", alphaTable.getRowCount());
                for (int a = 0; a < alphaTable.getRowCount(); a++) {
                    StringBuilder z = new StringBuilder("");
                    for (int b = 1; b < alphaTable.getColumnCount(); b++) {
                        String q = alphaTable.getValueAt(a, b) + " ";
                        z.append(q);
                    }
                    phonemes.put(alphaTable.getValueAt(a, 0), z);
                }
                ws.put("Phonemes", phonemes);
                if (!fontField.getText().contains("LCS-ConstructorII")) {
                    ws.put("Font", fontField.getText());
                    ws.put("FontType", "custom");
                } else {
                    ws.put("Font", "LCS-ConstructorII");
                    ws.put("FontType", "borrowed");
                }
                ws.put("CapRule", capitalizationCombo.getSelectedItem());
            } else if (writeSyst.equals("Abjad")) {
                ws.put("System", "Abjad");
                ws.put("GlyphsPerLetter", abjadGlyphsPerConsonantSpinner.getValue());
                ws.put("GlyphUseMap", abjadGlyphUseMap);
                ws.put("NumberOfLetters", abjadTable.getRowCount());
                for (int a = 0; a < abjadTable.getRowCount(); a++) {
                    StringBuilder z = new StringBuilder("");
                    for (int b = 1; b < abjadTable.getColumnCount(); b++) {
                        String q = (abjadTable.getValueAt(a, b) + "").trim() + " ";
                        z.append(q);
                    }
                    phonemes.put(abjadTable.getValueAt(a, 0), z);
                }
                ws.put("Phonemes", phonemes);
                if (!abjadFontField.getText().contains("LCS-ConstructorII")) {
                    ws.put("Font", abjadFontField.getText());
                    ws.put("FontType", "custom");
                } else {
                    ws.put("Font", "LCS-ConstructorII");
                    ws.put("FontType", "borrowed");
                }
                ws.put("VowelsShown", true);
            } else if (writeSyst.equals("Abugida")) {
                ws.put("System", "Abugida");
                ws.put("GlyphsPerLetter", 1);
                ws.put("NumberOfLetters", abugidaTable.getRowCount());
                for (int a = 0; a < abugidaTable.getRowCount(); a++) {
                    StringBuilder z = new StringBuilder("");
                    for (int b = 1; b < abugidaTable.getColumnCount(); b++) {
                        String q = abugidaTable.getValueAt(a, b) + "";
                        z.append(q);
                    }
                    phonemes.put(abugidaTable.getValueAt(a, 0), z);
                }
                ws.put("Phonemes", phonemes);
                if (!abugidaFontField.getText().contains("LCS-ConstructorII")) {
                    ws.put("Font", abugidaFontField.getText());
                    ws.put("FontType", "custom");
                } else {
                    ws.put("Font", "LCS-ConstructorII");
                    ws.put("FontType", "borrowed");
                }
            } else if (writeSyst.equals("Syllabary")) {
                if (syllabaryCustomFontCheck.isSelected()) {
                    ws.put("Font", syllabaryCustomFontField.getText());
                } else {
                    ws.put("Font", "LCS-ConstructorII");
                }
                ws.put("System", "Syllabary");
                ws.put("NumberOfSyllables", syllabaryTable.getRowCount() * (syllabaryTable.getColumnCount() - 1));
                HashMap syllables = new HashMap();
                for (int a = 0; a < syllabaryTable.getRowCount(); a++) {
                    for (int b = 1; b < syllabaryTable.getColumnCount(); b++) {
                        String syllable = syllabaryTable.getValueAt(a, 0) + ""
                                + syllabaryTable.getColumnName(b);
                        syllables.put(syllable, syllabaryTable.getValueAt(a, b));
                    }
                }
                ws.put("Syllables", syllables);
                if (syllabaryCustomFontCheck.isSelected()) {
                    ws.put("FontType", "custom");
                    ws.put("Font", syllabaryCustomFontField.getText());
                } else {
                    ws.put("FontType", "borrowed");
                }
            } else if (writeSyst.equals("Multigraphic")) {
                ws.put("System", "Multigraphic");
                ws.put("GraphemesPerGlyph", graphemesPerGlyphSpinner.getValue());
                ws.put("Font", multiCustomFontField.getText());
                if (multiBorrowedRadio.isSelected()) {
                    ws.put("FontType", "Borrowed");
                } else {
                    ws.put("FontType", "Custom");
                }
                ws.put("NumberOfLetters", multigraphicTable.getRowCount());
                for (int a = 0; a < multigraphicTable.getRowCount(); a++) {
                    StringBuilder z = new StringBuilder("");
                    for (int b = 1; b < multigraphicTable.getColumnCount(); b++) {
                        String q = multigraphicTable.getValueAt(a, b) + " ";
                        z.append(q);
                    }
                    phonemes.put(multigraphicTable.getValueAt(a, 0), z);
                }
                ws.put("Phonemes", phonemes);
            }
            //if (punctuationSet){
            HashMap punct = new HashMap();
            for (int p = 0; p < punctuationTable.getRowCount(); p++) {
                punct.put(punctuationTable.getValueAt(p, 0) + "",
                        punctuationTable.getValueAt(p, 1) + "");
                // }
                ws.put("Punctuation", punct);
            }
            return ws;
        }
    }

    public void setWritingSystem(java.util.HashMap ws) {
        writingSystem = ws;
    }

    public int getChosenOption() {
        return chosenOption;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                RwWritingSystemChooser dialog = new RwWritingSystemChooser(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Presets;
    private javax.swing.JScrollPane PresetsScroller;
    private javax.swing.JPanel abjadCard;
    private javax.swing.JLabel abjadCharLabel;
    private javax.swing.JPanel abjadCharLabelPanel;
    private javax.swing.JComboBox abjadConsonantGlyphNumberCombo;
    private javax.swing.JComboBox abjadConsonantUsedInCombo;
    private javax.swing.JLabel abjadConsonantUsedInLabel;
    private javax.swing.JLabel abjadCurrentVLabel;
    private javax.swing.JToggleButton abjadDirectionalityButton;
    private javax.swing.ButtonGroup abjadFontButtonGroup;
    private javax.swing.JTextField abjadFontField;
    private java.util.HashMap abjadGlyphUseMap;
    private javax.swing.JLabel abjadGlyphsPerConsonantLabel;
    private javax.swing.JSpinner abjadGlyphsPerConsonantSpinner;
    private javax.swing.JTextField abjadJumpToField;
    private javax.swing.JLabel abjadJumpToLabel;
    private javax.swing.JButton abjadLeftColumnButton;
    private javax.swing.JButton abjadRightColumnButton;
    private javax.swing.JScrollPane abjadScroller;
    private javax.swing.JTable abjadTable;
    private javax.swing.JCheckBox abjadVowelCarrierCheck;
    private javax.swing.JButton abugidaAutoFillButton;
    private javax.swing.JRadioButton abugidaBorrowedFontRadio;
    private javax.swing.JPanel abugidaCard;
    private javax.swing.JLabel abugidaCharLabel;
    private javax.swing.JButton abugidaChooseFontButton;
    private javax.swing.JLabel abugidaCurrentVLabel;
    private javax.swing.JRadioButton abugidaCustomFontRadio;
    private javax.swing.ButtonGroup abugidaDiacriticsButtonGroup;
    private javax.swing.JToggleButton abugidaDirectionalityButton;
    private javax.swing.JButton abugidaDownCharButton;
    private javax.swing.ButtonGroup abugidaFontButtonGroup;
    private javax.swing.JTextField abugidaFontField;
    private javax.swing.JLabel abugidaFontLabel;
    private javax.swing.JTextField abugidaJumpToField;
    private javax.swing.JLabel abugidaJumpToLabel;
    private javax.swing.JScrollPane abugidaScroller;
    private javax.swing.JTable abugidaTable;
    private javax.swing.JButton abugidaUpCharButton;
    private javax.swing.JLabel alphaCurrentVLabel;
    private java.util.HashMap alphaGlyphUseMap;
    private javax.swing.JLabel alphaGlyphsPerLetterLabel;
    private javax.swing.JSpinner alphaGlyphsPerLetterSpinner;
    private javax.swing.JTextField alphaJumpToField;
    private javax.swing.JLabel alphaJumpToLabel;
    private javax.swing.JButton alphaLeftColumnButton;
    private javax.swing.JComboBox alphaLetterGlyphNumberCombo;
    private javax.swing.JComboBox alphaLetterUsedInCombo;
    private javax.swing.JLabel alphaLetterUsedInLabel;
    private javax.swing.JButton alphaRightColumnButton;
    private javax.swing.JScrollPane alphaScroller;
    private javax.swing.JTable alphaTable;
    private javax.swing.ButtonGroup alphabetButtonGroup;
    private javax.swing.JPanel alphabetCard;
    private javax.swing.JButton autoFillSyllabaryButton;
    private javax.swing.JButton autofillAbjadButton;
    private javax.swing.JButton autofillAlphabetButton;
    private javax.swing.JRadioButton borrowedAbjadFontRadio;
    private javax.swing.JRadioButton borrowedCharsRadio;
    private javax.swing.JButton browse4WsButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JComboBox capitalizationCombo;
    private javax.swing.JLabel capitalizationLabel;
    private javax.swing.JLabel characterLabel;
    private javax.swing.JPanel characterLabelPanel;
    private javax.swing.JButton chooseAbjadFontButton;
    private javax.swing.JButton chooseAlphabetFontButton;
    private javax.swing.JComboBox consonantNumCombo;
    private javax.swing.JLabel consonantNumLabel;
    private javax.swing.JLabel currentHexValueLabel;
    private javax.swing.JRadioButton customAbjadFontRadio;
    private javax.swing.JRadioButton customFontRadio;
    private javax.swing.ButtonGroup diacriticsButtonGroup;
    private javax.swing.JToggleButton directionalityButton;
    private javax.swing.JButton downAbjadCharButton;
    private javax.swing.JButton downButton;
    private javax.swing.JButton downCharAbjadButton;
    private javax.swing.JButton downCharAbugidaButton;
    private javax.swing.JButton downCharButton;
    private javax.swing.JButton downPageAbjadButton;
    private javax.swing.JButton downPageAbugidaButton;
    private javax.swing.JButton downPageButton;
    private javax.swing.JTextField fontField;
    private javax.swing.JLabel fontLabel;
    private javax.swing.JLabel graphemesPerGlyphLabel;
    private javax.swing.JSpinner graphemesPerGlyphSpinner;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField jumpToField;
    private javax.swing.JLabel jumpToLabel;
    private javax.swing.JButton loadWsButton;
    private javax.swing.ButtonGroup mainButtonGroup;
    private javax.swing.JButton multiAutoFillButton;
    private javax.swing.JRadioButton multiBorrowedRadio;
    private javax.swing.JLabel multiCharacterLabel;
    private javax.swing.JButton multiChooseCustomFontButton;
    private javax.swing.JTextField multiCustomFontField;
    private javax.swing.JRadioButton multiCustomRadio;
    private javax.swing.JButton multiDownButton;
    private javax.swing.JButton multiDownButton1;
    private javax.swing.JButton multiDownCharButton;
    private javax.swing.JButton multiDownPageButton;
    private javax.swing.JTextField multiJumpToField;
    private javax.swing.JLabel multiJumpToLabel;
    private javax.swing.JButton multiLeftGlyphButton;
    private javax.swing.JButton multiRightGlyphButton;
    private javax.swing.JButton multiSetGlyphButton;
    private javax.swing.JButton multiUpCharButton;
    private javax.swing.JButton multiUpGlyphButton;
    private javax.swing.JButton multiUpPageButton;
    private javax.swing.JCheckBox multiUseEndersCheckbox;
    private javax.swing.JPanel multigraphicCard;
    private javax.swing.ButtonGroup multigraphicFontGroup;
    private javax.swing.JLabel multigraphicFontLabel;
    private javax.swing.JScrollPane multigraphicScroller;
    private javax.swing.JTable multigraphicTable;
    private javax.swing.JCheckBox noVowelCheckbox;
    private javax.swing.JButton okButton;
    private java.util.Vector presets;
    private javax.swing.JTable presetsTable;
    private javax.swing.JButton punctAutoFillButton;
    private javax.swing.JPanel punctCharPanel;
    private javax.swing.JLabel punctCharacterLabel;
    private javax.swing.JLabel punctCurrentHexValueLabel;
    private javax.swing.JButton punctDownCharButton;
    private javax.swing.JTextField punctJumpToField;
    private javax.swing.JLabel punctJumpToLabel;
    private javax.swing.JButton punctSetGlyphButton;
    private javax.swing.JPanel punctSpacerPanel;
    private javax.swing.JPanel punctSpacerPanel1;
    private javax.swing.JButton punctUpCharButton;
    private javax.swing.JButton punctUpGlyphButton;
    private javax.swing.JButton punctUpPageButton;
    private javax.swing.JButton punctiDownPageButton;
    private javax.swing.JPanel punctuationCard;
    private javax.swing.JScrollPane punctuationScroller;
    private javax.swing.JTable punctuationTable;
    private javax.swing.JButton saveWsButton;
    private javax.swing.JButton setAbjadCharButton;
    private javax.swing.JButton setAbugidaCharButton;
    private javax.swing.JButton setAlphaCharButton;
    private javax.swing.JButton setSyllableButton;
    private javax.swing.JPanel syllabaryCard;
    private javax.swing.JLabel syllabaryCharValueLabel;
    private javax.swing.JButton syllabaryChooseCustomFontButton;
    private javax.swing.JCheckBox syllabaryCustomFontCheck;
    private javax.swing.JTextField syllabaryCustomFontField;
    private javax.swing.JToggleButton syllabaryDirectionalityButton;
    private javax.swing.JButton syllabaryDownCharButton;
    private javax.swing.JButton syllabaryDownPageButton;
    private javax.swing.JScrollPane syllabaryScroller;
    private javax.swing.JTable syllabaryTable;
    private javax.swing.JButton syllabaryUpCharButton;
    private javax.swing.JButton syllabaryUpPageButton;
    private rw.RwCellRenderer2 tcr2;
    private javax.swing.JButton upAbjadCharButton;
    private javax.swing.JButton upAbjadPageButton;
    private javax.swing.JButton upButton;
    private javax.swing.JButton upCharAbjadButton;
    private javax.swing.JButton upCharAbugidaButton;
    private javax.swing.JButton upCharButton;
    private javax.swing.JButton upPageAbugidaButton;
    private javax.swing.JButton upPageButton;
    private javax.swing.JButton useSystemButton;
    private javax.swing.JPanel userDefdCard;
    private javax.swing.JScrollPane userDefdScroller;
    private javax.swing.JTable userDefdTable;
    private javax.swing.JLabel visibleCharLabel;
    private javax.swing.JCheckBox vowelCarrierCheck;
    private javax.swing.JComboBox vowelNumCombo;
    private javax.swing.JLabel vowelNumLabel;
    private java.util.HashMap writingSystem;
    private java.awt.CardLayout writingSystemLayout;
    private javax.swing.JTabbedPane writingSystemPane;
    // End of variables declaration//GEN-END:variables
    private int chosenOption;
    public int OK_OPTION = 1;
    public int CANCEL_OPTION = 0;
    public boolean punctuationSet = false;
    public boolean wsLoaded = false;
    private Vector vows;
    private Vector cons;
    private File wsFile;
}
