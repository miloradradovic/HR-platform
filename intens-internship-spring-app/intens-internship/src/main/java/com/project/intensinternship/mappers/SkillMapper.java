package com.project.intensinternship.mappers;

import com.project.intensinternship.dto.SkillDTO;
import com.project.intensinternship.model.Skill;

import java.util.ArrayList;
import java.util.List;

public class SkillMapper implements MapperInterface<Skill, SkillDTO>{
    @Override
    public Skill toEntity(SkillDTO dto) {
        return new Skill(dto.getId(), dto.getName());
    }

    @Override
    public SkillDTO toDto(Skill entity) {
        return new SkillDTO(entity.getId(), entity.getName());
    }

    @Override
    public List<SkillDTO> toListDtos(List<Skill> entities) {
        ArrayList<SkillDTO> dtos = new ArrayList<SkillDTO>();
        for(Skill skill : entities) {
            SkillDTO dto = this.toDto(skill);
            dtos.add(dto);
        }
        return dtos;
    }
}
