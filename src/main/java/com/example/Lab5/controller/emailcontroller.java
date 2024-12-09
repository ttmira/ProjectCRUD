package com.example.Lab5.controller;

import com.example.Lab5.service.emailservice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class emailcontroller {
        private final emailservice emailService;

        public emailcontroller(emailservice emailService) {
            this.emailService = emailService;
        }

        @GetMapping("/sendemail")
        public String sendemail (@RequestParam String to, @RequestParam String title, @RequestParam String text){
            emailService.sendemail(to,title,text);
            return "sent successfully!";
        }

}

