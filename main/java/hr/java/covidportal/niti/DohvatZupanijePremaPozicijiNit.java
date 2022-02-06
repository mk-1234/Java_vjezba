package main.java.hr.java.covidportal.niti;

import main.java.hr.java.covidportal.baza.BazaPodataka;
import main.java.hr.java.covidportal.model.Zupanija;

import java.util.concurrent.Callable;

public class DohvatZupanijePremaPozicijiNit implements Callable<Zupanija> {

    private Long pozicija;

    public DohvatZupanijePremaPozicijiNit(Long pozicija) {
        this.pozicija = pozicija;
    }

    @Override
    public Zupanija call() throws Exception {
        return BazaPodataka.dohvatZupanijePremaPoziciji(pozicija);
    }
}
