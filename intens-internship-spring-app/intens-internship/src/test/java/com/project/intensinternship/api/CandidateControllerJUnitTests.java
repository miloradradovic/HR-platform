package com.project.intensinternship.api;

import com.project.intensinternship.TestUtils;
import com.project.intensinternship.dto.CandidateDTO;
import com.project.intensinternship.dto.SearchParamsDTO;
import com.project.intensinternship.mappers.CandidateMapper;
import com.project.intensinternship.model.Candidate;
import com.project.intensinternship.service.CandidateService;

import static com.project.intensinternship.constants.UnitConstants.*;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CandidateControllerJUnitTests {

    private MockMvc mockMvc;

    private final CandidateMapper candidateMapper = new CandidateMapper();

    private final MediaType contentType = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            StandardCharsets.UTF_8);

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
    public void testAddNewCandidateContactNumberFail() throws Exception {
        CandidateDTO candidateToSave = TestUtils.generateCandidateToSave();
        candidateToSave.setContactNumber(CANDIDATE_EXIST_CONTACT_NUMBER);
        String json = TestUtils.json(candidateToSave);

        given(candidateService.saveOne(this.candidateMapper.toEntity(candidateToSave))).willReturn(null);
        this.mockMvc.perform(post("/candidates")
                .content(json)
                .contentType(contentType))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void testAddNewCandidateEmailFail() throws Exception {
        CandidateDTO candidateToSave = TestUtils.generateCandidateToSave();
        candidateToSave.setEmail(CANDIDATE_EXIST_EMAIL);
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
        SearchParamsDTO searchParamsDTO = TestUtils.generateSearchParamsDTO(SEARCH_PARAM_NAME, true);
        ArrayList<Candidate> result = TestUtils.generateSearchResult(SEARCH_PARAM_NAME, true);
        String json = TestUtils.json(searchParamsDTO);

        given(candidateService.searchCandidates(searchParamsDTO)).willReturn(result);
        this.mockMvc.perform(post("/candidates/search")
                .content(json)
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(SEARCH_COUNT_NAME_FOUND)));
    }

    @Test
    @Transactional
    public void testSearchByNameNotFound() throws Exception {
        SearchParamsDTO searchParamsDTO = TestUtils.generateSearchParamsDTO(SEARCH_PARAM_NAME, false);
        ArrayList<Candidate> result = TestUtils.generateSearchResult(SEARCH_PARAM_NAME, false);
        String json = TestUtils.json(searchParamsDTO);

        given(candidateService.searchCandidates(searchParamsDTO)).willReturn(result);
        this.mockMvc.perform(post("/candidates/search")
                .content(json)
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(EMPTY_LIST_SIZE)));
    }

    @Test
    @Transactional
    public void testSearchByOneSkillFound() throws Exception {
        SearchParamsDTO searchParamsDTO = TestUtils.generateSearchParamsDTO(SEARCH_PARAM_SKILL_ONE, true);
        ArrayList<Candidate> result = TestUtils.generateSearchResult(SEARCH_PARAM_SKILL_ONE, true);
        String json = TestUtils.json(searchParamsDTO);

        given(candidateService.searchCandidates(searchParamsDTO)).willReturn(result);
        this.mockMvc.perform(post("/candidates/search")
                .content(json)
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(SEARCH_COUNT_ONE_SKILL)));
    }

    @Test
    @Transactional
    public void testSearchByOneSkillNotFound() throws Exception {
        SearchParamsDTO searchParamsDTO = TestUtils.generateSearchParamsDTO(SEARCH_PARAM_SKILL_ONE, false);
        ArrayList<Candidate> result = TestUtils.generateSearchResult(SEARCH_PARAM_SKILL_ONE, false);
        String json = TestUtils.json(searchParamsDTO);

        given(candidateService.searchCandidates(searchParamsDTO)).willReturn(result);
        this.mockMvc.perform(post("/candidates/search")
                .content(json)
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(EMPTY_LIST_SIZE)));
    }

    @Test
    @Transactional
    public void testSearchByMoreSkillsFound() throws Exception {
        SearchParamsDTO searchParamsDTO = TestUtils.generateSearchParamsDTO(SEARCH_PARAM_SKILL_MORE, true);
        ArrayList<Candidate> result = TestUtils.generateSearchResult(SEARCH_PARAM_SKILL_MORE, true);
        String json = TestUtils.json(searchParamsDTO);

        given(candidateService.searchCandidates(searchParamsDTO)).willReturn(result);
        this.mockMvc.perform(post("/candidates/search")
                .content(json)
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(SEARCH_COUNT_MORE_SKILLS)));
    }

    @Test
    @Transactional
    public void testSearchByMoreSkillsNotFound() throws Exception {
        SearchParamsDTO searchParamsDTO = TestUtils.generateSearchParamsDTO(SEARCH_PARAM_SKILL_MORE, false);
        ArrayList<Candidate> result = TestUtils.generateSearchResult(SEARCH_PARAM_SKILL_MORE, false);
        String json = TestUtils.json(searchParamsDTO);

        given(candidateService.searchCandidates(searchParamsDTO)).willReturn(result);
        this.mockMvc.perform(post("/candidates/search")
                .content(json)
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(EMPTY_LIST_SIZE)));
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
        candidateToUpdate.setId(NON_EXISTENT_ID);
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
        candidateToUpdate.setEmail(CANDIDATE_UPDATE_EXIST_EMAIL);
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
        candidateToUpdate.setContactNumber(CANDIDATE_UPDATE_EXIST_CONTACT_NUMBER);
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
        candidateToUpdate.getSkills().add(SKILL_TO_UPDATE);
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
        int candidateId = REMOVE_SKILL_FROM_CANDIDATE_CID;
        int skillId = REMOVE_SKILL_FROM_CANDIDATE_SID;
        given(candidateService.removeSkill(candidateId, skillId)).willReturn(true);
        this.mockMvc.perform(delete("/candidates/" + candidateId + "/" + skillId)
                .contentType(contentType))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testRemoveSkillFromCandidateFail() throws Exception {
        int candidateId = NON_EXISTENT_ID;
        int skillId = REMOVE_SKILL_FROM_CANDIDATE_SID;
        given(candidateService.removeSkill(candidateId, skillId)).willReturn(false);
        this.mockMvc.perform(delete("/candidates/" + candidateId + "/" + skillId)
                .contentType(contentType))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void testRemoveSkillFromSkillFail() throws Exception {
        int candidateId = REMOVE_SKILL_FROM_CANDIDATE_CID;
        int skillId = NON_EXISTENT_ID;
        given(candidateService.removeSkill(candidateId, skillId)).willReturn(false);
        this.mockMvc.perform(delete("/candidates/" + candidateId + "/" + skillId)
                .contentType(contentType))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    @Rollback()
    public void testDeleteCandidateSuccess() throws Exception {
        int candidateId = DELETE_CANDIDATE_ID;
        given(candidateService.delete(candidateId)).willReturn(true);
        this.mockMvc.perform(delete("/candidates/" + candidateId)
                .contentType(contentType))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testDeleteCandidateFail() throws Exception {
        int candidateId = NON_EXISTENT_ID;
        given(candidateService.delete(candidateId)).willReturn(true);
        this.mockMvc.perform(delete("/candidates/" + candidateId)
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
                .andExpect(jsonPath("$", hasSize(ALL_CANDIDATES_SIZE)));
    }

    @Test
    @Transactional
    public void testGetCandidateByIdSuccess() throws Exception {
        int candidateId = GET_CANDIDATE_BY_ID;
        Candidate candidate = new Candidate();
        candidate.setId(candidateId);
        given(candidateService.findOne(candidateId)).willReturn(candidate);
        this.mockMvc.perform(get("/candidates/by-id/" + candidateId)
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(candidate.getId())));
    }

    @Test
    @Transactional
    public void testGetCandidateByIdFail() throws Exception {
        int candidateId = NON_EXISTENT_ID;
        Candidate candidate = new Candidate();
        candidate.setId(candidateId);
        given(candidateService.findOne(candidateId)).willReturn(null);
        this.mockMvc.perform(get("/candidates/by-id/" + candidateId)
                .contentType(contentType))
                .andExpect(status().isBadRequest());
    }

}
