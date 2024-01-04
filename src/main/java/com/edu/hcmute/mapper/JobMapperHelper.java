package com.edu.hcmute.mapper;


import com.edu.hcmute.entity.*;
import com.edu.hcmute.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JobMapperHelper {
    private final FieldRepository fieldRepository;
    private final PositionRepository positionRepository;
    private final ExperienceRangeRepository experienceRangeRepository;
    private final SalaryRangeRepository salaryRangeRepository;
    private final MajorRepository majorRepository;
    private final CompanyRepository companyRepository;
    private final SkillRepository skillRepository;
    private final WorkModeRepository workModeRepository;

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


}
