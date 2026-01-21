package com.vaccinator.immunizer.controller;

import com.vaccinator.immunizer.dto.*;
import com.vaccinator.immunizer.service.VaccineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vaccinator")
public class VaccineController {
    private final VaccineService vaccineService;

    public VaccineController(VaccineService vaccineService){
        this.vaccineService = vaccineService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> userLogin(@RequestBody LoginDTO loginCredentials){
        return ResponseEntity.ok(vaccineService.userLogin(loginCredentials));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody RegisterDTO registerDTO){
        return ResponseEntity.ok(vaccineService.register(registerDTO));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserVaccineResponseDTO> getVaccineDetails(@PathVariable long userId){
        return  ResponseEntity.ok(vaccineService.getVaccineDetails(userId));
    }

    @GetMapping("/vaccine/{vaccineId}")
    public ResponseEntity<VaccineDTO>  getVaccine(@PathVariable Long vaccineId){
        return ResponseEntity.ok(vaccineService.getVaccine(vaccineId));
    }
}
