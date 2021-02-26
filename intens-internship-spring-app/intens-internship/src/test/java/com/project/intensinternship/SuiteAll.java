package com.project.intensinternship;

import com.project.intensinternship.api.CandidateControllerJUnitTests;
import com.project.intensinternship.api.SkillControllerJUnitTests;
import com.project.intensinternship.service.CandidateServiceJUnitTests;
import com.project.intensinternship.service.SkillServiceJUnitTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CandidateControllerJUnitTests.class, SkillControllerJUnitTests.class, CandidateServiceJUnitTests.class, SkillServiceJUnitTests.class})
public class SuiteAll {
}
