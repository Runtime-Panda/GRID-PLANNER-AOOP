package com.gridplanner.ui;

import com.gridplanner.dao.SubstationDAO;
import com.gridplanner.model.Substation;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class SubstationPanel extends JPanel {
    private final SubstationDAO dao = new SubstationDAO();
    private final JTextField nameField = createDarkTextField();
    private final JTextField capacityField = createDarkTextField();
    private final JTextField priVoltField = createDarkTextField();
    private final JTextField secVoltField = createDarkTextField();
    private final DefaultTableModel tableModel;
    private final JTable table;

    public SubstationPanel() {
        setLayout(new BorderLayout(16, 16));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        setBackground(new Color(15, 23, 42)); // Dark canvas #0F172A

        // Form Card
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(30, 41, 59)); // Slate card #1E293B
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(51, 65, 85), 1),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addFormRow(formPanel, gbc, 0, "Substation Name:", nameField);
        addFormRow(formPanel, gbc, 1, "Capacity (MVA):", capacityField);
        addFormRow(formPanel, gbc, 2, "Primary Volt (kV):", priVoltField);
        addFormRow(formPanel, gbc, 3, "Secondary Volt (kV):", secVoltField);

        JButton saveBtn = createDarkButton("Save Substation", new Color(79, 70, 229)); // Indigo #4F46E5
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        formPanel.add(saveBtn, gbc);
        saveBtn.addActionListener(e -> saveSubstation());

        // Table
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Capacity (MVA)", "Primary (kV)", "Secondary (kV)"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        styleDarkTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(new Color(30, 41, 59));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(51, 65, 85), 1));

        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setOpaque(false);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controlPanel.setOpaque(false);

        JButton deleteBtn = createDarkButton("Delete Selected", new Color(220, 38, 38)); // Crimson #DC2626
        JButton refreshBtn = createDarkButton("Refresh", new Color(71, 85, 105)); // Slate #475569

        deleteBtn.addActionListener(e -> deleteSelected());
        refreshBtn.addActionListener(e -> loadTableData());

        controlPanel.add(deleteBtn);
        controlPanel.add(refreshBtn);
        rightPanel.add(controlPanel, BorderLayout.SOUTH);

        add(formPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);

        loadTableData();
    }

    private JTextField createDarkTextField() {
        JTextField field = new JTextField(14);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBackground(new Color(15, 23, 42));
        field.setForeground(new Color(241, 245, 249));
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(71, 85, 105), 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        return field;
    }

    private JButton createDarkButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        btn.setOpaque(true);
        return btn;
    }

    private void styleDarkTable(JTable tbl) {
        tbl.setRowHeight(32);
        tbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tbl.setBackground(new Color(30, 41, 59));
        tbl.setForeground(new Color(241, 245, 249));
        tbl.setSelectionBackground(new Color(79, 70, 229));
        tbl.setSelectionForeground(Color.WHITE);

        tbl.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tbl.getTableHeader().setBackground(new Color(15, 23, 42));
        tbl.getTableHeader().setForeground(new Color(148, 163, 184));
        tbl.setShowGrid(true);
        tbl.setGridColor(new Color(51, 65, 85));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.LEFT);
        centerRenderer.setBackground(new Color(30, 41, 59));
        centerRenderer.setForeground(new Color(241, 245, 249));
        tbl.setDefaultRenderer(Object.class, centerRenderer);
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent comp) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(new Color(226, 232, 240));
        panel.add(label, gbc);
        gbc.gridx = 1;
        panel.add(comp, gbc);
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
            JOptionPane.showMessageDialog(this, "Enter valid numerical values.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a substation row to delete.");
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete Substation ID " + id + "? Connected entities will CASCADE.", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (dao.deleteSubstation(id)) {
                    JOptionPane.showMessageDialog(this, "Substation Deleted!");
                    loadTableData();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Delete Error: " + ex.getMessage());
            }
        }
    }

    public void loadTableData() {
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