import java.util.Random;

/**
 * Vsebuje vso logiko igre.
 */

public class ModelIgre {
    int stPotez;
    int ciljnaVsota;
    int velikost;
    int trenutnaVsota;
    int current, previous;
    int[][] stevilke;
    StanjeCelice[][] stanja;
    boolean igreJeKonec;
    int zadnjeStPotez;
    boolean pomocVklopljena;
    Random rnd = new Random();

    /**
     * Ustvari igro z danimi parametri (znova).
     *
     * @param stPotez stevilo potez do konca igre
     * @param ciljnaVsota sestevek, ki se mu zeli priblizati uporabnik
     * @param velikost 10-20, velikost kvadratnega igralnega polja
     */

    void inicializacija (int stPotez, int ciljnaVsota, int velikost) {
        this.stPotez = zadnjeStPotez = stPotez;
        this.ciljnaVsota = ciljnaVsota;
        this.velikost = velikost;
        trenutnaVsota = 0;
        current = 1000;
        previous = 1;
        igreJeKonec = false;

        stevilke = new int[velikost][velikost];
        stanja = new StanjeCelice[velikost][velikost];
        for (int i = 0; i < velikost; i++) {
            for (int j = 0; j < velikost; j++) {
                stevilke[i][j] = rnd.nextInt(1,10);
                stanja[i][j] = StanjeCelice.DOVOLJENA;
            }
        }
        kliciListenerja();
    }

    /**
     * Ustvari igro glede na zeljeno velikost in zahtevnost.
     *
     * @param velikost 10-20, 0 ali null, velikost igralnega polja
     * @param tezavnost lahko null za random
     */
    void inicializacija (Integer velikost, Tezavnost tezavnost) {
        if (velikost == null || velikost == 0) velikost = rnd.nextInt(10, 21);
        if (tezavnost == null) tezavnost = Tezavnost.RANDOM;

        switch (tezavnost) {
            case RANDOM -> {
                stPotez = rnd.nextInt((velikost*velikost/9)-3, (velikost*velikost/9)+24);
                ciljnaVsota = rnd.nextInt(stPotez*3, stPotez*6);
            }
            case LAHKA -> {
                stPotez = rnd.nextInt((velikost*velikost/9)-3, (velikost*velikost/9)+3);
                ciljnaVsota = rnd.nextInt(stPotez*3, stPotez*5);
            }
            case SREDNJA -> {
                stPotez = rnd.nextInt((velikost*velikost/9)+4, (velikost*velikost/9)+12);
                ciljnaVsota = rnd.nextInt(stPotez*4, stPotez*6);
            }
            case TEZKA -> {
                stPotez = rnd.nextInt((velikost*velikost/9)+13, (velikost*velikost/9)+24);
                ciljnaVsota = rnd.nextInt(stPotez*5, stPotez*6);
            }
        }
        inicializacija(stPotez, ciljnaVsota, velikost);
    }

    /**
     * Uporabi shranjene vrednosti, da inicializira igro z istimi stevilkami in parametri, kot je bila zadnja.
     */
    void inicIstoIgro () {
        this.stPotez = zadnjeStPotez;
        trenutnaVsota = 0;
        current = 1000;
        previous = 1;
        igreJeKonec = false;

        stanja = new StanjeCelice[velikost][velikost];
        for (int i = 0; i < velikost; i++) {
            for (int j = 0; j < velikost; j++) {
                stanja[i][j] = StanjeCelice.DOVOLJENA;
            }
        }
        kliciListenerja();
    }

