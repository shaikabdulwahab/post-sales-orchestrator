package com.mod.cx.post_sales_orchestrator.service;

import com.mod.cx.post_sales_orchestrator.dto.ProjectDTO;
import com.mod.cx.post_sales_orchestrator.jooq.tables.daos.ProjectDao;
import com.mod.cx.post_sales_orchestrator.jooq.tables.pojos.Phase;
import com.mod.cx.post_sales_orchestrator.jooq.tables.pojos.Project;
import com.mod.cx.post_sales_orchestrator.jooq.tables.records.ProjectRecord;
import com.mod.cx.post_sales_orchestrator.service.base.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.jooq.DAO;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.mod.cx.post_sales_orchestrator.jooq.Tables.PHASE;
import static com.mod.cx.post_sales_orchestrator.jooq.Tables.PROJECT;

@Service
@RequiredArgsConstructor
public class ProjectService extends BaseServiceImpl<ProjectRecord, Project, Long> {

    private final ProjectDao projectDao;
    private final DSLContext dsl;
    private final ImageKitService imageKitService;

    @Override
    protected DAO<ProjectRecord, Project, Long> getDao() {
        return projectDao;
    }


    public List<Phase> getProjectPhases(Long projectId, int page, int size) {
        int offset = page * size;

        return dsl.selectFrom(PHASE)
                .where(PHASE.PROJECT_ID.eq(projectId))
                .limit(size)
                .offset(offset)
                .fetchInto(Phase.class);
    }


    public Project createProject(ProjectDTO projectDTO,
                                 MultipartFile bannerLogo,
                                 MultipartFile builderLogo,
                                 MultipartFile strategicPartnerLogo,
                                 MultipartFile projectLogo) throws Exception {

        String clientIdStr = String.valueOf(projectDTO.getClientId());

        if (projectLogo != null && !projectLogo.isEmpty()) {
            String url = imageKitService.uploadAndGetUrl(projectLogo, clientIdStr, "project-logos");
            projectDTO.setProjectLogo(url);
        }

        if (bannerLogo != null && !bannerLogo.isEmpty()) {
            String url = imageKitService.uploadAndGetUrl(bannerLogo, clientIdStr, "banner-logos");
            projectDTO.setBannerLogo(url);
        }

        if (builderLogo != null && !builderLogo.isEmpty()) {
            String url = imageKitService.uploadAndGetUrl(builderLogo, clientIdStr, "builder-logos");
            projectDTO.setBuilderLogo(url);
        }

        if (strategicPartnerLogo != null && !strategicPartnerLogo.isEmpty()) {
            String url = imageKitService.uploadAndGetUrl(strategicPartnerLogo, clientIdStr, "partner-logos");
            projectDTO.setStrategicPartnerLogo(url);
        }

        ProjectRecord record = dsl.newRecord(PROJECT);
        record.setClientId(projectDTO.getClientId());
        record.setName(projectDTO.getName());
        
        record.setProjectLogo(projectDTO.getProjectLogo());
        record.setBannerLogo(projectDTO.getBannerLogo());
        record.setStrategicPartnerLogo(projectDTO.getStrategicPartnerLogo());
        
        if (projectDTO.getProjectType() != null) {
            record.setProjectType(projectDTO.getProjectType().name());
        }
        if (projectDTO.getProductType() != null) {
            record.setProductType(projectDTO.getProductType().name());
        }
        record.setBuilderLogo(projectDTO.getBuilderLogo());

        record.store();
        
        // Re-fetch the record to get all database-generated values (like id, created_at, etc.)
        return dsl.selectFrom(PROJECT)
                  .where(PROJECT.ID.eq(record.getId()))
                  .fetchOneInto(Project.class);
    }
}
