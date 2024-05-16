package com.munioz.mark.practice.infrastructure.config;

import static java.util.Collections.singletonList;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;

@Configuration
public class MongoConfig {
	
	@Bean("mongoProperties1")
	@ConfigurationProperties(prefix = "mongodb.db1")
	@Primary
	public MongoProperties mongoDatasource1Properties() {
		return new MongoProperties();
	}
	
	@Bean("mongoProperties2")
	@ConfigurationProperties(prefix = "mongodb.db2")
	public MongoProperties mongoDatasource2Properties() {
		return new MongoProperties();
	}
	
	@Bean("mongoClient1")
	@Primary
	public MongoClient mongoClient1(@Qualifier("mongoProperties1") MongoProperties mongoProperties) {
		return MongoClients.create(MongoClientSettings.builder()
				.applyToClusterSettings(builder -> builder
						.hosts(singletonList(new ServerAddress(mongoProperties.getHost(), mongoProperties.getPort()))))
				.build());
	}
	
	@Bean("mongoClient2")
	public MongoClient mongoClient2(@Qualifier("mongoProperties2") MongoProperties mongoProperties) {
		return MongoClients.create(MongoClientSettings.builder()
				.applyToClusterSettings(builder -> builder
						.hosts(singletonList(new ServerAddress(mongoProperties.getHost(), mongoProperties.getPort()))))
				.build());
	}
	
	@Bean("mongoFactory1")
	@Primary
	public ReactiveMongoDatabaseFactory mongoDatabaseFactory1(
			@Qualifier("mongoClient1") MongoClient mongoClient,
			@Qualifier("mongoProperties1") MongoProperties mongoProperties) {
		
		return new SimpleReactiveMongoDatabaseFactory(mongoClient, mongoProperties.getDatabase());
	}
	
	@Bean("mongoFactory2")
	public ReactiveMongoDatabaseFactory mongoDatabaseFactory2(
			@Qualifier("mongoClient2") MongoClient mongoClient,
			@Qualifier("mongoProperties2") MongoProperties mongoProperties) {
		
		return new SimpleReactiveMongoDatabaseFactory(mongoClient, mongoProperties.getDatabase());
	}
	
	@Bean("mongoTemplate1")
	@Primary
	public ReactiveMongoTemplate mongoTemplate1(@Qualifier("mongoFactory1") ReactiveMongoDatabaseFactory factory) {
		return new ReactiveMongoTemplate(factory);
	}
	
	@Bean("mongoTemplate2")
	public ReactiveMongoTemplate mongoTemplate2(@Qualifier("mongoFactory2") ReactiveMongoDatabaseFactory factory) {
		return new ReactiveMongoTemplate(factory);
	}
}
