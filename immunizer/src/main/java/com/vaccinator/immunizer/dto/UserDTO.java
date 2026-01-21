package com.vaccinator.immunizer.dto;

import java.time.LocalDate;

public record UserDTO(Long id,
                      String fullName,
                      LocalDate dateOfBirth,
                      String gender,
                      String email,
                      String phone) {
}
