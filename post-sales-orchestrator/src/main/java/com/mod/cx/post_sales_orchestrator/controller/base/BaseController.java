package com.mod.cx.post_sales_orchestrator.controller.base;

import com.mod.cx.post_sales_orchestrator.service.base.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public abstract class BaseController<T, ID> {

    protected abstract BaseService<T, ID> getService();

    @PostMapping
    public ResponseEntity<T> create(@RequestBody T entity){
        return new ResponseEntity<>(getService().save(entity), HttpStatus.CREATED);
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

    @GetMapping("/page")
    public ResponseEntity<Page<T>> getAllPaginated(Pageable pageable){
        return ResponseEntity.ok(getService().findAll(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<T> update(@PathVariable ID id, @RequestBody T entity){
        return ResponseEntity.ok(getService().save(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable ID id){
        getService().deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
