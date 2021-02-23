package com.project.intensinternship.api;


import com.project.intensinternship.dto.CandidateDTO;
import com.project.intensinternship.dto.SearchParamsDTO;
import com.project.intensinternship.mappers.CandidateMapper;
import com.project.intensinternship.model.Candidate;
import com.project.intensinternship.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@CrossOrigin(origins = "https://localhost:4200")
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
    public ResponseEntity<Page<CandidateDTO>> searchCandidates(@RequestBody SearchParamsDTO searchParamsDTO, Pageable pageable){

        Page<Candidate> page = candidateService.searchCandidates(searchParamsDTO, pageable);
        List<CandidateDTO> dtos = toCandidateDTOList(page.toList());
        Page<CandidateDTO> pageCandidateDTOS = new PageImpl<>(dtos,page.getPageable(), page.getTotalElements());

        return new ResponseEntity<>(pageCandidateDTOS, HttpStatus.OK);
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

    @RequestMapping(value = "/{candidateId}/{skillId}", method= RequestMethod.DELETE)
    public ResponseEntity<?> removeSkillFromCandidate(@PathVariable Integer candidateId, @PathVariable Integer skillId){

        if(candidateService.removeSkill(candidateId, skillId)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/{id}", method= RequestMethod.DELETE)
    public ResponseEntity<String> deleteCandidate(@PathVariable Integer id){

        if(candidateService.delete(id)) {
            return new ResponseEntity<String>("Successfully deleted candidate!", HttpStatus.OK);
        }else {
            return new ResponseEntity<String>("Deleting failed!", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value= "/by-page",method = RequestMethod.GET)
    public ResponseEntity<Page<CandidateDTO>> getCandidates(Pageable pageable) {
        Page<Candidate> page = candidateService.findAll(pageable);
        List<CandidateDTO> dtos = toCandidateDTOList(page.toList());
        Page<CandidateDTO> pageCandidateDTOS = new PageImpl<>(dtos,page.getPageable(), page.getTotalElements());

        return new ResponseEntity<>(pageCandidateDTOS, HttpStatus.OK);
    }

    @RequestMapping(value= "/by-id/{id}",method = RequestMethod.GET)
    public ResponseEntity<CandidateDTO> getCandidateById(@PathVariable Integer id) {
        Candidate candidate = candidateService.findOne(id);
        if(candidate != null){
            return new ResponseEntity<CandidateDTO>(candidateMapper.toDto(candidate), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private List<CandidateDTO> toCandidateDTOList(List<Candidate> toList) {
        ArrayList<CandidateDTO> dtos = new ArrayList<CandidateDTO>();
        for(Candidate candidate : toList) {
            CandidateDTO dto = candidateMapper.toDto(candidate);
            dtos.add(dto);
        }
        return dtos;
    }
}
