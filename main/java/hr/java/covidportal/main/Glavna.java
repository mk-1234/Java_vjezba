package main.java.hr.java.covidportal.main;

import main.java.hr.java.covidportal.enumeracija.Enumeracija;
import main.java.hr.java.covidportal.genericsi.KlinikaZaInfektivneBolesti;
import main.java.hr.java.covidportal.iznimke.BolestIstihSimptoma;
import main.java.hr.java.covidportal.iznimke.DuplikatKontaktiraneOsobe;
import main.java.hr.java.covidportal.model.*;
import main.java.hr.java.covidportal.sort.CovidSorter;
import main.java.hr.java.covidportal.sort.SortiranjeVirusa;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Služi za evidenciju osoba zaraženih nekom bolešću, te drugih osnovnih informacija o tim osobama i njihovim kontaktima.
 *
 * @author Miroslav Krznar
 */
public class Glavna {

    private static final Logger logger = LoggerFactory.getLogger(Glavna.class);

    /**
     * Služi za pokretanje programa u kojem se od korisnika traži unos
     * županija, simptoma, bolesti, te osoba kako bi se napravila evidencija
     * o osnovnim podatcima tih osoba poput imena, prezimena, starosti,
     * županije u kojoj žive, bolesti od koje boluju, te osobama s kojima
     * su bile u kontaktu.
     * @param args podatak o argumentima komandne linije, ne unose se
     */
    public static void main(String[] args) {

        /*Scanner unos = new Scanner(System.in);

        List<Zupanija> zupanije = new ArrayList<>();
        List<Simptom> simptomi = new ArrayList<>();
        List<Bolest> bolesti = new ArrayList<>();
        List<Osoba> osobe = new ArrayList<>();

        File fileZupanije = new File("dat//zupanije.txt");
        File fileSimptomi = new File("dat//simptomi.txt");
        File fileBolesti = new File("dat//bolesti.txt");
        File fileVirusi = new File("dat//virusi.txt");
        File fileOsobe = new File("dat//osobe.txt");

        logger.info("\n-------------------\nProgram počinje!\n");

        ucitavanjeZupanija(zupanije, fileZupanije);

        ucitavanjeSimptoma(simptomi, fileSimptomi);

        ucitavanjeBolesti(simptomi, bolesti, fileBolesti);

        ucitavanjeVirusa(simptomi, bolesti, fileVirusi);

        ucitavanjeOsoba(zupanije, bolesti, osobe, fileOsobe);

        System.out.println("\n\n* * * * *   Popis osoba   * * * * *");
        ispisOsoba(osobe);

        listeBolesnih(bolesti, osobe);

        postotakZarazenih(zupanije);

        KlinikaZaInfektivneBolesti<Virus, Osoba> klinika = new KlinikaZaInfektivneBolesti<Virus, Osoba>
                (stvaranjeListeVirusa(bolesti), stvaranjeListeZarazenihOsoba(osobe));

        System.out.println("------------------------");
        System.out.println("Virusi sortirani po nazivu suprotno od poretka abecede:");

        sortiranjeVirusaMjerenjeVremena(klinika);

        System.out.println("------------------------");
        System.out.print("Unesite string za pretragu po prezimenu: ");

        trazenjeStringaUPrezimenu(unos, osobe);

        System.out.println("------------------------");
        System.out.println("Ispis broja simptoma za bolesti: ");

        ispisBrojaSimptomaZaBolesti(bolesti);

        serijalizacijaZupanija(zupanije);

        //testDeserijalizacijaIspis();

        System.out.println("------------------------");
        System.out.println("Gotovo!");
        logger.info("\nProgram je završen!\n-----------------------\n");*/

    }

    /**
     * Služi deserijalizaciji i ispisu podataka o županijama s postotkom zaraženih manjim od 2%.
     */
    /*public static void testDeserijalizacijaIspis() {
        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream("dat//zupanije.dat"))) {
            List<Zupanija> procitanaZupanija = (List<Zupanija>) in.readObject();
            logger.info("Deserijalizacija uspješno provedena!");
            System.out.println("------------------------");
            System.out.println("\n\nIspis deserijaliziranih zupanija: ");
            for(Zupanija z : procitanaZupanija) {
                System.out.println("Naziv: " + z.getNaziv());
                System.out.println("Broj stanovnika: " + z.getBrojStanovnika());
                System.out.println("Broj zaraženih: " + z.getBrojZarazenih());
            }
        } catch(IOException | ClassNotFoundException ex) {
            System.err.println(ex);
        }
    }*/

    /**
     * Služi serijalizaciji podataka o županijama u kojima je postotak zaraženih veći od 2%.
     * @param zupanije podatak o setu svih županija
     */
    /*public static void serijalizacijaZupanija(List<Zupanija> zupanije) {
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("dat//zupanije.dat"))) {
            List<Zupanija> zup = zupanije.stream()
                    .filter(z -> ((double)z.getBrojZarazenih() / (double)z.getBrojStanovnika()) > 0.02)
                    .collect(Collectors.toList());
            out.writeObject(zup);
            logger.info("Serijalizacija uspješno provedena!");
        } catch(IOException ex) {
            System.err.println(ex);
        }
    }*/

    /**
     * Služi učitavanju podataka o osobama iz datoteke.
     * @param zupanije podatak o setu svih županija iz kojeg se traži odgovarajuća
     *                 županija za osobu
     * @param bolesti podatak o setu svih bolesti iz kojeg se traži odgovarajuća
     *                bolest za osobu
     * @param osobe podatak o listi svih osoba iz koje se traži odgovarajuća osoba
     *              za uvrštavanje u kontakte
     * @param fileOsobe podatak o datoteci iz koje se učitavaju podatci
     */
    /*public static void ucitavanjeOsoba(List<Zupanija> zupanije, List<Bolest> bolesti, List<Osoba> osobe, File fileOsobe) {
        try(BufferedReader bReader = new BufferedReader(new FileReader(fileOsobe))) {
            String procitanaLinija;
            logger.info("Počinje učitavanje osoba.");
            System.out.println("Učitavanje osoba...");
            while((procitanaLinija = bReader.readLine()) != null) {
                Long id = Long.parseLong(procitanaLinija);

                procitanaLinija = bReader.readLine();
                String ime = procitanaLinija;

                procitanaLinija = bReader.readLine();
                String prezime = procitanaLinija;

                procitanaLinija = bReader.readLine();
                Integer godine = Integer.parseInt(procitanaLinija);

                procitanaLinija = bReader.readLine();
                Long idZupanije = Long.parseLong(procitanaLinija);

                procitanaLinija = bReader.readLine();
                Long idBolesti = Long.parseLong(procitanaLinija);

                procitanaLinija = bReader.readLine();

                List<Osoba> listaKontakata = new ArrayList<>();

                String temp = "";
                for(int i = 0; i < procitanaLinija.length(); i++) {
                    if(procitanaLinija.charAt(i) != ',') {
                        temp += procitanaLinija.charAt(i);
                    }
                    else {
                        Long tempLong = Long.parseLong(temp);
                        temp = "";
                        listaKontakata.add(nadjiOsobu(osobe, tempLong));
                    }
                }
                Long tempLong = Long.parseLong(temp);
                if(tempLong > 0) {
                    listaKontakata.add(nadjiOsobu(osobe, tempLong));
                }

                osobe.add(new Osoba.Builder()
                        .brojaId(id)
                        .sIme(ime)
                        .sPrezime(prezime)
                        .ovaStarost(godine)
                        .uZupanija(nadjiZupaniju(zupanije, idZupanije))
                        .imaZarazenBolescu(nadjiBolest(bolesti, idBolesti))
                        .uzKontaktiraneOsobe(listaKontakata)
                        .build()
                );
            }
            logger.info("Osobe uspješno učitane.");
        } catch(IOException ex) {
            System.out.println("Greška u otvaranju datoteke!");
            ex.printStackTrace();
        }
    }*/

