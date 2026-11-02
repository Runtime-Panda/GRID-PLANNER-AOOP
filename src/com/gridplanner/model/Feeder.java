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
        this.substationId = substationId;
        this.feederCode = feederCode;
        this.conductorType = conductorType;
        this.maxCurrentAmp = maxCurrentAmp;
        this.lengthKm = lengthKm;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getSubstationId() { return substationId; }
    public void setSubstationId(int substationId) { this.substationId = substationId; }

    public String getFeederCode() { return feederCode; }
    public void setFeederCode(String feederCode) { this.feederCode = feederCode; }

    public String getConductorType() { return conductorType; }
    public void setConductorType(String conductorType) { this.conductorType = conductorType; }

    public double getMaxCurrentAmp() { return maxCurrentAmp; }
    public void setMaxCurrentAmp(double maxCurrentAmp) { this.maxCurrentAmp = maxCurrentAmp; }

    public double getLengthKm() { return lengthKm; }
    public void setLengthKm(double lengthKm) { this.lengthKm = lengthKm; }
}