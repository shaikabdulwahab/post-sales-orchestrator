package com.mod.cx.post_sales_orchestrator.controller;

import com.mod.cx.post_sales_orchestrator.controller.base.BaseController;
import com.mod.cx.post_sales_orchestrator.dto.BlockRequest;
import com.mod.cx.post_sales_orchestrator.jooq.tables.pojos.Block;
import com.mod.cx.post_sales_orchestrator.service.BlockService;
import com.mod.cx.post_sales_orchestrator.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;;

@RestController
@RequestMapping("/api/v1/blocks")
@RequiredArgsConstructor
public class BlockController extends BaseController<Block, Long> {

    private final BlockService blockService;

    @Override
    protected BaseService<Block, Long> getService() {
        return blockService;
    }

    @PostMapping("/req")
    public ResponseEntity<?> createBlock(@RequestBody BlockRequest request) {
        try{
            blockService.createBlock(request);
            return ResponseEntity.ok("Block created successfully.");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to create block " + e.getMessage());
        }
    }
}