    /**
     * Služi učitavanju podataka o virusima iz datoteke.
     * @param simptomi podatak o setu svih simptoma iz kojeg se traži odgovarajući
     *                 simptom za virus
     * @param bolesti podatak o setu bolesti u koji se unose učitani podatci
     * @param fileVirusi podatak o datoteci iz koje se učitavaju podatci
     */
    /*public static void ucitavanjeVirusa(List<Simptom> simptomi, List<Bolest> bolesti, File fileVirusi) {
        try(BufferedReader bReader = new BufferedReader(new FileReader(fileVirusi))) {
            String procitanaLinija;
            logger.info("Počinje učitavanje virusa.");
            System.out.println("Učitavanje podataka o virusima...");
            while((procitanaLinija = bReader.readLine()) != null) {
                Long id = Long.parseLong(procitanaLinija);
                procitanaLinija = bReader.readLine();
                String naziv = procitanaLinija;

                procitanaLinija = bReader.readLine();
                List<Simptom> tempSetSimptoma = new ArrayList<>();

                String temp = "";
                for(int i = 0; i < procitanaLinija.length(); i++) {
                    if(procitanaLinija.charAt(i) != ',') {
                        temp += procitanaLinija.charAt(i);
                    }
                    else {
                        Long tempLong = Long.parseLong(temp);
                        temp = "";
                        tempSetSimptoma.add(nadjiSimptom(simptomi, tempLong));
                    }
                }
                Long tempLong = Long.parseLong(temp);
                if(tempLong > 0) {
                    tempSetSimptoma.add(nadjiSimptom(simptomi, tempLong));
                }

                bolesti.add(new Virus(id, naziv, tempSetSimptoma));
            }
            logger.info("Virusi uspješno učitani.");
        } catch(IOException ex) {
            System.out.println("Greška u otvaranju datoteke!");
            ex.printStackTrace();
        }
    }*/

    /**
     * Služi učitavanju podataka o bolestima iz datoteke.
     * @param simptomi podatak o setu svih simptoma iz kojeg se traži odgovarajući
     *                 simptom za bolest
     * @param bolesti podatak o setu bolesti u koji se unose učitani podatci
     * @param fileBolesti podatak o datoteci iz koje se učitavaju podatci
     */
    /*public static void ucitavanjeBolesti(List<Simptom> simptomi, List<Bolest> bolesti, File fileBolesti) {
        try(BufferedReader bReader = new BufferedReader(new FileReader(fileBolesti))) {
            String procitanaLinija;
            logger.info("Počinje učitavanje bolesti.");
            System.out.println("Učitavanje podataka o bolestima...");
            while((procitanaLinija = bReader.readLine()) != null) {
                Long id = Long.parseLong(procitanaLinija);
                procitanaLinija = bReader.readLine();
                String naziv = procitanaLinija;

                procitanaLinija = bReader.readLine();
                List<Simptom> tempSetSimptoma = new ArrayList<>();

                String temp = "";
                for(int i = 0; i < procitanaLinija.length(); i++) {
                    if(procitanaLinija.charAt(i) != ',') {
                        temp += procitanaLinija.charAt(i);
                    }
                    else {
                        Long tempLong = Long.parseLong(temp);
                        temp = "";
                        tempSetSimptoma.add(nadjiSimptom(simptomi, tempLong));
                    }
                }
                Long tempLong = Long.parseLong(temp);
                if(tempLong > 0) {
                    tempSetSimptoma.add(nadjiSimptom(simptomi, tempLong));
                }

                bolesti.add(new Bolest(id, naziv, tempSetSimptoma));
            }
            logger.info("Bolesti uspješno učitane.");
        } catch(IOException ex) {
            System.out.println("Greška u otvaranju datoteke!");
            ex.printStackTrace();
        }
    }*/

    /**
     * Služi učitavanju podataka o simptomima iz datoteke.
     * @param simptomi podatak o setu simptoma u koji se unose učitani podatci
     * @param fileSimptomi podatak o datoteci iz koje se učitavaju podatci
     */
    /*public static void ucitavanjeSimptoma(List<Simptom> simptomi, File fileSimptomi) {
        try(BufferedReader bReader = new BufferedReader(new FileReader(fileSimptomi))) {
            String procitanaLinija;
            logger.info("Počinje učitavanje simptoma.");
            System.out.println("Učitavanje podataka o simptomima...");
            while((procitanaLinija = bReader.readLine()) != null) {
                Long id = Long.parseLong(procitanaLinija);
                procitanaLinija = bReader.readLine();
                String naziv = procitanaLinija;
                procitanaLinija = bReader.readLine();
                Enumeracija vrijednost = Enumeracija.valueOf(procitanaLinija);
                simptomi.add(new Simptom(id, naziv, vrijednost));
            }
            logger.info("Simptomi uspješno učitani.");
        } catch(IOException ex) {
            System.out.println("Greška u otvaranju datoteke!");
            ex.printStackTrace();
        }
    }*/

