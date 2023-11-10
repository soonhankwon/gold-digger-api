package dev.golddiggerapi.expenditure.controller.dto;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Getter
public class ExpenditureByUserRequest {

    private final LocalDateTime start;

    private final LocalDateTime end;

    private final Long categoryId;

    private final Boolean hasMinAndMax;

    public ExpenditureByUserRequest(LocalDate start, LocalDate end, Long categoryId, Boolean hasMinAndMax) {
        this.start = start.atStartOfDay();
        this.end = end.atStartOfDay();
        this.categoryId = categoryId;
        this.hasMinAndMax = Objects.requireNonNullElse(hasMinAndMax, false);
        validateDate(start, end);
    }

    private void validateDate(LocalDate start, LocalDate end) {
        LocalDate dayOfServiceStart = LocalDate.of(2023, Month.JANUARY, 2);
        if (start.isBefore(dayOfServiceStart) || end.isBefore(dayOfServiceStart)) {
            throw new IllegalArgumentException("non service day");
        }
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("start can't after end");
        }

        long period = ChronoUnit.DAYS.between(start, end);
        if (period >= 30) {
            throw new IllegalArgumentException("period is up to 30 days");
        }
    }
}