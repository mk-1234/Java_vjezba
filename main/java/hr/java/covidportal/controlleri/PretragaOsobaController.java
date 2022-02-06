package main.java.hr.java.covidportal.controlleri;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.hr.java.covidportal.model.Bolest;
import main.java.hr.java.covidportal.model.Osoba;
import main.java.hr.java.covidportal.model.Zupanija;
import main.java.hr.java.covidportal.niti.DohvatSvihOsobaNit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Služi za upravljanje mogućnostima unutar ekrana za pretragu osoba. Moguće je unijeti ime i prezime po kojima se treba
 * vršiti pretraga. Sve osobe odgovarajućeg imena i prezimena će biti ispisane u tablici.
 */
public class PretragaOsobaController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(PretragaOsobaController.class);

    @FXML
    private TextField imeZaPretragu;

    @FXML
    private TextField prezimeZaPretragu;

    @FXML
    private TableView<Osoba> tablicaOsoba;

    @FXML
    private TableColumn<Osoba, String> stupacImeOsobe;

    @FXML
    private TableColumn<Osoba, String> stupacPrezimeOsobe;

    @FXML
    private TableColumn<Osoba, Integer> stupacStarostOsobe;

    @FXML
    private TableColumn<Osoba, Zupanija> stupacZupanijaOsobe;

    @FXML
    private TableColumn<Osoba, Bolest> stupacBolestOsobe;

    @FXML
    private TableColumn<Osoba, List<Osoba>> stupacKontaktiOsobe;

    private ObservableList<Osoba> observableListaOsoba;

    /**
     * Služi inicijalizaciji ekrana za pretragu po osobama. Na početku su u tablici navedene sve osobe koje
     * se mogu pretraživati.
     * @param url link
     * @param resourceBundle resursi
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        stupacImeOsobe.setCellValueFactory(new PropertyValueFactory<Osoba, String>("ime"));
        stupacPrezimeOsobe.setCellValueFactory(new PropertyValueFactory<Osoba, String>("prezime"));
        stupacStarostOsobe.setCellValueFactory(
                data -> new ReadOnlyObjectWrapper(izracunStarosti(data.getValue().getDatumRodjenja())));
        stupacZupanijaOsobe.setCellValueFactory(new PropertyValueFactory<Osoba, Zupanija>("zupanija"));
        stupacBolestOsobe.setCellValueFactory(new PropertyValueFactory<Osoba, Bolest>("zarazenBolescu"));
        stupacKontaktiOsobe.setCellValueFactory(new PropertyValueFactory<Osoba, List<Osoba>>("kontaktiraneOsobe"));

        ExecutorService executor = Executors.newCachedThreadPool();
        Future<List<Osoba>> listaOs = executor.submit(new DohvatSvihOsobaNit());
        try {
            observableListaOsoba = FXCollections.observableArrayList(listaOs.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        tablicaOsoba.setItems(observableListaOsoba);
    }

    public Integer izracunStarosti(LocalDate ld) {
        LocalDate now = LocalDate.now();
        Integer brojGodina = now.getYear() - ld.getYear();
        if(now.getMonthValue() < ld.getMonthValue()) {
            return --brojGodina;
        } else if(now.getMonthValue() > ld.getMonthValue()) {
            return brojGodina;
        } else {
            if(now.getDayOfMonth() < ld.getDayOfMonth()) {
                return --brojGodina;
            }
        }
        return brojGodina;
    }

    /**
     * Služi za pretragu osoba prema unesenom imenu i prezimenu.
     */
    public void pretragaOsoba() {
        tablicaOsoba.setItems(FXCollections.observableArrayList(nadjiOdgovarajuceOsobe()));
    }

    /**
     * Služi pronalasku svih osoba odgovarajućeg imena i prezimena, te njihovu unosu u listu.
     * @return vraća listu osoba odgovarajućeg imena i prezimena
     */
    public List<Osoba> nadjiOdgovarajuceOsobe() {
        return observableListaOsoba.stream()
                .filter(o -> o.getIme().toLowerCase().contains(imeZaPretragu.getText().toLowerCase()) &&
                        o.getPrezime().toLowerCase().contains(prezimeZaPretragu.getText().toLowerCase()))
                .collect(Collectors.toList());
    }
}
