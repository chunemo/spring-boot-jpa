package com.qiqi.tool;

/**
 * 二进制工具类
 * 
 * @author qc_zhong
 */
public class BinaryUtil {
	/**
	 * 添加二进制位
	 *
	 * @author qc_zhong
	 * @param flag
	 * @param bit
	 * @return
	 */
	public static int add(int flag, int bit) {
		return flag | bit;
	}

	/**
	 * 添加二进制位
	 *
	 * @author qc_zhong
	 * @param flag
	 * @param bit
	 * @return
	 */
	public static Integer add(Integer flag, Integer bit) {
		if (flag == null || bit == null) {
			return null;
		}

		return flag | bit;
	}

	/**
	 * 移除二进制位
	 *
	 * @author qc_zhong
	 * @param flag
	 * @param bit
	 * @return
	 */
	public static int remove(int flag, int bit) {
		return flag & (~bit);
	}

	/**
	 * 移除二进制位
	 *
	 * @author qc_zhong
	 * @param flag
	 * @param bit
	 * @return
	 */
	public static Integer remove(Integer flag, Integer bit) {
		if (flag == null || bit == null) {
			return null;
		}

		return flag & (~bit);
	}

	/**
	 * 是否包含位
	 *
	 * @author qc_zhong
	 * @param flag
	 * @param bit
	 * @return
	 */
	public static boolean contain(int flag, int bit) {
		return (flag & bit) > 0;
	}

	/**
	 * 是否包含位
	 *
	 * @author qc_zhong
	 * @param flag
	 * @param bit
	 * @return
	 */
	public static boolean contain(Integer flag, Integer bit) {
		if (flag == null || bit == null) {
			return false;
		}

		return (flag & bit) > 0;
	}

	/**
	 * 数值转换为二进制字符串
	 *
	 * @author qc_zhong
	 * @param flag
	 * @return
	 */
	public static String toBinaryString(int flag) {
		return Integer.toBinaryString(flag);
	}

	/**
	 * 数值转换为二进制字符串
	 *
	 * @author qc_zhong
	 * @param flag
	 * @return
	 */
	public static String toBinaryString(Integer flag) {
		if (flag == null) {
			return null;
		}
		return Integer.toBinaryString(flag);
	}
}