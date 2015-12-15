package com.mat.mailsender;

import org.springframework.mail.*;

import com.mat.dao.*;
import com.mat.interfaces.*;

public class MatSender {
	
	MailSender sender;
	SimpleMailMessage template;
	
	public MatSender(MailSender sender, SimpleMailMessage template) {
		this.sender = sender;
		this.template = template;
	}
	
	public MatSender() {}

	public MailSender getSender() {
		return sender;
	}

	public void setSender(MailSender sender) {
		this.sender = sender;
	}

	public SimpleMailMessage getTemplate() {
		return template;
	}

	public void setTemplate(SimpleMailMessage template) {
		this.template = template;
	}
	
	public void sendMail(UserDAO userDao, String hashCode) {
		try {
			template.setTo(userDao.getEmail());
			String text = "Dear " + userDao.getFirstName() + " " + userDao.getLastName() + "!\nYour activation URL is " + Constants.SERVER_MAT_URL + 
					"/" + Constants.SERVER_MAT_ACTIVATE +  "/" + userDao.getId() + "/" + hashCode + "\nYou can copy this URL to your browser.";
			template.setText(text);
			sender.send(template);
			//return true;
		} catch (MailException e) {
			//return false;
		}
	}
	
}