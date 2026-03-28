package com.mod.cx.post_sales_orchestrator.controller;

import com.mod.cx.post_sales_orchestrator.controller.base.BaseController;

import com.mod.cx.post_sales_orchestrator.dto.PhaseRequest;
import com.mod.cx.post_sales_orchestrator.dto.ProjectDTO;
import com.mod.cx.post_sales_orchestrator.jooq.tables.pojos.Project;
import com.mod.cx.post_sales_orchestrator.service.ImageKitService;
import com.mod.cx.post_sales_orchestrator.service.ProjectService;
import com.mod.cx.post_sales_orchestrator.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController extends BaseController<Project, Long> {

    private final ProjectService projectService;
    private final ImageKitService imageKitService; // Added for the sample endpoint

    @Override
    protected BaseService<Project, Long> getService() {
        return projectService;
    }

    @PostMapping("/phase")
    public ResponseEntity<?> createPhase(@RequestBody PhaseRequest request) {

        try {
            projectService.createPhase(request);
            return ResponseEntity.ok("Phase created successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to create phase " + e.getMessage());
        }

    }

    @GetMapping("/phase/{id}")
    public ResponseEntity<?> getPhase(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(projectService.getPhase(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to get phase " + e.getMessage());
        }
    }

    @GetMapping("/{projectId}/phases")
    public  ResponseEntity<?> getProjectPhases(
            @PathVariable Long projectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        try {
            return ResponseEntity.ok(projectService.getProjectPhases(projectId, page, size));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to get project phases " + e.getMessage());
        }
    }

    @DeleteMapping("/phase/{id}")
    public ResponseEntity<?> deletePhase(@PathVariable Long id) {

        try {
            projectService.deletePhase(id);
            return ResponseEntity.ok("Phase deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete phase " + e.getMessage());
        }
    }

    @PutMapping("/phase/{id}")
    public ResponseEntity<?> updatePhase(@PathVariable Long id, @RequestBody PhaseRequest request) {

        try {
            return ResponseEntity.ok(projectService.updatePhase(id, request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update phase " + e.getMessage());
        }
    }

    @PatchMapping("/phase/{id}")
    public ResponseEntity<?> updatePhasePartial(@PathVariable Long id, @RequestBody PhaseRequest request) {

        try {
            return ResponseEntity.ok(projectService.updatePhase(id, request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update phase " + e.getMessage());
        }
    }

    // Update to accept both JSON body for basic info and MultiPartFiles for logos
    @PostMapping(value = "/req", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createRequest(
            @RequestPart("projectData") ProjectDTO projectDTO,
            @RequestPart(value = "bannerLogo", required = false) MultipartFile bannerLogo,
            @RequestPart(value = "builderLogo", required = false) MultipartFile builderLogo,
            @RequestPart(value = "strategicPartnerLogo", required = false) MultipartFile strategicPartnerLogo,
            @RequestPart(value = "projectLogo", required = false) MultipartFile projectLogo) {
        
        try {
            Project savedProject = projectService.createProject(projectDTO, bannerLogo, builderLogo, strategicPartnerLogo, projectLogo);
            return ResponseEntity.ok(savedProject);
        } catch (Exception e) {
             return ResponseEntity.badRequest().body("Failed to process request: " + e.getMessage());
        }
    }
}