package com.mod.cx.post_sales_orchestrator.dto;

import com.mod.cx.post_sales_orchestrator.enums.ProductType;
import com.mod.cx.post_sales_orchestrator.enums.ProjectType;
import lombok.Data;

@Data
public class ProjectDTO {

    private long id;
    private long clientId;
    private String name;
    private ProjectType projectType;
    private ProductType productType;
    private String bannerLogo;
    private String builderLogo;
    private String strategicPartnerLogo;
    private String projectLogo;
}