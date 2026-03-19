package com.mod.cx.post_sales_orchestrator.dto;

import lombok.Data;

@Data
public class CustomerSignUpRequest {

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String domainUrl;

}
