package main.java.hr.java.covidportal.niti;

import main.java.hr.java.covidportal.baza.BazaPodataka;
import main.java.hr.java.covidportal.model.Simptom;

import java.util.List;
import java.util.concurrent.Callable;

public class DohvatSvihSimptomaNit implements Callable<List<Simptom>> {

    @Override
    public List<Simptom> call() throws Exception {
        return BazaPodataka.dohvatiSveSimptome();
    }
}
