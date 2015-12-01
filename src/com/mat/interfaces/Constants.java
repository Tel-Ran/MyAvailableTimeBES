package com.mat.interfaces;

public interface Constants {

	String ERROR_EXISTED_USER = "User with current email already exists";
	String ERROR_LOGIN = "Username or password is wrong";
	String ERROR_EXISTED_PERSON = "Current person is already in your Address Book";
	String ERROR_REPEAT_CALENDAR = "Error repeat until specified date";
	String ERROR_CREATE_CALENDAR = "Error to create new Calendar";
	String ERROR_REMOVE_CALENDAR = "Current calendar has not been not found";
	String ERROR_GET_WEEK = "There are no slots in the current week";
	String ERROR_EDIT_CALENDAR = "Error editing calendar";
	String ERROR_CREATE_CALENDAR_COLLABORATED = "Error creating collaborated calendar";
	String ERROR_CHANGE_USER_DATA = "Error changing User data";
	String ERROR_FIND_PERSON = "Current person has not been found";
	String ERROR_REMOVE_CLIENT = "there is no client to this slot";
	String ERROR_REMOVE_PERSON = "This person is used by some collaboration or sharing";
	String ERROR_GET_SHEDULERS = "Error getting shedullers";
	String ERROR_ADD_IMPORTED_PERSON = "Error adding imported Persons";
	
	String ERROR_SET_PARTICIPANTS = "Unable to add participants to specified Slot";
	
	String REQUEST_LOGIN = "/login";
	String REQUEST_CREATE_USER = "/createUser";
	String REQUEST_GET_CALENDARS = "/getCalendars";
	String REQUEST_ADD_PERSON = "/addPerson";
	String REQUEST_GET_CONTACTS = "/getContacts";
	String REQUEST_REPEAT_CALENDAR = "/repeatCalendar";
	String REQUEST_REMOVE_CALENDAR = "/removeCalendar";
	String REQUEST_CREATE_CALENDAR = "/createCalendar";
	String REQUEST_GET_WEEK = "/getWeek";
	String REQUEST_EDIT_CALENDAR = "/editCalendar";
	String REQUEST_CREATE_CALENDAR_COLLABORATED = "/createCollaboratedCal";
	String REQUEST_CHANGE_USER_DATA = "/changeData";
	String REQUEST_SET_CLIENT = "/setClient";
	String REQUEST_REMOVE_CLIENT = "/removeClient";
	String REQUEST_REMOVE_PERSON = "/removePerson";
	String REQUEST_SCHEDULERS = "/getSchedulers";	
	String REQUEST_PERSONS = "/getPersons";
	String REQUEST_ADD_IMPORTED_PRSONS = "/addImportedPersons";
	String REQUEST_SET_PARTICIPANTS = "setParticipants";
	
	int CALENDAR_TYPE_COLLABORATED = 2;
	String SLOT_STATUS_FREE = "free";
	String STATUS_GREEN = "green";
	
	String MAP_SLOT_LIST = "slots";
	String MAP_PERSON_LIST = "persons";

}