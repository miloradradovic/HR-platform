package com.project.intensinternship.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.intensinternship.dto.CandidateDTO;
import com.project.intensinternship.dto.SearchParamsDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.project.intensinternship.constants.IntegrationConstants.*;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class CandidateControllerIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @Sql(scripts = "classpath:delete-candidate-h2.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testCreateCandidateValid() {
        CandidateDTO saveCandidate = new CandidateDTO(CREATE_NEW_CANDIDATE_NAME, new Date(), CREATE_NEW_CANDIDATE_CONTACT_NUMBER_VALID, CREATE_NEW_CANDIDATE_EMAIL_VALID, new ArrayList<>());

        ResponseEntity<CandidateDTO> responseEntity =
                restTemplate.exchange("/candidates", HttpMethod.POST, new HttpEntity<>(saveCandidate, null),
                        CandidateDTO.class);

        CandidateDTO candidateDTO = responseEntity.getBody();
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(CREATE_NEW_CANDIDATE_NAME, candidateDTO.getFullName());
    }

    @Test
    public void testCreateCandidateInvalidEmail() {
        CandidateDTO saveCandidate = new CandidateDTO(CREATE_NEW_CANDIDATE_NAME, new Date(), CREATE_NEW_CANDIDATE_CONTACT_NUMBER_VALID, CREATE_NEW_CANDIDATE_EMAIL_INVALID, new ArrayList<>());

        ResponseEntity<CandidateDTO> responseEntity =
                restTemplate.exchange("/candidates", HttpMethod.POST, new HttpEntity<>(saveCandidate, null),
                        CandidateDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testCreateCandidateInvalidContactNumber() {
        CandidateDTO saveCandidate = new CandidateDTO(CREATE_NEW_CANDIDATE_NAME, new Date(), CREATE_NEW_CANDIDATE_CONTACT_NUMBER_INVALID, CREATE_NEW_CANDIDATE_EMAIL_VALID, new ArrayList<>());

        ResponseEntity<CandidateDTO> responseEntity =
                restTemplate.exchange("/candidates", HttpMethod.POST, new HttpEntity<>(saveCandidate, null),
                        CandidateDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testSearchCandidatesByNameFound() throws JsonProcessingException {
        SearchParamsDTO searchParamsDTO = new SearchParamsDTO(SEARCH_CANDIDATES_PARAM_NAME, SEARCH_CANDIDATES_VALUE_NAME_FOUND);
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseEntity<String> responseEntity =
                restTemplate.exchange("/candidates/search", HttpMethod.POST, new HttpEntity<>(searchParamsDTO, null),
                        String.class);

        List<CandidateDTO> found = objectMapper.readValue(responseEntity.getBody(), new TypeReference<List<CandidateDTO>>() {});
        assertEquals(SEARCH_CANDIDATES_PARAM_NAME_RESULT_COUNT, found.size());
    }

    @Test
    public void testSearchCandidatesByNameNotFound() throws JsonProcessingException {
        SearchParamsDTO searchParamsDTO = new SearchParamsDTO(SEARCH_CANDIDATES_PARAM_NAME, SEARCH_CANDIDATES_VALUE_NAME_NOT_FOUND);
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseEntity<String> responseEntity =
                restTemplate.exchange("/candidates/search", HttpMethod.POST, new HttpEntity<>(searchParamsDTO, null),
                        String.class);

        List<CandidateDTO> found = objectMapper.readValue(responseEntity.getBody(), new TypeReference<List<CandidateDTO>>() {});
        assertEquals(EMPTY_LIST_SIZE, found.size());
    }

    @Test
    public void testSearchCandidatesByOneSkillFound() throws JsonProcessingException {
        SearchParamsDTO searchParamsDTO = new SearchParamsDTO(SEARCH_CANDIDATES_PARAM_SKILL, SEARCH_CANDIDATES_VALUE_ONE_SKILL_FOUND);
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseEntity<String> responseEntity =
                restTemplate.exchange("/candidates/search", HttpMethod.POST, new HttpEntity<>(searchParamsDTO, null),
                        String.class);

        List<CandidateDTO> found = objectMapper.readValue(responseEntity.getBody(), new TypeReference<List<CandidateDTO>>() {});
        assertEquals(SEARCH_CANDIDATES_PARAM_SKILL_ONE_RESULT_COUNT, found.size());
    }

    @Test
    public void testSearchCandidatesByOneSkillNotFound() throws JsonProcessingException {
        SearchParamsDTO searchParamsDTO = new SearchParamsDTO(SEARCH_CANDIDATES_PARAM_SKILL, SEARCH_CANDIDATES_VALUE_ONE_SKILL_NOT_FOUND);
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseEntity<String> responseEntity =
                restTemplate.exchange("/candidates/search", HttpMethod.POST, new HttpEntity<>(searchParamsDTO, null),
                        String.class);

        List<CandidateDTO> found = objectMapper.readValue(responseEntity.getBody(), new TypeReference<List<CandidateDTO>>() {});
        assertEquals(EMPTY_LIST_SIZE, found.size());
    }

    @Test
    public void testSearchCandidatesByMoreSkillsFound() throws JsonProcessingException {
        SearchParamsDTO searchParamsDTO = new SearchParamsDTO(SEARCH_CANDIDATES_PARAM_SKILL, SEARCH_CANDIDATES_VALUE_MORE_SKILLS_FOUND);
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseEntity<String> responseEntity =
                restTemplate.exchange("/candidates/search", HttpMethod.POST, new HttpEntity<>(searchParamsDTO, null),
                        String.class);

        List<CandidateDTO> found = objectMapper.readValue(responseEntity.getBody(), new TypeReference<List<CandidateDTO>>() {});
        assertEquals(SEARCH_CANDIDATES_PARAM_SKILLS_MORE_RESULT_COUNT, found.size());
    }

    @Test
    public void testSearchCandidatesByMoreSkillsNotFound() throws JsonProcessingException {
        SearchParamsDTO searchParamsDTO = new SearchParamsDTO(SEARCH_CANDIDATES_PARAM_SKILL, SEARCH_CANDIDATES_VALUE_MORE_SKILLS_NOT_FOUND);
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseEntity<String> responseEntity =
                restTemplate.exchange("/candidates/search", HttpMethod.POST, new HttpEntity<>(searchParamsDTO, null),
                        String.class);

        List<CandidateDTO> found = objectMapper.readValue(responseEntity.getBody(), new TypeReference<List<CandidateDTO>>() {});
        assertEquals(EMPTY_LIST_SIZE, found.size());
    }

    @Test
    @Sql(scripts = "classpath:update-candidate-h2.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testUpdateCandidateValid() {
        CandidateDTO updateCandidate = new CandidateDTO(UPDATE_CANDIDATE_ID_VALID, UPDATE_CANDIDATE_NAME, new Date(), UPDATE_CANDIDATE_CONTACT_NUMBER_VALID, UPDATE_CANDIDATE_EMAIL_VALID, new ArrayList<>());

        ResponseEntity<CandidateDTO> responseEntity =
                restTemplate.exchange("/candidates", HttpMethod.PUT, new HttpEntity<>(updateCandidate, null),
                        CandidateDTO.class);

        CandidateDTO candidateDTO = responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(UPDATE_CANDIDATE_NAME, candidateDTO.getFullName());
    }

    @Test
    public void testUpdateCandidateInvalidId() {
        CandidateDTO updateCandidate = new CandidateDTO(UPDATE_CANDIDATE_ID_INVALID, UPDATE_CANDIDATE_NAME, new Date(), UPDATE_CANDIDATE_CONTACT_NUMBER_VALID, UPDATE_CANDIDATE_EMAIL_VALID, new ArrayList<>());

        ResponseEntity<CandidateDTO> responseEntity =
                restTemplate.exchange("/candidates", HttpMethod.PUT, new HttpEntity<>(updateCandidate, null),
                        CandidateDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testUpdateCandidateInvalidEmail() {
        CandidateDTO updateCandidate = new CandidateDTO(UPDATE_CANDIDATE_ID_VALID, UPDATE_CANDIDATE_NAME, new Date(), UPDATE_CANDIDATE_CONTACT_NUMBER_VALID, UPDATE_CANDIDATE_EMAIL_INVALID, new ArrayList<>());

        ResponseEntity<CandidateDTO> responseEntity =
                restTemplate.exchange("/candidates", HttpMethod.PUT, new HttpEntity<>(updateCandidate, null),
                        CandidateDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testUpdateCandidateInvalidContactNumber() {
        CandidateDTO updateCandidate = new CandidateDTO(UPDATE_CANDIDATE_ID_VALID, UPDATE_CANDIDATE_NAME, new Date(), UPDATE_CANDIDATE_CONTACT_NUMBER_INVALID, UPDATE_CANDIDATE_EMAIL_VALID, new ArrayList<>());

        ResponseEntity<CandidateDTO> responseEntity =
                restTemplate.exchange("/candidates", HttpMethod.PUT, new HttpEntity<>(updateCandidate, null),
                        CandidateDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @Sql(scripts = "classpath:update-candidate-with-skills-h2.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testUpdateCandidateWithSkills() {
        CandidateDTO updateCandidate = new CandidateDTO(UPDATE_CANDIDATE_ID_VALID, UPDATE_CANDIDATE_NAME, new Date(), UPDATE_CANDIDATE_CONTACT_NUMBER_VALID, UPDATE_CANDIDATE_EMAIL_VALID, new ArrayList<>());
        updateCandidate.getSkills().add(UPDATE_CANDIDATE_WITH_SKILL_NAME);
        ResponseEntity<CandidateDTO> responseEntity =
                restTemplate.exchange("/candidates/skills", HttpMethod.PUT, new HttpEntity<>(updateCandidate, null),
                        CandidateDTO.class);

        CandidateDTO candidateDTO = responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(UPDATE_CANDIDATE_WITH_SKILL_SET_SIZE, candidateDTO.getSkills().size());
    }

    @Test
    public void testUpdateCandidateWithSkillsInvalidId() {
        CandidateDTO updateCandidate = new CandidateDTO(UPDATE_CANDIDATE_ID_INVALID, UPDATE_CANDIDATE_NAME, new Date(), UPDATE_CANDIDATE_CONTACT_NUMBER_VALID, UPDATE_CANDIDATE_EMAIL_VALID, new ArrayList<>());
        updateCandidate.getSkills().add(UPDATE_CANDIDATE_WITH_SKILL_NAME);
        ResponseEntity<CandidateDTO> responseEntity =
                restTemplate.exchange("/candidates/skills", HttpMethod.PUT, new HttpEntity<>(updateCandidate, null),
                        CandidateDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @Sql(scripts = "classpath:add-skill-to-candidate-h2.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testRemoveSkillFromCandidate() {
        ResponseEntity<String> responseEntity =
                restTemplate.exchange("/candidates/" + REMOVE_SKILL_FROM_CANDIDATE_VALID_CID + "/" + REMOVE_SKILL_FROM_CANDIDATE_VALID_SID, HttpMethod.DELETE,
                        new HttpEntity<>(null), String.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testRemoveSkillFromCandidateInvalidCID() {
        ResponseEntity<String> responseEntity =
                restTemplate.exchange("/candidates/" + REMOVE_SKILL_FROM_CANDIDATE_INVALID_CID + "/" + REMOVE_SKILL_FROM_CANDIDATE_VALID_SID, HttpMethod.DELETE,
                        new HttpEntity<>(null), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testRemoveSkillFromCandidateInvalidSID() {
        ResponseEntity<String> responseEntity =
                restTemplate.exchange("/candidates/" + REMOVE_SKILL_FROM_CANDIDATE_VALID_CID + "/" + REMOVE_SKILL_FROM_CANDIDATE_INVALID_SID, HttpMethod.DELETE,
                        new HttpEntity<>(null), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @Sql(scripts = "classpath:add-candidate-h2.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testDeleteCandidate() {
        ResponseEntity<String> responseEntity =
                restTemplate.exchange("/candidates/" + DELETE_CANDIDATE_ID_VALID, HttpMethod.DELETE,
                        new HttpEntity<>(null), String.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testDeleteCandidateInvalidId() {
        ResponseEntity<String> responseEntity =
                restTemplate.exchange("/candidates/" + DELETE_CANDIDATE_ID_INVALID, HttpMethod.DELETE,
                        new HttpEntity<>(null), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testGetAllCandidates() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseEntity<String> responseEntity =
                restTemplate.exchange("/candidates", HttpMethod.GET, new HttpEntity<>(null),
                        String.class);

        List<CandidateDTO> found = objectMapper.readValue(responseEntity.getBody(), new TypeReference<List<CandidateDTO>>() {});
        assertEquals(GET_ALL_CANDIDATES_SIZE, found.size());
    }

    @Test
    public void testGetCandidateById() {
        ResponseEntity<CandidateDTO> responseEntity =
                restTemplate.exchange("/candidates/by-id/" + GET_CANDIDATE_BY_ID_VALID, HttpMethod.GET,
                        new HttpEntity<>(null), CandidateDTO.class);

        CandidateDTO candidateDTO = responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(GET_CANDIDATE_BY_ID_VALID, candidateDTO.getId());
    }

    @Test
    public void testGetCandidateByIdInvalid() {
        ResponseEntity<CandidateDTO> responseEntity =
                restTemplate.exchange("/candidates/by-id/" + GET_CANDIDATE_BY_ID_INVALID, HttpMethod.GET,
                        new HttpEntity<>(null), CandidateDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

}
