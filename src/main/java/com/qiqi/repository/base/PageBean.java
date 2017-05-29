package com.qiqi.repository.base;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * 分页对象
 * 
 * @author qc_zhong
 */
@Data
@JsonInclude(Include.NON_NULL)
public class PageBean<T> {
	// 当前页数, 从0计数
	private Integer number = 0;

	// 总页数
	private Integer totalPages = 0;

	// 总记录数
	private Long totalElements = -1L;

	// 页面大小
	private Integer size = 20;

	// ql查询语句
	private String ql;

	// 分页时计算总记录数的ql语句
	private String countQl;

	// 结果列表
	private List<T> content = new ArrayList<T>();

	/**
	 * 初始化
	 */
	public void init() {
		if (number == null || number < 0) {
			number = 0;
		}

		if (totalPages == null || totalPages < 0) {
			totalPages = 0;
		}

		if (totalElements == null) {
			totalElements = -1L;
		}

		if (size == null || size < 0) {
			size = 20;
		}
	}
}