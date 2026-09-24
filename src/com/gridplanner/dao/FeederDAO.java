package com.gridplanner.dao;

import com.gridplanner.config.DatabaseConnection;
import com.gridplanner.model.Feeder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeederDAO {

    public boolean addFeeder(Feeder f) throws SQLException {
        String sql = "INSERT INTO feeders (substation_id, feeder_code, conductor_type, max_current_amp, length_km) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, f.getSubstationId());
            pstmt.setString(2, f.getFeederCode());
            pstmt.setString(3, f.getConductorType());
            pstmt.setDouble(4, f.getMaxCurrentAmp());
            pstmt.setDouble(5, f.getLengthKm());
            
            return pstmt.executeUpdate() > 0;
        }
    }

    public List<Feeder> getAllFeeders() throws SQLException {
        List<Feeder> list = new ArrayList<>();
        String sql = "SELECT * FROM feeders";
        
        try (Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                list.add(new Feeder(
                    rs.getInt("feeder_id"),
                    rs.getInt("substation_id"),
                    rs.getString("feeder_code"),
                    rs.getString("conductor_type"),
                    rs.getDouble("max_current_amp"),
                    rs.getDouble("length_km")
                ));
            }
        }
        return list;
    }

    public boolean deleteFeeder(int id) throws SQLException {
        String sql = "DELETE FROM feeders WHERE feeder_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }
}