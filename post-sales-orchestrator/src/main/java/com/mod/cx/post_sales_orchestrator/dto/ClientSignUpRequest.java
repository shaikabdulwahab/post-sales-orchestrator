package com.mod.cx.post_sales_orchestrator.dto;

import lombok.Data;

@Data
public class ClientSignUpRequest {

    private String domainUrl;

    private String email;

    private String firstName;

    private String lastName;

    private String password;
}
