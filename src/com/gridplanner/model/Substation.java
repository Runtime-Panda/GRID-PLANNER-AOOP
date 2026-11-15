package com.gridplanner.model;

import com.gridplanner.exception.GridValidationException;
import com.gridplanner.interfaces.Validatable;

public class Substation implements Validatable {
    private int id;
    private String name;
    private double capacityMva;
    private double primaryVoltageKv;
    private double secondaryVoltageKv;

    public Substation(int id, String name, double capacityMva, double primaryVoltageKv, double secondaryVoltageKv) {
        this.id = id;
        this.name = name;
        this.capacityMva = capacityMva;
        this.primaryVoltageKv = primaryVoltageKv;
        this.secondaryVoltageKv = secondaryVoltageKv;
    }

    public Substation(String name, double capacityMva, double primaryVoltageKv, double secondaryVoltageKv) {
        this(0, name, capacityMva, primaryVoltageKv, secondaryVoltageKv);
    }

    @Override
    public void validate() throws GridValidationException {
        if (name == null || name.trim().isEmpty()) {
            throw new GridValidationException("Substation name cannot be empty.");
        }
        if (capacityMva <= 0) {
            throw new GridValidationException("Capacity (MVA) must be greater than zero.");
        }
        if (primaryVoltageKv <= secondaryVoltageKv) {
            throw new GridValidationException("Primary voltage must exceed secondary step-down voltage.");
        }
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getCapacityMva() { return capacityMva; }
    public double getPrimaryVoltageKv() { return primaryVoltageKv; }
    public double getSecondaryVoltageKv() { return secondaryVoltageKv; }
}