import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.Random;

public class ModelIgre implements Serializable {
    int stPotez;
    int ciljnaVsota;
    int velikost;
    int trenutnaVsota;
    int current, previous;
    int[][] stevilke;
    StanjeCelice[][] stanja;
    boolean igreJeKonec;
    transient Random rnd = new Random();
    int zadnjeStPotez;

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
        if (listener != null) {
            listener.modelSpremenjen();
        }
    }

    void inicializacija (Integer velikost, Tezavnost tezavnost) {
        if (velikost == null || velikost == 0) velikost = rnd.nextInt(10, 21);
        if (tezavnost == null) tezavnost = Tezavnost.RANDOM;

        switch (tezavnost) {
            case RANDOM -> {
                stPotez = rnd.nextInt(5, 50);
                ciljnaVsota = rnd.nextInt(stPotez, stPotez*9);
            }
            case LAHKA -> {
                stPotez = rnd.nextInt(3, 12);
                ciljnaVsota = rnd.nextInt(stPotez, stPotez*3);
            }
            case SREDNJA -> {
                stPotez = rnd.nextInt(10, 32);
                ciljnaVsota = rnd.nextInt(stPotez*3, stPotez*7);
            }
            case TEZKA -> {
                stPotez = rnd.nextInt(27, 50);
                ciljnaVsota = rnd.nextInt(stPotez*6, stPotez*9);
            }
        }
        inicializacija(stPotez, ciljnaVsota, velikost);
    }

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
        if (listener != null) {
            listener.modelSpremenjen();
        }
    }

    void inicStaroIgro (ModelIgre starModel) {
        stPotez = starModel.stPotez;
        ciljnaVsota = starModel.ciljnaVsota;
        velikost = starModel.velikost;
        trenutnaVsota = starModel.trenutnaVsota;
        current = starModel.current;
        previous = starModel.previous;
        stevilke = starModel.stevilke;
        stanja = starModel.stanja;
        igreJeKonec = starModel.igreJeKonec;
        zadnjeStPotez = starModel.zadnjeStPotez;

        if (listener != null) {
            listener.modelSpremenjen();
        }
    }

    void poteza (int vrstica, int stolpec) {
        previous = current;
        current = stevilke[vrstica][stolpec];
        trenutnaVsota += current;
        stanja[vrstica][stolpec] = StanjeCelice.PORABLJENA;
        boolean nekajJeDovoljeno = false;
        for (int i = 0; i < velikost; i++) {
            for (int j = 0; j < velikost; j++) {
                if (stanja[i][j] != StanjeCelice.PORABLJENA) {
                    if (dovoljena(i) && dovoljena(j)) {
                        stanja[i][j] = StanjeCelice.DOVOLJENA;
                        nekajJeDovoljeno = true;
                    } else {
                        stanja[i][j] = StanjeCelice.NEDOVOLJENA;
                    }
                }
            }
        }
        stPotez--;
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
        if (listener != null) {
            listener.modelSpremenjen();
        }
    }

    private boolean dovoljena (int i) {
        i++;
        return i % previous == 0 || i % current == 0;
    }

    interface ModelListener {
        void modelSpremenjen ();
    }

    transient ModelListener listener;

    public void setListener(ModelListener listener) {
        this.listener = listener;
    }
}
