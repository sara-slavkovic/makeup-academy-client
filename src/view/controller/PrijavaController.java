/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.controller;

import communication.Communication;
import domain.Grupa;
import domain.Korisnik;
import domain.Kurs;
import domain.Prijava;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import view.constans.Constants;
import view.coordinator.MainCoordinator;
import view.form.FrmPrijava;
import view.form.components.tableModel.KursTableModel;

/**
 *
 * @author Korisnik
 */
public class PrijavaController {
    
    private final FrmPrijava frmPrijava;
    private KursTableModel ktm;

    public PrijavaController(FrmPrijava frmPrijava) {
        this.frmPrijava = frmPrijava;
        addActionListener();
    }

    public void openForm() {
        frmPrijava.setLocationRelativeTo(MainCoordinator.getInstance().getMainKorisnikController().getFrmMainKorisnik());
        prepareView();
        frmPrijava.setVisible(true);
    }

    private void prepareView() {
        frmPrijava.setTitle("Prijava na kurs");
    }

    private void addActionListener() {
        frmPrijava.getBtnPosaljiPrijavuAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {                
                    //prvo iscitavanje podataka
                    String napomena = frmPrijava.getTxtAreaNapomena().getText().trim();
                    int selectedRow = frmPrijava.getTabelaKurseva().getSelectedRow();
                    
                    if(selectedRow >= 0){
                        KursTableModel ktmm = (KursTableModel) frmPrijava.getTabelaKurseva().getModel();
                        Kurs kurs = ktmm.getKursAt(selectedRow);
                        MainCoordinator.getInstance().addParam(Constants.PARAM_KURS, kurs);
                        
                        //kreiranje prijave
                        Prijava prijava = new Prijava();
                        Korisnik korisnik = (Korisnik) MainCoordinator.getInstance().getParam(Constants.CURRENT_USER);
                        prijava.setKorisnik(korisnik);
                        prijava.setKurs(kurs);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String currentDate = sdf.format(new Date());
                        java.sql.Date datumPrijave = java.sql.Date.valueOf(currentDate);
                        prijava.setDatumPrijave(datumPrijave);
                        prijava.setNapomena(napomena);
                        
                        //provera da li ima grupa - ako nema ne moze da se formira prijava
                        List<Grupa> grupe = Communication.getInstance().vratiSveGrupe();
                        if(grupe.isEmpty()){
                            JOptionPane.showMessageDialog(frmPrijava, "Neuspešno prijavljivanje za kurs.", "Prijava greška", JOptionPane.ERROR_MESSAGE);                            
                            JOptionPane.showMessageDialog(frmPrijava, "Trenutno nema nijedne grupe koja pohađa kurs", "Prijava greška", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        
                        Grupa grupa = new Grupa();
                        int grupaId = Communication.getInstance().vratiMaksIdGrupa();
                        grupa = Communication.getInstance().vratiGrupu(grupaId);
                        prijava.setGrupa(grupa);
                        Communication.getInstance().sacuvajPrijavu(prijava);
                        
                        JOptionPane.showMessageDialog(frmPrijava, "Uspešno ste se prijavili za kurs", "Prijava", JOptionPane.INFORMATION_MESSAGE);
                        frmPrijava.dispose();                        
                    }else {
                        JOptionPane.showMessageDialog(frmPrijava, "Neuspešno prijavljivanje za kurs.", "Prijava greška", JOptionPane.ERROR_MESSAGE);
                        JOptionPane.showMessageDialog(frmPrijava, "Nije odabran kurs", "Prijava greška", JOptionPane.ERROR_MESSAGE);
                    }

                    
                } catch (java.sql.SQLIntegrityConstraintViolationException sqle) {
                    Logger.getLogger(PrijavaController.class.getName()).log(Level.SEVERE, null, sqle);
                    JOptionPane.showMessageDialog(frmPrijava, "Neuspešno prijavljivanje za kurs. \nNarušen je integritet za grupu.", "Prijava greška", JOptionPane.ERROR_MESSAGE);

                } catch (Exception ex) {
                    Logger.getLogger(PrijavaController.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(frmPrijava, "Neuspešno prijavljivanje za kurs.", "Prijava greška", JOptionPane.ERROR_MESSAGE);
                }            
            }
        });
        
        frmPrijava.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                fillTableKursevi();//popunjavanje tabele kursevima iz baze
            }
        });
    }
    
    private void fillTableKursevi() {
        try {
            List<Kurs> kursevi = Communication.getInstance().vratiSveKurseve();
            if(kursevi.isEmpty()){
                JOptionPane.showMessageDialog(frmPrijava, "Trenutno nema kurseva", "Prijava greška", JOptionPane.ERROR_MESSAGE);
                frmPrijava.dispose();                
            }
            ktm = new KursTableModel(kursevi);
            frmPrijava.getTabelaKurseva().setModel(ktm);
        } catch (Exception ex) {
            Logger.getLogger(PrijavaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
