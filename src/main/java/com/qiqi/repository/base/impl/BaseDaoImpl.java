package com.qiqi.repository.base.impl;

import static org.springframework.data.jpa.repository.query.QueryUtils.toOrders;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.convert.QueryByExamplePredicateBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.qiqi.repository.base.BaseDao;
import com.qiqi.repository.base.PageBean;
import com.qiqi.repository.base.ResultHandler;
import com.qiqi.repository.base.Selections;

@Repository("baseDao")
public class BaseDaoImpl implements BaseDao {
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * 立即加载
	 *
	 * @author qc_zhong
	 * @param clazz
	 * @param id
	 * @return
	 */
	@Override
	public <T> T find(Class<T> clazz, Long id) {
		return entityManager.find(clazz, id);
	}

	/**
	 * 立即加载
	 *
	 * @author qc_zhong
	 * @param clazz
	 * @param example
	 * @return
	 */
	@Override
	public <T> T find(Class<T> clazz, Example<T> example) {
		try {
			return getQuery(new ExampleSpecification<T>(example), example.getProbeType(), (Sort) null).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * 立即加载
	 *
	 * @author qc_zhong
	 * @param clazz
	 * @param example
	 * @param selections
	 * @return
	 */
	public <T> T find(Class<T> clazz, Example<T> example, Selections<T> selections) {
		try {
			return getQuery(new ExampleSpecification<T>(example), selections, example.getProbeType(), (Sort) null).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * 立即加载
	 *
	 * @author qc_zhong
	 * @param clazz
	 * @param spec
	 * @return
	 */
	@Override
	public <T> T find(Class<T> clazz, Specification<T> spec) {
		try {
			return getQuery(spec, clazz, (Sort) null).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * 立即加载
	 *
	 * @author qc_zhong
	 * @param clazz
	 * @param spec
	 * @param selections
	 * @return
	 */
	@Override
	public <T> T find(Class<T> clazz, Specification<T> spec, Selections<T> selections) {
		try {
			return getQuery(spec, selections, clazz, (Sort) null).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * 立即加载
	 *
	 * @author qc_zhong
	 * @param clazz
	 * @param id
	 * @param lockMode
	 * @return
	 */
	@Override
	public <T> T find(Class<T> clazz, Long id, LockModeType lockMode) {
		return entityManager.find(clazz, id, lockMode);
	}

	/**
	 * 延迟加载
	 *
	 * @author qc_zhong
	 * @param clazz
	 * @param id
	 * @return
	 */
	@Override
	public <T> T getReference(Class<T> clazz, Long id) {
		return entityManager.getReference(clazz, id);
	}

	/**
	 * 加载列表, 不支持组合主键
	 *
	 * @author qc_zhong
	 * @param clazz
	 * @return
	 */
	@Override
	public <T> List<T> findAll(Class<T> clazz) {
		return getQuery(null, clazz, (Sort) null).getResultList();
	}

	/**
	 * 加载列表
	 *
	 * @author qc_zhong
	 * @param clazz
	 * @param selections
	 * @return
	 */
	@Override
	public <T> List<T> findAll(Class<T> clazz, Selections<T> selections) {
		return getQuery(null, selections, clazz, (Sort) null).getResultList();
	}

	/**
	 * 加载列表并排序
	 *
	 * @author qc_zhong
	 * @param clazz
	 * @param sort
	 * @return
	 */
	@Override
	public <T> List<T> findAll(Class<T> clazz, Sort sort) {
		return getQuery(null, clazz, sort).getResultList();
	}

	/**
	 * 加载列表并排序
	 *
	 * @author qc_zhong
	 * @param clazz
	 * @param selections
	 * @param sort
	 * @return
	 */
	@Override
	public <T> List<T> findAll(Class<T> clazz, Selections<T> selections, Sort sort) {
		return getQuery(null, selections, clazz, sort).getResultList();
	}

	/**
	 * 根据查询条件加载列表, 不支持组合主键
	 * 
	 * @author qc_zhong(钟其纯)
	 * @param example
	 * @return
	 */
	@Override
	public <T> List<T> findAll(Example<T> example) {
		Sort sort = new Sort(Direction.ASC, getIdName(example.getProbeType()));
		return getQuery(new ExampleSpecification<T>(example), example.getProbeType(), sort).getResultList();
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
	public <T> List<T> findAll(Example<T> example, Selections<T> selections) {
		Sort sort = new Sort(Direction.ASC, getIdName(example.getProbeType()));
		return getQuery(new ExampleSpecification<T>(example), selections, example.getProbeType(), sort).getResultList();
	}

	/**
	 * 根据查询条件加载列表并排序, 不支持组合主键
	 *
	 * @author qc_zhong
	 * @param example
	 * @param sort
	 * @return
	 */
	@Override
	public <T> List<T> findAll(Example<T> example, Sort sort) {
		return getQuery(new ExampleSpecification<T>(example), example.getProbeType(), sort).getResultList();
	}

	/**
	 * 根据查询条件加载列表
	 *
	 * @author qc_zhong
	 * @param example
	 * @param selections
	 * @param sort
	 * @return
	 */
	@Override
	public <T> List<T> findAll(Example<T> example, Selections<T> selections, Sort sort) {
		return getQuery(new ExampleSpecification<T>(example), selections, example.getProbeType(), sort).getResultList();
	}

	/**
	 * 根据查询条件加载列表
	 *
	 * @author qc_zhong
	 * @param clazz
	 * @param spec
	 * @return
	 */
	@Override
	public <T> List<T> findAll(Class<T> clazz, Specification<T> spec) {
		return getQuery(spec, clazz, (Sort) null).getResultList();
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
	public <T> List<T> findAll(Class<T> clazz, Specification<T> spec, Sort sort) {
		return getQuery(spec, clazz, sort).getResultList();
	}

	/**
	 * 根据查询条件加载列表
	 *
	 * @author qc_zhong
	 * @param clazz
	 * @param spec
	 * @param selections
	 * @return
	 */
	@Override
	public <T> List<T> findAll(Class<T> clazz, Specification<T> spec, Selections<T> selections) {
		return getQuery(spec, selections, clazz, (Sort) null).getResultList();
	}

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
	@Override
	public <T> List<T> findAll(Class<T> clazz, Specification<T> spec, Selections<T> selections, Sort sort) {
		return getQuery(spec, selections, clazz, sort).getResultList();
	}

	/**
	 * 持久化新建态实体
	 *
	 * @author qc_zhong
	 * @param entity
	 */
	@Override
	public <T> void persist(T entity) {
		entityManager.persist(entity);
	}

	/**
	 * 持久化游离态实体
	 *
	 * @author qc_zhong
	 * @param entity
	 * @return
	 */
	@Override
	public <T> T merge(T entity) {
		return entityManager.merge(entity);
	}

	/**
	 * 保存, 不支持组合主键
	 * 
	 * @author qc_zhong(钟其纯)
	 * @param entity
	 * @return
	 */
	@Override
	public <T> T save(Class<T> clazz, T entity) {
		Object val = getIdValue(clazz, entity);
		if (val == null) {
			entityManager.persist(entity);
			return entity;
		} else {
			return entityManager.merge(entity);
		}
	}

	/**
	 * 删除持久化实体
	 *
	 * @author qc_zhong
	 * @param entity
	 * @return
	 */
	@Override
	public <T> void remove(T entity) {
		entityManager.remove(entity);
	}

	/**
	 * 删除实体
	 *
	 * @author qc_zhong
	 * @param entity
	 */
	@Override
	public <T> void delete(T entity) {
		entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
	}

	/**
	 * 脱管
	 *
	 * @author qc_zhong
	 * @param entity
	 */
	@Override
	public <T> void detach(T entity) {
		entityManager.detach(entity);
	}

	/**
	 * 刷新
	 *
	 * @author qc_zhong
	 * @param entity
	 */
	@Override
	public <T> void refresh(T entity) {
		entityManager.refresh(entity);
	}

	/**
	 * 刷新
	 *
	 * @author qc_zhong
	 * @param entity
	 * @param lockMode
	 */
	@Override
	public <T> void refresh(T entity, LockModeType lockMode) {
		entityManager.refresh(entity, lockMode);
	}

	/**
	 * 刷新
	 *
	 * @author qc_zhong
	 * @param entity
	 * @param properties
	 */
	@Override
	public <T> void refresh(T entity, Map<String, Object> properties) {
		entityManager.refresh(entity, properties);
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
	public <T> void refresh(T entity, LockModeType lockMode, Map<String, Object> properties) {
		entityManager.refresh(entity, lockMode, properties);
	}

	/**
	 * 加锁
	 *
	 * @author qc_zhong
	 * @param entity
	 * @param lockMode
	 */
	@Override
	public <T> void lock(T entity, LockModeType lockMode) {
		entityManager.lock(entity, lockMode);
	}

	/**
	 * 获取实体的锁模式
	 *
	 * @author qc_zhong
	 * @param entity
	 * @return
	 */
	@Override
	public <T> LockModeType getLockMode(T entity) {
		return entityManager.getLockMode(entity);
	}

	/**
	 * 统计
	 *
	 * @author qc_zhong
	 * @param clazz
	 * @param example
	 * @return
	 */
	@Override
	public <T> Long count(Class<T> clazz, Example<T> example) {
		return executeCountQuery(getCountQuery(new ExampleSpecification<T>(example), clazz));
	}

	/**
	 * 统计
	 *
	 * @author qc_zhong
	 * @param clazz
	 * @param spec
	 * @return
	 */
	@Override
	public <T> Long count(Class<T> clazz, Specification<T> spec) {
		return executeCountQuery(getCountQuery(clazz, spec));
	}

	/**
	 * 是否存在, 不支持组合主键
	 *
	 * @author qc_zhong
	 * @param clazz
	 * @param id
	 * @return
	 */
	@Override
	public <T> boolean exists(Class<T> clazz, Long id) {
		String idName = getIdName(clazz);
		// ql
		String ql = QueryUtils.getExistsQueryString(clazz.getSimpleName(), "*", Arrays.asList(idName));
		// 去掉 AND 1 = 1，否则会被druid的wall过滤报错
		ql = ql.substring(0, ql.length() - 10);
		// query
		TypedQuery<Long> query = entityManager.createQuery(ql, Long.class);
		// 参数
		query.setParameter(idName, id);
		// return
		return query.getSingleResult() >= 1L;
	}

	/**
	 * 是否存在
	 *
	 * @author qc_zhong
	 * @param clazz
	 * @param entity
	 * @return
	 */
	@Override
	public <T> boolean exists(Class<T> clazz, Example<T> example) {
		return executeCountQuery(getCountQuery(new ExampleSpecification<T>(example), clazz)) >= 1L;
	}

	/**
	 * 是否存在
	 *
	 * @author qc_zhong
	 * @param clazz
	 * @param spec
	 * @return
	 */
	@Override
	public <T> boolean exists(Class<T> clazz, Specification<T> spec) {
		return executeCountQuery(getCountQuery(spec, clazz)) >= 1L;
	}

	/**
	 * flush
	 *
	 * @author qc_zhong
	 */
	@Override
	public void flush() {
		entityManager.flush();
	}

	/**
	 * 更新
	 *
	 * @author qc_zhong
	 * @param clazz
	 * @param id
	 * @param entity
	 * @return
	 */
	public <T> int update(Class<T> clazz, Long id, T entity) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaUpdate<T> updateQuery = cb.createCriteriaUpdate(clazz);
		Root<T> root = updateQuery.from(clazz);
		// update
		buildUpdate(clazz, updateQuery, entity);
		// where
		Predicate predicate = cb.conjunction();
		List<Expression<Boolean>> expressions = predicate.getExpressions();
		expressions.add(cb.equal(root.get(getIdName(clazz)), id));
		updateQuery.where(predicate);
		// executeUpdate
		return entityManager.createQuery(updateQuery).executeUpdate();
	}
	
	/**
	 * jpql查询
	 * 
	 * @author qc_zhong
	 * @param jpql
	 * @param args
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List getList(String jpql, Object... args) {
		// 创建query
		Query query = entityManager.createQuery(jpql);
		// 填充查询参数
		int i = 1;
		for (Object obj : args) {
			query.setParameter(i++, obj);
		}
		return query.getResultList();
	}
	
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
	@Override
	public List getList(String sql, ResultHandler resultHandler, Object... args) {
		Query query = entityManager.createNativeQuery(sql);
		// 填充查询参数
		int i = 1;
		for (Object obj : args) {
			query.setParameter(i++, obj);
		}
		// 查询
		@SuppressWarnings("unchecked")
		List<Object[]> result = query.getResultList();
		// return
		return resultHandler.getResult(result);
	}

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
	@Override
	public List getList(String jpql, int startPosition, int maxResult, Object... args) {
		// 创建query
		Query query = entityManager.createQuery(jpql);
		// 填充查询参数
		int i = 1;
		for (Object obj : args) {
			query.setParameter(i++, obj);
		}
		// 设置分页条件
		if (startPosition >= 0) {
			query.setFirstResult(startPosition);
		}
		if (maxResult > 0) {
			query.setMaxResults(maxResult);
		}
		// return
		return query.getResultList();
	}
	
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
	@Override
	public List getList(String sql, int startPosition, int maxResult, ResultHandler resultHandler, Object... args) {
		// 创建query
		Query query = entityManager.createNativeQuery(sql);
		// 填充查询参数
		int i = 1;
		for (Object obj : args) {
			query.setParameter(i++, obj);
		}
		// 设置分页条件
		if (startPosition >= 0) {
			query.setFirstResult(startPosition);
		}
		if (maxResult > 0) {
			query.setMaxResults(maxResult);
		}
		// 查询
		@SuppressWarnings("unchecked")
		List<Object[]> result = query.getResultList();
		// return
		return resultHandler.getResult(result);
	}

	/**
	 * jpql分页查询
	 * 
	 * @author qc_zhong
	 * @param pageBean
	 * @param args
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void queryByPage(PageBean pageBean, Object... args) {
		// 初始化
		pageBean.init();
		// 起始位置
		int startPosition = (pageBean.getNumber()) * pageBean.getSize();
		// 获取记录数
		int maxResult = pageBean.getSize();
		// 执行查询
		List list = this.getList(pageBean.getQl(), startPosition, maxResult, args);
		pageBean.setContent(list);
		// 统计总记录数
		Long count = this.count(pageBean.getCountQl(), false, args);
		if (null != count) {
			pageBean.setTotalElements(count);
		}
		// 计算总页数
		if (pageBean.getTotalElements() > 0) {
			int totalPages = pageBean.getTotalElements().intValue() / pageBean.getSize();
			int remainder = pageBean.getTotalElements().intValue() % pageBean.getSize();
			if (remainder > 0) {
				totalPages++;
			}
			pageBean.setTotalPages(totalPages);
		}
	}
	
	/**
	 * sql分页查询
	 *
	 * @author qc_zhong
	 * @param pageBean
	 * @param resultHandler
	 * @param args
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void queryByPage(PageBean pageBean, ResultHandler resultHandler, Object... args) {
		// 初始化
		pageBean.init();
		// 起始位置
		int startPosition = (pageBean.getNumber()) * pageBean.getSize();
		// 获取记录数
		int maxResult = pageBean.getSize();
		// 执行查询
		List list = this.getList(pageBean.getQl(), startPosition, maxResult, resultHandler, args);
		pageBean.setContent(list);
		// 统计总记录数
		Long count = this.count(pageBean.getCountQl(), true, args);
		if (null != count) {
			pageBean.setTotalElements(count);
		}
		// 计算总页数
		if (pageBean.getTotalElements() > 0) {
			int totalPages = pageBean.getTotalElements().intValue() / pageBean.getSize();
			int remainder = pageBean.getTotalElements().intValue() % pageBean.getSize();
			if (remainder > 0) {
				totalPages++;
			}
			pageBean.setTotalPages(totalPages);
		}
	}

	/**
	 * 统计总记录数
	 * 
	 * @author qc_zhong
	 * @param ql
	 * @param nativeQuery
	 * @param args
	 * @return
	 */
	private Long count(String ql, boolean nativeQuery, Object... args) {
		// 创建query
		Query query = null;
		if (nativeQuery) {
			query = entityManager.createNativeQuery(ql);
		} else {
			query = entityManager.createQuery(ql);
		}
		// 填充查询参数
		int i = 1;
		for (Object obj : args) {
			query.setParameter(i++, obj);
		}
		// 返回总记录数
		return ((Number) query.getSingleResult()).longValue();
	}

	private <T> TypedQuery<T> getQuery(Specification<T> spec, Class<T> domainClass, Sort sort) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(domainClass);

		Root<T> root = applySpecificationToCriteria(spec, domainClass, query);
		query.select(root);

		if (sort != null) {
			query.orderBy(toOrders(sort, root, builder));
		}

		return entityManager.createQuery(query);
	}

	private <T> TypedQuery<T> getQuery(Specification<T> spec, Selections<T> selections, Class<T> domainClass, Sort sort) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(domainClass);

		Root<T> root = applySpecificationToCriteria(spec, domainClass, query);
		query.multiselect(selections.toSelection(root));

		if (sort != null) {
			query.orderBy(toOrders(sort, root, builder));
		}

		return entityManager.createQuery(query);
	}

	private <S, U extends T, T> Root<U> applySpecificationToCriteria(Specification<U> spec, Class<U> domainClass, CriteriaQuery<S> query) {
		Assert.notNull(query, "query is not null");
		Assert.notNull(domainClass, "domainClass is not null");
		Root<U> root = query.from(domainClass);

		if (spec == null) {
			return root;
		}

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		Predicate predicate = spec.toPredicate(root, query, builder);

		if (predicate != null) {
			query.where(predicate);
		}

		return root;
	}

	private static class ExampleSpecification<T> implements Specification<T> {
		private final Example<T> example;

		public ExampleSpecification(Example<T> example) {

			Assert.notNull(example, "Example must not be null!");
			this.example = example;
		}

		@Override
		public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
			return QueryByExamplePredicateBuilder.getPredicate(root, cb, example);
		}
	}

	private <T> String getIdName(Class<T> clazz) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> query = cb.createQuery(clazz);
		Root<T> root = query.from(clazz);
		SingularAttribute<? super T, ?> id = root.getModel().getId(root.getModel().getIdType().getJavaType());
		Member member = id.getJavaMember();
		if (member instanceof Field) {
			Field field = (Field) member;
			return field.getName();
		} else {
			return null;
		}
	}

	private <T> Object getIdValue(Class<T> clazz, T entity) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> query = cb.createQuery(clazz);
		Root<T> root = query.from(clazz);
		SingularAttribute<? super T, ?> id = root.getModel().getId(root.getModel().getIdType().getJavaType());
		Member member = id.getJavaMember();
		if (member instanceof Field) {
			Field field = (Field) member;
			if (!Modifier.isPublic(field.getDeclaringClass().getModifiers()) || !Modifier.isPublic(field.getModifiers())) {
				field.setAccessible(true);
			}
			try {
				Object val = field.get(entity);
				return val;
			} catch (Exception e) {
				return null;
			}
		} else {
			return null;
		}
	}

	private <T> void buildUpdate(Class<T> clazz, CriteriaUpdate<T> updateQuery, T entity) {
		try {
			BeanInfo bi = Introspector.getBeanInfo(clazz, Object.class);
			PropertyDescriptor[] pd = bi.getPropertyDescriptors();

			for (PropertyDescriptor propertyDescriptor : pd) {
				// getter
				Method readMethod = propertyDescriptor.getReadMethod();
				if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
					readMethod.setAccessible(true);
				}
				// 获取属性值
				Object val = null;
				try {
					val = readMethod.invoke(entity);
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
				// 是否为空
				if (val != null) {
					String name = propertyDescriptor.getName();
					updateQuery.set(name, val);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}

	private <T> TypedQuery<Long> getCountQuery(Class<T> clazz, Specification<T> spec) {
		return getCountQuery(spec, clazz);
	}

	private <T> TypedQuery<Long> getCountQuery(Specification<T> spec, Class<T> domainClass) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> query = builder.createQuery(Long.class);

		Root<T> root = applySpecificationToCriteria(spec, domainClass, query);

		if (query.isDistinct()) {
			query.select(builder.countDistinct(root));
		} else {
			query.select(builder.count(root));
		}

		// Remove all Orders the Specifications might have applied
		query.orderBy(Collections.<Order> emptyList());

		return entityManager.createQuery(query);
	}

	private static Long executeCountQuery(TypedQuery<Long> query) {
		Assert.notNull(query, "query is not null");

		List<Long> totals = query.getResultList();
		Long total = 0L;

		for (Long element : totals) {
			total += element == null ? 0 : element;
		}

		return total;
	}
}