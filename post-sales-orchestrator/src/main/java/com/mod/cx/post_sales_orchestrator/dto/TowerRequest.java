package com.mod.cx.post_sales_orchestrator.dto;

import lombok.Data;

import java.util.List;

@Data
public class TowerRequest {

    private Long phaseId;
    private String name;
    private Integer totalFloors;
    private Integer maxUnitsPerFloor;
    private Boolean oc;
    private String reraStatus;
    private String constructionStatus;
    private List<String> inventoryType;
    private List<String> conditions;

}
