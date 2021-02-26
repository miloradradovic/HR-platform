package com.project.intensinternship.service;

import com.project.intensinternship.TestUtils;
import com.project.intensinternship.dto.SearchParamsDTO;
import com.project.intensinternship.model.Candidate;
import com.project.intensinternship.model.Skill;
import com.project.intensinternship.repository.CandidateRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import static com.project.intensinternship.constants.UnitConstants.*;

import javax.annotation.PostConstruct;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CandidateServiceJUnitTests {

    private MockMvc mockMvc;

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private SkillService skillService;

    @InjectMocks
    private CandidateService candidateService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @PostConstruct
    public void setup() {
        this.mockMvc = MockMvcBuilders.
                webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testFindAll(){
        List<Candidate> candidates = TestUtils.generateCandidates();

        given(candidateRepository.findAll()).willReturn(candidates);

        List<Candidate> found = candidateService.findAll();

        assertEquals(candidates.size(), found.size());

    }

    @Test
    public void testFindOne(){
        Candidate shouldFind = new Candidate();
        shouldFind.setId(GET_CANDIDATE_BY_ID);
        shouldFind.setEmail(CANDIDATE_EXIST_EMAIL);

        given(candidateRepository.findById(shouldFind.getId())).willReturn(shouldFind);

        Candidate found = candidateService.findOne(GET_CANDIDATE_BY_ID);

        assertEquals(shouldFind.getEmail(), found.getEmail());

    }

    @Test
    public void testFindOneFail(){
        Candidate shouldFind = new Candidate();
        shouldFind.setId(NON_EXISTENT_ID);
        shouldFind.setEmail(CANDIDATE_EXIST_EMAIL);

        given(candidateRepository.findById(shouldFind.getId())).willReturn(null);

        Candidate found = candidateService.findOne(NON_EXISTENT_ID);

        assertNull(found);

    }

    @Test
    @Transactional
    @Rollback()
    public void testSaveOneSuccess(){
        Candidate toSave = TestUtils.generateCandidateSaved(TestUtils.generateCandidateToSave());
        given(candidateRepository.save(toSave)).willReturn(toSave);
        given(candidateRepository.findByEmail(toSave.getEmail())).willReturn(null);
        given(candidateRepository.findByContactNumber(toSave.getContactNumber())).willReturn(null);

        Candidate savedCandidate = candidateService.saveOne(toSave);

        assertEquals(NEW_CANDIDATE_SAVED_ID, savedCandidate.getId());
    }

    @Test
    @Transactional
    public void testSaveOneFailEmail(){
        Candidate toSave = TestUtils.generateCandidateSaved(TestUtils.generateCandidateToSave());
        toSave.setEmail(CANDIDATE_EXIST_EMAIL);
        given(candidateRepository.save(toSave)).willReturn(null);
        given(candidateRepository.findByEmail(toSave.getEmail())).willReturn(toSave);
        given(candidateRepository.findByContactNumber(toSave.getContactNumber())).willReturn(null);

        Candidate savedCandidate = candidateService.saveOne(toSave);

        assertNull(savedCandidate);
    }

    @Test
    @Transactional
    public void testSaveOneFailContactNumber(){
        Candidate toSave = TestUtils.generateCandidateSaved(TestUtils.generateCandidateToSave());
        toSave.setContactNumber(CANDIDATE_EXIST_CONTACT_NUMBER);
        given(candidateRepository.save(toSave)).willReturn(null);
        given(candidateRepository.findByEmail(toSave.getEmail())).willReturn(null);
        given(candidateRepository.findByContactNumber(toSave.getContactNumber())).willReturn(toSave);

        Candidate savedCandidate = candidateService.saveOne(toSave);

        assertNull(savedCandidate);
    }

    @Test
    @Transactional
    @Rollback()
    public void testDelete(){
        Candidate candidate = new Candidate();
        candidate.setId(DELETE_CANDIDATE_ID);

        given(candidateRepository.findById(DELETE_CANDIDATE_ID)).willReturn(candidate);

        boolean deleted = candidateService.delete(DELETE_CANDIDATE_ID);

        assertTrue(deleted);
    }

    @Test
    @Transactional
    public void testDeleteFail(){
        Candidate candidate = new Candidate();
        candidate.setId(NON_EXISTENT_ID);

        given(candidateRepository.findById(NON_EXISTENT_ID)).willReturn(null);

        boolean deleted = candidateService.delete(NON_EXISTENT_ID);

        assertFalse(deleted);
    }

    @Test
    @Transactional
    @Rollback()
    public void testUpdateSuccess(){
        Candidate toUpdate = TestUtils.generateCandidateSaved(TestUtils.generateCandidateToSave());
        toUpdate.setId(NEW_CANDIDATE_UPDATED_ID);
        given(candidateRepository.save(toUpdate)).willReturn(toUpdate);
        given(candidateRepository.findByEmail(toUpdate.getEmail())).willReturn(null);
        given(candidateRepository.findByContactNumber(toUpdate.getContactNumber())).willReturn(null);
        given(candidateRepository.findById(toUpdate.getId())).willReturn(toUpdate);

        Candidate savedCandidate = candidateService.update(toUpdate);
        assertEquals(toUpdate.getContactNumber(), savedCandidate.getContactNumber());
    }

    @Test
    @Transactional
    public void testUpdateFailEmail(){
        Candidate toUpdate = TestUtils.generateCandidateSaved(TestUtils.generateCandidateToSave());
        toUpdate.setEmail(CANDIDATE_UPDATE_EXIST_EMAIL);
        given(candidateRepository.save(toUpdate)).willReturn(null);
        given(candidateRepository.findByEmail(toUpdate.getEmail())).willReturn(toUpdate);
        given(candidateRepository.findByContactNumber(toUpdate.getContactNumber())).willReturn(null);
        given(candidateRepository.findById(toUpdate.getId())).willReturn(toUpdate);

        Candidate savedCandidate = candidateService.update(toUpdate);

        assertNull(savedCandidate);
    }

    @Test
    @Transactional
    public void testUpdateFailContactNumber(){
        Candidate toUpdate = TestUtils.generateCandidateSaved(TestUtils.generateCandidateToSave());
        toUpdate.setContactNumber(CANDIDATE_UPDATE_EXIST_CONTACT_NUMBER);
        given(candidateRepository.save(toUpdate)).willReturn(null);
        given(candidateRepository.findByEmail(toUpdate.getEmail())).willReturn(null);
        given(candidateRepository.findByContactNumber(toUpdate.getContactNumber())).willReturn(toUpdate);
        given(candidateRepository.findById(toUpdate.getId())).willReturn(toUpdate);

        Candidate savedCandidate = candidateService.update(toUpdate);

        assertNull(savedCandidate);
    }

    @Test
    @Transactional
    @Rollback()
    public void testUpdateSkillsSuccess(){
        Candidate toUpdate = TestUtils.generateCandidateSaved(TestUtils.generateCandidateToSave());
        toUpdate.setId(NEW_CANDIDATE_UPDATED_ID);
        Skill s = new Skill();
        s.setName(SKILL_TO_UPDATE);
        toUpdate.getSkillSet().add(s);
        given(candidateRepository.save(toUpdate)).willReturn(toUpdate);
        given(candidateRepository.findById(toUpdate.getId())).willReturn(toUpdate);

        Candidate savedCandidate = candidateService.updateSkills(toUpdate);
        assertEquals(toUpdate.getSkillSet().size(), savedCandidate.getSkillSet().size());
    }

    @Test
    @Transactional
    public void testUpdateSkillsFail(){
        Candidate toUpdate = TestUtils.generateCandidateSaved(TestUtils.generateCandidateToSave());
        toUpdate.setId(NON_EXISTENT_ID);
        Skill s = new Skill();
        s.setName(SKILL_TO_UPDATE);
        toUpdate.getSkillSet().add(s);
        given(candidateRepository.findById(toUpdate.getId())).willReturn(null);

        Candidate savedCandidate = candidateService.updateSkills(toUpdate);
        assertNull(savedCandidate);
    }

    @Test
    @Transactional
    public void testSearchByNameFound(){
        SearchParamsDTO searchParamsDTO = TestUtils.generateSearchParamsDTO(SEARCH_PARAM_NAME, true);
        List<Candidate> candidates = TestUtils.generateSearchResult(SEARCH_PARAM_NAME, true);
        given(candidateRepository.searchByName(searchParamsDTO.getValue())).willReturn(candidates);

        List<Candidate> found = candidateService.searchCandidates(searchParamsDTO);
        assertEquals(candidates.size(), found.size());
    }

    @Test
    @Transactional
    public void testSearchByNameNotFound(){
        SearchParamsDTO searchParamsDTO = TestUtils.generateSearchParamsDTO(SEARCH_PARAM_NAME, false);
        List<Candidate> candidates = TestUtils.generateSearchResult(SEARCH_PARAM_NAME, false);
        given(candidateRepository.searchByName(searchParamsDTO.getValue())).willReturn(candidates);

        List<Candidate> found = candidateService.searchCandidates(searchParamsDTO);
        assertEquals(candidates.size(), found.size());
    }

    @Test
    @Transactional
    public void testSearchByOneSkillFound(){
        SearchParamsDTO searchParamsDTO = TestUtils.generateSearchParamsDTO(SEARCH_PARAM_SKILL_ONE, true);
        List<Candidate> candidates = TestUtils.generateSearchResult(SEARCH_PARAM_SKILL_ONE, true);
        given(candidateRepository.searchByOneSkill(searchParamsDTO.getValue())).willReturn(candidates);

        List<Candidate> found = candidateService.searchCandidates(searchParamsDTO);
        assertEquals(candidates.size(), found.size());
    }

    @Test
    @Transactional
    public void testSearchByOneSkillNotFound(){
        SearchParamsDTO searchParamsDTO = TestUtils.generateSearchParamsDTO(SEARCH_PARAM_SKILL_ONE, false);
        List<Candidate> candidates = TestUtils.generateSearchResult(SEARCH_PARAM_SKILL_ONE, false);
        given(candidateRepository.searchByOneSkill(searchParamsDTO.getValue())).willReturn(candidates);

        List<Candidate> found = candidateService.searchCandidates(searchParamsDTO);
        assertEquals(candidates.size(), found.size());
    }

    @Test
    @Transactional
    public void testSearchByMoreSkillsFound(){
        SearchParamsDTO searchParamsDTO = TestUtils.generateSearchParamsDTO(SEARCH_PARAM_SKILL_MORE, true);
        List<Candidate> candidates = TestUtils.generateSearchResult(SEARCH_PARAM_SKILL_MORE, true);
        List<Candidate> candidatesJava = TestUtils.generateCandidatesBySkill(GENERATE_CANDIDATES_BY_SKILLS_ONE);
        List<Candidate> candidatesCSharp = TestUtils.generateCandidatesBySkill(GENERATE_CANDIDATES_BY_SKILLS_TWO);

        given(candidateRepository.searchByOneSkill(SEARCH_VALUE_SKILL_ONE_FOUND)).willReturn(candidatesJava);
        given(candidateRepository.searchByOneSkill(SEARCH_VALUE_SKILL_TWO_FOUND)).willReturn(candidatesCSharp);

        List<Candidate> found = candidateService.searchCandidates(searchParamsDTO);
        assertEquals(candidates.size(), found.size());
    }

    @Test
    @Transactional
    public void testSearchLogicalAndFirstCase(){
        List<Candidate> searchResult = new ArrayList<>();
        Candidate c1 = new Candidate();
        c1.setId(CANDIDATE_LIST_ID_ONE);
        Candidate c2 = new Candidate();
        c2.setId(CANDIDATE_LIST_ID_TWO);
        searchResult.add(c1);
        searchResult.add(c2);

        List<Candidate> searchByOneSkill = new ArrayList<>();
        Candidate c3 = new Candidate();
        c3.setId(CANDIDATE_LIST_ID_ONE);
        searchByOneSkill.add(c3);

        List<Candidate> found = candidateService.searchLogicalAnd(searchResult, searchByOneSkill);
        assertEquals(LOGICAL_AND_RESULT_SIZE, found.size());
    }

    @Test
    @Transactional
    public void testSearchLogicalAndSecondCase(){
        List<Candidate> searchResult = new ArrayList<>();
        Candidate c1 = new Candidate();
        c1.setId(CANDIDATE_LIST_ID_ONE);
        searchResult.add(c1);

        List<Candidate> searchByOneSkill = new ArrayList<>();
        Candidate c2 = new Candidate();
        c2.setId(CANDIDATE_LIST_ID_TWO);
        Candidate c3 = new Candidate();
        c3.setId(CANDIDATE_LIST_ID_ONE);
        searchByOneSkill.add(c2);
        searchByOneSkill.add(c3);

        List<Candidate> found = candidateService.searchLogicalAnd(searchResult, searchByOneSkill);
        assertEquals(LOGICAL_AND_RESULT_SIZE, found.size());
    }

    @Test
    @Transactional
    public void testCandidateInTheListTrue(){
        List<Candidate> list = new ArrayList<>();
        Candidate c1 = new Candidate();
        c1.setId(CANDIDATE_LIST_ID_ONE);
        Candidate c2 = new Candidate();
        c2.setId(CANDIDATE_LIST_ID_TWO);
        Candidate c3 = new Candidate();
        c3.setId(CANDIDATE_LIST_ID_THREE);
        list.add(c1);
        list.add(c2);
        list.add(c3);

        boolean found = candidateService.candidateInTheList(list, c1);
        assertTrue(found);
    }

    @Test
    @Transactional
    public void testCandidateInTheListFalse(){
        List<Candidate> list = new ArrayList<>();
        Candidate c1 = new Candidate();
        c1.setId(CANDIDATE_LIST_ID_ONE);
        Candidate c2 = new Candidate();
        c2.setId(CANDIDATE_LIST_ID_TWO);
        Candidate c3 = new Candidate();
        c3.setId(CANDIDATE_LIST_ID_THREE);
        list.add(c1);
        list.add(c2);

        boolean found = candidateService.candidateInTheList(list, c3);
        assertFalse(found);
    }

    @Test
    @Transactional
    @Rollback()
    public void testRemoveSkillSuccess(){
        Candidate toUpdate = TestUtils.generateCandidateSaved(TestUtils.generateCandidateToSave());
        toUpdate.setId(REMOVE_SKILL_FROM_CANDIDATE_CID);
        Skill s = new Skill();
        s.setName(SKILL_TO_UPDATE);
        s.setId(REMOVE_SKILL_FROM_CANDIDATE_SID);
        toUpdate.getSkillSet().add(s);
        given(candidateRepository.save(toUpdate)).willReturn(toUpdate);
        given(candidateRepository.findById(toUpdate.getId())).willReturn(toUpdate);

        boolean removedSkill = candidateService.removeSkill(toUpdate.getId(), s.getId());
        assertTrue(removedSkill);
    }

    @Test
    @Transactional
    public void testRemoveSkillFailCandidateId(){
        Candidate toUpdate = TestUtils.generateCandidateSaved(TestUtils.generateCandidateToSave());
        toUpdate.setId(NON_EXISTENT_ID);
        Skill s = new Skill();
        s.setName(SKILL_TO_UPDATE);
        s.setId(REMOVE_SKILL_FROM_CANDIDATE_SID);
        toUpdate.getSkillSet().add(s);
        given(candidateRepository.findById(toUpdate.getId())).willReturn(null);

        boolean removedSkill = candidateService.removeSkill(toUpdate.getId(), s.getId());
        assertFalse(removedSkill);
    }

    @Test
    @Transactional
    public void testRemoveSkillFailSkillId(){
        Candidate toUpdate = TestUtils.generateCandidateSaved(TestUtils.generateCandidateToSave());
        toUpdate.setId(REMOVE_SKILL_FROM_CANDIDATE_CID);
        Skill s = new Skill();
        s.setName(SKILL_TO_UPDATE);
        s.setId(NON_EXISTENT_ID);
        toUpdate.getSkillSet().add(s);
        given(candidateRepository.findById(toUpdate.getId())).willReturn(null);

        boolean removedSkill = candidateService.removeSkill(toUpdate.getId(), s.getId());
        assertFalse(removedSkill);
    }

}
