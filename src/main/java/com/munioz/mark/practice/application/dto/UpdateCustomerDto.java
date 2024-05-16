package com.munioz.mark.practice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCustomerDto {
	private String id;
	private String name;
	private String email;
	private Integer repositoryId;
}
