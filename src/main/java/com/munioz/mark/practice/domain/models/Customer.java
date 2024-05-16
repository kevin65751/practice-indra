package com.munioz.mark.practice.domain.models;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
	private String id;
	private String name;
	private String email;
	private Boolean vip;
	private Date created;
	private Date modified;
}
