package com.gridplanner.dao;

import com.gridplanner.config.DatabaseConnection;
import com.gridplanner.model.Transformer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransformerDAO {

    public boolean addTransformer(Transformer transformer) throws SQLException {
        String sql = "INSERT INTO transformers (feeder_id, transformer_code, rated_kva, distance_from_substation_km) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, transformer.getFeederId());
            pstmt.setString(2, transformer.getCode());
            pstmt.setDouble(3, transformer.getRatedKva());
            pstmt.setDouble(4, transformer.getDistanceKm());
            return pstmt.executeUpdate() > 0;
        }
    }

    public List<Transformer> getAllTransformers() throws SQLException {
        List<Transformer> list = new ArrayList<>();
        String sql = "SELECT * FROM transformers ORDER BY transformer_id ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Transformer(
                        rs.getInt("transformer_id"),
                        rs.getInt("feeder_id"),
                        rs.getString("transformer_code"),
                        rs.getDouble("rated_kva"),
                        rs.getDouble("distance_from_substation_km")
                ));
            }
        }
        return list;
    }

    public List<Object[]> getAllTransformersWithFeederCode() throws SQLException {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT t.transformer_id, t.feeder_id, f.feeder_code, t.transformer_code, t.rated_kva, t.distance_from_substation_km " +
                     "FROM transformers t " +
                     "INNER JOIN feeders f ON t.feeder_id = f.feeder_id " +
                     "ORDER BY t.transformer_id ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("transformer_id");
                int feederId = rs.getInt("feeder_id");
                String feederCode = rs.getString("feeder_code");
                String code = rs.getString("transformer_code");
                double ratedKva = rs.getDouble("rated_kva");
                double distanceKm = rs.getDouble("distance_from_substation_km");

                String feederDisplay = feederId + " (" + feederCode + ")";
                list.add(new Object[]{id, feederDisplay, code, ratedKva, distanceKm});
            }
        }
        return list;
    }

    public boolean deleteTransformer(int id) throws SQLException {
        String sql = "DELETE FROM transformers WHERE transformer_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }
}