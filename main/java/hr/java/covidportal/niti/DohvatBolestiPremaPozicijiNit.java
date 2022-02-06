package main.java.hr.java.covidportal.niti;

import main.java.hr.java.covidportal.baza.BazaPodataka;
import main.java.hr.java.covidportal.model.Bolest;

import java.util.concurrent.Callable;

public class DohvatBolestiPremaPozicijiNit implements Callable<Bolest> {

    private Long pozicija;

    public DohvatBolestiPremaPozicijiNit(Long pozicija) {
        this.pozicija = pozicija;
    }

    @Override
    public Bolest call() throws Exception {
        return BazaPodataka.dohvatBolestiPremaPoziciji(pozicija);
    }
}
