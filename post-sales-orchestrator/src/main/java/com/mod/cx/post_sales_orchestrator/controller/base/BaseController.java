package com.mod.cx.post_sales_orchestrator.controller.base;

import com.mod.cx.post_sales_orchestrator.service.base.BaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public abstract class BaseController<T, ID> {

    protected abstract BaseService<T, ID> getService();

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody T entity){
        getService().insert(entity);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<T> getById(@PathVariable ID id){
        return getService().findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<T>> getAll(){
        return ResponseEntity.ok(getService().findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable ID id, @RequestBody T entity){
        getService().update(entity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable ID id){
        getService().deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
