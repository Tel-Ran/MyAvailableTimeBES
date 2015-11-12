package com.mat.interfaces;

public interface Constants {

	String ERROR_EXISTED_USER = "User with current email already exists";
	String ERROR_LOGIN = "Username or password is wrong";
	String ERROR_EXISTED_PERSON = "Current person is already in your Address Book";
	String ERROR_REPEAT = "";
	
	String REQUEST_LOGIN = "/login";
	String REQUEST_CREATE_USER = "/createUser";
	String REQUEST_GET_CALENDARS = "/getCalendars";
	String REQUEST_ADD_PERSON = "/addPerson";
	String REQUEST_GET_CONTACTS = "/getContacts";
	String REQUEST_REPEAT = "/repeat";
	
	String SLOT_STATUS_FREE = "free";
	
	

}
