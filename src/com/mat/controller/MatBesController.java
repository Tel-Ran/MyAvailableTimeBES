package com.mat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.mat.json.AddressBook;
import com.mat.json.MyCalendar;
import com.mat.json.Person;
import com.mat.json.User;
import com.mat.response.Response;
import com.mat.exception.RestException;
import com.mat.interfaces.*;

import java.util.*;

@Controller
@RequestMapping("/front-server")
public class MatBesController extends ExceptionHandlerController {

	@Autowired
	IMatRepository persistenceServices;
	@Autowired
	IExternalServices externalServices;

	@RequestMapping(value = Constants.REQUEST_CREATE_USER, method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> createUser(@RequestBody User user) throws RestException {
		try {
			if (persistenceServices.createUser(user))
				return Response.emptyResponse();
			return Response.errorResponse(Constants.ERROR_EXISTED_USER);
		} catch (Exception e) {
			throw new RestException(e);
		}
	}

	@RequestMapping(value = Constants.REQUEST_LOGIN, method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> loginUser(@RequestBody User user) throws RestException {
		try {
			User res;
			if ((res = persistenceServices.loginUser(user)) != null)
				return Response.successResponse(res);
			return Response.errorResponse(Constants.ERROR_LOGIN);
		} catch (Exception e) {
			throw new RestException(e);
		}
	}

	@RequestMapping(value = Constants.REQUEST_GET_CALENDARS + "/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getCalendars(@PathVariable int userId) throws RestException {
		try {
			User res = persistenceServices.getCalendars(userId);
			return Response.successResponse(res);
		} catch (Exception e) {
			throw new RestException(e);
		}
	}

	@RequestMapping(value = Constants.REQUEST_ADD_PERSON, method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addPerson(@RequestBody Person person) throws RestException {
		try {
			if (persistenceServices.addPersonToAddressBook(person))
				return Response.emptyResponse();
			return Response.errorResponse(Constants.ERROR_EXISTED_PERSON);
		} catch (Exception e) {
			throw new RestException(e);
		}
	}

	@RequestMapping(value = Constants.REQUEST_GET_CONTACTS + "/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getContactList(@PathVariable int userId) throws RestException {
		try {
			AddressBook contacts = persistenceServices.getAddressBook(userId);
			return Response.successResponse(contacts);
		} catch (Exception e) {
			throw new RestException(e);
		}
	}

	@RequestMapping(value = Constants.REQUEST_CREATE_CALENDAR, method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> createCalendar(@RequestBody MyCalendar newCalendar) throws RestException {
		try {
			MyCalendar res = persistenceServices.createCalendar(newCalendar);
			if(res != null)
				return Response.successResponse(res);
			return Response.errorResponse(Constants.ERROR_CREATE_CALENDAR);
		} catch (Exception e) {
			throw new RestException(e);
		}
	}
	
	@RequestMapping(value = Constants.REQUEST_REMOVE_CALENDAR + "/{calendarId}", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> removeCalendar(@PathVariable int calendarId) throws RestException {
		try {
			if(services.removeCalendar(calendarId)) {
				return Response.emptyResponse();
			}
			return Response.errorResponse(Constants.ERROR_REPEAT);
		} catch (Exception e) {
			throw new RestException(e);
		}
	}
	
}
