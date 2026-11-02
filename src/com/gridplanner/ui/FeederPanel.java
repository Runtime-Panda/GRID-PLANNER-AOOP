package com.gridplanner.ui;

import com.gridplanner.dao.FeederDAO;
import com.gridplanner.dao.SubstationDAO;
import com.gridplanner.dao.TransformerDAO;
import com.gridplanner.model.Feeder;
import com.gridplanner.model.Substation;
import com.gridplanner.model.Transformer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class FeederPanel extends JPanel {
    private final FeederDAO feederDAO = new FeederDAO();
    private final SubstationDAO subDAO = new SubstationDAO();
    private final TransformerDAO transDAO = new TransformerDAO();

    private final JComboBox<String> subComboBox = createDarkComboBox();
    private final JComboBox<String> transComboBox = createDarkComboBox();
    private final JTextField codeField = createDarkTextField();
    private final JTextField typeField = createDarkTextField();
    private final JTextField maxAmpField = createDarkTextField();
    private final JTextField lengthField = createDarkTextField();

    private final DefaultTableModel tableModel;
    private final JTable table;

    public FeederPanel() {
        setLayout(new BorderLayout(16, 16));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        setBackground(new Color(15, 23, 42));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(30, 41, 59));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(51, 65, 85), 1),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addFormRow(formPanel, gbc, 0, "Select Substation:", subComboBox);
        addFormRow(formPanel, gbc, 1, "Target Transformer:", transComboBox);
        addFormRow(formPanel, gbc, 2, "Feeder Code:", codeField);
        addFormRow(formPanel, gbc, 3, "Conductor Type:", typeField);
        addFormRow(formPanel, gbc, 4, "Max Amp:", maxAmpField);
        addFormRow(formPanel, gbc, 5, "Length (km):", lengthField);

        JButton saveBtn = createDarkButton("Save Feeder", new Color(79, 70, 229));
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        formPanel.add(saveBtn, gbc);
        saveBtn.addActionListener(e -> saveFeeder());

        tableModel = new DefaultTableModel(new String[]{"ID", "Substation ID", "Code", "Type", "Max Amp", "Length (km)"}, 0) {
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

        JButton deleteBtn = createDarkButton("Delete Selected", new Color(220, 38, 38));
        JButton refreshBtn = createDarkButton("Refresh", new Color(71, 85, 105));

        deleteBtn.addActionListener(e -> deleteSelected());
        refreshBtn.addActionListener(e -> refreshAll());

        controlPanel.add(deleteBtn);
        controlPanel.add(refreshBtn);
        rightPanel.add(controlPanel, BorderLayout.SOUTH);

        add(formPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);

        refreshAll();
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

    private JComboBox<String> createDarkComboBox() {
        JComboBox<String> box = new JComboBox<>();
        box.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        box.setBackground(new Color(15, 23, 42));
        box.setForeground(new Color(241, 245, 249));
        return box;
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

    public void refreshAll() {
        populateDropdowns();
        loadTableData();
    }

    private void populateDropdowns() {
        subComboBox.removeAllItems();
        transComboBox.removeAllItems();

        try {
            List<Substation> substations = subDAO.getAllSubstations();
            for (Substation s : substations) {
                subComboBox.addItem(s.getId() + " - " + s.getName());
            }

            List<Transformer> transformers = transDAO.getAllTransformers();
            for (Transformer t : transformers) {
                transComboBox.addItem("TR-" + t.getId() + " (" + t.getCode() + ", " + t.getDistanceKm() + " km)");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading dropdowns: " + e.getMessage());
        }
    }

    private void saveFeeder() {
        if (subComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Select a valid Substation.");
            return;
        }

        try {
            String selectedSub = (String) subComboBox.getSelectedItem();
            int subId = Integer.parseInt(selectedSub.split(" - ")[0]);

            String code = codeField.getText().trim();
            String type = typeField.getText().trim();
            double maxAmp = Double.parseDouble(maxAmpField.getText().trim());
            double len = Double.parseDouble(lengthField.getText().trim());

            Feeder f = new Feeder(subId, code, type, maxAmp, len);
            if (feederDAO.addFeeder(f)) {
                JOptionPane.showMessageDialog(this, "Feeder Saved!");
                clearFields();
                refreshAll();
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
            JOptionPane.showMessageDialog(this, "Select a feeder row to delete.");
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete Feeder ID " + id + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (feederDAO.deleteFeeder(id)) {
                    JOptionPane.showMessageDialog(this, "Feeder Deleted!");
                    refreshAll();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Delete Error: " + ex.getMessage());
            }
        }
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        try {
            List<Feeder> list = feederDAO.getAllFeeders();
            for (Feeder f : list) {
                tableModel.addRow(new Object[]{f.getId(), f.getSubstationId(), f.getFeederCode(), f.getConductorType(), f.getMaxCurrentAmp(), f.getLengthKm()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading feeders: " + e.getMessage());
        }
    }

    private void clearFields() {
        codeField.setText("");
        typeField.setText("");
        maxAmpField.setText("");
        lengthField.setText("");
    }
}