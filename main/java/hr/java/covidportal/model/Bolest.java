package main.java.hr.java.covidportal.model;

import java.util.List;
import java.util.Objects;

/**
 * Predstavlja klasu Bolest, koja nasljeđuje apstraktnu klasu ImenovaniEntitet, te je definirana
 * naslijeđenim identifikacijskim brojem i nazivom, te listom simptoma.
 *
 * @author Miroslav Krznar
 */
public class Bolest extends ImenovaniEntitet {

    private List<Simptom> simptomi;

    /**
     * Inicijalizira podatke o identifikacijskom broju, nazivu, i simptomima.
     * @param id podatak o identifikacijskom broju
     * @param naziv podatak o nazivu bolesti
     * @param simptomi lista simptoma
     */
    public Bolest(Long id, String naziv, List<Simptom> simptomi) {
        super(id, naziv);
        this.simptomi = simptomi;
    }

    public List<Simptom> getSimptomi() {
        return simptomi;
    }

    public void setSimptomi(List<Simptom> simptomi) {
        this.simptomi = simptomi;
    }

    /**
     * Provjerava jednakost objekata.
     * @param o objekt s kojim je potrebno usporediti
     * @return vraća true ili false ako se utvrdi da su objekti jednaki, odnosno nisu jednaki
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bolest)) return false;
        Bolest bolest = (Bolest) o;
        return Objects.equals(getNaziv(), bolest.getNaziv()) &&
                Objects.equals(simptomi, bolest.simptomi);
    }

    @Override
    public int hashCode() {
        return Objects.hash(simptomi);
    }

    @Override
    public String toString() {
        return getNaziv();
    }
}
