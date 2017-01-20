package com.coder.dream.guice.persist.jpa;

import com.coder.dream.guice.persist.jpa.util.FilterMap;
import com.coder.dream.guice.persist.jpa.util.OrderMap;
import com.coder.dream.guice.persist.jpa.util.Page;
import com.coder.dream.guice.persist.jpa.util.QueryUtils;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 所有的dao public方法只可能以如下前缀打
 * 只读事务用: find 或 count 打头
 * 写入事务用: save update delete 打头
 * 如果不按照这个约定编码将会出现连接无法释放, 事务无法提交的问题
 * @param <ID>
 * @param <T>
 */
public class BaseDao<ID, T> implements IBaseDao<ID,T> {

	protected Class<T> entityClass;
	protected Field createdAtField;
	protected Field updatedAtField;
	protected Field isDeletedField;
	
	@Inject
	protected Provider<EntityManager> entityManagerProvider;
 
	public BaseDao() {
      //  org.hibernate.jpa.HibernatePersistenceProvider
      //  org.hibernate.ejb.HibernatePersistence
        @SuppressWarnings("rawtypes")  
        Class clazz = getClass();
        while (clazz != Object.class) {  
            Type t = clazz.getGenericSuperclass();  
            if (t instanceof ParameterizedType) {  
                Type[] args = ((ParameterizedType) t).getActualTypeArguments();  
                if (args[0] instanceof Class) {
                	this.entityClass = (Class<T>) args[1];
					try {
						createdAtField = entityClass.getDeclaredField("createdAt");
	                	createdAtField.setAccessible(true);
					} catch (NoSuchFieldException | SecurityException e) {
						;
					}
					try {
						updatedAtField = entityClass.getDeclaredField("updatedAt");
						updatedAtField.setAccessible(true);
					} catch (NoSuchFieldException | SecurityException e) {
						;
					}
					try {
						isDeletedField = entityClass.getDeclaredField("isDeleted");
						isDeletedField.setAccessible(true);
					} catch (NoSuchFieldException | SecurityException e) {
						;
					}
                    break;
                }
            }
            clazz = clazz.getSuperclass();
        }
        
	}

	private void refreshUpdatedAt(T entity) {
		if (updatedAtField != null) {
        	try {
        		updatedAtField.set(entity, System.currentTimeMillis());
			} catch (IllegalArgumentException | IllegalAccessException e) {
				;
			}
        }
	}

    private void refreshCreatedAt(T entity) {
		if (createdAtField != null) {
			try {
				createdAtField.set(entity, System.currentTimeMillis());
			} catch (IllegalArgumentException | IllegalAccessException e) {
				;
			}
        }
	}
	
	private void refreshIsDeleted(T entity, boolean isDeleted) {
		if (isDeletedField != null) {
			try {
				isDeletedField.set(entity, isDeleted);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				;
			}
        }
	}

    public List<T> findByIds(List<ID> ids) {
        Map<ID, T> entityMap = this.innerFindMapByIds(ids);
        List<T> entities = new ArrayList<T>(ids.size());
        for (ID id : ids) {
            T user = entityMap.get(id);
            if (user != null) {
            	entities.add(user);
            }
        }
        return entities;
    }

    public T findOne(ID id) {
        EntityManager manager = entityManagerProvider.get();
        return manager.find(entityClass, id);
    }

    private Map<ID, T> innerFindMapByIds(List<ID> ids) {
        EntityManager manager = entityManagerProvider.get();
        Query query = manager.createQuery(String.format("select x from %s x where id in :ids", entityClass.getSimpleName()));

        List<T> entities = (List<T>)query.setParameter("ids", ids).getResultList();
        if (entities.size() == 0) return new HashMap<ID, T>(0);
        Map<ID, T> entityMap = new HashMap<ID, T>(entities.size());
        for (T entity : entities) {
            entityMap.put((ID) getProperty(entity, "id"), entity);
        }
        return entityMap;
    }

