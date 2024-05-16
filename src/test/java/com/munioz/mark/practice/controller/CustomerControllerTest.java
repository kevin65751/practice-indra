package com.munioz.mark.practice.controller;

import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.munioz.mark.practice.application.dto.CreateCustomerDto;
import com.munioz.mark.practice.application.dto.DeleteCustomerDto;
import com.munioz.mark.practice.application.dto.Result;
import com.munioz.mark.practice.application.dto.UpdateCustomerDto;
import com.munioz.mark.practice.application.services.CustomerService;
import com.munioz.mark.practice.domain.models.Customer;
import com.munioz.mark.practice.domain.ports.out.CustomerRepositoryFactoryPort;
import com.munioz.mark.practice.infrastructure.controllers.CustomerController;

import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = CustomerController.class)
@AutoConfigureWebTestClient
public class CustomerControllerTest {
	
	@Autowired
    private WebTestClient webTestClient;
	
	@MockBean
	private CustomerService customerService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	public void getCustomerTest() throws Exception {
		String customerId = String.format("%s-%s", UUID.randomUUID().toString(), CustomerRepositoryFactoryPort.DATA_SOURCE_1);
		
		Customer customer = Customer.builder()
				.id(customerId)
				.name("Testing name")
				.email("mmunoz@example.com")
				.vip(true)
				.created(new Date())
				.modified(new Date())
				.build();
		
		when(customerService.getById(customerId)).thenReturn(Mono.just(customer));
		
		webTestClient.get()
			.uri(String.format("/api/customer/%s", customerId))
			.exchange()
			.expectStatus().isOk()
			.expectBody().json(objectMapper.writeValueAsString(Result.success(customer)));
	}
	
	@Test
	public void getCustomerErrorTest() throws Exception {
		String customerId = String.format("%s-%s", UUID.randomUUID().toString(), CustomerRepositoryFactoryPort.DATA_SOURCE_1);
		
		when(customerService.getById(customerId)).thenThrow(new RuntimeException("Generic Test Exception"));
		
		webTestClient.get()
			.uri(String.format("/api/customer/%s", customerId))
			.exchange()
			.expectStatus().is5xxServerError()
			.expectBody().json(objectMapper.writeValueAsString(Result.error("Generic Test Exception")));
	}
	
	@Test
	public void createNormalCustomerTest() throws Exception {
		CreateCustomerDto createCustomerDto = CreateCustomerDto.builder()
				.name("Normal Customer")
				.email("normal@example.com")
				.vip(false)
				.build();
		
		Customer customer = Customer.builder()
				.name(createCustomerDto.getName())
				.email(createCustomerDto.getEmail())
				.vip(createCustomerDto.getVip())
				.created(new Date())
				.modified(new Date())
				.build();
		when(customerService.create(createCustomerDto)).thenReturn(Mono.just(customer));
		
		webTestClient.post()
			.uri("/api/customer/create")
			.contentType(MediaType.APPLICATION_JSON)
			.body(BodyInserters.fromValue(createCustomerDto))
			.exchange()
			.expectStatus().isOk()
			.expectBody().json(objectMapper.writeValueAsString(Result.success(customer)));
	}
	
	@Test
	public void modifyCustomerTest() throws Exception {
		UpdateCustomerDto modifyCustomerDto = UpdateCustomerDto.builder()
				.id(String.format("%s-%s", UUID.randomUUID().toString(), CustomerRepositoryFactoryPort.DATA_SOURCE_1))
				.name("Modified name")
				.email("Modified email")
				.build();
		
		Customer customer = Customer.builder()
				.id(modifyCustomerDto.getId())
				.name(modifyCustomerDto.getName())
				.email(modifyCustomerDto.getEmail())
				.vip(true)
				.created(new Date())
				.modified(new Date())
				.build();
		when(customerService.update(modifyCustomerDto)).thenReturn(Mono.just(customer));
		
		webTestClient.put()
			.uri("/api/customer/modify")
			.contentType(MediaType.APPLICATION_JSON)
			.body(BodyInserters.fromValue(modifyCustomerDto))
			.exchange()
			.expectStatus().isOk()
			.expectBody().json(objectMapper.writeValueAsString(Result.success(customer)));
	}
	
	@Test
	public void deleteCustomerTest() throws Exception {
		DeleteCustomerDto deleteCustomerDto = DeleteCustomerDto.builder()
				.id(String.format("%s-%s", UUID.randomUUID().toString(), CustomerRepositoryFactoryPort.DATA_SOURCE_1))
				.build();
		
		when(customerService.deleteById(deleteCustomerDto)).thenReturn(Mono.empty());
		
		webTestClient.delete()
			.uri("/api/customer/delete/" + deleteCustomerDto.getId())
			.exchange()
			.expectStatus().isOk();
	}
	
}
