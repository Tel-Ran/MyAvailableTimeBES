package com.mat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.mat.json.AddressBook;
import com.mat.json.MyCalendar;
import com.mat.json.Person;
import com.mat.json.Scheduler;
import com.mat.json.Slot;
import com.mat.json.User;
import com.mat.response.Response;
import com.mat.exception.RestException;
import com.mat.interfaces.*;

import java.text.SimpleDateFormat;
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
			if ((res = persistenceServices.loginUser(user)) != null) {
				List<Scheduler> schedulers = externalServices.getAuthorizedSchedulers(res.getUserId(),
						res.getSchedulers());
				res.setSchedulers(schedulers);
				return Response.successResponse(res);
			}
			return Response.errorResponse(Constants.ERROR_LOGIN);
		} catch (Throwable e) {
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
			if (res != null)

				return Response.successResponse(res);
			return Response.errorResponse(Constants.ERROR_CREATE_CALENDAR);
		} catch (Exception e) {
			throw new RestException(e);
		}
	}

	@RequestMapping(value = Constants.REQUEST_REMOVE_CALENDAR + "/{calendarId}", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> removeCalendar(@PathVariable int calendarId) throws RestException {
		try {
			if (persistenceServices.removeCalendar(calendarId)) {
				return Response.emptyResponse();
			}
			return Response.errorResponse(Constants.ERROR_REMOVE_CALENDAR);
		} catch (Exception e) {
			throw new RestException(e);
		}
	}

	@RequestMapping(value = Constants.REQUEST_GET_WEEK + "/{idCalendar}" + "/{weekNumber}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getWeek(@PathVariable int idCalendar, @PathVariable int weekNumber)
			throws RestException {
		try {
			MyCalendar myCalendar = persistenceServices.getWeek(idCalendar, weekNumber);
			if (myCalendar != null)
				return Response.successResponse(myCalendar);
			return Response.errorResponse(Constants.ERROR_GET_WEEK);
		} catch (Exception e) {
			throw new RestException(e);
		}
	}

	@RequestMapping(value = Constants.REQUEST_EDIT_CALENDAR, method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> editCalendar(@RequestBody MyCalendar myCalendar) throws RestException {
		try {
			if (persistenceServices.editCalendar(myCalendar))
				return Response.emptyResponse();
			return Response.errorResponse(Constants.ERROR_EDIT_CALENDAR);
		} catch (Exception e) {
			throw new RestException(e);
		}
	}

	@RequestMapping(value = Constants.REQUEST_CREATE_CALENDAR_COLLABORATED, method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> createCalendarCollaborated(@RequestBody MyCalendar newCalendar) throws RestException {
		try {
			MyCalendar res = persistenceServices.createCollaborationCal(newCalendar);
			if (res != null)
				return Response.successResponse(res);
			return Response.errorResponse(Constants.ERROR_CREATE_CALENDAR_COLLABORATED);
		} catch (Exception e) {
			throw new RestException(e);
		}
	}

	@RequestMapping(value = Constants.REQUEST_CHANGE_USER_DATA, method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> changeUserData(@RequestBody User user) throws RestException {
		try {
			if (persistenceServices.changeUserData(user))
				return Response.emptyResponse();
			return Response.errorResponse(Constants.ERROR_CHANGE_USER_DATA);
		} catch (Exception e) {
			throw new RestException(e);
		}
	}

	@RequestMapping(value = Constants.REQUEST_SET_CLIENT, method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> setClient(@RequestBody Slot slot) throws RestException {
		try {
			if (persistenceServices.setClientToSlot(slot))
				return Response.emptyResponse();
			return Response.errorResponse(Constants.ERROR_FIND_PERSON);
		} catch (Exception e) {
			throw new RestException(e);
		}
	}

	@RequestMapping(value = Constants.REQUEST_REPEAT_CALENDAR + "/{dateString}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> repeatCalendar(@PathVariable String dateString, @RequestBody MyCalendar myCalendar) throws RestException {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date date = df.parse(dateString);
			if (persistenceServices.repeatCalendar(myCalendar, date))
				return Response.emptyResponse();
			return Response.errorResponse(Constants.ERROR_REPEAT_CALENDAR);
		} catch (Exception e) {
			throw new RestException(e);
		}
	}
}
