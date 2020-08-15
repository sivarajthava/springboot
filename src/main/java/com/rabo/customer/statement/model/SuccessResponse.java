package com.rabo.customer.statement.model;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Builder
public class SuccessResponse {

	@Setter
	@Getter
	private boolean success;

	@Setter
	@Getter
	private String remarks;

	@Setter
	@Getter
	private List<StatementReport> reports;

}
