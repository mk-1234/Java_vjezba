package main.java.hr.java.covidportal.niti;

import main.java.hr.java.covidportal.model.ImenovaniEntitet;
import main.java.hr.java.covidportal.model.Zupanija;

import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class NajviseZarazenihNit implements Runnable {

    @Override
    public void run() {

        while(true) {
            List<Zupanija> zupanije = null;
            try {
                zupanije = Executors.newSingleThreadExecutor().submit(new DohvatSvihZupanijaNit()).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            Double postotak = zupanije.stream()
                    .mapToDouble(z -> ((double) z.getBrojZarazenih() / z.getBrojStanovnika()) * 100)
                    .max()
                    .orElseThrow();

            String naziv = zupanije.stream()
                    .filter(z -> ((double) z.getBrojZarazenih() / z.getBrojStanovnika()) * 100 == postotak)
                    .map(ImenovaniEntitet::getNaziv)
                    .collect(Collectors.joining());

            DecimalFormat df = new DecimalFormat("###.##");
            System.out.println(naziv + " ima najveći postotak zaraženih: " + df.format(postotak) + "%");

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
