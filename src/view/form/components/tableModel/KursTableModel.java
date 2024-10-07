/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.form.components.tableModel;

import domain.Kurs;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Korisnik
 */
public class KursTableModel extends AbstractTableModel {
    
    private List<Kurs> kursevi;
    private final String[] columnNames = { "ID","Naziv kursa", "Trajanje u nedeljama", "Tip šminkanja", "Predavač"};

    public KursTableModel(List<Kurs> kursevi) {
        this.kursevi = kursevi;
    }
    
    @Override
    public int getRowCount() {
        if(kursevi == null)
            return 0;
        return kursevi.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Kurs k = kursevi.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return k.getIdKursa();
            case 1:
                return k.getNazivKursa();
            case 2:
                return k.getTrajanjeUNedeljama();
            case 3:
                return k.getTipSminkanja().getNazivTipaSminkanja();
            case 4:
                return k.getPredavac().getIme() + " " + k.getPredavac().getPrezime();
            default:
                return "n/a";
        }
    }

    @Override
    public String getColumnName(int column) {
        if(column > columnNames.length)
            return "n/a";
        return columnNames[column];
    }
    
    public Kurs getKursAt(int selectedRow){
        return kursevi.get(selectedRow);
    }

    public List<Kurs> getKursevi() {
        return kursevi;
    }

    public void setKursevi(List<Kurs> kursevi) {
        this.kursevi = kursevi;
        fireTableDataChanged();
    }

    
}
