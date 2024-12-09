package com.example.Lab5.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;


@Service
public class emailservice {

    private final JavaMailSender mailsender;

    public emailservice(JavaMailSender mailsender) {
        this.mailsender = mailsender;
    }
    public void sendemail (String to, String title, String text){
        try {
            MimeMessage message = mailsender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            helper.setTo(to);
            helper.setSubject(title);
            helper.setText(text,true);
            mailsender.send(message);
        }
        catch (MessagingException e) {
            System.err.println("ошибка с отправкой в email" + e);
        }
    }
}
