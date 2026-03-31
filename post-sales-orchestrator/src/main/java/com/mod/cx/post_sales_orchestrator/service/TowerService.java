package com.mod.cx.post_sales_orchestrator.service;

import com.mod.cx.post_sales_orchestrator.dto.TowerRequest;
import com.mod.cx.post_sales_orchestrator.jooq.tables.daos.TowerDao;
import com.mod.cx.post_sales_orchestrator.jooq.tables.pojos.Tower;
import com.mod.cx.post_sales_orchestrator.jooq.tables.records.TowerRecord;
import com.mod.cx.post_sales_orchestrator.service.base.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.jooq.DAO;
import org.jooq.DSLContext;
import org.jooq.types.UByte;
import org.jooq.types.UShort;
import org.springframework.stereotype.Service;

import static com.mod.cx.post_sales_orchestrator.jooq.Tables.TOWER;

@Service
@RequiredArgsConstructor
public class TowerService extends BaseServiceImpl<TowerRecord, Tower, Long> {

    private final TowerDao towerDao;
    private final DSLContext dsl;

    @Override
    protected DAO<TowerRecord, Tower, Long> getDao() {
        return towerDao;
    }

    public void createTower(TowerRequest request) {
        boolean exists = dsl.fetchExists(
                dsl.selectFrom(TOWER)
                        .where(TOWER.PHASE_ID.eq(request.getPhaseId()))
                        .and(TOWER.NAME.equalIgnoreCase(request.getName()))
        );

        if(exists) {
            throw new RuntimeException("Tower already exists");
        }

        TowerRecord tower = dsl.newRecord(TOWER);
        tower.setPhaseId(request.getPhaseId());
        tower.setName(request.getName());
        tower.setTotalFloors(UShort.valueOf(request.getTotalFloors()));
        tower.setMaxUnitsPerFloor(UByte.valueOf(request.getMaxUnitsPerFloor()));
        tower.setOc(request.getOc() ? (byte) 1 : (byte) 0);
        if(request.getReraStatus() != null) tower.setReraStatus(request.getReraStatus());
        tower.setConstructionStatus(request.getConstructionStatus());
        tower.setInventoryType(request.getInventoryType());
        tower.setConditions(request.getConditions());

        tower.store();
    }

    public Tower getTower(Long id){
        return dsl.selectFrom(TOWER)
                .where(TOWER.ID.eq(id))
                .fetchOptionalInto(Tower.class)
                .orElseThrow(() -> new RuntimeException("Tower not found"));
    }

    public void deleteTower(Long id){
        int deletedRows = dsl
                .deleteFrom(TOWER)
                .where(TOWER.ID.eq(id))
                .execute();

        if(deletedRows == 0) {
            throw new RuntimeException("Tower not found");
        }
    }

    public Tower updateTower(Long id, TowerRequest request){
        TowerRecord tower = dsl.selectFrom(TOWER)
                .where(TOWER.ID.eq(id))
                .fetchOptional()
                .orElseThrow(() -> new RuntimeException("Tower not found"));

        if(request.getName() != null) tower.setName(request.getName());
        if(request.getTotalFloors() != null) tower.setTotalFloors(UShort.valueOf(request.getTotalFloors()));
        if(request.getMaxUnitsPerFloor() != null) tower.setMaxUnitsPerFloor(UByte.valueOf(request.getMaxUnitsPerFloor()));
        if(request.getOc() != null) tower.setOc(request.getOc() ? (byte) 1 : (byte) 0);
        if(request.getReraStatus() != null) tower.setReraStatus(request.getReraStatus());
        if(request.getConstructionStatus() != null) tower.setConstructionStatus(request.getConstructionStatus());
        if(request.getInventoryType() != null) tower.setInventoryType(request.getInventoryType());
        if(request.getConditions() != null) tower.setConditions(request.getConditions());

        tower.store();

        return tower.into(Tower.class);
    }
}
