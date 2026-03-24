package com.mod.cx.post_sales_orchestrator.dto;

import lombok.Data;

@Data
public class UpdateUserPassRequest {
    private String oldPassword;
    private String newPassword;
}
