package com.vaccinator.immunizer.repository;

import com.vaccinator.immunizer.entity.VaccinescheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VaccineScheduleRepository extends JpaRepository<VaccinescheduleEntity, Long> {
    @Query("select vs from VaccinescheduleEntity vs where vs.startPeriod.period <= :age and vs.endPeriod.period >= :age order by vs.mandatory desc")
    List<VaccinescheduleEntity> findByAge(@Param("age") int age);
}
