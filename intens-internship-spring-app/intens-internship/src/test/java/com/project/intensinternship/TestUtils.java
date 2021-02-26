package com.project.intensinternship;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.intensinternship.dto.CandidateDTO;
import com.project.intensinternship.dto.SearchParamsDTO;
import com.project.intensinternship.dto.SkillDTO;
import com.project.intensinternship.model.Candidate;
import com.project.intensinternship.model.Skill;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static com.project.intensinternship.constants.UnitConstants.*;

public class TestUtils {

    public static String json(Object object)
            throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return mapper.writeValueAsString(object);
    }

    public static CandidateDTO generateCandidateToSave(){
        CandidateDTO dto = new CandidateDTO();
        dto.setSkills(new ArrayList<>());
        dto.setFullName(NEW_CANDIDATE_NAME);
        dto.setEmail(NEW_CANDIDATE_EMAIL);
        dto.setContactNumber(NEW_CANDIDATE_CONTACT_NUMBER);
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        try{
            Date newDate = formatter.parse(formatter.format(new Date()));
            dto.setDateOfBirth(newDate);
        }catch(Exception e){
            e.printStackTrace();
        }
        return dto;
    }

    public static Candidate generateCandidateSaved(CandidateDTO dto){
        Candidate candidate = new Candidate();
        candidate.setId(NEW_CANDIDATE_SAVED_ID);
        candidate.setSkillSet(new HashSet<>());
        candidate.setContactNumber(dto.getContactNumber());
        candidate.setDateOfBirth(dto.getDateOfBirth());
        candidate.setEmail(dto.getEmail());
        candidate.setFullName(dto.getFullName());
        return candidate;
    }

    public static SearchParamsDTO generateSearchParamsDTO(String param, boolean found){
        SearchParamsDTO searchParamsDTO = new SearchParamsDTO();
        if (param.equals(SEARCH_PARAM_NAME) && found){
            searchParamsDTO.setParam(SEARCH_PARAM_NAME);
            searchParamsDTO.setValue(SEARCH_VALUE_NAME_FOUND);
            return searchParamsDTO;
        }else if (param.equals(SEARCH_PARAM_NAME) && !found){
            searchParamsDTO.setParam(SEARCH_PARAM_NAME);
            searchParamsDTO.setValue(SEARCH_VALUE_NAME_NOT_FOUND);
            return searchParamsDTO;
        }else if (param.equals(SEARCH_PARAM_SKILL_ONE) && found){
            searchParamsDTO.setParam(SEARCH_PARAM_SKILL);
            searchParamsDTO.setValue(SEARCH_VALUE_SKILL_ONE_FOUND);
            return searchParamsDTO;
        }else if (param.equals(SEARCH_PARAM_SKILL_ONE) && !found){
            searchParamsDTO.setParam(SEARCH_PARAM_SKILL);
            searchParamsDTO.setValue(SEARCH_VALUE_SKILL_ONE_NOT_FOUND);
            return searchParamsDTO;
        }else if (param.equals(SEARCH_PARAM_SKILL_MORE) && found){
            searchParamsDTO.setParam(SEARCH_PARAM_SKILL);
            searchParamsDTO.setValue(SEARCH_VALUE_SKILL_MORE_FOUND);
            return searchParamsDTO;
        }else { //param = skill2 and found = false
            searchParamsDTO.setParam(SEARCH_PARAM_SKILL);
            searchParamsDTO.setValue(SEARCH_VALUE_SKILL_MORE_NOT_FOUND);
            return searchParamsDTO;
        }
    }

    public static ArrayList<Candidate> generateSearchResult(String param, boolean found){
        if (found){
            ArrayList<Candidate> dtos = new ArrayList<>();
            if (param.equals(SEARCH_PARAM_NAME)){
                Candidate candidateDTO1 = new Candidate();
                candidateDTO1.setFullName(SEARCH_RESULT_NAME_ONE);
                Candidate candidateDTO2 = new Candidate();
                candidateDTO2.setFullName(SEARCH_RESULT_NAME_TWO);
                dtos.add(candidateDTO1);
                dtos.add(candidateDTO2);
                return dtos;
            }else if (param.equals(SEARCH_PARAM_SKILL_ONE)){
                Candidate candidateDTO1 = new Candidate();
                candidateDTO1.setId(SEARCH_RESULT_SKILL_ONE_ONE);
                Candidate candidateDTO2 = new Candidate();
                candidateDTO2.setId(SEARCH_RESULT_SKILL_ONE_TWO);
                Candidate candidateDTO3 = new Candidate();
                candidateDTO3.setId(SEARCH_RESULT_SKILL_ONE_THREE);
                Candidate candidateDTO4 = new Candidate();
                candidateDTO4.setId(SEARCH_RESULT_SKILL_ONE_FOUR);
                dtos.add(candidateDTO1);
                dtos.add(candidateDTO2);
                dtos.add(candidateDTO3);
                dtos.add(candidateDTO4);
                return dtos;
            }else{
                Candidate candidateDTO1 = new Candidate();
                candidateDTO1.setId(SEARCH_RESULT_SKILL_ONE_ONE);
                Candidate candidateDTO2 = new Candidate();
                candidateDTO2.setId(SEARCH_RESULT_SKILL_ONE_TWO);
                dtos.add(candidateDTO1);
                dtos.add(candidateDTO2);
                return dtos;
            }
        }else{
            return new ArrayList<>();
        }
    }

    public static CandidateDTO generateCandidateToUpdate() {
        CandidateDTO dto = new CandidateDTO();
        dto.setId(1);
        dto.setSkills(new ArrayList<>());
        dto.setFullName(NEW_CANDIDATE_NAME);
        dto.setEmail(NEW_CANDIDATE_EMAIL);
        dto.setContactNumber(NEW_CANDIDATE_CONTACT_NUMBER);
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        try{
            Date newDate = formatter.parse(formatter.format(new Date()));
            dto.setDateOfBirth(newDate);
        }catch(Exception e){
            e.printStackTrace();
        }
        return dto;
    }

    public static Candidate generateCandidateUpdated(CandidateDTO candidateToUpdate) {
        Candidate candidate = new Candidate();
        candidate.setId(NEW_CANDIDATE_UPDATED_ID);
        candidate.setSkillSet(new HashSet<>());
        candidate.setContactNumber(candidateToUpdate.getContactNumber());
        candidate.setDateOfBirth(candidateToUpdate.getDateOfBirth());
        candidate.setEmail(candidateToUpdate.getEmail());
        candidate.setFullName(candidateToUpdate.getFullName());
        return candidate;
    }

    public static ArrayList<Candidate> generateCandidates() {
        ArrayList<Candidate> candidates = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            Candidate candidate = new Candidate();
            candidates.add(candidate);
        }
        return candidates;
    }

    public static SkillDTO generateSkillToSave() {
        SkillDTO dto = new SkillDTO();
        dto.setName(NEW_SKILL_NAME);
        return dto;
    }

    public static Skill generateSkillSaved(SkillDTO skillToSave) {
        Skill skill = new Skill();
        skill.setId(NEW_SKILL_SAVED_ID);
        skill.setName(skillToSave.getName());
        return skill;
    }

    public static List<Skill> generateSkillsByCandidate() {
        ArrayList<Skill> skills = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            Skill s = new Skill();
            skills.add(s);
        }
        return skills;
    }

    public static ArrayList<Skill> generateAllSkills() {
        ArrayList<Skill> skills = new ArrayList<>();
        for(int i = 0; i < 20; i++){
            Skill s = new Skill();
            skills.add(s);
        }
        return skills;
    }

    public static List<Candidate> generateCandidatesBySkill(String s) {
        List<Candidate> candidates = new ArrayList<>();
        if(s.equals(GENERATE_CANDIDATES_BY_SKILLS_ONE)){
            Candidate c1 = new Candidate();
            c1.setId(CANDIDATE_BY_SKILL_JAVA_ONE);
            Candidate c2 = new Candidate();
            c2.setId(CANDIDATE_BY_SKILL_JAVA_TWO);
            Candidate c3 = new Candidate();
            c3.setId(CANDIDATE_BY_SKILL_JAVA_THREE);
            Candidate c4 = new Candidate();
            c4.setId(CANDIDATE_BY_SKILL_JAVA_FOUR);
            candidates.add(c1);
            candidates.add(c2);
            candidates.add(c3);
            candidates.add(c4);
            return candidates;
        }else{
            Candidate c1 = new Candidate();
            c1.setId(CANDIDATE_BY_SKILL_CSHARP_ONE);
            Candidate c2 = new Candidate();
            c2.setId(CANDIDATE_BY_SKILL_CSHARP_TWO);
            candidates.add(c1);
            candidates.add(c2);
            return candidates;
        }
    }
}
