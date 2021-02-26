package com.project.intensinternship.dto;

import javax.validation.constraints.NotBlank;

public class SkillDTO {

    private int id;

    @NotBlank
    private String name;

    public SkillDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public SkillDTO(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
