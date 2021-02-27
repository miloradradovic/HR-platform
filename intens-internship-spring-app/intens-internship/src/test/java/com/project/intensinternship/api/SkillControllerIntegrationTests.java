package com.project.intensinternship.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.intensinternship.dto.SkillDTO;
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

import java.util.List;

import static com.project.intensinternship.constants.IntegrationConstants.*;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class SkillControllerIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @Sql(scripts = "classpath:delete-skill-h2.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testCreateCandidateValid() {
        SkillDTO saveSkill = new SkillDTO(CREATE_NEW_SKILL_NAME_VALID);

        ResponseEntity<SkillDTO> responseEntity =
                restTemplate.exchange("/skills", HttpMethod.POST, new HttpEntity<>(saveSkill, null),
                        SkillDTO.class);

        SkillDTO skillDTO = responseEntity.getBody();
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(CREATE_NEW_SKILL_NAME_VALID, skillDTO.getName());
    }

    @Test
    public void testCreateCandidateInvalid() {
        SkillDTO saveSkill = new SkillDTO(CREATE_NEW_SKILL_NAME_INVALID);

        ResponseEntity<SkillDTO> responseEntity =
                restTemplate.exchange("/skills", HttpMethod.POST, new HttpEntity<>(saveSkill, null),
                        SkillDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testGetSkillsByCandidate() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseEntity<String> responseEntity =
                restTemplate.exchange("/skills/by-candidate/" + GET_SKILLS_BY_CANDIDATE_ID_VALID, HttpMethod.GET, new HttpEntity<>(null),
                        String.class);

        List<SkillDTO> found = objectMapper.readValue(responseEntity.getBody(), new TypeReference<List<SkillDTO>>() {});
        assertEquals(GET_SKILLS_BY_CANDIDATE_SIZE, found.size());
    }

    @Test
    public void testGetSkillsByCandidateInvalid() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseEntity<String> responseEntity =
                restTemplate.exchange("/skills/by-candidate/" + GET_SKILLS_BY_CANDIDATE_ID_INVALID, HttpMethod.GET, new HttpEntity<>(null),
                        String.class);

        List<SkillDTO> found = objectMapper.readValue(responseEntity.getBody(), new TypeReference<List<SkillDTO>>() {});
        assertEquals(EMPTY_LIST_SIZE, found.size());
    }

    @Test
    @Sql(scripts = "classpath:add-skill-h2.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testDeleteSkill() {
        ResponseEntity<String> responseEntity =
                restTemplate.exchange("/skills/" + DELETE_SKILL_ID_VALID, HttpMethod.DELETE,
                        new HttpEntity<>(null), String.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testDeleteSkillInvalid() {
        ResponseEntity<String> responseEntity =
                restTemplate.exchange("/skills/" + DELETE_SKILL_ID_INVALID, HttpMethod.DELETE,
                        new HttpEntity<>(null), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testGetAllSkills() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseEntity<String> responseEntity =
                restTemplate.exchange("/skills", HttpMethod.GET, new HttpEntity<>(null),
                        String.class);

        List<SkillDTO> found = objectMapper.readValue(responseEntity.getBody(), new TypeReference<List<SkillDTO>>() {});
        assertEquals(GET_ALL_SKILLS_SIZE, found.size());
    }
}
