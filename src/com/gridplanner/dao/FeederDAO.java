package com.gridplanner.dao;

import com.gridplanner.config.DatabaseConnection;
import com.gridplanner.exception.GridException;
import com.gridplanner.interfaces.GridRepository;
import com.gridplanner.model.Feeder;
import com.gridplanner.util.GridLogger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FeederDAO implements GridRepository<Feeder> {

    @Override
    public boolean save(Feeder feeder) throws GridException {
        feeder.validate();
        String sql = "INSERT INTO feeders (substation_id, feeder_code, conductor_type, max_current_amp, length_km) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, feeder.getSubstationId());
            pstmt.setString(2, feeder.getFeederCode());
            pstmt.setString(3, feeder.getConductorType());
            pstmt.setDouble(4, feeder.getMaxCurrentAmp());
            pstmt.setDouble(5, feeder.getLengthKm());
            boolean inserted = pstmt.executeUpdate() > 0;
            if (inserted) GridLogger.info("Added Feeder: " + feeder.getFeederCode());
            return inserted;
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new GridException("Feeder code '" + feeder.getFeederCode() + "' already exists or Substation ID is invalid.", e);
        } catch (SQLException e) {
            GridLogger.error("Error saving feeder", e);
            throw new GridException("Database error while saving feeder: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Feeder> findAll() throws GridException {
        List<Feeder> list = new ArrayList<>();
        String sql = "SELECT * FROM feeders ORDER BY feeder_id ASC";
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
            return list;
        } catch (SQLException e) {
            GridLogger.error("Error fetching feeders", e);
            throw new GridException("Failed to fetch feeders: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Feeder> findById(int id) throws GridException {
        return findAll().stream().filter(f -> f.getId() == id).findFirst();
    }

    @Override
    public boolean deleteById(int id) throws GridException {
        String sql = "DELETE FROM feeders WHERE feeder_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            boolean deleted = pstmt.executeUpdate() > 0;
            if (deleted) GridLogger.info("Deleted Feeder ID: " + id);
            return deleted;
        } catch (SQLException e) {
            GridLogger.error("Error deleting feeder ID " + id, e);
            throw new GridException("Failed to delete feeder: " + e.getMessage(), e);
        }
    }
}