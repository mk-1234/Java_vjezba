package main.java.hr.java.covidportal.controlleri;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.hr.java.covidportal.model.Bolest;
import main.java.hr.java.covidportal.model.Simptom;
import main.java.hr.java.covidportal.niti.DohvatSvihBolestiNit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Služi za upravljanje mogućnostima unutar ekrana za pretragu virusa. Moguće je unijeti naziv po kojem se treba
 * vršiti pretraga. Svi virusi odgovarajućeg naziva će biti ispisani u tablici.
 */
public class PretragaVirusaController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(PretragaVirusaController.class);

    @FXML
    private TextField nazivZaPretragu;

    @FXML
    private TableView<Bolest> tablicaVirusa;

    @FXML
    private TableColumn<Bolest, String> stupacNazivVirusa;

    @FXML
    private TableColumn<Bolest, List<Simptom>> stupacSimptomi;

    private ObservableList<Bolest> observableListaVirusa;

    /**
     * Služi inicijalizaciji ekrana za pretragu po virusima. Na početku su u tablici navedeni svi virusi koji
     * se mogu pretraživati.
     * @param url link
     * @param resourceBundle resursi
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        stupacNazivVirusa.setCellValueFactory(new PropertyValueFactory<Bolest, String>("naziv"));
        stupacSimptomi.setCellValueFactory(new PropertyValueFactory<Bolest, List<Simptom>>("simptomi"));

        ExecutorService executor = Executors.newCachedThreadPool();
        Future<List<Bolest>> listaVir = executor.submit(new DohvatSvihBolestiNit(2));
        try {
            observableListaVirusa = FXCollections.observableArrayList(listaVir.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        tablicaVirusa.setItems(observableListaVirusa);

    }

    /**
     * Služi za pretragu virusa prema unesenom nazivu.
     */
    public void pretragaVirusa() {
        tablicaVirusa.setItems(FXCollections.observableArrayList(nadjiOdgovarajuceViruse()));
    }

    /**
     * Služi pronalasku svih virusa odgovarajućeg naziva i njihovu unosu u listu.
     * @return vraća listu virusa odgovarajućeg naziva
     */
    public List<Bolest> nadjiOdgovarajuceViruse() {
        return observableListaVirusa.stream()
                .filter(v -> v.getNaziv().toLowerCase().contains(nazivZaPretragu.getText().toLowerCase()))
                .collect(Collectors.toList());
    }
}
