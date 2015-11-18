package com.mat.interfaces;

import java.util.*;

import com.mat.json.*;

public interface IExternalServices {
	void setCredential(int userId, Scheduler scheduler, Credential credential);

	List<Scheduler> getAuthorizedSchedulers(int userId, List<Scheduler> schedulers) throws Throwable; // null
																										// if
																										// not
																										// authorized?

	List<ExternalCalendar> getCalendars(int userId, List<Scheduler> schedulers) throws Throwable;

	List<Person> getContacts(int userId, List<Scheduler> schedulers) throws Throwable;

	boolean upload(int userId, UploadRequest request) throws Throwable;

	DownloadEventsResponse download(int userId, DownloadEventsRequest request) throws Throwable;
}