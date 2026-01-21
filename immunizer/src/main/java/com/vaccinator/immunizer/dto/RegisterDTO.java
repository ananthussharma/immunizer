package com.vaccinator.immunizer.dto;

import java.time.LocalDate;

public record RegisterDTO(String fullName,
                          LocalDate dateOfBirth,
                          String gender,
                          String email,
                          String phone,
                          String password) {
}
