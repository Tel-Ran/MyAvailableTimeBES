package com.mat.mediator;

import com.mat.dao.*;

import java.util.LinkedList;
import java.util.List;

import com.mat.json.*;

/**
 * Created by Armen on 04.11.15.
 */
public class ConvertorJsonToDao {

	public static List<SlotDAO> getSlotsDAO(List<Slot> slots) {
		if (slots == null)
			return null;
		List<SlotDAO> slotsDao = new LinkedList<SlotDAO>();
		for (Slot slot : slots) {
			slotsDao.add(convertSlot(slot));
		}
		return slotsDao;
	}

	public static SlotDAO convertSlot(Slot slot) {
		if (slot == null)
			return null;
		SlotDAO slotDao = new SlotDAO();
		slotDao.setBeginning(slot.getBeginning());
		slotDao.setMessageBar(slot.getMessageBar());
		slotDao.setStatus(convertStatus(slot.getStatus()));
		return slotDao;
	}

	public static List<PersonDAO> convertParticipants(List<Person> participants) {
		if (participants == null)
			return null;

		List<PersonDAO> personsDao = new LinkedList<PersonDAO>();
		for (Person person : participants) {
			personsDao.add(convertClient(person));
		}
		return personsDao;
	}

	public static StatusDAO convertStatus(Status status) {
		if (status == null)
			return null;
		StatusDAO statusDao = new StatusDAO();
		statusDao.setConfirmation(status.getConfirmation());
		statusDao.setStatusName(status.getStatusName());
		return statusDao;
	}

	public static PersonDAO convertClient(Person client) {
		if (client == null)
			return null;
		PersonDAO personDao = new PersonDAO();
		personDao.setEmail(client.getEmail());
		personDao.setLastName(client.getLastName());
		personDao.setName(client.getFirstName());
		return personDao;
	}

	public static CalendarDAO convertCalendar(MyCalendar myCalendar, UserDAO user) {
		if (myCalendar == null)
			return null;
		CalendarDAO calendarDao = new CalendarDAO();
		String calendarName = myCalendar.getCalendarName();
		int duration = myCalendar.getDuration();
		calendarDao.setUser(user);
		calendarDao.setCalendarName(calendarName);
		calendarDao.setDuration(duration);
		return calendarDao;
	}
}
