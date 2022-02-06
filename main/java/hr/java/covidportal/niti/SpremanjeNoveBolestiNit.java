package main.java.hr.java.covidportal.niti;

import javafx.scene.control.Alert;
import main.java.hr.java.covidportal.baza.BazaPodataka;
import main.java.hr.java.covidportal.model.Bolest;

import java.io.IOException;
import java.sql.SQLException;

public class SpremanjeNoveBolestiNit implements Runnable {

    private Bolest bolest;

    public SpremanjeNoveBolestiNit(Bolest bolest) {
        this.bolest = bolest;
    }

    @Override
    public void run() {
        try {
            BazaPodataka.spremiNovuBolest(bolest);
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Spremanje podataka");
            alert.setHeaderText("Unos bolesti");
            alert.setContentText("Neuspješno spremanje nove bolesti! Dogodila se greška u postupku.");
            alert.showAndWait();
        }
    }
}