    /**
     * Izvede izbrano potezo.
     *
     * @param vrstica
     * @param stolpec
     */
    void poteza (int vrstica, int stolpec) {
        previous = current;
        current = stevilke[vrstica][stolpec];
        trenutnaVsota += current;
        stanja[vrstica][stolpec] = StanjeCelice.PORABLJENA;
        stPotez--;

        boolean nekajJeDovoljeno = posodobiStanjaCelic(previous, current);

        igreJeKonec = (stPotez < 1) || (!nekajJeDovoljeno);
        if (igreJeKonec) {
            for (int i = 0; i < velikost; i++) {
                for (int j = 0; j < velikost; j++) {
                    if (stanja[i][j] != StanjeCelice.PORABLJENA) {
                        stanja[i][j] = StanjeCelice.NEDOVOLJENA;
                    }
                }
            }
        }
        kliciListenerja();
    }

    private boolean posodobiStanjaCelic(int previous, int current) {
        boolean nekajJeDovoljeno = false;
        for (int i = 0; i < velikost; i++) {
            for (int j = 0; j < velikost; j++) {
                if (stanja[i][j] != StanjeCelice.PORABLJENA) {
                    if (dovoljena(i, previous, current) && dovoljena(j, previous, current)) {
                        stanja[i][j] = StanjeCelice.DOVOLJENA;
                        nekajJeDovoljeno = true;
                    } else {
                        stanja[i][j] = StanjeCelice.NEDOVOLJENA;
                    }
                }
            }
        }
        return nekajJeDovoljeno;
    }

    void togglePomoc() {
        pomocVklopljena = !pomocVklopljena;
        if (!pomocVklopljena)
            skrijNamig();
    }

    /**
     * Ce je vklopljena pomoc, spremeni stanja celic, tako da uporabnik vidi,
     * kaksno bo stanje po potezi, preden naredi to potezo.
     *
     * @param vrstica
     * @param stolpec
     */
    void prikaziNamig(int vrstica, int stolpec) {
        if (!pomocVklopljena || stanja[vrstica][stolpec] != StanjeCelice.DOVOLJENA) {
            return;
        }

        posodobiStanjaCelic(current, stevilke[vrstica][stolpec]);
        stanja[vrstica][stolpec] = StanjeCelice.NAMIGUJOCA;
        kliciListenerja();
    }

    /**
     * Neha kazati prihodnje stanje.
     */
    void skrijNamig() {
        if (igreJeKonec)
            return;
        posodobiStanjaCelic(previous, current);
        kliciListenerja();
    }

    private boolean dovoljena (int i, int previous, int current) {
        i++;
        return i % previous == 0 || i % current == 0;
    }

    public interface ModelListener {
        void modelSpremenjen ();
    }
    ModelListener listener;

    private void kliciListenerja() {
        if (listener != null) {
            listener.modelSpremenjen();
        }
    }

    public void setListener(ModelListener listener) {
        this.listener = listener;
    }

    /**
     * Pripravi verzijo igre za shranjevanje s trenutnim stanjem modela.
     *
     * @return
     */
    ShranjenaIgra verzijaZaShranjevanje () {
        skrijNamig();
        ShranjenaIgra igra = new ShranjenaIgra();

        igra.setStPotez(stPotez);
        igra.setCiljnaVsota(ciljnaVsota);
        igra.setVelikost(velikost);
        igra.setTrenutnaVsota(trenutnaVsota);
        igra.setCurrent(current);
        igra.setPrevious(previous);
        igra.setStevilke(stevilke);
        igra.setStanja(stanja);
        igra.setIgreJeKonec(igreJeKonec);
        igra.setZadnjeStPotez(zadnjeStPotez);

        return igra;
    }

    /**
     * Nastavi modelu stanje prebrane igre.
     *
     * @param igra
     */
    void preberi(ShranjenaIgra igra) {
        stPotez = igra.getStPotez();
        ciljnaVsota = igra.getCiljnaVsota();
        velikost = igra.getVelikost();
        trenutnaVsota = igra.getTrenutnaVsota();
        current = igra.getCurrent();
        previous = igra.getPrevious();
        stevilke = igra.getStevilke();
        stanja = igra.getStanja();
        igreJeKonec = igra.isIgreJeKonec();
        zadnjeStPotez = igra.getZadnjeStPotez();

        kliciListenerja();
    }
}