package com.project.intensinternship.api;

import com.project.intensinternship.dto.SkillDTO;
import com.project.intensinternship.mappers.SkillMapper;
import com.project.intensinternship.model.Skill;
import com.project.intensinternship.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
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
    public ResponseEntity<SkillDTO> createSkill(@Valid @RequestBody SkillDTO skillDTO){

        Skill skill = skillService.saveOne(skillMapper.toEntity(skillDTO));
        if(skill != null) {
            skillDTO.setId(skill.getId());
            return new ResponseEntity<>(skillDTO, HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value= "/by-candidate/{id}",method = RequestMethod.GET)
    public ResponseEntity<List<SkillDTO>> getSkillsByCandidate(@PathVariable Integer id) {

        List<Skill> list = skillService.findByCandidate(id);

        return new ResponseEntity<>(this.skillMapper.toListDtos(list), HttpStatus.OK);
    }

    @RequestMapping(value= "/{id}",method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteSkill(@PathVariable Integer id) {

        if(skillService.delete(id)){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ArrayList<SkillDTO>> getAllSkills() {

        List<Skill> skills = skillService.findAll();
        ArrayList<SkillDTO> dtos = (ArrayList<SkillDTO>) this.skillMapper.toListDtos(skills);

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }
}
