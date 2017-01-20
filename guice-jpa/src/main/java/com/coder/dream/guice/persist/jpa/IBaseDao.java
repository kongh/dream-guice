package com.coder.dream.guice.persist.jpa;


import java.util.List;
import java.util.Map;

/**
 * Created by apple on 15/12/17.
 */
public interface IBaseDao<ID, T>  {


    T findOne(ID id);

    List<T> findByIds(List<ID> ids);

    Map<ID, T> findMapByIds(List<ID> ids);

    void save(T entity);

    void saveAll(List<T> entities);

    void update(T entity);

    void updateAll(List<T> entities);

    void delete(T entity);

    void deleteById(ID id);

}
