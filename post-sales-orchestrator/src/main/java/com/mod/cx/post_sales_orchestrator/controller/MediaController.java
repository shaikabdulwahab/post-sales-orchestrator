package com.mod.cx.post_sales_orchestrator.controller;

import com.mod.cx.post_sales_orchestrator.service.ImageKitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/media")
public class MediaController {

    private ImageKitService imageKitService;

    public MediaController(ImageKitService imageKitService){
        this.imageKitService = imageKitService;
    }

    @PostMapping("/upload/{userId}")
    public ResponseEntity<Map<String, String>> uploadDocument(
            @PathVariable String userId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("category") String category){

        try{
            return ResponseEntity.ok(imageKitService.uploadPrivateAsset(file, userId, category));
        } catch (Exception e) {
            log.error("Failed to upload document for userId: {}", userId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/url")
    public ResponseEntity<String> getUrl(@RequestParam("path") String filePath) {
        return ResponseEntity.ok(imageKitService.generateSecureUrl(filePath));
    }
}
