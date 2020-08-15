package com.rabo.customer.statement.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class RaboSwaggerConfig {

	@Bean
	public Docket postsApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("public-api").apiInfo(apiInfo()).select().build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("RABO Bank Statement Report API")
				.description("RABO bank statement report api for generate customer statement report")
				.termsOfServiceUrl("https://www.rabobank.com").license("RABO License").licenseUrl("admin@rabo.com")
				.version("1.0").build();
	}

}
