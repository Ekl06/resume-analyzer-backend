package com.example.resumeanalyzer.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;



@Service
public class GroqService {


    @Value("${groq.api.key}")
    private String apiKey;



    private final RestTemplate restTemplate = new RestTemplate();


    private final ObjectMapper mapper = new ObjectMapper();






    public String analyzeResume(String resumeText) throws Exception {



        String prompt = """

        You are a strict ATS Resume Analyzer.


        First check whether the uploaded document is a real resume.


        A valid resume usually contains:
        - Candidate name
        - Education
        - Skills
        - Experience or Projects
        - Contact information


        If the document is NOT a resume, return ONLY:

        INVALID_RESUME


        Do not generate ATS score for invalid documents.



        If it is a valid resume, return ONLY this format:



        ATS Score: XX/100


        Strengths:
        - point


        Weaknesses:
        - point


        Missing Skills:
        - point


        Suggestions:
        - point




        Resume:

        """ + resumeText;







        String url =
                "https://api.groq.com/openai/v1/chat/completions";






        HttpHeaders headers = new HttpHeaders();


        headers.setBearerAuth(apiKey.trim());


        headers.setContentType(MediaType.APPLICATION_JSON);






        Map<String,Object> body = Map.of(


                "model",

                "llama-3.1-8b-instant",




                "messages",

                List.of(

                        Map.of(

                                "role",

                                "user",

                                "content",

                                prompt

                        )

                ),




                "temperature",

                0.1

        );






        HttpEntity<Map<String,Object>> request =

                new HttpEntity<>(body, headers);






        ResponseEntity<String> response =

                restTemplate.exchange(

                        url,

                        HttpMethod.POST,

                        request,

                        String.class

                );







        JsonNode root =

                mapper.readTree(response.getBody());






        String result =

                root

                .get("choices")

                .get(0)

                .get("message")

                .get("content")

                .asText();







        if(result.contains("INVALID_RESUME")){


            throw new Exception(
                    "Uploaded file is not a valid resume"
            );


        }







        return result;


    }



}