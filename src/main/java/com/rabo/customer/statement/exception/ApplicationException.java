package com.rabo.customer.statement.exception;

public class ApplicationException extends RuntimeException {

	private static final long serialVersionUID = 3473566897850324182L;

	public ApplicationException(String message) {
		super(message);
	}
}
