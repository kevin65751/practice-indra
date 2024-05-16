package com.munioz.mark.practice.infrastructure.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.munioz.mark.practice.infrastructure.entities.CustomerEntity;
import com.munioz.mark.practice.infrastructure.repositories.first.CustomerRepository1;
import com.munioz.mark.practice.infrastructure.repositories.second.CustomerRepository2;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

@SpringBootTest
public class CustomRepositoryTest {
	private static final Logger log = LoggerFactory.getLogger(CustomRepositoryTest.class);

	@Autowired
	private CustomerRepository1 customerDatasource1Repository;
	
	@Autowired
	private CustomerRepository2 customerDatasource2Repository;

	@BeforeAll
	public static void setup() {
		try {
			/**
			 * Iniciando bases de datos embebidas para pruebas de integración
			 */
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
	public void testCrudRepo1() {
		testCrudRepo(customerDatasource1Repository);
	}
	
	@Test
	public void testCrudRepo2() {
		testCrudRepo(customerDatasource2Repository);
	}
	
	private void testCrudRepo(ReactiveMongoRepository<CustomerEntity, String> reactiveMongoRepository) {
		Date created = new Date();
		
		CustomerEntity customer = CustomerEntity.builder()
				.name("Mark")
				.email("mkmunozr@uni.pe")
				.created(created)
				.modified(created)
				.build();
		
		log.info("Guardando cliente: {}", customer.toString());
		CustomerEntity savedCustomer = reactiveMongoRepository.save(customer).block();
		assertNotNull(savedCustomer);
		assertEquals(customer.getName(), savedCustomer.getName());
		assertEquals(customer.getEmail(), savedCustomer.getEmail());
		assertNotNull(savedCustomer.getCreated());
		assertNotNull(savedCustomer.getModified());
		
		savedCustomer.setEmail("mark65751@gmail.com");
		savedCustomer.setModified(new Date());
		
		log.info("Modificando email: {}", savedCustomer.toString());
		CustomerEntity modifiedCustomer = reactiveMongoRepository.save(savedCustomer).block();
		assertNotNull(modifiedCustomer);
		assertEquals(savedCustomer.getName(), modifiedCustomer.getName());
		assertEquals(savedCustomer.getEmail(), modifiedCustomer.getEmail());
		assertNotNull(savedCustomer.getCreated());
		assertNotNull(savedCustomer.getModified());
		assertTrue(modifiedCustomer.getModified().after(modifiedCustomer.getCreated()));
		
		log.info("Eliminando cliente");
		reactiveMongoRepository.delete(modifiedCustomer).block();
		
		CustomerEntity deletedCustomer = reactiveMongoRepository.findById(modifiedCustomer.getId()).block();
		assertNull(deletedCustomer);
	}
}
