package com.rabo.customer.statement.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Builder
public class StatementReport {

	@Getter
	@Setter
	private Long transactionReference;
	
	@Getter
	@Setter
	private String accountNumber;

	@Getter
	@Setter
	private String description;

	@Getter
	@Setter
	private String remarks;
}
