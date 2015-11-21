package com.mat.json;

import java.util.*;

public class User {
	int userId;
	String email; // username
	String password;
	List<Scheduler> schedulers;// list of scheduler objects describing info
								// about Google/Outlook
	String timeZone;// time zone according to the Java format
	boolean format24; // flag containing true if time format 24 (12,18,21,...)
						// false time format 12 (6pm,9pm,...)
	String firstName;
	String lastName;
	String phoneNumber;

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	List<MyCalendar> calendars;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Scheduler> getSchedulers() {
		return schedulers;
	}

	public void setSchedulers(List<Scheduler> schedulers) {
		this.schedulers = schedulers;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public boolean isFormat24() {
		return format24;
	}

	public void setFormat24(boolean format24) {
		this.format24 = format24;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public List<MyCalendar> getCalendars() {
		return calendars;
	}

	public void setCalendars(List<MyCalendar> calendars) {
		this.calendars = calendars;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", email=" + email + ", password=" + password + ", schedulers=" + schedulers
				+ ", timeZone=" + timeZone + ", format24=" + format24 + ", firstName=" + firstName + ", lastName="
				+ lastName + ", calendars=" + calendars + "]";
	}

}
