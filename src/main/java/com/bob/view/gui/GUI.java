package com.bob.view.gui;

import com.bob.controller.Helper;
import com.bob.entities.Auto;
import com.bob.controller.DBHandler;
import com.bob.entities.Band;
import com.bob.entities.Klant;
import com.bob.entities.SetWielen;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

/**
 * Created by Roy on 05-08-14.
 */
public class GUI {
    private JPanel panel1;
    private JTextField achternaam;
    private JTextField kentekenTextField;
    private JTextField maatBand;
    private JComboBox velgType;
    private JTextField merkBand;
    private JComboBox TypeSet;
    private JTextField tussenvoegsel;
    private JTextField voornaam;
    private JTextField typeBand;
    private JCheckBox wieldoppen;
    private JCheckBox wielbouten;
    private JTextField pRV;
    private JTextField pLV;
    private JTextField pLA;
    private JTextField pRA;
    private JLabel lvOutput;
    private JLabel rvOutput;
    private JLabel laOutput;
    private JLabel raOutput;
    private JTextArea opmerkingen;
    private JButton opslaanButton;
    private JTextField merkAuto;
    private JTextField typeAuto;
    private boolean uppercase;
    LinkedList<JTextField> verplichteVelden = new LinkedList<JTextField>();


    public static void main(String[] args) {
        new DBHandler();

        JFrame frame = new JFrame("GUI");
        frame.setContentPane(new GUI().panel1);
        frame.setJMenuBar(initMenuBar());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private static JMenuBar initMenuBar() {
        JMenuBar menuBar;
        JMenu menu;
        JMenuItem menuItem;

        menuBar = new JMenuBar();
        menu = new JMenu("Bestand");
        menu.setMnemonic(KeyEvent.VK_A);
        menuBar.add(menu);

        //a group of JMenuItems
        menuItem = new JMenuItem("Instellingen");
        menu.add(menuItem);

        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new JFrame("Settings");
                new GUI_Settings();
            }
        });

        return menuBar;
    }

    private void initTextFields() {
        verplichteVelden.add(achternaam);
        verplichteVelden.add(kentekenTextField);
        verplichteVelden.add(merkBand);
        verplichteVelden.add(maatBand);
        verplichteVelden.add(pRV);
        verplichteVelden.add(pLV);
        verplichteVelden.add(pRA);
        verplichteVelden.add(pLA);
        verplichteVelden.add(merkAuto);
        verplichteVelden.add(typeAuto);
    }

    public GUI() {
        initTextFields();

        // 8 Teken limiet op het kenteken invoerveld
        kentekenTextField.setDocument(new JTextFieldLimit(8));

        // Kenteken field changed
        kentekenTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                emptyAuto();
                displayAuto();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                emptyAuto();
                displayAuto();
            }

            @Override
            public void changedUpdate(DocumentEvent e) { }

            private void displayAuto() {
                final String inputText = kentekenTextField.getText();

                if(inputText.length() < 8) uppercase = false;

                if(inputText.length() == 6 && inputText.matches("^[a-zA-Z0-9]+$")) {
                    Runnable setKenteken = new Runnable() {
                        @Override
                        public void run() {
                            kentekenTextField.setText(Helper.formatKenteken(inputText));
                        }
                    };

                    SwingUtilities.invokeLater(setKenteken);

                }

                if(inputText.length() == 8) {
                    Runnable setKenteken = new Runnable() {
                        @Override
                        public void run() {
                            if(!uppercase) {
                                kentekenTextField.setText(kentekenTextField.getText().toUpperCase());
                                uppercase = true;
                            }
                        }
                    };

                    SwingUtilities.invokeLater(setKenteken);

                    Auto auto = DBHandler.getAutoByKenteken(inputText);

                    if(auto != null) {
                        // Geef het merk en type van de auto weer
                        merkAuto.setText(auto.getMerk());
                        typeAuto.setText(auto.getType());

                        // Vul de klantgegevens in die bij de auto horen
                        if(auto.getEigenaar() != null) {
                            Klant klant = auto.getEigenaar();
                            voornaam.setText(klant.getVoornaam());
                            tussenvoegsel.setText(klant.getTussenvoegsel());
                            achternaam.setText(klant.getAchternaam());
                        }

                        // Haal de laatste set wielen van de auto op, zomer of winter
                        SetWielen wielen = DBHandler.getSetWielenByAuto(auto, isWinterband());
                        Band band = null;
                        if(wielen != null) band = wielen.getBand();

                        if(band != null) {
                            // Verander LM naar Lichtmetaal omdat dit zo in de GUI staat
                            String velgtype = (wielen.getVelg_type().equals("LM")) ? "Lichtmetaal" : wielen.getVelg_type();

                            // Zet de waardes van de velden naar de opgehaalde data
                            velgType.setSelectedItem(velgtype);
                            merkBand.setText(band.getMerk());
                            typeBand.setText(band.getType());
                            maatBand.setText(band.getMaat());
                            wielbouten.setSelected(wielen.isWielbouten());
                            wieldoppen.setSelected(wielen.isWieldoppen());

                            // Format de datum van de vorige wissel en geef deze weer bij de millimetrage
                            String datum = new SimpleDateFormat("dd-MM-yyyy").format(wielen.getDatum());
                            lvOutput.setText("was " + wielen.getProfiel_lv() + "mm op " + datum);
                            rvOutput.setText("was " + wielen.getProfiel_rv() + "mm op " + datum);
                            laOutput.setText("was " + wielen.getProfiel_la() + "mm op " + datum);
                            raOutput.setText("was " + wielen.getProfiel_ra() + "mm op " + datum);
                        }

                    }
                }
            }

            private void emptyAuto() {
                merkAuto.setText("");
                typeAuto.setText("");
                lvOutput.setText("");
                laOutput.setText("");
                rvOutput.setText("");
                raOutput.setText("");
            }
        });

        // Opslaan button pressed
        opslaanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Wanneer de verplichte velden niet ingevuld zijn, stop.
                if(!checkVerplichteVelden()) return;

                // Sla de set op en geef een melding weer
                if(saveSetWielen()) {
                    showAlert("Set opgeslagen.", false);
                    resetForm();
                } else {
                    showAlert("Er is iets mis gegaan bij het opslaan van de gegevens.", true);
                }
            }
        });

        // Band type veranderd
        TypeSet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                kentekenTextField.setText(kentekenTextField.getText());
            }
        });

        // Verwerk settings
        opmerkingen.setLineWrap(true);
        TypeSet.setSelectedItem(Settings.getFromSettings("standaard_type_band"));
    }

    class JTextFieldLimit extends PlainDocument {
        // Code om een limiet van een x aantal tekens op een JTextField te zetten
        private int limit;
        JTextFieldLimit(int limit) {
            super();
            this.limit = limit;
        }

        public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
            if (str == null)
                return;

            if ((getLength() + str.length()) <= limit) {
                super.insertString(offset, str, attr);
            }
        }
    }

    private boolean isWinterband() {
        return TypeSet.getSelectedItem().equals("Winterbanden");
    }

    private boolean saveSetWielen() {
        Klant klant = DBHandler.createOrGetKlant(achternaam.getText(), tussenvoegsel.getText(), voornaam.getText());
        Auto auto = DBHandler.createOrGetAuto(kentekenTextField.getText(), merkAuto.getText(), typeAuto.getText(), klant);
        Band band = DBHandler.createOrGetBand(merkBand.getText(), typeBand.getText(), maatBand.getText(), isWinterband());
        String velg_type = velgType.getSelectedItem().toString().equals("Lichtmetaal") ? "LM" : velgType.getSelectedItem().toString();

        SetWielen setWielen = new SetWielen();
        setWielen.setKlant(klant);
        setWielen.setAuto(auto);
        setWielen.setBand(band);
        setWielen.setVelg_type(velg_type);
        setWielen.setWieldoppen(wieldoppen.isSelected());
        setWielen.setWielbouten(wielbouten.isSelected());
        setWielen.setProfiel_lv(Double.parseDouble(pLV.getText()));
        setWielen.setProfiel_rv(Double.parseDouble(pRV.getText()));
        setWielen.setProfiel_la(Double.parseDouble(pLA.getText()));
        setWielen.setProfiel_ra(Double.parseDouble(pRA.getText()));
        setWielen.setOpmerking(opmerkingen.getText());

        return DBHandler.saveSetWielen(klant, auto, band, setWielen);
    }

    private void showAlert(String tekst, boolean error) {
        if(error) {
            JOptionPane.showMessageDialog(null,
                    tekst,
                    "Foutmelding",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, tekst);
        }
    }

    private void resetForm() {
        TypeSet.setSelectedItem("Zomerbanden");
        kentekenTextField.setText("");
        merkAuto.setText("");
        typeAuto.setText("");
        achternaam.setText("");
        tussenvoegsel.setText("");
        voornaam.setText("");
        merkBand.setText("");
        typeBand.setText("");
        maatBand.setText("");
        velgType.setSelectedItem("Staal");
        wieldoppen.setSelected(false);
        wielbouten.setSelected(false);
        pLV.setText("");
        pRV.setText("");
        pLA.setText("");
        pRA.setText("");
        lvOutput.setText("");
        rvOutput.setText("");
        laOutput.setText("");
        raOutput.setText("");
        opmerkingen.setText("");
    }

    private boolean checkVerplichteVelden() {
        for(JTextField textField : verplichteVelden) {
            if(textField.getText().equals("")) {
                showAlert("Gegevens niet volledig ingevuld.", true);
                return false;
            }
        }

        return true;
    }
}