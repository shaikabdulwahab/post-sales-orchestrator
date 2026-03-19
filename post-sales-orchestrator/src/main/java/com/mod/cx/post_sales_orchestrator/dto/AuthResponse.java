package com.mod.cx.post_sales_orchestrator.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String token; // JWT Token
    private String role;
    private String firstName;
    private String lastName;
    private Long clientId;
}