import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Igra extends JFrame {

    public ModelIgre modelIgre;

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

        //nekaj najgrsega na svetu, theres gotta be a better way
        menu.setNaloziIgroListener(model -> {
            modelIgre = model;
            modelIgre.rnd = new Random();
            prikazIgre.posodobi(modelIgre);

            Menu menuNov = new Menu(modelIgre);
            setJMenuBar(menuNov);
            menuNov.posodobi();

            modelIgre.setListener(() -> {
                prikazIgre.posodobi(modelIgre);
                menuNov.posodobi();
            });

            prikazIgre.polje.setListener((vrstica, stolpec) -> {
                modelIgre.poteza(vrstica, stolpec);
                if (modelIgre.igreJeKonec) {
                    String sporocilo = sporociRezultat(Math.abs(modelIgre.trenutnaVsota-modelIgre.ciljnaVsota));
                    JOptionPane.showMessageDialog(this, sporocilo, "Konec igre", JOptionPane.INFORMATION_MESSAGE);
                }
            });
        });
        //plus ne dela vec kot enkrat tkoda ja, tezave

        modelIgre.setListener(() -> {
            prikazIgre.posodobi(modelIgre);
            menu.posodobi();
        });

        prikazIgre.posodobi(modelIgre);

        prikazIgre.polje.setListener((vrstica, stolpec) -> {
            modelIgre.poteza(vrstica, stolpec);
            if (modelIgre.igreJeKonec) {
                String sporocilo = sporociRezultat(Math.abs(modelIgre.trenutnaVsota-modelIgre.ciljnaVsota));
                JOptionPane.showMessageDialog(this, sporocilo, "Konec igre", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        this.validate();
        this.pack();
        this.setSize(770, 845);
        this.setVisible(true);
    }

    String sporociRezultat (int rezultat) {
        String sporocilo;
        if (rezultat == 0) {
            sporocilo = "Zmagal si! Bravo:)";
        } else if (rezultat > 0 && rezultat < 10) {
            sporocilo = "Tvoj rezultat je "+rezultat+", bravo!";
        } else {
            sporocilo = "Tvoj rezultat je "+rezultat+"...";
        }
        return sporocilo;
    }

    public static void main(String[] args) {
        Igra taIgra = new Igra();
    }
}
