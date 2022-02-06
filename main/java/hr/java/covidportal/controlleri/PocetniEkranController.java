package main.java.hr.java.covidportal.controlleri;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.util.Duration;
import main.java.hr.java.covidportal.baza.BazaPodataka;
import main.java.hr.java.covidportal.main.Main;
import main.java.hr.java.covidportal.model.ImenovaniEntitet;
import main.java.hr.java.covidportal.model.Zupanija;
import main.java.hr.java.covidportal.niti.DohvatSvihZupanijaNit;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Služi za upravljanje mogućnostima unutar početnog ekrana. Moguće je odabrati pretrage po određenim grupama i
 * dodavanje novih elemenata u grupe.
 */
public class PocetniEkranController implements Initializable {

    @FXML
    private Label brojVirusaLabel;
    @FXML
    private Label nazivVirusaLabel;

    /**
     * Služi prikazu ekrana unutar kojeg je moguća pretraga županija prema njihovom nazivu.
     * @throws IOException baca se ako dođe do pogreške u postavljanju elemenata za prikaz novog ekrana
     */
    public void prikaziEkranZaPretraguZupanija() throws IOException {
        Parent pretragaZupanijaFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("pretragaZupanija.fxml"));
        Scene pretragaZupanijaScene = new Scene(pretragaZupanijaFrame, 600, 400);
        Main.getMainStage().setScene(pretragaZupanijaScene);
    }

    /**
     * Služi prikazu ekrana unutar kojeg je moguć unos nove županije.
     * @throws IOException baca se ako dođe do pogreške u postavljanju elemenata za prikaz novog ekrana
     */
    public void prikaziEkranZaNoviZapisZupanije() throws IOException {
        Parent noviZapisZupanijeFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("dodavanjeNoveZupanije.fxml"));
        Scene noviZapisZupanijeScene = new Scene(noviZapisZupanijeFrame, 600, 400);
        Main.getMainStage().setScene(noviZapisZupanijeScene);
    }

    /**
     * Služi prikazu ekrana unutar kojeg je moguća pretraga simptoma prema njihovom nazivu.
     * @throws IOException baca se ako dođe do pogreške u postavljanju elemenata za prikaz novog ekrana
     */
    public void prikaziEkranZaPretraguSimptoma() throws IOException {
        Parent pretragaSimptomaFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("pretragaSimptoma.fxml"));
        Scene pretragaSimptomaScene = new Scene(pretragaSimptomaFrame, 600, 400);
        Main.getMainStage().setScene(pretragaSimptomaScene);
    }

    /**
     * Služi prikazu ekrana unutar kojeg je moguć unos novog simptoma.
     * @throws IOException baca se ako dođe do pogreške u postavljanju elemenata za prikaz novog ekrana
     */
    public void prikaziEkranZaNoviZapisSimptoma() throws IOException {
        Parent noviZapisSimptomaFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("dodavanjeNovogSimptoma.fxml"));
        Scene noviZapisSimptomaScene = new Scene(noviZapisSimptomaFrame, 600, 400);
        Main.getMainStage().setScene(noviZapisSimptomaScene);
    }

    /**
     * Služi prikazu ekrana unutar kojeg je moguća pretraga bolesti prema njihovom nazivu.
     * @throws IOException baca se ako dođe do pogreške u postavljanju elemenata za prikaz novog ekrana
     */
    public void prikaziEkranZaPretraguBolesti() throws IOException {
        Parent pretragaBolestiFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("pretragaBolesti.fxml"));
        Scene pretragaBolestiScene = new Scene(pretragaBolestiFrame, 600, 400);
        Main.getMainStage().setScene(pretragaBolestiScene);
    }

    /**
     * Služi prikazu ekrana unutar kojeg je moguć unos nove bolesti.
     * @throws IOException baca se ako dođe do pogreške u postavljanju elemenata za prikaz novog ekrana
     */
    public void prikaziEkranZaNoviZapisBolesti() throws IOException {
        Parent noviZapisBolestiFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("dodavanjeNoveBolesti.fxml"));
        Scene noviZapisBolestiScene = new Scene(noviZapisBolestiFrame, 600, 400);
        Main.getMainStage().setScene(noviZapisBolestiScene);
    }

    /**
     * Služi prikazu ekrana unutar kojeg je moguća pretraga virusa prema njihovom nazivu.
     * @throws IOException baca se ako dođe do pogreške u postavljanju elemenata za prikaz novog ekrana
     */
    public void prikaziEkranZaPretraguVirusa() throws IOException {
        Parent pretragaVirusaFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("pretragaVirusa.fxml"));
        Scene pretragaVirusaScene = new Scene(pretragaVirusaFrame, 600, 400);
        Main.getMainStage().setScene(pretragaVirusaScene);
    }

    /**
     * Služi prikazu ekrana unutar kojeg je moguć unos novog virusa.
     * @throws IOException baca se ako dođe do pogreške u postavljanju elemenata za prikaz novog ekrana
     */
    public void prikaziEkranZaNoviZapisVirusa() throws IOException {
        Parent noviZapisVirusaFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("dodavanjeNovogVirusa.fxml"));
        Scene noviZapisVirusaScene = new Scene(noviZapisVirusaFrame, 600, 400);
        Main.getMainStage().setScene(noviZapisVirusaScene);
    }

    /**
     * Služi prikazu ekrana unutar kojeg je moguća pretraga osoba prema njihovom imenu.
     * @throws IOException baca se ako dođe do pogreške u postavljanju elemenata za prikaz novog ekrana
     */
    public void prikaziEkranZaPretraguOsoba() throws IOException {
        Parent pretragaOsobaFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("pretragaOsoba.fxml"));
        Scene pretragaOsobaScene = new Scene(pretragaOsobaFrame, 600, 400);
        Main.getMainStage().setScene(pretragaOsobaScene);
    }

    /**
     * Služi prikazu ekrana unutar kojeg je moguć unos nove osobe.
     * @throws IOException baca se ako dođe do pogreške u postavljanju elemenata za prikaz novog ekrana
     */
    public void prikaziEkranZaNoviZapisOsobe() throws IOException {
        Parent noviZapisOsobeFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("dodavanjeNoveOsobe.fxml"));
        Scene noviZapisOsobeScene = new Scene(noviZapisOsobeFrame, 600, 400);
        Main.getMainStage().setScene(noviZapisOsobeScene);
    }

    /**
     * Služi inicijalizaciji početnog ekrana.
     * @param url link
     * @param resourceBundle resursi
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        // ZADATCI -----------------------------------------------------------------------

        /*Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            try {
                //brojVirusaLabel.setText(BazaPodataka.ispisBrojaVirusa() + ", vrijeme: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                nazivVirusaLabel.setText(BazaPodataka.dohvatZadnjegVirusa() + ", vrijeme: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            } catch (IOException | SQLException ex) {
                ex.printStackTrace();
            }
        }), new KeyFrame(Duration.seconds(2)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();*/

        try {
            System.out.println(BazaPodataka.ispisBrojaVirusa() + ", vrijeme: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            //System.out.println(BazaPodataka.dohvatZadnjegVirusa() + ", vrijeme: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }



        /*try {
            System.out.println(BazaPodataka.dohvatZadnjegVirusa());
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }*/


        // Zakomentiran je dohvat naziva zato što zapne u čekanju, nisam usppio naći rješenje:(

        /*Timeline timeline2 = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            try {
                nazivVirusaLabel.setText(BazaPodataka.dohvatZadnjegVirusa() + ", vrijeme: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            } catch (IOException | SQLException ex) {
                ex.printStackTrace();
            }
        }), new KeyFrame(Duration.seconds(5)));
        timeline2.setCycleCount(Animation.INDEFINITE);
        timeline2.play();*/

    }

}
