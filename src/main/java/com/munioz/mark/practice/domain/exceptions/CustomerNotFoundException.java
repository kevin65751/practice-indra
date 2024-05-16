package com.munioz.mark.practice.domain.exceptions;

public class CustomerNotFoundException extends DomainException {
	private static final long serialVersionUID = 1L;

	public CustomerNotFoundException(String customerId) {
		super(String.format("Not found customer with id: %s", customerId));
	}
}
