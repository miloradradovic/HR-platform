package com.project.intensinternship.service;

import com.project.intensinternship.model.Skill;
import com.project.intensinternship.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class SkillService implements ServiceInterface<Skill> {

    @Autowired
    SkillRepository skillRepository;

    public Set<Skill> getSkills(Set<Skill> skillSet) {
        Set<Skill> newSkills = new HashSet<>();
        for(Skill skill : skillSet){
            Skill foundSkill = skillRepository.findByName(skill.getName());
            if(foundSkill == null){
                newSkills.add(skill);
            }else{
                newSkills.add(foundSkill);
            }
        }
        return newSkills;
    }

    @Override
    public Page<Skill> findAll(Pageable pageable) {
        return skillRepository.findAll(pageable);
    }

    @Override
    public Skill findOne(int id) {
        return skillRepository.findById(id).orElse(null);
    }

    @Override
    public Skill saveOne(Skill entity) {
        Skill find = skillRepository.findByName(entity.getName());
        if(find == null){
            return skillRepository.save(entity);
        }else{
            return null;
        }
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public Skill update(Skill entity) {
        return null;
    }

    public Page<Skill> findByCandidate(Integer id, Pageable pageable) {
        return skillRepository.findByCandidate(id, pageable);
    }
}
