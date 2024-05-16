package com.munioz.mark.practice.application.usecases;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.munioz.mark.practice.domain.models.Customer;
import com.munioz.mark.practice.domain.ports.in.UpdateCustomerUseCase;
import com.munioz.mark.practice.domain.ports.out.CustomerRepositoryFactoryPort;
import com.munioz.mark.practice.domain.ports.out.CustomerRepositoryPort;

import reactor.core.publisher.Mono;

@Component
public class UpdateCustomerUseCaseImpl implements UpdateCustomerUseCase {
	private static final Logger log = LoggerFactory.getLogger(UpdateCustomerUseCaseImpl.class);

	@Autowired
	private CustomerRepositoryFactoryPort customerRepositoryFactoryPort;
	
	@Override
	public Mono<Customer> update(int dataSourceId, Customer customer) {
		CustomerRepositoryPort customerRepository = customerRepositoryFactoryPort
				.getCustomerRepositoryPort(dataSourceId);
		
		log.info("Modify customer ({}): {}", customerRepository.getClass().getName(), customer);
		return customerRepository.save(customer);
	}
	
}
