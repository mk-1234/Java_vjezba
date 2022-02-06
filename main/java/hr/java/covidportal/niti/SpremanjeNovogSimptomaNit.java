package main.java.hr.java.covidportal.niti;

import javafx.scene.control.Alert;
import main.java.hr.java.covidportal.baza.BazaPodataka;
import main.java.hr.java.covidportal.model.Simptom;

import java.io.IOException;
import java.sql.SQLException;

public class SpremanjeNovogSimptomaNit implements Runnable {

    private Simptom simptom;

    public SpremanjeNovogSimptomaNit(Simptom simptom) {
        this.simptom = simptom;
    }

    @Override
    public void run() {
        try {
            BazaPodataka.spremiNoviSimptom(simptom);
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Spremanje podataka");
            alert.setHeaderText("Unos simptoma");
            alert.setContentText("Neuspješno spremanje novog simptoma! Dogodila se greška u postupku.");
            alert.showAndWait();
        }
    }
}
