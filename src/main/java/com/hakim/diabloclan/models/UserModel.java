package com.hakim.diabloclan.models;

public class UserModel {
	
	private String email;
	private String userName;
	private String password;
	private String battleTag;
	
	public UserModel() {
		
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBattleTag() {
		return battleTag;
	}

	public void setBattleTag(String battleTag) {
		this.battleTag = battleTag;
	}
	
	
	

}
