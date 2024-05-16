package com.munioz.mark.practice.application.usecases;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.munioz.mark.practice.domain.ports.in.DeleteCustomerUseCase;
import com.munioz.mark.practice.domain.ports.out.CustomerRepositoryFactoryPort;
import com.munioz.mark.practice.domain.ports.out.CustomerRepositoryPort;

import reactor.core.publisher.Mono;

@Component
public class DeleteCustomerUseCaseImpl implements DeleteCustomerUseCase {
	private static final Logger log = LoggerFactory.getLogger(DeleteCustomerUseCaseImpl.class);

	@Autowired
	private CustomerRepositoryFactoryPort customerRepositoryFactoryPort;
	
	@Override
	public Mono<Void> deleteById(int dataSourceId, String id) {
		CustomerRepositoryPort customerRepository = customerRepositoryFactoryPort
				.getCustomerRepositoryPort(dataSourceId);
		
		log.info("Deleting customer ({}): {}", dataSourceId, id);
		return customerRepository.deleteById(id);
	}
	
}
