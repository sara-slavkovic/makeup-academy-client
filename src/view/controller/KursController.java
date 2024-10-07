/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.controller;

import communication.Communication;
import domain.Kurs;
import domain.Predavac;
import domain.TipSminkanja;
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
import view.form.FrmKurs;
import view.form.util.FormMode;

/**
 *
 * @author Korisnik
 */
public class KursController {

    private final FrmKurs frmKurs;

    public KursController(FrmKurs frmKurs) {
        this.frmKurs = frmKurs;
        addActionListener();
    }

    private void addActionListener() {
        frmKurs.addSacuvajBtnAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }

            private void save() {
                try {

                    if (!validateForm()) {
                        return;
                    }

                    Kurs kurs = new Kurs();
                    kurs.setNazivKursa(frmKurs.getTxtNaziv().getText().trim());
                    kurs.setTrajanjeUNedeljama(Integer.parseInt(frmKurs.getTxtTrajanjeUNedeljama().getText().trim()));
                    kurs.setTipSminkanja((TipSminkanja) frmKurs.getCmbTipSminkanja().getSelectedItem());
                    kurs.setPredavac((Predavac) frmKurs.getCmbPredavac().getSelectedItem());

//                    //provera da li su polja prazna
//                    if (kurs.getNazivKursa().isEmpty() || kurs.getTrajanjeUNedeljama() == 0 || kurs.getTipSminkanja() == null
//                            || kurs.getPredavac() == null) {
//                        JOptionPane.showMessageDialog(frmKurs, "Kurs nije kreiran", "Kurs greška", JOptionPane.ERROR_MESSAGE);
//                        JOptionPane.showMessageDialog(frmKurs, "Nisu uneseni svi podaci", "Kurs greška", JOptionPane.ERROR_MESSAGE);
//                        return;
//                    }
                    //provera da li kurs postoji u bazi,pozivamo komunikaciju da salje zahtev serveru
                    List<Kurs> kurseviIzBaze = Communication.getInstance().vratiKurseveNaziv(kurs.getNazivKursa());
                    //provera da li se kurs koji korisnik zeli da kreira vec nalazi u bazi
                    for (Kurs k : kurseviIzBaze) {
                        if (k.getNazivKursa().equals(kurs.getNazivKursa())) {
                            JOptionPane.showMessageDialog(frmKurs, "Kurs nije kreiran", "Kurs greška", JOptionPane.ERROR_MESSAGE);
                            JOptionPane.showMessageDialog(frmKurs, "Kurs sa ovim imenom već postoji u bazi", "Kurs greška", JOptionPane.ERROR_MESSAGE);
                            frmKurs.getTxtNaziv().setText("");//da korisniku obrise ono sto je uneo za naziv
                            return;
                        }
                    }

                    //ako je sve ovo dobro proslo, da se sacuva kurs
                    Communication.getInstance().sacuvajKurs(kurs);

                    JOptionPane.showMessageDialog(frmKurs, "Uspešno kreiran kurs", "Čuvanje kursa", JOptionPane.INFORMATION_MESSAGE);
                    frmKurs.dispose();

                } catch (Exception e) {
                    Logger.getLogger(KursController.class.getName()).log(Level.SEVERE, null, e);
                    JOptionPane.showMessageDialog(frmKurs, "Kurs nije kreiran", "Kurs greška", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frmKurs.addOmoguciIzmeneBtnAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //da podesi polja za editovanje
                setupComponents(FormMode.FORM_EDIT);
            }
        });

