package com.project.intensinternship.api;

import com.project.intensinternship.dto.CandidateDTO;
import com.project.intensinternship.dto.SkillDTO;
import com.project.intensinternship.mappers.SkillMapper;
import com.project.intensinternship.model.Candidate;
import com.project.intensinternship.model.Skill;
import com.project.intensinternship.service.SkillService;
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
@RequestMapping(value="/skills", produces = MediaType.APPLICATION_JSON_VALUE)
public class SkillController {

    @Autowired
    SkillService skillService;

    private final SkillMapper skillMapper;

    public SkillController() {
        this.skillMapper = new SkillMapper();
    }

    @RequestMapping(method= RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SkillDTO> createSkill(@RequestBody SkillDTO skillDTO){

        Skill skill = skillService.saveOne(skillMapper.toEntity(skillDTO));
        if(skill != null) {
            skillDTO.setId(skill.getId());
            return new ResponseEntity<>(skillDTO, HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value= "/by-candidate/{id}/by-page",method = RequestMethod.GET)
    public ResponseEntity<Page<SkillDTO>> getSkillsByCandidate(@PathVariable Integer id, Pageable pageable) {

        Page<Skill> page = skillService.findByCandidate(id, pageable);
        List<SkillDTO> dtos = toSkillDTOList(page.toList());
        Page<SkillDTO> pageSkillDTOS = new PageImpl<>(dtos,page.getPageable(), page.getTotalElements());

        return new ResponseEntity<>(pageSkillDTOS, HttpStatus.OK);
    }

    private List<SkillDTO> toSkillDTOList(List<Skill> toList) {
        ArrayList<SkillDTO> dtos = new ArrayList<SkillDTO>();
        for(Skill skill : toList) {
            SkillDTO dto = skillMapper.toDto(skill);
            dtos.add(dto);
        }
        return dtos;
    }
}