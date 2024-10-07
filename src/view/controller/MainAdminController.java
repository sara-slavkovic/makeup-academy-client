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
import view.form.FrmMainAdmin;

/**
 *
 * @author Korisnik
 */
public class MainAdminController {
    
    private final FrmMainAdmin frmMainAdmin;

    public MainAdminController(FrmMainAdmin frmMainAdmin) {
        this.frmMainAdmin = frmMainAdmin;
        addActionListener();
    }

    public void openForm() {
        Korisnik korisnik = (Korisnik) MainCoordinator.getInstance().getParam(Constants.CURRENT_USER);
        //dodajemo labelu za trenutnog usera kako bismo znali o kom useru je rec
        frmMainAdmin.getLblCurrentUser().setText(korisnik.getIme()+" "+korisnik.getPrezime());
        frmMainAdmin.setVisible(true);
    }

    private void addActionListener() {
        frmMainAdmin.addKrajBtnActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Communication.getInstance().krajRada();
                    Korisnik k = (Korisnik) MainCoordinator.getInstance().getParam(Constants.CURRENT_USER);
                    JOptionPane.showMessageDialog(frmMainAdmin, "Doviđenja, " + k.getIme() + " " + k.getPrezime() + ". Prijatan dan!");
                    System.exit(0);
                } catch (Exception ex) {
                    Logger.getLogger(MainAdminController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        frmMainAdmin.jmiSoftverAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frmMainAdmin, "Softver je napravila studentkinja Sara Slavkovic.");
            }
        });
        
        frmMainAdmin.jmiKreirajKursAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainCoordinator.getInstance().openAddKursForm();
            }
        });
        
        frmMainAdmin.jmiPretraziKursAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainCoordinator.getInstance().openSearchKursForm();
            }
        });
        
        frmMainAdmin.jmiKreirajGrupuAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainCoordinator.getInstance().openAddGrupaForm();
            }
        });
        
        frmMainAdmin.jmiPretraziGrupuAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainCoordinator.getInstance().openSearchGrupaForm();
            }
        });
        
        frmMainAdmin.jmiRasporediAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainCoordinator.getInstance().openRasporediUGrupuForm();
            }
        });
        
        frmMainAdmin.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    Communication.getInstance().krajRada();
                    Korisnik k = (Korisnik) MainCoordinator.getInstance().getParam(Constants.CURRENT_USER);
                    JOptionPane.showMessageDialog(frmMainAdmin, "Doviđenja, " + k.getIme() + " " + k.getPrezime() );
                    System.exit(0);                
                } catch (Exception ex) {
                    Logger.getLogger(MainAdminController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        });
    }

    public FrmMainAdmin getFrmMainAdmin() {
        return frmMainAdmin;
    }
    
    
    
}
