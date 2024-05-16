package com.munioz.mark.practice.domain.ports.out;

import com.munioz.mark.practice.domain.models.Customer;

import reactor.core.publisher.Mono;

public interface CustomerRepositoryPort {

	public Mono<Customer> getById(String id);
	public Mono<Customer> save(Customer customer);
	public Mono<Customer> update(Customer customer);
	public Mono<Void> deleteById(String id);
	
}
