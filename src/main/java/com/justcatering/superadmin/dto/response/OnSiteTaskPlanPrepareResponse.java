package com.justcatering.superadmin.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response for Screen 1 Add — day slots for the next form screen.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnSiteTaskPlanPrepareResponse {

    /** Prefill manager / assigned member for the next screen. */
    private UserDropdownResponse assignedUser;

    private Integer numberOfDays;

    /** Empty day slots to render on the on-site manager form. */
    private List<OnSiteTaskPlanDayResponse> days;
}
