package com.mod.cx.post_sales_orchestrator.service;

import com.mod.cx.post_sales_orchestrator.dto.PhaseRequest;
import com.mod.cx.post_sales_orchestrator.jooq.tables.daos.PhaseDao;
import com.mod.cx.post_sales_orchestrator.jooq.tables.pojos.Block;
import com.mod.cx.post_sales_orchestrator.jooq.tables.pojos.Phase;
import com.mod.cx.post_sales_orchestrator.jooq.tables.pojos.Tower;
import com.mod.cx.post_sales_orchestrator.jooq.tables.records.PhaseRecord;
import com.mod.cx.post_sales_orchestrator.service.base.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.jooq.DAO;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.mod.cx.post_sales_orchestrator.jooq.Tables.*;

@Service
@RequiredArgsConstructor
public class PhaseService extends BaseServiceImpl<PhaseRecord, Phase, Long> {

    private final PhaseDao phaseDao;
    private final DSLContext dsl;

    @Override
    protected DAO<PhaseRecord, Phase, Long> getDao() {
        return phaseDao;
    }

    public void createPhase(PhaseRequest request) {

        boolean exists = dsl.fetchExists(
                dsl.selectFrom(PHASE)
                        .where(PHASE.PROJECT_ID.eq(request.getProjectId()))
                        .and(PHASE.NAME.equalIgnoreCase(request.getName()))
        );

        if(exists) {
            throw new RuntimeException("Phase already exists");
        }

        PhaseRecord phase = dsl.newRecord(PHASE);
        phase.setProjectId(request.getProjectId());
        phase.setName(request.getName());
        phase.setReraType(request.getReraType());
        phase.setOc(request.getOc());
        phase.setReraStatus(request.getReraStatus());
        phase.store();
    }

    public Phase getPhase(Long id) {
        return dsl.selectFrom(PHASE)
                .where(PHASE.ID.eq(id))
                .fetchOptionalInto(Phase.class)
                .orElseThrow(() -> new RuntimeException("Phase not found"));
    }

    public List<Tower> getPhaseTowers(Long phaseId , int page, int size) {
        int offset = page * size;

        return dsl.selectFrom(TOWER)
                .where(TOWER.PHASE_ID.eq(phaseId))
                .limit(size)
                .offset(offset)
                .fetchInto(Tower.class);
    }

    public List<Block> getPhaseBlocks(Long phaseId , int page, int size) {
        int offset = page * size;

        return dsl.selectFrom(BLOCK)
                .where(BLOCK.PHASE_ID.eq(phaseId))
                .limit(size)
                .offset(offset)
                .fetchInto(Block.class);
    }

    public void deletePhase(Long id) {
        int deletedRows = dsl.deleteFrom(PHASE)
                .where(PHASE.ID.eq(id))
                .execute();

        if (deletedRows == 0) {
            throw new RuntimeException("Phase not found");
        }
    }

    @Transactional
    public Phase updatePhase(Long id, PhaseRequest request) {
        PhaseRecord phase = dsl.selectFrom(PHASE)
                .where(PHASE.ID.eq(id))
                .fetchOptional()
                .orElseThrow(() -> new RuntimeException("Phase not found"));

        String projectType = dsl.selectFrom(PROJECT)
                .where(PROJECT.ID.eq(phase.getProjectId()))
                .fetchOptional()
                .orElseThrow(() -> new RuntimeException("Project not found"))
                .getProjectType();


        if (request.getName() != null && !request.getName().equalsIgnoreCase(phase.getName())) {

            boolean nameExists = dsl.fetchExists(
                    dsl.selectFrom(PHASE)
                            .where(PHASE.PROJECT_ID.eq(phase.getProjectId()))
                            .and(PHASE.NAME.equalIgnoreCase(request.getName()))
            );

            if (nameExists) {
                throw new RuntimeException("A phase with this name already exists for this project");
            }
            phase.setName(request.getName());
        }
        if(request.getReraType() != null) phase.setReraType(request.getReraType());
        if(request.getOc() != null) phase.setOc(request.getOc());
        if(phase.getReraType().equalsIgnoreCase("phasewise"))
        {
            if(phase.getReraStatus().equalsIgnoreCase("not_approved") && request.getReraStatus().equalsIgnoreCase("approved"))
            {
                if(projectType.equalsIgnoreCase("residential"))
                {
                    String productType = dsl.selectFrom(PROJECT)
                            .where(PROJECT.ID.eq(phase.getProjectId()))
                            .fetchOptional()
                            .orElseThrow(() -> new RuntimeException("Project not found"))
                            .getProductType();

                    if(productType.equalsIgnoreCase("apartment"))
                    {
                        dsl.update(TOWER)
                                .set(TOWER.RERA_STATUS, "APPROVED")
                                .where(TOWER.PHASE_ID.eq(id))
                                .execute();
                    }
                    else if(productType.equalsIgnoreCase("villament"))
                    {
                        dsl.update(BLOCK)
                                .set(BLOCK.RERA_STATUS, "APPROVED")
                                .where(BLOCK.PHASE_ID.eq(id))
                                .execute();
                    }
                }
            }
        }
        if(request.getReraStatus() != null) phase.setReraStatus(request.getReraStatus());

        phase.store();

        return phase.into(Phase.class);
    }

}
