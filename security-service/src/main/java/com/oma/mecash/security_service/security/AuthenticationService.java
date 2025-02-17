package com.oma.mecash.security_service.security;


import com.oma.mecash.security_service.enums.Role;
import com.oma.mecash.security_service.model.SecurityUser;
import com.oma.mecash.security_service.model.entity.AuthUser;
import com.oma.mecash.security_service.repository.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {

    private final AuthUserRepository authUserRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AuthUser user = authUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Set<Role> roles = user.getRoles() == null ? new HashSet<>(): user.getRoles();
        Set<SimpleGrantedAuthority> authorities =
                roles.stream()
                        .map(Role::getGrantedAuthorities)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toSet());

        return new SecurityUser(user.getId(), email, user.getPassword(), user.getCreatedDate(), authorities);
    }

//    public APIResponse<LoginResponse> loginResponse(LoginRequest loginRequest, AuthenticationManager authenticationManager, JwtService jwtService) {
//        String email = loginRequest.getEmail();
//        if (email == null || "".equals(email.trim())) {
//            return new APIResponse<>(false, "Email can not be empty", 400, new LoginResponse());
//        }
//        email = email.toLowerCase();
//
//        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, loginRequest.getPassword()));
//        if (!auth.isAuthenticated()) {
//            return new APIResponse<>(false, "Authentication failed", 403, new LoginResponse());
//        }
//
//        UserEntity user = userRepository.findByEmail(email);
//
//        String result = "Bearer " + jwtService.generateToken(new SecurityUser(email, loginRequest.getPassword(), auth.getAuthorities()));
//
//        LoginResponse response = LoginResponse.builder()
//                .id(user.getId())
//                .token(result)
//                .name(user.getName())
//                .email(user.getEmail())
//                .role(user.getUserRole())
//                .phoneNumber(user.getPhoneNumber())
//                .loginDate(LocalDateTime.now())
//                .companyName(user.getCompanyName())
//                .build();
//
//        return new APIResponse<>(true, "success", 200, response);
//    }

}