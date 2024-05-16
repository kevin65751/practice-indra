package com.munioz.mark.practice.infrastructure.entities;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerEntity {
	@MongoId
	private String id;
	private String name;
	private String email;
	private Boolean vip;
	private Date created;
	private Date modified;
}
