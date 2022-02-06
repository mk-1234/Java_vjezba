package main.java.hr.java.covidportal.model;

import main.java.hr.java.covidportal.enumeracija.Enumeracija;

import java.util.Objects;

/**
 * Predstavlja klasu Simptom, koja naslijeđuje apstraktnu klasu ImenovaniEntitet, te
 * je definirana naslijeđenim identifikacijskim brojem i nazivom, te vrijednošću.
 *
 * @author Miroslav Krznar
 */
public class Simptom extends ImenovaniEntitet {

    private Enumeracija vrijednost;

    /**
     * Inicijalizira podatke o identifikacijskom broju, nazivu, i vrijednosti.
     * @param id podatak o identifikacijskom broju
     * @param naziv podatak o nazivu simptoma
     * @param vrijednost podatak o vrijednosti simptoma
     */
    public Simptom(Long id, String naziv, Enumeracija vrijednost) {
        super(id, naziv);
        this.vrijednost = vrijednost;
    }

    public Enumeracija getVrijednost() {
        return vrijednost;
    }

    public void setVrijednost(Enumeracija vrijednost) {
        this.vrijednost = vrijednost;
    }

    /**
     * Provjerava jednakost objekata.
     * @param o objekt s kojim je potrebno usporediti
     * @return vraća true ili false ako se utvrdi da su objekti jednaki, odnosno nisu jednaki
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Simptom)) return false;
        Simptom simptom = (Simptom) o;
        return Objects.equals(getNaziv(), simptom.getNaziv()) &&
                (vrijednost == simptom.vrijednost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vrijednost);
    }

    @Override
    public String toString() {
        return getNaziv() + " (" + vrijednost.getVrijednost() + ")";
    }
}
