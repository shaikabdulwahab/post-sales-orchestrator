package com.mod.cx.post_sales_orchestrator.service;

import com.mod.cx.post_sales_orchestrator.dto.AuthResponse;
import com.mod.cx.post_sales_orchestrator.dto.LoginRequest;
import com.mod.cx.post_sales_orchestrator.exception.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import static com.mod.cx.post_sales_orchestrator.jooq.Tables.CLIENTS;
import static com.mod.cx.post_sales_orchestrator.jooq.Tables.USERS;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final DSLContext dsl;
    // private final PasswordEncoder passwordEncoder; // Uncomment when Spring Security is added

    public AuthResponse authenticate(LoginRequest request) {
        
        // 1. Identify the Client by the domain
        var client = dsl.selectFrom(CLIENTS)
                .where(CLIENTS.DOMAIN_URL.eq(request.getDomainUrl()))
                .fetchOptional()
                .orElseThrow(() -> new AuthException("Client not found for the given domain."));

        // 2. Find the User within THAT specific client
        var user = dsl.selectFrom(USERS)
                .where(USERS.EMAIL.eq(request.getEmail()))
                .and(USERS.CLIENT_ID.eq(client.getId()))
                .fetchOptional()
                .orElseThrow(() -> new AuthException("Invalid email or password."));

        // 3. Verify Password (In production, use BCrypt via Spring Security!)
        // if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) { ... }
        if (!user.getPasswordHash().equals(request.getPassword())) {
            throw new AuthException("Invalid email or password.");
        }

        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new AuthException("User account is disabled.");
        }

        // 4. Generate JWT Token (Placeholder logic here)
        String jwtToken = "placeholder-jwt-token-for-" + user.getId();

        // 5. Return context to the frontend so it knows where to route them
        return AuthResponse.builder()
                .token(jwtToken)
                .role(user.getRole())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .clientId(client.getId())
                .build();
    }
}