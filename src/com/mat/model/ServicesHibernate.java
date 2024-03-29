package com.mat.model;

import java.util.*;

import javax.persistence.*;

import org.springframework.transaction.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.*;
import com.mat.dao.*;
import com.mat.interfaces.*;
import com.mat.json.*;
import com.mat.mailsender.MatSender;
import com.mat.mediator.*;

public class ServicesHibernate implements IMatRepository {

	@PersistenceContext(unitName = "springHibernate")
	public EntityManager em;

	public MatSender matSender;
	
	public void setMatSender(MatSender matSender) {
		this.matSender = matSender;
	}

	/**
	 * Registration of new user.
	 */
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

	/**
	 * Login of the user. If there is no such a user in the Database, returns null.
	 */
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

	/**
	 * Returns User with all the calendars he has.
	 */
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

	/**
	 * Adding the person to the Address Book of the user.
	 */
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

	/**
	 * Return the Address Book of the user.
	 */
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

	/**
	 * Creates new calendar. 
	 */
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

	/**
	 * removes all slots in specified calendar from DB, then removes calendar
	 * from DB.
	 */
	@Override
	@Transactional
	public boolean removeCalendar(int id) {
		CalendarDAO cal = em.find(CalendarDAO.class, id);
		if (cal != null) {
			em.remove(cal);
			return true;
		}
		return false;
	}

	/**
	 * Gets current week of the calendar from Database
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public MyCalendar getWeek(int calendarId, int weekNumber) {
		List<Date> startEndDates = getStartEndDays(weekNumber);

		Query q = em.createQuery("select slot from SlotDAO slot where slot.calendar.id = ?1 and slot.beginning > ?2 and slot.beginning < ?3")
				.setParameter(1, calendarId)
				.setParameter(2, startEndDates.get(0))
				.setParameter(3, startEndDates.get(1));

		List<SlotDAO> slotsWeekDAO = q.getResultList();
		List<Slot> slotsWeekJson = ConvertorDaoToJson.getSlots(slotsWeekDAO);
		CalendarDAO calendarDAO = em.find(CalendarDAO.class, calendarId);// calendar
		MyCalendar myCalendar = new MyCalendar();// new calendar creating

		myCalendar.setCalendarId(calendarId);
		myCalendar.setCalendarName(calendarDAO.getCalendarName());
		myCalendar.setDuration(calendarDAO.getDuration());
		myCalendar.setSlots(slotsWeekJson);
		myCalendar.setWeekNumber(weekNumber);
		return myCalendar;
	}

	/**
	 * this is the service method definition of the first and last days in a week.
	 * @param weekNumber
	 * @return
	 */
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

