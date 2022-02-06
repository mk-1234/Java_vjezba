package main.java.hr.java.covidportal.niti;

import main.java.hr.java.covidportal.baza.BazaPodataka;
import main.java.hr.java.covidportal.model.Osoba;

import java.util.List;
import java.util.concurrent.Callable;

public class DohvatSvihOsobaNit implements Callable<List<Osoba>> {

    @Override
    public List<Osoba> call() throws Exception {
        return BazaPodataka.dohvatiSveOsobe();
    }
}
