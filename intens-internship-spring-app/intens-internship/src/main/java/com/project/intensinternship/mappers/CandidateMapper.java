package com.project.intensinternship.mappers;

import com.project.intensinternship.dto.CandidateDTO;
import com.project.intensinternship.model.Candidate;
import com.project.intensinternship.model.Skill;

import java.util.Date;

public class CandidateMapper implements MapperInterface<Candidate, CandidateDTO> {
    @Override
    public Candidate toEntity(CandidateDTO dto) {

        Candidate candidate = new Candidate(dto.getId(), dto.getFullName(), dto.getDateOfBirth(), dto.getContactNumber(), dto.getEmail());
        for(String skillName : dto.getSkills()){
            Skill newSkill = new Skill(skillName);
            candidate.getSkillSet().add(newSkill);
        }
        return candidate;
    }

    @Override
    public CandidateDTO toDto(Candidate entity) {
        CandidateDTO candidateDTO = new CandidateDTO(entity.getId(), entity.getFullName(), entity.getDateOfBirth(), entity.getContactNumber(), entity.getEmail());
        for(Skill skill : entity.getSkillSet()){
            candidateDTO.getSkills().add(skill.getName());
        }
        return candidateDTO;
    }
}
