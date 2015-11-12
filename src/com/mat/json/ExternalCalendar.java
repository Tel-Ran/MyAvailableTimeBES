package com.mat.json;

public class ExternalCalendar {
	String calendarService;// such as Google,Outlook,MyAvailable
	String calendarName;// such as "tennis",work,etc.

	public String getCalendarService() {
		return calendarService;
	}

	public void setCalendarService(String calendarService) {
		this.calendarService = calendarService;
	}

	public String getCalendarName() {
		return calendarName;
	}

	public void setCalendarName(String calendarName) {
		this.calendarName = calendarName;
	}

	@Override
	public String toString() {
		return "ExternalCalendar [calendarService=" + calendarService + ", calendarName=" + calendarName + "]";
	}

}
