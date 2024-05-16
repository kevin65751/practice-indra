package com.munioz.mark.practice.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(basePackages = { "com.munioz.mark.practice.infrastructure.repositories.first" }, reactiveMongoTemplateRef = "mongoTemplate1")
public class MongoDatasourceConfig1 {
	
}
