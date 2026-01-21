package com.vaccinator.immunizer.entity;

import jakarta.persistence.*;

    @Entity
    @Table(name = "vaccines")
    public class VaccineEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private String name;

        private String disease;

        private Integer totalDoses;

        private Integer gapBetweenDoses;

        private Boolean boosterRequired;

        private Integer boosterAfterDays;

        // getters & setters

        public Integer getBoosterAfterDays() {
            return boosterAfterDays;
        }

        public void setBoosterAfterDays(Integer boosterAfterDays) {
            this.boosterAfterDays = boosterAfterDays;
        }

        public Boolean getBoosterRequired() {
            return boosterRequired;
        }

        public void setBoosterRequired(Boolean boosterRequired) {
            this.boosterRequired = boosterRequired;
        }

        public String getDisease() {
            return disease;
        }

        public void setDisease(String disease) {
            this.disease = disease;
        }

        public Integer getGapBetweenDoses() {
            return gapBetweenDoses;
        }

        public void setGapBetweenDoses(Integer gapBetweenDoses) {
            this.gapBetweenDoses = gapBetweenDoses;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getTotalDoses() {
            return totalDoses;
        }

        public void setTotalDoses(Integer totalDoses) {
            this.totalDoses = totalDoses;
        }
    }


