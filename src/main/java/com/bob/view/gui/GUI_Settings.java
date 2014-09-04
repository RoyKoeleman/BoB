package com.bob.view.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Roy on 07-08-14.
 */
public class GUI_Settings extends JFrame {
    public JPanel panel1;
    private JComboBox standaardTypeBand;
    private JButton opslaanButton;

    public GUI_Settings() {
        setContentPane(panel1);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);

        // Zet de waardes van de GUI zoals deze opgeslagen staan
        standaardTypeBand.setSelectedItem(Settings.getFromSettings("standaard_type_band"));

        opslaanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Settings.writeToSettings("standaard_type_band", standaardTypeBand.getSelectedItem().toString());
                setVisible(false);
            }
        });
    }
}
