package com.vaccinator.immunizer.dto;

import java.time.LocalDate;

public record UserVaccineDTO(Long id,

                             // References
                             Long userId,
                             Long vaccineId,

                             // Dose tracking
                             Integer doseNumber,
                             LocalDate administeredDate,

                             // Computed fields
                             LocalDate nextDoseDate,
                             Boolean completed,
                             Boolean overdue,

                             // Metadata
                             String administeredBy,
                             String notes) {
}
