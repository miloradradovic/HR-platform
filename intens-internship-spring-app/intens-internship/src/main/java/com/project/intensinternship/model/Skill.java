package com.project.intensinternship.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "skills")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "skillSet")
    Set<Candidate> candidateSet;

    public Skill(int id, String name, Set<Candidate> candidateSet) {
        this.id = id;
        this.name = name;
        this.candidateSet = candidateSet;
    }

    public Skill(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Skill(String name){
        this.name = name;
    }

    public Skill(){
        this.candidateSet = new HashSet<>();
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

    public Set<Candidate> getCandidateSet() {
        return candidateSet;
    }

    public void setCandidateSet(Set<Candidate> candidateSet) {
        this.candidateSet = candidateSet;
    }
}
