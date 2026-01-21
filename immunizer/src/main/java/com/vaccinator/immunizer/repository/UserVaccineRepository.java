package com.vaccinator.immunizer.repository;

import com.vaccinator.immunizer.entity.UserVaccineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserVaccineRepository extends JpaRepository<UserVaccineEntity, Long> {
    List<UserVaccineEntity> findByUserId(Long userId);
}
