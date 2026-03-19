package com.mod.cx.post_sales_orchestrator.controller;

import com.mod.cx.post_sales_orchestrator.dto.AuthResponse;
import com.mod.cx.post_sales_orchestrator.dto.ClientSignUpRequest;
import com.mod.cx.post_sales_orchestrator.dto.CustomerSignUpRequest;
import com.mod.cx.post_sales_orchestrator.dto.LoginRequest;
import com.mod.cx.post_sales_orchestrator.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.authenticate(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // later need to use global exception handler
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @PostMapping("/register/client")
    public ResponseEntity<?> registerClient(@RequestBody ClientSignUpRequest request) {
        try {
            authService.registerClient(request);
            return ResponseEntity.ok("Client registered successfully.");
        } catch (Exception e) {
            // later need to use global exception handler
            return ResponseEntity.badRequest() .body(e.getMessage());
        }

    }

    @PostMapping("/register/customer")
    public ResponseEntity<?> registerCustomer(@RequestBody CustomerSignUpRequest request) {
        try {
            authService.registerCustomer(request);
            return ResponseEntity.ok("Customer registered successfully.");
        } catch (Exception e) {
            // later need to use global exception handler
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}