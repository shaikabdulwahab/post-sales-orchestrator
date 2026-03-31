package com.mod.cx.post_sales_orchestrator.controller;

import com.mod.cx.post_sales_orchestrator.controller.base.BaseController;
import com.mod.cx.post_sales_orchestrator.dto.TowerRequest;
import com.mod.cx.post_sales_orchestrator.jooq.tables.pojos.Tower;
import com.mod.cx.post_sales_orchestrator.service.TowerService;
import com.mod.cx.post_sales_orchestrator.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/towers")
@RequiredArgsConstructor
public class TowerController extends BaseController<Tower, Long> {

    private final TowerService towerService;

    @Override
    protected BaseService<Tower, Long> getService() {
        return towerService;
    }

    @PostMapping("/req")
    public ResponseEntity<?> createTower(@RequestBody TowerRequest request) {
        try{
            towerService.createTower(request);
            return ResponseEntity.ok("Tower created successfully.");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to create tower " + e.getMessage());
        }
    }

}
