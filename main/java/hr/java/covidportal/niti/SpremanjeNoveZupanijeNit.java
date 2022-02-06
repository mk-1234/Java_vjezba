package main.java.hr.java.covidportal.niti;

import javafx.scene.control.Alert;
import main.java.hr.java.covidportal.baza.BazaPodataka;
import main.java.hr.java.covidportal.model.Zupanija;

import java.io.IOException;
import java.sql.SQLException;

public class SpremanjeNoveZupanijeNit implements Runnable {

    private Zupanija zupanija;

    public SpremanjeNoveZupanijeNit(Zupanija zupanija) {
        this.zupanija = zupanija;
    }

    @Override
    public void run() {
        try {
            BazaPodataka.spremiNovuZupaniju(zupanija);
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Spremanje podataka");
            alert.setHeaderText("Unos županije");
            alert.setContentText("Neuspješno spremanje nove županije! Dogodila se greška u postupku.");
            alert.showAndWait();
        }
    }
}