    /**
     * Služi učitavanju podataka o županijama iz datoteke.
     * @param zupanije podatak o setu županija u koji se unose učitani podatci
     * @param fileZupanije podatak o datoteci iz koje se učitavaju podatci
     */
    /*public static void ucitavanjeZupanija(List<Zupanija> zupanije, File fileZupanije) {
        try(BufferedReader bReader = new BufferedReader(new FileReader(fileZupanije))) {
            String procitanaLinija;
            logger.info("Počinje učitavanje županija.");
            System.out.println("Učitavanje podataka o županijama...");
            while((procitanaLinija = bReader.readLine()) != null) {
                Long id = Long.parseLong(procitanaLinija);
                procitanaLinija = bReader.readLine();
                String naziv = procitanaLinija;
                procitanaLinija = bReader.readLine();
                Integer brojStanovnika = Integer.parseInt(procitanaLinija);
                procitanaLinija = bReader.readLine();
                Integer brojZarazenih = Integer.parseInt(procitanaLinija);
                zupanije.add(new Zupanija(id, naziv, brojStanovnika, brojZarazenih));
            }
            logger.info("Županije uspješno učitane.");
        } catch(IOException ex) {
            System.out.println("Greška u otvaranju datoteke!");
            ex.printStackTrace();
        }
    }*/

    /**
     * Služi za pronalazak simptoma s određenim id-om iz seta svih simptoma.
     * @param simptomi podatak o setu simptoma, sadrži sve učitane simptome
     * @param id podatak o identifikacijskom broju
     * @return vraća simptom s traženim id-om ili null ako nije pronađen takav simptom
     */
    /*public static Simptom nadjiSimptom(List<Simptom> simptomi, Long id) {
        return simptomi.stream()
                .filter(s -> id.equals(s.getId()))
                .findAny()
                .orElse(null);
    }*/

    /**
     * Služi za pronalazak županije s određenim id-om iz seta svih županija.
     * @param zupanije podatak o setu županija, sadrži sve učitane županije
     * @param id podatak o identifikacijskom broju
     * @return vraća županiju s traženim id-om ili null ako nije pronađena takva županija
     */
    /*public static Zupanija nadjiZupaniju(List<Zupanija> zupanije, Long id) {
        return zupanije.stream()
                .filter(z -> id.equals(z.getId()))
                .findAny()
                .orElse(null);
    }*/

    /**
     * Služi za pronalazak bolesti s određenim id-om iz seta svih bolesti.
     * @param bolesti podatak o setu bolesti, sadrži sve učitane bolesti
     * @param id podatak o identifikacijskom broju
     * @return vraća bolest s traženim id-om ili null ako nije pronađena takva bolest
     */
    /*public static Bolest nadjiBolest(List<Bolest> bolesti, Long id) {
        return bolesti.stream()
                .filter(b -> id.equals(b.getId()))
                .findAny()
                .orElse(null);
    }*/

    /**
     * Služi za pronalazak osobe s određenim id-om iz liste svih osoba.
     * @param osobe podatak o listi osoba, sadrži sve učitane osobe
     * @param id podatak o identifikacijskom broju
     * @return vraća osobu s traženim id-om ili null ako nije pronađena takva osoba
     */
    /*public static Osoba nadjiOsobu(List<Osoba> osobe, Long id) {
        return osobe.stream()
                .filter(o -> id.equals(o.getId()))
                .findAny()
                .orElse(null);
    }*/

    /**
     * Služi stvaranju liste svih virusa. Metoda prima
     * set svih unesenih bolesti, te provjerava jesu li
     * te bolesti virusi.
     * @param bolesti podatak o setu svih unesenih bolesti
     * @return vraća listu virusa
     */
    /*public static List<Virus> stvaranjeListeVirusa(List<Bolest> bolesti) {
        return bolesti.stream()
                .filter(b -> b instanceof Virus)
                .map(b -> (Virus)b)
                .collect(Collectors.toList());
    }*/

    /**
     * Služi stvaranju liste svih zaraženih osoba. Metoda prima
     * listu svih unesenih osoba, te provjerava jesu li osobe zaražene
     * virusom.
     * @param osobe podatak o listi svih unesenih osoba
     * @return vraća listu osoba koje su zaražene virusom
     */
    /*public static List<Osoba> stvaranjeListeZarazenihOsoba(List<Osoba> osobe) {
        return osobe.stream()
                .filter(o -> o.getZarazenBolescu() instanceof Virus)
                .collect(Collectors.toList());
    }*/

    /**
     * Služi za sortiranje svih virusa prema nazivu, obrnuto
     * od redosljeda abecede. Metoda sortira viruse korištenjem
     * lambdi, i mjeri vrijeme potrebno za izvršavanje, te potom
     * sortira korištenjem ugrađene sort metode liste, i mjeri
     * vrijeme. Na kraju navodi iznose mjerenja u poruci.
     * @param klinika podatak o objektu koji sadrži informacije o
     *                svim unesenim virusima, te zaraženim osobama
     */
    /*public static void sortiranjeVirusaMjerenjeVremena(KlinikaZaInfektivneBolesti<Virus, Osoba> klinika) {
        AtomicInteger counter = new AtomicInteger();
        Instant startLambda = Instant.now();
        klinika.dohvatiListuVirusa().stream()
                .sorted(new SortiranjeVirusa())
                .forEach(v -> System.out.println(counter.incrementAndGet() + ". " + v.getNaziv()));
        Instant endLambda = Instant.now();

        Instant startNoLambda = Instant.now();
        klinika.dohvatiListuVirusa().sort(new SortiranjeVirusa());
        Instant endNoLambda = Instant.now();

        System.out.println("Sortiranje objekata korištenjem lambdi traje "
                + Duration.between(startLambda, endLambda).toMillis() + " milisekundi, a bez lambda traje "
                + Duration.between(startNoLambda, endNoLambda).toMillis() + " milisekundi.");
    }*/

    /**
     * Služi za traženje određenog stringa u prezimenima
     * unesenih osoba. Potrebno je unijeti string koji će se
     * tražiti, te se ispisuje sve osobe s takvim stringom
     * u prezimenu, a ako nema takvih osoba, ispisuje se
     * odgovarajuća poruka.
     * @param unos služi za unos informacija preko tipkovnice
     * @param osobe podatak o listi svih osoba
     */
    /*public static void trazenjeStringaUPrezimenu(Scanner unos, List<Osoba> osobe) {
        String zaPretragu = unos.nextLine();
        logger.info("Uneseni string za pretragu je: " + zaPretragu);

        Optional<Osoba> optOsobe = osobe.stream()
                .findFirst()
                .filter(o -> o.getPrezime().contains(zaPretragu));

        if(optOsobe.isEmpty()) {
            System.out.println("Ne postoje osobe čije prezime sadrži taj naziv!");
            logger.info("Nema osoba koje sadrže neseni string!");
        }
        else {
            System.out.println("Osobe čije prezime sadrži \"" + zaPretragu + "\" su sljedeće:");
            osobe.stream()
                    .filter(o -> o.getPrezime().contains(zaPretragu))
                    .forEach(o -> System.out.println(o.getIme() + " " + o.getPrezime()));
        }
    }*/

