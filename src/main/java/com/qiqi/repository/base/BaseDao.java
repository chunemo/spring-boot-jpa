package com.qiqi.repository.base;

import java.util.List;
import java.util.Map;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public interface BaseDao {
	/**
	 * 立即加载
	 *
	 * @author qc_zhong
	 * @param clazz
	 * @param id
	 * @return
	 */
	<T> T find(Class<T> clazz, Long id);
	
	/**
	 * 立即加载
	 *
	 * @author qc_zhong
	 * @param clazz
	 * @param example
	 * @return
	 */
	<T> T find(Class<T> clazz, Example<T> example);
	
	/**
	 * 立即加载
	 *
	 * @author qc_zhong
	 * @param clazz
	 * @param example
	 * @param selections
	 * @return
	 */
	<T> T find(Class<T> clazz, Example<T> example, Selections<T> selections);
	
	/**
	 * 立即加载
	 *
	 * @author qc_zhong
	 * @param clazz
	 * @param spec
	 * @return
	 */
	<T> T find(Class<T> clazz, Specification<T> spec);
	
	/**
	 * 立即加载
	 *
	 * @author qc_zhong
	 * @param clazz
	 * @param spec
	 * @param selections
	 * @return
	 */
	<T> T find(Class<T> clazz, Specification<T> spec, Selections<T> selections);

	/**
	 * 立即加载
	 *
	 * @author qc_zhong
	 * @param clazz
	 * @param id
	 * @param lockMode
	 * @return
	 */
	<T> T find(Class<T> clazz, Long id, LockModeType lockMode);

	/**
	 * 延迟加载
	 *
	 * @author qc_zhong
	 * @param clazz
	 * @param id
	 * @return
	 */
	<T> T getReference(Class<T> clazz, Long id);

	/**
	 * 加载列表
	 *
	 * @author qc_zhong
	 * @param clazz
	 * @return
	 */
	<T> List<T> findAll(Class<T> clazz);
	
	/**
	 * 加载列表
	 *
	 * @author qc_zhong
	 * @param clazz
	 * @param selections
	 * @return
	 */
	<T> List<T> findAll(Class<T> clazz, Selections<T> selections);

	/**
	 * 加载列表并排序
	 *
	 * @author qc_zhong
	 * @param clazz
	 * @param sort
	 * @return
	 */
	<T> List<T> findAll(Class<T> clazz, Sort sort);
	
	/**
	 * 加载列表并排序
	 *
	 * @author qc_zhong
	 * @param clazz
	 * @param selections
	 * @param sort
	 * @return
	 */
	<T> List<T> findAll(Class<T> clazz, Selections<T> selections, Sort sort);

	/**
	 * 根据查询条件加载列表, 不支持组合主键
	 * 
	 * @author qc_zhong(钟其纯)
	 * @param example
	 * @return
	 */
	<T> List<T> findAll(Example<T> example);
	
	/**
	 * 根据查询条件加载列表
	 *
	 * @author qc_zhong
	 * @param example
	 * @param selections
	 * @return
	 */
	<T> List<T> findAll(Example<T> example, Selections<T> selections);

	/**
	 * 根据查询条件加载列表
	 *
	 * @author qc_zhong
	 * @param example
	 * @param sort
	 * @return
	 */
	<T> List<T> findAll(Example<T> example, Sort sort);
	
	/**
	 * 根据查询条件加载列表
	 *
	 * @author qc_zhong
	 * @param example
	 * @param selections
	 * @param sort
	 * @return
	 */
	<T> List<T> findAll(Example<T> example, Selections<T> selections, Sort sort);
	
	/**
	 * 根据查询条件加载列表
	 *
	 * @author qc_zhong
	 * @param clazz
	 * @param spec
	 * @return
	 */
	<T> List<T> findAll(Class<T> clazz, Specification<T> spec);
	
	/**
	 * 根据查询条件加载列表
	 *
	 * @author qc_zhong
	 * @param clazz TODO
	 * @param spec
	 * @param sort
	 * @return
	 */
	<T> List<T> findAll(Class<T> clazz, Specification<T> spec, Sort sort);
	
	/**
	 * 根据查询条件加载列表
	 *
	 * @author qc_zhong
	 * @param clazz
	 * @param spec
	 * @param selections
	 * @return
	 */
	<T> List<T> findAll(Class<T> clazz, Specification<T> spec, Selections<T> selections);
	
	/**
	 * 根据查询条件加载列表
	 *
	 * @author qc_zhong
	 * @param clazz
	 * @param spec
	 * @param selections
	 * @param sort
	 * @return
	 */
	<T> List<T> findAll(Class<T> clazz, Specification<T> spec, Selections<T> selections, Sort sort);

	/**
	 * 持久化新建态实体
	 *
	 * @author qc_zhong
	 * @param entity
	 */
	<T> void persist(T entity);

	/**
	 * 持久化游离态实体
	 *
	 * @author qc_zhong
	 * @param entity
	 * @return
	 */
	<T> T merge(T entity);

	/**
	 * 保存
	 * 
	 * @author qc_zhong(钟其纯)
	 * @param entity
	 * @return
	 */
	<T> T save(Class<T> clazz, T entity);

	/**
	 * 删除持久化实体
	 *
	 * @author qc_zhong
	 * @param entity
	 * @return
	 */
	<T> void remove(T entity);

	/**
	 * 删除实体
	 *
	 * @author qc_zhong
	 * @param entity
	 */
	<T> void delete(T entity);

	/**
	 * 脱管
	 *
	 * @author qc_zhong
	 * @param entity
	 */
	<T> void detach(T entity);

	/**
	 * 刷新
	 *
	 * @author qc_zhong
	 * @param entity
	 */
	<T> void refresh(T entity);

	/**
	 * 刷新
	 *
	 * @author qc_zhong
	 * @param entity
	 * @param lockMode
	 */
	<T> void refresh(T entity, LockModeType lockMode);

	/**
	 * 刷新
	 *
	 * @author qc_zhong
	 * @param entity
	 * @param properties
	 */
	<T> void refresh(T entity, Map<String, Object> properties);

	/**
	 * 刷新
	 *
	 * @author qc_zhong
	 * @param entity
	 * @param lockMode
	 * @param properties
	 */
	<T> void refresh(T entity, LockModeType lockMode, Map<String, Object> properties);

	/**
	 * 加锁
	 *
	 * @author qc_zhong
	 * @param entity
	 * @param lockMode
	 */
	<T> void lock(T entity, LockModeType lockMode);

	/**
	 * 获取实体的锁模式
	 *
	 * @author qc_zhong
	 * @param entity
	 * @return
	 */
	<T> LockModeType getLockMode(T entity);
	
	/**
	 * 统计
	 *
	 * @author qc_zhong
	 * @param clazz
	 * @param example
	 * @return
	 */
	<T> Long count(Class<T> clazz, Example<T> example);
	
	/**
	 * 统计
	 *
	 * @author qc_zhong
	 * @param clazz
	 * @param spec
	 * @return
	 */
	<T> Long count(Class<T> clazz, Specification<T> spec);

	/**
	 * 是否存在
	 *
	 * @author qc_zhong
	 * @param clazz
	 *            TODO
	 * @param id
	 * @return
	 */
	<T> boolean exists(Class<T> clazz, Long id);

	/**
	 * 是否存在
	 *
	 * @author qc_zhong
	 * @param clazz
	 *            TODO
	 * @param example
	 * @return
	 */
	<T> boolean exists(Class<T> clazz, Example<T> example);
	
	/**
	 * 是否存在
	 *
	 * @author qc_zhong
	 * @param clazz
	 * @param spec
	 * @return
	 */
	<T> boolean exists(Class<T> clazz, Specification<T> spec);

	/**
	 * flush
	 *
	 * @author qc_zhong
	 */
	void flush();
	
	/**
	 * 更新
	 *
	 * @author qc_zhong
	 * @param clazz
	 * @param id
	 * @param entity
	 * @return
	 */
	<T> int update(Class<T> clazz, Long id, T entity);
	
	/**
	 * jpql查询
	 * 
	 * @author qc_zhong
	 * @param jpql
	 * @param args
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List getList(String jpql, Object... args);
	
	/**
	 * sql查询
	 *
	 * @author qc_zhong
	 * @param sql
	 * @param resultHandler
	 * @param args
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List getList(String sql, ResultHandler resultHandler, Object... args);

	/**
	 * 指定分页条件的jpql查询
	 * 
	 * @author qc_zhong
	 * @param jpql
	 * @param startPosition
	 * @param maxResult
	 * @param args
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List getList(String jpql, int startPosition, int maxResult, Object... args);
	
	/**
	 * 指定分页条件的sql查询
	 *
	 * @author qc_zhong
	 * @param sql
	 * @param startPosition
	 * @param maxResult
	 * @param resultHandler
	 * @param args
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List getList(String sql, int startPosition, int maxResult, ResultHandler resultHandler, Object... args);

	/**
	 * jqpl分页查询
	 * 
	 * @author qc_zhong
	 * @param pageBean
	 * @param args
	 */
	@SuppressWarnings("rawtypes")
	public void queryByPage(PageBean pageBean, Object... args);
	
	/**
	 * sql分页查询
	 *
	 * @author qc_zhong
	 * @param pageBean
	 * @param resultHandler
	 * @param args
	 */
	@SuppressWarnings("rawtypes")
	public void queryByPage(PageBean pageBean, ResultHandler resultHandler, Object... args);
}
