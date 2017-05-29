package com.qiqi.tool;

import java.util.UUID;

/**
 * UUID工具类
 * 
 * @author qc_zhong
 *
 */
public class UuidUtil {

	public static String get32UUID() {
		String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
		return uuid;
	}
}

