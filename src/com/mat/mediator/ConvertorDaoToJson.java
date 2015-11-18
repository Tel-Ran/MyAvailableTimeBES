package com.mat.mediator;

import java.util.LinkedList;
import java.util.List;

import com.mat.dao.*;
import com.mat.json.*;

public class ConvertorDaoToJson {

	public static List<Slot> getSlots(List<SlotDAO> slotsDAO) {
		List<Slot> slotsJson = new LinkedList<Slot>();
		for (SlotDAO slotDao : slotsDAO) {
			Slot sl=convertSlot(slotDao);
			slotsJson.add(sl);
		}
		return slotsJson;
	}

	private static Slot convertSlot(SlotDAO slotDao) {
		Slot slotJson = new Slot();
		slotJson.setBeginning(slotDao.getBeginning());
		slotJson.setClient(convertPerson(slotDao.getClient()));
		slotJson.setMessageBar(slotDao.getMessageBar());
		slotJson.setStatus(convertStatus(slotDao.getStatus()));
		slotJson.setParticipants(convertParticipants(slotDao.getParticipants()));
		return slotJson;
	}

	private static List<Person> convertParticipants(List<PersonDAO> participants) {
		List<Person> personsJson = new LinkedList<Person>();
		for (PersonDAO person : participants) {
			personsJson.add(convertPerson(person));
		}
		return personsJson;
	}

	private static Status convertStatus(StatusDAO statusDAO) {
		Status statusJson = new Status();
		statusJson.setConfirmation(statusDAO.getConfirmation());
		statusJson.setStatusName(statusDAO.getStatusName());
		return statusJson;
	}

	private static Person convertPerson(PersonDAO client) {
		Person personJson = new Person();
		personJson.setEmail(client.getEmail());
		personJson.setFirstName(client.getName());
		personJson.setLastName(client.getLastName());
		return personJson;
	}

	public static MyCalendar convertCalendar(CalendarDAO calendarDao) {
		MyCalendar myCalendar = new MyCalendar();
		myCalendar.setCalendarId(calendarDao.getId());
		myCalendar.setCalendarName(calendarDao.getCalendarName());
		myCalendar.setDuration(calendarDao.getDuration());
		myCalendar.setUserId(calendarDao.getUser().getId());
		myCalendar.setSlots(getSlots(calendarDao.getSlots()));
		return myCalendar;
	}
}
