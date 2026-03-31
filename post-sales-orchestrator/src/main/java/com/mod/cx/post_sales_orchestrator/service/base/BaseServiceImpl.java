package com.mod.cx.post_sales_orchestrator.service.base;

import org.jooq.DAO;
import org.jooq.TableRecord;

import java.util.List;
import java.util.Optional;

public abstract class BaseServiceImpl<R extends TableRecord<R>, P, ID> implements BaseService<P, ID> {

    protected abstract DAO<R, P, ID> getDao();

    @Override
    public void create(P entity) {
        getDao().insert(entity);
    }

    @Override
    public void update(P entity) {
        getDao().update(entity);
    }

    @Override
    public Optional<P> findById(ID id) {
        return Optional.ofNullable(getDao().findById(id));
    }

    @Override
    public List<P> findAll() {
        return getDao().findAll();
    }

    @Override
    public void deleteById(ID id) {
        getDao().deleteById(id);
    }
}
