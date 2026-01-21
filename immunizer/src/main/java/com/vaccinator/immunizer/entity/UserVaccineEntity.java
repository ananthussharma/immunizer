package com.vaccinator.immunizer.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "user_vaccines")
public class UserVaccineEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* ======================
       Relationships
       ====================== */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vaccine_id", nullable = false)
    private VaccineEntity vaccine;

    /* ======================
       Dose tracking
       ====================== */

    private Integer doseNumber;

    private LocalDate administeredDate;

    /* ======================
       Computed / Status
       ====================== */

    private LocalDate nextDoseDate;

    private Boolean completed;

    private Boolean overdue;

    /* ======================
       Metadata
       ====================== */

    private String administeredBy;

    private String notes;


    public LocalDate getAdministeredDate() {
        return administeredDate;
    }

    public void setAdministeredDate(LocalDate administeredDate) {
        this.administeredDate = administeredDate;
    }

    public String getAdministeredBy() {
        return administeredBy;
    }

    public void setAdministeredBy(String administeredBy) {
        this.administeredBy = administeredBy;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Integer getDoseNumber() {
        return doseNumber;
    }

    public void setDoseNumber(Integer doseNumber) {
        this.doseNumber = doseNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getNextDoseDate() {
        return nextDoseDate;
    }

    public void setNextDoseDate(LocalDate nextDoseDate) {
        this.nextDoseDate = nextDoseDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getOverdue() {
        return overdue;
    }

    public void setOverdue(Boolean overdue) {
        this.overdue = overdue;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public VaccineEntity getVaccine() {
        return vaccine;
    }

    public void setVaccine(VaccineEntity vaccine) {
        this.vaccine = vaccine;
    }

}

