package com.gridplanner.ui;

import com.gridplanner.dao.FeederDAO;
import com.gridplanner.dao.TransformerDAO;
import com.gridplanner.model.Feeder;
import com.gridplanner.model.Transformer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class TransformerPanel extends JPanel {
    private final TransformerDAO dao = new TransformerDAO();
    private final FeederDAO feederDAO = new FeederDAO();

    private final JComboBox<String> feederComboBox = createDarkComboBox();
    private final JTextField codeField = createDarkTextField();
    private final JTextField ratedKvaField = createDarkTextField();
    private final JTextField distanceField = createDarkTextField();

    private final DefaultTableModel tableModel;
    private final JTable table;

    public TransformerPanel() {
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

        addFormRow(formPanel, gbc, 0, "Parent Feeder:", feederComboBox);
        addFormRow(formPanel, gbc, 1, "Transformer Code:", codeField);
        addFormRow(formPanel, gbc, 2, "Rated kVA:", ratedKvaField);
        addFormRow(formPanel, gbc, 3, "Distance (km):", distanceField);

        JButton saveBtn = createDarkButton("Save Transformer", new Color(79, 70, 229));
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        formPanel.add(saveBtn, gbc);
        saveBtn.addActionListener(e -> saveTransformer());

        // Updated column name to show both Feeder ID & Code clearly
        tableModel = new DefaultTableModel(new String[]{"ID", "Parent Feeder (ID & Code)", "Code", "Rated kVA", "Distance (km)"}, 0) {
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
        populateFeederDropdown();
        loadTableData();
    }

    private void populateFeederDropdown() {
        feederComboBox.removeAllItems();
        try {
            List<Feeder> feeders = feederDAO.getAllFeeders();
            for (Feeder f : feeders) {
                feederComboBox.addItem(f.getId() + " - " + f.getFeederCode());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading feeder dropdown: " + e.getMessage());
        }
    }

    private void saveTransformer() {
        if (feederComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Select a valid parent Feeder.");
            return;
        }

        try {
            String selectedFeeder = (String) feederComboBox.getSelectedItem();
            int feederId = Integer.parseInt(selectedFeeder.split(" - ")[0]);

            String code = codeField.getText().trim();
            double ratedKva = Double.parseDouble(ratedKvaField.getText().trim());
            double distance = Double.parseDouble(distanceField.getText().trim());

            Transformer t = new Transformer(feederId, code, ratedKva, distance);
            if (dao.addTransformer(t)) {
                JOptionPane.showMessageDialog(this, "Transformer Saved!");
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
            JOptionPane.showMessageDialog(this, "Select a transformer row to delete.");
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete Transformer ID " + id + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (dao.deleteTransformer(id)) {
                    JOptionPane.showMessageDialog(this, "Transformer Deleted!");
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
            List<Object[]> list = dao.getAllTransformersWithFeederCode();
            for (Object[] row : list) {
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading transformers: " + e.getMessage());
        }
    }

    private void clearFields() {
        codeField.setText("");
        ratedKvaField.setText("");
        distanceField.setText("");
    }
}