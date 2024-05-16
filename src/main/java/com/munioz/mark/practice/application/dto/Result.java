package com.munioz.mark.practice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Result<T> {
	private boolean success;
	private String error;
	private T content;
	
	public static Result<Object> error(String message) {
		return Result.builder()
				.success(false)
				.error(message)
				.build();
	}
	
	public static <S> Result<S> success(S content) {
		Result<S> result = new Result<S>();
		result.setContent(content);
		result.setSuccess(true);
		return result;
	}
	
	public static Result<Object> empty() {
		return Result.builder()
				.success(true)
				.build();
	}
	
}
