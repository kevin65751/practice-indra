package com.munioz.mark.practice.service;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.munioz.mark.practice.application.dto.CreateCustomerDto;
import com.munioz.mark.practice.application.dto.DeleteCustomerDto;
import com.munioz.mark.practice.application.dto.UpdateCustomerDto;
import com.munioz.mark.practice.application.services.CustomerService;
import com.munioz.mark.practice.domain.models.Customer;
import com.munioz.mark.practice.infrastructure.repositories.first.CustomerRepository1;

@SpringBootTest
public class CustomerServiceTest {
	private static final Logger log = LoggerFactory.getLogger(CustomerServiceTest.class);

	@Mock
	private CustomerRepository1 customerDatasource1Repository;
	
	@Mock
	private CustomerRepository1 customerDatasource2Repository;
	
	@Autowired
	private CustomerService customerService;
	
	@Test
	public void createNormalCustomerTest() {
		createNormalCustomer();
	}
	
	@Test
	public void createVipCustomerTest() {
		createVipCustomer();
	}
	
	@Test
	public void modifyNormalCustomerTest() {
		Customer customer = createNormalCustomer();
		
		UpdateCustomerDto modifyCustomerDto = UpdateCustomerDto.builder()
				.id(customer.getId())
				.name(customer.getName() + "-modified")
				.email(customer.getEmail() + "-modified")
				.build();
		
		Customer modifiedCustomer = customerService.update(modifyCustomerDto).block();
		assertNotNull(modifiedCustomer);
		assertNotEquals(customer.getName(), modifiedCustomer.getName());
		assertNotEquals(customer.getEmail(), modifiedCustomer.getEmail());
	}
	
	@Test
	public void modifyVipCustomerTest() {
		Customer customer = createVipCustomer();
		
		UpdateCustomerDto modifyCustomerDto = UpdateCustomerDto.builder()
				.id(customer.getId())
				.name(customer.getName() + "-modified")
				.email(customer.getEmail() + "-modified")
				.build();
		
		Customer modifiedCustomer = customerService.update(modifyCustomerDto).block();
		assertNotNull(modifiedCustomer);
		assertNotEquals(customer.getName(), modifiedCustomer.getName());
		assertNotEquals(customer.getEmail(), modifiedCustomer.getEmail());
	}
	
	@Test
	public void deleteNormalCustomerTest() {
		Customer customer = createNormalCustomer();
		customerService.deleteById(DeleteCustomerDto.builder()
				.id(customer.getId())
				.build()).block();
		assertNull(customerService.getById(customer.getId()).block());
	}
	
	@Test
	public void deleteVipCustomerTest() {
		Customer customer = createVipCustomer();
		customerService.deleteById(DeleteCustomerDto.builder()
				.id(customer.getId())
				.build()).block();
		assertNull(customerService.getById(customer.getId()).block());
	}
	

	private Customer createNormalCustomer() {
		CreateCustomerDto createCustomerDto = CreateCustomerDto.builder()
				.name("Normal Customer")
				.email("mmunoz@example.com")
				.vip(false)
				.build();
		
		log.info("Creating Normal Customer: {}", createCustomerDto);
		Customer customer = customerService.create(createCustomerDto).block();
		assertNotNull(customer);
		return customer;
	}

	public Customer createVipCustomer() {
		CreateCustomerDto createCustomerDto = CreateCustomerDto.builder()
				.name("VIP Customer")
				.email("mmunoz@example.vip.com")
				.vip(true)
				.build();
		
		log.info("Creating VIP Customer: {}", createCustomerDto);
		Customer customer = customerService.create(createCustomerDto).block();
		assertNotNull(customer);
		return customer;
	}
	
}
