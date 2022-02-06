package main.java.hr.java.covidportal.controlleri;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.hr.java.covidportal.model.Bolest;
import main.java.hr.java.covidportal.model.Osoba;
import main.java.hr.java.covidportal.model.Zupanija;
import main.java.hr.java.covidportal.niti.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Služi za upravljanje mogućnostima unutar ekrana za unos nove osobe. Moguće je unijeti ime, prezime, datum rođenja,
 * županiju, bolest, i kontakte nove osobe.
 */
public class DodavanjeNoveOsobeController implements Initializable {

    @FXML
    private TextField imeNoveOsobe;

    @FXML
    private TextField prezimeNoveOsobe;

    @FXML
    private DatePicker datumRodjenjaNoveOsobe;

    @FXML
    private ComboBox<Zupanija> zupanijaNoveOsobe;

    @FXML
    private ComboBox<Bolest> bolestNoveOsobe;

    @FXML
    private ComboBox<Osoba> kontaktiNoveOsobe;

    @FXML
    private TableView<Osoba> tablicaKontakataNoveOsobe;

    @FXML
    private TableColumn<Osoba, String> stupacImenaKontakataNoveOsobe;

    @FXML
    private TableColumn<Osoba, String> stupacPrezimenaKontakataNoveOsobe;

    private Zupanija odabranaZupanija;
    private Bolest odabranaBolest;

    /**
     * Služi inicijalizaciji ekrana za unos nove osobe. Ekran sadrži sva potrebna polja kako bi se unijeli
     * odgovarajući podatci.
     * @param url link
     * @param resourceBundle resursi
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<List<Zupanija>> listaZup = executor.submit(new DohvatSvihZupanijaNit());
        Future<List<Bolest>> listaBol = executor.submit(new DohvatSvihBolestiNit(0));
        Future<List<Osoba>> listaOs = executor.submit(new DohvatSvihOsobaNit());

        try {
            zupanijaNoveOsobe.getItems().addAll(listaZup.get());
            bolestNoveOsobe.getItems().addAll(listaBol.get());
            kontaktiNoveOsobe.getItems().addAll(listaOs.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        stupacImenaKontakataNoveOsobe.setCellValueFactory(new PropertyValueFactory<>("ime"));
        stupacPrezimenaKontakataNoveOsobe.setCellValueFactory(new PropertyValueFactory<>("prezime"));

        zupanijaNoveOsobe.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) -> {
            ExecutorService executor2 = Executors.newSingleThreadExecutor();
            Future<Zupanija> zup = executor2.submit(new DohvatZupanijePremaPozicijiNit(t1.longValue()));
            try {
                odabranaZupanija = zup.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

        bolestNoveOsobe.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) -> {
            ExecutorService executor3 = Executors.newSingleThreadExecutor();
            Future<Bolest> bol = executor3.submit(new DohvatBolestiPremaPozicijiNit(t1.longValue()));
            try {
                odabranaBolest = bol.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

        kontaktiNoveOsobe.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) -> {
            Osoba o = null;
            try {
                o = Executors.newSingleThreadExecutor()
                        .submit(new DohvatOsobePremaPozicijiNit(t1.longValue()))
                        .get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            if(!provjeraOsobe(tablicaKontakataNoveOsobe, o)) {
                tablicaKontakataNoveOsobe.getItems().add(o);
            } else {
                System.out.println("Osoba je već unesena!");
            }
        });

        tablicaKontakataNoveOsobe.setRowFactory(t -> {
            TableRow<Osoba> red = new TableRow<>();
            red.setOnMouseClicked(event -> {
                if(event.getClickCount() == 1 && (!red.isEmpty())) {
                    tablicaKontakataNoveOsobe.getItems().remove(red.getItem());
                }
            });
            return red;
        });
    }

    /**
     * Služi dodavanju nove osobe u bazu podataka. Funkcija prvo provjerava jesu li svi potrebni podatci uneseni,
     * a zatim unosi te podatke u bazu.
     */
    public void dodajOsobu() {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Spremanje podataka");
        alert.setHeaderText("Unos osobe");

        if(imeNoveOsobe.getText().equals("") ||
                prezimeNoveOsobe.getText().equals("") ||
                datumRodjenjaNoveOsobe.getValue() == null ||
                odabranaZupanija == null ||
                odabranaBolest == null)
        {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Neuspješno spremanje nove osobe! Provjerite unesene podatke.");
            alert.showAndWait();
        }
        else {
            Osoba osoba = new Osoba.Builder()
                    .brojaId(100L)
                    .sIme(imeNoveOsobe.getText())
                    .sPrezime(prezimeNoveOsobe.getText())
                    .ovajDatum(datumRodjenjaNoveOsobe.getValue())
                    .uZupanija(odabranaZupanija)
                    .imaZarazenBolescu(odabranaBolest)
                    .uzKontaktiraneOsobe(tablicaKontakataNoveOsobe.getItems())
                    .build();

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(new SpremanjeNoveOsobeNit(osoba));

            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setContentText("Nova osoba je uspješno spremljena!");
            alert.showAndWait();
        }
    }

    /**
     * Služi za provjeru nalazi li se odabrana osoba već u tablici kontakata. U slučaju da je, funkcija vraća TRUE.
     * @param tablica tablica kontakata
     * @param o podatak o odabranoj osobi
     * @return vraća TRUE/FALSE ovisno o tome postoji li već ista osoba u tablici
     */
    public Boolean provjeraOsobe(TableView<Osoba> tablica, Osoba o) {
        for(Osoba osoba : tablica.getItems()) {
            if(o.equals(osoba)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

}
