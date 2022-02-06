package main.java.hr.java.covidportal.model;

import java.util.List;

/**
 * Predstavlja klasu Virus, koja naslijeđuje klasu Bolest, te implementira
 * sučelje Zarazno. Definirana je naslijeđenim identifikacijskim brojem,
 * nazivom, i listom simptoma, te implementacijom metode prelazakZarazeNaOsobu.
 *
 * @author Miroslav Krznar
 */

public class Virus extends Bolest implements Zarazno {

    /**
     * Inicijalizira podatke o identifikacijskom broju, nazivu virusa, i simptomima.
     * @param id podatak o identifikacijskom broju
     * @param naziv podatak o nazivu virusa
     * @param simptomi lista simptomima
     */
    public Virus(Long id, String naziv, List<Simptom> simptomi) {
        super(id, naziv, simptomi);
    }

    @Override
    public void prelazakZarazeNaOsobu(Osoba osoba) {
        osoba.setZarazenBolescu(this);
    }



}
