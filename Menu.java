import javax.swing.*;
import java.io.IOException;
import java.nio.file.Path;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Menu extends JMenuBar {
    ModelIgre modelIgre;
    int zacetnoStPotez;
    int zacetnaCiljnaVsota;
    int zacetnaVelikost;
    Tezavnost zacetnaTezavnost;
    IzbiraVelikosti[] velikosti;
    IzbiraTezavnosti[] tezavnosti;

    private final ActionListener velikostListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            IzbiraVelikosti gumbVelikost = ((IzbiraVelikosti) e.getSource());
            zacetnaVelikost = gumbVelikost.velikost;
            modelIgre.inicializacija(zacetnaVelikost, zacetnaTezavnost);
        }
    };

    private final ActionListener tezavnostListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            IzbiraTezavnosti gumbTezavnost = ((IzbiraTezavnosti) e.getSource());
            zacetnaTezavnost = gumbTezavnost.tezavnost;
            modelIgre.inicializacija(zacetnaVelikost, zacetnaTezavnost);
        }
    };

    class IzbiraVelikosti extends JRadioButton {
        int velikost;
        public IzbiraVelikosti (Integer velikost, String label, boolean jeIzbrana) {
            this.velikost = velikost;
            this.setText(label);
            this.setSelected(jeIzbrana);
            this.addActionListener(velikostListener);
        }
    }

    class IzbiraTezavnosti extends JRadioButton {
        Tezavnost tezavnost;
        public IzbiraTezavnosti (Tezavnost tezavnost, String label, boolean jeIzbrana) {
            this.tezavnost = tezavnost;
            this.setText(label);
            this.setSelected(jeIzbrana);
            this.addActionListener(tezavnostListener);
        }
    }

    Menu(ModelIgre model) {
        this.modelIgre = model;
        this.zacetnoStPotez = model.stPotez;
        this.zacetnaVelikost = model.velikost;
        this.zacetnaCiljnaVsota = model.ciljnaVsota;
        this.zacetnaTezavnost = Tezavnost.RANDOM;

        JMenu nastavitveIgre = new JMenu("Igra");
        JMenu nastaviZahtevnost = new JMenu("Zahtevnost");
        JMenu nastaviVelikost = new JMenu("Velikost");
        JMenu pomoc = new JMenu("Pomoč");
        add(nastavitveIgre);
        add(nastaviZahtevnost);
        add(nastaviVelikost);
        add(pomoc);

        velikosti = new IzbiraVelikosti[12];
        velikosti[0] = new IzbiraVelikosti(0, "naključna", true);
        nastaviVelikost.add(velikosti[0]);
        for (int i = 1; i < velikosti.length; i++) {
            velikosti[i] = new IzbiraVelikosti(i+9, (i+9)+"x"+(i+9), false);
            nastaviVelikost.add(velikosti[i]);
        }

        tezavnosti = new IzbiraTezavnosti[4];
        tezavnosti[0] = new IzbiraTezavnosti(Tezavnost.RANDOM, "naključna", true);
        tezavnosti[1] = new IzbiraTezavnosti(Tezavnost.LAHKA, "lahka", false);
        tezavnosti[2] = new IzbiraTezavnosti(Tezavnost.SREDNJA, "srednja", false);
        tezavnosti[3] = new IzbiraTezavnosti(Tezavnost.TEZKA, "težka", false);
        for (int i = 0; i < tezavnosti.length; i++) {
            nastaviZahtevnost.add(tezavnosti[i]);
        }

        JMenuItem novaIgra = new JMenuItem("Nova igra");
        ActionListener novaIgraListener = e -> modelIgre.inicializacija(zacetnaVelikost, zacetnaTezavnost);
        novaIgra.addActionListener(novaIgraListener);
        nastavitveIgre.add(novaIgra);

        JMenuItem istaIgra = new JMenuItem("Poskusi znova");
        ActionListener istaIgraListener = e -> modelIgre.inicIstoIgro();
        istaIgra.addActionListener(istaIgraListener);
        nastavitveIgre.add(istaIgra);

        JMenuItem customIgra = new JMenuItem("Nastavi svojo igro");
        ActionListener customIgraListener = e -> nastaviSvojoIgro(this);
        customIgra.addActionListener(customIgraListener);
        nastavitveIgre.add(customIgra);

        JMenuItem shraniIgro = new JMenuItem("Shrani igro");
        ActionListener shraniIgroListener = e -> {
            try {
                izberiPathInShrani(this);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        };
        shraniIgro.addActionListener(shraniIgroListener);
        nastavitveIgre.add(shraniIgro);

        JMenuItem naloziIgro = new JMenuItem("Naloži igro");
        naloziIgro.addActionListener(naloziIgroListener);
        nastavitveIgre.add(naloziIgro);
    }

    private void izberiPathInShrani(JMenuBar parentMenu) throws IOException {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Izberi ciljno mapo");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int izbranaMapa = fileChooser.showSaveDialog(parentMenu);

        if (izbranaMapa == JFileChooser.APPROVE_OPTION) {
            Path izbranDirectory = fileChooser.getSelectedFile().toPath();
            shraniModel(izbranDirectory);
        }
    }

    private void shraniModel(Path izbranPath) throws IOException {
        Shranjevanje shranjevanje = new Shranjevanje();
        try {
            ModelIgre model = modelIgre;
            shranjevanje.shrani(model, izbranPath.resolve("MoreOrLessGame"+rnd.nextInt(1,100000)+".dat"));
        } catch (IOException e) {
            throw e;
        }
    }

    private Path izberiPathInNalozi(JMenuItem parentMenuItem) throws IOException {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Izberi dokument");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        Path izbranDirectory;
        int izbranaMapa = fileChooser.showSaveDialog(parentMenuItem);

        if (izbranaMapa == JFileChooser.APPROVE_OPTION) {
            izbranDirectory = fileChooser.getSelectedFile().toPath();
        } else {
            izbranDirectory = null;
        }
        return izbranDirectory;
    }

    interface NalagalniListener {
        void naloziModel (ModelIgre model);
    }

    NalagalniListener nalagalniListener;

    public void setNaloziIgroListener(NalagalniListener nalagalniListener) {
        this.nalagalniListener = nalagalniListener;
    }

    private final ActionListener naloziIgroListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Shranjevanje shranjevanje = new Shranjevanje();
            JMenuItem parentMenuItem = (JMenuItem) e.getSource();
            ModelIgre taModelIgre;
            try {
                taModelIgre = shranjevanje.preberi(izberiPathInNalozi(parentMenuItem));
            } catch (IOException exception) {
                try {
                    throw exception;
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            if (nalagalniListener != null) {
                nalagalniListener.naloziModel(taModelIgre);
            }
        }
    };

    void nastaviSvojoIgro (JMenuBar parentMenu) {
        JTextField velikostField = new JTextField();
        JTextField stPotezField = new JTextField();
        JTextField ciljnaVsotaField = new JTextField();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Velikost:"));
        panel.add(velikostField);
        panel.add(new JLabel("Število potez:"));
        panel.add(stPotezField);
        panel.add(new JLabel("Ciljna vsota:"));
        panel.add(ciljnaVsotaField);

        int result = JOptionPane.showConfirmDialog(parentMenu, panel, "Nastavi svojo igro",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            int velikost = 0;
            int stPotez = 0;
            int ciljnaVsota = 0;

            if (velikostField.getText().isEmpty()) {
                if (zacetnaVelikost == 0) {
                    velikost = rnd.nextInt(10, 21);
                } else {
                    velikost = zacetnaVelikost;
                }
            } else if (!isInteger(velikostField.getText())) {
                JOptionPane.showMessageDialog(this, "Velikost polja mora biti celo število.", "Nepravilna velikost polja", JOptionPane.WARNING_MESSAGE);
            } else {
                int st = Integer.parseInt(velikostField.getText());
                if (st < 10 || st > 20) {
                    JOptionPane.showMessageDialog(this, "Velikost polja mora biti med 10 in 20.", "Nepravilna velikost polja", JOptionPane.WARNING_MESSAGE);
                } else {
                    velikost = zacetnaVelikost = st;
                }
            }

            if (stPotezField.getText().isEmpty()) {
                stPotez = rnd.nextInt(5, 50);
            } else if (!isInteger(stPotezField.getText())) {
                JOptionPane.showMessageDialog(this, "Število potez mora biti celo število.", "Nepravilno število potez", JOptionPane.WARNING_MESSAGE);
            } else {
                int st = Integer.parseInt(stPotezField.getText());
                if (st < 1 || st > velikost*velikost) {
                    JOptionPane.showMessageDialog(this, "Število potez mora biti večje od nič in manjše od števila vseh polj.", "Nepravilno število potez", JOptionPane.WARNING_MESSAGE);
                } else {
                    stPotez = st;
                }
            }

            if (ciljnaVsotaField.getText().isEmpty()) {
                ciljnaVsota = rnd.nextInt(stPotez, stPotez*9);
            } else if (!isInteger(ciljnaVsotaField.getText())) {
                JOptionPane.showMessageDialog(this, "Ciljna vsota mora biti celo število.", "Nepravilna ciljna vsota", JOptionPane.WARNING_MESSAGE);
            } else {
                int st = Integer.parseInt(ciljnaVsotaField.getText());
                if (st < stPotez || st > stPotez*9) {
                    JOptionPane.showMessageDialog(this, "Ciljna vsota mora biti dosegljiva znotraj danega igralnega polja.", "Nepravilna ciljna vsota", JOptionPane.WARNING_MESSAGE);
                } else {
                    ciljnaVsota = st;
                }
            }

            if (stPotez > 0 && ciljnaVsota > 0 && velikost > 0) {
                modelIgre.inicializacija(stPotez, ciljnaVsota, velikost);
            }
        }
    }

    private boolean isInteger (String string) {
        try {
            int x = Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    Random rnd = new Random();



    void posodobi() {
        for (int i = 0; i < velikosti.length; i++) {
            if (velikosti[i].velikost == zacetnaVelikost) {
                velikosti[i].setSelected(true);
            } else {
                velikosti[i].setSelected(false);
            }
        }
        for (int i = 0; i < tezavnosti.length; i++) {
            if (tezavnosti[i].tezavnost == zacetnaTezavnost) {
                tezavnosti[i].setSelected(true);
            } else {
                tezavnosti[i].setSelected(false);
            }
        }
    }
}
