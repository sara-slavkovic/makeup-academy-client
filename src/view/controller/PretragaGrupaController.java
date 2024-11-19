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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import view.constans.Constants;
import view.coordinator.MainCoordinator;
import view.form.FrmPretragaGrupa;
import view.form.components.tableModel.GrupaTableModel;

/**
 *
 * @author Korisnik
 */
public class PretragaGrupaController {

    private FrmPretragaGrupa frmPretragaGrupa;
    private GrupaTableModel gtm;

    public PretragaGrupaController(FrmPretragaGrupa frmPretragaGrupa) {
        this.frmPretragaGrupa = frmPretragaGrupa;
        addActionListener();
    }

    public void openForm() {
        frmPretragaGrupa.setLocationRelativeTo(MainCoordinator.getInstance().getMainAdminController().getFrmMainAdmin());
        prepareView();
        frmPretragaGrupa.setVisible(true);
    }

    private void prepareView() {
        frmPretragaGrupa.setTitle("Pretraga grupa");
    }

    private void addActionListener() {
        frmPretragaGrupa.getBtnDetaljiAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = frmPretragaGrupa.getTabelaGrupa().getSelectedRow();
                if (selectedRow >= 0) {
                    GrupaTableModel gtmm = (GrupaTableModel) frmPretragaGrupa.getTabelaGrupa().getModel();
                    Grupa grupa = gtmm.getGrupaAt(selectedRow);

                    //////////////
                    try {
                        List<Kurs> kursevi = Communication.getInstance().vratiKurseveNaziv(grupa.getKurs().getNazivKursa());
                        Kurs kurs = kursevi.get(0);
                        MainCoordinator.getInstance().addParam(Constants.PARAM_KURS, kurs);
                    } catch (Exception ex) {
                        Logger.getLogger(PretragaGrupaController.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(frmPretragaGrupa, "Sistem ne može da učita kurs");
                    }
                    //////////////

                    MainCoordinator.getInstance().addParam(Constants.PARAM_GRUPA, grupa);
                    MainCoordinator.getInstance().openGrupaDetailsForm();
                } else {
                    JOptionPane.showMessageDialog(frmPretragaGrupa, "Sistem ne može da učita grupu polaznika kursa.", "Grupa greška", JOptionPane.ERROR_MESSAGE);
                    JOptionPane.showMessageDialog(frmPretragaGrupa, "Niste odabrali grupu iz tabele", "Grupa greška", JOptionPane.ERROR_MESSAGE);
                }
                fillTableGrupa();
            }
        });

        frmPretragaGrupa.getBtnFiltrirajAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String nazivIzPretrage = frmPretragaGrupa.getTxtNazivGrupe().getText().trim();
                    if (nazivIzPretrage.isEmpty()) {
                        JOptionPane.showMessageDialog(frmPretragaGrupa, "Morate uneti naziv grupe", "Grupa greška", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    List<Grupa> grupe = Communication.getInstance().vratiGrupeNaziv(nazivIzPretrage);
                    if (grupe.isEmpty()) {
                        fillTableGrupa();
                        JOptionPane.showMessageDialog(frmPretragaGrupa, "Sistem ne može da nađe grupe polaznika kursa po zadatoj vrednosti", "Grupa greška", JOptionPane.ERROR_MESSAGE);
                        JOptionPane.showMessageDialog(frmPretragaGrupa, "Ovakvih grupa nema u bazi", "Grupa greška", JOptionPane.ERROR_MESSAGE);
                        frmPretragaGrupa.getTxtNazivGrupe().setText("");
                        return;
                    }
                    //ako sve to uspe
                    JOptionPane.showMessageDialog(frmPretragaGrupa, "Sistem je našao grupe polaznika kursa po zadatoj vrednosti");
                    gtm = (GrupaTableModel) frmPretragaGrupa.getTabelaGrupa().getModel();
                    gtm.setGrupe(grupe);
                    frmPretragaGrupa.getTxtNazivGrupe().setText("");
                } catch (Exception ex) {
                    Logger.getLogger(PretragaGrupaController.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(frmPretragaGrupa, "Sistem ne može da nađe grupe polaznika kursa po zadatoj vrednosti", "Grupa greška", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frmPretragaGrupa.getBtnPrikaziSveAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fillTableGrupa();
            }
        });
        
        //izvestaj
        frmPretragaGrupa.getBtnKreirajIzvestajAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = frmPretragaGrupa.getTabelaGrupa().getSelectedRow();
                if(selectedRow == -1){
                    JOptionPane.showMessageDialog(frmPretragaGrupa, "Sistem ne može da učita grupu polaznika kursa.", "Grupa greška", JOptionPane.ERROR_MESSAGE);
                    JOptionPane.showMessageDialog(frmPretragaGrupa, "Niste odabrali grupu iz tabele", "Grupa greška", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else{
                    try {
                        GrupaTableModel gtm = (GrupaTableModel) frmPretragaGrupa.getTabelaGrupa().getModel();
                        Grupa g = gtm.getGrupaAt(selectedRow);
                        JOptionPane.showMessageDialog(frmPretragaGrupa, "Sistem je učitao grupu polaznika kursa");

                        List<RasporedKursa> rasporedKursa = g.getRasporediKurseva();
                        //slanje parametara preko hashmape
                        Map<String,Object> parameters = new HashMap<>();
                        parameters.put("REPORT_PARAMETERS_MAP", rasporedKursa);
                        JRBeanCollectionDataSource rasporedKursaDataSource = new JRBeanCollectionDataSource(rasporedKursa);

                        parameters.put("rasporediKurseva", rasporedKursaDataSource);
                        List<Grupa> listaGrupa = new ArrayList<>();
                        listaGrupa.add(g);

                        JasperPrint jprint1 = JasperFillManager.fillReport("Grupa.jasper", parameters,  new JRBeanCollectionDataSource(listaGrupa));
                        JasperViewer viewer = new JasperViewer(jprint1, false);
                        viewer.setVisible(true);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frmPretragaGrupa, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }            
                }                
            }
        });

        frmPretragaGrupa.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                fillTableGrupa();
            }
        });
    }

    private void fillTableGrupa() {
        try {
            List<Grupa> grupe = Communication.getInstance().vratiSveGrupe();
            gtm = new GrupaTableModel(grupe);
            frmPretragaGrupa.getTabelaGrupa().setModel(gtm);
        } catch (Exception ex) {
            Logger.getLogger(PretragaGrupaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