    private Object getProperty(Object entity, String property){
	    if(entity == null){
	        return null;
        }

        try {
            return BeanUtils.getProperty(entity, "id");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<ID, T> findMapByIds(List<ID> ids) {
        return this.innerFindMapByIds(ids);
    }

    @Override
    public void save(T entity) {
        EntityManager manager = entityManagerProvider.get();
        refreshCreatedAt(entity);
        refreshUpdatedAt(entity);
        manager.persist(entity);
        manager.flush();
    }
    @Override
    public void saveAll(List<T> entities) {
        EntityManager manager = entityManagerProvider.get();
        int size = entities.size();
        for (int i=0; i<size; i++){
            T entity = entities.get(i);
            refreshCreatedAt(entity);
            refreshUpdatedAt(entity);
            manager.persist(entity);
            if (i % 30 == 0){
                manager.flush();
                manager.clear();
            }
        }
    }

    public void update(T entity) {
        EntityManager manager = entityManagerProvider.get();
        refreshUpdatedAt(entity);
        manager.merge(entity);
        manager.flush();
    }

    public void updateAll(List<T> entities) {
        EntityManager manager = entityManagerProvider.get();
        for (int i = 0, size = entities.size(); i < size; i++) {
            T entity = entities.get(i);
            refreshUpdatedAt(entity);
            manager.merge(entity);
            if (i % 30 == 0) {
                manager.flush();
                manager.clear();
            }
        }
    }

    private void innerDelete(T entity) {
        if (entity == null) return;
        EntityManager manager = entityManagerProvider.get();
        if (isDeletedField!=null) {
            refreshUpdatedAt(entity);
            this.refreshIsDeleted(entity, true);
            manager.merge(entity);
            manager.flush();
        } else {
            manager.remove(manager.contains(entity) ? entity : manager.merge(entity));
            manager.flush();
        }
    }
    public void delete(T entity) {
        this.innerDelete(entity);
    }



    public void deleteById(ID id) {
    	if (id == null) return;
        T entity = this.findOne(id);
        this.innerDelete(entity);
    }

    /**
     * 查找一个
     * @param filterMap
     * @return
     */
    protected T findOne(FilterMap filterMap){
        TypedQuery<T> query = getQuery(filterMap,null);
        return getSingleResult(query);
    }

    /**
     * 查找一个
     * @param filterMap
     * @return
     */
    protected T findOne(FilterMap filterMap, OrderMap orderMap){
        TypedQuery<T> query = getQuery(filterMap,orderMap);
        return getSingleResult(query);
    }



    /**
     * 列表
     * @param filterMap
     * @return
     */
    protected List<T> list(FilterMap filterMap) {
        return list(filterMap,null);
    }



    /**
     * 列表
     * @param filterMap
     * @param orderMap
     * @return
     */
    protected List<T> list(FilterMap filterMap,OrderMap orderMap) {
        return list(filterMap,orderMap,null,null);
    }



    /**
     *
     * @param filterMap
     * @param offset
     * @param size
     * @return
     */
    protected List<T> list(FilterMap filterMap,Integer offset,Integer size) {
        return list(filterMap,null,offset,size);
    }

    /**
     * 列表
     * @param filterMap
     * @param orderMap
     * @param offset
     * @param size
     * @return
     */
    protected List<T> list(FilterMap filterMap, OrderMap orderMap, Integer offset, Integer size) {
        TypedQuery<T> query = getQuery(filterMap, orderMap);
        if(offset != null && size != null){
            Preconditions.checkNotNull(offset, "偏移量不能为null");
            Preconditions.checkState(offset >= 0, "偏移量必须大于等于0");
            Preconditions.checkNotNull(size,"每页记录大小不能为null");
            Preconditions.checkState(size > 0,"每页记录大小必须大于0");

            query.setFirstResult(offset);
            query.setMaxResults(size);
        }
        return query.getResultList();
    }


    /**
     * 分页列表
     * @param filterMap
     * @param offset 偏移量（从0开始）
     * @param size (每页大小)
     * @return
     */
    protected Page<T> page(FilterMap filterMap, Integer offset, Integer size){
        return page(filterMap,null,offset,size);
    }



    /**
     * 分页列表
     * @param filterMap
     * @param orderMap
     * @param offset 偏移量（从0开始）
     * @param size (每页大小)
     * @return
     */
    protected Page<T> page(FilterMap filterMap, OrderMap orderMap, Integer offset, Integer size){
        Preconditions.checkNotNull(offset, "偏移量不能为null");
        Preconditions.checkState(offset >= 0, "偏移量必须大于等于0");
        Preconditions.checkNotNull(size,"每页记录大小不能为null");
        Preconditions.checkState(size > 0,"每页记录大小必须大于0");
        Long total = count(filterMap);
        if(total == 0){
            return Page.build(new ArrayList<T>(),0L);
        }
        TypedQuery<T> query = getQuery(filterMap,orderMap);
        query.setFirstResult(offset);
        query.setMaxResults(size);
        List<T> list = query.getResultList();
        return Page.build(list,total);
    }


    protected Long count(FilterMap filterMap) {
        TypedQuery<Long> countQuery = getCountQuery(filterMap);
        Long longValue = getSingleResult(countQuery);
        return longValue == null ? 0L : longValue;
    }

    protected EntityManager getEntityManager(){
        return entityManagerProvider.get();
    }

    private TypedQuery<Long> getCountQuery(FilterMap filterMap) {
        EntityManager entityManager = getEntityManager();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        //
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<?> root = query.from(entityClass);
        Predicate predicate = QueryUtils.buildPredicates(builder, root, filterMap);
        if(predicate != null){
            query.where(predicate);
        }else {
            query.where(builder.conjunction());
        }

        CriteriaQuery<Long> select = query.select(builder.count(root));
        return entityManager.createQuery(select);
    }

    protected TypedQuery<T> getQuery(FilterMap filterMap,OrderMap orderMap) {
        EntityManager entityManager = getEntityManager();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        Predicate predicate = QueryUtils.buildPredicates(builder, root, filterMap);
        if(predicate != null){
            query.where(predicate);
        }else {
            query.where(builder.conjunction());
        }

        query.select(root);
        List<Order> orders = QueryUtils.buildOrders(builder, root, orderMap);
        if(CollectionUtils.isNotEmpty(orders)) {
            query.orderBy(orders);
        }
        return entityManager.createQuery(query);
    }

    protected Integer delete(FilterMap filterMap){
        Query query = getDeleteQuery(filterMap);
        return query.executeUpdate();
    }

    protected Query getDeleteQuery(FilterMap filterMap){
        EntityManager entityManager = getEntityManager();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaDelete<T> query = builder.createCriteriaDelete(entityClass);
        Root<T> root = query.from(entityClass);
        Predicate predicate = QueryUtils.buildPredicates(builder, root, filterMap);
        if(predicate != null){
            query.where(predicate);
        }else {
            query.where(builder.conjunction());
        }

        return entityManager.createQuery(query);
    }

    protected <T> T getSingleResult(Query query) {
        List<T> list = query.getResultList();
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }
}