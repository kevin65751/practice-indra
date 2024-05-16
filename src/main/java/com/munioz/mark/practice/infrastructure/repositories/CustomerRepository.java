package com.munioz.mark.practice.infrastructure.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.munioz.mark.practice.infrastructure.entities.CustomerEntity;

public interface CustomerRepository extends ReactiveMongoRepository<CustomerEntity, String> {

}
