package com.mat.dao;

import java.io.Serializable;

import javax.persistence.*;
import java.util.*;

@SuppressWarnings("serial")
@Entity
public class PersonDAO implements Serializable {

	@Id
	@GeneratedValue
	int id;
	
	//if person was created by user in Address book, next three fields are used 
	String email;
	String name;
	String lastName;
	//if person was fetched from the social network, next two fields are used
	String socialNetwork;
	String nickNameNetwork;

	@ManyToOne
	UserDAO user;
	
	@ManyToMany(mappedBy="participants")
	List<SlotDAO> slots;
	
	@OneToMany(mappedBy="client")
	List<SlotDAO> slotsShared;
	
	
	public PersonDAO() {
		super();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	public List<SlotDAO> getSlots() {
		return slots;
	}

	public void setSlots(List<SlotDAO> slots) {
		this.slots = slots;
	}

	public List<SlotDAO> getSlotsShared() {
		return slotsShared;
	}

	public void setSlotsShared(List<SlotDAO> slotsShared) {
		this.slotsShared = slotsShared;
	}

	public String getSocialNetwork() {
		return socialNetwork;
	}

	public void setSocialNetwork(String socialNetwork) {
		this.socialNetwork = socialNetwork;
	}

	public String getNickNameNetwork() {
		return nickNameNetwork;
	}

	public void setNickNameNetwork(String nickNameNetwork) {
		this.nickNameNetwork = nickNameNetwork;
	}
}
