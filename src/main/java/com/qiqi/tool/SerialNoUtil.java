package com.qiqi.tool;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 序列号工具类
 * 
 * @author qc_zhong(钟其纯)
 */
public class SerialNoUtil {
	/**
	 * 生成ID序列号
	 * 
	 * @author qc_zhong(钟其纯)
	 * @param id
	 * @param format
	 * @param beginIndex
	 * @return
	 */
	public static String createIdSerialNo(Long id, String format, int beginIndex) {
		String random = String.format(format, id);
		return random.substring(beginIndex);
	}

	/**
	 * 生成六位ID序列号
	 * 
	 * @author qc_zhong(钟其纯)
	 * @param id
	 * @return
	 */
	public static String createSixIdSerialNo(Long id) {
		return createIdSerialNo(id, "%010d", 4);
	}

	/**
	 * 生成日期格式的序列号<br>
	 * 前缀 + 年月日 + 六位ID序列号
	 * 
	 * @author qc_zhong(钟其纯)
	 * @param id
	 * @param prefix
	 * @return
	 */
	public static String createDateSerialNo(Long id, String prefix) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		StringBuilder serialNo = new StringBuilder();
		serialNo.append(prefix);
		serialNo.append(formatter.format(new Date()));
		serialNo.append(createSixIdSerialNo(id));
		return serialNo.toString();
	}

	/**
	 * 生成时间格式序列号<br>
	 * 前缀 + 年月日时分秒 + 六位ID序列号
	 * 
	 * @author qc_zhong(钟其纯)
	 * @param id
	 * @param prefix
	 * @return
	 */
	public static String createTimeSerialNo(Long id, String prefix) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddhhmmss");
		StringBuilder serialNo = new StringBuilder();
		serialNo.append(prefix);
		serialNo.append(formatter.format(new Date()));
		serialNo.append(createSixIdSerialNo(id));
		return serialNo.toString();
	}
}