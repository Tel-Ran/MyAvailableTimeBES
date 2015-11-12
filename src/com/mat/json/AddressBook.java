package com.mat.json;

import java.util.*;

public class AddressBook {
	
	List<Person> persons;

	public List<Person> getPersons() {
		return persons;
	}

	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}

	@Override
	public String toString() {
		return "AddressBook [persons=" + persons + "]";
	}

}
