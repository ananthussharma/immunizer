package com.vaccinator.immunizer.dto;

import java.util.List;

public record UserVaccineResponseDTO(
        List<UserVaccineDTO> administered,
        List<UpcomingVaccineDTO> upcoming
) {
}
