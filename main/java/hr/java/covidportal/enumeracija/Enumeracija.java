package main.java.hr.java.covidportal.enumeracija;

/**
 * Predstavlja klasu Enumeracija, koja sadrži konstante
 * koje se upotrebljavaju pri unosu vrijednosti simptoma.
 * Definirana je svojom vrijednošću.
 */
public enum Enumeracija {
    Produktivni("Produktivni"),
    Intenzivno("Intenzivno"),
    Visoka("Visoka"),
    Jaka("Jaka");


    private String vrijednost;

    /**
     * Inicijalizira podatak o vrijednosti.
     * @param vrijednost podatak o vrijednosti konstante
     */
    private Enumeracija(String vrijednost) {
        this.vrijednost = vrijednost;
    }

    public String getVrijednost() {
        return vrijednost;
    }
}
