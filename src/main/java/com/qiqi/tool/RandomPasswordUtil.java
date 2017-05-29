package com.qiqi.tool;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * 随机密码工具类
 * 
 * @author qc_zhong
 *
 */
public class RandomPasswordUtil {
	public static String generatePassword(int count, boolean letters, boolean numbers) {
		return RandomStringUtils.random(count, letters, numbers);
	}
}