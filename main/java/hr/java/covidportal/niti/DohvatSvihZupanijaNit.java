package main.java.hr.java.covidportal.niti;

import main.java.hr.java.covidportal.baza.BazaPodataka;
import main.java.hr.java.covidportal.model.Zupanija;

import java.util.List;
import java.util.concurrent.Callable;

public class DohvatSvihZupanijaNit implements Callable<List<Zupanija>> {

    @Override
    public List<Zupanija> call() throws Exception {
        return BazaPodataka.dohvatiSveZupanije();
    }
}
