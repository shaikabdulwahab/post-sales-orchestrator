package com.mod.cx.post_sales_orchestrator.service.base;

import java.util.List;
import java.util.Optional;

public interface BaseService<T, ID> {

    void insert(T entity);

    void update(T entity);

    Optional<T> findById(ID id);

    List<T> findAll();

    void deleteById(ID id);
}
