package com.mod.cx.post_sales_orchestrator.service;

import com.mod.cx.post_sales_orchestrator.dto.PhaseRequest;
import com.mod.cx.post_sales_orchestrator.jooq.tables.daos.PhaseDao;
import com.mod.cx.post_sales_orchestrator.jooq.tables.pojos.Phase;
import com.mod.cx.post_sales_orchestrator.jooq.tables.records.PhaseRecord;
import com.mod.cx.post_sales_orchestrator.service.base.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.jooq.DAO;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import static com.mod.cx.post_sales_orchestrator.jooq.Tables.PHASE;

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

    public void deletePhase(Long id) {
        int deletedRows = dsl.deleteFrom(PHASE)
                .where(PHASE.ID.eq(id))
                .execute();

        if (deletedRows == 0) {
            throw new RuntimeException("Phase not found");
        }
    }

    public Phase updatePhase(Long id, PhaseRequest request) {
        PhaseRecord phase = dsl.selectFrom(PHASE)
                .where(PHASE.ID.eq(id))
                .fetchOptional()
                .orElseThrow(() -> new RuntimeException("Phase not found"));

        if(request.getName() != null) phase.setName(request.getName());
        if(request.getReraType() != null) phase.setReraType(request.getReraType());
        if(request.getOc() != null) phase.setOc(request.getOc());
        if(request.getReraStatus() != null) phase.setReraStatus(request.getReraStatus());

        phase.store();

        return phase.into(Phase.class);
    }

}
