package com.project.intensinternship.service;

import com.project.intensinternship.model.Candidate;
import com.project.intensinternship.model.Skill;
import com.project.intensinternship.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SkillService implements ServiceInterface<Skill> {

    @Autowired
    SkillRepository skillRepository;

    @Autowired
    CandidateService candidateService;

    public Set<Skill> getSkills(Set<Skill> skillSet) { // finding skills in database if exist, otherwise initializes new skill and only sets the name
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
    public List<Skill> findAll() {
        return skillRepository.findAll();
    }

    @Override
    public Skill findOne(int id) {
        return skillRepository.findById(id);
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
    @Transactional
    public boolean delete(int id) {
        Skill found = skillRepository.findById(id);
        if(found == null){
            return false;
        }else{
            for(Candidate candidate : found.getCandidateSet()){
                Candidate candidateSet = candidateService.findOne(candidate.getId());
                for(Skill s : candidateSet.getSkillSet()){
                    if(s.getId() == id){
                        candidateSet.getSkillSet().remove(s); // removes row from candidate-skill table
                        candidateService.update(candidateSet); // then do the candidate update before doing the skill delete
                        break;
                    }
                }
            }
            skillRepository.delete(found);
            return true;
        }
    }

    @Override
    public Skill update(Skill entity) { // method is never used in the context of application. It has to be here because of the ServiceInterface.
        return null;
    }

    public List<Skill> findByCandidate(Integer id) {
        return skillRepository.findByCandidate(id);
    }
}
