package com.munioz.mark.practice.domain.ports.in;

import com.munioz.mark.practice.domain.models.Customer;

import reactor.core.publisher.Mono;

public interface CreateCustomerUseCase {
	
	public Mono<Customer> create(int dataBaseId, Customer customer);
	
}
