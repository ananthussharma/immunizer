package com.vaccinator.immunizer.service;

import com.vaccinator.immunizer.dao.VaccineDao;
import com.vaccinator.immunizer.dto.*;
import com.vaccinator.immunizer.entity.UserEntity;
import com.vaccinator.immunizer.entity.UserVaccineEntity;
import com.vaccinator.immunizer.entity.VaccinescheduleEntity;
import com.vaccinator.immunizer.entity.VaccineDoseEntity;
import com.vaccinator.immunizer.entity.VaccineEntity;
import com.vaccinator.immunizer.entity.ScheduleEntity;
import com.vaccinator.immunizer.repository.UserRepository;
import com.vaccinator.immunizer.repository.UserVaccineRepository;
import com.vaccinator.immunizer.repository.VaccineRepository;
import com.vaccinator.immunizer.repository.VaccineScheduleRepository;
import com.vaccinator.immunizer.repository.VaccineDoseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VaccineService {
    private final VaccineDao vaccineDao;
    private final UserVaccineRepository userVaccineRepository;
    private final VaccineRepository vaccineRepository;
    private final UserRepository userRepository;
    private final VaccineScheduleRepository vaccineScheduleRepository;
    private final VaccineDoseRepository vaccineDoseRepository;

    public VaccineService(VaccineDao vaccineDao, UserVaccineRepository userVaccineRepository, VaccineRepository vaccineRepository, UserRepository userRepository, VaccineScheduleRepository vaccineScheduleRepository, VaccineDoseRepository vaccineDoseRepository){
        this.vaccineDao = vaccineDao;
        this.userVaccineRepository = userVaccineRepository;
        this.vaccineRepository = vaccineRepository;
        this.userRepository = userRepository;
        this.vaccineScheduleRepository = vaccineScheduleRepository;
        this.vaccineDoseRepository = vaccineDoseRepository;
    }

    public UserDTO userLogin(LoginDTO loginCredentials) {
     return vaccineDao.login(loginCredentials);
    }

    public UserDTO register(RegisterDTO registerDTO) {
        return vaccineDao.register(registerDTO);
    }

    public UserVaccineResponseDTO getVaccineDetails(long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        List<UserVaccineEntity> entities = userVaccineRepository.findByUserId(userId);

        List<UserVaccineDTO> administered = entities.stream().map(e -> {
            LocalDate nextDose = e.getNextDoseDate();
            // compute fallback next dose if not present and doses remaining
            if (nextDose == null && e.getVaccine() != null && e.getAdministeredDate() != null && e.getDoseNumber() != null) {
                VaccineEntity v = e.getVaccine();
                if (v.getTotalDoses() != null && e.getDoseNumber() < v.getTotalDoses()) {
                    // consult VaccineDoseEntity for specific gap if present
                    Optional<VaccineDoseEntity> doseSpec = vaccineDoseRepository.findByVaccineIdAndDoseNumber(v.getId(), e.getDoseNumber()+1);
                    if (doseSpec.isPresent() && doseSpec.get().getMinGapDays() != null) {
                        nextDose = e.getAdministeredDate().plusDays(doseSpec.get().getMinGapDays());
                    } else if (v.getGapBetweenDoses() != null) {
                        nextDose = e.getAdministeredDate().plusDays(v.getGapBetweenDoses());
                    }
                }
            }
            return new UserVaccineDTO(
                    e.getId(),
                    e.getUser() != null ? e.getUser().getId() : null,
                    e.getVaccine() != null ? e.getVaccine().getId() : null,
                    e.getDoseNumber(),
                    e.getAdministeredDate(),
                    nextDose,
                    e.getCompleted(),
                    e.getOverdue(),
                    e.getAdministeredBy(),
                    e.getNotes()
            );
        }).collect(Collectors.toList());

        // compute user age in months
        LocalDate dob = user.getDateOfBirth();
        int ageInMonths = 0;
        if (dob != null) {
            ageInMonths = (int) ChronoUnit.MONTHS.between(dob, LocalDate.now());
        }

        // now query schedules where schedule.period is considered in months
        List<VaccinescheduleEntity> schedules = vaccineScheduleRepository.findByAge(ageInMonths);

        // prepare upcoming list: include pending doses first (from user vaccines where doses remain)
        Map<Long, UpcomingVaccineDTO> upcomingMap = new LinkedHashMap<>();

        // helper: map vaccineId -> list of user vaccine entries for that vaccine
        Map<Long, List<UserVaccineEntity>> byVaccine = new HashMap<>();
        for (UserVaccineEntity e : entities) {
            if (e.getVaccine() != null) {
                byVaccine.computeIfAbsent(e.getVaccine().getId(), k -> new ArrayList<>()).add(e);
            }
        }

        for (UserVaccineEntity e : entities) {
            VaccineEntity v = e.getVaccine();
            if (v == null) continue;
            Integer total = v.getTotalDoses();
            Integer doseNo = e.getDoseNumber();
            LocalDate nextDose = e.getNextDoseDate();
            if ((nextDose == null || (doseNo != null && total != null && doseNo < total)) && doseNo != null && total != null && doseNo < total) {
                // compute expected next dose date if missing, using VaccineDoseEntity.minGapDays first
                if (nextDose == null && e.getAdministeredDate() != null) {
                    Optional<VaccineDoseEntity> doseSpec = vaccineDoseRepository.findByVaccineIdAndDoseNumber(v.getId(), doseNo + 1);
                    if (doseSpec.isPresent() && doseSpec.get().getMinGapDays() != null) {
                        nextDose = e.getAdministeredDate().plusDays(doseSpec.get().getMinGapDays());
                    } else if (v.getGapBetweenDoses() != null) {
                        nextDose = e.getAdministeredDate().plusDays(v.getGapBetweenDoses());
                    }
                }

                // compute recommended date window from schedules if present
                LocalDate recommendedStartDate = null;
                LocalDate recommendedEndDate = null;
                VaccinescheduleEntity matchedSchedule = schedules.stream()
                        .filter(s -> s.getVaccine() != null && Objects.equals(s.getVaccine().getId(), v.getId()))
                        .findFirst().orElse(null);
                if (matchedSchedule != null && dob != null) {
                    ScheduleEntity start = matchedSchedule.getStartPeriod();
                    ScheduleEntity end = matchedSchedule.getEndPeriod();
                    Integer startMonths = start != null ? start.getPeriod() : null;
                    Integer endMonths = end != null ? end.getPeriod() : null;
                    if (startMonths != null) recommendedStartDate = dob.plusMonths(startMonths);
                    if (endMonths != null) recommendedEndDate = dob.plusMonths(endMonths);
                }

                UpcomingVaccineDTO up = new UpcomingVaccineDTO(
                        v.getId(),
                        v.getName(),
                        v.getDisease(),
                        v.getTotalDoses(),
                        doseNo + 1,
                        nextDose,
                        null,
                        null,
                        false,
                        true,
                        false,
                        recommendedStartDate,
                        recommendedEndDate
                );
                upcomingMap.put(v.getId(), up);
            }
        }

        // add schedule-based upcoming vaccines, excluding those already in upcomingMap
        for (VaccinescheduleEntity vs : schedules) {
            VaccineEntity v = vs.getVaccine();
            if (v == null) continue;
            Long vid = v.getId();
            if (upcomingMap.containsKey(vid)) continue; // already pending
            // skip if user already fully completed this vaccine
            boolean alreadyCompleted = byVaccine.getOrDefault(vid, Collections.emptyList()).stream().anyMatch(ue -> Boolean.TRUE.equals(ue.getCompleted()));
            if (alreadyCompleted) continue;

            // detect booster: user has completed primary (doseNumber == totalDoses) and time since last administered >= boosterAfterDays
            boolean booster = false;
            LocalDate boosterDate = null;
            if (Boolean.TRUE.equals(v.getBoosterRequired()) && v.getBoosterAfterDays() != null) {
                List<UserVaccineEntity> userEntries = byVaccine.getOrDefault(vid, Collections.emptyList());
                if (!userEntries.isEmpty()) {
                    // find latest administered date
                    UserVaccineEntity latest = userEntries.stream().filter(ue -> ue.getAdministeredDate() != null).max(Comparator.comparing(UserVaccineEntity::getAdministeredDate)).orElse(null);
                    if (latest != null && Boolean.TRUE.equals(latest.getCompleted())) {
                        boosterDate = latest.getAdministeredDate().plusDays(v.getBoosterAfterDays());
                        if (!boosterDate.isAfter(LocalDate.now())) {
                            booster = true;
                        }
                    }
                }
            }

            // recommended age start/end
            Integer startMonths = vs.getStartPeriod() != null ? vs.getStartPeriod().getPeriod() : null;
            Integer endMonths = vs.getEndPeriod() != null ? vs.getEndPeriod().getPeriod() : null;
            LocalDate recommendedStartDate = null;
            LocalDate recommendedEndDate = null;
            if (dob != null) {
                if (startMonths != null) recommendedStartDate = dob.plusMonths(startMonths);
                if (endMonths != null) recommendedEndDate = dob.plusMonths(endMonths);
            }

            // If booster and applicable, set nextDoseNumber and nextDoseDate accordingly
            Integer nextDoseNumber = 1;
            LocalDate nextDoseDate = null;
            if (booster) {
                nextDoseNumber = v.getTotalDoses() != null ? v.getTotalDoses() + 1 : null;
                nextDoseDate = boosterDate;
            }

            UpcomingVaccineDTO up = new UpcomingVaccineDTO(
                    v.getId(),
                    v.getName(),
                    v.getDisease(),
                    v.getTotalDoses(),
                    nextDoseNumber,
                    nextDoseDate,
                    startMonths,
                    endMonths,
                    Boolean.TRUE.equals(vs.getMandatory()),
                    !byVaccine.getOrDefault(vid, Collections.emptyList()).isEmpty(),
                    booster,
                    recommendedStartDate,
                    recommendedEndDate
            );
            upcomingMap.put(vid, up);
            if (upcomingMap.size() >= 5) break;
        }

        List<UpcomingVaccineDTO> upcoming = new ArrayList<>(upcomingMap.values()).stream().limit(5).collect(Collectors.toList());

        return new UserVaccineResponseDTO(administered, upcoming);
    }

    public VaccineDTO getVaccine(Long vaccineId) {
        VaccineEntity v = vaccineRepository.findById(vaccineId)
                .orElseThrow(() -> new RuntimeException("Vaccine not found"));
        return new VaccineDTO(v.getId(), v.getName(), v.getDisease(), v.getTotalDoses(), v.getGapBetweenDoses(), v.getBoosterRequired(), v.getBoosterAfterDays());
    }
}
