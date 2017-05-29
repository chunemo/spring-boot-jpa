package com.qiqi.repository.base;

import java.util.List;

/**
 * 查询结果处理器
 * 
 * @author qc_zhong
 *
 */
public interface ResultHandler {
	/**
	 * 获取查询结果
	 *
	 * @author qc_zhong
	 *
	 * @param result
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List getResult(List<Object[]> result);
}
