package com.munioz.mark.practice.infrastructure.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.UUID;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.mongodb.MongoSocketException;
import com.munioz.mark.practice.application.dto.CreateCustomerDto;
import com.munioz.mark.practice.application.dto.DeleteCustomerDto;
import com.munioz.mark.practice.application.dto.Result;
import com.munioz.mark.practice.application.dto.UpdateCustomerDto;
import com.munioz.mark.practice.application.services.CustomerService;
import com.munioz.mark.practice.domain.models.Customer;
import com.munioz.mark.practice.domain.ports.out.CustomerRepositoryFactoryPort;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest(classes = {}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = {MongoAutoConfiguration.class, MongoReactiveAutoConfiguration.class, MongoDataAutoConfiguration.class, MongoReactiveRepositoriesAutoConfiguration.class})
@Slf4j
public class CustomerResilienceControllerTest {
	private static final Logger log = LoggerFactory.getLogger(CustomerResilienceControllerTest.class);
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@MockBean
	private CustomerService customerService;
	
	
	public static class CustomAnswer implements Answer<Customer> {
		private int callCount = 0;
		
		@Override
		public Customer answer(InvocationOnMock invocation) throws Throwable {
			callCount++;
			throw new MongoSocketException("Mongo database connection", null);
		}
		
		public int getCallCount() {
			return callCount;
		}
	}
	
	@Test
	public void getCustomerRetryMongoDbTest() throws Exception {
		String customerId = String.format("%s-%s", UUID.randomUUID().toString(), CustomerRepositoryFactoryPort.DATA_SOURCE_1);
		
		CustomAnswer customAnswer = new CustomAnswer();
		when(customerService.getById(customerId)).then(customAnswer);
		
		ResponseEntity<Result> response = restTemplate.getForEntity("/api/customer/" + customerId, Result.class);
		
		log.info("Response: {}", response.toString());
		assertTrue(response.getStatusCode().is2xxSuccessful());
		assertFalse(response.getBody().isSuccess());
		assertEquals(3, customAnswer.getCallCount());
	}
	
	@Test
	public void createCustomerCircuiteBreaker() {
		CreateCustomerDto createCustomerDto = CreateCustomerDto.builder()
				.build();
		
		CustomAnswer answer = new CustomAnswer();
		when(customerService.create(createCustomerDto)).then(answer);
		
		IntStream.rangeClosed(1, 5)
			.forEach(i -> {
				ResponseEntity<Result> response = restTemplate.postForEntity("/api/customer/create", createCustomerDto, Result.class);
				log.info("{} -> {}", i, response.toString());
				assertEquals(response.getStatusCode().value(), HttpStatus.INTERNAL_SERVER_ERROR.value());
			});
		
		ResponseEntity<Result> response = restTemplate.postForEntity("/api/customer/create", createCustomerDto, Result.class);
		log.info("Expecting Service Unavailable for create: {}", response.toString());
		assertEquals(response.getStatusCode().value(), HttpStatus.SERVICE_UNAVAILABLE.value());
	}
	
	@Test
	public void updateCustomerCircuiteBreaker() {
		UpdateCustomerDto updateCustomerDto = UpdateCustomerDto.builder()
				.build();
		
		CustomAnswer answer = new CustomAnswer();
		when(customerService.update(updateCustomerDto)).then(answer);
		
		IntStream.rangeClosed(1, 5)
			.forEach(i -> {
				ResponseEntity<Result> response = restTemplate.exchange("/api/customer/modify", HttpMethod.PUT, new HttpEntity<UpdateCustomerDto>(updateCustomerDto), Result.class);
				log.info("{} -> {}", i, response.toString());
				assertEquals(response.getStatusCode().value(), HttpStatus.INTERNAL_SERVER_ERROR.value());
			});
		
		ResponseEntity<Result> response = restTemplate.exchange("/api/customer/modify", HttpMethod.PUT, new HttpEntity<UpdateCustomerDto>(updateCustomerDto), Result.class);
		log.info("Expecting Service Unavailable for update: {}", response.toString());
		assertEquals(response.getStatusCode().value(), HttpStatus.SERVICE_UNAVAILABLE.value());
	}
	
	@Test
	public void deleteCustomerCircuiteBreaker() {
		DeleteCustomerDto deleteCustomerDto = DeleteCustomerDto.builder()
				.id(UUID.randomUUID().toString())
				.build();
		
		CustomAnswer answer = new CustomAnswer();
		when(customerService.deleteById(deleteCustomerDto)).then(answer);
		
		IntStream.rangeClosed(1, 5)
			.forEach(i -> {
				ResponseEntity<Result> response = restTemplate.exchange("/api/customer/delete/" + deleteCustomerDto.getId(), HttpMethod.DELETE, HttpEntity.EMPTY, Result.class);
				log.info("{} -> {}", i, response.toString());
				assertEquals(response.getStatusCode().value(), HttpStatus.INTERNAL_SERVER_ERROR.value());
			});
		
		ResponseEntity<Result> response = restTemplate.exchange("/api/customer/delete/" + deleteCustomerDto.getId(), HttpMethod.DELETE, HttpEntity.EMPTY, Result.class);
		log.info("Expecting Service Unavailable for delete: {}", response.toString());
		assertEquals(response.getStatusCode().value(), HttpStatus.SERVICE_UNAVAILABLE.value());
	}
	
}
