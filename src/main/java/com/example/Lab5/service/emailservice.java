//package com.example.Lab5.service;
//
//import org.springframework.stereotype.Service;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//@Service
//public class emailservice {
//    private final JavaMailSender mail_sender;
//
//    public emailService(JavaMailSender mail_sender) {
//        this.mail_sender = mail_sender;
//    }
//
//    public void send_email (String to, String title, String text){
//        try {
//            MimeMessage message = mail_sender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message,true);
//
//            helper.setTo(to);
//            helper.setSubject(title);
//            helper.setText(text,true);
//
//            mail_sender.send(message);
//        }
//        catch (MessagingException e) {
//            System.err.println("ошибка с отправкой в email" + e);
//        }
//    }
//}
