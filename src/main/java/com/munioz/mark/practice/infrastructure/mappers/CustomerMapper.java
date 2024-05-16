package com.munioz.mark.practice.infrastructure.mappers;

import com.munioz.mark.practice.domain.models.Customer;
import com.munioz.mark.practice.infrastructure.entities.CustomerEntity;

public class CustomerMapper {
	
	public static Customer toCustomer(CustomerEntity customerEntity) {
		return Customer.builder()
				.id(customerEntity.getId())
				.name(customerEntity.getName())
				.email(customerEntity.getEmail())
				.vip(customerEntity.getVip())
				.created(customerEntity.getCreated())
				.modified(customerEntity.getModified())
				.build();
	}
	
	public static CustomerEntity toCustomerEntity(Customer customer) {
		return CustomerEntity.builder()
				.id(customer.getId())
				.name(customer.getName())
				.email(customer.getEmail())
				.vip(customer.getVip())
				.created(customer.getCreated())
				.modified(customer.getModified())
				.build();
	}
}
