package com.oma.mecash.user_service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Arrays;
import java.util.List;

@EntityScan({"com.oma.mecash.security_service.model", "com.oma.mecash.user_service.model"})
@ComponentScan({
		"com.oma.mecash.security_service.service",
		"com.oma.mecash.security_service.config",
		"com.oma.mecash.security_service",
		"com.oma.mecash.user_service.service",
		"com.oma.mecash.user_service.controller",
		"com.oma.mecash.user_service.exception"
})
@EnableJpaRepositories({"com.oma.mecash.security_service.repository", "com.oma.mecash.user_service.repository"})
@SpringBootApplication
@Slf4j
public class UserServiceApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext run = SpringApplication.run(UserServiceApplication.class, args);

		Arrays.stream(run.getBeanDefinitionNames())
				.forEach((bean) ->System.out.println("Bean: " + bean));
	}

}
