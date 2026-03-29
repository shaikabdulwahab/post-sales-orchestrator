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

    @PostMapping("/phase")
    public ResponseEntity<?> createPhase(@RequestBody PhaseRequest request) {

        try {
            phaseService.createPhase(request);
            return ResponseEntity.ok("Phase created successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to create phase " + e.getMessage());
        }

    }

    @GetMapping("/phase/{id}")
    public ResponseEntity<?> getPhase(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(phaseService.getPhase(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to get phase " + e.getMessage());
        }
    }

    @DeleteMapping("/phase/{id}")
    public ResponseEntity<?> deletePhase(@PathVariable Long id) {

        try {
            phaseService.deletePhase(id);
            return ResponseEntity.ok("Phase deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete phase " + e.getMessage());
        }
    }

    @PutMapping("/phase/{id}")
    public ResponseEntity<?> updatePhase(@PathVariable Long id, @RequestBody PhaseRequest request) {

        try {
            return ResponseEntity.ok(phaseService.updatePhase(id, request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update phase " + e.getMessage());
        }
    }

    @PatchMapping("/phase/{id}")
    public ResponseEntity<?> updatePhasePartial(@PathVariable Long id, @RequestBody PhaseRequest request) {

        try {
            return ResponseEntity.ok(phaseService.updatePhase(id, request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update phase " + e.getMessage());
        }
    }

}
