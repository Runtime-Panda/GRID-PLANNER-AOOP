package com.gridplanner;

import com.gridplanner.ui.FeederPanel;
import com.gridplanner.ui.SubstationPanel;
import com.gridplanner.ui.TransformerPanel;

import javax.swing.*;
import java.awt.*;

public class App {
    public static void main(String[] args) {
        try {
            // Force cross-platform look so custom colors render properly without OS overrides
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Global Swing dark theme overrides
        UIManager.put("TabbedPane.background", new Color(15, 23, 42));
        UIManager.put("TabbedPane.foreground", new Color(226, 232, 240));
        UIManager.put("TabbedPane.selected", new Color(30, 41, 59));
        UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
        UIManager.put("TabbedPane.tabsOverlapBorder", true);

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Electricity Grid Planner");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 600);
            frame.setLocationRelativeTo(null);
            frame.getContentPane().setBackground(new Color(15, 23, 42));

            SubstationPanel subPanel = new SubstationPanel();
            FeederPanel feederPanel = new FeederPanel();
            TransformerPanel transPanel = new TransformerPanel();

            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.setBackground(new Color(15, 23, 42));
            tabbedPane.setForeground(new Color(226, 232, 240));
            tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));

            tabbedPane.addTab("Substations", subPanel);
            tabbedPane.addTab("Feeders", feederPanel);
            tabbedPane.addTab("Transformers", transPanel);

            tabbedPane.addChangeListener(e -> {
                int selectedIndex = tabbedPane.getSelectedIndex();
                if (selectedIndex == 0) subPanel.loadTableData();
                else if (selectedIndex == 1) feederPanel.refreshAll();
                else if (selectedIndex == 2) transPanel.refreshAll();
            });

            frame.add(tabbedPane);
            frame.setVisible(true);
        });
    }
}