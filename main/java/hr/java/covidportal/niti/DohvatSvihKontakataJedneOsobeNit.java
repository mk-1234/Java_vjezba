package main.java.hr.java.covidportal.niti;

import main.java.hr.java.covidportal.baza.BazaPodataka;
import main.java.hr.java.covidportal.model.Osoba;

import java.sql.Connection;
import java.util.List;
import java.util.concurrent.Callable;

public class DohvatSvihKontakataJedneOsobeNit implements Callable<List<Osoba>> {

    private Connection veza;
    private Long id;

    public DohvatSvihKontakataJedneOsobeNit(Connection veza, Long id) {
        this.veza = veza;
        this.id = id;
    }

    @Override
    public List<Osoba> call() throws Exception {
        return BazaPodataka.dohvatiSveKontakteJedneOsobe(veza, id);
    }
}