	// This method changes/creates only slots are in a common calendar, neither
	// in a calendar for collaboration.
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
				oldSlotDAO.setMessageBar(slot.getMessageBar());
				oldSlotDAO.setStatus(ConvertorJsonToDao.convertStatus(slot.getStatus()));
			}
		}
		return true;
	}

	/**
	 * creates new collaboration calendar based on the calendar from Database
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public MyCalendar createCollaborationCal(MyCalendar newCollaboratedCalendar) {

		List<Date> startEndDates = getStartEndDays(newCollaboratedCalendar.getWeekNumber());
		CalendarDAO cal = em.find(CalendarDAO.class, newCollaboratedCalendar.getCalendarId());
		UserDAO user = cal.getUser();

		CalendarDAO calendarDaoCollaborated = new CalendarDAO();
		calendarDaoCollaborated.setTypeCalendar(Constants.CALENDAR_TYPE_COLLABORATED);
		calendarDaoCollaborated.setCalendarName(cal.getCalendarName() + " (Collaborated)");
		calendarDaoCollaborated.setDuration(cal.getDuration());
		calendarDaoCollaborated.setUser(user);

		Query q1 = em.createQuery("select slot from SlotDAO slot where slot.calendar.id = ?1 and slot.beginning > ?2")
				.setParameter(1, newCollaboratedCalendar.getCalendarId()).setParameter(2, startEndDates.get(0));

		List<SlotDAO> slotsDAO = q1.getResultList();
		calendarDaoCollaborated.setSlots(slotsDAO);
		em.persist(calendarDaoCollaborated);
		// forms response to FES
		Query q2 = em
				.createQuery(
						"select slot from SlotDAO slot where slot.calendar.id = ?1 and slot.beginning > ?2 and slot.beginning < ?3")
				.setParameter(1, newCollaboratedCalendar.getCalendarId()).setParameter(2, startEndDates.get(0))
				.setParameter(3, startEndDates.get(1));
		List<SlotDAO> slotsWeekDAO = q2.getResultList();
		newCollaboratedCalendar.setCalendarId(calendarDaoCollaborated.getId());
		newCollaboratedCalendar.setCalendarName(calendarDaoCollaborated.getCalendarName());
		newCollaboratedCalendar.setDuration(calendarDaoCollaborated.getDuration());
		newCollaboratedCalendar.setSlots(ConvertorDaoToJson.getSlots(slotsWeekDAO));
		return newCollaboratedCalendar;
	}

	/**
	 * changes settings of the user
	 */
	@Override
	@Transactional
	public boolean changeUserData(User user) {
		if (user != null) {
			UserDAO userDao = em.find(UserDAO.class, user.getUserId());
			userDao.setFirstName(user.getFirstName());
			userDao.setLastName(user.getLastName());
			userDao.setEmail(user.getEmail());
			userDao.setTimeZone(user.getTimeZone());
			userDao.setFormat24(user.isFormat24());
			userDao.setPhoneNumber(user.getPhoneNumber());
			return true;
		}
		return false;
	}

	/**
	 * sets the client from Address Book to the slot (sharing) 
	 */
	@Override
	@Transactional
	public boolean setClientToSlot(Slot slot) {
		SlotDAO slotDAO = em.find(SlotDAO.class, slot.getId());
		PersonDAO clientDAO = em.find(PersonDAO.class, slot.getClient().getId());
		if (clientDAO == null)
			return false;
		slotDAO.setClient(clientDAO);
		return true;
	}

	/**
	 * removes client from the slot
	 */
	@Override
	@Transactional
	public boolean removeClientFromSlot(int slotId) {
		SlotDAO slotDAO = em.find(SlotDAO.class, slotId);
		PersonDAO client = slotDAO.getClient();
		if (client == null)
			return false;
		em.remove(client);
		return true;
	}

	/**
	 * repeats calendar's slots of CURRENT WEEK until specified date
	 */
	@Override
	@Transactional
	public boolean repeatCalendar(MyCalendar calendar, Date date) {
		CalendarDAO calDao = em.find(CalendarDAO.class, calendar.getCalendarId());
		if (calDao != null) {
			Calendar javaCalendar = new GregorianCalendar();
			Calendar firstDayOfWeek = new GregorianCalendar();
			Calendar lastDayOfWeek = new GregorianCalendar();
			javaCalendar.setFirstDayOfWeek(Calendar.MONDAY);
			firstDayOfWeek.setFirstDayOfWeek(Calendar.MONDAY);
			lastDayOfWeek.setFirstDayOfWeek(Calendar.MONDAY);
			firstDayOfWeek.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			lastDayOfWeek.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			Date weekBegin = new Date(firstDayOfWeek.getTimeInMillis());
			Date weekEnd = new Date(lastDayOfWeek.getTimeInMillis());
			List<SlotDAO> slots = calDao.getSlots();
			List<SlotDAO> slotsOfCurrentWeek = new LinkedList<SlotDAO>();
			for (SlotDAO slot : slots) {
				if (slot.getBeginning().compareTo(weekEnd) > 0) {
					em.remove(slot);
				}
				if (slot.getBeginning().compareTo(weekBegin) >= 0 && slot.getBeginning().compareTo(weekEnd) <= 0) {
					slotsOfCurrentWeek.add(slot);
				}
			}
			for (SlotDAO slot : slotsOfCurrentWeek) {
				javaCalendar.setTime(slot.getBeginning());
				javaCalendar.add(Calendar.DATE, 7);
				while (javaCalendar.getTime().compareTo(date) < 0) {
					SlotDAO newSlot = new SlotDAO();
					newSlot.setBeginning(javaCalendar.getTime());
					newSlot.setCalendar(calDao);
					newSlot.setClient(slot.getClient());
					newSlot.setMessageBar(slot.getMessageBar());
					StatusDAO newStatus = new StatusDAO();
					newStatus.setConfirmation(0);
					newStatus.setStatusName(Constants.SLOT_STATUS_FREE);
					newSlot.setStatus(newStatus);
					em.persist(newSlot);
					javaCalendar.add(Calendar.DATE, 7);
				}
			}
		}
		return false;
	}

	/**
	 * removes the person from the Address Book
	 */
	@Override
	@Transactional
	public boolean removePerson(int id) {
		PersonDAO personDao = em.find(PersonDAO.class, id);
		List<SlotDAO> charedSlots = personDao.getSlotsShared();
		List<SlotDAO> collaboratedSlots = personDao.getSlots();
		if (charedSlots.size() == 0 && collaboratedSlots.size() == 0) {
			em.remove(personDao);
			return true;
		}
		return false;
	}

	/**
	 * returns the list of schedulers of the user
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Scheduler> getSchedulers(int userId) {
		Query query = em.createQuery("select sch from SchedulerDAO sch where sch.user.id = ?1").setParameter(1, userId);
		List<SchedulerDAO> shedullerDao = query.getResultList();
		List<Scheduler> scheduler = ConvertorDaoToJson.getScheduller(shedullerDao);
		return scheduler;
	}
	
	/**
	 * method adds new entry to the schedulers list of the user 
	 */
	@Override
	@Transactional
	public boolean addScheduler(Scheduler entry){
		int userId = entry.getUserId();
		UserDAO user = em.find(UserDAO.class, userId);
		if(user == null)
			return false;
		
		SchedulerDAO entryDAO = new SchedulerDAO();
		entryDAO.setAccountName(entry.getAccountName());
		entryDAO.setSchedulerName(entry.getSchedulerName());
		entryDAO.setUser(user);
		return true;
	}
	
	/**
	 * method edits current scheduler
	 */
	@Override
	@Transactional
	public boolean editScheduler(Scheduler scheduler){
		SchedulerDAO entity = em.find(SchedulerDAO.class, scheduler.getId());
		if(entity == null)
			return false;
		entity.setAccountName(scheduler.getAccountName());
		entity.setSchedulerName(scheduler.getSchedulerName());
		return true;
	}
	
	@Override
	@Transactional
	public boolean removeScheduler(int id){
		SchedulerDAO entity = em.find(SchedulerDAO.class, id);
		if(entity == null)
			return false;
		em.remove(entity);
		return true;
	}
	
	/**
	 * method adds the persons obtained from a Database
	 */
	@Override
	@Transactional
	public boolean addImportedPersons(AddressBook book) {
		List<Person> persons = book.getPersons();
		if (persons != null) {
			for (Person p : persons) {
				addPersonToAddressBook(p);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Adds all participants to all slots in LinkedHashMap to Calendar with ID, specified as URL parameter @see src/com/mat/controller/MatBesController.java
	 * @param int calendarId
	 * @param LinkedHashMap<String, List>
	 * This Map contains one list of <json.Slot> slots and one list of <json.Person> persons
	 * stored in Map with constants MAP_SLOT_LIST = "slots" and MAP_PERSON_LIST = "persons" respectively 
	 */
	@Override
	@Transactional
	public boolean setParticipantsToSlots(int calendarId, LinkedHashMap<String, List<?>> slotsAndParticipants) {
		ObjectMapper om = new ObjectMapper();
		List<Person> personListJson = new LinkedList<>();
		List<Slot> slotListJson = new LinkedList<>();
		// Check the type of Calendar in DB (whether it is available for Collaboration)
		if (em.find(CalendarDAO.class, calendarId).getTypeCalendar() != Constants.CALENDAR_TYPE_COLLABORATED)
			return false;
		
		for (Object pers : slotsAndParticipants.get(Constants.MAP_PERSON_LIST)) {
			Person person = om.convertValue(pers, Person.class);
			personListJson.add(person);
		}
		for (Object slott : slotsAndParticipants.get(Constants.MAP_SLOT_LIST)) {
			Slot slot = om. convertValue(slott, Slot.class);
			slotListJson.add(slot);
		}
		List<PersonDAO> personListDao = new LinkedList<>();
		for (Person person : personListJson) {
			PersonDAO personDao = em.find(PersonDAO.class, person.getId());
			personListDao.add(personDao);
		}
		for (Slot slot : slotListJson) {
			SlotDAO slotDao = em.find(SlotDAO.class, slot.getId()); 
			if (slotDao != null) {
				slotDao.setParticipants(personListDao);
			}
			else {
				slotDao = ConvertorJsonToDao.convertSlot(slot);
				CalendarDAO cal = em.find(CalendarDAO.class, calendarId);
				slotDao.setCalendar(cal);
				slotDao.setParticipants(personListDao);
				em.persist(slotDao);
			}
		}
		return true;
	}
	
	@Override
	@Transactional
	public boolean activateUser(int userId) {
		UserDAO userDao = em.find(UserDAO.class, userId);
		if (userDao != null && userDao.isActivated() == Constants.USER_STATUS_NOT_ACTIVATED) {
			String hashCode = String.valueOf(userDao.getEmail().hashCode()) + String.valueOf(userDao.getFirstName().hashCode()) + String.valueOf(userDao.getLastName().hashCode());
			userDao.setHashForActivation(hashCode);
			//if (matSender.sendMail(userDao, hashCode))
			/*MatBesController.*/matSender.sendMail(userDao, hashCode);
				return true;
			//return false;
		}
		return false;
	}
	
}