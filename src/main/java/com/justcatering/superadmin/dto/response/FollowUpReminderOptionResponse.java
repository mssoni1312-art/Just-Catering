package com.justcatering.superadmin.dto.response;

import com.justcatering.superadmin.enums.FollowUpReminder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Reminder option for follow-up dropdowns.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowUpReminderOptionResponse {

    private FollowUpReminder code;
    private int minutes;
    private String label;
}
