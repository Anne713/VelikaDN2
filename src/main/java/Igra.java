import javax.swing.*;
import java.awt.*;

/**
 * Okno igre More or less less is more.
 * Poseduje ModelIgre, Menu in PrikazIgre.
 */
public class Igra extends JFrame {

    public ModelIgre modelIgre;

    /**
     * Konstruktor nastavi okno in ustvari ModelIgre, PrikazIgre in Menu.
     */

    public Igra () {
        setTitle("More or less less is more");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        PrikazIgre prikazIgre = new PrikazIgre();
        add(prikazIgre, BorderLayout.CENTER);

        modelIgre = new ModelIgre();
        modelIgre.inicializacija(null, null);

        Menu menu = new Menu(modelIgre);
        setJMenuBar(menu);

        prikazIgre.posodobi(modelIgre);
        modelIgre.setListener(() -> prikazIgre.posodobi(modelIgre));

        prikazIgre.polje.setListener(new IgralnoPolje.Listener() {
            @Override
            public void celicaKliknjena(int vrstica, int stolpec) {
                modelIgre.poteza(vrstica, stolpec);
                if (modelIgre.igreJeKonec) {
                    String sporocilo = sporociRezultat(Math.abs(modelIgre.trenutnaVsota-modelIgre.ciljnaVsota));
                    JOptionPane.showMessageDialog(Igra.this, sporocilo, "Konec igre", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            @Override
            public void celicaPodMisko(int vrstica, int stolpec) {
                modelIgre.prikaziNamig(vrstica, stolpec);
            }
            @Override
            public void celicaNiVecPodMisko(int vrstica, int stolpec) {
                modelIgre.skrijNamig();
            }
        });

        this.validate();
        this.pack();
        this.setSize(770, 845);
        this.setVisible(true);
    }

    /**
     * Pripravi sporocilo, ki se izpise uporabniku ob koncu igre.
     *
     * @param rezultat
     * @return sporocilo, ki je razlicno navduseno glede na rezultat
     */
    String sporociRezultat (int rezultat) {
        String sporocilo;
        if (rezultat == 0) {
            sporocilo = "Zmagaš! Bravo:)";
        } else if (rezultat > 0 && rezultat < 10) {
            sporocilo = "Tvoj rezultat je "+rezultat+", bravo!";
        } else {
            sporocilo = "Tvoj rezultat je "+rezultat+" ...";
        }
        return sporocilo;
    }

    public static void main(String[] args) {
        new Igra();
    }
}
