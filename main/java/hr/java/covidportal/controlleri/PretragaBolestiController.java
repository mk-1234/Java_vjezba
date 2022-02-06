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
 * Služi za upravljanje mogućnostima unutar ekrana za pretragu bolesti. Moguće je unijeti naziv po kojem se treba
 * vršiti pretraga. Sve bolesti odgovarajućeg naziva će biti ispisane u tablici.
 */
public class PretragaBolestiController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(PretragaBolestiController.class);

    @FXML
    private TextField nazivZaPretragu;

    @FXML
    private TableView<Bolest> tablicaBolesti;

    @FXML
    private TableColumn<Bolest, String> stupacNazivBolesti;

    @FXML
    private TableColumn<Bolest, List<Simptom>> stupacSimptomi;

    private ObservableList<Bolest> observableListaBolesti;

    /**
     * Služi inicijalizaciji ekrana za pretragu po bolestima. Na početku su u tablici navedene sve bolesti koje
     * se mogu pretraživati.
     * @param url link
     * @param resourceBundle resursi
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        stupacNazivBolesti.setCellValueFactory(new PropertyValueFactory<Bolest, String>("naziv"));
        stupacSimptomi.setCellValueFactory(new PropertyValueFactory<Bolest, List<Simptom>>("simptomi"));

        ExecutorService executor = Executors.newCachedThreadPool();
        Future<List<Bolest>> listaBol = executor.submit(new DohvatSvihBolestiNit(1));
        try {
            observableListaBolesti = FXCollections.observableArrayList(listaBol.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        tablicaBolesti.setItems(observableListaBolesti);
    }

    /**
     * Služi za pretragu bolesti prema unesenom nazivu.
     */
    public void pretragaBolesti() {
        tablicaBolesti.setItems(FXCollections.observableArrayList(nadjiOdgovarajuceBolesti()));
    }

    /**
     * Služi pronalasku svih bolesti odgovarajućeg naziva i njihovu unosu u listu.
     * @return vraća listu bolesti odgovarajućeg naziva
     */
    public List<Bolest> nadjiOdgovarajuceBolesti() {
        return observableListaBolesti.stream()
                .filter(b -> b.getNaziv().toLowerCase().contains(nazivZaPretragu.getText().toLowerCase()))
                .collect(Collectors.toList());
    }
}
