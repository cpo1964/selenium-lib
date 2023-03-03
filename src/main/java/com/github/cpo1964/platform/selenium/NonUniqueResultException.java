package com.github.cpo1964.platform.selenium;

public class NonUniqueResultException extends RuntimeException {
	private static final long serialVersionUID = -1757423191400510323L;

	public NonUniqueResultException() {
		super("Only one result is allowed for fetchOne calls");
	}

	public NonUniqueResultException(String message) {
		super(message);
	}

	public NonUniqueResultException(Exception e) {
		super(e);
	}
}
