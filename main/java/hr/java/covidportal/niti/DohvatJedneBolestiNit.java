package main.java.hr.java.covidportal.niti;

import main.java.hr.java.covidportal.baza.BazaPodataka;
import main.java.hr.java.covidportal.model.Bolest;

import java.sql.Connection;
import java.util.concurrent.Callable;

public class DohvatJedneBolestiNit implements Callable<Bolest> {

    private Connection veza;
    private Long id;

    public DohvatJedneBolestiNit(Connection veza, Long id) {
        this.veza = veza;
        this.id = id;
    }

    @Override
    public Bolest call() throws Exception {
        return BazaPodataka.dohvatJedneBolesti(veza, id);
    }
}
