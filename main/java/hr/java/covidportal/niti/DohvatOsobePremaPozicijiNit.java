package main.java.hr.java.covidportal.niti;

import main.java.hr.java.covidportal.baza.BazaPodataka;
import main.java.hr.java.covidportal.model.Osoba;

import java.util.concurrent.Callable;

public class DohvatOsobePremaPozicijiNit implements Callable<Osoba> {

    private Long pozicija;

    public DohvatOsobePremaPozicijiNit(Long pozicija) {
        this.pozicija = pozicija;
    }

    @Override
    public Osoba call() throws Exception {
        return BazaPodataka.dohvatOsobePremaPoziciji(pozicija);
    }
}
