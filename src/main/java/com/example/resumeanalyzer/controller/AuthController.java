package com.example.resumeanalyzer.controller;


import com.example.resumeanalyzer.model.User;
import com.example.resumeanalyzer.repository.UserRepository;
import com.example.resumeanalyzer.service.EmailService;
import java.util.Map;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {



    @Autowired
    private UserRepository userRepository;



    @Autowired
    private EmailService emailService;






    @PostMapping("/register")
    public String register(
            @RequestBody User user
    ){



        if(userRepository.findByEmail(user.getEmail()).isPresent()){


            return "Email already exists";


        }




        userRepository.save(user);



        return "Registration Successful";


    }









    @PostMapping("/login")
    public Map<String,String> login(
            @RequestBody User user
    ){



        User existingUser =
                userRepository
                .findByEmail(user.getEmail())
                .orElse(null);




        if(existingUser == null){


            return Map.of(
                    "message",
                    "User not found"
            );


        }






        if(!existingUser.getPassword()
                .equals(user.getPassword())){


            return Map.of(
                    "message",
                    "Wrong Password"
            );


        }





        return Map.of(

                "message",
                "Login Successful",

                "email",
                existingUser.getEmail(),

                "name",
                existingUser.getName()

        );



    }









    @PostMapping("/forgot-password")
    public String forgotPassword(
            @RequestBody Map<String,String> data
    ){



        String email = data.get("email");



        User user =
                userRepository
                .findByEmail(email)
                .orElse(null);




        if(user == null){

            return "Email not registered";

        }





        String otp =
                String.valueOf(
                new Random().nextInt(900000)+100000
                );




        user.setResetOtp(otp);


        userRepository.save(user);




        emailService.sendOtpEmail(
                email,
                otp
        );




        return "OTP sent successfully";


    }









    @PostMapping("/verify-otp")
    public String verifyOtp(
            @RequestBody Map<String,String> data
    ){



        User user =
                userRepository
                .findByEmail(data.get("email"))
                .orElse(null);




        if(user == null){

            return "User not found";

        }




        if(user.getResetOtp()
                .equals(data.get("otp"))){


            return "OTP Verified";


        }




        return "Invalid OTP";


    }









    @PostMapping("/reset-password")
    public String resetPassword(
            @RequestBody Map<String,String> data
    ){



        User user =
                userRepository
                .findByEmail(data.get("email"))
                .orElse(null);




        if(user == null){

            return "User not found";

        }





        user.setPassword(
                data.get("password")
        );



        user.setResetOtp(null);



        userRepository.save(user);




        return "Password Updated Successfully";


    }



}