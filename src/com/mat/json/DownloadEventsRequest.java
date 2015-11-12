package com.mat.json;

import java.util.*;

/**
 * 
 * information in download request from FES
 *
 */
public class DownloadEventsRequest {

	int userId;
	// Dates range [from,to]
	Date fromDate;
	Date toDate;
	// information about requested calendars
	List<ExternalCalendar> calendars;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public List<ExternalCalendar> getCalendars() {
		return calendars;
	}

	public void setCalendars(List<ExternalCalendar> calendars) {
		this.calendars = calendars;
	}

	@Override
	public String toString() {
		return "DownloadEventsRequest [userId=" + userId + ", fromDate=" + fromDate + ", toDate=" + toDate
				+ ", calendars=" + calendars + "]";
	}
}
