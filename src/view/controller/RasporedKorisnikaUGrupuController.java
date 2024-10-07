/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.controller;

import communication.Communication;
import domain.Grupa;
import domain.Kurs;
import domain.Prijava;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import view.coordinator.MainCoordinator;
import view.form.FrmRasporedKorisnikaUGrupu;
import view.form.components.tableModel.GrupaTableModel;
import view.form.components.tableModel.PrijavaTableModel;

/**
 *
 * @author Korisnik
 */
public class RasporedKorisnikaUGrupuController {

    private FrmRasporedKorisnikaUGrupu frmRaspored;
    private PrijavaTableModel ptm;
    private GrupaTableModel gtm;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public RasporedKorisnikaUGrupuController(FrmRasporedKorisnikaUGrupu frmRaspored) {
        this.frmRaspored = frmRaspored;
        addActionListener();
    }

    public void openForm() {
        frmRaspored.setLocationRelativeTo(MainCoordinator.getInstance().getMainAdminController().getFrmMainAdmin());
        prepareView();
        frmRaspored.setVisible(true);
    }

    private void prepareView() {
        frmRaspored.setTitle("Raspored korisnika u grupu");
        frmRaspored.getBtnRasporedi().setEnabled(false);
        fillTablePrijava();
    }

    private void addActionListener() {
        frmRaspored.addbtnNadjiListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int selectedRow = frmRaspored.getTabelaPrijava().getSelectedRow();
                    if (selectedRow >= 0) {
                        PrijavaTableModel ptmm = (PrijavaTableModel) frmRaspored.getTabelaPrijava().getModel();
                        Prijava prijava = ptmm.getPrijavaAt(selectedRow);

                        Kurs k = prijava.getKurs();

                        List<Grupa> grupeZaPrikaz = new ArrayList<>();
                        List<Grupa> grupe = Communication.getInstance().vratiSveGrupe();
                        if (grupe.isEmpty()) {
                            JOptionPane.showMessageDialog(frmRaspored, "Sistem ne može da nađe grupe polaznika kursa za prijavljeni kurs.", "Raspoređivanje greška", JOptionPane.ERROR_MESSAGE);
                            JOptionPane.showMessageDialog(frmRaspored, "Ne postoje grupe u bazi", "Raspoređivanje greška", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        for (Grupa grupa : grupe) {
                            if (grupa.getKurs().equals(k)) {
                                grupeZaPrikaz.add(grupa);
                            }
                        }

                        if (grupeZaPrikaz.isEmpty()) {
                            if (gtm != null) {
                                gtm.obrisiSve();//da bi se obrisao prethodni prikaz grupa ako je za neki drugi kurs bilo rezultata
                            }
                            JOptionPane.showMessageDialog(frmRaspored, "Sistem ne može da nađe grupe polaznika kursa za prijavljeni kurs.", "Raspoređivanje greška", JOptionPane.ERROR_MESSAGE);
                            JOptionPane.showMessageDialog(frmRaspored, "Ne postoje grupe za ovaj kurs.", "Raspoređivanje greška", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        JOptionPane.showMessageDialog(frmRaspored, "Sistem je našao grupe za prijavljeni kurs");
                        gtm = new GrupaTableModel(grupeZaPrikaz);
                        frmRaspored.getTabelaGrupa().setModel(gtm);
                        frmRaspored.getBtnRasporedi().setEnabled(true);
                    } else {
                        JOptionPane.showMessageDialog(frmRaspored, "Sistem ne može da nađe grupe polaznika kursa za prijavljeni kurs.", "Raspoređivanje greška", JOptionPane.ERROR_MESSAGE);
                        JOptionPane.showMessageDialog(frmRaspored, "Niste selektovali prijavu.", "Raspoređivanje greška", JOptionPane.ERROR_MESSAGE);

                    }
                } catch (Exception ex) {
                    Logger.getLogger(RasporedKorisnikaUGrupuController.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(frmRaspored, "Sistem ne može da nađe grupe polaznika kursa za prijavljeni kurs.", "Raspoređivanje greška", JOptionPane.ERROR_MESSAGE);
                    //JOptionPane.showMessageDialog(frmRaspored, "Niste selektovali prijavu za rasporedjivanje", "Raspored greška", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frmRaspored.addbtnRasporediListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                edit();

            }

            private void edit() {
                try {
                    int selectedRowPrijava = frmRaspored.getTabelaPrijava().getSelectedRow();
                    int selectedRowGrupa = frmRaspored.getTabelaGrupa().getSelectedRow();

                    if (selectedRowPrijava >= 0 && selectedRowGrupa >= 0) {
                        PrijavaTableModel ptmm = (PrijavaTableModel) frmRaspored.getTabelaPrijava().getModel();
                        Prijava prijava = ptmm.getPrijavaAt(selectedRowPrijava);
                        GrupaTableModel gtmm = (GrupaTableModel) frmRaspored.getTabelaGrupa().getModel();
                        Grupa grupa = gtmm.getGrupaAt(selectedRowGrupa);

                        if (prijava.getGrupa().getNazivGrupe().equals(grupa.getNazivGrupe())) {
                            JOptionPane.showMessageDialog(frmRaspored, "Sistem ne može da rasporedi polaznika u grupu kursa", "Raspored greška", JOptionPane.ERROR_MESSAGE);
                            JOptionPane.showMessageDialog(frmRaspored, "Korisnik je već prati kurs u odabranoj grupi", "Raspored greška", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        prijava.setGrupa(grupa);
                        Communication.getInstance().promeniPrijavu(prijava);

                        JOptionPane.showMessageDialog(frmRaspored, "Sistem je uspešno rasporedio polaznika u grupu kursa");
                        fillTablePrijava();
                    } else {
                        JOptionPane.showMessageDialog(frmRaspored, "Sistem ne može da rasporedi polaznika u grupu kursa", "Raspored greška", JOptionPane.ERROR_MESSAGE);
                        JOptionPane.showMessageDialog(frmRaspored, "Niste selektovali grupu i/ili prijavu za raspoređivanje ", "Raspored greška", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    frmRaspored.dispose();
                } catch (Exception ex) {
                    Logger.getLogger(RasporedKorisnikaUGrupuController.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(frmRaspored, "Sistem ne može da rasporedi polaznika u grupu kursa", "Raspored greška", JOptionPane.ERROR_MESSAGE);

                }
            }
        });
    }

    private void fillTablePrijava() {
        try {
            List<Prijava> prijave = Communication.getInstance().vratiSvePrijave();
            ptm = new PrijavaTableModel(prijave);
            frmRaspored.getTabelaPrijava().setModel(ptm);
        } catch (Exception ex) {
            Logger.getLogger(RasporedKorisnikaUGrupuController.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(frmRaspored, "Greška pri učitvanju prijava", "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }

}
