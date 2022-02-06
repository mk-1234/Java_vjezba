package main.java.hr.java.covidportal.model;

/**
 * Predstavlja sučelje Zarazno, koje je određeno metodom prelazakZarazeNaOsobu.
 *
 * @author Miroslav Krznar
 */
public interface Zarazno {

    /**
     * Omogućuje klasama koje implementiraju sučelje korištenje metodom, koja
     * prima kao parametar objekt klase Osoba. Objektu osobe se postavlja
     * bolest klase u kojoj je metoda pozvana.
     * @param osoba parametar metode, podatak o objektu klase Osoba
     */
    void prelazakZarazeNaOsobu(Osoba osoba);

}
