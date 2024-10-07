/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.controller;

import communication.Communication;
import domain.Grupa;
import domain.Kurs;
import domain.RasporedKursa;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import view.constans.Constants;
import view.coordinator.MainCoordinator;
import view.form.FrmGrupa;
import view.form.components.tableModel.KursTableModel;
import view.form.components.tableModel.RasporedKursaTableModel;
import view.form.util.FormMode;

/**
 *
 * @author Korisnik
 */
public class GrupaController {

    private FrmGrupa frmGrupa;
    private KursTableModel ktm;
    private RasporedKursaTableModel rktm;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public GrupaController(FrmGrupa frmGrupa) {
        this.frmGrupa = frmGrupa;
        addActionListener();
    }

    public void openForm(FormMode formMode) {
        if (formMode.equals(formMode.FORM_VIEW)) {
            frmGrupa.addWindowListener(new WindowAdapter() {
                @Override
                public void windowOpened(WindowEvent e) {
                    JOptionPane.showMessageDialog(frmGrupa, "Sistem je učitao grupu polaznika kursa");
                }
            });
        }
        frmGrupa.setLocationRelativeTo(MainCoordinator.getInstance().getMainAdminController().getFrmMainAdmin());
        prepareView(formMode);
        frmGrupa.setVisible(true);
    }

