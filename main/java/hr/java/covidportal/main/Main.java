package main.java.hr.java.covidportal.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Služi za postavljanje glavnog stage-a i scene-a, povezivanje s početnim ekranom, i pokretanje aplikacije.
 *
 * @author Miroslav Krznar
 */
public class Main extends Application {

    private static Stage mainStage;

    /**
     * Povezuje s početnim ekranom, postavlja scene, i prikazuje prozor aplikacije.
     * @param primaryStage glavni prozor aplikacije
     * @throws Exception iznimka koja se baca u slučaju da je došlo do greške u postavljanju i otvaranju aplikacije.
     */
    @Override
    public void start(Stage primaryStage) throws Exception{

        mainStage = primaryStage;

        //Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("izbornik.fxml"));
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("noviEkranZaIspisOVirusima.fxml"));

        primaryStage.setTitle("Pocetni ekran");
        primaryStage.setScene(new Scene(root, 500, 300));
        primaryStage.show();
    }

    /**
     * Služi dohvatu glavnog stage-a kako bi se u istom prozoru odvijao ostatak programa.
     * @return vraća početni stage
     */
    public static Stage getMainStage() {
        return mainStage;
    }

    /**
     * Služi za pokretanje aplikacije preko metode launch. Aplikacija se koristi za pretragu podataka pojedinih
     * kategorija prema nazivu.
     * @param args podatak o argumentima komandne linije
     */
    public static void main(String[] args) {
        launch(args);
    }
}
