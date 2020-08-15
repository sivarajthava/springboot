package com.rabo.customer.statement.business;

import javax.annotation.PreDestroy;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomerStatementTerminator {

	@PreDestroy
	public void onDestroy() throws Exception {
		log.info("Spring Container is destroyed!!!");
	}
}
