package com.munioz.mark.practice.domain.ports.out;

public interface CustomerRepositoryFactoryPort {
	public static final int DATA_SOURCE_1 = 1, DATA_SOURCE_2 = 2;
	
	public CustomerRepositoryPort getCustomerRepositoryPort(int dataSourceId);
}
