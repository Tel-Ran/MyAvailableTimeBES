package com.mat.model;

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import com.mat.dao.*;
import com.mat.interfaces.Constants;
import com.mat.interfaces.IMatRepository;
import com.mat.json.*;

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
	public boolean repeatCalendar(int calendarId, Date date) {
		CalendarDAO calendar = em.find(CalendarDAO.class, calendarId);
		Iterable<SlotDAO> slots = calendar.getSlots();

		Calendar javaCal = new GregorianCalendar();
		javaCal.setFirstDayOfWeek(Calendar.MONDAY);

		for (SlotDAO slot : slots) {
			javaCal.setTime(slot.getBeginning());
			javaCal.add(Calendar.DATE, 7);
			if (javaCal.getTime().compareTo(date) < 0) {
				SlotDAO newSlot = new SlotDAO();
				newSlot.setBeginning(javaCal.getTime());
				newSlot.setCalendar(slot.getCalendar());
				newSlot.setClient(slot.getClient());
				newSlot.setMessageBar(slot.getMessageBar());
				StatusDAO newStatus = new StatusDAO();
				newStatus.setConfirmation(0);
				newStatus.setStatusName(Constants.SLOT_STATUS_FREE);
				newSlot.setStatus(newStatus);
				em.persist(newSlot);
			}
		}
		return true;
	}
}
