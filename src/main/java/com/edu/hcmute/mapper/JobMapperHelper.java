package com.edu.hcmute.mapper;


import com.edu.hcmute.entity.*;
import com.edu.hcmute.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JobMapperHelper {
    private final FieldRepository fieldRepository;
    private final PositionRepository positionRepository;
    private final ExperienceRangeRepository experienceRangeRepository;
    private final SalaryRangeRepository salaryRangeRepository;
    private final MajorRepository majorRepository;
    private final CompanyRepository companyRepository;
    private final SkillRepository skillRepository;
    private final WorkModeRepository workModeRepository;
    private static final String CAN_NOT_PARSE_TIME = "Không thể chuyển đổi thời gian";

    public Field mapFieldIdToField(Integer fieldId) {
        if (fieldId != null) {
            return fieldRepository.findById(fieldId).orElse(null);
        }
        return null;
    }

    public Position mapPositionIdToPosition(Integer positionId) {
        if (positionId != null) {
            return positionRepository.findById(positionId).orElse(null);
        }
        return null;
    }

    public ExperienceRange mapExperienceRangeIdToExperienceRange(Integer experienceRangeId) {
        if (experienceRangeId != null) {
            return experienceRangeRepository.findById(experienceRangeId).orElse(null);
        }
        return null;
    }

    public SalaryRange mapSalaryIdToSalaryRange(Integer salaryRangeId) {
        if (salaryRangeId != null) {
            return salaryRangeRepository.findById(salaryRangeId).orElse(null);
        }
        return null;
    }

    public Major mapMajorIdToMajor(Integer majorId) {
        if (majorId != null) {
            return majorRepository.findById(majorId).orElse(null);
        }
        return null;
    }

    public Company mapCompanyIdToCompany(Integer companyId) {
        if (companyId != null) {
            return companyRepository.findById(companyId).orElse(null);
        }
        return null;
    }

    public List<Skill> mapSkillIdsToSkills(List<Integer> skillIds) {
        if (skillIds != null) {
            return skillIds.stream().map(skillId -> skillRepository.findById(skillId).orElse(null)).toList();
        }
        return null;
    }
    public WorkMode mapWorkModeIdToWorkMode(Integer workModeId) {
        if (workModeId != null) {
            return workModeRepository.findById(workModeId).orElse(null);
        }
        return null;
    }

    public Instant mapStringToInstant(String time) {
        DateTimeFormatter formatter;

        if (time.length() == 19) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        } else if (time.length() == 16) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        } else {
            log.error("Invalid time format: " + time);
            return null;
        }

        try {
            LocalDateTime localDateTime = LocalDateTime.parse(time, formatter);
            return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        } catch (Exception e) {
            log.error("Can't parse time: " + time, e);
            return null;
        }
    }

    public Integer mapDeadlineToRestAppliedDays(Instant deadline) {
        System.out.println(deadline);
        if (deadline != null) {
            Instant now = Instant.now();
            long diff = deadline.toEpochMilli() - now.toEpochMilli();
            return (int) (diff / (24 * 60 * 60 * 1000));
        }
        return null;
    }


}
