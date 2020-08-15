package com.rabo.customer.statement.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class FailedResponse {

	private String errorMessage;

	private String errorMessageType;

	private String statusCode;

	private boolean success;

	@JsonInclude(Include.NON_NULL)
	private String remarks;

}
