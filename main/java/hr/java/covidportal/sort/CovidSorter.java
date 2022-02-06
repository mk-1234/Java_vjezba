package main.java.hr.java.covidportal.sort;

import main.java.hr.java.covidportal.model.Zupanija;

import java.util.Comparator;

/**
 * Predstavlja klasu CovidSorter koja implementira sučelje
 * komparator, a prihvaća tip objekata Zupanija.
 */
public class CovidSorter implements Comparator<Zupanija> {

    /**
     * Služi usporedbi dviju županija prema postotku zaraženih.
     * Usporedba se vrši s ciljem sortiranja županija od najvećeg
     * broja zaraženih prema najmanjem.
     * @param z1 podatak o prvoj županiji za usporedbu
     * @param z2 podatak o drugoj županiji za usporedbu
     * @return vraća 1 ako je postotak zaraženih prve županije manji
     *      od postotka zaraženih druge županije, -1 ako je suprotno,
     *      a 0 ako je postotak zaraženih u obje županije jednak
     */
    @Override
    public int compare(Zupanija z1, Zupanija z2) {
        if((z1.getBrojZarazenih().doubleValue() / z1.getBrojStanovnika().doubleValue()) <
                (z2.getBrojZarazenih().doubleValue()) / z2.getBrojStanovnika().doubleValue())
        {
            return 1;
        }
        else if((z1.getBrojZarazenih().doubleValue() / z1.getBrojStanovnika().doubleValue()) >
                (z2.getBrojZarazenih().doubleValue()) / z2.getBrojStanovnika().doubleValue())
        {
            return -1;
        }
        else {
            return 0;
        }
    }

}
