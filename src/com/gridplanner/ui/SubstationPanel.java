package com.gridplanner.ui;

import com.gridplanner.dao.SubstationDAO;
import com.gridplanner.model.Substation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class SubstationPanel extends JPanel {
    private final SubstationDAO dao = new SubstationDAO();
    private final JTextField nameField = new JTextField(12);
    private final JTextField capacityField = new JTextField(12);
    private final JTextField priVoltField = new JTextField(12);
    private final JTextField secVoltField = new JTextField(12);
    private final DefaultTableModel tableModel;

    public SubstationPanel() {
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Add Substation"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Labels and Fields
        addFormRow(formPanel, gbc, 0, "Name:", nameField);
        addFormRow(formPanel, gbc, 1, "Capacity (MVA):", capacityField);
        addFormRow(formPanel, gbc, 2, "Primary Volt (kV):", priVoltField);
        addFormRow(formPanel, gbc, 3, "Secondary Volt (kV):", secVoltField);

        JButton saveBtn = new JButton("Save Substation");
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        formPanel.add(saveBtn, gbc);

        saveBtn.addActionListener(e -> saveSubstation());

        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Capacity (MVA)", "Primary (kV)", "Secondary (kV)"}, 0);
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

    private void saveSubstation() {
        try {
            String name = nameField.getText().trim();
            double cap = Double.parseDouble(capacityField.getText().trim());
            double pri = Double.parseDouble(priVoltField.getText().trim());
            double sec = Double.parseDouble(secVoltField.getText().trim());

            Substation s = new Substation(name, cap, pri, sec);
            if (dao.addSubstation(s)) {
                JOptionPane.showMessageDialog(this, "Substation Saved!");
                clearFields();
                loadTableData();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter valid numbers for capacity and voltage.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        try {
            List<Substation> list = dao.getAllSubstations();
            for (Substation s : list) {
                tableModel.addRow(new Object[]{s.getId(), s.getName(), s.getCapacityMva(), s.getPrimaryVoltageKv(), s.getSecondaryVoltageKv()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }

    private void clearFields() {
        nameField.setText("");
        capacityField.setText("");
        priVoltField.setText("");
        secVoltField.setText("");
    }
}