package com.project.intensinternship.service;

import com.project.intensinternship.dto.SearchParamsDTO;
import com.project.intensinternship.model.Candidate;
import com.project.intensinternship.model.Skill;
import com.project.intensinternship.repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        if(candidateRepository.findByEmail(entity.getEmail()) == null &&
                candidateRepository.findByContactNumber(entity.getContactNumber()) == null){ // email and contact number are unique
            Set<Skill> skills = skillService.getSkills(entity.getSkillSet()); // finds Skill objects in database and sets their id and name, otherwise returns Skill with the set name
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
            candidate.setSkillSet(new HashSet<>()); // removing rows from candidate-skill table
            candidateRepository.delete(candidate);
            return true;
        }
    }

    @Override
    public Candidate update(Candidate entity) {
        Candidate candidate = candidateRepository.findById(entity.getId());
        Candidate candidateEmail;
        Candidate candidateContactNumber;
        if(candidate == null){
            return null;
        }
        if (candidate.getContactNumber().equals(entity.getContactNumber()) || candidate.getEmail().equals(entity.getEmail())){ // if unique data wasn't changed
            candidateEmail = null;
            candidateContactNumber = null;
        }else if(!candidate.getEmail().equals(entity.getEmail()) && candidate.getContactNumber().equals(entity.getEmail())){ // if email was changed
            candidateEmail = candidateRepository.findByEmail(entity.getEmail());
            candidateContactNumber = null;
        }else if(candidate.getEmail().equals(entity.getEmail()) && !candidate.getContactNumber().equals(entity.getContactNumber())){ // if contact number was changed
            candidateEmail = null;
            candidateContactNumber = candidateRepository.findByContactNumber(entity.getContactNumber());
        }else{ // both were changed
            candidateEmail = candidateRepository.findByEmail(entity.getEmail());
            candidateContactNumber = candidateRepository.findByContactNumber(entity.getContactNumber());
        }
        if(candidateEmail != null || candidateContactNumber != null){
            return null;
        }else{
            Set<Skill> skills = skillService.getSkills(entity.getSkillSet()); // finds Skill objects in database and sets their id and name, otherwise returns Skill with the set name
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
            Set<Skill> skills = skillService.getSkills(entity.getSkillSet()); // finds Skill objects in database and sets their id and name, otherwise returns Skill with the set name
            candidate.setSkillSet(skills);
            return candidateRepository.save(candidate);
        }
    }

    public List<Candidate> searchCandidates(SearchParamsDTO searchParamsDTO){
        if(searchParamsDTO.getParam().equals("name")){
            return candidateRepository.searchByName(searchParamsDTO.getValue()); // search by name
        }else{ // search by skill
            String[] splitted = searchParamsDTO.getValue().split(","); // checking if it's search by one or more skills
            if(splitted.length == 1){ // search by one
                return candidateRepository.searchByOneSkill(searchParamsDTO.getValue());
            }else{ // search by more
                List<Candidate> searchResult = new ArrayList<>();
                int iterationNumber = 0;
                for(String skillName : splitted){ // for each skill do the search, then do the logical AND if necessary
                    iterationNumber += 1;
                    if (searchResult.size() == 0 && iterationNumber == 1){ // the first iteration
                        searchResult = candidateRepository.searchByOneSkill(skillName);
                    }else{
                        searchResult = searchLogicalAnd(searchResult, candidateRepository.searchByOneSkill(skillName)); // doing the logical AND - finding candidates that exist in both lists
                    }
                }
                return searchResult;
            }
        }
    }

    public List<Candidate> searchLogicalAnd(List<Candidate> searchResult, List<Candidate> searchByOneSkill) {
        List<Candidate> finalResult = new ArrayList<>();
        if (searchByOneSkill.size() < searchResult.size()){ // iterating will be done through the smaller list to lower the number of needed iterations
            for (Candidate candidate : searchByOneSkill){
                if (candidateInTheList(searchResult, candidate)){ // checking if candidate exists in the other list
                    finalResult.add(candidate);
                }
            }
        }else{
            for (Candidate candidate : searchResult){
                if (candidateInTheList(searchByOneSkill, candidate)){ // checking if candidate exists in the other list
                    finalResult.add(candidate);
                }
            }
        }
        return finalResult;
    }

    public boolean candidateInTheList(List<Candidate> list, Candidate candidate) {
        for(Candidate c : list){
            if(c.getId() == candidate.getId()){ // checking if candidate is in the list
                return true;
            }
        }
        return false;
    }

    public boolean removeSkill(int candidateId, Integer skillId) {
        Candidate found = candidateRepository.findById(candidateId);
        int sizeBefore;
        if(found == null){
            return false;
        }else{
            sizeBefore = found.getSkillSet().size();
            for(Skill skill : found.getSkillSet()){ // removes the skill from the candidate's skillset
                if(skill.getId() == skillId){
                    found.getSkillSet().remove(skill);
                    break;
                }
            }
        }
        if(found.getSkillSet().size() == sizeBefore){ // skill was not found, therefore return false
            return false;
        }
        candidateRepository.save(found);
        return true;
    }
}
