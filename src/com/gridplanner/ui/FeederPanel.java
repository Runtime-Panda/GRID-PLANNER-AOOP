package com.gridplanner.ui;

import com.gridplanner.dao.FeederDAO;
import com.gridplanner.model.Feeder;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class FeederPanel extends JPanel {
    private final FeederDAO dao = new FeederDAO();
    private final JTextField subIdField = new JTextField(12);
    private final JTextField codeField = new JTextField(12);
    private final JTextField typeField = new JTextField(12);
    private final JTextField maxAmpField = new JTextField(12);
    private final JTextField lengthField = new JTextField(12);
    private final DefaultTableModel tableModel;

    public FeederPanel() {
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Add Feeder"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addFormRow(formPanel, gbc, 0, "Substation ID:", subIdField);
        addFormRow(formPanel, gbc, 1, "Feeder Code:", codeField);
        addFormRow(formPanel, gbc, 2, "Conductor Type:", typeField);
        addFormRow(formPanel, gbc, 3, "Max Amp:", maxAmpField);
        addFormRow(formPanel, gbc, 4, "Length (km):", lengthField);

        JButton saveBtn = new JButton("Save Feeder");
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        formPanel.add(saveBtn, gbc);

        saveBtn.addActionListener(e -> saveFeeder());

        tableModel = new DefaultTableModel(new String[]{"ID", "Substation ID", "Code", "Type", "Max Amp", "Length (km)"}, 0);
        JTable table = new JTable(tableModel);

        add(formPanel, BorderLayout.WEST);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadTableData();
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String label, JTextField field) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void saveFeeder() {
        try {
            int subId = Integer.parseInt(subIdField.getText().trim());
            String code = codeField.getText().trim();
            String type = typeField.getText().trim();
            double maxAmp = Double.parseDouble(maxAmpField.getText().trim());
            double len = Double.parseDouble(lengthField.getText().trim());

            Feeder f = new Feeder(subId, code, type, maxAmp, len);
            if (dao.addFeeder(f)) {
                JOptionPane.showMessageDialog(this, "Feeder Saved!");
                loadTableData();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        try {
            List<Feeder> list = dao.getAllFeeders();
            for (Feeder f : list) {
                tableModel.addRow(new Object[]{f.getId(), f.getSubstationId(), f.getFeederCode(), f.getConductorType(), f.getMaxCurrentAmp(), f.getLengthKm()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading feeders: " + e.getMessage());
        }
    }
}