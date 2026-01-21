package com.vaccinator.immunizer.dao;

import com.vaccinator.immunizer.dto.LoginDTO;
import com.vaccinator.immunizer.dto.RegisterDTO;
import com.vaccinator.immunizer.dto.UserDTO;
import com.vaccinator.immunizer.entity.UserEntity;
import com.vaccinator.immunizer.repository.UserRepository;
import com.vaccinator.immunizer.util.PasswordUtil;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VaccineDao {
    private final UserRepository userRepository;
    public VaccineDao(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    public UserDTO login(LoginDTO loginCredentials) {
        String email = loginCredentials.email();
        String password = loginCredentials.password();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid Email"));

        String hashedInput = PasswordUtil.sha256Hex(password);
        if (!hashedInput.equals(user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }
        return toDTO(user);
    }

    public UserDTO register(RegisterDTO registerDTO) {
        Optional<UserEntity> existing = userRepository.findByEmail(registerDTO.email());
        if (existing.isPresent()) {
            throw new RuntimeException("Email already registered");
        }
        UserEntity user = new UserEntity();
        user.setFullName(registerDTO.fullName());
        user.setDateOfBirth(registerDTO.dateOfBirth());
        user.setGender(registerDTO.gender());
        user.setEmail(registerDTO.email());
        user.setPhone(registerDTO.phone());
        user.setPassword(PasswordUtil.sha256Hex(registerDTO.password()));

        UserEntity saved = userRepository.save(user);
        return toDTO(saved);
    }

    private UserDTO toDTO(UserEntity user){
        return new UserDTO(user.getId(), user.getFullName(), user.getDateOfBirth(), user.getGender(), user.getEmail(), user.getPhone());
    }
}
