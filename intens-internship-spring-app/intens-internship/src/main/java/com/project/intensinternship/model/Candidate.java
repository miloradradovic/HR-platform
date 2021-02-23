package com.project.intensinternship.model;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "candidates")
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "fullName", unique = false, nullable = false)
    private String fullName;

    @Column(name = "dateOfBirth", unique = false, nullable = false)
    private Date dateOfBirth;

    @Column(name = "contactNumber", unique = true, nullable = false)
    private String contactNumber;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "candidate_skill",
            joinColumns = @JoinColumn(name = "candidateId"),
            inverseJoinColumns = @JoinColumn(name = "skillId"))
    private Set<Skill> skillSet;

    public Candidate(){
        this.skillSet = new HashSet<>();
    }

    public Candidate(int id, String fullName, Date dateOfBirth, String contactNumber, String email, Set<Skill> skillSet) {
        this.id = id;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.contactNumber = contactNumber;
        this.email = email;
        this.skillSet = skillSet;
    }

    public Candidate(String fullName, Date dateOfBirth, String contactNumber, String email, Set<Skill> skillSet) {
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.contactNumber = contactNumber;
        this.email = email;
        this.skillSet = skillSet;
    }

    public Candidate(int id, String fullName, Date dateOfBirth, String contactNumber, String email) {
        this.id = id;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.contactNumber = contactNumber;
        this.email = email;
        this.skillSet = new HashSet<>();
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

    public Set<Skill> getSkillSet() {
        return skillSet;
    }

    public void setSkillSet(Set<Skill> skillSet) {
        this.skillSet = skillSet;
    }
}