    /**
     * Služi ispisu broja simptoma svih bolesti koje su unesene.
     * @param bolesti podatak o setu koji sadrži sve bolesti
     */
    /*public static void ispisBrojaSimptomaZaBolesti(List<Bolest> bolesti) {
        bolesti.stream()
                .map(b -> b.getNaziv() + " ima " + b.getSimptomi().size()
                        + (b.getSimptomi().size() == 1 ? " simptom." : " simptoma."))
                .forEach(System.out::println);
    }*/

    /**
     * Služi izračunu, te potom ispisu najvećeg postotka zaraženih
     * među svim županijama. Županije se na početku sortira prema
     * postotku zaraženih te se na kraju županija uzima s vrha te
     * liste i ispisuje. Izračun se vrši dijeljenjem broja zaraženih
     * stanovnika s ukupnim brojem stanovnika, te množenjem sa 100 kako
     * bi se dobio postotak.
     * @param zupanije podatak o setu svih unesenih županija
     */
    /*public static void postotakZarazenih(List<Zupanija> zupanije) {
        SortedSet<Zupanija> sortedZupanije = new TreeSet<>(new CovidSorter());
        for(Zupanija zupanija : zupanije) {
            sortedZupanije.add(zupanija);
        }
        String nazivZup = sortedZupanije.first().getNaziv();
        Double brZar = sortedZupanije.first().getBrojZarazenih().doubleValue();
        Double brSt = sortedZupanije.first().getBrojStanovnika().doubleValue();
        Double postotak = (brZar / brSt) * 100;
        System.out.println("Najviše zaraženih osoba ima u županiji " + nazivZup + ": " + postotak.intValue() + "%.");
    }*/

    /**
     * Služi ispisu svih osoba koje boluju od pojedinih bolesti.
     * Ispisuje se za svaku bolest posebno. Mapa listaOsobaPremaBolesti
     * prihvaća kao ključ bolest, a kao vrijednost listu osoba koje boluju
     * od te bolesti. Ako postoje osobe koje boluju od pojedine bolesti,
     * ta bolest zajedno s pripadajućom listom oboljelih će biti ispisana.
     * @param bolesti podatak o setu svih unesenih bolesti
     * @param osobe podatak o listi svih unesenih osoba
     */
    /*public static void listeBolesnih(List<Bolest> bolesti, List<Osoba> osobe) {
        System.out.println("------------------------");
        Map<Bolest, List<Osoba>> listaOsobaPremaBolesti = new HashMap<>();
        for(Bolest bolest : bolesti) {
            List<Osoba> listaOsoba = new ArrayList<>();
            for(Osoba osoba : osobe) {
                if(osoba.getZarazenBolescu().equals(bolest)) {
                    listaOsoba.add(osoba);
                }
            }
            listaOsobaPremaBolesti.put(bolest, listaOsoba);
        }
        for(Bolest bolest : listaOsobaPremaBolesti.keySet()) {
            if(!(listaOsobaPremaBolesti.get(bolest).isEmpty())) {
                System.out.print("Od " + ((bolest instanceof Virus) ? "virusa " : "bolesti ") + bolest.getNaziv() + " boluju: ");
                int brojac = 0;
                for(Osoba osoba : listaOsobaPremaBolesti.get(bolest)) {
                    System.out.print(osoba.getIme() + " " + osoba.getPrezime());
                    brojac++;
                    System.out.print((brojac == listaOsobaPremaBolesti.get(bolest).size()) ? "." : ", ");
                }
                System.out.println();
            }
        }
    }*/

    /**
     * Služi konačnom ispisu svih unesenih osoba, te svih unesenih
     * podataka o tim osobama (ime, prezime, starost, županija,
     * bolest, kontakti). U slučaju da nema kontaktiranih osoba, to
     * će se ispisati.
     * @param osobe podatak o listi osoba
     */
    /*public static void ispisOsoba(List<Osoba> osobe) {
        for(Osoba osoba : osobe) {
            System.out.println("------------------------");
            System.out.println("Ime i prezime: " + osoba.getIme() + " " + osoba.getPrezime());
            //System.out.println("Starost: " + osoba.getStarost());
            System.out.println("DOB: " + osoba.getDatumRodjenja());
            System.out.println("Županija prebivališta: " + osoba.getZupanija().getNaziv());
            System.out.println("Zaražen bolešću: " + osoba.getZarazenBolescu().getNaziv());
            System.out.println("Kontaktirane osobe:");
            for(Osoba osoba1 : osoba.getKontaktiraneOsobe()) {
                System.out.println(osoba1.getIme() + " " + osoba1.getPrezime());
            }
            if(osoba.getKontaktiraneOsobe().isEmpty()) {
                System.out.println("Nema kontaktiranih osoba.");
            }
        }
    }*/

    /**
     * Služi provjeri unesenog broja, kako bi se utvrdilo nalazi li se unutar
     * prihvatljivih okvira. Ako je vrijednost veća od x, i manja ili jednaka y,
     * tada je vrijednost zadovoljavajuća, te metoda vraća true. U suprotnom,
     * ispisuje se poruka o grešci te se traži ponovni unos. Često se koristi kod
     * izbora između nekoliko vrijednosti, kako bi se osiguralo da se ne odabere
     * nedostupna vrijednost.
     * @param vrijednost podatak o vrijednosti koja se provjerava
     * @param x donja granica prihvatljive vrijednosti
     * @param y gornja granica prihvatljive vrijednosti
     * @return vraća true/false odgovor na pitanje o prihvatljivosti izbora
     */
    /*public static boolean isIspravanUnos(Integer vrijednost, Integer x, Integer y) {
        if (vrijednost > x && vrijednost <= y) {
            return true;
        }
        else {
            logger.info("Unos broja nije u odgovarajućim granicama!");
            System.out.println("Neispravan unos, molimo pokušajte ponovno!");
            return false;
        }
    }*/

    /**
     * Provjerava je li uneseni podatak broj. U slučaju da nije,
     * tražit će se ponavljanje unosa sve dok se ne unese ispravan
     * format podatka, tj. broj. Pogrešan unos baca iznimku koja se
     * obrađuje u catch bloku te ispisuje grešku.
     * @param unos služi za unos informacija preko tipkovnice
     * @param str podatak o tekstu koji se ponavlja prilikom novog unosa
     * @return metoda vraća uneseni broj
     */
    /*public static Integer unosBroja(Scanner unos, String str) {
        boolean jeBroj = false;
        Integer x = 0;
        while(!jeBroj) {
            try {
                System.out.print(str);
                x = unos.nextInt();
                unos.nextLine();
                jeBroj = true;
            }
            catch(InputMismatchException ex) {
                unos.nextLine();
                logger.error("Greška! Došlo je do pogreške u formatu podatka!", ex);
                System.out.println("Pogreška u formatu podataka, molimo ponovite unos!");
            }
        }
        return x;
    }*/



