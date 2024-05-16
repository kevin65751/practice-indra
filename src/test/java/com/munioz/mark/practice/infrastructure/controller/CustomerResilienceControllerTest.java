package com.munioz.mark.practice.infrastructure.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import com.mongodb.MongoSocketException;
import com.munioz.mark.practice.application.dto.Result;
import com.munioz.mark.practice.application.services.CustomerService;
import com.munioz.mark.practice.domain.exceptions.CustomerNotFoundException;
import com.munioz.mark.practice.domain.models.Customer;
import com.munioz.mark.practice.domain.ports.out.CustomerRepositoryFactoryPort;

import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
	
	public static class CustomDomainExceptionAnswer implements Answer<Customer> {
		private int callCount = 0;
		private String customerId;
		
		public CustomDomainExceptionAnswer(String customerId) {
			this.customerId = customerId;
		}
		
		@Override
		public Customer answer(InvocationOnMock invocation) throws Throwable {
			callCount++;
			throw new CustomerNotFoundException(customerId);
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
	public void getCustomerDontRetryForDomainExceptionsTest() throws Exception {
		String customerId = String.format("%s-%s", UUID.randomUUID().toString(), CustomerRepositoryFactoryPort.DATA_SOURCE_1);
		
		CustomDomainExceptionAnswer answer = new CustomDomainExceptionAnswer(customerId);
		
		when(customerService.getById(customerId)).then(answer);
		
		ResponseEntity<Result> response = restTemplate.getForEntity("/api/customer/" + customerId, Result.class);
		
		log.info("Response: {}", response.toString());
		assertTrue(response.getStatusCode().is2xxSuccessful());
		assertFalse(response.getBody().isSuccess());
		assertEquals(1, answer.getCallCount());
	}
}
