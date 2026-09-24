package com.gridplanner.dao;

import com.gridplanner.config.DatabaseConnection;
import com.gridplanner.model.Substation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubstationDAO {

    public boolean addSubstation(Substation s) throws SQLException {
        String sql = "INSERT INTO substations (name, capacity_mva, primary_voltage_kv, secondary_voltage_kv) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, s.getName());
            pstmt.setDouble(2, s.getCapacityMva());
            pstmt.setDouble(3, s.getPrimaryVoltageKv());
            pstmt.setDouble(4, s.getSecondaryVoltageKv());
            
            return pstmt.executeUpdate() > 0;
        }
    }

    public List<Substation> getAllSubstations() throws SQLException {
        List<Substation> list = new ArrayList<>();
        String sql = "SELECT * FROM substations";
        
        try (Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                list.add(new Substation(
                    rs.getInt("substation_id"),
                    rs.getString("name"),
                    rs.getDouble("capacity_mva"),
                    rs.getDouble("primary_voltage_kv"),
                    rs.getDouble("secondary_voltage_kv")
                ));
            }
        }
        return list;
    }

    public boolean deleteSubstation(int id) throws SQLException {
        String sql = "DELETE FROM substations WHERE substation_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }
}