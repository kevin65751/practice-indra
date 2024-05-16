package com.munioz.mark.practice.application.usecases;

import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.munioz.mark.practice.domain.models.Customer;
import com.munioz.mark.practice.domain.ports.in.CreateCustomerUseCase;
import com.munioz.mark.practice.domain.ports.out.CustomerRepositoryFactoryPort;
import com.munioz.mark.practice.domain.ports.out.CustomerRepositoryPort;

import reactor.core.publisher.Mono;

@Component
public class CreateCustomerUseCaseImpl implements CreateCustomerUseCase {
	private static final Logger log = LoggerFactory.getLogger(CreateCustomerUseCaseImpl.class);
	
	@Autowired
	private CustomerRepositoryFactoryPort customerRepositoryFactoryPort;
	
	@Override
	public Mono<Customer> create(int dataBaseId, Customer customer) {
		CustomerRepositoryPort customerRepository = customerRepositoryFactoryPort
				.getCustomerRepositoryPort(dataBaseId);
		
		customer.setId(String.format("%s-%s", UUID.randomUUID().toString(), dataBaseId));
		customer.setCreated(new Date());
		customer.setModified(new Date());
		
		log.info("Creating costumer ({}) : {}", customerRepository.getClass().getName(), customer);
		return customerRepository.save(customer);
	}
	
}
