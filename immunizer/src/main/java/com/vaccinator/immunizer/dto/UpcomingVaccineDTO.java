package com.vaccinator.immunizer.dto;

import java.time.LocalDate;

public record UpcomingVaccineDTO(
        Long vaccineId,
        String name,
        String disease,
        Integer totalDoses,
        Integer nextDoseNumber,
        LocalDate nextDoseDate,
        Integer recommendedAgeStart,
        Integer recommendedAgeEnd,
        Boolean mandatory,
        Boolean alreadyAdministered,
        Boolean booster,
        LocalDate recommendedStartDate,
        LocalDate recommendedEndDate
) {}
