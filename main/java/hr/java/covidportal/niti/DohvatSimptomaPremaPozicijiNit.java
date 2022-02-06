package main.java.hr.java.covidportal.niti;

import main.java.hr.java.covidportal.baza.BazaPodataka;
import main.java.hr.java.covidportal.model.Simptom;

import java.util.concurrent.Callable;

public class DohvatSimptomaPremaPozicijiNit implements Callable<Simptom> {

    private Long pozicija;

    public DohvatSimptomaPremaPozicijiNit(Long pozicija) {
        this.pozicija = pozicija;
    }

    @Override
    public Simptom call() throws Exception {
        return BazaPodataka.dohvatSimptomaPremaPoziciji(pozicija);
    }
}
