package com.vaccinator.immunizer.repository;

import com.vaccinator.immunizer.entity.VaccineDoseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VaccineDoseRepository extends JpaRepository<VaccineDoseEntity, Long> {
    Optional<VaccineDoseEntity> findByVaccineIdAndDoseNumber(Long vaccineId, Integer doseNumber);
}
