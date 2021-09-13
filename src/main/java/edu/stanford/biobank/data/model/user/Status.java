package edu.stanford.biobank.data.model.user;

public enum Status {
	ACTIVE("Active"), LOCKED("Locked"), DELETED("Deleted");
	
	private final String name;
	
	Status (String name) {
		this.name = name;
	}
}
