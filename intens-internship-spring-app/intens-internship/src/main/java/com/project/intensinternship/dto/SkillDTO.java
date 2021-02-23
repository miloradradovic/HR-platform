package com.project.intensinternship.dto;

import java.util.ArrayList;

public class SkillDTO {

    private int id;


    private String name;


    private ArrayList<String> candidateEmails;

    public SkillDTO(int id, String name, ArrayList<String> candidateEmails) {
        this.id = id;
        this.name = name;
        this.candidateEmails = candidateEmails;
    }

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

    public ArrayList<String> getCandidateEmails() {
        return candidateEmails;
    }

    public void setCandidateEmails(ArrayList<String> candidateEmails) {
        this.candidateEmails = candidateEmails;
    }
}
