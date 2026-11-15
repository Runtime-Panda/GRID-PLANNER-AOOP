package com.gridplanner.dao;

import com.gridplanner.config.DatabaseConnection;
import com.gridplanner.exception.GridException;
import com.gridplanner.interfaces.GridRepository;
import com.gridplanner.model.Substation;
import com.gridplanner.util.GridLogger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SubstationDAO implements GridRepository<Substation> {

    @Override
    public boolean save(Substation substation) throws GridException {
        substation.validate();
        String sql = "INSERT INTO substations (name, capacity_mva, primary_voltage_kv, secondary_voltage_kv) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, substation.getName());
            pstmt.setDouble(2, substation.getCapacityMva());
            pstmt.setDouble(3, substation.getPrimaryVoltageKv());
            pstmt.setDouble(4, substation.getSecondaryVoltageKv());
            boolean inserted = pstmt.executeUpdate() > 0;
            if (inserted) GridLogger.info("Added Substation: " + substation.getName());
            return inserted;
        } catch (SQLException e) {
            GridLogger.error("Error saving substation", e);
            throw new GridException("Database error while saving substation: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Substation> findAll() throws GridException {
        List<Substation> list = new ArrayList<>();
        String sql = "SELECT * FROM substations ORDER BY substation_id ASC";
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
            return list;
        } catch (SQLException e) {
            GridLogger.error("Error fetching substations", e);
            throw new GridException("Failed to fetch substations: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Substation> findById(int id) throws GridException {
        return findAll().stream().filter(s -> s.getId() == id).findFirst();
    }

    @Override
    public boolean deleteById(int id) throws GridException {
        String sql = "DELETE FROM substations WHERE substation_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            boolean deleted = pstmt.executeUpdate() > 0;
            if (deleted) GridLogger.info("Deleted Substation ID: " + id);
            return deleted;
        } catch (SQLException e) {
            GridLogger.error("Error deleting substation ID " + id, e);
            throw new GridException("Failed to delete substation: " + e.getMessage(), e);
        }
    }
}