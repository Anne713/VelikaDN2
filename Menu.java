import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.awt.event.ActionListener;
import java.util.Random;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Menu extends JMenuBar {
    ModelIgre modelIgre;
    int zacetnoStPotez;
    int zacetnaCiljnaVsota;
    int zacetnaVelikost;
    Tezavnost zacetnaTezavnost;
    IzbiraVelikosti[] velikosti;
    IzbiraTezavnosti[] tezavnosti;

    class IzbiraVelikosti extends JRadioButton {
        final int velikost;
        public IzbiraVelikosti (int velikost, String label, boolean jeIzbrana) {
            this.velikost = velikost;
            this.setText(label);
            this.setSelected(jeIzbrana);
            this.addActionListener(e -> {
                zacetnaVelikost = velikost;
                modelIgre.inicializacija(zacetnaVelikost, zacetnaTezavnost);
                posodobi();
            });
        }
    }
    class IzbiraTezavnosti extends JRadioButton {
        final Tezavnost tezavnost;
        public IzbiraTezavnosti (Tezavnost tezavnost, String label, boolean jeIzbrana) {
            this.tezavnost = tezavnost;
            this.setText(label);
            this.setSelected(jeIzbrana);
            this.addActionListener(e -> {
                zacetnaTezavnost = tezavnost;
                modelIgre.inicializacija(zacetnaVelikost, zacetnaTezavnost);
                posodobi();
            });
        }
    }
    Menu(ModelIgre model) {
        this.modelIgre = model;
        this.zacetnoStPotez = model.stPotez;
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
        for (IzbiraTezavnosti izbira : tezavnosti) {
            nastaviZahtevnost.add(izbira);
        }

        dodajMenuItem(nastavitveIgre, "Nova igra", e -> modelIgre.inicializacija(zacetnaVelikost, zacetnaTezavnost));
        dodajMenuItem(nastavitveIgre, "Poskusi znova", e -> modelIgre.inicIstoIgro());
        dodajMenuItem(nastavitveIgre, "Nastavi svojo igro", e -> nastaviSvojoIgro());
        nastavitveIgre.add(new JSeparator());
        dodajMenuItem(nastavitveIgre, "Shrani igro", e -> izberiPathInShrani());
        dodajMenuItem(nastavitveIgre, "Naloži igro", e -> izberiPathInNalozi());
        nastavitveIgre.add(new JSeparator());
        dodajMenuItem(nastavitveIgre, "Izhod", e -> System.exit(0));

        dodajMenuItem(pomoc, "Navodila igre", e -> prikaziNavodila());

        JCheckBox vklopiPomoc = new JCheckBox("Prikaži pomoč");
        vklopiPomoc.addActionListener(e -> modelIgre.togglePomoc());
        pomoc.add(vklopiPomoc);

    }

    void dodajMenuItem(JMenu parent, String label, ActionListener actionListener) {
        JMenuItem item = new JMenuItem(label);
        item.addActionListener(actionListener);
        parent.add(item);
    }

    private void izberiPathInShrani() {
        LocalDateTime cas = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String doberCas = cas.format(formatter);
        String imeDatoteke = "Igra_"+doberCas+".moreOrLess";

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Shrani kot");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(FILTER);
        fileChooser.setSelectedFile(new File(imeDatoteke));

        if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION)
            return;

        Path izbranFile = fileChooser.getSelectedFile().toPath();
        try {
            new Shranjevanje().shrani(modelIgre, izbranFile);
        } catch (IOException e) {
            prikaziNapako(e.getMessage());
        }
    }

    private void izberiPathInNalozi() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Izberi dokument");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(FILTER);

        if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
            return;

        Path izbranPath = fileChooser.getSelectedFile().toPath();
        try {
            new Shranjevanje().preberi(modelIgre, izbranPath);
        } catch (IOException e) {
            prikaziNapako("Prišlo je do napake pri branju datoteke.");
        }
    }

    void prikaziNapako(String sporocilo) {
        if (sporocilo == null)
            sporocilo = "Neznana napaka.";

        JOptionPane.showMessageDialog(this, sporocilo, "Napaka", JOptionPane.ERROR_MESSAGE);
    }

    void prikaziNavodila() {
        try (InputStream is = Menu.class.getResourceAsStream("navodila.txt")) {
            String vsebina = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            JOptionPane.showMessageDialog(this, vsebina, "Navodila", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void nastaviSvojoIgro() {
        String predvidenaVelikost;
        if (zacetnaVelikost == 0) {
            predvidenaVelikost = "";
        } else {
            predvidenaVelikost = Integer.toString(zacetnaVelikost);
        }
        JTextField velikostField = new JTextField(predvidenaVelikost);
        JTextField stPotezField = new JTextField(""+zacetnoStPotez);
        JTextField ciljnaVsotaField = new JTextField(""+zacetnaCiljnaVsota);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Velikost:"));
        panel.add(velikostField);
        panel.add(new JLabel("Število potez:"));
        panel.add(stPotezField);
        panel.add(new JLabel("Ciljna vsota:"));
        panel.add(ciljnaVsotaField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Nastavi svojo igro",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            int velikost = 0;
            int stPotez = 0;
            int ciljnaVsota = 0;
            boolean napakaPriVnosu = false;
            String napake = "";

            if (velikostField.getText().isEmpty()) {
                if (zacetnaVelikost == 0) {
                    velikost = rnd.nextInt(10, 21);
                } else {
                    velikost = zacetnaVelikost;
                }
            } else if (isNotInteger(velikostField.getText())) {
                napakaPriVnosu = true;
                napake += "Velikost polja mora biti celo število. \n";
            } else {
                int st = Integer.parseInt(velikostField.getText());
                if (st < 10 || st > 20) {
                    napakaPriVnosu = true;
                    napake += "Velikost polja mora biti med 10 in 20. \n";
                } else {
                    velikost = zacetnaVelikost = st;
                }
            }

            if (stPotezField.getText().isEmpty()) {
                stPotez = rnd.nextInt(5, 50);
            } else if (isNotInteger(stPotezField.getText())) {
                napakaPriVnosu = true;
                napake += "Število potez mora biti celo število. \n";
            } else {
                int st = Integer.parseInt(stPotezField.getText());
                if (st < 1 || st > velikost*velikost) {
                    napakaPriVnosu = true;
                    napake += "Število potez mora biti večje od nič in manjše od števila vseh polj. \n";
                } else {
                    stPotez = zacetnoStPotez = st;
                }
            }

            if (ciljnaVsotaField.getText().isEmpty()) {
                ciljnaVsota = rnd.nextInt(stPotez, stPotez*9);
            } else if (isNotInteger(ciljnaVsotaField.getText())) {
                napakaPriVnosu = true;
                napake += "Ciljna vsota mora biti celo število. \n";
            } else {
                int st = Integer.parseInt(ciljnaVsotaField.getText());
                if (st < stPotez || st > stPotez*9) {
                    napakaPriVnosu = true;
                    napake += "Ciljna vsota mora biti dosegljiva znotraj danega igralnega polja. \n";
                } else {
                    ciljnaVsota = zacetnaCiljnaVsota = st;
                }
            }

            if (napakaPriVnosu) {
                JOptionPane.showMessageDialog(this, napake, "Nepravilen vnos", JOptionPane.WARNING_MESSAGE);
                nastaviSvojoIgro();
            }

            if (stPotez > 0 && ciljnaVsota > 0 && velikost > 0) {
                modelIgre.inicializacija(stPotez, ciljnaVsota, velikost);
            }
        }
    }

    private boolean isNotInteger(String string) {
        try {
            Integer.parseInt(string);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    Random rnd = new Random();

    void posodobi() {
        for (IzbiraVelikosti izbira : velikosti) {
            izbira.setSelected(izbira.velikost == zacetnaVelikost);
        }
        for (IzbiraTezavnosti izbiraTezavnosti : tezavnosti) {
            izbiraTezavnosti.setSelected(izbiraTezavnosti.tezavnost == zacetnaTezavnost);
        }
    }

    public static final FileFilter FILTER = new FileFilter() {
        @Override
        public boolean accept(File file) {
            return file.isDirectory() || file.getName().endsWith(".moreOrLess");
        }

        @Override
        public String getDescription() {
            return "MoreOrLess files (*.moreOrLess)";
        }
    };
}
