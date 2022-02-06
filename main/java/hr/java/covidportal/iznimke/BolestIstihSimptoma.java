package main.java.hr.java.covidportal.iznimke;

/**
 * Predstavlja klasu BolestIstihSimptoma, neoznačenu iznimku,
 * koja proslijeđuje poruku u catch nakon bacanja iznimke. Do bacanja
 * iznimke dolazi kada bolest ima iste simptome kao i već unesena bolest.
 *
 * @author Miroslav Krznar
 */
public class BolestIstihSimptoma extends RuntimeException {

    /**
     * Proslijeđuje poruku primljenu pri bacanju iznimke.
     * @param message podatak o poruci
     */
    public BolestIstihSimptoma(String message) {
        super(message);
    }

    /**
     * Proslijeđuje uzrok primljen pri bacanju iznimke.
     * @param cause podatak o uzroku
     */
    public BolestIstihSimptoma(Throwable cause) {
        super(cause);
    }

    /**
     * Proslijeđuje poruku i uzrok primljene pri bacanju iznimke.
     * @param message podatak o poruci
     * @param cause podatak o uzroku
     */
    public BolestIstihSimptoma(String message, Throwable cause) {
        super(message, cause);
    }

}