    private void prepareView(FormMode formMode) {
        frmGrupa.setTitle("Grupa");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = df.format(new Date());
        frmGrupa.getTxtDatumPocetkaKursa().setText(currentDate);
        List<RasporedKursa> rasporedi = new ArrayList<>();
        rktm = new RasporedKursaTableModel(rasporedi);
        frmGrupa.getTabelaRasporeda().setModel(rktm);
        fillTableKursevi();

        try {
            setUpComponents(formMode);
        } catch (Exception ex) {
            Logger.getLogger(GrupaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void addActionListener() {
        frmGrupa.addDodajRasporedBtnAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addItem();
            }

            private void addItem() {
                try {
//                    if (frmGrupa.getTxtVreme().getText().isEmpty() || frmGrupa.getTxtBrojSale().getText().isEmpty()
//                            || frmGrupa.getTxtBrojCasova().getText().isEmpty()) {
//
//                        JOptionPane.showMessageDialog(frmGrupa, "Nisu uneti svi podaci o rasporedu kursa", "Grupa greška", JOptionPane.ERROR_MESSAGE);
//                        return;
//                    }

                    if (!validateRKForm()) {
                        return;
                    }

                    RasporedKursa rk = new RasporedKursa();
                    String danUNedelji = String.valueOf(frmGrupa.getCmbDanUNedelji().getSelectedItem());
                    String vreme = frmGrupa.getTxtVreme().getText().trim();
                    String opisKursa = frmGrupa.getTxtAreaOpisKursa().getText().trim();
                    if (opisKursa.isEmpty()) {
                        rk.setOpisKursa("/");//to je ona defaultna vrednost za opis kao /
                    }
//                    int sala = 0, brojCasova = 0;
//                    try {
//                        sala = Integer.parseInt(frmGrupa.getTxtBrojSale().getText().trim());
//                        brojCasova = Integer.parseInt(frmGrupa.getTxtBrojCasova().getText().trim());
//                    } catch (NumberFormatException e) {
//                        JOptionPane.showMessageDialog(frmGrupa, "Broj časova i/ili sala moraju da budu uneti kao cifre", "Grupa greška", JOptionPane.ERROR_MESSAGE);
//                        return;
//                    }
                    int sala = Integer.parseInt(frmGrupa.getTxtBrojSale().getText().trim());
                    int brojCasova = Integer.parseInt(frmGrupa.getTxtBrojCasova().getText().trim());

                    //ako to sve prodje
                    Grupa grupa = new Grupa();
                    int idGrupe = Communication.getInstance().vratiMaksIdGrupa();
                    grupa.setIdGrupe(idGrupe);
                    rk.setGrupa(grupa);
                    rk.setDanUNedelji(danUNedelji);
                    rk.setBrojCasova(brojCasova);
                    rk.setSala(sala);
                    rk.setVreme(vreme);
                    rk.setOpisKursa(opisKursa);

                    rktm = (RasporedKursaTableModel) frmGrupa.getTabelaRasporeda().getModel();
                    List<RasporedKursa> rasporedi = rktm.getRasporedi();
                    //provera da li je isti dan u nedelji za istu grupu
                    for (RasporedKursa rasporedKursa : rasporedi) {
                        if (rasporedKursa.getDanUNedelji().equals(danUNedelji)) {
                            JOptionPane.showMessageDialog(frmGrupa, "Raspored za ovu grupu i dan u nedelji je već dodat", "Grupa greška", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                    rktm.dodajRaspored(rk);
                    //da se obrisu podaci u formi za unos novih podataka
                    frmGrupa.getTxtBrojSale().setText("");
                    frmGrupa.getTxtBrojCasova().setText("");
                    frmGrupa.getTxtAreaOpisKursa().setText("");
                    frmGrupa.getTxtVreme().setText("");
                } catch (Exception ex) {
                    Logger.getLogger(GrupaController.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(frmGrupa, "Sistem ne može da upamti raspored", "Raspored grupa greška", JOptionPane.ERROR_MESSAGE);
                }
            }

            private boolean validateRKForm() {
                if (frmGrupa.getTxtBrojCasova().getText().isBlank()) {
                    JOptionPane.showMessageDialog(frmGrupa, "Morate uneti broj časova", "Grupa greška", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if (frmGrupa.getTxtBrojSale().getText().isBlank()) {
                    JOptionPane.showMessageDialog(frmGrupa, "Morate uneti broj sale", "Grupa greška", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if (frmGrupa.getTxtVreme().getText().isEmpty()) {
                    JOptionPane.showMessageDialog(frmGrupa, "Morate uneti vreme održavanja kursa", "Grupa greška", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                try {
                    int sala = Integer.parseInt(frmGrupa.getTxtBrojSale().getText().trim());
                    int brojCasova = Integer.parseInt(frmGrupa.getTxtBrojCasova().getText().trim());
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(frmGrupa, "Broj časova i/ili sala moraju da budu uneti kao cifre", "Grupa greška", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                if (!proveriVreme()) {
                    JOptionPane.showMessageDialog(frmGrupa, "Vreme mora biti u formatu sati:minuti (HH:mm)", "Grupa greška", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                return true;
            }

            private boolean proveriVreme() {
                try {
                    String vreme = frmGrupa.getTxtVreme().getText().trim();

                    String[] deloviVremena = vreme.split(":");
                    if (deloviVremena.length != 2) {
                        JOptionPane.showMessageDialog(frmGrupa, "Niste lepo uneli vreme", "Greška", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }

                    int sati = Integer.parseInt(deloviVremena[0]);
                    int minuti = Integer.parseInt(deloviVremena[1]);

                    if (sati < 0 || sati > 23) {
                        JOptionPane.showMessageDialog(frmGrupa, "Niste lepo uneli sate", "Greška", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }

                    if (minuti < 0 || minuti > 59) {
                        JOptionPane.showMessageDialog(frmGrupa, "Niste lepo uneli minute", "Greška", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }

                    return true;
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frmGrupa, "Neispravan unos vremena početka kursa", "Greška", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        });

        frmGrupa.addObrisiRasporedBtnAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = frmGrupa.getTabelaRasporeda().getSelectedRow();
                if (selectedRow >= 0) {
                    rktm = (RasporedKursaTableModel) frmGrupa.getTabelaRasporeda().getModel();
                    rktm.obrisiRaspored(selectedRow);
                    //rktm.refresh();
                } else {
                    JOptionPane.showMessageDialog(frmGrupa, "Morate izabrati raspored koji želite da obrišete", "Grupa greška", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frmGrupa.addSacuvajBtnAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }

            private void save() {
                try {
//                    if(frmGrupa.getTxtNazivGrupe().getText().isEmpty() || frmGrupa.getTxtDatumPocetkaKursa().getText().isEmpty()){
//                        JOptionPane.showMessageDialog(frmGrupa, "Sistem ne može da zapamti grupu polaznika kursa.", "Grupa greška", JOptionPane.ERROR_MESSAGE);
//                        JOptionPane.showMessageDialog(frmGrupa, "Nisu uneti svi podaci o grupi", "Grupa greška", JOptionPane.ERROR_MESSAGE);
//                        return;                        
//                    }

                    if (!validateForm()) {
                        return;
                    }
                    String nazivGrupe = frmGrupa.getTxtNazivGrupe().getText().trim();
                    String datumPocetka = frmGrupa.getTxtDatumPocetkaKursa().getText().trim();
                    java.sql.Date datumPocetkaKursa = java.sql.Date.valueOf(datumPocetka);

                    Grupa grupa = new Grupa();
                    int idGrupe = Communication.getInstance().vratiMaksIdGrupa();

                    ktm = (KursTableModel) frmGrupa.getTabelaKurseva().getModel();

                    int selectedRow = frmGrupa.getTabelaKurseva().getSelectedRow();
                    if (selectedRow >= 0) {
                        Kurs kurs = ktm.getKursAt(selectedRow);
                        grupa.setKurs(kurs);
                    } else {
                        JOptionPane.showMessageDialog(frmGrupa, "Sistem ne može da zapamti grupu polaznika kursa.", "Grupa greška", JOptionPane.ERROR_MESSAGE);
                        JOptionPane.showMessageDialog(frmGrupa, "Morate izabrati kurs za koji se kreira grupa", "Grupa greška", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    rktm = (RasporedKursaTableModel) frmGrupa.getTabelaRasporeda().getModel();
                    List<RasporedKursa> rasporedi = rktm.getRasporedi();

                    if (rasporedi.isEmpty()) {
                        JOptionPane.showMessageDialog(frmGrupa, "Sistem ne može da zapamti grupu polaznika kursa.", "Grupa greška", JOptionPane.ERROR_MESSAGE);
                        JOptionPane.showMessageDialog(frmGrupa, "Morate uneti bar jedan raspored termina za grupu", "Grupa greška", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    grupa.setIdGrupe(idGrupe);
                    grupa.setNazivGrupe(nazivGrupe);
                    grupa.setDatumPocetkaKursa(datumPocetkaKursa);
                    grupa.setRasporediKurseva(rasporedi);
                    Communication.getInstance().sacuvajGrupu(grupa);
                    JOptionPane.showMessageDialog(frmGrupa, "Sistem je zapamtio grupu polaznika kursa", "Čuvanje grupe", JOptionPane.INFORMATION_MESSAGE);
                    frmGrupa.dispose();
                } catch (Exception ex) {
                    Logger.getLogger(GrupaController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            private boolean validateForm() {
                String nazivGrupe = frmGrupa.getTxtNazivGrupe().getText().trim();
                String datumPocetka = frmGrupa.getTxtDatumPocetkaKursa().getText().trim();

                if (nazivGrupe.isEmpty()) {
                    JOptionPane.showMessageDialog(frmGrupa, "Sistem ne može da zapamti grupu polaznika kursa.", "Grupa greška", JOptionPane.ERROR_MESSAGE);
                    JOptionPane.showMessageDialog(frmGrupa, "Niste uneli naziv grupe", "Grupa greška", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                if (datumPocetka.isEmpty() || !(proveriDatum(datumPocetka))) {
                    JOptionPane.showMessageDialog(frmGrupa, "Sistem ne može da zapamti grupu polaznika kursa.", "Grupa greška", JOptionPane.ERROR_MESSAGE);
                    JOptionPane.showMessageDialog(frmGrupa, "Datum mora biti u formatu godina-mesec-dan (yyyy-MM-dd)", "Grupa greška", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                return true;
            }

            private boolean proveriDatum(String datumPocetka) {
                try {
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = df.parse(datumPocetka);
                } catch (ParseException ex) {
                    return false;
                }

                String[] deloviDatuma = datumPocetka.split("-");
                if (deloviDatuma.length != 3 || !proveraDelovaDatuma(deloviDatuma)) {
                    return false;
                }
                return true;
            }

            private boolean proveraDelovaDatuma(String[] deloviDatuma) {
                int godina = Integer.parseInt(deloviDatuma[0]);
                int mesec = Integer.parseInt(deloviDatuma[1]);
                int dan = Integer.parseInt(deloviDatuma[2]);

                int year = Calendar.getInstance().get(Calendar.YEAR);//trenutna godina
                if (godina > year) {
                    JOptionPane.showMessageDialog(frmGrupa, "Niste lepo uneli godinu", "Grupa greška", JOptionPane.ERROR_MESSAGE);

                    return false;
                }
                if (mesec < 1 || mesec > 12) {
                    JOptionPane.showMessageDialog(frmGrupa, "Niste lepo uneli mesec", "Grupa greška", JOptionPane.ERROR_MESSAGE);

                    return false;
                }
                if (mesec == 2) {
                    if (dan < 1 || dan > 28) {
                        JOptionPane.showMessageDialog(frmGrupa, "Niste lepo uneli dan", "Grupa greška", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }
                if (mesec == 1 || mesec == 3 || mesec == 5 || mesec == 7 || mesec == 8 || mesec == 10 || mesec == 12) {
                    if (dan < 1 || dan > 31) {
                        JOptionPane.showMessageDialog(frmGrupa, "Niste lepo uneli dan", "Grupa greška", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }
                if (mesec == 4 || mesec == 6 || mesec == 9 || mesec == 11) {
                    if (dan < 1 || dan > 30) {
                        JOptionPane.showMessageDialog(frmGrupa, "Niste lepo uneli dan", "Grupa greška", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }
                return true;
            }
        });

        frmGrupa.addOtkaziBtnAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frmGrupa.dispose();
            }
        });

    }

    private void fillTableKursevi() {
        try {
            List<Kurs> kursevi = Communication.getInstance().vratiSveKurseve();
            if (kursevi.isEmpty()) {
                JOptionPane.showMessageDialog(frmGrupa, "Trenutno nema kurseva u bazi. Kreirajte prvo kurseve", "Prijava greška", JOptionPane.ERROR_MESSAGE);
                frmGrupa.dispose();
            }
            ktm = new KursTableModel(kursevi);
            frmGrupa.getTabelaKurseva().setModel(ktm);
        } catch (Exception ex) {
            Logger.getLogger(GrupaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setUpComponents(FormMode formMode) throws Exception {
        switch (formMode) {
            case FORM_ADD://sve je omoguceno kad se dodaje
                frmGrupa.getBtnOtkazi().setEnabled(true);
                frmGrupa.getBtnDodajRaspored().setEnabled(true);
                frmGrupa.getBtnObrisiRaspored().setEnabled(true);
                frmGrupa.getBtnSacuvaj().setEnabled(true);

                frmGrupa.getTxtAreaOpisKursa().setEnabled(true);
                frmGrupa.getCmbDanUNedelji().setEnabled(true);
                frmGrupa.getTxtBrojCasova().setEnabled(true);
                frmGrupa.getTxtBrojSale().setEnabled(true);
                frmGrupa.getTxtDatumPocetkaKursa().setEnabled(true);
                frmGrupa.getTxtNazivGrupe().setEnabled(true);
                frmGrupa.getTxtVreme().setEnabled(true);
                frmGrupa.getTabelaKurseva().setEnabled(true);
                frmGrupa.getTabelaRasporeda().setEnabled(true);
                break;

            case FORM_VIEW://ovo je kad se klikne na detalji preko pretrage
                //moze samo dugme otkazi
                frmGrupa.getBtnOtkazi().setEnabled(true);
                frmGrupa.getBtnDodajRaspored().setEnabled(false);
                frmGrupa.getBtnObrisiRaspored().setEnabled(false);
                frmGrupa.getBtnSacuvaj().setEnabled(false);

                frmGrupa.getTxtAreaOpisKursa().setEnabled(false);
                frmGrupa.getCmbDanUNedelji().setEnabled(false);
                frmGrupa.getTxtBrojCasova().setEnabled(false);
                frmGrupa.getTxtBrojSale().setEnabled(false);
                frmGrupa.getTxtDatumPocetkaKursa().setEnabled(false);
                frmGrupa.getTxtNazivGrupe().setEnabled(false);
                frmGrupa.getTxtVreme().setEnabled(false);
                frmGrupa.getTabelaKurseva().setEnabled(false);
                frmGrupa.getTabelaRasporeda().setEnabled(false);
                
                //izvlacenje podataka o grupi
                Grupa grupa = (Grupa) MainCoordinator.getInstance().getParam(Constants.PARAM_GRUPA);
                //postavljanje podataka o grupi
                frmGrupa.getTxtNazivGrupe().setText(grupa.getNazivGrupe());
                frmGrupa.getTxtDatumPocetkaKursa().setText(sdf.format(grupa.getDatumPocetkaKursa()));
                List<RasporedKursa> rasporedi = Communication.getInstance().vratiSveRasporedeKurseva();
                RasporedKursaTableModel rkt = (RasporedKursaTableModel) frmGrupa.getTabelaRasporeda().getModel();
                //frmGrupa.getTabelaKurseva().setModel(rktm);
                for (RasporedKursa raspored : rasporedi) {
                    if (raspored.getGrupa().getIdGrupe() == grupa.getIdGrupe()) {
                        rkt.dodajRaspored(raspored);
                    }
                }
                
                
                //////////////////////////////////////
                //postavljanje tog odabranog kursa u tabelu kurseva
                List<Kurs> kurs = new ArrayList<>();
                Kurs kursIzPretrage = (Kurs) MainCoordinator.getInstance().getParam(Constants.PARAM_KURS);
                kurs.add(kursIzPretrage);
                KursTableModel ktmm = (KursTableModel) frmGrupa.getTabelaKurseva().getModel();
                ktmm.setKursevi(kurs);

                break;
        }
    }

}
