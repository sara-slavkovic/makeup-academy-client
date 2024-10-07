/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package communication;

import domain.Grupa;
import domain.Korisnik;
import domain.Kurs;
import domain.Predavac;
import domain.Prijava;
import domain.RasporedKursa;
import domain.TipSminkanja;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import view.constans.Constants;
import view.coordinator.MainCoordinator;

/**
 *
 * @author Korisnik
 */
public class Communication {

    //singleton
    private static Communication instance;
    Socket socket;
    Sender sender;//posalji
    Receiver receiver;///primi

    private Communication() {
        try {
            socket = new Socket("localhost", 9000);
            sender = new Sender(socket);
            receiver = new Receiver(socket);
        } catch (IOException ex) {
            Logger.getLogger(Communication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Communication getInstance() {
        if (instance == null) {
            instance = new Communication();
        }
        return instance;
    }

    public Socket getSocket() {
        return socket;
    }

    /////////////////////////////////////////////////////////////////
    // metode za slanje zahteva serveru i primanje odgovora od njega
    /////////////////////////////////////////////////////////////////
    public Korisnik login(String username, String password) throws Exception {
        Korisnik korisnik = new Korisnik();
        korisnik.setKorisnickoIme(username);
        korisnik.setLozinka(password);

        Request request = new Request(Operation.LOGIN, korisnik);//ovo je kao klzahtev kz = new klzaht(...)
        sender.send(request);//komunikacija.getinst.posaljizahtev
        /////////////ide se u proccessclientrequests da se obradi zahtev
        Response response = (Response) receiver.receive();//serverskiodg so = komunikac.getinst.primiodg
        if (response.getException() == null) {//ako se pojavi exception
            return (Korisnik) response.getResult();//so.getodgovor
        } else {
            throw response.getException();
        }
    }

    public void krajRada() throws Exception {
        Korisnik ulogovani = (Korisnik) MainCoordinator.getInstance().getParam(Constants.CURRENT_USER);
        Request request = new Request(Operation.KRAJ_RADA, ulogovani);
        sender.send(request);
    }

    public void dodajKorisnika(Korisnik k) throws Exception {
        Request request = new Request(Operation.SACUVAJ_KORISNIKA, k);
        sender.send(request);
        /////////////
        Response response = (Response) receiver.receive();
        if (response.getException() != null) {
            throw response.getException();
        }

    }

    public List<Kurs> vratiSveKurseve() throws Exception {
        Request request = new Request(Operation.VRATI_SVE_KURSEVE, null);
        sender.send(request);
        /////////
        Response response = (Response) receiver.receive();
        if (response.getException() == null) {
            return (List<Kurs>) response.getResult();
        } else {
            throw response.getException();
        }
    }

    public void sacuvajKurs(Kurs kurs) throws Exception {
        Request request = new Request(Operation.SACUVAJ_KURS, kurs);
        sender.send(request);
        ////////////////
        Response response = (Response) receiver.receive();
        if (response.getException() != null) {
            throw response.getException();
        }
    }

    public List<TipSminkanja> vratiSveTipoveSminkanja() throws Exception {
        Request request = new Request(Operation.VRATI_SVE_TIPOVE_SMINKANJA, null);
        sender.send(request);
        ///////////////
        Response response = (Response) receiver.receive();
        if (response.getException() == null) {
            return (List<TipSminkanja>) response.getResult();
        } else {
            throw response.getException();
        }
    }

    public List<Predavac> vratiSvePredavace() throws Exception {
        Request request = new Request(Operation.VRATI_SVE_PREDAVACE, null);
        sender.send(request);
        ///////////////////////
        Response response = (Response) receiver.receive();
        if (response.getException() == null) {
            return (List<Predavac>) response.getResult();
        } else {
            throw response.getException();
        }
    }

    public void obrisiKurs(Kurs kurs) throws Exception {
        Request request = new Request(Operation.OBRISI_KURS, kurs);
        sender.send(request);
        ////////////
        Response response = (Response) receiver.receive();
        if (response.getException() != null) {
            throw response.getException();
        }
    }

    public List<Kurs> vratiKurseveNaziv(String nazivIzPretrage) throws Exception {
        Request request = new Request(Operation.VRATI_KURSEVE_NAZIV, nazivIzPretrage);
        sender.send(request);
        /////////////////
        Response response = (Response) receiver.receive();
        if (response.getException() == null) {
            return (List<Kurs>) response.getResult();
        } else {
            throw response.getException();
        }
    }

    public void izmeniKurs(Kurs kurs) throws Exception {
        Request request = new Request(Operation.IZMENI_KURS, kurs);
        sender.send(request);
        /////////////////////
        Response response = (Response) receiver.receive();
        if (response.getException() != null) {
            throw response.getException();
        }
    }

    public List<Grupa> vratiSveGrupe() throws Exception {
        Request request = new Request(Operation.VRATI_SVE_GRUPE, null);
        sender.send(request);
        /////////////////
        Response response = (Response) receiver.receive();
        if (response.getException() == null) {
            return (List<Grupa>) response.getResult();
        } else {
            throw response.getException();
        }
    }

    public int vratiMaksIdGrupa() throws Exception {
        Request request = new Request(Operation.VRATI_MAKS_ID_GRUPA, null);
        sender.send(request);
        ///////////////////////
        Response response = (Response) receiver.receive();
        if (response.getException() == null) {
            return (int) response.getResult();
        } else {
            throw response.getException();
        }
    }

    public Grupa vratiGrupu(int grupaId) throws Exception {
        Request request = new Request(Operation.VRATI_GRUPU, grupaId);
        sender.send(request);
        //////////////////////////
        Response response = (Response) receiver.receive();
        if (response.getException() == null) {
            return (Grupa) response.getResult();
        } else {
            throw response.getException();
        }
    }

    public void sacuvajPrijavu(Prijava prijava) throws Exception {
        Request request = new Request(Operation.SACUVAJ_PRIJAVU, prijava);
        sender.send(request);
        //////////////////////////
        Response response = (Response) receiver.receive();
        if (response.getException() != null) {
            throw response.getException();
        }
    }

    public List<Grupa> vratiGrupeNaziv(String nazivIzPretrage) throws Exception {
        Request request = new Request(Operation.VRATI_GRUPE_NAZIV, nazivIzPretrage);
        sender.send(request);
        /////////////////
        Response response = (Response) receiver.receive();
        if (response.getException() == null) {
            return (List<Grupa>) response.getResult();
        } else {
            throw response.getException();
        }
    }

    public List<RasporedKursa> vratiSveRasporedeKurseva() throws Exception {
        Request request = new Request(Operation.VRATI_SVE_RASPOREDE, null);
        sender.send(request);
        ///////////////////////
        Response response = (Response) receiver.receive();
        if (response.getException() == null) {
            return (List<RasporedKursa>) response.getResult();
        } else {
            throw response.getException();
        }
    }

    public void sacuvajGrupu(Grupa grupa) throws Exception {
        Request request = new Request(Operation.SACUVAJ_GRUPU, grupa);
        sender.send(request);
        //////////////////////////
        Response response = (Response) receiver.receive();
        if (response.getException() != null) {
            throw response.getException();
        }
    }

    public List<Prijava> vratiSvePrijave() throws Exception {
        Request request = new Request(Operation.VRATI_SVE_PRIJAVE, null);
        sender.send(request);
        ///////////////////////
        Response response = (Response) receiver.receive();
        if (response.getException() == null) {
            return (List<Prijava>) response.getResult();
        } else {
            throw response.getException();
        }
    }

    public void promeniPrijavu(Prijava prijava) throws Exception {
        Request request = new Request(Operation.IZMENI_PRIJAVU, prijava);
        sender.send(request);
        //////////////////////////
        Response response = (Response) receiver.receive();
        if (response.getException() != null) {
            throw response.getException();
        }
    }

}
