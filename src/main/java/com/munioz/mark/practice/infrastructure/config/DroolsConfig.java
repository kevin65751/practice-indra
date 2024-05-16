package com.munioz.mark.practice.infrastructure.config;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DroolsConfig {
	public static KieServices kieServices = KieServices.Factory.get();
	
	public KieFileSystem kieFileSystem() {
		KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
		kieFileSystem.write(ResourceFactory.newClassPathResource("rules/customer.drl"));
		return kieFileSystem;
	}
	
	@Bean
	public KieContainer kieContainer() {
		KieRepository kieRepository = kieServices.getRepository();
		kieRepository.addKieModule(new KieModule() {
			@Override
			public ReleaseId getReleaseId() {
				return kieRepository.getDefaultReleaseId();
			}
		});
		
		KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem());
		kieBuilder.buildAll();
		KieModule kieModule = kieBuilder.getKieModule();
		KieContainer kieContainer = kieServices.newKieContainer(kieModule.getReleaseId());
		return kieContainer;
	}
	
	@Bean
	public KieSession kieSession(KieContainer kieContainer) {
		return kieContainer.newKieSession();
	}
}
