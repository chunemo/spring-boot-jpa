package com.qiqi.service.base.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.LockModeType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import com.qiqi.repository.base.BaseDao;
import com.qiqi.repository.base.Selections;
import com.qiqi.service.base.BaseService;

@Transactional
public class BaseServiceImpl<T> implements BaseService<T> {
	@Autowired
	@Qualifier("baseDao")
	private BaseDao baseDao;

	@SuppressWarnings("rawtypes")
	private Class clazz;

	@SuppressWarnings({ "unchecked" })
	private <S extends T> Class<S> getClazz() {
		if (this.clazz == null) {
			Type genType = this.getClass().getGenericSuperclass();
			if (!(genType instanceof ParameterizedType)) {
				return null;
			}
			Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
			if (!(params[0] instanceof Class)) {
				return null;
			}
			return (Class<S>) params[0];
		}
		return this.clazz;
	}

	/**
	 * 立即加载
	 *
	 * @author qc_zhong
	 * @param id
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public T find(Long id) {
		if (getClazz() == null) {
			return null;
		}
		return baseDao.find(getClazz(), id);
	}
	
	/**
	 * 立即加载
	 *
	 * @author qc_zhong
	 * @param example
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public T find(Example<T> example) {
		if (getClazz() == null) {
			return null;
		}
		return baseDao.find(getClazz(), example);
	}
	
	/**
	 * 立即加载
	 *
	 * @author qc_zhong
	 * @param example
	 * @param selections
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public T find(Example<T> example, Selections<T> selections) {
		if (getClazz() == null) {
			return null;
		}
		return baseDao.find(getClazz(), example, selections);
	}
	
	/**
	 * 立即加载
	 *
	 * @author qc_zhong
	 * @param entity
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public T find(T entity) {
		return find(Example.of(entity));
	}
	
	/**
	 * 立即加载
	 *
	 * @author qc_zhong
	 * @param entity
	 * @param selections
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public T find(T entity, Selections<T> selections) {
		return find(Example.of(entity), selections);
	}
	
	/**
	 * 立即加载
	 *
	 * @author qc_zhong
	 * @param spec
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public T find(Specification<T> spec) {
		if (getClazz() == null) {
			return null;
		}
		return baseDao.find(getClazz(), spec);
	}
	
	/**
	 * 立即加载
	 *
	 * @author qc_zhong
	 * @param spec
	 * @param selections
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public T find(Specification<T> spec, Selections<T> selections) {
		if (getClazz() == null) {
			return null;
		}
		return baseDao.find(getClazz(), spec, selections);
	}

	/**
	 * 立即加载
	 *
	 * @author qc_zhong
	 * @param id
	 * @param lockMode
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public T find(Long id, LockModeType lockMode) {
		if (getClazz() == null) {
			return null;
		}
		return baseDao.find(getClazz(), id, lockMode);
	}

	/**
	 * 延迟加载
	 *
	 * @author qc_zhong
	 * @param id
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public T getReference(Long id) {
		if (getClazz() == null) {
			return null;
		}
		return baseDao.getReference(getClazz(), id);
	}

	/**
	 * 加载列表
	 *
	 * @author qc_zhong
	 * @param clazz
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public List<T> findAll() {
		if (getClazz() == null) {
			return null;
		}
		return baseDao.findAll(getClazz());
	}
	
	/**
	 * 加载列表
	 *
	 * @author qc_zhong
	 * @param selections
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public List<T> findAll(Selections<T> selections) {
		if (getClazz() == null) {
			return null;
		}
		return baseDao.findAll(getClazz(), selections);
	}

	/**
	 * 加载列表
	 *
	 * @author qc_zhong
	 * @param sort
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public List<T> findAll(Sort sort) {
		if (getClazz() == null) {
			return null;
		}
		return baseDao.findAll(getClazz(), sort);
	}
	
	/**
	 * 加载列表
	 *
	 * @author qc_zhong
	 * @param selections
	 * @param sort
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public List<T> findAll(Selections<T> selections, Sort sort) {
		if (getClazz() == null) {
			return null;
		}
		return baseDao.findAll(getClazz(), selections, sort);
	}

	/**
	 * 根据查询条件加载列表
	 * 
	 * @author qc_zhong(钟其纯)
	 * @param example
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public <S extends T> List<S> findAll(Example<S> example) {
		return baseDao.findAll(example);
	}
	
	/**
	 * 根据查询条件加载列表
	 *
	 * @author qc_zhong
	 * @param example
	 * @param selections
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public <S extends T> List<S> findAll(Example<S> example, Selections<S> selections) {
		return baseDao.findAll(example, selections);
	}

	/**
	 * 根据查询条件加载列表并排序
	 *
	 * @author qc_zhong
	 * @param example
	 * @param sort
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public <S extends T> List<S> findAll(Example<S> example, Sort sort) {
		return baseDao.findAll(example, sort);
	}
	
	/**
	 * 根据查询条件加载列表并排序
	 *
	 * @author qc_zhong
	 * @param example
	 * @param selections
	 * @param sort
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public <S extends T> List<S> findAll(Example<S> example, Selections<S> selections, Sort sort) {
		return baseDao.findAll(example, selections, sort);
	}
	
	/**
	 * 根据查询条件加载列表
	 *
	 * @author qc_zhong
	 * @param spec
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public <S extends T> List<S> findAll(Specification<S> spec) {
		if (getClazz() == null) {
			return null;
		}
		return baseDao.findAll(getClazz(), spec);
	}
	
	/**
	 * 根据查询条件加载列表
	 *
	 * @author qc_zhong
	 * @param spec
	 * @param sort
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public <S extends T> List<S> findAll(Specification<S> spec, Sort sort) {
		if (getClazz() == null) {
			return null;
		}
		return baseDao.findAll(getClazz(), spec, sort);
	}
	
	/**
	 * 根据查询条件加载列表
	 *
	 * @author qc_zhong
	 * @param spec
	 * @param selections
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public <S extends T> List<S> findAll(Specification<S> spec, Selections<S> selections) {
		if (getClazz() == null) {
			return null;
		}
		return baseDao.findAll(getClazz(), spec, selections);
	}
	
	/**
	 * 根据查询条件加载列表
	 *
	 * @author qc_zhong
	 * @param spec
	 * @param selections
	 * @param sort
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public <S extends T> List<S> findAll(Specification<S> spec, Selections<S> selections, Sort sort) {
		if (getClazz() == null) {
			return null;
		}
		return baseDao.findAll(getClazz(), spec, selections, sort);
	}

	/**
	 * 持久化新建态实体
	 *
	 * @author qc_zhong
	 * @param entity
	 */
	@Override
	public <S extends T> S save(S entity) {
		if (getClazz() == null) {
			return null;
		}
		return baseDao.save(getClazz(), entity);
	}

