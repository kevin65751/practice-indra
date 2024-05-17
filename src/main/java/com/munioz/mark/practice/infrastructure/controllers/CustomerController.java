package com.munioz.mark.practice.infrastructure.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.munioz.mark.practice.application.dto.CreateCustomerDto;
import com.munioz.mark.practice.application.dto.DeleteCustomerDto;
import com.munioz.mark.practice.application.dto.Result;
import com.munioz.mark.practice.application.dto.UpdateCustomerDto;
import com.munioz.mark.practice.application.services.CustomerService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	
	@GetMapping("/{id}")
	@Retry(name = "retryCustomerApi", fallbackMethod = "fallbackAfterRetry")
	public Mono<Result<?>> getCustomerById(@PathVariable("id") String id) {
		return customerService.getById(id).map(Result::success);
	}
	
	@PostMapping("/create")
	@CircuitBreaker(name = "createCircuiteBreakerApi")
	public Mono<Result<?>> createCustomer(@RequestBody CreateCustomerDto createCustomerDto) {
		return customerService.create(createCustomerDto).map(Result::success);
	}
	
	@PutMapping("/modify")
	@CircuitBreaker(name = "updateCircuiteBreakerApi")
	public Mono<Result<?>> modifyCustomer(@RequestBody UpdateCustomerDto modifyCustomerDto) {
		return customerService.update(modifyCustomerDto).map(Result::success);
	}
	
	@DeleteMapping("/delete/{id}")
	@CircuitBreaker(name = "deleteCircuiteBreakerApi")
	public Mono<Result<Object>> deleteCustomer(@PathVariable("id") String id) {
		DeleteCustomerDto deleteCustomerDto = DeleteCustomerDto.builder()
				.id(id)
				.build();
		
		return customerService.deleteById(deleteCustomerDto).map(Result::success);
	}

	public Mono<Result<Object>> fallbackCircuitBreaker(Exception exception) {
		return Mono.just(Result.error(String.format("Fallback: %s", exception.getMessage())));
	}
	
	public Mono<Result<Object>> fallbackAfterRetry(Exception exception) {
		return Mono.just(Result.error(String.format("Fallback: %s", exception.getMessage())));
	}
	
}
