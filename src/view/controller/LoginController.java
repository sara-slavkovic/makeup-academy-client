/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.controller;

import communication.Communication;
import domain.Korisnik;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import view.constans.Constants;
import view.coordinator.MainCoordinator;
import view.form.FrmLogin;

/**
 *
 * @author Korisnik
 */
public class LoginController {
    
    private final FrmLogin frmLogin;

    public LoginController(FrmLogin frmLogin) {
        this.frmLogin = frmLogin;
        addActionListener();
    }
    
    public void openForm() {
        frmLogin.setVisible(true);
        frmLogin.setTitle("Prijava");
    }
    
    private void addActionListener() {
        frmLogin.loginAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginUser();
            }

            private void loginUser() {
                resetForm();
                try {
                    String username = frmLogin.getTxtUsername().getText().trim();
                    String password = String.copyValueOf(frmLogin.getPswPassword().getPassword());
                    
                    validateForm(username, password);
                    
                    Korisnik korisnik = Communication.getInstance().login(username, password);
                    
                    JOptionPane.showMessageDialog(frmLogin, "Dobrodošli " + korisnik.getIme() + " " + korisnik.getPrezime(), "Prijava", JOptionPane.INFORMATION_MESSAGE);
                    JOptionPane.showMessageDialog(frmLogin, "Uspešno ste se prijavili", "Prijava", JOptionPane.INFORMATION_MESSAGE);

                    MainCoordinator.getInstance().addParam(Constants.CURRENT_USER, korisnik);
                    
                    //frmlogin.kor ima postavljene username i pass na admin admin
                    if(frmLogin.kor.getKorisnickoIme().equals(korisnik.getKorisnickoIme()) &&
                            frmLogin.kor.getLozinka().equals(korisnik.getLozinka())){
                        //ako je admin, otvori ovu formu
                        MainCoordinator.getInstance().openMainAdminForm();
                    }else{
                        //ako je obican korisnik, otvori ovu formu
                        MainCoordinator.getInstance().openMainKorisnikForm();
                    }
                    frmLogin.dispose();
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(frmLogin, "Neuspešno prijavljivanje", "Login greška", JOptionPane.ERROR_MESSAGE);
                }
            }

            private void resetForm() {
                frmLogin.getLblUsernameError().setText("");
                frmLogin.getLblPasswordError().setText("");
            }

            private void validateForm(String username, String password) throws Exception {
                String errorMessage = "";
                if(username.isEmpty()){
                    frmLogin.getLblUsernameError().setText("Polje korisničko ime ne sme biti prazno!");
                    errorMessage += "Polje korisničko ime ne sme biti prazno!\n";
                }
                if(password.isEmpty()){
                    frmLogin.getLblPasswordError().setText("Polje lozinka ne sme biti prazno!");
                    errorMessage += "Polje lozinka ne sme biti prazno!\n";
                }
                if(!errorMessage.isEmpty()){
                    throw new Exception(errorMessage);
                }
            }
        });
        
        frmLogin.registracijaAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registracija(e);
            }

            private void registracija(ActionEvent e) {
                MainCoordinator.getInstance().openRegistrationForm(frmLogin);
            }
        });
        

        
    }


    
    
}