	/**
	 * 批量保存
	 *
	 * @author qc_zhong
	 * @param entities
	 * @return
	 */
	@Override
	public <S extends T> List<S> save(Iterable<S> entities) {
		List<S> result = new ArrayList<S>();
		if (entities == null) {
			return result;
		}
		for (S entity : entities) {
			result.add(save(entity));
		}
		return result;
	}

	/**
	 * 保存flush
	 *
	 * @author qc_zhong
	 * @param entity
	 * @return
	 */
	@Override
	public <S extends T> S saveAndFlush(S entity) {
		S result = save(entity);
		flush();
		return result;
	}

	/**
	 * 删除
	 *
	 * @author qc_zhong
	 * @param id
	 */
	@Override
	public void delete(Long id) {
		T entity = find(id);
		if (entity != null) {
			baseDao.delete(entity);
		}
	}

	/**
	 * 删除
	 *
	 * @author qc_zhong
	 * @param entity
	 * @return
	 */
	@Override
	public void delete(T entity) {
		baseDao.delete(entity);
	}

	/**
	 * 胶管
	 *
	 * @author qc_zhong
	 * @param entity
	 */
	@Override
	public void detach(T entity) {
		baseDao.detach(entity);
	}

	/**
	 * 刷新
	 *
	 * @author qc_zhong
	 * @param entity
	 */
	@Override
	@Transactional(readOnly = true)
	public void refresh(T entity) {
		baseDao.refresh(entity);
	}

	/**
	 * 刷新
	 *
	 * @author qc_zhong
	 * @param entity
	 * @param lockMode
	 */
	@Override
	@Transactional(readOnly = true)
	public void refresh(T entity, LockModeType lockMode) {
		baseDao.refresh(entity, lockMode);
	}

	/**
	 * 刷新
	 *
	 * @author qc_zhong
	 * @param entity
	 * @param properties
	 */
	@Override
	@Transactional(readOnly = true)
	public void refresh(T entity, Map<String, Object> properties) {
		baseDao.refresh(entity, properties);
	}

	/**
	 * 刷新
	 *
	 * @author qc_zhong
	 * @param entity
	 * @param lockMode
	 * @param properties
	 */
	@Override
	@Transactional(readOnly = true)
	public void refresh(T entity, LockModeType lockMode, Map<String, Object> properties) {
		baseDao.refresh(entity, lockMode, properties);
	}

	/**
	 * 加锁
	 *
	 * @author qc_zhong
	 * @param entity
	 * @param lockMode
	 */
	@Override
	public void lock(T entity, LockModeType lockMode) {
		baseDao.lock(entity, lockMode);
	}

	/**
	 * 获取实体的锁模式
	 *
	 * @author qc_zhong
	 * @param entity
	 * @return
	 */
	@Override
	public LockModeType getLockMode(T entity) {
		return baseDao.getLockMode(entity);
	}
	
	/**
	 * 统计
	 *
	 * @author qc_zhong
	 * @param entity
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public Long count(T entity) {
		if (getClazz() == null) {
			return 0L;
		}
		return baseDao.count(getClazz(), Example.of(entity));
	}
	
	/**
	 * 统计
	 *
	 * @author qc_zhong
	 * @param spec
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public Long count(Specification<T> spec) {
		if (getClazz() == null) {
			return 0L;
		}
		return baseDao.count(getClazz(), spec);
	}

	/**
	 * 是否存在
	 *
	 * @author qc_zhong
	 * @param id
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean exists(Long id) {
		if (getClazz() == null) {
			return false;
		}
		return baseDao.exists(getClazz(), id);
	}

	/**
	 * 是否存在
	 *
	 * @author qc_zhong
	 * @param entity
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean exists(T entity) {
		if (getClazz() == null) {
			return false;
		}
		return baseDao.exists(getClazz(), Example.of(entity));
	}
	
	/**
	 * 是否存在
	 *
	 * @author qc_zhong
	 * @param spec
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean exists(Specification<T> spec) {
		if (getClazz() == null) {
			return false;
		}
		return baseDao.exists(getClazz(), spec);
	}

	/**
	 * flush
	 *
	 * @author qc_zhong
	 */
	@Override
	public void flush() {
		baseDao.flush();
	}
	
	/**
	 * 更新
	 *
	 * @author qc_zhong
	 * @param id
	 * @param entity
	 * @return
	 */
	@Override
	public int update(Long id, T entity) {
		if (getClazz() == null) {
			return 0;
		}
		return baseDao.update(getClazz(), id, entity);
	}
}