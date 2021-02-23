package com.project.intensinternship.service;

import com.project.intensinternship.dto.SearchParamsDTO;
import com.project.intensinternship.model.Candidate;
import com.project.intensinternship.model.Skill;
import com.project.intensinternship.repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CandidateService implements ServiceInterface<Candidate> {

    @Autowired
    CandidateRepository candidateRepository;

    @Autowired
    SkillService skillService;


    @Override
    public Page<Candidate> findAll(Pageable pageable) {
        return candidateRepository.findAll(pageable);
    }

    @Override
    public Candidate findOne(int id) {
        return candidateRepository.findById(id).orElse(null);
    }

    @Override
    public Candidate saveOne(Candidate entity) {
        if(candidateRepository.findByEmail(entity.getEmail()) == null){
            Set<Skill> skills = skillService.getSkills(entity.getSkillSet());
            entity.setSkillSet(skills);
            return candidateRepository.save(entity);
        }else{
            return null;
        }
    }

    @Override
    public boolean delete(int id) {
        Candidate candidate = candidateRepository.findById(id).orElse(null);
        if(candidate == null){
            return false;
        }else{
            candidate.setSkillSet(new HashSet<>());
            candidateRepository.delete(candidate);
            return true;
        }
    }

    @Override
    public Candidate update(Candidate entity) {
        Candidate candidate = candidateRepository.findById(entity.getId()).orElse(null);
        if(candidate == null){
            return candidate;
        }else{
            Set<Skill> skills = skillService.getSkills(entity.getSkillSet());
            entity.setSkillSet(skills);
            return candidateRepository.save(entity);
        }
    }

    public Page<Candidate> searchCandidates(SearchParamsDTO searchParamsDTO, Pageable pageable){
        if(searchParamsDTO.getParam().equals("name")){
            Page<Candidate> candidates = candidateRepository.searchByName(searchParamsDTO.getValue(), pageable);
            return candidates;
        }else{
            String[] splitted = searchParamsDTO.getValue().split(",");
            if(splitted.length == 1){ // search by one
                Page<Candidate> candidates = candidateRepository.searchByOneSkill(searchParamsDTO.getValue(), pageable);
                return candidates;
            }else{
                Page<Candidate> candidates = candidateRepository.searchByMoreSkills(Arrays.asList(splitted), pageable);
                return candidates;
            }
        }
    }

    public boolean removeSkill(Integer candidateId, Integer skillId) {
        Candidate found = candidateRepository.findById(candidateId).orElse(null);
        int sizeBefore = found.getSkillSet().size();
        if(found == null){
            return false;
        }else{
            for(Skill skill : found.getSkillSet()){
                if(skill.getId() == skillId){
                    found.getSkillSet().remove(skill);
                    break;
                }
            }
        }
        if(found.getSkillSet().size() == sizeBefore){
            return false;
        }
        candidateRepository.save(found);
        return true;
    }
}
