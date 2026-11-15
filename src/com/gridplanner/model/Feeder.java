package com.gridplanner.model;

import com.gridplanner.exception.GridValidationException;
import com.gridplanner.interfaces.Validatable;

public class Feeder implements Validatable {
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

    @Override
    public void validate() throws GridValidationException {
        if (feederCode == null || feederCode.trim().isEmpty()) {
            throw new GridValidationException("Feeder code is required.");
        }
        if (conductorType == null || conductorType.trim().isEmpty()) {
            throw new GridValidationException("Conductor type is required.");
        }
        if (maxCurrentAmp <= 0 || lengthKm <= 0) {
            throw new GridValidationException("Max current (Amp) and length (km) must be positive.");
        }
    }

    public int getId() { return id; }
    public int getSubstationId() { return substationId; }
    public String getFeederCode() { return feederCode; }
    public String getConductorType() { return conductorType; }
    public double getMaxCurrentAmp() { return maxCurrentAmp; }
    public double getLengthKm() { return lengthKm; }
}