package main.java.hr.java.covidportal.controlleri;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import main.java.hr.java.covidportal.main.Main;
import main.java.hr.java.covidportal.model.ImenovaniEntitet;
import main.java.hr.java.covidportal.model.Zupanija;
import main.java.hr.java.covidportal.niti.DohvatSvihZupanijaNit;
import main.java.hr.java.covidportal.niti.NajviseZarazenihNit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Služi za upravljanje mogućnostima unutar ekrana za pretragu županija. Moguće je unijeti naziv po kojem se treba
 * vršiti pretraga. Sve županije odgovarajućeg naziva će biti ispisane u tablici.
 */
public class PretragaZupanijaController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(PretragaZupanijaController.class);

    @FXML
    private TextField nazivZaPretragu;

    @FXML
    private TableView<Zupanija> tablicaZupanija;

    @FXML
    private TableColumn<Zupanija, String> stupacNazivZupanije;

    @FXML
    private TableColumn<Zupanija, Integer> stupacBrojStanovnika;

    @FXML
    private TableColumn<Zupanija, Integer> stupacBrojZarazenih;

    private ObservableList<Zupanija> observableListaZupanija;

    /**
     * Služi inicijalizaciji ekrana za pretragu po županijama. Na početku su u tablici navedene sve županije koje
     * se mogu pretraživati.
     * @param url link
     * @param resourceBundle resursi
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        stupacNazivZupanije.setCellValueFactory(new PropertyValueFactory<Zupanija, String>("naziv"));
        stupacBrojStanovnika.setCellValueFactory(new PropertyValueFactory<Zupanija, Integer>("brojStanovnika"));
        stupacBrojZarazenih.setCellValueFactory(new PropertyValueFactory<Zupanija, Integer>("brojZarazenih"));

        ExecutorService executor = Executors.newCachedThreadPool();
        Future<List<Zupanija>> listaZup = executor.submit(new DohvatSvihZupanijaNit());
        try {
            observableListaZupanija = FXCollections.observableArrayList(listaZup.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        tablicaZupanija.setItems(observableListaZupanija);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new NajviseZarazenihNit());

        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            try {
                List<Zupanija> zupanije = Executors.newSingleThreadExecutor().submit(new DohvatSvihZupanijaNit()).get();
                Double postotak = zupanije.stream()
                        .mapToDouble(z -> ((double) z.getBrojZarazenih() / z.getBrojStanovnika()) * 100)
                        .max()
                        .orElseThrow();

                String naziv = zupanije.stream()
                        .filter(z -> ((double) z.getBrojZarazenih() / z.getBrojStanovnika()) * 100 == postotak)
                        .map(ImenovaniEntitet::getNaziv)
                        .collect(Collectors.joining());

                DecimalFormat df = new DecimalFormat("###.##");
                Main.getMainStage().setTitle(naziv + " (" + df.format(postotak) + "%)");
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        }), new KeyFrame(Duration.seconds(10)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    /**
     * Služi za pretragu županija prema unesenom nazivu.
     */
    public void pretragaZupanija() {
        tablicaZupanija.setItems(FXCollections.observableArrayList(nadjiOdgovarajuceZupanije()));
    }

    /**
     * Služi pronalasku svih županija odgovarajućeg naziva i njihovu unosu u listu.
     * @return vraća listu županija odgovarajućeg naziva
     */
    public List<Zupanija> nadjiOdgovarajuceZupanije() {
        return observableListaZupanija.stream()
                .filter(z -> z.getNaziv().toLowerCase().contains(nazivZaPretragu.getText().toLowerCase()))
                .collect(Collectors.toList());
    }
}
