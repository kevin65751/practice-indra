package com.munioz.mark.practice.application.usecases;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.munioz.mark.practice.domain.exceptions.CustomerNotFoundException;
import com.munioz.mark.practice.domain.models.Customer;
import com.munioz.mark.practice.domain.ports.in.GetCustomerUseCase;
import com.munioz.mark.practice.domain.ports.out.CustomerRepositoryFactoryPort;

import reactor.core.publisher.Mono;

@Component
public class GetCustomerUseCaseImpl implements GetCustomerUseCase {

	@Autowired
	private CustomerRepositoryFactoryPort customerRepositoryFactoryPort;
	
	@Override
	public Mono<Customer> getById(String id) {
		return Mono.firstWithValue(
				customerRepositoryFactoryPort
				.getCustomerRepositoryPort(CustomerRepositoryFactoryPort.DATA_SOURCE_1)
				.getById(id), 
				customerRepositoryFactoryPort
				.getCustomerRepositoryPort(CustomerRepositoryFactoryPort.DATA_SOURCE_2)
				.getById(id)).onErrorMap(NoSuchElementException.class, (e) -> new CustomerNotFoundException(id));
	}
	
}
