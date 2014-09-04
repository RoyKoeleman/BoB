package com.bob.view.gui;

import javax.swing.*;

/**
 * Created by Roy on 09-08-14.
 */
public class GUI_List {
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JTable table1;
    private JList winterbandenList;

    public static void main(String[] args) {
        JFrame frame = new JFrame("GUI_List");
        frame.setContentPane(new GUI_List().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public GUI_List() {
    }
}
