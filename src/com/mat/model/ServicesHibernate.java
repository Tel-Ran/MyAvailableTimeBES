package com.mat.model;

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import com.mat.dao.*;
import com.mat.interfaces.*;
import com.mat.json.*;
import com.mat.mediator.ConvertorDaoToJson;
import com.mat.mediator.ConvertorJsonToDao;

public class ServicesHibernate implements IMatRepository {

	@PersistenceContext(unitName = "springHibernate")
	public EntityManager em;

	@SuppressWarnings("unchecked")
	@Transactional
	public boolean createUser(User user) {
		Query q = em.createQuery("select user from UserDAO user where user.email=?1");
		q.setParameter(1, user.getEmail());
		List<UserDAO> response = q.getResultList();
		if (response.size() == 0) {
			UserDAO userDAO = new UserDAO();
			userDAO.setEmail(user.getEmail());
			userDAO.setFirstName(user.getFirstName());
			userDAO.setLastName(user.getLastName());
			userDAO.setPassword(user.getPassword());
			em.persist(userDAO);
			return true;
		}
		return false;
	}

	// login
	@Override
	@Transactional
	public User loginUser(User user) {
		Query q = em.createQuery("SELECT u FROM UserDAO u where u.email = ?1");
		q.setParameter(1, user.getEmail());
		UserDAO response;
		try {
			response = (UserDAO) q.getSingleResult();
		} catch (NoResultException exception) {
			return null;
		}
		if (user.getPassword().equals(response.getPassword())) {
			user.setFirstName(response.getFirstName());
			user.setLastName(response.getLastName());
			user.setTimeZone(response.getTimeZone());
			user.setFormat24(response.isFormat24());
			user.setUserId(response.getId());
			List<SchedulerDAO> schedulersDao = response.getShedullers();
			if (schedulersDao != null && schedulersDao.size() != 0) {
				List<Scheduler> schedulers = new LinkedList<>();
				for (SchedulerDAO sch : schedulersDao) {
					Scheduler newSch = new Scheduler();
					newSch.setAccountName(sch.getAccountName());
					newSch.setSchedulerName(sch.getSchedulerName());
					schedulers.add(newSch);
				}
				user.setSchedulers(schedulers);
			}
			return user;
		}
		return null;
	}

	@Override
	@Transactional
	public User getCalendars(int userId) {
		UserDAO userDao = em.find(UserDAO.class, userId);
		Iterable<CalendarDAO> daoCalendarsList = userDao.getCalendars();

		List<MyCalendar> calendars = new LinkedList<>();
		for (CalendarDAO calendarDao : daoCalendarsList) {
			MyCalendar temp = new MyCalendar();
			temp.setCalendarId(calendarDao.getId());
			temp.setCalendarName(calendarDao.getCalendarName());
			calendars.add(temp);
		}
		User res = new User();
		res.setCalendars(calendars);
		return res;
	}

	@Override
	@Transactional
	public boolean addPersonToAddressBook(Person person) {
		int userId = person.getUserId();

		UserDAO user = em.find(UserDAO.class, userId);
		List<PersonDAO> personsDao = user.getAddressBook();
		for (PersonDAO temp : personsDao) {
			if (person.getEmail().equals(temp.getEmail()))
				return false;
		}
		PersonDAO personDAO = new PersonDAO();
		personDAO.setLastName(person.getLastName());
		personDAO.setEmail(person.getEmail());
		personDAO.setName(person.getFirstName());

		personDAO.setUser(user);
		em.persist(personDAO);
		return true;
	}

	@Override
	@Transactional
	public AddressBook getAddressBook(int userId) {
		UserDAO user = em.find(UserDAO.class, userId);
		List<PersonDAO> persons = user.getAddressBook();
		List<Person> contacts = new LinkedList<Person>();
		for (PersonDAO person : persons) {
			Person temp = new Person();
			temp.setFirstName(person.getName());
			temp.setLastName(person.getLastName());
			temp.setEmail(person.getEmail());
			contacts.add(temp);
		}
		AddressBook res = new AddressBook();
		res.setPersons(contacts);
		return res;
	}

	@Override
	@Transactional
	public MyCalendar createCalendar(MyCalendar newCalendar) {
		UserDAO userDao = em.find(UserDAO.class, newCalendar.getUserId());
		CalendarDAO calendarDao = new CalendarDAO();
		calendarDao.setCalendarName(newCalendar.getCalendarName());
		calendarDao.setDuration(newCalendar.getDuration());
		calendarDao.setUser(userDao);
		em.persist(calendarDao);
		newCalendar.setCalendarId(calendarDao.getId());
		return newCalendar;
	}

