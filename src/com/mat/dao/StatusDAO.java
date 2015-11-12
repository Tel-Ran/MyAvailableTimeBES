package com.mat.dao;

import java.io.Serializable;

import javax.persistence.*;

@SuppressWarnings("serial")
@Embeddable
public class StatusDAO implements Serializable {

	String statusName; // status identification (collaborated/shared/available)
	int confirmation; // status progress designation

	public StatusDAO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public int getConfirmation() {
		return confirmation;
	}

	public void setConfirmation(int confirmation) {
		this.confirmation = confirmation;
	}

}
