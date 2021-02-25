package com.project.intensinternship.service;

import com.project.intensinternship.dto.SearchParamsDTO;
import com.project.intensinternship.model.Candidate;
import com.project.intensinternship.model.Skill;
import com.project.intensinternship.repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CandidateService implements ServiceInterface<Candidate> {

    @Autowired
    CandidateRepository candidateRepository;

    @Autowired
    SkillService skillService;

    @Override
    public List<Candidate> findAll() {
        return candidateRepository.findAll();
    }

    @Override
    public Candidate findOne(int id) {
        return candidateRepository.findById(id);
    }

    @Override
    public Candidate saveOne(Candidate entity) {
        if(candidateRepository.findByEmail(entity.getEmail()) == null && candidateRepository.findByContactNumber(entity.getContactNumber()) == null){
            Set<Skill> skills = skillService.getSkills(entity.getSkillSet());
            entity.setSkillSet(skills);
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            try{
                Date newDate = formatter.parse(formatter.format(entity.getDateOfBirth()));
                entity.setDateOfBirth(newDate);
            }catch(Exception e){
                return null;
            }
            return candidateRepository.save(entity);
        }else{
            return null;
        }
    }

    @Override
    public boolean delete(int id) {
        Candidate candidate = candidateRepository.findById(id);
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
        Candidate candidate = candidateRepository.findById(entity.getId());
        Candidate candidateEmail = new Candidate();
        Candidate candidateContactNumber = new Candidate();
        if(candidate == null){
            return null;
        }
        if (candidate.getContactNumber().equals(entity.getContactNumber()) || candidate.getEmail().equals(entity.getEmail())){
            candidateEmail = null;
            candidateContactNumber = null;
        }else if(!candidate.getEmail().equals(entity.getEmail()) && candidate.getContactNumber().equals(entity.getEmail())){
            candidateEmail = candidateRepository.findByEmail(entity.getEmail());
            candidateContactNumber = null;
        }else if(candidate.getEmail().equals(entity.getEmail()) && !candidate.getContactNumber().equals(entity.getContactNumber())){
            candidateEmail = null;
            candidateContactNumber = candidateRepository.findByContactNumber(entity.getContactNumber());
        }else{
            candidateEmail = candidateRepository.findByEmail(entity.getEmail());
            candidateContactNumber = candidateRepository.findByContactNumber(entity.getContactNumber());
        }
        if(candidateEmail != null || candidateContactNumber != null){
            return null;
        }else{
            Set<Skill> skills = skillService.getSkills(entity.getSkillSet());
            candidate.setSkillSet(skills);
            candidate.setFullName(entity.getFullName());
            candidate.setEmail(entity.getEmail());
            candidate.setDateOfBirth(entity.getDateOfBirth());
            candidate.setContactNumber(entity.getContactNumber());
            return candidateRepository.save(candidate);
        }
    }

    public Candidate updateSkills(Candidate entity) {
        Candidate candidate = candidateRepository.findById(entity.getId());
        if(candidate == null){
            return null;
        }else{
            Set<Skill> skills = skillService.getSkills(entity.getSkillSet());
            candidate.setSkillSet(skills);
            return candidateRepository.save(candidate);
        }
    }

    public List<Candidate> searchCandidates(SearchParamsDTO searchParamsDTO){
        if(searchParamsDTO.getParam().equals("name")){
            return candidateRepository.searchByName(searchParamsDTO.getValue());
        }else{
            String[] splitted = searchParamsDTO.getValue().split(",");
            if(splitted.length == 1){ // search by one
                return candidateRepository.searchByOneSkill(searchParamsDTO.getValue());
            }else{
                List<Candidate> searchResult = new ArrayList<>();
                for(String skillName : splitted){
                    if (searchResult.size() == 0){
                        searchResult = candidateRepository.searchByOneSkill(skillName);
                    }else{
                        searchResult = searchLogicalAnd(searchResult, candidateRepository.searchByOneSkill(skillName));
                    }
                }
                return searchResult;
            }
        }
    }

    private List<Candidate> searchLogicalAnd(List<Candidate> searchResult, List<Candidate> searchByOneSkill) {
        List<Candidate> finalResult = new ArrayList<>();
        if (searchByOneSkill.size() < searchResult.size()){
            for (Candidate candidate : searchByOneSkill){
                if (candidateInTheList(searchResult, candidate)){
                    finalResult.add(candidate);
                }
            }
            return finalResult;
        }else{
            for (Candidate candidate : searchResult){
                if (candidateInTheList(searchByOneSkill, candidate)){
                    finalResult.add(candidate);
                }
            }
            return finalResult;
        }
    }

    private boolean candidateInTheList(List<Candidate> list, Candidate candidate) {
        for(Candidate c : list){
            if(c.getId() == candidate.getId()){
                return true;
            }
        }
        return false;
    }

    public boolean removeSkill(Integer candidateId, Integer skillId) {
        Candidate found = candidateRepository.findById(candidateId).orElse(null);
        int sizeBefore;
        if(found == null){
            return false;
        }else{
            sizeBefore = found.getSkillSet().size();
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
