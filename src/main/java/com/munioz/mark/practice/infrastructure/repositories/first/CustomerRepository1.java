package com.munioz.mark.practice.infrastructure.repositories.first;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.munioz.mark.practice.infrastructure.repositories.CustomerRepository;

@Repository
@Lazy
public interface CustomerRepository1 extends CustomerRepository {
	
}
