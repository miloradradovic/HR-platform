package com.project.intensinternship.service;

import com.project.intensinternship.TestUtils;
import com.project.intensinternship.dto.CandidateDTO;
import com.project.intensinternship.dto.SearchParamsDTO;
import com.project.intensinternship.model.Candidate;
import com.project.intensinternship.model.Skill;
import com.project.intensinternship.repository.CandidateRepository;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        shouldFind.setId(1);
        shouldFind.setEmail("radovic.milorad1998@gmail.com");

        given(candidateRepository.findById(shouldFind.getId())).willReturn(shouldFind);

        Candidate found = candidateService.findOne(1);

        assertEquals(shouldFind.getEmail(), found.getEmail());

    }

    @Test
    public void testFindOneFail(){
        Candidate shouldFind = new Candidate();
        shouldFind.setId(-1);
        shouldFind.setEmail("radovic.milorad1998@gmail.com");

        given(candidateRepository.findById(shouldFind.getId())).willReturn(null);

        Candidate found = candidateService.findOne(-1);

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

        assertEquals(11, savedCandidate.getId());
    }

    @Test
    @Transactional
    public void testSaveOneFailEmail(){
        Candidate toSave = TestUtils.generateCandidateSaved(TestUtils.generateCandidateToSave());
        toSave.setEmail("radovic.milorad1998@gmail.com");
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
        toSave.setContactNumber("+381621127650");
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
        int candidateId = 1;
        Candidate candidate = new Candidate();
        candidate.setId(1);

        given(candidateRepository.findById(candidateId)).willReturn(candidate);

        boolean deleted = candidateService.delete(1);

        assertTrue(deleted);
    }

    @Test
    @Transactional
    public void testDeleteFail(){
        int candidateId = -1;
        Candidate candidate = new Candidate();
        candidate.setId(-1);

        given(candidateRepository.findById(candidateId)).willReturn(null);

        boolean deleted = candidateService.delete(-1);

        assertFalse(deleted);
    }

    @Test
    @Transactional
    @Rollback()
    public void testUpdateSuccess(){
        Candidate toUpdate = TestUtils.generateCandidateSaved(TestUtils.generateCandidateToSave());
        toUpdate.setId(1);
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
        toUpdate.setEmail("radovic.milorad1998@gmail.com");
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
        toUpdate.setContactNumber("+381621127650");
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
        toUpdate.setId(1);
        Skill s = new Skill();
        s.setName("update skill");
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
        toUpdate.setId(-1);
        Skill s = new Skill();
        s.setName("update skill");
        toUpdate.getSkillSet().add(s);
        given(candidateRepository.findById(toUpdate.getId())).willReturn(null);

        Candidate savedCandidate = candidateService.updateSkills(toUpdate);
        assertNull(savedCandidate);
    }

    @Test
    @Transactional
    public void testSearchByNameFound(){
        SearchParamsDTO searchParamsDTO = TestUtils.generateSearchParamsDTO("name", true);
        List<Candidate> candidates = TestUtils.generateSearchResult("name", true);
        given(candidateRepository.searchByName(searchParamsDTO.getValue())).willReturn(candidates);

        List<Candidate> found = candidateService.searchCandidates(searchParamsDTO);
        assertEquals(found.size(), candidates.size());
    }

    @Test
    @Transactional
    public void testSearchByNameNotFound(){
        SearchParamsDTO searchParamsDTO = TestUtils.generateSearchParamsDTO("name", false);
        List<Candidate> candidates = TestUtils.generateSearchResult("name", false);
        given(candidateRepository.searchByName(searchParamsDTO.getValue())).willReturn(candidates);

        List<Candidate> found = candidateService.searchCandidates(searchParamsDTO);
        assertEquals(found.size(), candidates.size());
    }

    @Test
    @Transactional
    public void testSearchByOneSkillFound(){
        SearchParamsDTO searchParamsDTO = TestUtils.generateSearchParamsDTO("skill1", true);
        List<Candidate> candidates = TestUtils.generateSearchResult("skill1", true);
        given(candidateRepository.searchByOneSkill(searchParamsDTO.getValue())).willReturn(candidates);

        List<Candidate> found = candidateService.searchCandidates(searchParamsDTO);
        assertEquals(found.size(), candidates.size());
    }

    @Test
    @Transactional
    public void testSearchByOneSkillNotFound(){
        SearchParamsDTO searchParamsDTO = TestUtils.generateSearchParamsDTO("skill1", false);
        List<Candidate> candidates = TestUtils.generateSearchResult("skill1", false);
        given(candidateRepository.searchByOneSkill(searchParamsDTO.getValue())).willReturn(candidates);

        List<Candidate> found = candidateService.searchCandidates(searchParamsDTO);
        assertEquals(found.size(), candidates.size());
    }

    @Test
    @Transactional
    public void testSearchByMoreSkillsFound(){
        SearchParamsDTO searchParamsDTO = TestUtils.generateSearchParamsDTO("skill2", true);
        List<Candidate> candidates = TestUtils.generateSearchResult("skill2", true);
        List<Candidate> candidatesJava = TestUtils.generateCandidatesBySkill("java");
        List<Candidate> candidatesCSharp = TestUtils.generateCandidatesBySkill("c#");

        given(candidateRepository.searchByOneSkill("Java programming")).willReturn(candidatesJava);
        given(candidateRepository.searchByOneSkill("C# programming")).willReturn(candidatesCSharp);

        List<Candidate> found = candidateService.searchCandidates(searchParamsDTO);
        assertEquals(candidates.size(), found.size());
    }

    @Test
    @Transactional
    public void testSearchLogicalAndFirstCase(){
        List<Candidate> searchResult = new ArrayList<>();
        Candidate c1 = new Candidate();
        c1.setId(1);
        Candidate c2 = new Candidate();
        c2.setId(2);
        searchResult.add(c1);
        searchResult.add(c2);

        List<Candidate> searchByOneSkill = new ArrayList<>();
        Candidate c3 = new Candidate();
        c3.setId(1);
        searchByOneSkill.add(c3);

        List<Candidate> found = candidateService.searchLogicalAnd(searchResult, searchByOneSkill);
        assertEquals(found.size(), 1);
    }

    @Test
    @Transactional
    public void testSearchLogicalAndSecondCase(){
        List<Candidate> searchResult = new ArrayList<>();
        Candidate c1 = new Candidate();
        c1.setId(1);
        searchResult.add(c1);

        List<Candidate> searchByOneSkill = new ArrayList<>();
        Candidate c2 = new Candidate();
        c2.setId(2);
        Candidate c3 = new Candidate();
        c3.setId(1);
        searchByOneSkill.add(c2);
        searchByOneSkill.add(c3);

        List<Candidate> found = candidateService.searchLogicalAnd(searchResult, searchByOneSkill);
        assertEquals(found.size(), 1);
    }

    @Test
    @Transactional
    public void testCandidateInTheListTrue(){
        List<Candidate> list = new ArrayList<>();
        Candidate c1 = new Candidate();
        c1.setId(1);
        Candidate c2 = new Candidate();
        c2.setId(2);
        Candidate c3 = new Candidate();
        c3.setId(3);
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
        c1.setId(1);
        Candidate c2 = new Candidate();
        c2.setId(2);
        Candidate c3 = new Candidate();
        c3.setId(3);
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
        toUpdate.setId(1);
        Skill s = new Skill();
        s.setName("update skill");
        s.setId(1);
        toUpdate.getSkillSet().add(s);
        given(candidateRepository.save(toUpdate)).willReturn(toUpdate);
        given(candidateRepository.findById(toUpdate.getId())).willReturn(toUpdate);

        boolean removedSkill = candidateService.removeSkill(1, 1);
        assertTrue(removedSkill);
    }

    @Test
    @Transactional
    public void testRemoveSkillFailCandidateId(){
        Candidate toUpdate = TestUtils.generateCandidateSaved(TestUtils.generateCandidateToSave());
        toUpdate.setId(-1);
        Skill s = new Skill();
        s.setName("update skill");
        s.setId(1);
        toUpdate.getSkillSet().add(s);
        given(candidateRepository.findById(toUpdate.getId())).willReturn(null);

        boolean removedSkill = candidateService.removeSkill(-1, 1);
        assertFalse(removedSkill);
    }

    @Test
    @Transactional
    public void testRemoveSkillFailSkillId(){
        Candidate toUpdate = TestUtils.generateCandidateSaved(TestUtils.generateCandidateToSave());
        toUpdate.setId(-1);
        Skill s = new Skill();
        s.setName("update skill");
        s.setId(1);
        toUpdate.getSkillSet().add(s);
        given(candidateRepository.findById(toUpdate.getId())).willReturn(null);

        boolean removedSkill = candidateService.removeSkill(1, -1);
        assertFalse(removedSkill);
    }

}
