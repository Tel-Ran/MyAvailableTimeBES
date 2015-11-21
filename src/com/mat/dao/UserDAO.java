package com.mat.dao;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class UserDAO {

	@Id
	@GeneratedValue
	int id;

	String email; // username

	String password;

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

	@OneToMany(mappedBy = "user")
	List<SchedulerDAO> shedullers;
	@OneToMany(mappedBy = "user")
	List<SocialNetworkDAO> sNetworks;

	@OneToMany(mappedBy = "user")
	List<PersonDAO> addressBook;

	@OneToMany(mappedBy = "user")
	List<CalendarDAO> calendars;

	public UserDAO() {
		super();
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

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public List<SchedulerDAO> getShedullers() {
		return shedullers;
	}

	public void setShedullers(List<SchedulerDAO> shedullers) {
		this.shedullers = shedullers;
	}

	public List<SocialNetworkDAO> getsNetworks() {
		return sNetworks;
	}

	public void setsNetworks(List<SocialNetworkDAO> sNetworks) {
		this.sNetworks = sNetworks;
	}

	public List<PersonDAO> getAddressBook() {
		return addressBook;
	}

	public void setAddressBook(List<PersonDAO> addressBook) {
		this.addressBook = addressBook;
	}

	public List<CalendarDAO> getCalendars() {
		return calendars;
	}

	public void setCalendars(List<CalendarDAO> calendars) {
		this.calendars = calendars;
	}

}
