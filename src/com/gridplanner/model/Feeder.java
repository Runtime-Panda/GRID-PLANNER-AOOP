package com.gridplanner.model;

public class Feeder {
    private int id;
    private int substationId;
    private String feederCode;
    private String conductorType;
    private double maxCurrentAmp;
    private double lengthKm;

    public Feeder(int id, int substationId, String feederCode, String conductorType, double maxCurrentAmp, double lengthKm) {
        this.id = id;
        this.substationId = substationId;
        this.feederCode = feederCode;
        this.conductorType = conductorType;
        this.maxCurrentAmp = maxCurrentAmp;
        this.lengthKm = lengthKm;
    }

    public Feeder(int substationId, String feederCode, String conductorType, double maxCurrentAmp, double lengthKm) {
        this(0, substationId, feederCode, conductorType, maxCurrentAmp, lengthKm);
    }

    public int getId() { return id; }
    public int getSubstationId() { return substationId; }
    public String getFeederCode() { return feederCode; }
    public String getConductorType() { return conductorType; }
    public double getMaxCurrentAmp() { return maxCurrentAmp; }
    public double getLengthKm() { return lengthKm; }

    @Override
    public String toString() {
        return feederCode + " (" + lengthKm + " km)";
    }
}