package main.java.hr.java.covidportal.sort;

import main.java.hr.java.covidportal.model.Simptom;

import java.util.Comparator;

/**
 * Predstavlja klasu UsporediSimptome koja implementira sučelje
 * komparator, a prihvaća tip objekata Simptom.
 */
public class UsporediSimptome implements Comparator<Simptom> {

    /**
     * Služi usporedbi dvaju simptoma prema njihovim nazivima.
     * Usporedba se vrši u svrhu sortiranja naziva od 'najvećeg'
     * prema 'najmanjem' po abecedi, suprotno od poretka abecede.
     * @param s1 podatak o prvom simptomu za usporedbu
     * @param s2 podatak o drugom simptomu za usporedbu
     * @return vraća 1 ako je naziv prvog simptoma 'manji'
     *      od naziva drugog simptoma, -1 ako je suprotno,
     *      a 0 ako su nazivi simptoma jednaki
     */
    @Override
    public int compare(Simptom s1, Simptom s2) {
        if(s1.getNaziv().compareTo(s2.getNaziv()) < 0) {
            return 1;
        }
        else if(s1.getNaziv().compareTo(s2.getNaziv()) > 0) {
            return -1;
        }
        else return 0;
    }

}
