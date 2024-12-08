//package com.example.Lab5.controller;
//
//import com.example.Lab5.service.emailservice;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class emailcontroller {
//        private final emailservice emailService;
//
//        public emailController(emailservice emailService) {
//            this.emailService = emailService;
//        }
//
//        @GetMapping("/send/email")
//        public String send_email (@RequestParam String to, @RequestParam String title, @RequestParam String text){
//            emailService.send_email(to,title,text);
//            return "уведомление отправлено!!!";
//        }
//
//}