	@Override
	@Transactional
	public boolean removeCalendar(int id) {
		CalendarDAO cal = em.find(CalendarDAO.class, id);
		if (cal != null) {
			List<SlotDAO> slots = cal.getSlots();
			for (SlotDAO slot : slots) {
				em.remove(slot);
			}
			em.remove(cal);
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public MyCalendar getWeek(int calendarId, int weekNumber) {
		List<Date> startEndDates = getStartEndDays(weekNumber);

		Query q = em
				.createQuery(
						"select slot from SlotDAO slot where slot.calendar.id = ?1 and slot.beginning > ?2 and slot.beginning < ?3")
				.setParameter(1, calendarId).setParameter(2, startEndDates.get(0))
				.setParameter(3, startEndDates.get(1));

		List<SlotDAO> slotsWeekDAO = q.getResultList();
		List<Slot> slotsWeekJson = ConvertorDaoToJson.getSlots(slotsWeekDAO);
		CalendarDAO calendarDAO = em.find(CalendarDAO.class, calendarId);// calendar
																			// from
																			// the
																			// Database
		MyCalendar myCalendar = new MyCalendar();// new calendar creating

		myCalendar.setCalendarId(calendarId);
		myCalendar.setCalendarName(calendarDAO.getCalendarName());
		myCalendar.setDuration(calendarDAO.getDuration());
		myCalendar.setSlots(slotsWeekJson);
		myCalendar.setWeekNumber(weekNumber);
		return myCalendar;
	}

	private List<Date> getStartEndDays(int weekNumber) {
		int coef = 7 * weekNumber;
		List<Date> res = new ArrayList<Date>();
		Calendar calendar = new GregorianCalendar();
		int difference = calendar.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
		if (difference < 0) {
			calendar.add(Calendar.DAY_OF_WEEK, -7 - difference + coef);
		} else {
			calendar.add(Calendar.DAY_OF_WEEK, -difference + coef);
		}
		// setting min time

		calendar.add(Calendar.DAY_OF_YEAR, -1);
		calendar.set(Calendar.HOUR, 11);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);

		res.add(calendar.getTime());
		// setting max time
		calendar.add(Calendar.DAY_OF_WEEK, 7);

		res.add(calendar.getTime());
		return res;
	}

	@Override
	@Transactional
	public boolean editCalendar(MyCalendar myCalendarJson) {
		List<Slot> slotsJson = myCalendarJson.getSlots();
		CalendarDAO calendarDAO = em.find(CalendarDAO.class, myCalendarJson.getCalendarId());
		for (Slot slot : slotsJson) {
			if (slot.getId() == 0) {
				SlotDAO newSlotDAO = ConvertorJsonToDao.convertSlot(slot);
				newSlotDAO.setCalendar(calendarDAO);
				em.persist(newSlotDAO);
			} else {
				SlotDAO oldSlotDAO = em.find(SlotDAO.class, slot.getId());
				oldSlotDAO.setClient(ConvertorJsonToDao.convertClient(slot.getClient()));
				oldSlotDAO.setMessageBar(slot.getMessageBar());
				oldSlotDAO.setStatus(ConvertorJsonToDao.convertStatus(slot.getStatus()));
				oldSlotDAO.setParticipants(ConvertorJsonToDao.convertParticipants(slot.getParticipants()));
			}
		}
		return true;
	}

	@Override
	@Transactional
	public MyCalendar createCollaborationCal(MyCalendar newCollaboratedCalendar) {
		List<Date> startEndDates = getStartEndDays(newCollaboratedCalendar.getWeekNumber());
		UserDAO user = em.find(UserDAO.class, newCollaboratedCalendar.getUserId());

		CalendarDAO calendarDaoCollaborated = new CalendarDAO();
		calendarDaoCollaborated.setTypeCalendar(Constants.CALENDAR_TYPE_COLLABORATED);
		calendarDaoCollaborated.setCalendarName(newCollaboratedCalendar.getCalendarName());
		calendarDaoCollaborated.setDuration(newCollaboratedCalendar.getDuration());
		calendarDaoCollaborated.setUser(user);

		Query q = em.createQuery("select slot from SlotDAO slot where slot.calendar.id = ?1 and slot.beginning > ?2")
				.setParameter(1, newCollaboratedCalendar.getCalendarId()).setParameter(2, startEndDates.get(0));

		List<SlotDAO> slotsWeekDAO = q.getResultList();
		calendarDaoCollaborated.setSlots(slotsWeekDAO);
		em.persist(calendarDaoCollaborated);
		newCollaboratedCalendar.setCalendarId(calendarDaoCollaborated.getId());
		return newCollaboratedCalendar;
	}
}
