package com.project.intensinternship.dto;

import java.util.ArrayList;
import java.util.Date;

public class CandidateDTO {

    private int id;


    private String fullName;


    private Date dateOfBirth;


    private String contactNumber;


    private String email;


    private ArrayList<String> skills;


    public CandidateDTO(int id, String fullName, Date dateOfBirth, String contactNumber, String email, ArrayList<String> skills) {
        this.id = id;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.contactNumber = contactNumber;
        this.email = email;
        this.skills = skills;
    }

    public CandidateDTO(int id, String fullName, Date dateOfBirth, String contactNumber, String email) {
        this.id = id;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.contactNumber = contactNumber;
        this.email = email;
        this.skills = new ArrayList<>();
    }

    public CandidateDTO(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<String> getSkills() {
        return skills;
    }

    public void setSkills(ArrayList<String> skills) {
        this.skills = skills;
    }
}
