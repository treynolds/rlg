/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rw;

import java.awt.Component;
import java.awt.Font;
import java.util.Locale;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Ted
 */
public class RwCellRenderer2 extends DefaultTableCellRenderer {

    private String fontName="LCS-ConstructorII";

    public RwCellRenderer2(){
        super();
    }

    public RwCellRenderer2(Font f){
        super();
        fontName=f.getFontName();
    }

    public Component getTableCellRendererComponent
       (JTable table, Object value, boolean isSelected,
       boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent
           (table, value, isSelected, hasFocus, row, column);
        if(column > 0 ){
            cell.setFont(new Font(fontName, 0, 20));
            setHorizontalAlignment(JLabel.CENTER);
        } else {
            cell.setFont(new Font("LCS-ConstructorII", 0, 14));
            setHorizontalAlignment(JLabel.LEFT);
        }
        return cell;
    }

    public void setFontName(String fn){
        fontName=fn;
    }
}