    // ---------- FUNKCIJE SE NE KORISTE !!! ------------------------------------------------


    /**
     * Služi unosu simptoma u bazu podataka. Unosi se naziv, te
     * vrijednost simptoma, provjerava je li vrijednost ispravno
     * unesena, tj. postoji li među konstantama klase Enumeracija, te
     * potom uvrštava u set simptoma koji služi kao baza podataka
     * svih simptoma.
     * @param unos služi za unos informacija preko tipkovnice
     * @param simptomi podatak o setu simptoma
     * @param brojSimptoma predstavlja broj simptoma koje treba unijeti
     */
    /*public static void unosSimptoma(Scanner unos, Set<Simptom> simptomi, Integer brojSimptoma) {

        for(int i = 0; i < brojSimptoma; i++) {
            System.out.print("\nUnesite naziv " + (i + 1) + ". simptoma: ");
            String ime = unos.nextLine();
            logger.info("Naziv " + (i + 1) + ". simptoma je: " + ime);
            boolean ispravanUnos = false;
            String vrij = "";
            while(!ispravanUnos) {
                try {
                    System.out.print("Unesite vrijednost " + (i + 1) + ". simptoma (RIJETKO, SREDNJE, ILI CESTO): ");
                    vrij = unos.nextLine();

                    Enumeracija e = Enumeracija.valueOf(vrij);
                    ispravanUnos = true;

                    logger.info("Vrijednost " + (i + 1) + ". simptoma je: " + e.getVrijednost());
                    //simptomi.add(new Simptom(ime, e));
                }
                catch(IllegalArgumentException ex) {
                    logger.error("Pogrešan unos vrijednosti!", ex);
                    System.out.println("Pogrešan unos vrijednosti. Molim, ponovite!\n");

                }
            }
        }
    }*/

    /**
     * Služi unosu županija u bazu podataka. Unosi se naziv, broj
     * stanovnika županije, te broj zaraženih stanovnika. Provjerava
     * se jesu li uneseni brojevi, te u pozitivnom slučaju uvrštava se
     * u set županija koji služi kao baza podataka svih županija.
     * @param unos služi za unos informacija preko tipkovnice
     * @param zupanije podatak o setu županija
     * @param brojZupanija predstavlja broj županija koje treba unijeti
     */
    /*public static void unosZupanije(Scanner unos, Set<Zupanija> zupanije, Integer brojZupanija) {
        for(int i = 0; i < brojZupanija; i++) {
            System.out.print("\nUnesite naziv " + (i + 1) + ". županije: ");
            String ime = unos.nextLine();
            logger.info("Naziv " + (i + 1) + ". županije je: " + ime);
            String s = "Unesite broj stanovnika " + (i + 1) + ". županije: ";
            Integer broj = unosBroja(unos, s);
            logger.info("Broj stanovnika " + (i + 1) + ". županije je: " + broj);
            s = "Unesite broj zaraženih stanovnika " + (i + 1) + ". županije: ";
            Integer brojZar = unosBroja(unos, s);
            logger.info("Broj zaraženih stanovnika " + (i + 1) + ". županije je: " + broj);
            //zupanije.add(new Zupanija(ime, broj, brojZar));
        }
    }*/

    /**
     * Služi za provjeru i prolazak kontakta koji se već nalazi u listi
     * kontakata osobe čiji se kontakti provjeravaju. Metoda ne dopušta
     * odabir osobe koja se već nalazi u kontaktima. U listi postojećih
     * kontakata, za svaku osobu (osoba), provjerava je li nova odabrana
     * osoba (b) ista kao ona među kontaktima. Petlja se vrti ovisno o broju
     * kontakta koji se unosi. Za prvi kontakt se ne provjerava, jer se nema
     * s čime usporediti. Ako metoda nađe jednaku osobu, baca se iznimka
     * koja se potom hvata u metodi unosOsobe. Ako takve osobe nema, može
     * se uvrstiti u kontakte.
     * @param a podatak o listi osoba koje predstavljaju kontakte osobe
     * @param b nova odabrana osoba, koju treba provjeriti
     * @param j broj kontakta koji se unosi
     * @throws DuplikatKontaktiraneOsobe
     */
    /*public static void provjeraDuplikata(List<Osoba> a, Osoba b, int j) throws DuplikatKontaktiraneOsobe {
        for(Osoba osoba : a) {
            if(osoba.equals(b)) {
                throw new DuplikatKontaktiraneOsobe("Odabrana osoba se već nalazi među kontaktiranim osobama. " +
                        "Molimo Vas da odaberete neku drugu osobu.");
            }
        }
    }*/

    /**
     * Provjerava postoji li bolest s jednakim simptomima, te u slučaju
     * pronalaska baca iznimku koja se hvata u metodi unosBolesti.
     * Metoda prima set simptoma koji se trebaju provjeriti prije nego
     * se proslijede za stvaranje nove bolesti za bazu podataka, zatim prima
     * set svih do tada unesenih bolesti, i broj bolesti koja se unosi a ovdje
     * predstavlja broj bolesti koje se nalaze u setu i za koje treba usporediti
     * simptome. S obzirom na to da simptomi u setu simptoma nisu nužno poredani,
     * na početku treba sortirati u privremenom setu simptome koje želimo
     * usporediti. Njih dobivamo iz seta simptoma koji se provjeravaju za
     * unos nove bolesti, te iz svake od bolesti iz baze podataka bolesti pomoću
     * getSimptomi metode. Uspoređuje se dva privremena seta, te ako su jednaki, baca
     * se iznimka BolestIstihSimptoma.
     * @param a podatak o setu simptoma koje treba usporediti s drugim bolestima
     * @param b set bolesti, baza podataka svih bolesti
     * @param broj predstavlja broj unesenih bolesti
     * @throws BolestIstihSimptoma
     */
    /*public static void provjeraSimptoma(Set<Simptom> a, Set<Bolest> b, int broj) throws BolestIstihSimptoma {

        SortedSet<Simptom> temp = new TreeSet<>(new UsporediSimptome());
        for(Simptom simptom : a) {
            temp.add(simptom);
        }
        for(int i = 0; i < broj; i++) {
            SortedSet<Simptom> tempBolest = new TreeSet<>(new UsporediSimptome());
            int brojac = 0;
            for(Bolest bolest : b) {
                if(brojac == i) {
                    for(Simptom simptom : bolest.getSimptomi()) {
                        tempBolest.add(simptom);
                    }
                }
                brojac++;
            }
            if(temp.equals(tempBolest)) {
                throw new BolestIstihSimptoma("Pogrešan unos, već ste unijeli bolest ili virus s istim " +
                        "simptomima. Molimo ponovite unos.");
            }
        }
    }*/

