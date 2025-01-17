package com.munioz.mark.practice.application.services;

import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.munioz.mark.practice.application.dto.CreateCustomerDto;
import com.munioz.mark.practice.application.dto.DeleteCustomerDto;
import com.munioz.mark.practice.application.dto.UpdateCustomerDto;
import com.munioz.mark.practice.domain.models.Customer;
import com.munioz.mark.practice.domain.ports.in.CreateCustomerUseCase;
import com.munioz.mark.practice.domain.ports.in.DeleteCustomerUseCase;
import com.munioz.mark.practice.domain.ports.in.GetCustomerUseCase;
import com.munioz.mark.practice.domain.ports.in.UpdateCustomerUseCase;

import reactor.core.publisher.Mono;

@Service
public class CustomerService {
	@Autowired
	private CreateCustomerUseCase createCustomerUseCase;
	
	@Autowired
	private GetCustomerUseCase getCustomerUseCase;
	
	@Autowired
	private UpdateCustomerUseCase updateCustomerUseCase;
	
	@Autowired
	private DeleteCustomerUseCase deleteCustomerUseCase;
	
	@Autowired
	private KieSession kieSession;

	
	public Mono<Customer> create(CreateCustomerDto createCustomerDto) {
		kieSession.insert(createCustomerDto);
		kieSession.fireAllRules();
		
		return createCustomerUseCase.create(createCustomerDto.getRepositoryId(), Customer.builder()
				.name(createCustomerDto.getName())
				.email(createCustomerDto.getEmail())
				.vip(createCustomerDto.getVip())
				.build());
	}
	
	public Mono<Customer> update(UpdateCustomerDto updateCustomerDto) {
		kieSession.insert(updateCustomerDto);
		kieSession.fireAllRules();
		
		Customer customer = getById(updateCustomerDto.getId()).block();
		customer.setName(updateCustomerDto.getName());
		customer.setEmail(updateCustomerDto.getEmail());
		
		return updateCustomerUseCase.update(updateCustomerDto.getRepositoryId(), customer);
	}

	public Mono<Customer> getById(String id) {
		return getCustomerUseCase.getById(id);
	}
	
	public Mono<Void> deleteById(DeleteCustomerDto deleteCustomerDto) {
		kieSession.insert(deleteCustomerDto);
		kieSession.fireAllRules();
		
		if(deleteCustomerDto.getRepositoryId() == null) return Mono.error(new RuntimeException("No se encontró repositorio para el id: " + deleteCustomerDto.getId()));
		return deleteCustomerUseCase.deleteById(deleteCustomerDto.getRepositoryId(), deleteCustomerDto.getId());
	}
	
}
