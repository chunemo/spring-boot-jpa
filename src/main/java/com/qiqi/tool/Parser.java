package com.qiqi.tool;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;

import com.alibaba.fastjson.JSON;
import com.qiqi.tool.bean.Checker;

/**
 * 类型转换器
 * 
 * @author qc_zhong
 */
public class Parser {
	/**
	 * Integer转换
	 * 
	 * @author qc_zhong(钟其纯)
	 * @param obj
	 * @return
	 */
	public static Integer parseInteger(Object obj) {
		Integer ret = null;
		try {
			ret = Integer.parseInt(obj.toString());
		} catch (Exception e) {
		}
		return ret;
	}

	/**
	 * Long转换
	 * 
	 * @author qc_zhong(钟其纯)
	 * @param obj
	 * @return
	 */
	public static Long parseLong(Object obj) {
		Long ret = null;
		try {
			ret = Long.parseLong(obj.toString());
		} catch (Exception e) {
		}
		return ret;
	}

	/**
	 * Double转换
	 * 
	 * @author qc_zhong(钟其纯)
	 * @param obj
	 * @return
	 */
	public static Double parseDouble(Object obj) {
		Double ret = null;
		try {
			ret = Double.parseDouble(obj.toString());
		} catch (Exception e) {
		}
		return ret;
	}

	/**
	 * boolean转换
	 * 
	 * @author qc_zhong(钟其纯)
	 * @param obj
	 * @return
	 */
	public static Boolean parseBoolean(Object obj) {
		Boolean ret = null;
		try {
			if (Checker.getInstance().notEmpty(obj)) {
				String val = obj.toString().trim();
				if (val.equalsIgnoreCase("true") || val.equalsIgnoreCase("false")) {
					ret = Boolean.parseBoolean(obj.toString());
				}
			}
		} catch (Exception e) {
		}
		return ret;
	}

	/**
	 * Character转换
	 *
	 * @author qc_zhong
	 * @param obj
	 * @return
	 */
	public static Character parseCharacter(Object obj) {
		Character c = null;
		try {
			c = Character.valueOf(obj.toString().charAt(0));
		} catch (Exception e) {
		}
		return c;
	}

	/**
	 * Date转换
	 * 
	 * @author qc_zhong(钟其纯)
	 * @param obj
	 * @return
	 */
	public static Date parseDate(Object obj) {
		Date date = null;
		try {
			date = DateUtils.parseDate(obj.toString(), "yyyy-MM-dd");
		} catch (Exception e) {
		}
		return date;
	}

	/**
	 * Date转换
	 * 
	 * @author qc_zhong(钟其纯)
	 * @param date
	 * @return
	 */
	public static String parseDate(Date date) {
		String ret = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			ret = formatter.format(date);
		} catch (Exception e) {
		}
		return ret;
	}

	/**
	 * Date转换(yyyyMMdd)
	 *
	 * @author qc_zhong
	 * @param obj
	 * @return
	 */
	public static Date parseSimpleDate(Object obj) {
		Date date = null;
		try {
			date = DateUtils.parseDate(obj.toString(), "yyyyMMdd");
		} catch (Exception e) {
		}
		return date;
	}

	/**
	 * Date转换(yyyyMMdd)
	 *
	 * @author qc_zhong
	 * @param date
	 * @return
	 */
	public static String parseSimpleDate(Date date) {
		String ret = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			ret = formatter.format(date);
		} catch (Exception e) {
		}
		return ret;
	}

	/**
	 * yyyy-MM-dd HH:mm时间转换
	 * 
	 * @author qc_zhong(钟其纯)
	 * @param obj
	 * @return
	 */
	public static Date parseDateHHmm(Object obj) {
		Date time = null;
		try {
			time = DateUtils.parseDate(obj.toString(), "yyyy-MM-dd HH:mm");
		} catch (Exception e) {
		}
		return time;
	}

	/**
	 * time转换
	 * 
	 * @author qc_zhong(钟其纯)
	 * @param obj
	 * @return
	 */
	public static Date parseTime(Object obj) {
		Date time = null;
		try {
			time = DateUtils.parseDate(obj.toString(), "yyyy-MM-dd HH:mm:ss");
		} catch (Exception e) {
		}
		return time;
	}

	/**
	 * time转换
	 * 
	 * @author qc_zhong(钟其纯)
	 * @param time
	 * @return
	 */
	public static String parseTime(Date time) {
		String ret = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			ret = formatter.format(time);
		} catch (Exception e) {
		}
		return ret;
	}

	/**
	 * time转换(yyyyMMddHHmmss)
	 *
	 * @author qc_zhong
	 * @param obj
	 * @return
	 */
	public static Date parseSimpleTime(Object obj) {
		Date time = null;
		try {
			time = DateUtils.parseDate(obj.toString(), "yyyyMMddHHmmss");
		} catch (Exception e) {
		}
		return time;
	}

	/**
	 * time转换(yyyyMMddHHmmss)
	 * 
	 * @author qc_zhong(钟其纯)
	 * @param time
	 * @return
	 */
	public static String parseSimpleTime(Date time) {
		String ret = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			ret = formatter.format(time);
		} catch (Exception e) {
		}
		return ret;
	}

	/**
	 * HH:mm时刻转换
	 * 
	 * @author qc_zhong(钟其纯)
	 * @param obj
	 * @return
	 */
	public static Date parseTimeHHmm(Object obj) {
		Date date = null;
		try {
			date = DateUtils.parseDate(obj.toString(), "HH:mm");
		} catch (Exception e) {
		}
		return date;
	}

	/**
	 * HH:mm时刻转换
	 * 
	 * @author qc_zhong(钟其纯)
	 * @param date
	 * @return
	 */
	public static String parseTimeHHmm(Date date) {
		String ret = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			ret = formatter.format(date);
		} catch (Exception e) {
		}
		return ret;
	}

	/**
	 * json转换
	 *
	 * @author qc_zhong
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <T> T parseJson(String json, Class<T> clazz) {
		T t = null;
		try {
			t = JSON.parseObject(json, clazz);
		} catch (Exception e) {
		}
		if (t == null) {
			try {
				t = clazz.newInstance();
			} catch (Exception e) {
			}
		}
		return t;
	}

	/**
	 * json转换
	 *
	 * @author qc_zhong
	 * @param obj
	 * @return
	 */
	public static String parseJson(Object obj) {
		if (Checker.getInstance().isEmpty(obj)) {
			return null;
		}
		return JSON.toJSONString(obj);
	}

	/**
	 * json转换
	 *
	 * @author qc_zhong
	 * @param obj
	 * @param clazz
	 * @return
	 */
	public static <T> T parseJson(Object obj, Class<T> clazz) {
		if (Checker.getInstance().isEmpty(obj)) {
			return null;
		}

		String json = parseJson(obj);
		return parseJson(json, clazz);
	}

	/**
	 * json list转换
	 *
	 * @author qc_zhong
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> parseJsonList(String json, Class<T> clazz) {
		List<T> list = new ArrayList<T>();
		if (Checker.getInstance().isEmpty(json)) {
			return list;
		} else {
			try {
				list = JSON.parseArray(json, clazz);
			} catch (Exception e) {
			}
			return list;
		}
	}
}