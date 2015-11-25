package com.mat.external_tests;

import java.util.List;

import com.mat.interfaces.IExternalServices;
import com.mat.json.*;

public class ExternalServicesImplimentation implements IExternalServices {

	@Override
	public void setCredential(int userId, Scheduler scheduler, MatCredential credential) {

	}

	@Override
	public List<Scheduler> getAuthorizedSchedulers(int userId, List<Scheduler> schedulers) throws Throwable {
		return schedulers;
	}

	@Override
	public List<ExternalCalendar> getCalendars(int userId, List<Scheduler> schedulers) throws Throwable {
		return null;
	}

	@Override
	public List<Person> getContacts(int userId, List<Scheduler> schedulers) throws Throwable {
		return null;
	}

	@Override
	public boolean upload(int userId, UploadRequest request) throws Throwable {
		return false;
	}

	@Override
	public DownloadEventsResponse download(int userId, DownloadEventsRequest request) throws Throwable {
		return null;
	}

}
