package main.java.hr.java.covidportal.controlleri;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import main.java.hr.java.covidportal.enumeracija.Enumeracija;
import main.java.hr.java.covidportal.model.Simptom;
import main.java.hr.java.covidportal.niti.SpremanjeNovogSimptomaNit;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Služi za upravljanje mogućnostima unutar ekrana za unos novog simptoma. Moguće je unijeti naziv, i vrijednost
 * novog simptoma.
 */
public class DodavanjeNovogSimptomaController implements Initializable {

    @FXML
    private TextField nazivNovogSimptoma;

    @FXML
    private ComboBox<String> vrijednostNovogSimptoma;

    private Enumeracija odabranaVrijednost;

    /**
     * Služi inicijalizaciji ekrana za unos novog simptoma. Ekran sadrži sva potrebna polja kako bi se unijeli
     * odgovarajući podatci.
     * @param url link
     * @param resourceBundle resursi
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        vrijednostNovogSimptoma.getItems().removeAll(vrijednostNovogSimptoma.getItems());
        vrijednostNovogSimptoma.getItems()
                .addAll(Enumeracija.Produktivni.getVrijednost(),
                        Enumeracija.Intenzivno.getVrijednost(),
                        Enumeracija.Visoka.getVrijednost(),
                        Enumeracija.Jaka.getVrijednost()
                );
        vrijednostNovogSimptoma.setValue("Odaberite vrijednost");

        vrijednostNovogSimptoma.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) -> {
            if(t1.equals(0)) {
                odabranaVrijednost = Enumeracija.Produktivni;
            } else if(t1.equals(1)) {
                odabranaVrijednost = Enumeracija.Intenzivno;
            } else if(t1.equals(2)) {
                odabranaVrijednost = Enumeracija.Visoka;
            } else if(t1.equals(3)) {
                odabranaVrijednost = Enumeracija.Jaka;
            }
        });

    }

    /**
     * Služi dodavanju novog simptoma u bazu podataka. Funkcija prvo provjerava jesu li svi potrebni podatci uneseni,
     * a zatim upisuje te podatke u bazu.
     */
    public void dodajSimptom() {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Spremanje podataka");
        alert.setHeaderText("Unos simptoma");

        if(nazivNovogSimptoma.getText().equals("") || odabranaVrijednost == null) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Neuspješno spremanje novog simptoma! Provjerite unesene podatke.");
            alert.showAndWait();
        }
        else {
            Simptom simptom = new Simptom(100L, nazivNovogSimptoma.getText(), odabranaVrijednost);

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(new SpremanjeNovogSimptomaNit(simptom));

            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setContentText("Novi simptom je uspješno spremljen!");
            alert.showAndWait();
        }
    }

}
