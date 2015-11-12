package com.mat.dao;

import java.util.*;

import javax.persistence.*;


@Entity
public class SlotDAO {

	@Id
	@GeneratedValue
	int id;
	
	@Temporal(TemporalType.TIMESTAMP)
	Date beginning;

	@Embedded
	StatusDAO status;

	@ManyToMany
	List<PersonDAO> participants;

	@ManyToOne
	PersonDAO client;
	String messageBar;

	@ManyToOne
	CalendarDAO calendar;

	public SlotDAO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public StatusDAO getStatus() {
		return status;
	}

	public void setStatus(StatusDAO status) {
		this.status = status;
	}

	public List<PersonDAO> getParticipants() {
		return participants;
	}

	public void setParticipants(List<PersonDAO> participants) {
		this.participants = participants;
	}

	public PersonDAO getClient() {
		return client;
	}

	public void setClient(PersonDAO client) {
		this.client = client;
	}

	public String getMessageBar() {
		return messageBar;
	}

	public void setMessageBar(String messageBar) {
		this.messageBar = messageBar;
	}

	public CalendarDAO getCalendar() {
		return calendar;
	}

	public void setCalendar(CalendarDAO calendar) {
		this.calendar = calendar;
	}

	public Date getBeginning() {
		return beginning;
	}

	public void setBeginning(Date beginning) {
		this.beginning = beginning;
	}
}
