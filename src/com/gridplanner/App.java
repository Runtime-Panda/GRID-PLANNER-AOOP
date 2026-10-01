package com.gridplanner;

import com.gridplanner.ui.FeederPanel;
import com.gridplanner.ui.SubstationPanel;
import com.gridplanner.ui.TransformerPanel;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Electricity Grid Planner - Phase 1");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(950, 550);
            frame.setLocationRelativeTo(null);

            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.addTab("Substations", new SubstationPanel());
            tabbedPane.addTab("Feeders", new FeederPanel());
            tabbedPane.addTab("Transformers", new TransformerPanel());

            frame.add(tabbedPane);
            frame.setVisible(true);
        });
    }
}