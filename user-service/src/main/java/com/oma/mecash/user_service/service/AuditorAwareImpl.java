package com.oma.mecash.user_service.service;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;

@Service
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