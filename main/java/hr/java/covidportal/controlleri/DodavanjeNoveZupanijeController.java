package main.java.hr.java.covidportal.controlleri;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import main.java.hr.java.covidportal.model.Zupanija;
import main.java.hr.java.covidportal.niti.SpremanjeNoveZupanijeNit;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Služi za upravljanje mogućnostima unutar ekrana za unos nove županije. Moguće je unijeti naziv, broj stanovnika, i
 * broj zaraženih stanovnika nove županije.
 */
public class DodavanjeNoveZupanijeController {

    @FXML
    private TextField nazivNoveZupanije;

    @FXML
    private TextField brojStNoveZupanije;

    @FXML
    private TextField brojZarStNoveZupanije;

    /**
     * Služi dodavanju nove županije u bazu podataka. Funkcija prvo provjerava jesu li svi potrebni podatci
     * uneseni, a zatim upisuje te podatke u bazu.
     */
    public void dodajZupaniju() {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Spremanje podataka");
        alert.setHeaderText("Unos županije");

        if(nazivNoveZupanije.getText().equals("") ||
                brojStNoveZupanije.getText().equals("") ||
                brojZarStNoveZupanije.getText().equals(""))
        {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Neuspješno spremanje nove županije! Provjerite unesene podatke.");
            alert.showAndWait();
        }
        else {
            Zupanija zupanija = new Zupanija(
                    100L,
                    nazivNoveZupanije.getText(),
                    Integer.parseInt(brojStNoveZupanije.getText()),
                    Integer.parseInt(brojZarStNoveZupanije.getText())
            );

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(new SpremanjeNoveZupanijeNit(zupanija));

            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setContentText("Nova županija je uspješno spremljena!");
            alert.showAndWait();
        }
    }

}
