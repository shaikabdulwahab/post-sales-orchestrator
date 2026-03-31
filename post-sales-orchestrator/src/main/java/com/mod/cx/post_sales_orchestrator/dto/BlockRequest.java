package com.mod.cx.post_sales_orchestrator.dto;

import com.mod.cx.post_sales_orchestrator.enums.ConstructionStatus;
import com.mod.cx.post_sales_orchestrator.enums.ReraStatus;
import lombok.Data;

import java.util.List;

@Data
public class BlockRequest {

    private Long phaseId;
    private String name;
    private Boolean oc;
    private ReraStatus reraStatus;
    private ConstructionStatus constructionStatus;
    private List<String> inventoryType;
    private List<String> conditions;
}
