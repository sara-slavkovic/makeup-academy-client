/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.controller;

import communication.Communication;
import domain.Korisnik;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import view.constans.Constants;
import view.coordinator.MainCoordinator;
import view.form.FrmMainKorisnik;

/**
 *
 * @author Korisnik
 */
public class MainKorisnikController {

    private final FrmMainKorisnik frmMainKorisnik;

    public MainKorisnikController(FrmMainKorisnik frmMainKorisnik) {
        this.frmMainKorisnik = frmMainKorisnik;
        addActionListener();
    }

    public void openForm() {
        Korisnik korisnik = (Korisnik) MainCoordinator.getInstance().getParam(Constants.CURRENT_USER);
        //labela da znam o kom korisniku je rec
        frmMainKorisnik.getLblCurrentUser().setText(korisnik.getIme() + " " + korisnik.getPrezime());
        frmMainKorisnik.setVisible(true);
    }

    private void addActionListener() {
        frmMainKorisnik.addKrajBtnActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Communication.getInstance().krajRada();
                    Korisnik k = (Korisnik) MainCoordinator.getInstance().getParam(Constants.CURRENT_USER);
                    JOptionPane.showMessageDialog(frmMainKorisnik, "Doviđenja, " + k.getIme() + " " + k.getPrezime() + ". Prijatan dan!");
                    System.exit(0);
                } catch (Exception ex) {
                    Logger.getLogger(MainKorisnikController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        frmMainKorisnik.addPrijavaActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainCoordinator.getInstance().openAddPrijavaForm();
            }
        });
        
        frmMainKorisnik.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    Communication.getInstance().krajRada();
                    Korisnik k = (Korisnik) MainCoordinator.getInstance().getParam(Constants.CURRENT_USER);
                    JOptionPane.showMessageDialog(frmMainKorisnik, "Doviđenja, " + k.getIme() + " " + k.getPrezime());
                    System.exit(0);
                } catch (Exception ex) {
                    Logger.getLogger(MainKorisnikController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });
    }

    public FrmMainKorisnik getFrmMainKorisnik() {
        return frmMainKorisnik;
    }

    
}
