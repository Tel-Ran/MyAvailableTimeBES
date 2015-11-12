package com.mat.interfaces;


import java.util.Date;

import com.mat.json.AddressBook;
import com.mat.json.Person;
import com.mat.json.User;

public interface IMatRepository {
	boolean createUser(User user);

	User loginUser(User user);

	User getCalendars(int userId);

	AddressBook getAddressBook(int userId);

	boolean addPersonToAddressBook(Person person);

	boolean repeatCalendar(int calendarId, Date date);
}
