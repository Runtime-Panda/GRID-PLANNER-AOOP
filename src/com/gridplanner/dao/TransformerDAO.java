package com.gridplanner.dao;

import com.gridplanner.config.DatabaseConnection;
import com.gridplanner.exception.GridException;
import com.gridplanner.interfaces.GridRepository;
import com.gridplanner.model.Transformer;
import com.gridplanner.util.GridLogger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransformerDAO implements GridRepository<Transformer> {

    @Override
    public boolean save(Transformer transformer) throws GridException {
        transformer.validate();
        String sql = "INSERT INTO transformers (feeder_id, transformer_code, rated_kva, distance_from_substation_km) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, transformer.getFeederId());
            pstmt.setString(2, transformer.getCode());
            pstmt.setDouble(3, transformer.getRatedKva());
            pstmt.setDouble(4, transformer.getDistanceKm());
            boolean inserted = pstmt.executeUpdate() > 0;
            if (inserted) GridLogger.info("Added Transformer: " + transformer.getCode());
            return inserted;
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new GridException("Transformer code '" + transformer.getCode() + "' already exists or Feeder ID is invalid.", e);
        } catch (SQLException e) {
            GridLogger.error("Error saving transformer", e);
            throw new GridException("Database error while saving transformer: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Transformer> findAll() throws GridException {
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
            return list;
        } catch (SQLException e) {
            GridLogger.error("Error fetching transformers", e);
            throw new GridException("Failed to fetch transformers: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Transformer> findById(int id) throws GridException {
        return findAll().stream().filter(t -> t.getId() == id).findFirst();
    }

    public List<Object[]> findAllWithFeederCode() throws GridException {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT t.transformer_id, t.feeder_id, f.feeder_code, t.transformer_code, t.rated_kva, t.distance_from_substation_km " +
                     "FROM transformers t INNER JOIN feeders f ON t.feeder_id = f.feeder_id ORDER BY t.transformer_id ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String feederDisplay = rs.getInt("feeder_id") + " (" + rs.getString("feeder_code") + ")";
                list.add(new Object[]{
                        rs.getInt("transformer_id"),
                        feederDisplay,
                        rs.getString("transformer_code"),
                        rs.getDouble("rated_kva"),
                        rs.getDouble("distance_from_substation_km")
                });
            }
            return list;
        } catch (SQLException e) {
            GridLogger.error("Error fetching joined transformers", e);
            throw new GridException("Failed to load joined transformer records: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteById(int id) throws GridException {
        String sql = "DELETE FROM transformers WHERE transformer_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            boolean deleted = pstmt.executeUpdate() > 0;
            if (deleted) GridLogger.info("Deleted Transformer ID: " + id);
            return deleted;
        } catch (SQLException e) {
            GridLogger.error("Error deleting transformer ID " + id, e);
            throw new GridException("Failed to delete transformer: " + e.getMessage(), e);
        }
    }
}