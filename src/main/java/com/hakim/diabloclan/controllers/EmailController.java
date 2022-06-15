package com.hakim.diabloclan.controllers;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hakim.diabloclan.models.EmailModel;

import com.hakim.diabloclan.services.EmailSenderService;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class EmailController {

	@Autowired
	private EmailSenderService emailSenderService;
	
	@PostMapping("/sendemail")
	public String sendEmail(@RequestBody EmailModel emailModel){
		return emailSenderService.sendEmail(emailModel);
	}
		
	
	
}
