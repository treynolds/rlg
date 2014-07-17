package rw;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author ted
 */
public class RwCellRenderer extends DefaultTableCellRenderer {
    public Component getTableCellRendererComponent
       (JTable table, Object value, boolean isSelected,
       boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent
           (table, value, isSelected, hasFocus, row, column);
        if(column > 0 && column < 3){
            cell.setFont(new Font("LCS-ConstructorII", 0, 16));
        } else if (column == 3){
            cell.setFont(new Font("LCS-ConstructorII", 0, 24));
        } else if (column == 4 ){
            cell.setFont(new Font(systemFont, 0, 24));
        }
        return cell;
    }
    public void setSystemFont(String font){
        systemFont = font;
    }
    private String systemFont = "LCS-ConstructorII";
}
