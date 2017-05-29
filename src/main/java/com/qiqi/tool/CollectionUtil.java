package com.qiqi.tool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 集合工具类
 * 
 * @author qc_zhong(钟其纯)
 *
 */
public class CollectionUtil {
	/**
	 * 拆分列表
	 * 
	 * @author qc_zhong(钟其纯)
	 * 
	 * @param list
	 * @param size
	 * @return
	 */
	public static <T> List<List<T>> split(List<T> list, int size) {
		// 返回列表
		List<List<T>> ls = new ArrayList<List<T>>();

		if (list == null || list.size() == 0 || size <= 0) {
			ls.add(list);
		} else {
			int ret = list.size() / size;
			int remainder = list.size() % size;

			// 遍历
			int index = 0;
			for (; index < ret; index++) {
				int fromIndex = size * index;
				int toIndex = size * (index + 1);
				ls.add(list.subList(fromIndex, toIndex));
			}

			// 剩余
			if (remainder > 0) {
				int fromIndex = size * index;
				int toIndex = fromIndex + remainder;
				ls.add(list.subList(fromIndex, toIndex));
			}
		}

		return ls;
	}
	
	/**
	 * 拆分集合
	 * 
	 * @author qc_zhong(钟其纯)
	 * 
	 * @param list
	 * @param size
	 * @return
	 */
	public static <T> List<List<T>> split(Collection<T> collection, int size) {
		List<T> list = new ArrayList<T>();
		list.addAll(collection);

		return split(list, size);
	}
}