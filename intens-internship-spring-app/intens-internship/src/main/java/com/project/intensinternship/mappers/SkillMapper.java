package com.project.intensinternship.mappers;

import com.project.intensinternship.dto.SkillDTO;
import com.project.intensinternship.model.Skill;

public class SkillMapper implements MapperInterface<Skill, SkillDTO>{
    @Override
    public Skill toEntity(SkillDTO dto) {
        return new Skill(dto.getId(), dto.getName());
    }

    @Override
    public SkillDTO toDto(Skill entity) {
        return new SkillDTO(entity.getId(), entity.getName());
    }
}
