package com.vaccinator.immunizer.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "vaccine_schedule")
public class VaccinescheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vaccine_id", nullable = false)
    private VaccineEntity vaccine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "start_period_id", nullable = false)
    private ScheduleEntity startPeriod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "end_period_id", nullable = false)
    private ScheduleEntity endPeriod;

    @Column(nullable = false)
    private Boolean mandatory;

    public VaccineEntity getVaccine() {
        return vaccine;
    }

    public void setVaccine(VaccineEntity vaccine) {
        this.vaccine = vaccine;
    }

    public ScheduleEntity getStartPeriod() {
        return startPeriod;
    }

    public void setStartPeriod(ScheduleEntity startPeriod) {
        this.startPeriod = startPeriod;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getMandatory() {
        return mandatory;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }

    public ScheduleEntity getEndPeriod() {
        return endPeriod;
    }

    public void setEndPeriod(ScheduleEntity endPeriod) {
        this.endPeriod = endPeriod;
    }
}
