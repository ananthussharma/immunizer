package com.vaccinator.immunizer.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "vaccine_dose",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"vaccine_id", "dose_number"})
        }
)
public class VaccineDoseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* ==========================
       Vaccine reference
       ========================== */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vaccine_id", nullable = false)
    private VaccineEntity vaccine;

    @Column(name = "dose_number", nullable = false)
    private Integer doseNumber;

    @Column(name = "min_gap_days")
    private Integer minGapDays;

    public Integer getDoseNumber() {
        return doseNumber;
    }

    public void setDoseNumber(Integer doseNumber) {
        this.doseNumber = doseNumber;
    }

    public VaccineEntity getVaccine() {
        return vaccine;
    }

    public void setVaccine(VaccineEntity vaccine) {
        this.vaccine = vaccine;
    }

    public Integer getMinGapDays() {
        return minGapDays;
    }

    public void setMinGapDays(Integer minGapDays) {
        this.minGapDays = minGapDays;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
