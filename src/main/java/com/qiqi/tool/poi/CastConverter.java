package com.qiqi.tool.poi;

/**
 * 类型转换器接口
 * 
 * @author qc_zhong
 * @param <T>
 */
public interface CastConverter {
	/**
	 * 类型转换
	 *
	 * @author qc_zhong
	 * @param obj
	 * @return
	 */
	String cast(Object obj);

	/**
	 * 泛型类型转换
	 *
	 * @author qc_zhong
	 * @param s
	 * @return
	 */
	Object cast(String string);
}