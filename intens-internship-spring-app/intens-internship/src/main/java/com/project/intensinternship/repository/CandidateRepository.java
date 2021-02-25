package com.project.intensinternship.repository;

import com.project.intensinternship.model.Candidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Integer> {

    Candidate findById(int id);

    Candidate findByEmail(String email);

    @Query(value = "SELECT * from candidates where upper(full_name) LIKE upper(CONCAT('%', :value, '%'))", nativeQuery = true)
    List<Candidate> searchByName(String value);

    @Query(value = "SELECT distinct * from candidates where candidates.id in (SELECT candidate_id from candidate_skill where skill_id in (SELECT id from skills where upper(name) LIKE upper(CONCAT('%', :value, '%'))))", nativeQuery = true)
    List<Candidate> searchByOneSkill(String value);

    @Query(value = "SELECT distinct * from candidates where candidates.id in (SELECT candidate_id from candidate_skill where skill_id in (SELECT id from skills where name in :splitted))", nativeQuery = true)
    List<Candidate> searchByMoreSkills(Collection<String> splitted);

    Candidate findByContactNumber(String contactNumber);
}
