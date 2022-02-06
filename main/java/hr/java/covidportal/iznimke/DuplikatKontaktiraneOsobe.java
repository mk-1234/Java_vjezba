package main.java.hr.java.covidportal.iznimke;

/**
 * Predstavlja klasu DuplikatKontaktiraneOsobe, označenu iznimku,
 * koja proslijeđuje poruku u catch nakon bacanja iznimke. Do bacanja
 * iznimke dolazi kada je kao kontakt osobe odabrana već ranije
 * odabrana osoba.
 *
 * @author Miroslav Krznar
 */
public class DuplikatKontaktiraneOsobe extends Exception {

    /**
     * Proslijeđuje poruku primljenu pri bacanju iznimke.
     * @param message podatak o poruci
     */
    public DuplikatKontaktiraneOsobe(String message) {
        super(message);
    }

    /**
     * Proslijeđuje uzrok primljen pri bacanju iznimke.
     * @param cause podatak o uzroku
     */
    public DuplikatKontaktiraneOsobe(Throwable cause) {
        super(cause);
    }

    /**
     * Proslijeđuje poruku i uzrok primljene pri bacanju iznimke.
     * @param message podatak o poruci
     * @param cause podatak o uzroku
     */
    public DuplikatKontaktiraneOsobe(String message, Throwable cause) {
        super(message, cause);
    }

}
