package com.gridplanner.service;

import com.gridplanner.exception.CapacityExceededException;
import com.gridplanner.exception.GridException;
import com.gridplanner.interfaces.GridRepository;
import com.gridplanner.model.Feeder;
import com.gridplanner.model.Transformer;

import java.util.List;

public class GridAnalyticsService {
    private final GridRepository<Feeder> feederRepo;
    private final GridRepository<Transformer> transformerRepo;

    public GridAnalyticsService(GridRepository<Feeder> feederRepo, GridRepository<Transformer> transformerRepo) {
        this.feederRepo = feederRepo;
        this.transformerRepo = transformerRepo;
    }

    /**
     * Uses Java Streams to compute total kVA load currently assigned to a feeder
     * and blocks allocation if it exceeds feeder capacity (assuming 11kV nominal).
     */
    public void verifyFeederHeadroom(int feederId, double additionalKva) throws GridException {
        Feeder feeder = feederRepo.findById(feederId)
                .orElseThrow(() -> new GridException("Target Feeder ID " + feederId + " does not exist."));

        List<Transformer> allTransformers = transformerRepo.findAll();

        double currentLoadKva = allTransformers.stream()
                .filter(t -> t.getFeederId() == feederId)
                .mapToDouble(Transformer::getRatedKva)
                .sum();

        // Approximate max kVA capacity at 11kV 3-phase: sqrt(3) * 11kV * I_max
        double maxFeederKva = 1.732 * 11.0 * feeder.getMaxCurrentAmp();

        if ((currentLoadKva + additionalKva) > maxFeederKva) {
            throw new CapacityExceededException(String.format(
                    "Feeder %s overload! Current: %.1f kVA + New: %.1f kVA exceeds Max Limit: %.1f kVA",
                    feeder.getFeederCode(), currentLoadKva, additionalKva, maxFeederKva
            ));
        }
    }
}