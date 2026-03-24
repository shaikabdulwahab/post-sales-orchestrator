package com.mod.cx.post_sales_orchestrator.dto;

import lombok.Data;

@Data
public class PhaseRequest {

    private Long projectId;
    private String name;
    private String reraType;
    private String oc;
    private String reraStatus;

}
