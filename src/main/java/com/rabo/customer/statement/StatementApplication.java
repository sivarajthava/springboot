package com.rabo.customer.statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

/**
 * 
 * @author Sivaraj
 * @category : Bootstrap class
 * @since <b> Feb-29-2020 </b>
 * @version 1.0
 * 
 *          <pre>
 *          Statement application bootstrap class - RABO Bank Customer Statement
 *          Report Generator
 * 
 *          <pre>
 */
@SpringBootApplication(scanBasePackages = "com.rabo.customer.statement")
public class StatementApplication {

	private static final Logger logger = LoggerFactory.getLogger(StatementApplication.class);

	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		logger.info("StatementApplication initializatin - Begin");
		SpringApplication application = new SpringApplication();
		// application.setBannerMode(Banner.Mode.OFF);
		application.setRegisterShutdownHook(true);
		application.run(StatementApplication.class);
		logger.info("StatementApplication initializatin - Completed");
	}

	/**
	 * Log the successful bootstrap of the application
	 * 
	 * @param ctx
	 * @return
	 */
	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			logger.info("Rabo Customer Statement Processor API loaded successfully - {}", ctx.getApplicationName());
			logger.debug("Rabo Customer Statement Processor API loaded successfully for environemnt - {}",
					StringUtils.arrayToCommaDelimitedString(ctx.getEnvironment().getActiveProfiles()));
		};
	}

	/**
	 * For multi-part resolver bean registration
	 * 
	 * @return
	 *//*
	@Bean
	public MultipartResolver multipartResolver() {
		return new StandardServletMultipartResolver();
	}*/

}
