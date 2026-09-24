package com.gridplanner.model;

public class Transformer {
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

    public int getId() { return id; }
    public int getFeederId() { return feederId; }
    public String getCode() { return code; }
    public double getRatedKva() { return ratedKva; }
    public double getDistanceKm() { return distanceKm; }
}