package com.gridplanner.model;

public class Substation {
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

    public int getId() { return id; }
    public String getName() { return name; }
    public double getCapacityMva() { return capacityMva; }
    public double getPrimaryVoltageKv() { return primaryVoltageKv; }
    public double getSecondaryVoltageKv() { return secondaryVoltageKv; }

    @Override
    public String toString() {
        return name + " (" + capacityMva + " MVA)";
    }
}