import javax.swing.*;
import java.awt.*;

/**
 * Igralnemu polju doda izpis stevila potez, trenutne in ciljne vsote.
 */
public class PrikazIgre extends JPanel {
    JLabel stPotez = new JLabel("Število potez: ?");
    JLabel vsote = new JLabel("Vsota: ?     Cilj: ?");
    IgralnoPolje polje = new IgralnoPolje();

    PrikazIgre() {
        setLayout(new BorderLayout());
        JPanel zgoraj = new JPanel(new BorderLayout());
        stPotez.setHorizontalAlignment(SwingConstants.CENTER);
        vsote.setHorizontalAlignment(SwingConstants.CENTER);
        zgoraj.add(stPotez, BorderLayout.SOUTH);
        zgoraj.add(vsote, BorderLayout.NORTH);
        add(zgoraj, BorderLayout.NORTH);

        add(polje, BorderLayout.CENTER);
    }

    /**
     * Poklicati vsakic, ko se spremeni model.
     *
     * @param model
     */
    void posodobi(ModelIgre model) {
        polje.posodobi(model);
        stPotez.setText("Število potez: "+model.stPotez);
        vsote.setText("Vsota: "+model.trenutnaVsota+"     Cilj: "+model.ciljnaVsota);
        this.repaint();
    }
}
