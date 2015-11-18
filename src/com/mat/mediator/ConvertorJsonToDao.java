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
		List<SlotDAO> slotsDao = new LinkedList<SlotDAO>();
		for (Slot slot : slots) {
			slotsDao.add(convertSlot(slot));
		}
		return slotsDao;
	}

	public static SlotDAO convertSlot(Slot slot) {
		SlotDAO slotDao=new SlotDAO();
		slotDao.setBeginning(slot.getBeginning());
		slotDao.setClient(convertClient(slot.getClient()));
		slotDao.setMessageBar(slot.getMessageBar());
		slotDao.setStatus(convertStatus(slot.getStatus()));
		slotDao.setParticipants(convertParticipants(slot.getParticipants()));
		
		return slotDao;
	}

	public static List<PersonDAO> convertParticipants(List<Person> participants) {
		List<PersonDAO> personsDao=new LinkedList<PersonDAO>();
		for(Person person:participants){
			personsDao.add(convertClient(person));
		}
		return personsDao;
	}

	public static StatusDAO convertStatus(Status status) {
		StatusDAO statusDao=new StatusDAO();
		statusDao.setConfirmation(status.getConfirmation());
		statusDao.setStatusName(status.getStatusName());
		return statusDao;
	}

	public static PersonDAO convertClient(Person client) {
		PersonDAO personDao=new PersonDAO();
		personDao.setEmail(client.getEmail());
		personDao.setLastName(client.getLastName());
		personDao.setName(client.getFirstName());
		return personDao;
	}

	public static CalendarDAO convertCalendar(MyCalendar myCalendar,
			UserDAO user) {
		CalendarDAO calendarDao = new CalendarDAO();
		String calendarName = myCalendar.getCalendarName();
		int duration = myCalendar.getDuration();
		calendarDao.setUser(user);
		calendarDao.setCalendarName(calendarName);
		calendarDao.setDuration(duration);
		return calendarDao;
	}
}
