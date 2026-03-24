package com.mod.cx.post_sales_orchestrator.dto;

import lombok.Data;

@Data
public class UserRequest {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private boolean isActive;
}
