package com.oma.mecash.wallet_service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan({"com.oma.mecash.security_service.model", "com.oma.mecash.wallet_service.model"})
@ComponentScan({
        "com.oma.mecash.security_service.service",
        "com.oma.mecash.security_service.config",
        "com.oma.mecash.security_service",
        "com.oma.mecash.wallet_service.service",
        "com.oma.mecash.wallet_service.controller"})
@EnableJpaRepositories({"com.oma.mecash.security_service.repository", "com.oma.mecash.wallet_service.repository"})
@SpringBootApplication
@Slf4j
public class WalletServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(WalletServiceApplication.class, args);
    }
}