package com.oma.mecash.wallet_service.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;

@Service
@Slf4j
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // Implement logic to retrieve the current user's email (from security context, JWT, etc.)
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .flatMap(principal -> Stream.of(principal)
                        .filter(UserDetails.class::isInstance)
                        .map(UserDetails.class::cast)
                        .map(UserDetails::getUsername)
                        .findFirst()
                        .or(() -> principal instanceof String ? Optional.of((String) principal) : Optional.empty())
                );
    }
}