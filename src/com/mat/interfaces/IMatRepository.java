package com.mat.interfaces;



import java.util.*;
import com.mat.json.*;

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

	boolean changeUserData(User user);

	boolean setClientToSlot(Slot slot);
	
	boolean repeatCalendar(MyCalendar calendar, Date date);

	boolean removeClientFromSlot(int slotId);
	
	boolean removePerson(int id);
	
	boolean setParticipantsToSlots(int calendarId, LinkedHashMap<String, List<?>> slotsAndParticipants);

	List<Scheduler> getSchedulers(int userId);

	boolean addImportedPersons(AddressBook book);

	boolean addScheduler(Scheduler entry);

	boolean editScheduler(Scheduler scheduler);

	boolean removeScheduler(int id);

}
