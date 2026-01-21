package com.vaccinator.immunizer.dto;

public record VaccineDTO(Long id,
                         String name,                 // e.g. Hepatitis B
                         String disease,              // e.g. Hepatitis
                         Integer totalDoses,
                         Integer gapBetweenDoses,     // days
                         Boolean boosterRequired,
                         Integer boosterAfterDays     // null if not required
) {
}
