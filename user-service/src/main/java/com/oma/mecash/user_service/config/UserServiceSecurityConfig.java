package com.oma.mecash.user_service.config;

import com.oma.mecash.security_service.config.SecurityConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(SecurityConfig.class)
public class UserServiceSecurityConfig {
}
