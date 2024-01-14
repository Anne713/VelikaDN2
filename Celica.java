import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class Celica extends JButton {
    final int vrstica, stolpec;
    public Celica(int vrstica, int stolpec) {
        this.vrstica = vrstica;
        this.stolpec = stolpec;
        setMargin(new Insets(0, 0, 0, 0));
        setFocusPainted(false);

        Font font = getFont();
        setFont(font.deriveFont(font.getSize()+3f));
        setForeground(Color.darkGray);
    }

    Color siva = new Color(240, 240, 240);
    void posodobi(int stevilka, StanjeCelice stanje) {
        setText(""+stevilka);
        switch (stanje) {
            case PORABLJENA -> {
                setEnabled(false);
                setBackground(siva);
                setBorder(new LineBorder(Color.orange, 2));
            }
            case DOVOLJENA -> {
                setEnabled(true);
                setBackground(Color.white);
                setBorder(new LineBorder(Color.gray, 2));
            }
            case NEDOVOLJENA -> {
                setEnabled(false);
                setBackground(siva);
                setBorder(new LineBorder(Color.LIGHT_GRAY, 2));
            }
            case NAMIGUJOCA -> {
                setEnabled(true);
                setBackground(new Color(150, 230, 255));
                setBorder(new LineBorder(Color.gray, 2));
            }
        }
    }
}
