package com.jsp.quiz_application.service;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String message) {

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(to);
        mail.setSubject(subject);
        mail.setText(message);

        mailSender.send(mail);
    }
    public void sendEmailWithAttachment(String to,
                                        String subject,
                                        String text,
                                        byte[] pdf,String fileName) throws Exception {

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper =
                new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text,true);

        helper.addAttachment(fileName,
                new ByteArrayResource(pdf));

        mailSender.send(message);

        System.out.println("Email sent to " + to);
    }
    public void sendHtmlEmail(String to,
                              String subject,
                              String htmlContent) throws Exception {

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper =
                new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setFrom("sakshikore8070@gmail.com");
        helper.setSubject(subject);
        helper.setText(htmlContent, true);



        mailSender.send(message);

        System.out.println("HTML Email sent to " + to);
    }
}