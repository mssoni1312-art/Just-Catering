package com.justcatering.superadmin.dto.response;

import com.justcatering.superadmin.enums.CalendarEventSource;
import com.justcatering.superadmin.enums.CalendarEventStatus;
import com.justcatering.superadmin.enums.ClientStage;
import com.justcatering.superadmin.enums.FollowUpStatus;
import com.justcatering.superadmin.enums.FollowUpType;
import com.justcatering.superadmin.enums.Priority;
import java.time.LocalTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A single client or follow-up entry on the daily calendar schedule.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarScheduleItemResponse {

    private UUID uuid;
    private CalendarEventSource source;
    private String title;
    private UUID clientUuid;
    private String clientName;
    private CalendarEventStatus calendarStatus;
    private ClientStage clientStage;
    private FollowUpType followUpType;
    private FollowUpStatus followUpStatus;
    private LocalTime scheduledTime;
    private String contactPerson;
    private String mobile;
    private String assignedUserName;
    private Priority priority;
}
