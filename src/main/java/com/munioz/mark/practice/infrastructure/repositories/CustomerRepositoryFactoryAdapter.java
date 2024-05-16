package com.munioz.mark.practice.infrastructure.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.munioz.mark.practice.domain.ports.out.CustomerRepositoryFactoryPort;
import com.munioz.mark.practice.domain.ports.out.CustomerRepositoryPort;
import com.munioz.mark.practice.infrastructure.repositories.first.CustomerRepository1Adapter;
import com.munioz.mark.practice.infrastructure.repositories.second.CustomerRepository2Adapter;

@Component
public class CustomerRepositoryFactoryAdapter implements CustomerRepositoryFactoryPort {

	@Autowired
	private CustomerRepository1Adapter customerRepository1Adapter;
	
	@Autowired
	private CustomerRepository2Adapter customerRepository2Adapter;
	
	
	@Override
	public CustomerRepositoryPort getCustomerRepositoryPort(int dataSourceId) {
		switch (dataSourceId) {
			case DATA_SOURCE_2: 
				return customerRepository2Adapter;
			case DATA_SOURCE_1: 
			default:
				return customerRepository1Adapter; 
		}
	}
	
}
