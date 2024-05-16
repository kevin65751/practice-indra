package com.munioz.mark.practice.domain.ports.in;

import reactor.core.publisher.Mono;

public interface DeleteCustomerUseCase {

	public Mono<Void> deleteById(int dataBaseId, String id);
	
}
