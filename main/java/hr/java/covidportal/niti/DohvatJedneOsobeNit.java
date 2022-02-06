package main.java.hr.java.covidportal.niti;

import main.java.hr.java.covidportal.baza.BazaPodataka;
import main.java.hr.java.covidportal.model.Osoba;

import java.sql.Connection;
import java.util.concurrent.Callable;

public class DohvatJedneOsobeNit implements Callable<Osoba> {

    private Connection veza;
    private Long id;

    public DohvatJedneOsobeNit(Connection veza, Long id) {
        this.veza = veza;
        this.id = id;
    }

    @Override
    public Osoba call() throws Exception {
        return BazaPodataka.dohvatJedneOsobe(veza, id);
    }
}