        frmKurs.addOtkaziBtnAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frmKurs.dispose();
            }
        });

        frmKurs.addObrisiBtnAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                delete();
            }

            private void delete() {
                try {
                    Kurs kurs = makeKursFromForm();
                    if (kurs == null) {
                        JOptionPane.showMessageDialog(frmKurs, "Sistem ne može da obriše kurs.\nFale podaci sa forme", "Kurs greška", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    Communication.getInstance().obrisiKurs(kurs);
                    JOptionPane.showMessageDialog(frmKurs, "Sistem je obrisao kurs", "Brisanje kursa", JOptionPane.INFORMATION_MESSAGE);
                    frmKurs.dispose();
                } catch (Exception ex) {
                    Logger.getLogger(KursController.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(frmKurs, "Sistem ne može da obriše kurs", "Kurs greška", JOptionPane.ERROR_MESSAGE);
                    JOptionPane.showMessageDialog(frmKurs, "Proveriti da li postoji grupa koja pohađa ovaj kurs i/ili se već neko prijavio na kurs", "Kurs greška", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frmKurs.addIzmeniBtnAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                edit();
            }

            private void edit() {
                try {
                    if(!validateForm()){
                        return;
                    }
                    Kurs kurs = makeKursFromForm();//da pokupi podatke iz forme koji su uneti za izmenu

//                    if (kurs == null) {
//                        JOptionPane.showMessageDialog(frmKurs, "Sistem ne može da promeni kurs.", "Kurs greška", JOptionPane.ERROR_MESSAGE);
//                        JOptionPane.showMessageDialog(frmKurs, "Proveriti da li su svi podaci lepo uneti", "Kurs greška", JOptionPane.ERROR_MESSAGE);
//                        return;
//                    }

                    //provera da li je nesto izmenjeno zapravo, da ne cuva isti objekat bez izmene
                    List<Kurs> kurseviIzBaze = Communication.getInstance().vratiSveKurseve();
                    for (Kurs k : kurseviIzBaze) {
                        if (k.equals(kurs)) {
                            JOptionPane.showMessageDialog(frmKurs, "Sistem ne može da promeni kurs.", "Kurs greška", JOptionPane.ERROR_MESSAGE);
                            JOptionPane.showMessageDialog(frmKurs, "Niste izmenili nijedan podatak kod odabranog kursa", "Kurs greška", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }

                    //ako je sve uspesno
                    Communication.getInstance().izmeniKurs(kurs);
                    JOptionPane.showMessageDialog(frmKurs, "Sistem je promenio kurs", "Izmena kursa", JOptionPane.INFORMATION_MESSAGE);
                    frmKurs.dispose();
                } 
//                catch (NumberFormatException e) {
//                    JOptionPane.showMessageDialog(frmKurs, "Sistem ne može da promeni kurs.", "Kurs greška", JOptionPane.ERROR_MESSAGE);
//                    JOptionPane.showMessageDialog(frmKurs, "Trajanje u nedeljama mora da bude uneto u vidu cifre.", "Kurs greška", JOptionPane.ERROR_MESSAGE);
//                } 
                catch (Exception ex) {
                    Logger.getLogger(KursController.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(frmKurs, "Sistem ne može da promeni kurs", "Kurs greška", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public void openForm(FormMode formMode) {
        if (formMode.equals(FormMode.FORM_VIEW)) {
            frmKurs.addWindowListener(new WindowAdapter() {
                @Override
                public void windowOpened(WindowEvent e) {
                    JOptionPane.showMessageDialog(frmKurs, "Sistem je učitao kurs");
                }
            });
        }
        frmKurs.setLocationRelativeTo(MainCoordinator.getInstance().getMainAdminController().getFrmMainAdmin());
        prepareView(formMode);
        frmKurs.setVisible(true);
    }

    private void prepareView(FormMode formMode) {
        try {
            fillCbTipSminkanja();
            fillCbPRedavac();
            frmKurs.setTitle("Kurs");
        } catch (Exception ex) {
            Logger.getLogger(KursController.class.getName()).log(Level.SEVERE, null, ex);
        }
        setupComponents(formMode);
    }

    private void fillCbTipSminkanja() throws Exception {
        frmKurs.getCmbTipSminkanja().removeAllItems();
        List<TipSminkanja> tipoviSminkanja = Communication.getInstance().vratiSveTipoveSminkanja();
        for (TipSminkanja tipSminkanja : tipoviSminkanja) {
            //frmKurs.getCmbTipSminkanja().addItem(tipSminkanja.toString());// tostring vraca nazivtipasminkanja
            frmKurs.getCmbTipSminkanja().addItem(tipSminkanja);
        }
    }

    private void fillCbPRedavac() throws Exception {
        frmKurs.getCmbPredavac().removeAllItems();
        List<Predavac> predavaci = Communication.getInstance().vratiSvePredavace();
        for (Predavac predavac : predavaci) {
            //frmKurs.getCmbPredavac().addItem(predavac.toString());//tostring vraca ime " " prezime
            frmKurs.getCmbPredavac().addItem(predavac);
        }
    }

    private boolean validateForm() {
        if (frmKurs.getTxtNaziv().getText().isEmpty()) {
            JOptionPane.showMessageDialog(frmKurs, "Kurs nije kreiran", "Kurs greška", JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(frmKurs, "Niste uneli naziv kursa", "Kurs greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (frmKurs.getTxtTrajanjeUNedeljama().getText().isEmpty()) {
            JOptionPane.showMessageDialog(frmKurs, "Kurs nije kreiran", "Kurs greška", JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(frmKurs, "Niste uneli trajanje kursa", "Kurs greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            int trajanje = Integer.parseInt(frmKurs.getTxtTrajanjeUNedeljama().getText().trim());
        } catch (NumberFormatException ex) {
            Logger.getLogger(KursController.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(frmKurs, "Kurs nije kreiran", "Kurs greška", JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(frmKurs, "Trajanje u nedeljama treba da se unese u vidu cifre", "Kurs greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (frmKurs.getCmbTipSminkanja().getSelectedItem() == null || frmKurs.getCmbPredavac().getSelectedItem() == null) {
            JOptionPane.showMessageDialog(frmKurs, "Kurs nije kreiran", "Kurs greška", JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(frmKurs, "Nisu odabrani tip šminkanja i/ili predavač", "Kurs greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void setupComponents(FormMode formMode) {
        switch (formMode) {
            case FORM_ADD:
                frmKurs.getBtnIzmeni().setEnabled(false);
                frmKurs.getBtnObrisi().setEnabled(false);
                frmKurs.getBtnOmoguciIzmene().setEnabled(false);
                frmKurs.getBtnOtkazi().setEnabled(true);
                frmKurs.getBtnSacuvaj().setEnabled(true);

                frmKurs.getTxtID().setEnabled(false);
                frmKurs.getTxtNaziv().setEnabled(true);
                frmKurs.getTxtTrajanjeUNedeljama().setEnabled(true);
                frmKurs.getCmbPredavac().setEnabled(true);
                frmKurs.getCmbTipSminkanja().setEnabled(true);
                break;
            case FORM_VIEW:
                frmKurs.getBtnIzmeni().setEnabled(false);
                frmKurs.getBtnObrisi().setEnabled(true);
                frmKurs.getBtnOmoguciIzmene().setEnabled(true);
                frmKurs.getBtnOtkazi().setEnabled(true);
                frmKurs.getBtnSacuvaj().setEnabled(false);

                frmKurs.getTxtID().setEnabled(false);
                frmKurs.getTxtNaziv().setEnabled(false);
                frmKurs.getTxtTrajanjeUNedeljama().setEnabled(false);
                frmKurs.getCmbPredavac().setEnabled(false);
                frmKurs.getCmbTipSminkanja().setEnabled(false);

                //izvlacenje podataka o kursu
                Kurs kurs = (Kurs) MainCoordinator.getInstance().getParam(Constants.PARAM_KURS);//poslali smo ovaj param
                //iz pretragekursevacontrollera, to je selektovani kurs

                //postavljanje podataka o kursu na formi, jer ce se otvoriti forma sa prikazom detalja o kursu
                frmKurs.getTxtID().setText(kurs.getID(kurs));
                frmKurs.getTxtNaziv().setText(kurs.getNazivKursa());
                frmKurs.getTxtTrajanjeUNedeljama().setText(String.valueOf(kurs.getTrajanjeUNedeljama()));
                frmKurs.getCmbTipSminkanja().setSelectedItem(kurs.getTipSminkanja());
                frmKurs.getCmbPredavac().setSelectedItem(kurs.getPredavac());

                break;
            case FORM_EDIT:
                frmKurs.getBtnIzmeni().setEnabled(true);
                frmKurs.getBtnObrisi().setEnabled(false);
                frmKurs.getBtnOmoguciIzmene().setEnabled(false);
                frmKurs.getBtnOtkazi().setEnabled(true);
                frmKurs.getBtnSacuvaj().setEnabled(false);

                frmKurs.getTxtID().setEnabled(false);
                frmKurs.getTxtNaziv().setEnabled(true);
                frmKurs.getTxtTrajanjeUNedeljama().setEnabled(true);
                frmKurs.getCmbPredavac().setEnabled(true);
                frmKurs.getCmbTipSminkanja().setEnabled(true);
                break;
        }
    }

    private Kurs makeKursFromForm() {
        //da pokupi podatke iz forme
        Kurs kurs = new Kurs();
        kurs.setIdKursa(Integer.parseInt(frmKurs.getTxtID().getText().trim()));
        kurs.setNazivKursa(frmKurs.getTxtNaziv().getText());
        kurs.setTrajanjeUNedeljama(Integer.parseInt(frmKurs.getTxtTrajanjeUNedeljama().getText().trim()));
        kurs.setTipSminkanja((TipSminkanja) frmKurs.getCmbTipSminkanja().getSelectedItem());
        kurs.setPredavac((Predavac) frmKurs.getCmbPredavac().getSelectedItem());

        if (kurs.getNazivKursa().isEmpty() || kurs.getTrajanjeUNedeljama() == 0 || kurs.getTipSminkanja() == null
                || kurs.getPredavac() == null) {
            return null;
        }
        return kurs;
    }

}
