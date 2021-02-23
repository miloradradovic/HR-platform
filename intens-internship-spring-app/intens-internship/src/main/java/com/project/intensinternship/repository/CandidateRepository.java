package com.project.intensinternship.repository;

import com.project.intensinternship.model.Candidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Integer> {

    Candidate findByEmail(String email);

    @Query(value = "SELECT * from candidates where upper(full_name) LIKE upper(CONCAT('%', :value, '%'))", nativeQuery = true)
    Page<Candidate> searchByName(String value, Pageable pageable);

    @Query(value = "SELECT distinct * from candidates where candidates.id in (SELECT candidate_id from candidate_skill where skill_id in (SELECT id from skills where upper(name) LIKE upper(CONCAT('%', :value, '%'))))", nativeQuery = true)
    Page<Candidate> searchByOneSkill(String value, Pageable pageable);

    @Query(value = "SELECT distinct * from candidates where candidates.id in (SELECT candidate_id from candidate_skill where skill_id in (SELECT id from skills where name in :splitted))", nativeQuery = true)
    Page<Candidate> searchByMoreSkills(Collection<String> splitted, Pageable pageable);
}
