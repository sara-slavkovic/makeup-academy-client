/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.form.components.tableModel;

import domain.Prijava;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Korisnik
 */
public class PrijavaTableModel extends AbstractTableModel {

    private List<Prijava> prijave;
    private final String[] columnNames = {"Ime i prezime", "Kurs", "Datum prijave", "Napomena", "Grupa"};
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public PrijavaTableModel(List<Prijava> prijave) {
        this.prijave = prijave;
    }
    
    @Override
    public int getRowCount() {
        if (prijave == null) {
            return 0;
        }
        return prijave.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Prijava p = prijave.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return p.getKorisnik().getIme() + " " + p.getKorisnik().getPrezime();
            case 1:
                return p.getKurs().getNazivKursa();
            case 2:
                return sdf.format(p.getDatumPrijave());
            case 3:
                return p.getNapomena();
            case 4:
                if (p.getGrupa() == null || p.getGrupa().getNazivGrupe() == null) {
                    return "/";
                }
                return p.getGrupa().getNazivGrupe();
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

    public Prijava getPrijavaAt(int selectedRow){
        return prijave.get(selectedRow);
    }
}
