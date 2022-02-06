package main.java.hr.java.covidportal.model;

import java.util.Objects;

/**
 * Predstavlja klasu Županija, koja naslijeđuje apstraktnu klasu ImenovaniEntitet, te
 * je definirana naslijeđenim identifikacijskim brojem i nazivom, te brojem stanovnika
 * i brojem zaraženih.
 *
 * @author Miroslav Krznar
 */
public class Zupanija extends ImenovaniEntitet {

    private Integer brojStanovnika;
    private Integer brojZarazenih;

    /**
     * Inicijalizira podatke o identifikacijskom broju, nazivu,
     * broju stanovnika, i broju zaraženih.
     * @param id podatak o identifikacijskom broju
     * @param naziv podatak o nazivu županije
     * @param brojStanovnika podatak o broju stanovnika
     * @param brojZarazenih podatak o broju zaraženih
     */
    public Zupanija(Long id, String naziv, Integer brojStanovnika, Integer brojZarazenih) {
        super(id, naziv);
        this.brojStanovnika = brojStanovnika;
        this.brojZarazenih = brojZarazenih;
    }

    public Integer getBrojStanovnika() {
        return brojStanovnika;
    }

    public void setBrojStanovnika(Integer brojStanovnika) {
        this.brojStanovnika = brojStanovnika;
    }

    public Integer getBrojZarazenih() {
        return brojZarazenih;
    }

    public void setBrojZarazenih(Integer brojZarazenih) {
        this.brojZarazenih = brojZarazenih;
    }

    /**
     * Provjerava jednakost objekata.
     * @param o objekt s kojim je potrebno usporediti
     * @return vraća true ili false ako se utvrdi da su objekti jednaki, odnosno nisu jednaki
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Zupanija)) return false;
        Zupanija zupanija = (Zupanija) o;
        return Objects.equals(getNaziv(), zupanija.getNaziv()) &&
                Objects.equals(brojStanovnika, zupanija.brojStanovnika) &&
                Objects.equals(brojZarazenih, zupanija.brojZarazenih);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brojStanovnika, brojZarazenih);
    }

    @Override
    public String toString() {
        return getNaziv();
    }
}
