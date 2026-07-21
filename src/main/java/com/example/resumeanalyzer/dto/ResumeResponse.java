package com.example.resumeanalyzer.dto;

public class ResumeResponse {

    private String text;

    public ResumeResponse() {
    }

    public ResumeResponse(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}