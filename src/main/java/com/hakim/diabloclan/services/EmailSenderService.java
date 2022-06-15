package com.hakim.diabloclan.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.hakim.diabloclan.models.EmailModel;

@Service
public class EmailSenderService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	public String sendEmail(EmailModel emailModel) {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom("hakimososos@gmail.com");
			message.setTo(emailModel.getToEmail());
			message.setText(emailModel.getBody());
			message.setSubject(emailModel.getSubject());
			
			mailSender.send(message);
			
			System.out.println("Mail sent successfully...");
			
			return "Mail sent successfully...";
	}
}
