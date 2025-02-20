package com.jtc.util;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;



@Service
public class EmailService {

	   private final JavaMailSender mailSender;

	public EmailService(JavaMailSender mailSender) {
		super();
		this.mailSender = mailSender;
	}
	
	
	  @Async
	    public void sendEmail(String to, String subject, String body) {
	        try {
	            MimeMessage message = mailSender.createMimeMessage();
	            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
	            
	            helper.setTo(to);
	            helper.setSubject(subject);
	            helper.setText(body, true); // true indicates HTML content
	            
	            mailSender.send(message);
	        } catch (MessagingException e) {
	            throw new RuntimeException("Failed to send email", e);
	        }
	    }
	   
	   
	
	
}
