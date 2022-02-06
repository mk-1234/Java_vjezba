package main.java.hr.java.covidportal.genericsi;

import main.java.hr.java.covidportal.model.Osoba;
import main.java.hr.java.covidportal.model.Virus;

import java.util.List;

/**
 * Predstavlja generičku klasu KlinikaZaInfektivneBolesti, koja je
 * definirana listom virusa i listom osoba zaraženih virusima.
 * @param <T> generički tip, nasljeđuje klasu Virus
 * @param <S> generički tip, nasljeđuje klasu Osoba
 */
public class KlinikaZaInfektivneBolesti<T extends Virus, S extends Osoba> {

    private List<T> listaVirusa;
    private List<S> listaZarazenihOsoba;

    /**
     * Inicijalizira podatke o listi virusa i listi zaraženih osoba.
     * @param listaVirusa podatak o listi virusa
     * @param listaZarazenihOsoba podatak o listi zaraženih osoba
     */
    public KlinikaZaInfektivneBolesti(List<T> listaVirusa, List<S> listaZarazenihOsoba) {
        this.listaVirusa = listaVirusa;
        this.listaZarazenihOsoba = listaZarazenihOsoba;
    }

    /**
     * Služi za dodavanje virusa u listu virusa.
     * @param virus podatak o virusu koji treba dodati u listu
     */
    public void dodajVirus(T virus) {
        listaVirusa.add(virus);
    }

    /**
     * Služi za dodavanje osobe u listu zaraženih osoba.
     * @param osoba podatak o osobi koju treba dodati u listu
     */
    public void dodajZarazenuOsobu(S osoba) {
        listaZarazenihOsoba.add(osoba);
    }

    public List<T> dohvatiListuVirusa() {
        return listaVirusa;
    }

    public List<S> dohvatiListuZarOsoba() {
        return listaZarazenihOsoba;
    }
}