    /**
     * Provjerava se ispravnost odabrane vrijednosti. Vrijednost
     * mora biti veća od x, i manja ili jednaka y kako bi bila valjana.
     * Maksimalni broj kontakata koji se može odabrati je broj do tada
     * unesenih osoba, a minimalni broj je 0, kada osoba nema uopće
     * kontakte. Ako je vrijednost valjana vraća se true, ako nije baca se
     * iznimka koja se potom hvata u metodi unosOsobe.
     * @param vrijednost vrijednost koja se provjerava
     * @param x donja granica prihvatljivosti izbora
     * @param y gornja granica prihvatljivosti izbora
     * @return vraća true ako je vrijednost valjana
     * @throws ArithmeticException
     */
    /*public static boolean provjeraBrojaKontakata(Integer vrijednost, Integer x, Integer y) throws ArithmeticException {
        if (vrijednost > x && vrijednost <= y) {
            return true;
        }
        else {
            throw new ArithmeticException();
        }
    }*/


    /**
     * Unosi podatke o osobi: ime, prezime, starost, županiju u kojoj
     * živi, bolest od koje boluje, te kontaktirane osobe. Svaka osoba
     * među kontakte može uvrstiti samo osobe koju su već ranije unesene
     * u evidenciju, te prema tome, prva osoba neće imati kontakata. Od
     * osoba koje su unesene nakon prve, te mogu imati kontakte, traži se
     * unos broja kontaktiranih osoba (ograničava se na 0 - broj ranije
     * unesenih osoba), te ako je broj veći od 0, traži se odabir tog broja
     * osoba iz liste do tada unesenih osoba, pri čemu se ne može istu
     * osobu odabrati više puta.
     * @param unos služi za unos informacija preko tipkovnice
     * @param zupanije podatak o setu županija
     * @param bolesti podatak o setu bolesti
     * @param osobe podatak o listi osoba
     * @param brojOsoba podatak o ukupnom broju osoba koje treba unijeti
     * @param brojZupanija podatak o ukupnom broju županija
     * @param brBolVir podatak o ukupnom broju bolesti i virusa
     */
    /*public static void unosOsobe(Scanner unos, Set<Zupanija> zupanije, Set<Bolest> bolesti, List<Osoba> osobe,
                                 Integer brojOsoba, Integer brojZupanija, Integer brBolVir)
    {
        for(int i = 0; i < brojOsoba; i++) {
            System.out.println("\n *** " + (i + 1) + ". OSOBA ***");
            System.out.print("\nUnesite ime: ");
            String imeOsobe = unos.nextLine();
            logger.info("Uneseno je ime " + (i + 1) + ". osobe: " + imeOsobe);
            System.out.print("Unesite prezime: ");
            String prezimeOsobe = unos.nextLine();
            logger.info("Uneseno je prezime " + (i + 1) + ". osobe: " + prezimeOsobe);
            String str = "Unesite starost: ";
            Integer godine = unosBroja(unos, str);
            logger.info("Unesena je starost " + (i + 1) + ". osobe: " + godine);

            Integer odabirZupanije = izborZupanije(unos, zupanije, brojZupanija);
            int brojac = 1;
            for (Zupanija zupanija : zupanije) {
                if (brojac == odabirZupanije) {
                    logger.info("Unesena je županija " + (i + 1) + ". osobe: " + zupanija.getNaziv());
                }
                brojac++;
            }

            Integer odabirBolesti = izborBolesti(unos, bolesti, brBolVir);
            brojac = 1;
            for (Bolest bolest : bolesti) {
                if (brojac == odabirBolesti) {
                    logger.info("Unesena je bolest " + (i + 1) + ". osobe: " + bolest.getNaziv());
                }
                brojac++;
            }

            List<Osoba> o = new ArrayList<>();

            if (i > 0) {
                Integer brojKontakata = 0;
                boolean ispravanUnos = false;

                while (!ispravanUnos) {
                    str = "\nUnesite broj osoba koje su bile u kontaktu s " + (i + 1) + ". osobom (0 - " + i + "): ";
                    brojKontakata = unosBroja(unos, str);
                    logger.info("Odabrano je " + brojKontakata + " kontakata.");

                    try {
                        ispravanUnos = provjeraBrojaKontakata(brojKontakata, -1, i);
                        logger.info("Unesen je broj osoba u kontaktu s " + (i + 1) + ". osobom: " + brojKontakata);
                    } catch (ArithmeticException ex) {
                        logger.error("Unesen je pogrešan broj osoba, moguće je maksimalno " + i, ex);
                        System.out.println("Neispravan unos, možete odabrati maksimalno " + i + " osobu.\n");
                    }

                }

                if (brojKontakata > 0) {
                    Integer odabirOsobe = 0;
                    System.out.println("\nUnesite osobe koje su bile u kontaktu s " + (i + 1) + ". osobom: ");
                    for (int j = 0; j < brojKontakata; j++) {

                        ispravanUnos = false;
                        while (!ispravanUnos) {
                            str = "\nOdaberite " + (j + 1) + ". osobu:\n";
                            brojac = 1;
                            for (Osoba osoba : osobe) {
                                str += brojac + ". " + osoba.getIme() + " " + osoba.getPrezime() + "\n";
                                brojac++;
                            }
                            str += "Odabir (1 - " + brojKontakata + "): ";
                            odabirOsobe = unosBroja(unos, str);
                            logger.info("Odabrani kontakt je pod brojem " + odabirOsobe);

                            try {
                                if (j > 0) {
                                    brojac = 1;
                                    for (Osoba osoba : osobe) {
                                        if (brojac == odabirOsobe) {
                                            provjeraDuplikata(o, osoba, j);
                                        }
                                        brojac++;
                                    }
                                }
                                ispravanUnos = isIspravanUnos(odabirOsobe, 0, i);
                                if (ispravanUnos) {
                                    brojac = 1;
                                    for (Osoba osoba : osobe) {
                                        if (brojac == odabirOsobe) {
                                            logger.info("Odabrana je osoba " + osoba.getIme() + " " +
                                                    osoba.getPrezime() + " kao kontakt osobe " + imeOsobe +
                                                    " " + prezimeOsobe + ".");
                                        }
                                        brojac++;
                                    }
                                }
                            }
                            catch (DuplikatKontaktiraneOsobe ex) {
                                brojac = 1;
                                for (Osoba osoba : osobe) {
                                    if (brojac == odabirOsobe) {
                                        logger.error("Greška! Osoba " + osoba.getIme() + " " +
                                                osoba.getPrezime() + " je već odabrana kao kontakt!", ex);
                                    }
                                    brojac++;
                                }
                                System.out.println(ex.getMessage());
                            }
                        }

                        brojac = 1;
                        for (Osoba osoba : osobe) {
                            if (brojac == odabirOsobe) {
                                o.add(osoba);
                            }
                            brojac++;
                        }
                    }
                    novaOsoba(zupanije, bolesti, osobe, i, imeOsobe, prezimeOsobe, godine, odabirZupanije, odabirBolesti, o);
                }
                else {
                    novaOsoba(zupanije, bolesti, osobe, i, imeOsobe, prezimeOsobe, godine, odabirZupanije, odabirBolesti, o);
                }
            }
            else {
                novaOsoba(zupanije, bolesti, osobe, i, imeOsobe, prezimeOsobe, godine, odabirZupanije, odabirBolesti, o);
            }
        }
    }*/

