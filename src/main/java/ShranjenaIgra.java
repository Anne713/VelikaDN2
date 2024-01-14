/**
 * Ima vse lastnosti modela igre, ki jih zelimo shraniti,
 * ter getterje in setterje zanje.
 */
public class ShranjenaIgra {
    private int stPotez;
    private int ciljnaVsota;
    private int velikost;
    private int trenutnaVsota;
    private int current, previous;
    private int[][] stevilke;
    private StanjeCelice[][] stanja;
    private boolean igreJeKonec;
    private int zadnjeStPotez;

    public int getStPotez() {
        return stPotez;
    }

    public void setStPotez(int stPotez) {
        this.stPotez = stPotez;
    }

    public int getCiljnaVsota() {
        return ciljnaVsota;
    }

    public void setCiljnaVsota(int ciljnaVsota) {
        this.ciljnaVsota = ciljnaVsota;
    }

    public int getVelikost() {
        return velikost;
    }

    public void setVelikost(int velikost) {
        this.velikost = velikost;
    }

    public int getTrenutnaVsota() {
        return trenutnaVsota;
    }

    public void setTrenutnaVsota(int trenutnaVsota) {
        this.trenutnaVsota = trenutnaVsota;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getPrevious() {
        return previous;
    }

    public void setPrevious(int previous) {
        this.previous = previous;
    }

    public int[][] getStevilke() {
        return stevilke;
    }

    public void setStevilke(int[][] stevilke) {
        this.stevilke = stevilke;
    }

    public StanjeCelice[][] getStanja() {
        return stanja;
    }

    public void setStanja(StanjeCelice[][] stanja) {
        this.stanja = stanja;
    }

    public boolean isIgreJeKonec() {
        return igreJeKonec;
    }

    public void setIgreJeKonec(boolean igreJeKonec) {
        this.igreJeKonec = igreJeKonec;
    }

    public int getZadnjeStPotez() {
        return zadnjeStPotez;
    }

    public void setZadnjeStPotez(int zadnjeStPotez) {
        this.zadnjeStPotez = zadnjeStPotez;
    }
}
