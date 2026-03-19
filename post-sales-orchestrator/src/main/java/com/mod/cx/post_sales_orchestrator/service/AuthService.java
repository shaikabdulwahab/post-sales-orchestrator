package com.mod.cx.post_sales_orchestrator.service;

import com.mod.cx.post_sales_orchestrator.dto.AuthResponse;
import com.mod.cx.post_sales_orchestrator.dto.ClientSignUpRequest;
import com.mod.cx.post_sales_orchestrator.dto.CustomerSignUpRequest;
import com.mod.cx.post_sales_orchestrator.dto.LoginRequest;
import com.mod.cx.post_sales_orchestrator.enums.role;
import com.mod.cx.post_sales_orchestrator.exception.AuthException;
import com.mod.cx.post_sales_orchestrator.jooq.tables.records.ClientsRecord;
import com.mod.cx.post_sales_orchestrator.jooq.tables.records.UsersRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static com.mod.cx.post_sales_orchestrator.jooq.Tables.CLIENTS;
import static com.mod.cx.post_sales_orchestrator.jooq.Tables.USERS;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final DSLContext dsl;
    private final PasswordEncoder passwordEncoder;

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
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
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

    @Transactional
    public void registerClient(ClientSignUpRequest request) {

        boolean domainExists = dsl.fetchExists(
                dsl.selectFrom(CLIENTS).where(CLIENTS.DOMAIN_URL.eq(request.getDomainUrl()))
        );
        if(domainExists) throw new AuthException("Domain is already in use.");

        ClientsRecord client = dsl.newRecord(CLIENTS);
        client.setDomainUrl(request.getDomainUrl());
        client.store();

        UsersRecord adminUser = dsl.newRecord(USERS);
        adminUser.setClientId(client.getId());
        adminUser.setEmail(request.getEmail());
        adminUser.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        adminUser.setFirstName(request.getFirstName());
        adminUser.setLastName(request.getLastName());
        adminUser.setRole(String.valueOf(role.CLIENT));
        adminUser.setIsActive((byte) 1);
        adminUser.store();

    }

    public void registerCustomer(CustomerSignUpRequest request) {

        var client = dsl.selectFrom(CLIENTS)
                .where(CLIENTS.DOMAIN_URL.eq(request.getDomainUrl()))
                .fetchOptional()
                .orElseThrow(() -> new AuthException("Client not found for the given domain."));


        boolean userExists = dsl.fetchExists(
                dsl.selectFrom(USERS).where(USERS.EMAIL.eq(request.getEmail()))
        );
        if(userExists) throw new AuthException("User already exists.");

        UsersRecord customerUser = dsl.newRecord(USERS);
        customerUser.setClientId(client.getId());
        customerUser.setEmail(request.getEmail());
        customerUser.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        customerUser.setFirstName(request.getFirstName());
        customerUser.setLastName(request.getLastName());
        customerUser.setRole(String.valueOf(role.CUSTOMER));
        customerUser.setIsActive((byte) 1);
        customerUser.store();
    }
}