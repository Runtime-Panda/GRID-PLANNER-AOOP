-- Active: 1790233332595@@127.0.0.1@3306
CREATE DATABASE IF NOT EXISTS grid_planner_db;
USE grid_planner_db;

DROP TABLE IF EXISTS consumer_clusters;
DROP TABLE IF EXISTS transformers;
DROP TABLE IF EXISTS feeders;
DROP TABLE IF EXISTS substations;

CREATE TABLE substations (
    substation_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    capacity_mva DECIMAL(10,2) NOT NULL,
    primary_voltage_kv DECIMAL(5,2) NOT NULL,
    secondary_voltage_kv DECIMAL(5,2) NOT NULL
);

CREATE TABLE feeders (
    feeder_id INT PRIMARY KEY AUTO_INCREMENT,
    substation_id INT NOT NULL,
    feeder_code VARCHAR(50) NOT NULL UNIQUE,
    conductor_type VARCHAR(50) NOT NULL,
    max_current_amp DECIMAL(8,2) NOT NULL,
    length_km DECIMAL(6,2) NOT NULL,
    FOREIGN KEY (substation_id) REFERENCES substations(substation_id) ON DELETE CASCADE
);

CREATE TABLE transformers (
    transformer_id INT PRIMARY KEY AUTO_INCREMENT,
    feeder_id INT NOT NULL,
    transformer_code VARCHAR(50) NOT NULL UNIQUE,
    rated_kva DECIMAL(8,2) NOT NULL,
    distance_from_substation_km DECIMAL(6,2) NOT NULL,
    FOREIGN KEY (feeder_id) REFERENCES feeders(feeder_id) ON DELETE CASCADE
);

CREATE TABLE consumer_clusters (
    cluster_id INT PRIMARY KEY AUTO_INCREMENT,
    transformer_id INT NOT NULL,
    cluster_name VARCHAR(100) NOT NULL,
    connected_load_kw DECIMAL(8,2) NOT NULL,
    power_factor DECIMAL(3,2) DEFAULT 0.90,
    FOREIGN KEY (transformer_id) REFERENCES transformers(transformer_id) ON DELETE CASCADE
);

INSERT INTO substations (name, capacity_mva, primary_voltage_kv, secondary_voltage_kv) 
VALUES ('Central Substation Alpha', 50.00, 33.00, 11.00);

INSERT INTO feeders (substation_id, feeder_code, conductor_type, max_current_amp, length_km) 
VALUES (1, 'FDR-NORTH-01', 'ACSR Dog', 200.00, 4.50);