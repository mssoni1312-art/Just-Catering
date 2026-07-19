package com.justcatering.superadmin.service.impl;

import com.justcatering.superadmin.dto.response.CalendarDateSummaryResponse;
import com.justcatering.superadmin.dto.response.CalendarScheduleItemResponse;
import com.justcatering.superadmin.dto.response.CalendarScheduleResponse;
import com.justcatering.superadmin.dto.response.CalendarSummaryResponse;
import com.justcatering.superadmin.entity.Client;
import com.justcatering.superadmin.enums.CalendarEventSource;
import com.justcatering.superadmin.enums.CalendarEventStatus;
import com.justcatering.superadmin.enums.ClientStage;
import com.justcatering.superadmin.exception.BusinessException;
import com.justcatering.superadmin.repository.ClientRepository;
import com.justcatering.superadmin.service.CalendarService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link CalendarService}.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalendarServiceImpl implements CalendarService {

    private final ClientRepository clientRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public CalendarSummaryResponse getSummary(LocalDate fromDate, LocalDate toDate) {
        validateDateRange(fromDate, toDate);

        Map<LocalDate, Set<CalendarEventStatus>> statusesByDate = new LinkedHashMap<>();

        for (Client client : clientRepository.findActiveClientsWithDealDateBetween(fromDate, toDate)) {
            CalendarEventStatus status = resolveClientStatus(client.getClientStage());
            if (status != null) {
                addStatus(statusesByDate, client.getDealDate(), status);
            }
        }

        List<CalendarDateSummaryResponse> dates = statusesByDate.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> CalendarDateSummaryResponse.builder()
                        .date(entry.getKey())
                        .statuses(new TreeSet<>(entry.getValue()))
                        .build())
                .toList();

        return CalendarSummaryResponse.builder()
                .fromDate(fromDate)
                .toDate(toDate)
                .dates(dates)
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CalendarScheduleResponse getSchedule(LocalDate date) {
        List<CalendarScheduleItemResponse> items = new ArrayList<>();

        for (Client client : clientRepository.findActiveClientsByDealDate(date)) {
            CalendarEventStatus status = resolveClientStatus(client.getClientStage());
            if (status == null) {
                continue;
            }
            items.add(toClientScheduleItem(client, status));
        }

        items.sort(Comparator
                .comparing(CalendarScheduleItemResponse::getScheduledTime,
                        Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(CalendarScheduleItemResponse::getTitle, String.CASE_INSENSITIVE_ORDER));

        return CalendarScheduleResponse.builder()
                .date(date)
                .totalCount(items.size())
                .items(items)
                .build();
    }

    private void validateDateRange(LocalDate fromDate, LocalDate toDate) {
        if (fromDate == null || toDate == null) {
            throw new BusinessException("Both fromDate and toDate are required");
        }
        if (toDate.isBefore(fromDate)) {
            throw new BusinessException("toDate must be on or after fromDate");
        }
    }

    private void addStatus(
            Map<LocalDate, Set<CalendarEventStatus>> statusesByDate,
            LocalDate date,
            CalendarEventStatus status
    ) {
        statusesByDate.computeIfAbsent(date, ignored -> EnumSet.noneOf(CalendarEventStatus.class)).add(status);
    }

    private CalendarEventStatus resolveClientStatus(ClientStage clientStage) {
        if (clientStage == null) {
            return null;
        }
        return switch (clientStage) {
            case NEW, INTERESTED -> CalendarEventStatus.NEW;
            case IN_PROGRESS, ACTIVE, ON_HOLD -> CalendarEventStatus.IN_PROGRESS;
            case COMPLETED -> CalendarEventStatus.COMPLETED;
            case CHURNED -> null;
        };
    }

    private CalendarScheduleItemResponse toClientScheduleItem(Client client, CalendarEventStatus status) {
        return CalendarScheduleItemResponse.builder()
                .uuid(client.getUuid())
                .source(CalendarEventSource.CLIENT)
                .title(client.getClientName())
                .clientUuid(client.getUuid())
                .clientName(client.getClientName())
                .calendarStatus(status)
                .clientStage(client.getClientStage())
                .contactPerson(client.getContactPerson())
                .mobile(client.getMobile())
                .priority(client.getPriority())
                .build();
    }
}
