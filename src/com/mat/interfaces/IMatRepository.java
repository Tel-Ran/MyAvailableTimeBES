package com.mat.interfaces;



import com.mat.json.AddressBook;
import com.mat.json.MyCalendar;
import com.mat.json.Person;
import com.mat.json.User;

public interface IMatRepository {
	boolean createUser(User user);

	User loginUser(User user);

	User getCalendars(int userId);

	AddressBook getAddressBook(int userId);

	boolean addPersonToAddressBook(Person person);

	MyCalendar createCalendar(MyCalendar newCalendar);

	boolean removeCalendar(int id);
	
	MyCalendar getWeek(int calendarId, int weekNumber);

	boolean editCalendar(MyCalendar myCalendar);

	MyCalendar createCollaborationCal(MyCalendar myCalendar);

}
