package main.java.hr.java.covidportal.niti;

import javafx.scene.control.Alert;
import main.java.hr.java.covidportal.baza.BazaPodataka;
import main.java.hr.java.covidportal.model.Virus;

import java.io.IOException;
import java.sql.SQLException;

public class SpremanjeNovogVirusaNit implements Runnable {

    private Virus virus;

    public SpremanjeNovogVirusaNit(Virus virus) {
        this.virus = virus;
    }

    @Override
    public void run() {
        try {
            BazaPodataka.spremiNovuBolest(virus);
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Spremanje podataka");
            alert.setHeaderText("Unos virusa");
            alert.setContentText("Neuspješno spremanje novog virusa! Dogodila se greška u postupku.");
            alert.showAndWait();
        }
    }
}
