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
        dto.setFullName("test full name");
        dto.setEmail("test email");
        dto.setContactNumber("test contact number");
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
        candidate.setId(11);
        candidate.setSkillSet(new HashSet<>());
        candidate.setContactNumber(dto.getContactNumber());
        candidate.setDateOfBirth(dto.getDateOfBirth());
        candidate.setEmail(dto.getEmail());
        candidate.setFullName(dto.getFullName());
        return candidate;
    }

    public static SearchParamsDTO generateSearchParamsDTO(String param, boolean found){
        SearchParamsDTO searchParamsDTO = new SearchParamsDTO();
        if (param.equals("name") && found){
            searchParamsDTO.setParam("name");
            searchParamsDTO.setValue("Mar");
            return searchParamsDTO;
        }else if (param.equals("name") && !found){
            searchParamsDTO.setParam("name");
            searchParamsDTO.setValue("nonexistent");
            return searchParamsDTO;
        }else if (param.equals("skill1") && found){
            searchParamsDTO.setParam("skill");
            searchParamsDTO.setValue("Java programming");
            return searchParamsDTO;
        }else if (param.equals("skill1") && !found){
            searchParamsDTO.setParam("skill");
            searchParamsDTO.setValue("nonexistent");
            return searchParamsDTO;
        }else if (param.equals("skill2") && found){
            searchParamsDTO.setParam("skill");
            searchParamsDTO.setValue("Java programming,C# programming");
            return searchParamsDTO;
        }else { //param = skill2 and found = false
            searchParamsDTO.setParam("skill");
            searchParamsDTO.setValue("Java programming,Nonexistent");
            return searchParamsDTO;
        }
    }

    public static ArrayList<Candidate> generateSearchResult(String param, boolean found){
        if (found){
            ArrayList<Candidate> dtos = new ArrayList<>();
            if (param.equals("name")){
                Candidate candidateDTO1 = new Candidate();
                candidateDTO1.setFullName("Marko Markovic");
                Candidate candidateDTO2 = new Candidate();
                candidateDTO2.setFullName("Marija Markovic");
                dtos.add(candidateDTO1);
                dtos.add(candidateDTO2);
                return dtos;
            }else if (param.equals("skill1")){
                Candidate candidateDTO1 = new Candidate();
                candidateDTO1.setId(1);
                Candidate candidateDTO2 = new Candidate();
                candidateDTO2.setId(6);
                Candidate candidateDTO3 = new Candidate();
                candidateDTO3.setId(7);
                Candidate candidateDTO4 = new Candidate();
                candidateDTO4.setId(10);
                dtos.add(candidateDTO1);
                dtos.add(candidateDTO2);
                dtos.add(candidateDTO3);
                dtos.add(candidateDTO4);
                return dtos;
            }else{
                Candidate candidateDTO1 = new Candidate();
                candidateDTO1.setId(1);
                Candidate candidateDTO2 = new Candidate();
                candidateDTO2.setId(6);
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
        dto.setFullName("test full name");
        dto.setEmail("test email");
        dto.setContactNumber("test contact number");
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
        candidate.setId(1);
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
        dto.setName("New skill");
        return dto;
    }

    public static Skill generateSkillSaved(SkillDTO skillToSave) {
        Skill skill = new Skill();
        skill.setId(21);
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
        if(s.equals("java")){
            Candidate c1 = new Candidate();
            c1.setId(1);
            Candidate c2 = new Candidate();
            c2.setId(6);
            Candidate c3 = new Candidate();
            c3.setId(7);
            Candidate c4 = new Candidate();
            c4.setId(10);
            candidates.add(c1);
            candidates.add(c2);
            candidates.add(c3);
            candidates.add(c4);
            return candidates;
        }else{
            Candidate c1 = new Candidate();
            c1.setId(1);
            Candidate c2 = new Candidate();
            c2.setId(6);
            candidates.add(c1);
            candidates.add(c2);
            return candidates;
        }
    }
}
