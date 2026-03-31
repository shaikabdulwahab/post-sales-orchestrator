package com.mod.cx.post_sales_orchestrator.service;

import com.mod.cx.post_sales_orchestrator.dto.BlockRequest;
import com.mod.cx.post_sales_orchestrator.jooq.tables.daos.BlockDao;
import com.mod.cx.post_sales_orchestrator.jooq.tables.pojos.Block;
import com.mod.cx.post_sales_orchestrator.jooq.tables.records.BlockRecord;
import com.mod.cx.post_sales_orchestrator.service.base.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.jooq.DAO;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import static com.mod.cx.post_sales_orchestrator.jooq.Tables.*;

@Service
@RequiredArgsConstructor
public class BlockService extends BaseServiceImpl<BlockRecord, Block, Long> {

    private final BlockDao blockDao;
    private final DSLContext dsl;

    @Override
    protected DAO<BlockRecord, Block, Long> getDao() {
        return blockDao;
    }

    public void createBlock(BlockRequest request){
        boolean exists = dsl.fetchExists(
                dsl.selectFrom(BLOCK)
                        .where(BLOCK.NAME.eq(request.getName()))
                        .and(BLOCK.PHASE_ID.eq(request.getPhaseId()))
        );

        if(exists){
            throw new RuntimeException("Block already exists");
        }

        String phaseReraType = dsl.selectFrom(PHASE)
                .where(PHASE.ID.eq(request.getPhaseId()))
                .fetchOptional()
                .orElseThrow(() -> new RuntimeException("Phase not found"))
                .getReraType();

        BlockRecord block = dsl.newRecord(BLOCK);
        block.setPhaseId(request.getPhaseId());
        block.setName(request.getName());
        block.setOc(request.getOc() ? (byte) 1 : (byte) 0);
        if(phaseReraType.equalsIgnoreCase("phasewise")){
            String phaseReraStatus = dsl.selectFrom(PHASE)
                    .where(PHASE.ID.eq(request.getPhaseId()))
                    .fetchOptional()
                    .orElseThrow(() -> new RuntimeException("Phase not found"))
                    .getReraStatus();
            if(phaseReraStatus.equalsIgnoreCase("approved"))
                block.setReraStatus("approved");
        }
        else {
            block.setReraStatus(String.valueOf(request.getReraStatus()));
        }
        block.setConstructionStatus(String.valueOf(request.getConstructionStatus()));
        block.setInventoryType(request.getInventoryType());
        block.setConditions(request.getConditions());
        block.store();
    }

    public Block getBlock(Long id){
        return dsl.selectFrom(BLOCK)
                .where(BLOCK.ID.eq(id))
                .fetchOptionalInto(Block.class)
                .orElseThrow(() -> new RuntimeException("Block not found"));
    }

    public void deleteBlock(Long id){
        int deletedRows = dsl
                .deleteFrom(BLOCK)
                .where(BLOCK.ID.eq(id))
                .execute();

        if(deletedRows == 0) {
            throw new RuntimeException("Tower not found");
        }
    }

    public Block updateBlock(Long id, BlockRequest request){
        BlockRecord block = dsl.selectFrom(BLOCK)
                .where(BLOCK.ID.eq(id))
                .fetchOptional()
                .orElseThrow(() -> new RuntimeException("Block not found"));

        String phaseReraStatus = dsl.selectFrom(PHASE)
                .where(PHASE.ID.eq(block.getPhaseId()))
                .fetchOptional()
                .orElseThrow(() -> new RuntimeException("Phase not found"))
                .getReraStatus();


        if(request.getName() != null) block.setName(request.getName());
        if(request.getOc() != null) block.setOc(request.getOc() ? (byte) 1 : (byte) 0);
        if(request.getReraStatus() != null){
            if(phaseReraStatus.equalsIgnoreCase("approved"))
                block.setReraStatus("approved");
            else
                block.setReraStatus(String.valueOf(request.getReraStatus()));
        }
        if(request.getConstructionStatus() != null) block.setConstructionStatus(String.valueOf(request.getConstructionStatus()));
        if(request.getInventoryType() != null) block.setInventoryType(request.getInventoryType());
        if(request.getConditions() != null) block.setConditions(request.getConditions());

        block.store();

        return block.into(Block.class);
    }
}
