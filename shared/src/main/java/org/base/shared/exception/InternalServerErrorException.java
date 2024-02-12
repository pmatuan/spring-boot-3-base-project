package org.base.shared.exception;

public class InternalServerErrorException extends RuntimeException {

	public InternalServerErrorException() {
		super("ERROR: Internal Server Error!");
	}

}
