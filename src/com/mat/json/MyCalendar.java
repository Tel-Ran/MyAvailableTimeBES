package com.mat.json;

import java.util.*;

/**
 * 
 * Information about my available calendar
 *
 */
public class MyCalendar {
	int userId;
	int calendarId;
	String calendarName;
	int weekNumber;
	List<Slot> slots;
	int duration; // time period in minutes

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getCalendarId() {
		return calendarId;
	}

	public void setCalendarId(int calendarId) {
		this.calendarId = calendarId;
	}

	public String getCalendarName() {
		return calendarName;
	}

	public void setCalendarName(String calendarName) {
		this.calendarName = calendarName;
	}

	public int getWeekNumber() {
		return weekNumber;
	}

	public void setWeekNumber(int weekNumber) {
		this.weekNumber = weekNumber;
	}

	public List<Slot> getSlots() {
		return slots;
	}

	public void setSlots(List<Slot> slots) {
		this.slots = slots;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	@Override
	public String toString() {
		return "MyCalendar [userId=" + userId + ", calendarId=" + calendarId + ", calendarName=" + calendarName
				+ ", weekNumber=" + weekNumber + ", slots=" + slots + ", duration=" + duration + "]";
	}

}
