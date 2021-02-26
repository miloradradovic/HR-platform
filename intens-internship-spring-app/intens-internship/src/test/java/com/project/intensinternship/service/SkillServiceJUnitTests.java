package com.project.intensinternship.service;

import com.project.intensinternship.TestUtils;
import com.project.intensinternship.model.Candidate;
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

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SkillServiceJUnitTests {

    private MockMvc mockMvc;

    @Mock
    SkillRepository skillRepository;

    @Mock
    CandidateService candidateService;

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
        s1.setName("New skill");
        Skill s2 = new Skill();
        s2.setName("Java programming");
        skillsToFind.add(s1);
        skillsToFind.add(s2);

        Skill foundSkill = new Skill(1, "Java programming");

        given(skillRepository.findByName("Java programming")).willReturn(foundSkill);
        given(skillRepository.findByName("New skill")).willReturn(null);

        Set<Skill> found = skillService.getSkills(skillsToFind);

        for(Skill s : found){
            if (s.getName().equals("Java programming")){
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
        int skillId = 1;

        Skill shouldFind = new Skill();
        shouldFind.setId(1);
        shouldFind.setName("Java programming");

        given(skillRepository.findById(skillId)).willReturn(shouldFind);

        Skill found = skillService.findOne(skillId);

        assertEquals(shouldFind.getName(), found.getName());

    }

    @Test
    public void testFindOneFail(){
        int skillId = -1;

        given(skillRepository.findById(skillId)).willReturn(null);

        Skill found = skillService.findOne(skillId);

        assertNull(found);

    }

    @Test
    @Transactional
    @Rollback()
    public void testSaveOneSuccess(){
        Skill toSave = new Skill();
        toSave.setName("New skill");
        Skill saved = new Skill();
        saved.setName("New skill");
        saved.setId(21);
        given(skillRepository.save(toSave)).willReturn(saved);
        given(skillRepository.findByName(toSave.getName())).willReturn(null);

        Skill savedSkill = skillService.saveOne(toSave);

        assertEquals(21, savedSkill.getId());
    }

    @Test
    @Transactional
    public void testSaveOneFail(){
        Skill toSave = new Skill();
        toSave.setName("Java programming");
        given(skillRepository.findByName(toSave.getName())).willReturn(toSave);

        Skill savedSkill = skillService.saveOne(toSave);

        assertNull(savedSkill);
    }

    @Test
    @Transactional
    @Rollback()
    public void testDelete(){
        int skillId = 1;
        Skill skill = new Skill();
        skill.setId(1);

        given(skillRepository.findById(skillId)).willReturn(skill);

        boolean deleted = skillService.delete(skillId);

        assertTrue(deleted);
    }

    @Test
    @Transactional
    public void testDeleteFail(){
        int skillId = -1;

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
        int candidateId = 1;
        List<Skill> foundSkills = new ArrayList<>();
        Skill s1 = new Skill();
        s1.setId(1);
        Skill s2 = new Skill();
        s2.setId(2);
        Skill s3 = new Skill();
        s3.setId(7);
        Skill s4 = new Skill();
        s4.setId(15);
        Skill s5 = new Skill();
        s5.setId(17);
        foundSkills.add(s1);
        foundSkills.add(s2);
        foundSkills.add(s3);
        foundSkills.add(s4);
        foundSkills.add(s5);

        given(skillRepository.findByCandidate(candidateId)).willReturn(foundSkills);

        List<Skill> result = skillService.findByCandidate(candidateId);

        assertEquals(foundSkills.size(), result.size());

    }

}
