/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.controller;

import communication.Communication;
import domain.Korisnik;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;
import view.form.FrmRegistracija;

/**
 *
 * @author Korisnik
 */
public class RegistracijaController {
    
    private final FrmRegistracija frmRegistracija;

    public RegistracijaController(FrmRegistracija frmRegistracija) {
        this.frmRegistracija = frmRegistracija;
        addActionListener();
    }

    public void openForm() {
        //prvo da se postave default vrednosti
        fillDefaultValues();
        frmRegistracija.setTitle("Registracija");
        frmRegistracija.setVisible(true);
    }
    
    private void addActionListener() {
        frmRegistracija.registracijaAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registracijaKorisnika();
            }

            private void registracijaKorisnika() {
                resetForm();
                
                String ime = frmRegistracija.getTxtIme().getText().trim();
                String prezime = frmRegistracija.getTxtPrezime().getText().trim();
                String datumRodj = frmRegistracija.getTxtBirth().getText().trim();
                String kontaktTel = frmRegistracija.getTxtTelefon().getText().trim();
                String mejl = frmRegistracija.getTxtMejl().getText().trim();
                String username = frmRegistracija.getTxtUsername().getText().trim();
                String password = String.copyValueOf(frmRegistracija.getPswPassword().getPassword());
                
                if(validateForm(ime,prezime,datumRodj,kontaktTel,mejl,username,password)){
                    //ako je uspesno prosla validacija, poziva se komunikacija i metoda dodajkorisnika
                    try {
                        java.sql.Date datumSQL = java.sql.Date.valueOf(datumRodj);
                        Korisnik k = new Korisnik(0, ime, prezime, datumSQL, kontaktTel, mejl, 
                                username, password);
                        Communication.getInstance().dodajKorisnika(k);
                        JOptionPane.showMessageDialog(frmRegistracija, "Sistem je kreirao korisnički nalog");
                        frmRegistracija.dispose();                        
                    } catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(frmRegistracija, "Sistem ne može da kreira korisnički nalog", "", JOptionPane.ERROR_MESSAGE);
                        resetFields();
                    }

                }
            }

            private void resetForm() {//labele za poruku o gresci da se postave na ""
                frmRegistracija.getLblImeError().setText("");
                frmRegistracija.getLblPrezimeError().setText("");
                frmRegistracija.getLblBirthError().setText("");
                frmRegistracija.getLblTelefonError().setText("");
                frmRegistracija.getLblMejlError().setText("");
                frmRegistracija.getLblUsernameError().setText("");
                frmRegistracija.getLblPasswordError().setText("");
            }
            
            private boolean validateForm(String ime, String prezime, String datumRodj, String kontaktTel, String mejl, String username, String password) {
                boolean signal = true;
                
                //provera praznih polja
                if(ime.isEmpty()){
                    frmRegistracija.getLblImeError().setText("Niste uneli ime");
                    signal = false;
                }
                if(prezime.isEmpty()){
                    frmRegistracija.getLblPrezimeError().setText("Niste uneli prezime");
                    signal = false;
                }
                if(datumRodj.isEmpty()){
                    frmRegistracija.getLblBirthError().setText("Niste uneli datum rođenja");
                    signal = false;
                }
                if(kontaktTel.isEmpty()) {
                    frmRegistracija.getLblTelefonError().setText("Niste uneli broj telefona");
                    signal = false;
                }
                if(mejl.isEmpty() || !mejl.contains("@")) {
                    frmRegistracija.getLblMejlError().setText("Mejl mora da sadrži znak @");
                    signal = false;
                }
                if(username.isEmpty()) {
                    frmRegistracija.getLblUsernameError().setText("Niste uneli korisničko ime");
                    signal = false;
                }
                if(password.isEmpty()) {
                    frmRegistracija.getLblPasswordError().setText("Niste uneli lozinku");
                    signal = false;
                }
                
                //provera da li je lepo unet ostatak
                if(!password.isEmpty()){
                    if(password.length() < 8){
                        frmRegistracija.getLblPasswordError().setText("Lozinka mora da sadrži više od 7 karaktera");
                        signal = false;
                    }
                }
                if(!datumRodj.isEmpty()){
                    if(!proveriDatum(datumRodj)){
                        frmRegistracija.getLblBirthError().setText("Datum mora biti unet u formatu godina-mesec-dan (yyyy-MM-dd)");
                        signal = false;
                    }
                    String[] deloviDatuma = datumRodj.split("-");
                    if(deloviDatuma.length != 3 || !proveraDelovaDatuma(deloviDatuma)){
                        signal = false;//return false;
                    }
                }
                if(!kontaktTel.isEmpty()){
                    if(!proveriTelefon(kontaktTel)){
                        frmRegistracija.getLblTelefonError().setText("Niste lepo uneli telefon");
                        signal = false;
                    }
                }
                
                return signal;
            }

            private boolean proveriDatum(String datumRodj) {
                try {
                   DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                   Date date = df.parse(datumRodj);                   
                } catch (ParseException e) {
                    return false;
                }
                return true;
            }

            private boolean proveraDelovaDatuma(String[] deloviDatuma) {
                int godina = Integer.parseInt(deloviDatuma[0]);
                int mesec = Integer.parseInt(deloviDatuma[1]);
                int dan = Integer.parseInt(deloviDatuma[2]);
                
                int year = Calendar.getInstance().get(Calendar.YEAR);
                if(godina > year){
                    frmRegistracija.getLblBirthError().setText("Niste lepo uneli godinu");
                    return false;
                }
                if(mesec < 1 || mesec > 12){
                    frmRegistracija.getLblBirthError().setText("Niste lepo uneli mesec");
                    return false;
                }
                if(mesec == 2){
                    if(dan < 1 || dan > 28){
                        frmRegistracija.getLblBirthError().setText("Niste lepo uneli dan");
                        return false;
                    }
                }
                if (mesec == 1 || mesec == 3 || mesec == 5 || mesec == 7 || mesec == 8 || mesec == 10 || mesec == 12) {
                    if (dan < 1 || dan > 31) {
                        frmRegistracija.getLblBirthError().setText("Niste lepo uneli dan");
                        return false;
                    }
                }
                if (mesec == 4 || mesec == 6 || mesec == 9 || mesec == 11) {
                    if (dan < 1 || dan > 30) {
                        frmRegistracija.getLblBirthError().setText("Niste lepo uneli dan");
                        return false;
                    }
                }
                return true;//ako ne udje ni u jedan if uslov
            }

            private boolean proveriTelefon(String kontaktTel) {
                if(!kontaktTel.substring(0,2).contains("06")){
                    return false;
                }
                if(kontaktTel.length() != 9 && kontaktTel.length() != 10){
                    return false;
                }
                for (int i = 0; i < kontaktTel.length(); i++) {
                    if(!Character.isDigit(kontaktTel.charAt(i))){
                        return false;
                    }
                }
                return true;
            }

            private void resetFields() {//postavljanje polja na default vrednosti
                frmRegistracija.getTxtIme().setText("");
                frmRegistracija.getTxtPrezime().setText("");

                frmRegistracija.getTxtMejl().setText("");
                frmRegistracija.getPswPassword().setText("");

                frmRegistracija.getTxtUsername().setText("");
                fillDefaultValues();//ovde popunjava datum i telefon
            }

        });
    }

    private void fillDefaultValues() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = df.format(new Date());
        frmRegistracija.getTxtBirth().setText(currentDate);
        frmRegistracija.getTxtTelefon().setText("06XXXXXXXX");
    }

    
    
}
