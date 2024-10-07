/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.form.components.tableModel;

import domain.Grupa;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Korisnik
 */
public class GrupaTableModel extends AbstractTableModel {

    private final String[] columnNames = {"Naziv grupe", "Datum početka kursa", "Naziv kursa"};
    private List<Grupa> grupe;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public GrupaTableModel(List<Grupa> grupe) {
        this.grupe = grupe;
    }

    @Override
    public int getRowCount() {
        if (grupe == null) {
            return 0;
        }
        return grupe.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Grupa g = grupe.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return g.getNazivGrupe();
            case 1:
                return sdf.format(g.getDatumPocetkaKursa());
            case 2:
                return g.getKurs().getNazivKursa();
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
    
    public Grupa getGrupaAt(int row){
        return grupe.get(row);
    }

    public List<Grupa> getGrupe() {
        return grupe;
    }

    public void setGrupe(List<Grupa> grupe) {
        this.grupe = grupe;
        fireTableDataChanged();
    }

    public void obrisiSve(){
        grupe.removeAll(grupe);
        fireTableDataChanged();
    }
}
