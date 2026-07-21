package com.example.resumeanalyzer.controller;

import com.example.resumeanalyzer.model.JobAnalysis;
import com.example.resumeanalyzer.model.User;
import com.example.resumeanalyzer.repository.JobAnalysisRepository;
import com.example.resumeanalyzer.repository.UserRepository;
import com.example.resumeanalyzer.service.GroqService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RestController
@RequestMapping("/api/job")
@CrossOrigin("*")
public class JobController {


    @Autowired
    private GroqService geminiService;


    @Autowired
    private UserRepository userRepository;


    @Autowired
    private JobAnalysisRepository jobRepository;



    @PostMapping("/analyze")
    public String analyzeJob(
            @RequestParam("resumeText") String resumeText,
            @RequestParam("jobDescription") String jobDescription,
            @RequestParam("email") String email
    ) throws Exception {



        String prompt = """

Analyze this resume against the given job description.

Return:

Match Percentage: XX%

Matching Skills:
- 

Missing Skills:
-

Suggestions:
-


Resume:

""" + resumeText + """

Job Description:

""" + jobDescription;



        String result = geminiService.analyzeResume(prompt);




        // Extract match percentage

        String matchScore = "0";


        Pattern pattern = Pattern.compile("(\\d+)\\s*%");

        Matcher matcher = pattern.matcher(result);


        if(matcher.find()){

            matchScore = matcher.group(1);

        }




        User user = userRepository
                .findByEmail(email)
                .orElse(null);




        JobAnalysis analysis = new JobAnalysis();


        analysis.setJobTitle("Job Matching Analysis");


        analysis.setMatchScore(matchScore);


        analysis.setAnalysis(result);


        analysis.setUser(user);



        jobRepository.save(analysis);



        return result;

    }





    @GetMapping("/history")
    public List<JobAnalysis> history(
            @RequestParam("email") String email
    ){


        User user = userRepository
                .findByEmail(email)
                .orElse(null);



        return jobRepository.findByUser(user);

    }


}