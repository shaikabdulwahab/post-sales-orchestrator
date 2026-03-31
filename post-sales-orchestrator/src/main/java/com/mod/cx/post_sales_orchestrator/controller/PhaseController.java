package com.mod.cx.post_sales_orchestrator.controller;

import com.mod.cx.post_sales_orchestrator.controller.base.BaseController;
import com.mod.cx.post_sales_orchestrator.dto.PhaseRequest;
import com.mod.cx.post_sales_orchestrator.jooq.tables.pojos.Phase;
import com.mod.cx.post_sales_orchestrator.service.PhaseService;
import com.mod.cx.post_sales_orchestrator.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/Phase")
@RequiredArgsConstructor
public class PhaseController extends BaseController<Phase, Long> {

    private final PhaseService phaseService;


    @Override
    protected BaseService<Phase, Long> getService() {
        return phaseService;
    }

    @PostMapping("/req")
    public ResponseEntity<?> createPhase(@RequestBody PhaseRequest request) {

        try {
            phaseService.createPhase(request);
            return ResponseEntity.ok("Phase created successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to create phase " + e.getMessage());
        }

    }

    @GetMapping("/{phaseId}/towers")
    public ResponseEntity<?> getPhaseTowers(@PathVariable Long phaseId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){

        try {
            return ResponseEntity.ok(phaseService.getPhaseTowers(phaseId, page, size));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to get phase towers " + e.getMessage());
        }
    }

    @GetMapping("/{phaseId}/blocks")
    public ResponseEntity<?> getPhaseBlocks(@PathVariable Long phaseId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){

        try {
            return ResponseEntity.ok(phaseService.getPhaseBlocks(phaseId, page, size));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to get phase blocks " + e.getMessage());
        }
    }

}
