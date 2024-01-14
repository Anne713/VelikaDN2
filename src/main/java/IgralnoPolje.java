import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Mreza vseh celic v igri. Celicam doda listenerja.
 */
public class IgralnoPolje extends JPanel {
    int zadnjaVelikost = 0;
    Celica[][] celice;
    Listener listener;

    public IgralnoPolje () {
    }

    public interface Listener {
        void celicaKliknjena(int vrstica, int stolpec);
        void celicaPodMisko(int vrstica, int stolpec);
        void celicaNiVecPodMisko(int vrstica, int stolpec);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    /**
     * ??
     */
    private final ActionListener celicaListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Celica celica = ((Celica) e.getSource());
            if (listener != null) {
                listener.celicaKliknjena(celica.vrstica, celica.stolpec);
            }
        }
    };

    /**
     * ??
     */
    private final MouseListener miskaListener = new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            Celica celica = ((Celica) e.getSource());
            if (listener != null) {
                listener.celicaPodMisko(celica.vrstica, celica.stolpec);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            Celica celica = ((Celica) e.getSource());
            if (listener != null) {
                listener.celicaNiVecPodMisko(celica.vrstica, celica.stolpec);
            }
        }
    };

    static final int S = 32;
    static final int D = S + 2;

    /**
     * Izrise mrezo celic glede na njihovo trenutno stanje, jim doda listenerja,
     * in izrise oznake vrstic in stolpcev.
     * Potrebno poklicati vsakic, ko se spremenijo stanja celic.
     *
     * @param model
     */

    void posodobi(ModelIgre model) {
        if (model.velikost != zadnjaVelikost) {
            int n = zadnjaVelikost = model.velikost;

            removeAll();
            setLayout(null);

            int offset = (41 - 2*n) * D / 4;

            celice = new Celica[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    Celica celica = celice[i][j] = new Celica(i, j);
                    this.add(celica);
                    celica.setBounds(offset + (j+1) * D, offset + (i+1) * D, S, S);
                    celica.addActionListener(celicaListener);
                    celica.addMouseListener(miskaListener);
                }
            }

            setPreferredSize(new Dimension((n+2) * D, (n+2) * D));

            for (int i = 0; i < n; i++) {
                JLabel labelZgoraj = new JLabel(""+(1+i));
                labelZgoraj.setHorizontalAlignment(SwingConstants.CENTER);
                labelZgoraj.setVerticalAlignment(SwingConstants.BOTTOM);
                this.add(labelZgoraj);
                labelZgoraj.setBounds(offset + (i+1) * D, offset, S, S);

                JLabel labelLevo = new JLabel(""+(1+i));
                labelLevo.setHorizontalAlignment(SwingConstants.RIGHT);
                labelLevo.setVerticalAlignment(SwingConstants.CENTER);
                this.add(labelLevo);
                labelLevo.setBounds(offset, offset + (i+1) * D, S, S);
            }
        }
        for (int i = 0; i < zadnjaVelikost; i++) {
            for (int j = 0; j < zadnjaVelikost; j++) {
                celice[i][j].posodobi(model.stevilke[i][j], model.stanja[i][j]);
            }
        }
    }
}
