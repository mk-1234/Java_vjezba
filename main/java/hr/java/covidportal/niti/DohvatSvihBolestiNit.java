package main.java.hr.java.covidportal.niti;

import main.java.hr.java.covidportal.baza.BazaPodataka;
import main.java.hr.java.covidportal.model.Bolest;

import java.util.List;
import java.util.concurrent.Callable;

public class DohvatSvihBolestiNit implements Callable<List<Bolest>> {

    private Integer vrsta;

    public DohvatSvihBolestiNit(Integer vrsta) {
        this.vrsta = vrsta;
    }

    @Override
    public List<Bolest> call() throws Exception {
        return BazaPodataka.dohvatiSveBolesti(vrsta);
    }
}
