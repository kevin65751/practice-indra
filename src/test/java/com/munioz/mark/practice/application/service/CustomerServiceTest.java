package com.munioz.mark.practice.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import com.munioz.mark.practice.application.dto.CreateCustomerDto;
import com.munioz.mark.practice.application.dto.DeleteCustomerDto;
import com.munioz.mark.practice.application.dto.UpdateCustomerDto;
import com.munioz.mark.practice.application.services.CustomerService;
import com.munioz.mark.practice.domain.models.Customer;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import reactor.test.StepVerifier;

@SpringBootTest
public class CustomerServiceTest {
	private static final Logger log = LoggerFactory.getLogger(CustomerServiceTest.class);

	@Autowired
	private CustomerService customerService;

	@BeforeAll
	public static void setup() {
		try {
			log.info("Setting up");
			
			MongodStarter mongodStarter = MongodStarter.getDefaultInstance();
			
			IMongodConfig mongodConfig1 = new MongodConfigBuilder()
					.version(Version.Main.PRODUCTION)
					.net(new Net("localhost", 27017, Network.localhostIsIPv6()))
					.build();
			MongodExecutable mongodExecutable1 = mongodStarter.prepare(mongodConfig1);
			mongodExecutable1.start();
			
			IMongodConfig mongodConfig2 = new MongodConfigBuilder()
					.version(Version.Main.PRODUCTION)
					.net(new Net("localhost", 27018, Network.localhostIsIPv6()))
					.build();
			MongodExecutable mongodExecutable2 = mongodStarter.prepare(mongodConfig2);
			mongodExecutable2.start();
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(-1);
		}
	}
	
	@Test
	public void createNormalCustomerTest() throws Exception {
		createNormalCustomer();
	}
	
	@Test
	public void createVipCustomerTest() throws Exception {
		createVipCustomer();
	}
	
	@Test
	public void updateNormalCustomerTest() throws Exception {
		Customer customer = createNormalCustomer();
		
		UpdateCustomerDto updateCustomerDto = UpdateCustomerDto.builder()
				.id(customer.getId())
				.name(customer.getName() + "-modified")
				.email(customer.getEmail() + "-modified")
				.build();
		
		Customer updatedCustomer = customerService.update(updateCustomerDto).block();
		assertNotNull(updatedCustomer);
		assertNotEquals(customer.getName(), updatedCustomer.getName());
		assertNotEquals(customer.getEmail(), updatedCustomer.getEmail());
	}
	
	@Test
	public void updateVipCustomerTest() throws Exception {
		Customer customer = createVipCustomer();
		
		UpdateCustomerDto updateCustomerDto = UpdateCustomerDto.builder()
				.id(customer.getId())
				.name(customer.getName() + "-modified")
				.email(customer.getEmail() + "-modified")
				.build();
		
		Customer updatedCustomer = customerService.update(updateCustomerDto).block();
		assertNotNull(updatedCustomer);
		assertNotEquals(customer.getName(), updatedCustomer.getName());
		assertNotEquals(customer.getEmail(), updatedCustomer.getEmail());
		assertTrue(updatedCustomer.getModified().after(customer.getCreated()));
	}
	
	@Test
	public void deleteNormalCustomerTest() throws Exception {
		Customer customer = createNormalCustomer();
		
		customerService.deleteById(DeleteCustomerDto.builder()
				.id(customer.getId())
				.build()).block();
		
		StepVerifier.create(customerService.getById(customer.getId()))
			.expectError(NotFoundException.class);
	}
	
	@Test
	public void deleteVipCustomerTest() throws Exception {
		Customer customer = createVipCustomer();
		
		customerService.deleteById(DeleteCustomerDto.builder()
				.id(customer.getId())
				.build()).block();
		
		StepVerifier.create(customerService.getById(customer.getId()))
		.expectError(NotFoundException.class);
	}
	

	private Customer createNormalCustomer() throws Exception {
		CreateCustomerDto createCustomerDto = CreateCustomerDto.builder()
				.name("Normal Customer")
				.email("mmunoz@example.com")
				.vip(false)
				.build();
		
		log.info("Creating Normal Customer: {}", createCustomerDto);
		Customer customer = customerService.create(createCustomerDto).block();
		
		assertNotNull(customer);
		assertEquals(customer.getName(), createCustomerDto.getName());
		assertEquals(customer.getEmail(), createCustomerDto.getEmail());
		assertEquals(customer.getVip(), createCustomerDto.getVip());
		assertNotNull(customer.getCreated());
		assertNotNull(customer.getModified());
		return customer;
	}

	public Customer createVipCustomer() throws Exception {
		CreateCustomerDto createCustomerDto = CreateCustomerDto.builder()
				.name("VIP Customer")
				.email("mmunoz@example.vip.com")
				.vip(true)
				.build();
		
		log.info("Creating VIP Customer: {}", createCustomerDto);
		Customer customer = customerService.create(createCustomerDto).block();
		
		assertNotNull(customer);
		assertEquals(customer.getName(), createCustomerDto.getName());
		assertEquals(customer.getEmail(), createCustomerDto.getEmail());
		assertEquals(customer.getVip(), createCustomerDto.getVip());
		assertNotNull(customer.getCreated());
		assertNotNull(customer.getModified());
		
		return customer;
	}
	
}
