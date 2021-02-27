package com.project.intensinternship;

import com.project.intensinternship.api.CandidateControllerIntegrationTests;
import com.project.intensinternship.api.CandidateControllerJUnitTests;
import com.project.intensinternship.api.SkillControllerIntegrationTests;
import com.project.intensinternship.api.SkillControllerJUnitTests;
import com.project.intensinternship.service.CandidateServiceIntegrationTests;
import com.project.intensinternship.service.CandidateServiceJUnitTests;
import com.project.intensinternship.service.SkillServiceIntegrationTests;
import com.project.intensinternship.service.SkillServiceJUnitTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.test.context.TestPropertySource;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CandidateControllerJUnitTests.class, SkillControllerJUnitTests.class,
        CandidateServiceJUnitTests.class, SkillServiceJUnitTests.class,
        CandidateControllerIntegrationTests.class, SkillControllerIntegrationTests.class,
        CandidateServiceIntegrationTests.class, SkillServiceIntegrationTests.class})
@TestPropertySource("classpath:test.properties")
public class SuiteAll {
}
