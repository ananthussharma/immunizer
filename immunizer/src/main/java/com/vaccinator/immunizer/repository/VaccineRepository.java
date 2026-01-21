package com.vaccinator.immunizer.repository;

import com.vaccinator.immunizer.entity.VaccineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VaccineRepository extends JpaRepository<VaccineEntity, Long> {
}
