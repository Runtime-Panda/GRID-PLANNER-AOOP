package com.gridplanner.ui;

import com.gridplanner.dao.TransformerDAO;
import com.gridplanner.model.Transformer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class TransformerPanel extends JPanel {
    private final TransformerDAO dao = new TransformerDAO();
    private final JTextField feederIdField = new JTextField(12);
    private final JTextField codeField = new JTextField(12);
    private final JTextField ratedKvaField = new JTextField(12);
    private final JTextField distanceField = new JTextField(12);
    private final DefaultTableModel tableModel;

    public TransformerPanel() {
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Add Transformer"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addFormRow(formPanel, gbc, 0, "Feeder ID:", feederIdField);
        addFormRow(formPanel, gbc, 1, "Transformer Code:", codeField);
        addFormRow(formPanel, gbc, 2, "Rated kVA:", ratedKvaField);
        addFormRow(formPanel, gbc, 3, "Distance (km):", distanceField);

        JButton saveBtn = new JButton("Save Transformer");
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        formPanel.add(saveBtn, gbc);

        saveBtn.addActionListener(e -> saveTransformer());

        tableModel = new DefaultTableModel(new String[]{"ID", "Feeder ID", "Code", "Rated kVA", "Distance (km)"}, 0);
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

    private void saveTransformer() {
        try {
            int feederId = Integer.parseInt(feederIdField.getText().trim());
            String code = codeField.getText().trim();
            double ratedKva = Double.parseDouble(ratedKvaField.getText().trim());
            double distance = Double.parseDouble(distanceField.getText().trim());

            Transformer t = new Transformer(feederId, code, ratedKva, distance);
            if (dao.addTransformer(t)) {
                JOptionPane.showMessageDialog(this, "Transformer Saved Successfully!");
                clearFields();
                loadTableData();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numerical values for Feeder ID, Rated kVA, and Distance.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        try {
            List<Transformer> list = dao.getAllTransformers();
            for (Transformer t : list) {
                tableModel.addRow(new Object[]{t.getId(), t.getFeederId(), t.getCode(), t.getRatedKva(), t.getDistanceKm()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading transformers data: " + e.getMessage());
        }
    }

    private void clearFields() {
        feederIdField.setText("");
        codeField.setText("");
        ratedKvaField.setText("");
        distanceField.setText("");
    }
}