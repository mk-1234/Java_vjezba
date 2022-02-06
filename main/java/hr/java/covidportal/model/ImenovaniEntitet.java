package main.java.hr.java.covidportal.model;

import java.io.Serializable;

/**
 * Predstavlja apstraktnu klasu ImenovaniEntitet, koja implementira
 * sučelje Serializable, te je definirana identifikacijskim brojem i nazivom.
 *
 * @author Miroslav Krznar
 */
public abstract class ImenovaniEntitet implements Serializable {

    private Long id;
    private String naziv;

    /**
     * Inicijalizira podatak o identifikacijskom broju i nazivu.
     * @param id podatak o identifikacijskom broju
     * @param naziv podatak o nazivu
     */
    public ImenovaniEntitet(Long id, String naziv) {
        this.id = id;
        this.naziv = naziv;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

}
