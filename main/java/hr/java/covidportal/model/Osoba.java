package main.java.hr.java.covidportal.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Predstavlja klasu Osoba, koja je definirana identifikacijskim brojem, imenom,
 * prezimenom, datumom rođenja, županijom u kojoj osoba živi, bolešću osobe, te listom kontakata.
 *
 * @author Miroslav Krznar
 */
public class Osoba {

    private Long id;
    private String ime;
    private String prezime;
    private LocalDate datumRodjenja;
    private Zupanija zupanija;
    private Bolest zarazenBolescu;
    private List<Osoba> kontaktiraneOsobe;

    /**
     * Inicijalizira objekt Osoba, preko builder pattern-a postavlja
     * atribute na zadane vrijednosti, te provjerava je li osoba zaražena virusom,
     * u kom slučaju preko objekta virusa pozivom metode prelazakZarazeNaOsobu,
     * postavlja virus na sve osobe iz liste kontakata.
     */
    public static class Builder {

        private Long id;
        private String ime;
        private String prezime;
        private LocalDate datumRodjenja;
        private Zupanija zupanija;
        private Bolest zarazenBolescu;
        private List<Osoba> kontaktiraneOsobe;

        public Builder brojaId(Long id) {
            this.id = id;
            return this;
        }

        public Builder sIme(String ime) {
            this.ime = ime;
            return this;
        }

        public Builder sPrezime(String prezime) {
            this.prezime = prezime;
            return this;
        }

        public Builder ovajDatum(LocalDate datumRodjenja) {
            this.datumRodjenja = datumRodjenja;
            return this;
        }

        public Builder uZupanija(Zupanija zupanija) {
            this.zupanija = zupanija;
            return this;
        }

        public Builder imaZarazenBolescu(Bolest zarazenBolescu) {
            this.zarazenBolescu = zarazenBolescu;
            return this;
        }

        public Builder uzKontaktiraneOsobe(List<Osoba> kontaktiraneOsobe) {
            this.kontaktiraneOsobe = kontaktiraneOsobe;
            return this;
        }

        public Osoba build() {
            Osoba osoba = new Osoba();
            osoba.id = this.id;
            osoba.ime = this.ime;
            osoba.prezime = this.prezime;
            osoba.datumRodjenja = this.datumRodjenja;
            osoba.zupanija = this.zupanija;
            osoba.zarazenBolescu = this.zarazenBolescu;
            osoba.kontaktiraneOsobe = this.kontaktiraneOsobe;
            if((zarazenBolescu instanceof Virus virus) && (kontaktiraneOsobe != null)) {
                for(Osoba osoba1 : kontaktiraneOsobe) {
                    virus.prelazakZarazeNaOsobu(osoba1);
                }
            }

            return osoba;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public LocalDate getDatumRodjenja() {
        return datumRodjenja;
    }

    public void setDatumRodjenja(LocalDate datumRodjenja) {
        this.datumRodjenja = datumRodjenja;
    }

    public Zupanija getZupanija() {
        return zupanija;
    }

    public void setZupanija(Zupanija zupanija) {
        this.zupanija = zupanija;
    }

    public Bolest getZarazenBolescu() {
        return zarazenBolescu;
    }

    public void setZarazenBolescu(Bolest zarazenBolescu) {
        this.zarazenBolescu = zarazenBolescu;
    }

    public List<Osoba> getKontaktiraneOsobe() {
        return kontaktiraneOsobe;
    }

    public void setKontaktiraneOsobe(List<Osoba> kontaktiraneOsobe) {
        this.kontaktiraneOsobe = kontaktiraneOsobe;
    }

    /**
     * Provjerava jednakost objekata.
     * @param o objekt s kojim je potrebno usporediti
     * @return vraća true ili false ako se utvrdi da su objekti jednaki, odnosno nisu jednaki
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Osoba)) return false;
        Osoba osoba = (Osoba) o;
        return Objects.equals(id, osoba.id) &&
                Objects.equals(ime, osoba.ime) &&
                Objects.equals(prezime, osoba.prezime) &&
                Objects.equals(datumRodjenja, osoba.datumRodjenja) &&
                Objects.equals(zupanija, osoba.zupanija) &&
                Objects.equals(zarazenBolescu, osoba.zarazenBolescu) &&
                Objects.equals(kontaktiraneOsobe, osoba.kontaktiraneOsobe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ime, prezime, datumRodjenja, zupanija, zarazenBolescu, kontaktiraneOsobe);
    }

    @Override
    public String toString() {
        return ime + " " + prezime;
    }
}
