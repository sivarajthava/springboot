package com.rabo.customer.statement.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@Slf4j
public class ReportSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		log.debug("configure() - Request received ");
		http.csrf().disable().headers().frameOptions().sameOrigin().and().authorizeRequests().anyRequest().anonymous()
				.and().httpBasic().disable();
		log.debug("configure() - Request accepted ");
	}

}
