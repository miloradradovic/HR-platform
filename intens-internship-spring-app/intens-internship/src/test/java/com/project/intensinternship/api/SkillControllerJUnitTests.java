package com.project.intensinternship.api;

import com.project.intensinternship.TestUtils;
import com.project.intensinternship.dto.SkillDTO;
import com.project.intensinternship.mappers.SkillMapper;
import com.project.intensinternship.model.Skill;
import com.project.intensinternship.service.SkillService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SkillControllerJUnitTests {

    private MockMvc mockMvc;

    private SkillMapper skillMapper = new SkillMapper();

    private MediaType contentType = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Mock
    private SkillService skillService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @PostConstruct
    public void setup() {
        this.mockMvc = MockMvcBuilders.
                webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @Transactional
    @Rollback()
    public void testAddNewSkillSuccess() throws Exception {
        SkillDTO skillToSave = TestUtils.generateSkillToSave();
        Skill skillSaved = TestUtils.generateSkillSaved(skillToSave);
        String json = TestUtils.json(skillToSave);
        given(skillService.saveOne(this.skillMapper.toEntity(skillToSave))).willReturn(skillSaved);
        this.mockMvc.perform(post("/skills")
                .content(json)
                .contentType(contentType))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(skillSaved.getId())));
    }

    @Test
    @Transactional
    public void testAddNewSkillFail() throws Exception {
        SkillDTO skillToSave = TestUtils.generateSkillToSave();
        skillToSave.setName("Java programming");
        String json = TestUtils.json(skillToSave);
        given(skillService.saveOne(this.skillMapper.toEntity(skillToSave))).willReturn(null);
        this.mockMvc.perform(post("/skills")
                .content(json)
                .contentType(contentType))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void testGetSkillsByCandidateId() throws Exception {
        int candidateId = 1;
        List<Skill> skills = TestUtils.generateSkillsByCandidate();
        given(skillService.findByCandidate(candidateId)).willReturn(skills);
        this.mockMvc.perform(get("/skills/by-candidate/1")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)));
    }

    @Test
    @Transactional
    @Rollback()
    public void testDeleteSkillSuccess() throws Exception {
        int skillId = 1;
        given(skillService.delete(skillId)).willReturn(true);
        this.mockMvc.perform(delete("/skills/1")
                .contentType(contentType))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testDeleteSkillFail() throws Exception {
        int skillId = -1;
        given(skillService.delete(skillId)).willReturn(false);
        this.mockMvc.perform(delete("/skills/-1")
                .contentType(contentType))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void testGetAllSkills() throws Exception {
        ArrayList<Skill> skills = TestUtils.generateAllSkills();

        given(skillService.findAll()).willReturn(skills);
        this.mockMvc.perform(get("/skills")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(20)));
    }
}