    /**
     * Služi inicijalizaciji nove osobe preko builder pattern-a.
     * Proslijeđuju se podatci o imenu, prezimenu, godinama, županiji,
     * bolesti, i listi osoba koje predstavljaju kontakte.
     * @param zupanije podatak o setu županija
     * @param bolesti podatak o setu bolesti
     * @param osobe podatak o listi osoba
     * @param i predstavlja broj osobe za koju se unose podatci
     * @param imeOsobe podatak o imenu
     * @param prezimeOsobe podatak o prezimenu
     * @param godine podatak o godinama
     * @param odabirZupanije podatak o odabranoj županiji
     * @param odabirBolesti podatak o odabranoj bolesti
     * @param o podatak o listi osoba koje predstavljaju kontakte
     */
    /*public static void novaOsoba(Set<Zupanija> zupanije, Set<Bolest> bolesti, List<Osoba> osobe, int i, String imeOsobe,
                                  String prezimeOsobe, Integer godine, Integer odabirZupanije, Integer odabirBolesti,
                                  List<Osoba> o)
    {
        int brojac = 1;
        int brojac2 = 1;
        for(Zupanija zupanija : zupanije) {
            if(brojac == odabirZupanije) {
                for(Bolest bolest : bolesti) {
                    if(brojac2 == odabirBolesti) {
                        osobe.add(new Osoba.Builder()
                                .sIme(imeOsobe)
                                .sPrezime(prezimeOsobe)
                                .ovaStarost(godine)
                                .uZupanija(zupanija)
                                .imaZarazenBolescu(bolest)
                                .uzKontaktiraneOsobe(o)
                                .build());
                    }
                    brojac2++;
                }
            }
            brojac++;
        }
    }*/

    /**
     * Služi izboru županije u kojoj se nalazi osoba za koju unosimo
     * podatke. Bira se između svih ranije unesenih županija, zatim
     * se provjerava postoji li županija pod tim brojem (izbor je od 1
     * do maksimalnog broja županija), te ukoliko je izbor odgovarajući
     * vraća se taj broj natrag gdje je metoda pozvana.
     * @param unos služi za unos informacija preko tipkovnice
     * @param zupanije podatak o setu županija
     * @param brojZupanija podatak o ukupnom broju županija
     * @return vraća se broj odabrane županije
     */
    /*public static Integer izborZupanije(Scanner unos, Set<Zupanija> zupanije, Integer brojZupanija) {
        Integer odabirZupanije = 0;
        boolean ispravanUnos = false;
        while(!ispravanUnos) {
            String str = "\nUnesite županiju prebivališta: \n";
            int brojac = 1;
            for(Zupanija zupanija : zupanije) {
                str += brojac + ". " + zupanija.getNaziv() + "\n";
                brojac++;
            }
            str += "Odabir (1 - " + brojZupanija + "): ";
            odabirZupanije = unosBroja(unos, str);
            logger.info("Odabrana županija je pod brojem " + odabirZupanije);
            ispravanUnos = isIspravanUnos(odabirZupanije, 0, brojZupanija);
            if(ispravanUnos) {
                brojac = 1;
                for(Zupanija zupanija : zupanije) {
                    if(brojac == odabirZupanije) {
                        logger.info("Odabrana je " + zupanija.getNaziv() + " županija.");
                    }
                    brojac++;
                }
            }
        }
        return odabirZupanije;
    }*/

    /**
     * Služi izboru bolesti od koje boluje osoba za koju unosimo
     * podatke. Bira se između svih ranije unesenih bolesti, zatim
     * se provjerava postoji li bolest pod tim brojem (izbor je od 1
     * do maksimalnog broja bolesti), te ukoliko je izbor odgovarajući
     * vraća se taj broj natrag gdje je metoda pozvana.
     * @param unos služi za unos informacija preko tipkovnice
     * @param bolesti podatak o setu bolesti
     * @param brojBolesti podatak o ukupnom broju bolesti
     * @return vraća se broj odabrane bolesti
     */
    /*public static Integer izborBolesti(Scanner unos, Set<Bolest> bolesti, Integer brojBolesti) {
        Integer odabirBolesti = 0;
        boolean ispravanUnos = false;
        while(!ispravanUnos){
            String str = "\nUnesite bolest: \n";
            int brojac = 1;
            for(Bolest bolest : bolesti) {
                str += brojac + ". " + bolest.getNaziv() + "\n";
                brojac++;
            }
            str += "Odabir (1 - " + brojBolesti + "): ";
            odabirBolesti = unosBroja(unos, str);
            logger.info("Odabrana bolest je pod brojem " + odabirBolesti);
            ispravanUnos = isIspravanUnos(odabirBolesti, 0, brojBolesti);
            if(ispravanUnos) {
                brojac = 1;
                for(Bolest bolest : bolesti) {
                    if(brojac == odabirBolesti) {
                        logger.info("Odabrana je bolest " + bolest.getNaziv() + ".");
                    }
                    brojac++;
                }
            }
        }
        return odabirBolesti;
    }*/

