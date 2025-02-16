package com.oma.mecash.security_service.service;

import com.oma.mecash.security_service.exception.AuthenticateUserException;
import com.oma.mecash.security_service.exception.TransactionPinException;
import com.oma.mecash.security_service.model.SecurityUser;
import com.oma.mecash.security_service.model.entity.AuthUser;
import com.oma.mecash.security_service.repository.AuthUserRepository;
import com.oma.mecash.security_service.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthUserService {

    private final AuthUserRepository authUserRepository;

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;


    public SecurityUser getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null){
            throw new AuthenticateUserException("User does not exist");
        }
        Object principal = authentication.getPrincipal();
        log.info("principal: {}", principal);
        return (SecurityUser) principal;
    }

    public  AuthUser saveUser(AuthUser user) {
        return authUserRepository.save(user);
    }

    public AuthUser findUserByEmail(String email) {
        return authUserRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new AuthenticateUserException("Auth User does not exist"));
    }

    public String generateAccessToken(String userName, String password) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(userName, password);
        authenticationManager.authenticate(authentication);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

        return jwtService.generateToken(userDetails);
    }

    public void updatePin(AuthUser user, String pin){
        if(!passwordEncoder.matches(pin, user.getTransactionPin())){
            throw new TransactionPinException(pin);
        }
        user.setTransactionPin(passwordEncoder.encode(pin));
        authUserRepository.save(user);
    }

}
