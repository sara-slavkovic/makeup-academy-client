/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.form.components.tableModel;

import domain.RasporedKursa;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Korisnik
 */
public class RasporedKursaTableModel extends AbstractTableModel {

    private final String[] columnNames = {"Dan u nedelji", "Broj časova", "Sala", "Vreme", "Opis"};
    private List<RasporedKursa> rasporedi;

    public RasporedKursaTableModel(List<RasporedKursa> rasporedi) {
        this.rasporedi = rasporedi;
    }

    @Override
    public int getRowCount() {
        if (rasporedi == null) {
            return 0;
        }
        return rasporedi.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        RasporedKursa rk = rasporedi.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return rk.getDanUNedelji();
            case 1:
                return rk.getBrojCasova();
            case 2:
                return rk.getSala();
            case 3:
                return rk.getVreme();
            case 4:
                return rk.getOpisKursa();
            default:
                return "n/a";
        }
    }

    @Override
    public String getColumnName(int column) {
        if (column > columnNames.length) {
            return "n/a";
        }
        return columnNames[column];
    }
    
    public void dodajRaspored(RasporedKursa raspored){
        rasporedi.add(raspored);
        fireTableDataChanged();
    }
    
    public List<RasporedKursa> getRasporedi() {
        return rasporedi;
    }

    public void obrisiRaspored(int selectedRow) {
        rasporedi.remove(selectedRow);
        fireTableDataChanged();
    }

    public void refresh() {
        fireTableDataChanged();
    }

}
