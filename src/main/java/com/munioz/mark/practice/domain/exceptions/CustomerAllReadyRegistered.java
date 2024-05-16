package com.munioz.mark.practice.domain.exceptions;

public class CustomerAllReadyRegistered extends DomainException {
	private static final long serialVersionUID = 1L;

	public CustomerAllReadyRegistered(String id) {
		super(String.format("All ready exist customer with id: {}", id));
	}
}
