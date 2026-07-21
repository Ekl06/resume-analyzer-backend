package com.example.resumeanalyzer.model;

import jakarta.persistence.*;

@Entity
public class ResumeAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String fileName;


    private String atsScore;



    @Column(columnDefinition = "TEXT")
    private String analysis;



    @Column(columnDefinition = "TEXT")
    private String resumeText;



    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;




    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }



    public String getFileName() {
        return fileName;
    }


    public void setFileName(String fileName) {
        this.fileName = fileName;
    }



    public String getAtsScore() {
        return atsScore;
    }


    public void setAtsScore(String atsScore) {
        this.atsScore = atsScore;
    }



    public String getAnalysis() {
        return analysis;
    }


    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }



    public String getResumeText() {
        return resumeText;
    }


    public void setResumeText(String resumeText) {
        this.resumeText = resumeText;
    }



    public User getUser() {
        return user;
    }


    public void setUser(User user) {
        this.user = user;
    }

}