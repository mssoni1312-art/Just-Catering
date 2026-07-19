package com.justcatering.superadmin.service;

import com.justcatering.superadmin.dto.response.ProjectProfitLossResponse;
import java.util.UUID;

/**
 * Project profit and loss computation service contract.
 */
public interface ProjectProfitLossService {

    /**
     * Computes profit and loss for a client project.
     *
     * @param clientUuid client UUID
     * @return computed P&amp;L summary
     */
    ProjectProfitLossResponse getProfitLoss(UUID clientUuid);
}
