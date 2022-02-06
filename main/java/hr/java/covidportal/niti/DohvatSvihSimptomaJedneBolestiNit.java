package main.java.hr.java.covidportal.niti;

import main.java.hr.java.covidportal.baza.BazaPodataka;
import main.java.hr.java.covidportal.model.Simptom;

import java.sql.Connection;
import java.util.List;
import java.util.concurrent.Callable;

public class DohvatSvihSimptomaJedneBolestiNit implements Callable<List<Simptom>> {

    private Connection veza;
    private Long id;

    public DohvatSvihSimptomaJedneBolestiNit(Connection veza, Long id) {
        this.veza = veza;
        this.id = id;
    }

    @Override
    public List<Simptom> call() throws Exception {
        return BazaPodataka.dohvatiSveSimptomeJedneBolesti(veza, id);
    }
}
