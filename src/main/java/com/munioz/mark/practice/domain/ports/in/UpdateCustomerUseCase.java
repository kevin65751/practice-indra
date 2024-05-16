package com.munioz.mark.practice.domain.ports.in;

import com.munioz.mark.practice.domain.models.Customer;

import reactor.core.publisher.Mono;

public interface UpdateCustomerUseCase {
	
	public Mono<Customer> update(int dataBaseId, Customer customer);
	
}
