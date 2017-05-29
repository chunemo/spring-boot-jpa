package com.qiqi.tool;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

/**
 * 数组工具类
 * 
 * @author qc_zhong
 */
public class ArrayUtil {
	/**
	 * 拆分字符串
	 *
	 * @author qc_zhong
	 * @param string
	 * @param size
	 * @return
	 */
	public static List<String> split(String string, int size) {
		List<String> list = new ArrayList<String>();

		if (string == null || string.length() == 0 || size <= 0) {

		} else {
			int len = string.length();
			int ret = len / size;
			int remainder = len % size;
			// 遍历
			int index = 0;
			for (; index < ret; index++) {
				int fromIndex = size * index;
				int toIndex = size * (index + 1);
				list.add(string.substring(fromIndex, toIndex));
			}
			// 剩余
			if (remainder > 0) {
				int fromIndex = size * index;
				int toIndex = fromIndex + remainder;
				list.add(string.substring(fromIndex, toIndex));
			}
		}

		return list;
	}

	/**
	 * 拆分数组
	 *
	 * @author qc_zhong
	 * @param array
	 * @param size
	 * @return
	 */
	public static <T> List<T[]> split(T[] array, int size) {
		List<T[]> list = new ArrayList<T[]>();

		if (array == null || array.length == 0 || size <= 0) {

		} else {
			int len = array.length;
			int ret = len / size;
			int remainder = len % size;
			// 遍历
			int index = 0;
			for (; index < ret; index++) {
				int fromIndex = size * index;
				int toIndex = size * (index + 1);
				list.add(ArrayUtils.subarray(array, fromIndex, toIndex));
			}
			// 剩余
			if (remainder > 0) {
				int fromIndex = size * index;
				int toIndex = fromIndex + remainder;
				list.add(ArrayUtils.subarray(array, fromIndex, toIndex));
			}
		}

		return list;
	}

	/**
	 * 拆分字节数组
	 *
	 * @author qc_zhong
	 * @param array
	 * @param size
	 * @return
	 */
	public static List<byte[]> split(byte[] array, int size) {
		List<byte[]> list = new ArrayList<byte[]>();

		if (array == null || array.length == 0 || size <= 0) {

		} else {
			int len = array.length;
			int ret = len / size;
			int remainder = len % size;
			// 遍历
			int index = 0;
			for (; index < ret; index++) {
				int fromIndex = size * index;
				int toIndex = size * (index + 1);
				list.add(ArrayUtils.subarray(array, fromIndex, toIndex));
			}
			// 剩余
			if (remainder > 0) {
				int fromIndex = size * index;
				int toIndex = fromIndex + remainder;
				list.add(ArrayUtils.subarray(array, fromIndex, toIndex));
			}
		}

		return list;
	}

	/**
	 * 拆分字节数组
	 *
	 * @author qc_zhong
	 * @param array
	 * @param size
	 * @return
	 */
	public static List<String> splits(byte[] array, int size) {
		List<String> list = new ArrayList<String>();

		if (array == null || array.length == 0 || size <= 0) {

		} else {
			int len = array.length;
			int ret = len / size;
			int remainder = len % size;
			// 遍历
			int index = 0;
			for (; index < ret; index++) {
				int fromIndex = size * index;
				int toIndex = size * (index + 1);
				list.add(new String(ArrayUtils.subarray(array, fromIndex, toIndex)));
			}
			// 剩余
			if (remainder > 0) {
				int fromIndex = size * index;
				int toIndex = fromIndex + remainder;
				list.add(new String(ArrayUtils.subarray(array, fromIndex, toIndex)));
			}
		}

		return list;
	}

	public static String toString(List<byte[]> list) {
		byte[] all = new byte[] {};
		for (byte[] bs : list) {
			all = ArrayUtils.addAll(all, bs);
		}
		return new String(all);
	}

	public static String toString(List<byte[]> list, String charset) {
		byte[] all = new byte[] {};
		for (byte[] bs : list) {
			all = ArrayUtils.addAll(all, bs);
		}
		String string = null;
		try {
			string = new String(all, charset);
		} catch (Exception e) {
		}
		return string;
	}

	/**
	 * 合并字符串
	 *
	 * @author qc_zhong
	 * @param list
	 * @return
	 */
	public static String merge(List<String> list) {
		byte[] all = new byte[] {};
		for (String string : list) {
			all = ArrayUtils.addAll(all, string.getBytes());
		}
		return new String(all);
	}

	/**
	 * 根据指定字符集，合并字符串
	 *
	 * @author qc_zhong
	 * @param list
	 * @return
	 */
	public static String merge(List<String> list, String charset) {
		byte[] all = new byte[] {};
		for (String string : list) {
			try {
				all = ArrayUtils.addAll(all, string.getBytes(charset));
			} catch (Exception e) {
			}
		}
		return new String(all);
	}
}