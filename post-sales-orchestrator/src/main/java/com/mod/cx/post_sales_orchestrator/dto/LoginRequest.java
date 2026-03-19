package com.mod.cx.post_sales_orchestrator.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
    
    /**
     * The frontend should extract this from the browser (e.g., window.location.hostname)
     * Example: "nandiproperties.postsales.com"
     */
    private String domainUrl; 
}