package com.mat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
	IMatRepository services;

	@RequestMapping(value = Constants.REQUEST_CREATE_USER, method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> createUser(@RequestBody User user) throws RestException {
		try {
			if (services.createUser(user))
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
			if ((res = services.loginUser(user)) != null)
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
			User res = services.getCalendars(userId);
			return Response.successResponse(res);
		} catch (Exception e) {
			throw new RestException(e);
		}
	}

	@RequestMapping(value = Constants.REQUEST_ADD_PERSON, method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addPerson(@RequestBody Person person) throws RestException {
		try {
			if (services.addPersonToAddressBook(person))
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
			AddressBook contacts = services.getAddressBook(userId);
			return Response.successResponse(contacts);
		} catch (Exception e) {
			throw new RestException(e);
		}
	}
	
	@RequestMapping(value = Constants.REQUEST_REPEAT, method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> repeatCalendar(@RequestBody int calendarId, @RequestBody Date date) throws RestException {
		try {
			if(services.repeatCalendar(calendarId, date))
				return Response.emptyResponse();
			return Response.errorResponse(Constants.ERROR_REPEAT);
		} catch (Exception e) {
			throw new RestException(e);
		}
	}

}