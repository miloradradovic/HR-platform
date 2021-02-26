package com.project.intensinternship.api;


import com.project.intensinternship.dto.CandidateDTO;
import com.project.intensinternship.dto.SearchParamsDTO;
import com.project.intensinternship.mappers.CandidateMapper;
import com.project.intensinternship.model.Candidate;
import com.project.intensinternship.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(value="/candidates", produces = MediaType.APPLICATION_JSON_VALUE)
public class CandidateController {

    @Autowired
    CandidateService candidateService;

    private final CandidateMapper candidateMapper;

    public CandidateController() {
        super();
        this.candidateMapper = new CandidateMapper();
    }

    @RequestMapping(method= RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CandidateDTO> createCandidate(@RequestBody CandidateDTO candidateDTO){

        Candidate candidate = candidateService.saveOne(candidateMapper.toEntity(candidateDTO));
        if(candidate != null) {
            candidateDTO.setId(candidate.getId());
            return new ResponseEntity<>(candidateDTO, HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/search", method= RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CandidateDTO>> searchCandidates(@RequestBody SearchParamsDTO searchParamsDTO){

        List<Candidate> candidates = candidateService.searchCandidates(searchParamsDTO);

        return new ResponseEntity<>(this.candidateMapper.toListDtos(candidates), HttpStatus.OK);
    }

    @RequestMapping(method= RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CandidateDTO> updateCandidate(@RequestBody CandidateDTO candidateDTO){

        Candidate candidate = candidateService.update(candidateMapper.toEntity(candidateDTO));
        if(candidate != null) {
            candidateDTO.setId(candidate.getId());
            return new ResponseEntity<>(candidateDTO, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/skills", method= RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CandidateDTO> updateCandidateWithSkill(@RequestBody CandidateDTO candidateDTO){

        Candidate candidate = candidateService.updateSkills(candidateMapper.toEntity(candidateDTO));
        if(candidate != null) {
            candidateDTO.setId(candidate.getId());
            return new ResponseEntity<>(candidateDTO, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/{candidateId}/{skillId}", method= RequestMethod.DELETE)
    public ResponseEntity<?> removeSkillFromCandidate(@PathVariable Integer candidateId, @PathVariable Integer skillId){

        if(candidateService.removeSkill(candidateId, skillId)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/{id}", method= RequestMethod.DELETE)
    public ResponseEntity<?> deleteCandidate(@PathVariable Integer id){

        if(candidateService.delete(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<CandidateDTO>> getCandidates() {
        List<Candidate> list = candidateService.findAll();

        return new ResponseEntity<>(this.candidateMapper.toListDtos(list), HttpStatus.OK);
    }

    @RequestMapping(value= "/by-id/{id}",method = RequestMethod.GET)
    public ResponseEntity<CandidateDTO> getCandidateById(@PathVariable Integer id) {
        Candidate candidate = candidateService.findOne(id);
        if(candidate != null){
            return new ResponseEntity<>(candidateMapper.toDto(candidate), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
