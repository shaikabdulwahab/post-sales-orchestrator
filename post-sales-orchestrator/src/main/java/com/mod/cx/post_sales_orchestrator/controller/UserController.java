package com.mod.cx.post_sales_orchestrator.controller;

import com.mod.cx.post_sales_orchestrator.controller.base.BaseController;
import com.mod.cx.post_sales_orchestrator.dto.UpdateUserPassRequest;
import com.mod.cx.post_sales_orchestrator.dto.UserRequest;
import com.mod.cx.post_sales_orchestrator.jooq.tables.pojos.Users;
import com.mod.cx.post_sales_orchestrator.service.UserService;
import com.mod.cx.post_sales_orchestrator.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController extends BaseController<Users, Long> {
    
    private final UserService userService;
    
    @Override
    protected BaseService<Users, Long> getService() {
        return userService;
    }
    
    @GetMapping("/{clientId}/users")
    public ResponseEntity<?> getClientUsers(
            @PathVariable Long clientId, 
            @RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "10") int size) {
        try {
            return ResponseEntity.ok(userService.getClientUsers(clientId, page, size));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to get client users " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}/activate")
    public ResponseEntity<?> makeUserActive(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.makeUserActive(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to make user active " + e.getMessage());
        }
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<?> makeUserInActive(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.makeUserInActive(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to make user inactive " + e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserRequest request) {
        try {
            return ResponseEntity.ok(userService.updateUser(id, request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update user " + e.getMessage());
        }
    }
    
    @PatchMapping("/{id}/password")
    public ResponseEntity<?> updatePassword(@PathVariable Long id, @RequestBody UpdateUserPassRequest request) {
        try {
            userService.updatePassword(id, request);
            return ResponseEntity.ok("Password updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update password " + e.getMessage());
        }
    }
    
}
