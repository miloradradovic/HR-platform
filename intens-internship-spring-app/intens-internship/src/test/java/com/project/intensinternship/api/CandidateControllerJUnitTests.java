package com.project.intensinternship.api;

import com.project.intensinternship.TestUtils;
import com.project.intensinternship.dto.CandidateDTO;
import com.project.intensinternship.dto.SearchParamsDTO;
import com.project.intensinternship.mappers.CandidateMapper;
import com.project.intensinternship.model.Candidate;
import com.project.intensinternship.service.CandidateService;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CandidateControllerJUnitTests {

    private MockMvc mockMvc;

    private CandidateMapper candidateMapper = new CandidateMapper();

    private MediaType contentType = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Mock
    private CandidateService candidateService;

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
    public void testAddNewCandidateSuccess() throws Exception {
        CandidateDTO candidateToSave = TestUtils.generateCandidateToSave();
        Candidate candidateSaved = TestUtils.generateCandidateSaved(candidateToSave);
        String json = TestUtils.json(candidateToSave);
        given(candidateService.saveOne(this.candidateMapper.toEntity(candidateToSave))).willReturn(candidateSaved);
        this.mockMvc.perform(post("/candidates")
                .content(json)
                .contentType(contentType))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(candidateSaved.getId())));
    }

    @Test
    @Transactional
    public void testAddNewCandidateEmailFail() throws Exception {
        CandidateDTO candidateToSave = TestUtils.generateCandidateToSave();
        candidateToSave.setContactNumber("+381621127651");
        Candidate candidateSaved = TestUtils.generateCandidateSaved(candidateToSave);
        String json = TestUtils.json(candidateToSave);

        given(candidateService.saveOne(this.candidateMapper.toEntity(candidateToSave))).willReturn(null);
        this.mockMvc.perform(post("/candidates")
                .content(json)
                .contentType(contentType))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void testAddNewCandidateContactNumberFail() throws Exception {
        CandidateDTO candidateToSave = TestUtils.generateCandidateToSave();
        candidateToSave.setEmail("radovic.milorad1998@gmail.com");
        Candidate candidateSaved = TestUtils.generateCandidateSaved(candidateToSave);
        String json = TestUtils.json(candidateToSave);

        given(candidateService.saveOne(this.candidateMapper.toEntity(candidateToSave))).willReturn(null);
        this.mockMvc.perform(post("/candidates")
                .content(json)
                .contentType(contentType))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void testSearchByNameFound() throws Exception {
        SearchParamsDTO searchParamsDTO = TestUtils.generateSearchParamsDTO("name", true);
        ArrayList<Candidate> result = TestUtils.generateSearchResult("name", true);
        String json = TestUtils.json(searchParamsDTO);

        given(candidateService.searchCandidates(searchParamsDTO)).willReturn(result);
        this.mockMvc.perform(post("/candidates/search")
                .content(json)
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @Transactional
    public void testSearchByNameNotFound() throws Exception {
        SearchParamsDTO searchParamsDTO = TestUtils.generateSearchParamsDTO("name", false);
        ArrayList<Candidate> result = TestUtils.generateSearchResult("name", false);
        String json = TestUtils.json(searchParamsDTO);

        given(candidateService.searchCandidates(searchParamsDTO)).willReturn(result);
        this.mockMvc.perform(post("/candidates/search")
                .content(json)
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @Transactional
    public void testSearchByOneSkillFound() throws Exception {
        SearchParamsDTO searchParamsDTO = TestUtils.generateSearchParamsDTO("skill1", true);
        ArrayList<Candidate> result = TestUtils.generateSearchResult("skill1", true);
        String json = TestUtils.json(searchParamsDTO);

        given(candidateService.searchCandidates(searchParamsDTO)).willReturn(result);
        this.mockMvc.perform(post("/candidates/search")
                .content(json)
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)));
    }

    @Test
    @Transactional
    public void testSearchByOneSkillNotFound() throws Exception {
        SearchParamsDTO searchParamsDTO = TestUtils.generateSearchParamsDTO("skill1", false);
        ArrayList<Candidate> result = TestUtils.generateSearchResult("skill1", false);
        String json = TestUtils.json(searchParamsDTO);

        given(candidateService.searchCandidates(searchParamsDTO)).willReturn(result);
        this.mockMvc.perform(post("/candidates/search")
                .content(json)
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @Transactional
    public void testSearchByMoreSkillsFound() throws Exception {
        SearchParamsDTO searchParamsDTO = TestUtils.generateSearchParamsDTO("skill2", true);
        ArrayList<Candidate> result = TestUtils.generateSearchResult("skill2", true);
        String json = TestUtils.json(searchParamsDTO);

        given(candidateService.searchCandidates(searchParamsDTO)).willReturn(result);
        this.mockMvc.perform(post("/candidates/search")
                .content(json)
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @Transactional
    public void testSearchByMoreSkillsNotFound() throws Exception {
        SearchParamsDTO searchParamsDTO = TestUtils.generateSearchParamsDTO("skill2", false);
        ArrayList<Candidate> result = TestUtils.generateSearchResult("skill2", false);
        String json = TestUtils.json(searchParamsDTO);

        given(candidateService.searchCandidates(searchParamsDTO)).willReturn(result);
        this.mockMvc.perform(post("/candidates/search")
                .content(json)
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @Transactional
    @Rollback()
    public void testUpdateCandidateSuccess() throws Exception {
        CandidateDTO candidateToUpdate = TestUtils.generateCandidateToUpdate();
        Candidate candidateUpdated = TestUtils.generateCandidateUpdated(candidateToUpdate);
        String json = TestUtils.json(candidateToUpdate);
        given(candidateService.update(this.candidateMapper.toEntity(candidateToUpdate))).willReturn(candidateUpdated);
        this.mockMvc.perform(put("/candidates/skills")
                .content(json)
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(candidateUpdated.getId())))
                .andExpect(jsonPath("$.fullName", is(candidateUpdated.getFullName())))
                .andExpect(jsonPath("$.contactNumber", is(candidateUpdated.getContactNumber())))
                .andExpect(jsonPath("$.email", is(candidateUpdated.getEmail())));
    }

    @Test
    @Transactional
    public void testUpdateCandidateFailId() throws Exception {
        CandidateDTO candidateToUpdate = TestUtils.generateCandidateToUpdate();
        candidateToUpdate.setId(-1);
        Candidate candidateUpdated = TestUtils.generateCandidateUpdated(candidateToUpdate);
        String json = TestUtils.json(candidateToUpdate);
        given(candidateService.update(this.candidateMapper.toEntity(candidateToUpdate))).willReturn(null);
        this.mockMvc.perform(put("/candidates")
                .content(json)
                .contentType(contentType))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void testUpdateCandidateFailEmail() throws Exception {
        CandidateDTO candidateToUpdate = TestUtils.generateCandidateToUpdate();
        candidateToUpdate.setEmail("pera.peric@gmail.com");
        Candidate candidateUpdated = TestUtils.generateCandidateUpdated(candidateToUpdate);
        String json = TestUtils.json(candidateToUpdate);
        given(candidateService.update(this.candidateMapper.toEntity(candidateToUpdate))).willReturn(null);
        this.mockMvc.perform(put("/candidates")
                .content(json)
                .contentType(contentType))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void testUpdateCandidateFailContactNumber() throws Exception {
        CandidateDTO candidateToUpdate = TestUtils.generateCandidateToUpdate();
        candidateToUpdate.setContactNumber("+381621127651");
        Candidate candidateUpdated = TestUtils.generateCandidateUpdated(candidateToUpdate);
        String json = TestUtils.json(candidateToUpdate);
        given(candidateService.update(this.candidateMapper.toEntity(candidateToUpdate))).willReturn(null);
        this.mockMvc.perform(put("/candidates")
                .content(json)
                .contentType(contentType))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    @Rollback()
    public void testUpdateCandidateWithSkillSuccess() throws Exception {
        CandidateDTO candidateToUpdate = TestUtils.generateCandidateToUpdate();
        String skill = "skill to update";
        candidateToUpdate.getSkills().add(skill);
        Candidate candidateUpdated = TestUtils.generateCandidateUpdated(candidateToUpdate);
        String json = TestUtils.json(candidateToUpdate);
        given(candidateService.update(this.candidateMapper.toEntity(candidateToUpdate))).willReturn(candidateUpdated);
        this.mockMvc.perform(put("/candidates")
                .content(json)
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(candidateUpdated.getId())))
                .andExpect(jsonPath("$.fullName", is(candidateUpdated.getFullName())))
                .andExpect(jsonPath("$.contactNumber", is(candidateUpdated.getContactNumber())))
                .andExpect(jsonPath("$.email", is(candidateUpdated.getEmail())));
    }


    @Test
    @Transactional
    @Rollback()
    public void testRemoveSkillFromCandidateSuccess() throws Exception {
        int candidateId = 1;
        int skillId = 2;
        given(candidateService.removeSkill(candidateId, skillId)).willReturn(true);
        this.mockMvc.perform(delete("/candidates/1/2")
                .contentType(contentType))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testRemoveSkillFromCandidateFail() throws Exception {
        int candidateId = -1;
        int skillId = 2;
        given(candidateService.removeSkill(candidateId, skillId)).willReturn(false);
        this.mockMvc.perform(delete("/candidates/-1/2")
                .contentType(contentType))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void testRemoveSkillFromSkillFail() throws Exception {
        int candidateId = 1;
        int skillId = 3;
        given(candidateService.removeSkill(candidateId, skillId)).willReturn(false);
        this.mockMvc.perform(delete("/candidates/1/3")
                .contentType(contentType))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    @Rollback()
    public void testDeleteCandidateSuccess() throws Exception {
        int candidateId = 1;
        given(candidateService.delete(candidateId)).willReturn(true);
        this.mockMvc.perform(delete("/candidates/1")
                .contentType(contentType))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testDeleteCandidateFail() throws Exception {
        int candidateId = -1;
        given(candidateService.delete(candidateId)).willReturn(true);
        this.mockMvc.perform(delete("/candidates/-1")
                .contentType(contentType))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void testGetAllCandidates() throws Exception {
        ArrayList<Candidate> candidates = TestUtils.generateCandidates();

        given(candidateService.findAll()).willReturn(candidates);
        this.mockMvc.perform(get("/candidates")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(10)));
    }

    @Test
    @Transactional
    public void testGetCandidateByIdSuccess() throws Exception {
        int candidateId = 1;
        Candidate candidate = new Candidate();
        candidate.setId(candidateId);
        given(candidateService.findOne(candidateId)).willReturn(candidate);
        this.mockMvc.perform(get("/candidates/by-id/1")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(candidate.getId())));
    }

    @Test
    @Transactional
    public void testGetCandidateByIdFail() throws Exception {
        int candidateId = -1;
        Candidate candidate = new Candidate();
        candidate.setId(candidateId);
        given(candidateService.findOne(candidateId)).willReturn(null);
        this.mockMvc.perform(get("/candidates/by-id/-1")
                .contentType(contentType))
                .andExpect(status().isBadRequest());
    }

}
