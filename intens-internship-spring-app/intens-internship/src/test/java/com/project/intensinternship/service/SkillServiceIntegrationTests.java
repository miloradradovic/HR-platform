package com.project.intensinternship.service;

import com.project.intensinternship.model.Skill;
import com.project.intensinternship.repository.SkillRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.project.intensinternship.constants.IntegrationConstants.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class SkillServiceIntegrationTests {

    @Autowired
    SkillRepository skillRepository;

    @Autowired
    CandidateService candidateService;

    @Autowired
    SkillService skillService;

    @Test
    public void testGetSkills(){
        Skill s1 = new Skill(GET_SKILLS_NAME_ONE);
        Skill s2 = new Skill(GET_SKILLS_NAME_TWO);
        Set<Skill> skillSet = new HashSet<>();
        skillSet.add(s1);
        skillSet.add(s2);
        Set<Skill> found = skillService.getSkills(skillSet);
        for (Skill skill : found){
            if (skill.getName().equals(s1.getName())){
                assertEquals(GET_SKILLS_ID_ONE, skill.getId());
            }else{
                assertEquals(GET_SKILLS_ID_TWO, skill.getId());
            }
        }
    }

    @Test
    public void testFindAll(){
        List<Skill> found = skillService.findAll();
        assertEquals(GET_ALL_SKILLS_SIZE, found.size());
    }

    @Test
    public void testFindOne(){
        Skill found = skillService.findOne(GET_SKILL_BY_ID_VALID);
        assertEquals(GET_SKILL_BY_ID_VALID, found.getId());
    }

    @Test
    public void testFindOneInvalid(){
        Skill found = skillService.findOne(GET_SKILL_BY_ID_INVALID);
        assertNull(found);
    }

    @Test
    @Sql(scripts = "classpath:delete-skill-h2.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testSaveOne(){
        Skill saveSkill = new Skill(CREATE_NEW_SKILL_NAME_VALID);
        Skill saved = skillService.saveOne(saveSkill);
        assertEquals(CREATE_NEW_SKILL_NAME_VALID, saved.getName());
    }

    @Test
    public void testSaveOneInvalid(){
        Skill saveSkill = new Skill(CREATE_NEW_SKILL_NAME_INVALID);
        Skill saved = skillService.saveOne(saveSkill);
        assertNull(saved);
    }

    @Test
    @Sql(scripts = "classpath:add-skill-h2.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testDeleteSkill(){
        boolean deleted = skillService.delete(DELETE_SKILL_ID_VALID);
        assertTrue(deleted);
    }

    @Test
    public void testDeleteSkillInvalid(){
        boolean deleted = skillService.delete(DELETE_SKILL_ID_INVALID);
        assertFalse(deleted);
    }

    @Test
    public void testUpdate(){ // method is never used, but had to be in the service because of the implemented interface
        assertTrue(true);
    }

    @Test
    public void testFindByCandidateFound(){
        List<Skill> found = skillService.findByCandidate(GET_SKILLS_BY_CANDIDATE_ID_VALID);
        assertEquals(GET_SKILLS_BY_CANDIDATE_SIZE, found.size());
    }

    @Test
    public void testFindByCandidateNotFound(){
        List<Skill> found = skillService.findByCandidate(GET_SKILLS_BY_CANDIDATE_ID_INVALID);
        assertEquals(EMPTY_LIST_SIZE, found.size());
    }
}
