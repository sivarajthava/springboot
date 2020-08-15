package com.rabo.customer.statement.filter;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.ThreadContext;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Sivaraj
 * @category : Request filter class
 * @since <b> Feb-29-2020 </b>
 * @version 1.0
 * 
 *          <pre>
 *          StatementFilter - Filter the all incoming request.
 * 
 *          <pre>
 */
@Component
@Slf4j
public class StatementFilter implements Filter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		ThreadContext.put("fsreqid", Objects.nonNull(request.getHeader("fsreqid")) ? request.getHeader("fsreqid")
				: UUID.randomUUID().toString());
		log.debug("doFilter() - Request received ");
		HttpServletResponse response = (HttpServletResponse) res;
		// TODO - Only for develop purpose. Production version you can use node.js for
		// request routing instead of localhost
		response.setHeader("Access-Control-Allow-Origin", "*");// request.getHeader("Origin")
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me");
		chain.doFilter(req, res);
		log.debug("doFilter() - Request processed");
	}

	@Override
	public void init(FilterConfig filterConfig) {
		log.debug("Statement filter is instantiated succesfully");
	}

	@Override
	public void destroy() {
		log.debug("Statement filter is destroyed succesfully");
	}

}