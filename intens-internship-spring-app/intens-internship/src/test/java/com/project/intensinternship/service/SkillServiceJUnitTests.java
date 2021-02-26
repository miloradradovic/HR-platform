package com.project.intensinternship.service;

import com.project.intensinternship.TestUtils;
import com.project.intensinternship.model.Skill;
import com.project.intensinternship.repository.SkillRepository;
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

import javax.annotation.PostConstruct;
import java.util.*;

import static com.project.intensinternship.constants.UnitConstants.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SkillServiceJUnitTests {

    private MockMvc mockMvc;

    @Mock
    SkillRepository skillRepository;

    @InjectMocks
    SkillService skillService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @PostConstruct
    public void setup() {
        this.mockMvc = MockMvcBuilders.
                webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testGetSkills(){
        Set<Skill> skillsToFind = new HashSet<>();
        Skill s1 = new Skill();
        s1.setName(GET_SKILLS_NOT_EXIST);
        Skill s2 = new Skill();
        s2.setName(GET_SKILLS_EXISTS);
        skillsToFind.add(s1);
        skillsToFind.add(s2);

        Skill foundSkill = new Skill(GET_SKILLS_EXISTS_ID, GET_SKILLS_EXISTS);

        given(skillRepository.findByName(GET_SKILLS_EXISTS)).willReturn(foundSkill);
        given(skillRepository.findByName(GET_SKILLS_NOT_EXIST)).willReturn(null);

        Set<Skill> found = skillService.getSkills(skillsToFind);

        for(Skill s : found){
            if (s.getName().equals(GET_SKILLS_EXISTS)){
                assertEquals(foundSkill.getId(), s.getId());
            }
        }

    }

    @Test
    public void testFindAll(){
        List<Skill> skills = TestUtils.generateAllSkills();

        given(skillRepository.findAll()).willReturn(skills);

        List<Skill> found = skillService.findAll();

        assertEquals(skills.size(), found.size());

    }

    @Test
    public void testFindOne(){
        int skillId = GET_SKILL_BY_ID;

        Skill shouldFind = new Skill();
        shouldFind.setId(GET_SKILL_BY_ID);
        shouldFind.setName(GET_SKILL_BY_ID_NAME);

        given(skillRepository.findById(skillId)).willReturn(shouldFind);

        Skill found = skillService.findOne(skillId);

        assertEquals(shouldFind.getName(), found.getName());

    }

    @Test
    public void testFindOneFail(){
        int skillId = NON_EXISTENT_ID;

        given(skillRepository.findById(skillId)).willReturn(null);

        Skill found = skillService.findOne(skillId);

        assertNull(found);

    }

    @Test
    @Transactional
    @Rollback()
    public void testSaveOneSuccess(){
        Skill toSave = new Skill();
        toSave.setName(NEW_SKILL_NAME);
        Skill saved = new Skill();
        saved.setName(NEW_SKILL_NAME);
        saved.setId(NEW_SKILL_SAVED_ID);
        given(skillRepository.save(toSave)).willReturn(saved);
        given(skillRepository.findByName(toSave.getName())).willReturn(null);

        Skill savedSkill = skillService.saveOne(toSave);

        assertEquals(saved.getId(), savedSkill.getId());
    }

    @Test
    @Transactional
    public void testSaveOneFail(){
        Skill toSave = new Skill();
        toSave.setName(NEW_SKILL_FAIL_NAME);
        given(skillRepository.findByName(toSave.getName())).willReturn(toSave);

        Skill savedSkill = skillService.saveOne(toSave);

        assertNull(savedSkill);
    }

    @Test
    @Transactional
    @Rollback()
    public void testDelete(){
        int skillId = DELETE_SKILL_ID;
        Skill skill = new Skill();
        skill.setId(DELETE_SKILL_ID);

        given(skillRepository.findById(skillId)).willReturn(skill);

        boolean deleted = skillService.delete(skillId);

        assertTrue(deleted);
    }

    @Test
    @Transactional
    public void testDeleteFail(){
        int skillId = NON_EXISTENT_ID;

        given(skillRepository.findById(skillId)).willReturn(null);

        boolean deleted = skillService.delete(skillId);

        assertFalse(deleted);
    }

    @Test
    public void testUpdate(){ // method is not used, but had to be in the SkillService because of ServiceInterface. It always returns null.
        Skill skill = skillService.update(null);
        assertNull(skill);
    }

    @Test
    public void testFindByCandidate(){
        int candidateId = GET_SKILLS_BY_CANDIDATE_ID;
        List<Skill> foundSkills = TestUtils.generateSkillsByCandidate();

        given(skillRepository.findByCandidate(candidateId)).willReturn(foundSkills);

        List<Skill> result = skillService.findByCandidate(candidateId);

        assertEquals(foundSkills.size(), result.size());

    }

}
