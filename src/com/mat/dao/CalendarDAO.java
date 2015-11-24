package com.mat.dao;

import java.util.*;

import javax.persistence.*;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
public class CalendarDAO {

	@Id
	@GeneratedValue
	int id;

	@ManyToOne
	UserDAO user;

	String calendarName;

	int typeCalendar;

	int duration;

	@OneToMany(mappedBy = "calendar")
	@Cascade(value = CascadeType.DELETE)
	List<SlotDAO> slots;

	public CalendarDAO() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public UserDAO getUser() {
		return user;
	}

	public void setUser(UserDAO user) {
		this.user = user;
	}

	public String getCalendarName() {
		return calendarName;
	}

	public void setCalendarName(String calendarName) {
		this.calendarName = calendarName;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public List<SlotDAO> getSlots() {
		return slots;
	}

	public void setSlots(List<SlotDAO> slots) {
		this.slots = slots;
	}

	public int getTypeCalendar() {
		return typeCalendar;
	}

	public void setTypeCalendar(int typeCalendar) {
		this.typeCalendar = typeCalendar;
	}

}
