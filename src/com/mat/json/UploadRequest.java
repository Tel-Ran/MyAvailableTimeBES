package com.mat.json;

/*
 * information for my available calendar upload
 */
import java.util.*;

public class UploadRequest {
	int userId;
	String myCalendarName;
	int duration; // time period in minutes
	List<ExternalCalendar> calendars; // calendars for upload Google,Outlook
	List<Slot> slots;// list of dates from which the selected slots begin

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getMyCalendarName() {
		return myCalendarName;
	}

	public void setMyCalendarName(String myCalendarName) {
		this.myCalendarName = myCalendarName;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public List<ExternalCalendar> getCalendars() {
		return calendars;
	}

	public void setCalendars(List<ExternalCalendar> calendars) {
		this.calendars = calendars;
	}

	public List<Slot> getSlots() {
		return slots;
	}

	public void setSlots(List<Slot> slots) {
		this.slots = slots;
	}

	@Override
	public String toString() {
		return "UploadRequest [userId=" + userId + ", myCalendarName=" + myCalendarName + ", duration=" + duration
				+ ", calendars=" + calendars + ", slots=" + slots + "]";
	}

}
