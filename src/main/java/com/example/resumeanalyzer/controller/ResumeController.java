package com.example.resumeanalyzer.controller;


import com.example.resumeanalyzer.dto.ResumeResponse;
import com.example.resumeanalyzer.model.ResumeAnalysis;
import com.example.resumeanalyzer.model.User;
import com.example.resumeanalyzer.repository.ResumeAnalysisRepository;
import com.example.resumeanalyzer.repository.UserRepository;
import com.example.resumeanalyzer.service.GroqService;
import com.example.resumeanalyzer.service.PdfService;
import com.example.resumeanalyzer.service.ResumeService;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class ResumeController {



    @Autowired
    private ResumeService resumeService;


    @Autowired
    private GroqService geminiService;


    @Autowired
    private ResumeAnalysisRepository repository;


    @Autowired
    private UserRepository userRepository;


    @Autowired
    private PdfService pdfService;







    @PostMapping("/upload")
    public ResumeResponse uploadResume(
            @RequestParam("file") MultipartFile file,
            @RequestParam("email") String email
    ) throws Exception {



        if(file.isEmpty()){

            throw new Exception(
                    "File is empty"
            );

        }






        String text =
                resumeService.extractText(file);






        if(text == null || text.trim().length() < 150){


            throw new Exception(
                    "Please upload a valid resume"
            );


        }







        String lowerText =
                text.toLowerCase();






        boolean hasEmail =
                lowerText.matches(
                "(?s).*\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\\b.*"
                );





        boolean hasPhone =
                lowerText.matches(
                "(?s).*\\b[0-9]{10}\\b.*"
                );





        boolean hasEducation =

                lowerText.contains("education")
                ||
                lowerText.contains("b.tech")
                ||
                lowerText.contains("btech")
                ||
                lowerText.contains("bachelor")
                ||
                lowerText.contains("degree")
                ||
                lowerText.contains("university")
                ||
                lowerText.contains("college");






        boolean hasSkills =

                lowerText.contains("skills")
                ||
                lowerText.contains("technical skills")
                ||
                lowerText.contains("programming")
                ||
                lowerText.contains("java")
                ||
                lowerText.contains("python")
                ||
                lowerText.contains("javascript");







        boolean hasExperience =

                lowerText.contains("experience")
                ||
                lowerText.contains("internship")
                ||
                lowerText.contains("work experience")
                ||
                lowerText.contains("employment");







        boolean hasProject =

                lowerText.contains("project")
                ||
                lowerText.contains("projects");









        int points = 0;





        if(hasEmail)
            points++;




        if(hasPhone)
            points++;




        if(hasEducation)
            points++;




        if(hasSkills)
            points++;




        if(hasExperience || hasProject)
            points++;








        if(points < 4){


            throw new Exception(
                    "Uploaded file is not a valid resume"
            );


        }








        String analysis =

                geminiService.analyzeResume(text);









        User user =

                userRepository
                .findByEmail(email)
                .orElse(null);









        ResumeAnalysis resume =

                new ResumeAnalysis();









        resume.setFileName(

                file.getOriginalFilename()

        );






        resume.setResumeText(text);






        resume.setAnalysis(analysis);






        resume.setAtsScore(

                extractScore(analysis)

        );






        resume.setUser(user);








        repository.save(resume);








        return new ResumeResponse(analysis);



    }









    @GetMapping("/history")
    public List<ResumeAnalysis> getHistory(

            @RequestParam("email") String email

    ){



        User user =

                userRepository
                .findByEmail(email)
                .orElse(null);




        return repository.findByUser(user);



    }









    @DeleteMapping("/delete/{id}")
    public String deleteResume(

            @PathVariable Long id

    ){



        repository.deleteById(id);



        return "Resume deleted successfully";


    }









    @GetMapping("/download-report")
    public ResponseEntity<byte[]> downloadReport(

            @RequestParam("analysis") String analysis

    ) throws Exception {



        byte[] pdf =

                pdfService.generateReport(analysis);





        return ResponseEntity.ok()

                .header(

                        HttpHeaders.CONTENT_DISPOSITION,

                        "attachment; filename=AI_Resume_Report.pdf"

                )

                .contentType(

                        MediaType.APPLICATION_PDF

                )

                .body(pdf);



    }









    private String extractScore(String analysis){



        try{



            Pattern pattern =

                    Pattern.compile(

                    "ATS\\s*(?:Compatibility)?\\s*Score[:\\s]*(\\d+)",

                    Pattern.CASE_INSENSITIVE

                    );




            Matcher matcher =

                    pattern.matcher(analysis);




            if(matcher.find()){


                return matcher.group(1);


            }




        }
        catch(Exception e){


            e.printStackTrace();


        }






        return "0";


    }



}