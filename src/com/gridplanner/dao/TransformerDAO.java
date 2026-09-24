package com.gridplanner.dao;

import com.gridplanner.config.DatabaseConnection;
import com.gridplanner.model.Transformer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransformerDAO {

    public boolean addTransformer(Transformer t) throws SQLException {
        String sql = "INSERT INTO transformers (feeder_id, transformer_code, rated_kva, distance_from_substation_km) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, t.getFeederId());
            pstmt.setString(2, t.getCode());
            pstmt.setDouble(3, t.getRatedKva());
            pstmt.setDouble(4, t.getDistanceKm());
            
            return pstmt.executeUpdate() > 0;
        }
    }

    public List<Transformer> getAllTransformers() throws SQLException {
        List<Transformer> list = new ArrayList<>();
        String sql = "SELECT * FROM transformers";
        
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

    public boolean deleteTransformer(int id) throws SQLException {
        String sql = "DELETE FROM transformers WHERE transformer_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }
}