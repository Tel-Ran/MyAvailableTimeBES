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
import com.mat.utils.DateUtil;

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
	public boolean removeCalendar(int id){
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

	@Override
	@Transactional
	public MyCalendar getWeek(int calendarId, int weekNumber) {
		List<Date> startEndDates = getStartEndDays(weekNumber);
		MyCalendar myCalendar = getMyCalendar(calendarId);
		List<Slot> slots = myCalendar.getSlots();
		List<Slot> slotsWeek = new LinkedList<Slot>();
		for (Slot slot : slots) {
			if (slot.getBeginning().after(startEndDates.get(0)) && slot.getBeginning().before(startEndDates.get(1))) {
				slotsWeek.add(slot);
			}
		}
		myCalendar.setSlots(slotsWeek);
		myCalendar.setWeekNumber(weekNumber);
		return myCalendar;
	}
	
	private MyCalendar getMyCalendar(int calendarId) {
		CalendarDAO calendarDao = em.find(CalendarDAO.class, calendarId);
		return ConvertorDaoToJson.convertCalendar(calendarDao);
	}

	private List<Date> getStartEndDays(int weekNumber) {
		int coef = 7 * weekNumber;
		List<Date> dates = new LinkedList<Date>();
		Calendar calendar = new GregorianCalendar();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		int difference = calendar.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
		if (difference < 0) {
			calendar.add(Calendar.DAY_OF_WEEK, -7 - difference + coef);
		} else {
			calendar.add(Calendar.DAY_OF_WEEK, -difference + coef);
		}
		dates.add(calendar.getTime());
		calendar.add(Calendar.DAY_OF_WEEK, 7);
		dates.add(calendar.getTime());
		return dates;
	}

	@Override
	@Transactional
	public boolean editCalendar(MyCalendar myCalendar) {
		int myCalendarId = myCalendar.getCalendarId();
		CalendarDAO calendarDao = em.find(CalendarDAO.class, myCalendarId);
		List<Slot> weekSlots = myCalendar.getSlots();
		List<SlotDAO> slotsDao = calendarDao.getSlots();
		for (Slot changedSlot : weekSlots) {
			if (!isTheSameDateSlot(slotsDao, changedSlot)) {
				SlotDAO sl = ConvertorJsonToDao.convertSlot(changedSlot);
				sl.setCalendar(calendarDao);
				em.persist(sl);
			}
		}
		return true;
	}

	protected boolean isTheSameDateSlot(List<SlotDAO> slotsDao, Slot changedSlot) {
		for (SlotDAO slotDao : slotsDao) {
			if (DateUtil.idTheSameDate(changedSlot.getBeginning(), slotDao.getBeginning())) {
				slotDao.setMessageBar(changedSlot.getMessageBar());
				slotDao.setStatus(ConvertorJsonToDao.convertStatus(changedSlot.getStatus()));
				slotDao.setClient(ConvertorJsonToDao.convertClient(changedSlot.getClient()));
				slotDao.setParticipants(ConvertorJsonToDao.convertParticipants(changedSlot.getParticipants()));
				return true;
			}
		}
		return false;
	}
}
