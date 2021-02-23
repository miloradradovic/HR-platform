package com.project.intensinternship.repository;

import com.project.intensinternship.model.Candidate;
import com.project.intensinternship.model.Skill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Integer> {
    Skill findByName(String name);

    @Query(value = "SELECT * from skills where id in (SELECT skill_id from candidate_skill where candidate_id = :id)", nativeQuery = true)
    Page<Skill> findByCandidate(Integer id, Pageable pageable);
}
