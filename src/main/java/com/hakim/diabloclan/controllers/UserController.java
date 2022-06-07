package com.hakim.diabloclan.controllers;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hakim.diabloclan.models.UserModel;
import com.hakim.diabloclan.services.AccountService;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class UserController {

	@Autowired
	private AccountService accountService;
	
	@PostMapping("/user")
	public String saveUser(@RequestBody UserModel userModel) throws InterruptedException, ExecutionException {
		return accountService.saveAccount(userModel);
	}
	
	@PostMapping("/connection")
	public boolean connection(@RequestBody UserModel userModel) throws InterruptedException, ExecutionException {
		return accountService.connection(userModel);
	}
}
