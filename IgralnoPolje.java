import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class IgralnoPolje extends JPanel {

    interface Listener {
        void celicaKliknjena (int vrstica, int stolpec);
    }
    Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private final ActionListener celicaListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Celica celica = ((Celica) e.getSource());
            if (listener != null) {
                listener.celicaKliknjena(celica.vrstica, celica.stolpec);
            }
        }
    };

    int zadnjaVelikost = 0;
    Celica[][] celice;

    public IgralnoPolje () {
    }

    static final int S = 32;
    static final int D = S + 2;

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
