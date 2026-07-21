package com.example.resumeanalyzer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailService {


    @Autowired
    private JavaMailSender mailSender;





    public void sendVerificationEmail(
            String toEmail,
            String token
    ){


        SimpleMailMessage message = new SimpleMailMessage();


        message.setTo(toEmail);


        message.setSubject(
                "Verify your AI Resume Analyzer Account"
        );


        message.setText(
                "Welcome to AI Resume Analyzer!\n\n"
                +
                "Verify your account using this link:\n\n"
                +
                "http://localhost:5173/verify?token="
                + token
        );



        mailSender.send(message);


    }







    public void sendOtpEmail(
            String toEmail,
            String otp
    ){


        SimpleMailMessage message = new SimpleMailMessage();



        message.setTo(toEmail);



        message.setSubject(
                "AI Resume Analyzer Password Reset OTP"
        );



        message.setText(

                "Your password reset OTP is:\n\n"

                + otp

                +

                "\n\nThis OTP is valid for a limited time."

        );



        mailSender.send(message);


    }


}