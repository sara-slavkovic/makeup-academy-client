/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.coordinator;

import java.util.HashMap;
import java.util.Map;
import view.controller.GrupaController;
import view.controller.KursController;
import view.controller.LoginController;
import view.controller.MainAdminController;
import view.controller.MainKorisnikController;
import view.controller.PretragaGrupaController;
import view.controller.PretragaKursevaController;
import view.controller.PrijavaController;
import view.controller.RasporedKorisnikaUGrupuController;
import view.controller.RegistracijaController;
import view.form.FrmGrupa;
import view.form.FrmKurs;
import view.form.FrmLogin;
import view.form.FrmMainAdmin;
import view.form.FrmMainKorisnik;
import view.form.FrmPretragaGrupa;
import view.form.FrmPretragaKurseva;
import view.form.FrmPrijava;
import view.form.FrmRasporedKorisnikaUGrupu;
import view.form.FrmRegistracija;
import view.form.util.FormMode;

/**
 *
 * @author Korisnik
 */
public class MainCoordinator {

    //singleton
    private static MainCoordinator instance;
    private final MainKorisnikController mainKorisnikController;
    private final MainAdminController mainAdminController;
    private final Map<String, Object> params;

    private MainCoordinator() {
        mainKorisnikController = new MainKorisnikController(new FrmMainKorisnik());
        mainAdminController = new MainAdminController(new FrmMainAdmin());
        params = new HashMap<>();//parametri se salju preko hash mape
    }

    public static MainCoordinator getInstance() {
        if (instance == null) {
            instance = new MainCoordinator();
        }
        return instance;
    }

    public void addParam(String name, Object key) {
        params.put(name, key);
    }

    public Object getParam(String name) {
        return params.get(name);
    }

    public MainKorisnikController getMainKorisnikController() {
        return mainKorisnikController;
    }

    public MainAdminController getMainAdminController() {
        return mainAdminController;
    }

    ////////////////////////////////////////////////////////
    //metode za koordinisanje formama
    ////////////////////////////////////////////////////////
    public void openLoginForm() {
        LoginController loginController = new LoginController(new FrmLogin());
        loginController.openForm();
    }

    public void openRegistrationForm(FrmLogin frmLogin) {
        RegistracijaController registracijaController = new RegistracijaController(new FrmRegistracija(frmLogin, true));
        registracijaController.openForm();
    }

    public void openMainAdminForm() {
        mainAdminController.openForm();
    }

    public void openMainKorisnikForm() {
        mainKorisnikController.openForm();
    }

    public void openAddKursForm() {
        KursController kursController = new KursController(new FrmKurs(mainAdminController.getFrmMainAdmin(), true));
        kursController.openForm(FormMode.FORM_ADD);
    }

    public void openSearchKursForm() {
        FrmPretragaKurseva frmPretragaKurseva = new FrmPretragaKurseva(mainAdminController.getFrmMainAdmin(), true);
        PretragaKursevaController pretragaKursevaController = new PretragaKursevaController(frmPretragaKurseva);
        pretragaKursevaController.openForm();
    }

    public void openKursDetailsForm() {
        FrmKurs frmKurs = new FrmKurs(mainAdminController.getFrmMainAdmin(), true);
        KursController kursController = new KursController(frmKurs);
        kursController.openForm(FormMode.FORM_VIEW);
    }

    public void openAddPrijavaForm() {
        FrmPrijava frmPrijava = new FrmPrijava(mainKorisnikController.getFrmMainKorisnik(), true);
        PrijavaController prijavaController = new PrijavaController(frmPrijava);
        prijavaController.openForm();
    }

    public void openAddGrupaForm() {
        FrmGrupa frmGrupa = new FrmGrupa(mainAdminController.getFrmMainAdmin(), true);
        GrupaController grupaController = new GrupaController(frmGrupa);
        grupaController.openForm(FormMode.FORM_ADD);
    }

    public void openSearchGrupaForm() {
        FrmPretragaGrupa frmPretragaGrupa = new FrmPretragaGrupa(mainAdminController.getFrmMainAdmin(), true);
        PretragaGrupaController pretragaGrupaController = new PretragaGrupaController(frmPretragaGrupa);
        pretragaGrupaController.openForm();
    }

    public void openGrupaDetailsForm() {
        FrmGrupa frmGrupa = new FrmGrupa(mainAdminController.getFrmMainAdmin(), true);
        GrupaController grupaController = new GrupaController(frmGrupa);
        grupaController.openForm(FormMode.FORM_VIEW);
    }

    public void openRasporediUGrupuForm() {
        FrmRasporedKorisnikaUGrupu frmRasporedKorisnikaUGrupu = new FrmRasporedKorisnikaUGrupu(mainAdminController.getFrmMainAdmin(), true);
        RasporedKorisnikaUGrupuController rpController = new RasporedKorisnikaUGrupuController(frmRasporedKorisnikaUGrupu);
        rpController.openForm();
    }

}