    /**
     * Služi stvaranju baze podataka o bolestima, te simptomima
     * pojedinih bolesti, između kojih se kasnije bira pri unosu osoba.
     * Potrebno je odmah unijeti radi li se o bolesti ili virusu. Razlog
     * je što osoba zaražena virusom može prenijeti virus i na druge, pa
     * je potrebno znati koja je vrsta bolesti. Moguć je izbor između
     * dvije opcije, bolesti i virusa, što se provjeri metodom isIspravanUnos.
     * Moguće je unijeti samo unaprijed određen broj bolesti i virusa.
     * Potom se unosi naziv te broj simptoma bolesti, pri čemu se
     * broj simptoma provjerava (mora biti od 1 do maksimalnog
     * broja simptoma u bazi podataka). Nakon toga se za svaku bolest
     * unose simptomi u metodi izborSimptoma, te se nakon toga u metodi
     * provjeraSimptoma provjerava postoji li ranije unesena bolest s
     * istim simptomima. Ako je sve u redu, bolest se sprema u bazu podataka.
     * @param unos služi za unos informacija preko tipkovnice
     * @param simptomi podatak o setu simptoma
     * @param bolesti podatak o setu bolesti
     * @param brBolVir predstavlja ukupan broj bolesti i virusa koji se unose
     * @param brojBolesti podatak o ukupnom broju bolesti
     * @param brojVirusa podatak o ukupnom broju virusa
     * @param brojSimptoma podatak o ukupnom broju simptoma
     */
    /*public static void unosBolesti(Scanner unos, Set<Simptom> simptomi, Set<Bolest> bolesti, Integer brBolVir,
                                   Integer brojBolesti, Integer brojVirusa, Integer brojSimptoma)
    {
        Map<Integer, Integer> abc = new HashMap<>();
        for(int i = 0; i < brBolVir; i++) {
            boolean ispravanUnos = false;
            Integer vrsta = Integer.valueOf(0);
            while (!ispravanUnos) {
                String str = "\nUnosite li bolest ili virus?\n1) Bolest\n2) Virus\nOdabir >> ";
                vrsta = unosBroja(unos, str);
                logger.info("Odabrana vrsta bolesti je pod brojem " + vrsta + ".");
                ispravanUnos = isIspravanUnos(vrsta, 0, 2);
                if (ispravanUnos) {
                    if (abc.containsKey(vrsta)) {
                        Integer x = abc.get(vrsta);
                        abc.put(vrsta, x + 1);
                    }
                    else {
                        abc.put(vrsta, 1);
                    }
                    if (vrsta.equals(1) && (abc.get(vrsta) > brojBolesti)) {
                        Integer x = abc.get(vrsta);
                        abc.put(vrsta, x - 1);
                        logger.info("Odabrano je previše bolesti!");
                        System.out.println("Odabrali ste previše bolesti, molim izaberite virus!");
                        ispravanUnos = false;
                    }
                    if (vrsta.equals(2) && (abc.get(vrsta) > brojVirusa)) {
                        Integer x = abc.get(vrsta);
                        abc.put(vrsta, x - 1);
                        logger.info("Odabrano je previše virusa!");
                        System.out.println("Odabrali ste previše virusa, molim izaberite bolest!");
                        ispravanUnos = false;
                    }
                }
                if (ispravanUnos) {
                    logger.info("Odabran je unos " + (vrsta.equals(1) ? "bolesti" : "virusa") + ".");
                }
            }

            String ime = "";
            Set<Simptom> s = new LinkedHashSet<>();

            boolean istiSimptomi = true;
            while (istiSimptomi) {
                Integer broj = 0;
                System.out.print("\nUnesite naziv " + (i + 1) + ". bolesti ili virusa: ");
                ime = unos.nextLine();

                ispravanUnos = false;
                while (!ispravanUnos) {
                    String str = "Unesite broj simptoma (1 - " + brojSimptoma + "): ";
                    broj = unosBroja(unos, str);
                    logger.info("Broj simptoma odabran za unos je " + broj);
                    ispravanUnos = isIspravanUnos(broj, 0, brojSimptoma);
                    if (ispravanUnos) {
                        logger.info("Odabrano je " + broj + "simptoma.");

                    }
                }

                izborSimptoma(unos, simptomi, s, broj, brojSimptoma);

                try {
                    provjeraSimptoma(s, bolesti, i);
                    istiSimptomi = false;
                } catch (BolestIstihSimptoma ex) {
                    logger.error("Greška! Već postoji bolest jednakih simptoma.", ex);
                    System.out.println(ex.getMessage());
                }
            }

            if (vrsta.equals(1)) {
                //bolesti.add(new Bolest(ime, s));
            }
            else {
                //bolesti.add(new Virus(ime, s));
            }
        }
    }*/

    /**
     * Služi izboru simptoma određene bolesti za koju unosimo podatke.
     * Bira se između svih ranije unesenih simptoma, zatim se provjerava
     * postoji li simptom pod tim brojem (izbor je od 1 do maksimalnog
     * broja simptoma), te ukoliko je izbor odgovarajući, simptom se uvrštava
     * u set simptoma koji će se proslijediti kao podatak za evidenciju
     * o bolesti.
     * @param unos služi za unos informacija preko tipkovnice
     * @param simptomi podatak o setu simptoma
     * @param s podatak o setu simptoma koji se određuju za bolest
     * @param broj predstavlja broj simptoma bolesti koji se unose
     * @param brojSimptoma podatak o ukupnom broju simptoma
     */
    /*public static void izborSimptoma(Scanner unos, Set<Simptom> simptomi, Set<Simptom> s, Integer broj, Integer brojSimptoma) {
        boolean ispravanUnos = false;
        Integer odabir = 0;
        Integer brojac = 0;
        for(int i = 0; i < broj; i++) {
            ispravanUnos = false;
            while (!ispravanUnos) {
                String str = "\nOdaberite " + (i + 1) + ". simptom:\n";
                brojac = 1;
                for(Simptom simptom : simptomi) {
                    str += brojac + ". " + simptom.getNaziv() + ", " + simptom.getVrijednost().getVrijednost() + "\n";
                    brojac++;
                }
                str += "Odabir (1 - " + brojSimptoma + "): ";
                odabir = unosBroja(unos, str);
                logger.info("Odabrani simptom je pod brojem " + odabir + ".");
                ispravanUnos = isIspravanUnos(odabir, 0, brojSimptoma);
                if (ispravanUnos) {
                    brojac = 1;
                    for(Simptom simptom : simptomi) {
                        if(brojac.equals(odabir)) {
                            logger.info("Odabran je simptom " + simptom.getNaziv() + ".");
                        }
                        brojac++;
                    }
                }
            }
            brojac = 1;
            for(Simptom simptom : simptomi) {
                if(brojac.equals(odabir)) {
                    s.add(simptom);
                }
                brojac++;
            }
        }
    }*/

}
