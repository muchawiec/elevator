package com.iddqd.elevator.api.exceptions;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(BAD_REQUEST)
public class WrongElevatorException extends RuntimeException {

	public WrongElevatorException(String message) {
		super(message);
	}
}
