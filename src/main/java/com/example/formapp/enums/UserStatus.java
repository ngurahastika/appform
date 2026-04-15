package com.example.formapp.enums;

public enum UserStatus {
	ACTIVE("Active"), INACTIVE("Inactive"), LOCKED("Locked"), BLOCKED("Blocked");

	private final String label;

	UserStatus(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
	
	

}
