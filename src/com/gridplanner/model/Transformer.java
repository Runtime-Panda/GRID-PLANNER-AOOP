package com.gridplanner.model;

import com.gridplanner.exception.GridValidationException;
import com.gridplanner.interfaces.Validatable;

public class Transformer implements Validatable {
    private int id;
    private int feederId;
    private String code;
    private double ratedKva;
    private double distanceKm;

    public Transformer(int id, int feederId, String code, double ratedKva, double distanceKm) {
        this.id = id;
        this.feederId = feederId;
        this.code = code;
        this.ratedKva = ratedKva;
        this.distanceKm = distanceKm;
    }

    public Transformer(int feederId, String code, double ratedKva, double distanceKm) {
        this(0, feederId, code, ratedKva, distanceKm);
    }

    @Override
    public void validate() throws GridValidationException {
        if (code == null || code.trim().isEmpty()) {
            throw new GridValidationException("Transformer code cannot be empty.");
        }
        if (ratedKva <= 0) {
            throw new GridValidationException("Rated kVA must be greater than zero.");
        }
        if (distanceKm < 0) {
            throw new GridValidationException("Distance cannot be negative.");
        }
    }

    public int getId() { return id; }
    public int getFeederId() { return feederId; }
    public String getCode() { return code; }
    public double getRatedKva() { return ratedKva; }
    public double getDistanceKm() { return distanceKm; }
}