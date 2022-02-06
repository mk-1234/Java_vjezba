package main.java.hr.java.covidportal.controlleri;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.hr.java.covidportal.enumeracija.Enumeracija;
import main.java.hr.java.covidportal.model.Simptom;
import main.java.hr.java.covidportal.niti.DohvatSvihSimptomaNit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Služi za upravljanje mogućnostima unutar ekrana za pretragu simptoma. Moguće je unijeti naziv po kojem se treba
 * vršiti pretraga. Svi simptomi odgovarajućeg naziva će biti ispisani u tablici.
 */
public class PretragaSimptomaController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(PretragaSimptomaController.class);

    @FXML
    private TextField nazivZaPretragu;

    @FXML
    private TableView<Simptom> tablicaSimptoma;

    @FXML
    private TableColumn<Simptom, String> stupacNazivSimptoma;

    @FXML
    private TableColumn<Simptom, Enumeracija> stupacVrijednostSimptoma;

    private ObservableList<Simptom> observableListaSimptoma;

    /**
     * Služi inicijalizaciji ekrana za pretragu po simptomima. Na početku su u tablici navedeni svi simptomi koji
     * se mogu pretraživati.
     * @param url link
     * @param resourceBundle resursi
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        stupacNazivSimptoma.setCellValueFactory(new PropertyValueFactory<Simptom, String>("naziv"));
        stupacVrijednostSimptoma.setCellValueFactory(new PropertyValueFactory<Simptom, Enumeracija>("vrijednost"));

        ExecutorService executor = Executors.newCachedThreadPool();
        Future<List<Simptom>> listaSim = executor.submit(new DohvatSvihSimptomaNit());
        try {
            observableListaSimptoma = FXCollections.observableArrayList(listaSim.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        tablicaSimptoma.setItems(observableListaSimptoma);
    }

    /**
     * Služi za pretragu simptoma prema unesenom nazivu.
     */
    public void pretragaSimptoma() {
        tablicaSimptoma.setItems(FXCollections.observableArrayList(nadjiOdgovarajuceSimptome()));
    }

    /**
     * Služi pronalasku svih simptoma odgovarajućeg naziva i njihovu unosu u listu.
     * @return vraća listu simptoma odgovarajućeg naziva
     */
    public List<Simptom> nadjiOdgovarajuceSimptome() {
        return observableListaSimptoma.stream()
                .filter(s -> s.getNaziv().toLowerCase().contains(nazivZaPretragu.getText().toLowerCase()))
                .collect(Collectors.toList());
    }
}
