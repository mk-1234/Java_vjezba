package main.java.hr.java.covidportal.controlleri;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.hr.java.covidportal.model.Bolest;
import main.java.hr.java.covidportal.model.Simptom;
import main.java.hr.java.covidportal.niti.DohvatSimptomaPremaPozicijiNit;
import main.java.hr.java.covidportal.niti.DohvatSvihSimptomaNit;
import main.java.hr.java.covidportal.niti.SpremanjeNoveBolestiNit;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Služi za upravljanje mogućnostima unutar ekrana za unos nove bolesti. Moguće je unijeti naziv, i simptome
 * nove bolesti.
 */
public class DodavanjeNoveBolestiController implements Initializable {

    @FXML
    private TextField nazivNoveBolesti;

    @FXML
    private ComboBox<Simptom> simptomiNoveBolesti;

    @FXML
    private TableView<Simptom> tablicaOdabranihSimptoma;

    @FXML
    private TableColumn<Simptom, String> stupacOdabranihSimptoma;

    @FXML
    private Label dodatniTekst;

    /**
     * Služi inicijalizaciji ekrana za unos nove bolesti. Ekran sadrži sva potrebna polja kako bi se unijeli
     * odgovarajući podatci.
     * @param url link
     * @param resourceBundle resursi
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<List<Simptom>> listaSim = executor.submit(new DohvatSvihSimptomaNit());
        try {
            simptomiNoveBolesti.setItems(FXCollections.observableArrayList(listaSim.get()));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        stupacOdabranihSimptoma.setCellValueFactory(new PropertyValueFactory<Simptom, String>("naziv"));

        simptomiNoveBolesti.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) -> {
            Simptom s = null;

            ExecutorService executor2 = Executors.newSingleThreadExecutor();
            Future<Simptom> sim = executor2.submit(new DohvatSimptomaPremaPozicijiNit(t1.longValue()));
            try {
                s = sim.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }


            if(!provjeraSimptoma(tablicaOdabranihSimptoma, s)) {
                tablicaOdabranihSimptoma.getItems().add(s);
                dodatniTekst.setText("Odaberite novi simptom ili kliknite na 'Spremi' ako ste zadovoljni unosom!\n" +
                        "(Klik na redak za brisanje unosa)");
            } else {
                System.out.println("Simptom je već unesen!");
            }
        });

        tablicaOdabranihSimptoma.setRowFactory(t -> {
            TableRow<Simptom> red = new TableRow<>();
            red.setOnMouseClicked(event -> {
                if(event.getClickCount() == 1 && (!red.isEmpty())) {
                    tablicaOdabranihSimptoma.getItems().remove(red.getItem());
                }
            });
            return red;
        });
    }

    /**
     * Služi dodavanju nove bolesti u bazu podataka. Funkcija prvo provjerava jesu li svi potrebni podatci uneseni,
     * a zatim unosi te podatke u bazu.
     */
    public void dodajBolest() {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Spremanje podataka");
        alert.setHeaderText("Unos bolesti");

        if(nazivNoveBolesti.getText().equals("") || tablicaOdabranihSimptoma.getItems().isEmpty()) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Neuspješno spremanje nove bolesti! Provjerite unesene podatke.");
            alert.showAndWait();
        }
        else {
            Bolest bolest = new Bolest(100L, nazivNoveBolesti.getText(), tablicaOdabranihSimptoma.getItems());

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(new SpremanjeNoveBolestiNit(bolest));

            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setContentText("Nova bolest je uspješno spremljena!");
            alert.showAndWait();
        }
    }

    /**
     * Služi za provjeru nalazi li se odabrani simptom već u tablici simptoma. U slučaju da je, funkcija vraća TRUE.
     * @param tablica tablica simptoma
     * @param s podatak o odabranom simptomu
     * @return vraća TRUE/FALSE ovisno o tome postoji li već isti simptom u tablici
     */
    public Boolean provjeraSimptoma(TableView<Simptom> tablica, Simptom s) {
        for(Simptom simptom : tablica.getItems()) {
            if(s.equals(simptom)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

}
