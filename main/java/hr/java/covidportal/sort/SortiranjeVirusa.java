package main.java.hr.java.covidportal.sort;

import main.java.hr.java.covidportal.model.Virus;

import java.util.Comparator;

/**
 * Predstavlja klasu SortiranjeVirusa koja implementira sučelje
 * komparator, a prihvaća tip objekata Virus.
 */
public class SortiranjeVirusa implements Comparator<Virus> {

    /**
     * Služi usporedbi dvaju virusa prema njihovim nazivima.
     * Usporedba se vrši u svrhu sortiranja naziva od 'najvećeg'
     * prema 'najmanjem' po abecedi, suprotno od poretka abecede.
     * @param v1 podatak o prvom virusu za usporedbu
     * @param v2 podatak o drugom virusu za usporedbu
     * @return vraća 1 ako je naziv prvog virusa 'manji'
     *      od naziva drugog virusa, -1 ako je suprotno,
     *      a 0 ako su nazivi virusa jednaki
     */
    @Override
    public int compare(Virus v1, Virus v2) {
        if(v1.getNaziv().compareTo(v2.getNaziv()) < 0) {
            return 1;
        }
        else if(v1.getNaziv().compareTo(v2.getNaziv()) > 0) {
            return -1;
        }
        else {
            return 0;
        }
    }
}
