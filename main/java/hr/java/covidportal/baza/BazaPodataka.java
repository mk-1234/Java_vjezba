package main.java.hr.java.covidportal.baza;

import main.java.hr.java.covidportal.enumeracija.Enumeracija;
import main.java.hr.java.covidportal.model.*;
import main.java.hr.java.covidportal.niti.*;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

/**
 * Služi za komunikaciju s bazom podataka. Omogućuje povezivanje, zatvaranje veze, te dohvat jedne, dohvat svih, i
 * spremanje novih županija, simptoma, bolesti, i osoba.
 */
public class BazaPodataka {

    public static Boolean aktivnaVezaSBazomPodataka = Boolean.FALSE;

    private static final String DATABASE_FILE = "src\\main\\resources\\database.properties";

    /**
     * Otvara vezu s bazom podataka, koristeći podatke iz properties datoteke.
     * @return vraća vezu
     * @throws SQLException baca iznimku u slučaju problema u otvaranju veze
     * @throws IOException baca iznimku u slučaju problema u učitavanju properties datoteke
     */
    public static synchronized Connection otvaranjeVeze() throws SQLException, IOException {

        while(aktivnaVezaSBazomPodataka) {
            try {
                System.out.println("Čekanje na oslobađanje pristupa bazi podataka.");
                BazaPodataka.class.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        aktivnaVezaSBazomPodataka = Boolean.TRUE;
        //System.out.println("Slobodan pristup bazi podataka.");

        Properties svojstva = new Properties();
        svojstva.load(new FileReader(DATABASE_FILE));

        String urlBazePodataka = svojstva.getProperty("bazaPodatakaUrl");
        String korisnickoIme = svojstva.getProperty("korisnickoIme");
        String lozinka = svojstva.getProperty("lozinka");

        return DriverManager.getConnection(urlBazePodataka, korisnickoIme, lozinka);
    }

    /**
     * Zatvara vezu s bazom podataka.
     * @param veza veza koju je potrebno zatvoriti
     * @throws SQLException baca iznimku u slučaju problema u zatvaranju veze
     */
    public static synchronized void zatvaranjeVeze(Connection veza) throws SQLException {
        veza.close();

        aktivnaVezaSBazomPodataka = Boolean.FALSE;
        BazaPodataka.class.notifyAll();
        //System.out.println("Oslobođen pristup bazi podataka.");
    }

    /**
     * Služi za dohvat svih županija iz baze podataka. Otvara vezu, šalje SQL upit, te unosi dobivene podatke u listu
     * županija koju potom vraća.
     * @return vraća listu svih županija
     * @throws SQLException baca iznimku u slučaju problema u otvaranju/zatvaranju veze, te stvaranju
     * i provođenju upita.
     * @throws IOException baca iznimku u slučaju problema u učitavanju properties datoteke pri otvaranju veze
     */
    public static List<Zupanija> dohvatiSveZupanije() throws SQLException, IOException {

        List<Zupanija> listaZupanija = new ArrayList<>();
        Connection veza = otvaranjeVeze();

        //System.out.println("Dohvat svih županija.");

        Statement stmt = veza.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM ZUPANIJA");

        while(rs.next()) {
            Long id = rs.getLong("ID");
            String naziv = rs.getString("NAZIV");
            Integer brojStanovnika = rs.getInt("BROJ_STANOVNIKA");
            Integer brojZarazenih = rs.getInt("BROJ_ZARAZENIH_STANOVNIKA");
            listaZupanija.add(new Zupanija(id, naziv, brojStanovnika, brojZarazenih));
        }

        //System.out.println("Kraj dohvata svih županija - " + listaZupanija);

        rs.close();
        stmt.close();
        zatvaranjeVeze(veza);

        return listaZupanija;
    }

    /**
     * Služi za dohvat jedne županije, prema id-u. Otvara vezu, šalje SQL upit, te dobivene podatke vraća.
     * @param idZup id županije koju treba vratiti
     * @return vraća zatraženu zupaniju
     * @throws SQLException baca iznimku u slučaju problema u otvaranju/zatvaranju veze, te stvaranju
     * i provođenju upita.
     * @throws IOException baca iznimku u slučaju problema u učitavanju properties datoteke pri otvaranju veze
     */
    public static Zupanija dohvatJedneZupanije(Connection veza, Long idZup) throws SQLException {
        //System.out.println("Dohvat jedne županije.");

        PreparedStatement prepStmt = veza.prepareStatement("SELECT * FROM ZUPANIJA WHERE ID = ?");
        prepStmt.setLong(1, idZup);
        ResultSet rs = prepStmt.executeQuery();

        rs.next();
        Long id = rs.getLong("ID");
        String naziv = rs.getString("NAZIV");
        Integer brojStanovnika = rs.getInt("BROJ_STANOVNIKA");
        Integer brojZarazenih = rs.getInt("BROJ_ZARAZENIH_STANOVNIKA");

        //System.out.println("Kraj dohvata jedne županije - " + naziv);

        rs.close();
        prepStmt.close();

        return new Zupanija(id, naziv, brojStanovnika, brojZarazenih);
    }

    /**
     * Služi pronalasku županije prema poziciji te županije u ComboBox-u. Ne može se dobiti županija prema odabranom
     * id-u, jer je moguće nepodudaranje id-eva u slučaju brisanja nekog ranijeg unosa iz baze podataka.
     * @param pozicija pozicija odabrane županije u ComboBox-u
     * @return vraća zatraženu županiju
     * @throws SQLException baca iznimku u slučaju problema u otvaranju/zatvaranju veze, te stvaranju
     * i provođenju upita.
     * @throws IOException baca iznimku u slučaju problema u učitavanju properties datoteke pri otvaranju veze
     */
    public static Zupanija dohvatZupanijePremaPoziciji(Long pozicija) throws IOException, SQLException {
        Connection veza = otvaranjeVeze();

        //System.out.println("Dohvat županije prema poziciji.");

        Statement stmt = veza.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM ZUPANIJA");

        Long id = 0L;
        String naziv = "";
        Integer brojStanovnika = 0;
        Integer brojZarazenih = 0;

        Integer brojac = 0;

        while(rs.next()) {
            if(brojac == pozicija.intValue()) {
                id = rs.getLong("ID");
                naziv = rs.getString("NAZIV");
                brojStanovnika = rs.getInt("BROJ_STANOVNIKA");
                brojZarazenih = rs.getInt("BROJ_ZARAZENIH_STANOVNIKA");
            }
            brojac++;
        }

        //System.out.println("Kraj dohvata županije prema poziciji - " + naziv);

        rs.close();
        stmt.close();
        zatvaranjeVeze(veza);

        return new Zupanija(id, naziv, brojStanovnika, brojZarazenih);
    }

    /**
     * Služi za spremanje nove županije u bazu podataka. Otvara vezu, šalje SQL upit kojim provjerava broj trenutnih
     * unosa, i taj broj povećava za jedan te ga koristi pri resetiranju "ID" stupca, kako bi id-evi u bazi podataka
     * bili uzastopni. Nakon toga priprema novi upit, a provođenjem unosi podatke u bazu.
     * @param zupanija županija koju je potrebno unijeti u bazu podataka
     * @throws SQLException baca iznimku u slučaju problema u otvaranju/zatvaranju veze, te stvaranju
     * i provođenju upita.
     * @throws IOException baca iznimku u slučaju problema u učitavanju properties datoteke pri otvaranju veze
     */
    public static void spremiNovuZupaniju(Zupanija zupanija) throws SQLException, IOException {
        Connection veza = otvaranjeVeze();

        //System.out.println("Spremanje nove županije.");

        PreparedStatement prepStmt = veza.prepareStatement(
                "INSERT INTO ZUPANIJA(NAZIV, BROJ_STANOVNIKA, BROJ_ZARAZENIH_STANOVNIKA) VALUES(?, ?, ?);");
        prepStmt.setString(1, zupanija.getNaziv());
        prepStmt.setInt(2, zupanija.getBrojStanovnika());
        prepStmt.setInt(3, zupanija.getBrojZarazenih());
        prepStmt.executeUpdate();
        prepStmt.close();

        //System.out.println("Kraj spremanja nove županije - " + zupanija.getNaziv());

        zatvaranjeVeze(veza);
    }

    /**
     * Služi za dohvat svih simptoma iz baze podataka. Otvara vezu, šalje SQL upit, te unosi dobivene podatke u listu
     * simptoma koju potom vraća.
     * @return vraća listu svih simptoma
     * @throws SQLException baca iznimku u slučaju problema u otvaranju/zatvaranju veze, te stvaranju
     * i provođenju upita.
     * @throws IOException baca iznimku u slučaju problema u učitavanju properties datoteke pri otvaranju veze
     */
    public static List<Simptom> dohvatiSveSimptome() throws SQLException, IOException {

        List<Simptom> listaSimptoma = new ArrayList<>();
        Connection veza = otvaranjeVeze();

        //System.out.println("Dohvat svih simptoma.");

        Statement stmt = veza.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM SIMPTOM");

        while(rs.next()) {
            Long id = rs.getLong("ID");
            String naziv = rs.getString("NAZIV");
            String vrijednost = rs.getString("VRIJEDNOST");
            listaSimptoma.add(new Simptom(id, naziv, Enumeracija.valueOf(vrijednost)));
        }

        //System.out.println("Kraj dohvata svih simptoma - " + listaSimptoma);

        rs.close();
        stmt.close();
        zatvaranjeVeze(veza);

        return listaSimptoma;
    }

    /**
     * Služi za dohvat jednog simptoma, prema id-u. Otvara vezu, šalje SQL upit, te dobivene podatke vraća.
     * @param idSim id simptoma kojeg treba vratiti
     * @return vraća zatraženi simptom
     * @throws SQLException baca iznimku u slučaju problema u otvaranju/zatvaranju veze, te stvaranju
     * i provođenju upita.
     * @throws IOException baca iznimku u slučaju problema u učitavanju properties datoteke pri otvaranju veze
     */
    public static Simptom dohvatJednogSimptoma(Connection veza, Long idSim) throws SQLException {
        //System.out.println("Dohvat jednog simptoma.");

        PreparedStatement prepStmt = veza.prepareStatement("SELECT * FROM SIMPTOM WHERE ID = ?");
        prepStmt.setLong(1, idSim);
        ResultSet rs = prepStmt.executeQuery();

        rs.next();
        Long id = rs.getLong("ID");
        String naziv = rs.getString("NAZIV");
        Enumeracija vrijednost = Enumeracija.valueOf(rs.getString("VRIJEDNOST"));

        //System.out.println("Kraj dohvata jednog simptoma - " + naziv);

        rs.close();
        prepStmt.close();

        return new Simptom(id, naziv, vrijednost);
    }

    /**
     * Služi pronalasku simptoma prema poziciji tog simptoma u ComboBox-u. Ne može se dobiti simptom prema odabranom
     * id-u, jer je moguće nepodudaranje id-eva u slučaju brisanja nekog ranijeg unosa iz baze podataka.
     * @param pozicija pozicija odabranog simptoma u ComboBox-u
     * @return vraća zatraženi simptom
     * @throws SQLException baca iznimku u slučaju problema u otvaranju/zatvaranju veze, te stvaranju
     * i provođenju upita.
     * @throws IOException baca iznimku u slučaju problema u učitavanju properties datoteke pri otvaranju veze
     */
    public static Simptom dohvatSimptomaPremaPoziciji(Long pozicija) throws IOException, SQLException {
        Connection veza = otvaranjeVeze();

        //System.out.println("Dohvat simptoma prema poziciji.");

        Statement stmt = veza.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM SIMPTOM");

        Long id = 0L;
        String naziv = "";
        Enumeracija vrijednost = null;

        Integer brojac = 0;

        while(rs.next()) {
            if(brojac == pozicija.intValue()) {
                id = rs.getLong("ID");
                naziv = rs.getString("NAZIV");
                vrijednost = Enumeracija.valueOf(rs.getString("VRIJEDNOST"));
            }
            brojac++;
        }

        //System.out.println("Kraj dohvata simptoma prema poziciji - " + naziv);

        rs.close();
        stmt.close();
        zatvaranjeVeze(veza);

        return new Simptom(id, naziv, vrijednost);
    }

    /**
     * Služi za spremanje novog simptoma u bazu podataka. Otvara vezu, šalje SQL upit kojim provjerava broj trenutnih
     * unosa, i taj broj povećava za jedan te ga koristi pri resetiranju "ID" stupca, kako bi id-evi u bazi podataka
     * bili uzastopni. Nakon toga priprema novi upit, a provođenjem unosi podatke u bazu.
     * @param simptom simptom kojeg je potrebno unijeti u bazu podataka
     * @throws SQLException baca iznimku u slučaju problema u otvaranju/zatvaranju veze, te stvaranju
     * i provođenju upita.
     * @throws IOException baca iznimku u slučaju problema u učitavanju properties datoteke pri otvaranju veze
     */
    public static void spremiNoviSimptom(Simptom simptom) throws SQLException, IOException {
        Connection veza = otvaranjeVeze();

        //System.out.println("Spremanje novog simptoma.");

        PreparedStatement prepStmt = veza.prepareStatement("INSERT INTO SIMPTOM(NAZIV, VRIJEDNOST) VALUES(?, ?);");
        prepStmt.setString(1, simptom.getNaziv());
        prepStmt.setString(2, simptom.getVrijednost().getVrijednost());
        prepStmt.executeUpdate();
        prepStmt.close();

        //System.out.println("Kraj spremanja novog simptoma - " + simptom.getNaziv());

        zatvaranjeVeze(veza);
    }

    /**
     * Služi za dohvat svih bolesti, samo bolesti, ili samo virusa iz baze podataka. Otvara vezu, šalje SQL upit,
     * te unosi dobivene podatke u listu bolesti koju potom vraća.
     * @param vrsta označava što treba dohvatiti - sve bolesti(0), samo bolesti(1), ili samo viruse(2)
     * @return vraća listu zatraženih bolesti, prema ulaznom parametru
     * @throws SQLException baca iznimku u slučaju problema u otvaranju/zatvaranju veze, te stvaranju
     * i provođenju upita.
     * @throws IOException baca iznimku u slučaju problema u učitavanju properties datoteke pri otvaranju veze
     */
    public static List<Bolest> dohvatiSveBolesti(int vrsta) throws SQLException, IOException {

        List<Bolest> listaBolesti = new ArrayList<>();
        Connection veza = otvaranjeVeze();

        //switch (vrsta) {
        //    case 1 -> System.out.println("Dohvat samo bolesti.");
        //    case 2 -> System.out.println("Dohvat samo virusa.");
        //    default -> System.out.println("Dohvat svih bolesti.");
        //}

        Statement stmt = veza.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM BOLEST");

        while(rs.next()) {
            Long id = rs.getLong("ID");
            String naziv = rs.getString("NAZIV");
            Boolean virus = rs.getBoolean("VIRUS");

            List<Simptom> listaSimptoma = new ArrayList<>();

            try {
                listaSimptoma = Executors.newSingleThreadExecutor().submit(new DohvatSvihSimptomaJedneBolestiNit(veza, id)).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            if(vrsta == 1 && !virus) {
                listaBolesti.add(new Bolest(id, naziv, listaSimptoma));
            }
            if(vrsta == 2 && virus) {
                listaBolesti.add(new Virus(id, naziv, listaSimptoma));
            }
            if(vrsta == 0) {
                listaBolesti.add(new Bolest(id, naziv, listaSimptoma));
            }
        }

        //switch (vrsta) {
        //    case 1 -> System.out.println("Kraj dohvata samo bolesti - " + listaBolesti);
        //    case 2 -> System.out.println("Kraj dohvata samo virusa - " + listaBolesti);
        //    default -> System.out.println("Kraj dohvata svih bolesti - " + listaBolesti);
        //}

        rs.close();
        stmt.close();
        zatvaranjeVeze(veza);

        return listaBolesti;
    }

    /**
     * Služi za dohvat svih simptoma neke bolesti iz baze podataka. Otvara vezu, šalje SQL upit, te unosi dobivene
     * podatke u listu simptoma koju potom vraća.
     * @param idBol id bolesti za koju treba naći simptome
     * @return vraća listu zatraženih simptoma
     * @throws SQLException baca iznimku u slučaju problema u otvaranju/zatvaranju veze, te stvaranju
     * i provođenju upita.
     * @throws IOException baca iznimku u slučaju problema u učitavanju properties datoteke pri otvaranju veze
     */
    public static List<Simptom> dohvatiSveSimptomeJedneBolesti(Connection veza, Long idBol) throws SQLException {
        List<Simptom> listaSimptoma = new ArrayList<>();

        //System.out.println("Dohvat svih simptoma jedne bolesti.");

        PreparedStatement prepStmt = veza.prepareStatement("SELECT * FROM BOLEST_SIMPTOM WHERE BOLEST_ID = ?");
        prepStmt.setLong(1, idBol);
        ResultSet rs = prepStmt.executeQuery();

        while(rs.next()) {
            Long idBolesti = rs.getLong("BOLEST_ID");
            Long idSimptoma = rs.getLong("SIMPTOM_ID");
            listaSimptoma.add(dohvatJednogSimptoma(veza, idSimptoma));
        }

        //System.out.println("Kraj dohvata svih simptoma jedne bolesti - " + listaSimptoma);

        rs.close();
        prepStmt.close();

        return listaSimptoma;
    }

    /**
     * Služi za dohvat jedne bolesti, prema id-u. Otvara vezu, šalje SQL upit, te dobivene podatke vraća.
     * @param idBol id bolesti koju treba vratiti
     * @return vraća zatraženu bolest ili virus
     * @throws SQLException baca iznimku u slučaju problema u otvaranju/zatvaranju veze, te stvaranju
     * i provođenju upita.
     * @throws IOException baca iznimku u slučaju problema u učitavanju properties datoteke pri otvaranju veze
     */
    public static Bolest dohvatJedneBolesti(Connection veza, Long idBol) throws SQLException {
        //System.out.println("Dohvat jedne bolesti.");

        PreparedStatement prepStmt = veza.prepareStatement("SELECT * FROM BOLEST WHERE ID = ?");
        prepStmt.setLong(1, idBol);
        ResultSet rs = prepStmt.executeQuery();

        rs.next();
        Long id = rs.getLong("ID");
        String naziv = rs.getString("NAZIV");
        Boolean jeVirus = rs.getBoolean("VIRUS");

        List<Simptom> listaSimptoma = null;
        try {
            listaSimptoma = Executors.newSingleThreadExecutor().submit(new DohvatSvihSimptomaJedneBolestiNit(veza, idBol)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        //System.out.println("Kraj dohvata jedne bolesti - " + naziv);

        rs.close();
        prepStmt.close();

        if(jeVirus) {
            return new Virus(id, naziv, listaSimptoma);
        }
        return new Bolest(id, naziv, listaSimptoma);
    }

    /**
     * Služi pronalasku bolesti prema poziciji te bolesti u ComboBox-u. Ne može se dobiti bolest prema odabranom id-u,
     * jer je moguće nepodudaranje id-eva u slučaju brisanja nekog ranijeg unosa iz baze podataka.
     * @param pozicija pozicija odabrane bolesti u ComboBox-u
     * @return vraća zatraženu bolest
     * @throws SQLException baca iznimku u slučaju problema u otvaranju/zatvaranju veze, te stvaranju
     * i provođenju upita.
     * @throws IOException baca iznimku u slučaju problema u učitavanju properties datoteke pri otvaranju veze
     */
    public static Bolest dohvatBolestiPremaPoziciji(Long pozicija) throws IOException, SQLException {
        Connection veza = otvaranjeVeze();

        //System.out.println("Dohvat bolesti prema poziciji.");

        Statement stmt = veza.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM BOLEST");

        Long id = 0L;
        String naziv = "";
        List<Simptom> listaSimptoma = new ArrayList<>();

        Integer brojac = 0;

        while(rs.next()) {
            if(brojac == pozicija.intValue()) {
                id = rs.getLong("ID");
                naziv = rs.getString("NAZIV");

                try {
                    listaSimptoma = Executors.newSingleThreadExecutor().submit(new DohvatSvihSimptomaJedneBolestiNit(veza, id)).get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            brojac++;
        }

        //System.out.println("Kraj dohvata bolesti prema poziciji - " + naziv);

        rs.close();
        stmt.close();
        zatvaranjeVeze(veza);

        return new Bolest(id, naziv, listaSimptoma);
    }

    /**
     * Služi za spremanje nove bolesti u bazu podataka. Otvara vezu, i šalje SQL upit kojim unosi novu bolest u tablicu.
     * Potom dohvaća broj id-a zadnje bolesti. Nakon toga priprema novi upit, za unos u tablicu simptoma bolesti, a
     * provođenjem unosi podatke u bazu.
     * @param bolest bolest koju je potrebno unijeti u bazu podataka
     * @throws SQLException baca iznimku u slučaju problema u otvaranju/zatvaranju veze, te stvaranju
     * i provođenju upita.
     * @throws IOException baca iznimku u slučaju problema u učitavanju properties datoteke pri otvaranju veze
     */
    public static void spremiNovuBolest(Bolest bolest) throws SQLException, IOException {
        Connection veza = otvaranjeVeze();

        //System.out.println("Spremanje nove bolesti.");

        PreparedStatement prepStmt = veza.prepareStatement("INSERT INTO BOLEST(NAZIV, VIRUS) VALUES(?, ?);");
        prepStmt.setString(1, bolest.getNaziv());
        prepStmt.setBoolean(2, (bolest instanceof Virus) ? Boolean.TRUE : Boolean.FALSE );
        prepStmt.executeUpdate();
        prepStmt.close();

        Statement stmt = veza.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM BOLEST");
        Long nextIdNumber = 0L;
        while(rs.next()) {
            nextIdNumber = rs.getLong("ID");
        }
        rs.close();
        stmt.close();

        PreparedStatement prepStmt2 = veza.prepareStatement(
                "INSERT INTO BOLEST_SIMPTOM(BOLEST_ID, SIMPTOM_ID) VALUES (?, ?);");
        for(int i = 0; i < bolest.getSimptomi().size(); i++) {
            prepStmt2.setLong(1, nextIdNumber);
            prepStmt2.setLong(2, bolest.getSimptomi().get(i).getId());
            prepStmt2.executeUpdate();
        }
        prepStmt2.close();

        //System.out.println("Kraj spremanja nove bolesti - " + bolest.getNaziv());

        zatvaranjeVeze(veza);
    }

    /**
     * Služi za dohvat svih osoba iz baze podataka. Otvara vezu, šalje SQL upit, te unosi dobivene podatke u listu
     * osoba koju potom vraća.
     * @return vraća listu svih osoba
     * @throws SQLException baca iznimku u slučaju problema u otvaranju/zatvaranju veze, te stvaranju
     * i provođenju upita.
     * @throws IOException baca iznimku u slučaju problema u učitavanju properties datoteke pri otvaranju veze
     */
    public static List<Osoba> dohvatiSveOsobe() throws SQLException, IOException {

        List<Osoba> listaOsoba = new ArrayList<>();
        Connection veza = otvaranjeVeze();

        //System.out.println("Dohvat svih osoba.");

        Statement stmt = veza.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM OSOBA");

        while(rs.next()) {
            Long id = rs.getLong("ID");
            String ime = rs.getString("IME");
            String prezime = rs.getString("PREZIME");
            Date date = (Date) rs.getDate("DATUM_RODJENJA");
            Instant instant = Instant.ofEpochMilli(date.getTime());
            LocalDate localDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
            Long idZup = rs.getLong("ZUPANIJA_ID");
            Long idBol = rs.getLong("BOLEST_ID");

            try {
                Zupanija z = Executors.newSingleThreadExecutor().submit(new DohvatJedneZupanijeNit(veza, idZup)).get();
                Bolest b = Executors.newSingleThreadExecutor().submit(new DohvatJedneBolestiNit(veza, idBol)).get();
                List<Osoba> k = Executors.newSingleThreadExecutor().submit(new DohvatSvihKontakataJedneOsobeNit(veza, id)).get();
                listaOsoba.add(new Osoba.Builder()
                        .brojaId(id)
                        .sIme(ime)
                        .sPrezime(prezime)
                        .ovajDatum(localDate)
                        .uZupanija(z)
                        .imaZarazenBolescu(b)
                        .uzKontaktiraneOsobe(k)
                        .build()
                );
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        //System.out.println("Kraj dohvata svih osoba - " + listaOsoba);

        rs.close();
        stmt.close();
        zatvaranjeVeze(veza);

        return listaOsoba;
    }

    /**
     * Služi za dohvat svih kontakata neke osobe iz baze podataka. Otvara vezu, šalje SQL upit, te unosi dobivene
     * podatke u listu kontakata koju potom vraća.
     * @param idOs id osobe čije kontakte treba naći
     * @return vraća listu zatraženih kontakata
     * @throws SQLException baca iznimku u slučaju problema u otvaranju/zatvaranju veze, te stvaranju
     * i provođenju upita.
     * @throws IOException baca iznimku u slučaju problema u učitavanju properties datoteke pri otvaranju veze
     */
    public static List<Osoba> dohvatiSveKontakteJedneOsobe(Connection veza, Long idOs) throws SQLException {
        List<Osoba> listaKontakata = new ArrayList<>();

        //System.out.println("Dohvat svih kontakata jedne osobe.");

        PreparedStatement prepStmt = veza.prepareStatement("SELECT * FROM KONTAKTIRANE_OSOBE WHERE OSOBA_ID = ?");
        prepStmt.setLong(1, idOs);
        ResultSet rs = prepStmt.executeQuery();

        while(rs.next()) {
            Long idOsobe = rs.getLong("OSOBA_ID");
            Long idKontakta = rs.getLong("KONTAKTIRANA_OSOBA_ID");
            try {
                listaKontakata.add(Executors.newSingleThreadExecutor().submit(new DohvatJedneOsobeNit(veza, idKontakta)).get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        //System.out.println("Kraj dohvata svih kontakata jedne osobe - " + listaKontakata);

        rs.close();
        prepStmt.close();

        return listaKontakata;
    }

    /**
     * Služi za dohvat jedne osobe, prema id-u. Otvara vezu, šalje SQL upit, te dobivene podatke vraća.
     * @param idOs id osobe koju treba vratiti
     * @return vraća zatraženu osobu
     * @throws SQLException baca iznimku u slučaju problema u otvaranju/zatvaranju veze, te stvaranju
     * i provođenju upita.
     * @throws IOException baca iznimku u slučaju problema u učitavanju properties datoteke pri otvaranju veze
     */
    public static Osoba dohvatJedneOsobe(Connection veza, Long idOs) throws SQLException {
        //System.out.println("Dohvat jedne osobe.");

        PreparedStatement prepStmt = veza.prepareStatement("SELECT * FROM OSOBA WHERE ID = ?");
        prepStmt.setLong(1, idOs);
        ResultSet rs = prepStmt.executeQuery();

        rs.next();
        Long id = rs.getLong("ID");
        String ime = rs.getString("IME");
        String prezime = rs.getString("PREZIME");
        Date date = (Date) rs.getDate("DATUM_RODJENJA");
        Instant instant = Instant.ofEpochMilli(date.getTime());
        LocalDate localDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
        Long idZup = rs.getLong("ZUPANIJA_ID");
        Long idBol = rs.getLong("BOLEST_ID");

        //System.out.println("Kraj dohvata jedne osobe - " + ime + " " + prezime);

        rs.close();
        prepStmt.close();

        Osoba osoba = null;
        try {
            Zupanija z = Executors.newSingleThreadExecutor().submit(new DohvatJedneZupanijeNit(veza, idZup)).get();
            Bolest b = Executors.newSingleThreadExecutor().submit(new DohvatJedneBolestiNit(veza, idBol)).get();
            List<Osoba> k = Executors.newSingleThreadExecutor().submit(new DohvatSvihKontakataJedneOsobeNit(veza, id)).get();
            osoba = new Osoba.Builder()
                    .brojaId(id)
                    .sIme(ime)
                    .sPrezime(prezime)
                    .ovajDatum(localDate)
                    .uZupanija(z)
                    .imaZarazenBolescu(b)
                    .uzKontaktiraneOsobe(k)
                    .build();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return osoba;
    }

    /**
     * Služi pronalasku osobe prema poziciji te osobe u ComboBox-u. Ne može se dobiti osoba prema odabranom id-u, jer
     * je moguće nepodudaranje id-eva u slučaju brisanja nekog ranijeg unosa iz baze podataka.
     * @param pozicija pozicija odabrane osobe u ComboBox-u
     * @return vraća zatraženu osobu
     * @throws SQLException baca iznimku u slučaju problema u otvaranju/zatvaranju veze, te stvaranju
     * i provođenju upita.
     * @throws IOException baca iznimku u slučaju problema u učitavanju properties datoteke pri otvaranju veze
     */
    public static Osoba dohvatOsobePremaPoziciji(Long pozicija) throws IOException, SQLException {
        Connection veza = otvaranjeVeze();

        //System.out.println("Dohvat osobe prema poziciji.");

        Statement stmt = veza.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM OSOBA");

        Long id = 0L;
        String ime = "";
        String prezime = "";
        LocalDate localDate = null;
        Long idZup = 0L;
        Long idBol = 0L;

        Integer brojac = 0;

        while(rs.next()) {
            if(brojac == pozicija.intValue()) {
                id = rs.getLong("ID");
                ime = rs.getString("IME");
                prezime = rs.getString("PREZIME");
                Date date = (Date) rs.getDate("DATUM_RODJENJA");
                Instant instant = Instant.ofEpochMilli(date.getTime());
                localDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
                idZup = rs.getLong("ZUPANIJA_ID");
                idBol = rs.getLong("BOLEST_ID");
            }
            brojac++;
        }

        //System.out.println("Kraj dohvata osobe prema poziciji - " + ime + " " + prezime);

        Osoba osoba = null;
        try {
            osoba = new Osoba.Builder()
                    .brojaId(id)
                    .sIme(ime)
                    .sPrezime(prezime)
                    .ovajDatum(localDate)
                    .uZupanija(Executors.newSingleThreadExecutor().submit(new DohvatJedneZupanijeNit(veza, idZup)).get())
                    .imaZarazenBolescu(Executors.newSingleThreadExecutor().submit(new DohvatJedneBolestiNit(veza, idBol)).get())
                    .uzKontaktiraneOsobe(Executors.newSingleThreadExecutor().submit(new DohvatSvihKontakataJedneOsobeNit(veza, id)).get())
                    .build();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        rs.close();
        stmt.close();
        zatvaranjeVeze(veza);
        return osoba;
    }

    /**
     * Služi za spremanje nove osobe u bazu podataka. Otvara vezu, i šalje SQL upit kojim unosi novu osobu u tablicu.
     * Potom dohvaća broj id-a zadnje osobe. Nakon toga priprema novi upit, za unos u tablicu kontakata osobe, a
     * provođenjem unosi podatke u bazu.
     * @param osoba osoba koju je potrebno unijeti u bazu podataka
     * @throws SQLException baca iznimku u slučaju problema u otvaranju/zatvaranju veze, te stvaranju
     * i provođenju upita.
     * @throws IOException baca iznimku u slučaju problema u učitavanju properties datoteke pri otvaranju veze
     */
    public static void spremiNovuOsobu(Osoba osoba) throws SQLException, IOException {
        Connection veza = otvaranjeVeze();

        //System.out.println("Spremanje nove osobe.");

        PreparedStatement prepStmt = veza.prepareStatement(
                "INSERT INTO OSOBA(IME, PREZIME, DATUM_RODJENJA, ZUPANIJA_ID, BOLEST_ID) VALUES (?, ?, ?, ?, ?);");
        prepStmt.setString(1, osoba.getIme());
        prepStmt.setString(2, osoba.getPrezime());
        prepStmt.setDate(3, Date.valueOf(osoba.getDatumRodjenja()));
        prepStmt.setLong(4, osoba.getZupanija().getId());
        prepStmt.setLong(5, osoba.getZarazenBolescu().getId());
        prepStmt.executeUpdate();
        prepStmt.close();

        Statement stmt = veza.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM OSOBA");
        Long nextIdNumber = 0L;
        while(rs.next()) {
            nextIdNumber = rs.getLong("ID");
        }
        rs.close();
        stmt.close();

        PreparedStatement prepStmt2 = veza.prepareStatement(
                "INSERT INTO KONTAKTIRANE_OSOBE(OSOBA_ID, KONTAKTIRANA_OSOBA_ID) VALUES (?, ?);");
        for(int i = 0; i < osoba.getKontaktiraneOsobe().size(); i++) {
            prepStmt2.setLong(1, nextIdNumber);
            prepStmt2.setLong(2, osoba.getKontaktiraneOsobe().get(i).getId());
            prepStmt2.executeUpdate();
        }
        prepStmt2.close();

        //System.out.println("Kraj spremanja nove osobe - " + osoba.getIme() + " " + osoba.getPrezime());

        zatvaranjeVeze(veza);
    }





    // ZADATCI -----------------------------------------------------------------------------------

    public static String ispisBrojaVirusa() throws IOException, SQLException {
        String upit = "SELECT COUNT(*) AS broj FROM BOLEST WHERE VIRUS = 1;";

        Connection veza = otvaranjeVeze();
        Statement stmt = veza.createStatement();
        ResultSet rs = stmt.executeQuery(upit);

        Integer broj = 0;

        while(rs.next()) {
            broj = rs.getInt("broj");
        }

        //System.out.println("broj virusa: " + broj);

        rs.close();
        stmt.close();
        zatvaranjeVeze(veza);

        return "Broj virusa: " + broj;
    }

    public static String dohvatZadnjegVirusa() throws IOException, SQLException {
        String upit = "SELECT * FROM BOLEST WHERE VIRUS = 1 ORDER BY ID DESC LIMIT 1;";

        Connection veza = otvaranjeVeze();
        Statement stmt = veza.createStatement();
        ResultSet rs = stmt.executeQuery(upit);

        Long id = rs.getLong("ID");
        String naziv = rs.getString("NAZIV");
        Boolean jeVirus = rs.getBoolean("VIRUS");

        List<Simptom> listaSimptoma = null;
        try {
            listaSimptoma = Executors.newSingleThreadExecutor().submit(new DohvatSvihSimptomaJedneBolestiNit(veza, id)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        rs.close();
        stmt.close();
        zatvaranjeVeze(veza);

        return naziv;
    }
}
