package com.mat.json;

public class Contact {
	String email;
	String displayedName;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDisplayedName() {
		return displayedName;
	}

	public void setDisplayedName(String displayedName) {
		this.displayedName = displayedName;
	}

	@Override
	public String toString() {
		return "Contact [email=" + email + ", displayedName=" + displayedName + "]";
	}

}
