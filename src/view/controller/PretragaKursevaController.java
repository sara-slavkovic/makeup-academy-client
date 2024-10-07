/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.controller;

import communication.Communication;
import domain.Kurs;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import view.constans.Constants;
import view.coordinator.MainCoordinator;
import view.form.FrmPretragaKurseva;
import view.form.components.tableModel.KursTableModel;

/**
 *
 * @author Korisnik
 */
public class PretragaKursevaController {
    
    private FrmPretragaKurseva frmPretragaKurseva;
    private KursTableModel ktm;

    public PretragaKursevaController(FrmPretragaKurseva frmPretragaKurseva) {
        this.frmPretragaKurseva = frmPretragaKurseva;
        addActionListener();
    }
    
    public void openForm() {
        frmPretragaKurseva.setLocationRelativeTo(MainCoordinator.getInstance().getMainAdminController().getFrmMainAdmin());
        prepareView();
        frmPretragaKurseva.setVisible(true);
    }

    private void prepareView() {
        frmPretragaKurseva.setTitle("Pretraga kurseva");
    }
    
    private void addActionListener() {
        frmPretragaKurseva.getBtnDetaljiAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = frmPretragaKurseva.getTabelaKurseva().getSelectedRow();
                if(selectedRow >= 0){
                    //proba
//                    KursTableModel ktm = (KursTableModel) frmPretragaKurseva.getTabelaKurseva().getModel();
//                    Kurs k = ktm.getKursevi().get(selectedRow);
                    Kurs kurs = ((KursTableModel) frmPretragaKurseva.getTabelaKurseva().getModel()).getKursAt(selectedRow);
                    MainCoordinator.getInstance().addParam(Constants.PARAM_KURS, kurs);
                    MainCoordinator.getInstance().openKursDetailsForm();
                }else{
                    JOptionPane.showMessageDialog(frmPretragaKurseva, "Sistem ne može da učita kurs.", "Kurs greška", JOptionPane.ERROR_MESSAGE);
                    JOptionPane.showMessageDialog(frmPretragaKurseva, "Nije selektovan kurs za prikaz detalja", "Kurs greška", JOptionPane.ERROR_MESSAGE);
                }
                fillTableKursevi();
            }
        });
        
        frmPretragaKurseva.getBtnFiltrirajAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {                
                    String nazivIzPretrage = frmPretragaKurseva.getTxtNazivKursa().getText().trim();
                    if(nazivIzPretrage.isEmpty()){
                        JOptionPane.showMessageDialog(frmPretragaKurseva, "Morate uneti naziv kursa", "Kurs greška", JOptionPane.ERROR_MESSAGE);
                        return;                    
                    }
                    List<Kurs> kurseviIzBazePoNazivu = Communication.getInstance().vratiKurseveNaziv(nazivIzPretrage);
                    if(kurseviIzBazePoNazivu.isEmpty()){//u bazi se ne poklapa nijedan naziv sa nazivom iz pretrage
                        fillTableKursevi();//ova metoda prikazuje sve kurseve iz baze
                        JOptionPane.showMessageDialog(frmPretragaKurseva, "Sistem ne može da nađe kurseve po zadatoj vrednosti", "Kurs greška", JOptionPane.ERROR_MESSAGE);
                        return;                        
                    }
                    //ako sve uspe, sistem nadje kurs iz baze
                    JOptionPane.showMessageDialog(frmPretragaKurseva, "Sistem je našao kurseve po zadatoj vrednosti");
//                    KursTableModel ktm = new KursTableModel(kurseviIzBazePoNazivu);
//                    frmPretragaKurseva.getTabelaKurseva().setModel(ktm);
                    ktm = (KursTableModel) frmPretragaKurseva.getTabelaKurseva().getModel();
                    ktm.setKursevi(kurseviIzBazePoNazivu);
                    frmPretragaKurseva.getTxtNazivKursa().setText("");//da obrise ono sto je uneto malopre za pretragu jer je pretraga gotova
                } catch (Exception ex) {
                    Logger.getLogger(PretragaKursevaController.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(frmPretragaKurseva, "Sistem ne može da nađe kurseve po zadatoj vrednosti", "Kurs greška", JOptionPane.ERROR_MESSAGE);

                }                    
            }
        });
        
        frmPretragaKurseva.getBtnPrikaziSveAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //popunjavanje tabele sa kursevima iz baze
                fillTableKursevi();
            }
        });
        
        frmPretragaKurseva.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                fillTableKursevi();
            }
        });
    }


    private void fillTableKursevi() {
        try {
            List<Kurs> kurseviIzBaze = Communication.getInstance().vratiSveKurseve();
            ktm = new KursTableModel(kurseviIzBaze);
            frmPretragaKurseva.getTabelaKurseva().setModel(ktm);
            
//            for (Kurs kurs : kurseviIzBaze) {
//                System.out.println(kurs.getTipSminkanja());
//            }
        } catch (Exception ex) {
            Logger.getLogger(PretragaKursevaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    
}
