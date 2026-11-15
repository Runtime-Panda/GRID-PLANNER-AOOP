package com.gridplanner.util;

import com.gridplanner.exception.GridException;
import javax.swing.table.TableModel;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CsvExporter {
    public static String exportTableToCsv(TableModel model, String entityPrefix) throws GridException {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = entityPrefix + "_export_" + timestamp + ".csv";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // Write headers
            for (int col = 0; col < model.getColumnCount(); col++) {
                writer.write(model.getColumnName(col));
                if (col < model.getColumnCount() - 1) writer.write(",");
            }
            writer.newLine();

            // Write rows
            for (int row = 0; row < model.getRowCount(); row++) {
                for (int col = 0; col < model.getColumnCount(); col++) {
                    Object val = model.getValueAt(row, col);
                    writer.write(val != null ? val.toString().replace(",", ";") : "");
                    if (col < model.getColumnCount() - 1) writer.write(",");
                }
                writer.newLine();
            }
            GridLogger.info("Exported CSV report: " + fileName);
            return fileName;
        } catch (IOException e) {
            GridLogger.error("CSV Export failed", e);
            throw new GridException("Failed to write CSV file: " + e.getMessage(), e);
        }
    }
}