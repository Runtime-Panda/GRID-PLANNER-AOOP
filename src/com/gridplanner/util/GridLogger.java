package com.gridplanner.util;

import java.io.IOException;
import java.util.logging.*;

public class GridLogger {
    private static final Logger LOGGER = Logger.getLogger("GridPlannerLogger");

    static {
        try {
            FileHandler fileHandler = new FileHandler("grid_planner.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
            LOGGER.setLevel(Level.ALL);
        } catch (IOException e) {
            System.err.println("Failed to initialize file logger: " + e.getMessage());
        }
    }

    public static void info(String msg) {
        LOGGER.info(msg);
    }

    public static void error(String msg, Throwable t) {
        LOGGER.log(Level.SEVERE, msg, t);
    }
}