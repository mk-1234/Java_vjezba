package main.java.hr.java.covidportal.niti;

import main.java.hr.java.covidportal.baza.BazaPodataka;
import main.java.hr.java.covidportal.model.Zupanija;

import java.sql.Connection;
import java.util.concurrent.Callable;

public class DohvatJedneZupanijeNit implements Callable<Zupanija> {

    private Connection veza;
    private Long id;

    public DohvatJedneZupanijeNit(Connection veza, Long id) {
        this.veza = veza;
        this.id = id;
    }

    @Override
    public Zupanija call() throws Exception {
        return BazaPodataka.dohvatJedneZupanije(veza, id);
    }
}
