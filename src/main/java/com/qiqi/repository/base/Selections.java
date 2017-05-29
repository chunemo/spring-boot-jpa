package com.qiqi.repository.base;

import java.util.List;

import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

/**
 * jpa select colomns interface
 * 
 * @author qc_zhong
 * @param <T>
 */
@FunctionalInterface
public interface Selections<T> {
	List<Selection<?>> toSelection(Root<T> root);
}