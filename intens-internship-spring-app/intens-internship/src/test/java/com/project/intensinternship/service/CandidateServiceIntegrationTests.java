package com.project.intensinternship.service;

import com.project.intensinternship.dto.SearchParamsDTO;
import com.project.intensinternship.model.Candidate;
import com.project.intensinternship.model.Skill;
import com.project.intensinternship.repository.CandidateRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static com.project.intensinternship.constants.IntegrationConstants.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class CandidateServiceIntegrationTests {

    @Autowired
    CandidateRepository candidateRepository;

    @Autowired
    SkillService skillService;

    @Autowired
    CandidateService candidateService;

    @Test
    public void testFindAll(){
        List<Candidate> candidateDTOList = candidateService.findAll();
        assertEquals(GET_ALL_CANDIDATES_SIZE, candidateDTOList.size());
    }

    @Test
    public void testFindOneValid(){
        Candidate found = candidateService.findOne(GET_CANDIDATE_BY_ID_VALID);
        assertEquals(GET_CANDIDATE_BY_ID_VALID, found.getId());
        assertEquals(GET_CANDIDATE_BY_ID_EMAIL_VALID, found.getEmail());
    }

    @Test
    @Sql(scripts = "classpath:delete-candidate-h2.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testSaveOneValid(){
        Candidate saveCandidate = new Candidate(CREATE_NEW_CANDIDATE_NAME, new Date(), CREATE_NEW_CANDIDATE_CONTACT_NUMBER_VALID, CREATE_NEW_CANDIDATE_EMAIL_VALID, new HashSet<>());
        Candidate saved = candidateService.saveOne(saveCandidate);
        assertEquals(saveCandidate.getEmail(), saved.getEmail());
    }

    @Test
    public void testSaveOneInvalidEmail(){
        Candidate saveCandidate = new Candidate(CREATE_NEW_CANDIDATE_NAME, new Date(), CREATE_NEW_CANDIDATE_CONTACT_NUMBER_VALID, CREATE_NEW_CANDIDATE_EMAIL_INVALID, new HashSet<>());
        Candidate saved = candidateService.saveOne(saveCandidate);
        assertNull(saved);
    }

    @Test
    public void testSaveOneInvalidContactNumber(){
        Candidate saveCandidate = new Candidate(CREATE_NEW_CANDIDATE_NAME, new Date(), CREATE_NEW_CANDIDATE_CONTACT_NUMBER_INVALID, CREATE_NEW_CANDIDATE_EMAIL_VALID, new HashSet<>());
        Candidate saved = candidateService.saveOne(saveCandidate);
        assertNull(saved);
    }

    @Test
    @Sql(scripts = "classpath:add-candidate-h2.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testDeleteCandidateValid(){
        boolean deleted = candidateService.delete(DELETE_CANDIDATE_ID_VALID);
        assertTrue(deleted);
    }

    @Test
    public void testDeleteCandidateInvalid(){
        boolean deleted = candidateService.delete(DELETE_CANDIDATE_ID_INVALID);
        assertFalse(deleted);
    }

    @Test
    @Sql(scripts = "classpath:update-candidate-h2.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testUpdateCandidateValid(){
        Candidate updateCandidate = new Candidate(UPDATE_CANDIDATE_ID_VALID, UPDATE_CANDIDATE_NAME, new Date(), UPDATE_CANDIDATE_CONTACT_NUMBER_VALID, UPDATE_CANDIDATE_EMAIL_VALID, new HashSet<>());
        Candidate updated = candidateService.update(updateCandidate);
        assertEquals(updateCandidate.getEmail(), updated.getEmail());
        assertEquals(updateCandidate.getId(), updated.getId());
    }

    @Test
    public void testUpdateCandidateInvalidId(){
        Candidate updateCandidate = new Candidate(UPDATE_CANDIDATE_ID_INVALID, UPDATE_CANDIDATE_NAME, new Date(), UPDATE_CANDIDATE_CONTACT_NUMBER_VALID, UPDATE_CANDIDATE_EMAIL_VALID, new HashSet<>());
        Candidate updated = candidateService.update(updateCandidate);
        assertNull(updated);
    }

    @Test
    public void testUpdateCandidateInvalidEmail(){
        Candidate updateCandidate = new Candidate(UPDATE_CANDIDATE_ID_VALID, UPDATE_CANDIDATE_NAME, new Date(), UPDATE_CANDIDATE_CONTACT_NUMBER_VALID, UPDATE_CANDIDATE_EMAIL_INVALID, new HashSet<>());
        Candidate updated = candidateService.update(updateCandidate);
        assertNull(updated);
    }

    @Test
    public void testUpdateCandidateInvalidContactNumber(){
        Candidate updateCandidate = new Candidate(UPDATE_CANDIDATE_ID_VALID, UPDATE_CANDIDATE_NAME, new Date(), UPDATE_CANDIDATE_CONTACT_NUMBER_INVALID, UPDATE_CANDIDATE_EMAIL_VALID, new HashSet<>());
        Candidate updated = candidateService.update(updateCandidate);
        assertNull(updated);
    }

    @Test
    @Sql(scripts = "classpath:update-candidate-with-skills-h2.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testUpdateCandidateWithSkillValid(){
        Candidate updateCandidate = new Candidate(UPDATE_CANDIDATE_ID_VALID, UPDATE_CANDIDATE_NAME, new Date(), UPDATE_CANDIDATE_CONTACT_NUMBER_VALID, UPDATE_CANDIDATE_EMAIL_VALID, new HashSet<>());
        Skill skill = new Skill(UPDATE_CANDIDATE_WITH_SKILL_NAME);
        updateCandidate.getSkillSet().add(skill);
        Candidate updated = candidateService.updateSkills(updateCandidate);
        assertEquals(UPDATE_CANDIDATE_ID_VALID, updated.getId());
    }

    @Test
    public void testUpdateCandidateWithSkillInvalidId(){
        Candidate updateCandidate = new Candidate(UPDATE_CANDIDATE_ID_INVALID, UPDATE_CANDIDATE_NAME, new Date(), UPDATE_CANDIDATE_CONTACT_NUMBER_VALID, UPDATE_CANDIDATE_EMAIL_VALID, new HashSet<>());
        Skill skill = new Skill(UPDATE_CANDIDATE_WITH_SKILL_NAME);
        updateCandidate.getSkillSet().add(skill);
        Candidate updated = candidateService.updateSkills(updateCandidate);
        assertNull(updated);
    }

    @Test
    public void testSearchByNameFound(){
        SearchParamsDTO searchParamsDTO = new SearchParamsDTO(SEARCH_CANDIDATES_PARAM_NAME, SEARCH_CANDIDATES_VALUE_NAME_FOUND);
        List<Candidate> searchResult = candidateService.searchCandidates(searchParamsDTO);
        assertEquals(SEARCH_CANDIDATES_PARAM_NAME_RESULT_COUNT, searchResult.size());
    }

    @Test
    public void testSearchByNameNotFound(){
        SearchParamsDTO searchParamsDTO = new SearchParamsDTO(SEARCH_CANDIDATES_PARAM_NAME, SEARCH_CANDIDATES_VALUE_NAME_NOT_FOUND);
        List<Candidate> searchResult = candidateService.searchCandidates(searchParamsDTO);
        assertEquals(EMPTY_LIST_SIZE, searchResult.size());
    }

    @Test
    public void testSearchBySkillOneFound(){
        SearchParamsDTO searchParamsDTO = new SearchParamsDTO(SEARCH_CANDIDATES_PARAM_SKILL, SEARCH_CANDIDATES_VALUE_ONE_SKILL_FOUND);
        List<Candidate> searchResult = candidateService.searchCandidates(searchParamsDTO);
        assertEquals(SEARCH_CANDIDATES_PARAM_SKILL_ONE_RESULT_COUNT, searchResult.size());
    }

    @Test
    public void testSearchBySkillOneNotFound(){
        SearchParamsDTO searchParamsDTO = new SearchParamsDTO(SEARCH_CANDIDATES_PARAM_SKILL, SEARCH_CANDIDATES_VALUE_ONE_SKILL_NOT_FOUND);
        List<Candidate> searchResult = candidateService.searchCandidates(searchParamsDTO);
        assertEquals(EMPTY_LIST_SIZE, searchResult.size());
    }

    @Test
    public void testSearchBySkillsMoreFound(){
        SearchParamsDTO searchParamsDTO = new SearchParamsDTO(SEARCH_CANDIDATES_PARAM_SKILL, SEARCH_CANDIDATES_VALUE_MORE_SKILLS_FOUND);
        List<Candidate> searchResult = candidateService.searchCandidates(searchParamsDTO);
        assertEquals(SEARCH_CANDIDATES_PARAM_SKILLS_MORE_RESULT_COUNT, searchResult.size());
    }

    @Test
    public void testSearchBySkillsMoreNotFound(){
        SearchParamsDTO searchParamsDTO = new SearchParamsDTO(SEARCH_CANDIDATES_PARAM_SKILL, SEARCH_CANDIDATES_VALUE_MORE_SKILLS_NOT_FOUND);
        List<Candidate> searchResult = candidateService.searchCandidates(searchParamsDTO);
        assertEquals(EMPTY_LIST_SIZE, searchResult.size());
    }

    @Test
    public void testLogicalAndFirstCase(){
        List<Candidate> candidates1 = new ArrayList<>();
        List<Candidate> candidates2 = new ArrayList<>();
        Candidate c1 = candidateService.findOne(LOGICAL_AND_CANDIDATE_ONE);
        Candidate c2 = candidateService.findOne(LOGICAL_AND_CANDIDATE_TWO);
        Candidate c3 = candidateService.findOne(LOGICAL_AND_CANDIDATE_THREE);
        candidates1.add(c1);
        candidates1.add(c2);
        candidates1.add(c3);
        candidates2.add(c1);
        candidates2.add(c3);
        List<Candidate> result = candidateService.searchLogicalAnd(candidates1, candidates2);
        assertEquals(LOGICAL_AND_RESULT_SIZE, result.size());
    }

    @Test
    public void testLogicalAndSecondCase(){
        List<Candidate> candidates1 = new ArrayList<>();
        List<Candidate> candidates2 = new ArrayList<>();
        Candidate c1 = candidateService.findOne(LOGICAL_AND_CANDIDATE_ONE);
        Candidate c2 = candidateService.findOne(LOGICAL_AND_CANDIDATE_TWO);
        Candidate c3 = candidateService.findOne(LOGICAL_AND_CANDIDATE_THREE);
        candidates2.add(c1);
        candidates2.add(c2);
        candidates2.add(c3);
        candidates1.add(c1);
        candidates1.add(c3);
        List<Candidate> result = candidateService.searchLogicalAnd(candidates1, candidates2);
        assertEquals(LOGICAL_AND_RESULT_SIZE, result.size());
    }

    @Test
    public void testCandidateInTheList(){
        List<Candidate> candidates1 = new ArrayList<>();
        Candidate c1 = candidateService.findOne(LOGICAL_AND_CANDIDATE_ONE);
        Candidate c2 = candidateService.findOne(LOGICAL_AND_CANDIDATE_TWO);
        Candidate c3 = candidateService.findOne(LOGICAL_AND_CANDIDATE_THREE);
        candidates1.add(c1);
        candidates1.add(c2);
        candidates1.add(c3);

        boolean found = candidateService.candidateInTheList(candidates1, c1);
        assertTrue(found);
    }

    @Test
    public void testCandidateInTheListNot(){
        List<Candidate> candidates1 = new ArrayList<>();
        Candidate c1 = candidateService.findOne(LOGICAL_AND_CANDIDATE_ONE);
        Candidate c2 = candidateService.findOne(LOGICAL_AND_CANDIDATE_TWO);
        Candidate c3 = candidateService.findOne(LOGICAL_AND_CANDIDATE_THREE);
        candidates1.add(c1);
        candidates1.add(c2);

        boolean found = candidateService.candidateInTheList(candidates1, c3);
        assertFalse(found);
    }

    @Test
    @Sql(scripts = "classpath:add-skill-to-candidate-h2.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testRemoveSkillFromCandidateValid(){
        boolean done = candidateService.removeSkill(REMOVE_SKILL_FROM_CANDIDATE_VALID_CID, REMOVE_SKILL_FROM_CANDIDATE_VALID_SID);
        assertTrue(done);
    }

    @Test
    public void testRemoveSkillFromCandidateInvalidCandidate(){
        boolean done = candidateService.removeSkill(REMOVE_SKILL_FROM_CANDIDATE_INVALID_CID, REMOVE_SKILL_FROM_CANDIDATE_VALID_SID);
        assertFalse(done);
    }

    @Test
    public void testRemoveSkillFromCandidateInvalidSkill(){
        boolean done = candidateService.removeSkill(REMOVE_SKILL_FROM_CANDIDATE_VALID_CID, REMOVE_SKILL_FROM_CANDIDATE_INVALID_SID);
        assertFalse(done);
    }

}
