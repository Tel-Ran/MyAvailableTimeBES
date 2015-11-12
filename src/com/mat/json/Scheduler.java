package com.mat.json;

public class Scheduler {
	String schedulerName;
	String accountName;

	public String getSchedulerName() {
		return schedulerName;
	}

	public void setSchedulerName(String schedulerName) {
		this.schedulerName = schedulerName;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	@Override
	public String toString() {
		return "Scheduler [schedulerName=" + schedulerName + ", accountName=" + accountName + "]";
	}

}
