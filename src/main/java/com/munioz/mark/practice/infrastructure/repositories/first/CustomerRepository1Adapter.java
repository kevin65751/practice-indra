package com.munioz.mark.practice.infrastructure.repositories.first;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.munioz.mark.practice.domain.models.Customer;
import com.munioz.mark.practice.domain.ports.out.CustomerRepositoryPort;
import com.munioz.mark.practice.infrastructure.mappers.CustomerMapper;

import reactor.core.publisher.Mono;

@Component
public class CustomerRepository1Adapter implements CustomerRepositoryPort {
	
	@Autowired
	private CustomerRepository1 customerRepository1;

	@Override
	public Mono<Customer> getById(String id) {
		return customerRepository1.findById(id).map(CustomerMapper::toCustomer);
	}

	@Override
	public Mono<Customer> save(Customer customer) {
		return customerRepository1.save(CustomerMapper.toCustomerEntity(customer)).map(CustomerMapper::toCustomer);
	}

	@Override
	public Mono<Customer> update(Customer customer) {
		return customerRepository1.save(CustomerMapper.toCustomerEntity(customer)).map(CustomerMapper::toCustomer);
	}

	@Override
	public Mono<Void> deleteById(String id) {
		return customerRepository1.deleteById(id);
	}
	
}
