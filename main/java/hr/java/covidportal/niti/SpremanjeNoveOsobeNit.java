package main.java.hr.java.covidportal.niti;

import javafx.scene.control.Alert;
import main.java.hr.java.covidportal.baza.BazaPodataka;
import main.java.hr.java.covidportal.model.Osoba;

import java.io.IOException;
import java.sql.SQLException;

public class SpremanjeNoveOsobeNit implements Runnable {

    private Osoba osoba;

    public SpremanjeNoveOsobeNit(Osoba osoba) {
        this.osoba = osoba;
    }

    @Override
    public void run() {
        try {
            BazaPodataka.spremiNovuOsobu(osoba);
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Spremanje podataka");
            alert.setHeaderText("Unos osobe");
            alert.setContentText("Neuspješno spremanje nove osobe! Dogodila se greška u postupku.");
            alert.showAndWait();
        }
    }
}
