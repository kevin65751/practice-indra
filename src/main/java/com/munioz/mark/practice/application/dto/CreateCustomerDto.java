package com.munioz.mark.practice.application.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateCustomerDto {
	private String name;
	private String email;
	private Boolean vip;
	private Integer repositoryId;
}
