package com.qiqi.tool.bean;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.qiqi.tool.ReflectionUtils;

/**
 * 检查器
 * 
 * @author qc_zhong(钟其纯)
 */
public class Checker {
	private static Checker instance;

	private Checker() {
		super();
	}

	public static Checker getInstance() {
		if (instance == null) {
			synchronized (Checker.class) {
				if (instance == null) {
					instance = new Checker();
				}
			}
		}
		return instance;
	}

	/**
	 * 批量检查请求参数是否为空
	 * 
	 * @author qc_zhong(钟其纯)
	 * @param obj
	 * @param map
	 * @throws Exception 
	 * @throws CommonException
	 */
	public void checkEmptyParam(Object obj, Map<String, Exception> map) throws Exception {
		if (obj != null && map != null && map.size() > 0) {
			for (Iterator<String> it = map.keySet().iterator(); it.hasNext();) {
				String name = it.next();

				// 支持级联
				int index = name.indexOf(".");
				String other;
				Map<String, Exception> cascadeMap = null;
				if (index > 0) {
					String property = name.substring(0, index); // 主属性
					other = name.substring(index + 1, name.length()); // 级联属性
					// 提取异常
					cascadeMap = new HashMap<String, Exception>();
					cascadeMap.put(other, map.get(name));
					cascadeMap.put(property, map.get(name));
					// 更新属性
					name = property;
				}
				
				Field field = ReflectionUtils.getDeclaredField(obj, name);
				if (field != null) {
					if (!Modifier.isPublic(field.getModifiers())) {
						field.setAccessible(true);
					}

					// 获取属性值
					Object val = null;
					try {
						val = field.get(obj);
					} catch (Exception e) {
						System.err.println(e.getMessage());
					}

					if (isEmpty(val)) {
						Exception ex = null;
						if (index > 0) {
							ex = cascadeMap.get(name);
						} else {
							ex = map.get(name);
						}

						throw ex;
					}
					// 级联
					if (index > 0) {
						if (val instanceof Collection) {
							Collection<?> collection = (Collection<?>) val;
							Iterator<?> iterator = collection.iterator();
							while (iterator.hasNext()) {
								Object e = iterator.next();
								checkEmptyParam(e, cascadeMap);
							}
						} else {
							checkEmptyParam(val, cascadeMap);
						}
					}
				}
			}
		}
	}

	/**
	 * 批量检查请求参数是否为空
	 * 
	 * @author qc_zhong(钟其纯)
	 * @param obj
	 * @param properties
	 */
	public void checkEmptyParam(Object obj, String... properties) {
		if (obj != null) {
			List<String> list = Arrays.asList(properties);
			for (int i = 0; i < list.size(); i++) {
				Field field = ReflectionUtils.getDeclaredField(obj, list.get(i));
				if (field != null) {
					if (!Modifier.isPublic(field.getModifiers())) {
						field.setAccessible(true);
					}

					// 获取属性值
					Object val = null;
					try {
						val = field.get(obj);
					} catch (Exception e) {
						System.err.println(e.getMessage());
					}

					if (isEmpty(val)) {
						throw new IllegalArgumentException();
					}
				}
			}
		}
	}
	
	/**
	 * 批量检查请求参数是否为空
	 *
	 * @author qc_zhong
	 * @param obj
	 * @param properties
	 */
	public void checkEmptyParam(Object obj, Collection<String> properties) {
		String[] array = properties.toArray(new String[] {});
		checkEmptyParam(obj, array);
	}

	/**
	 * 检查实体id
	 *
	 * @author qc_zhong
	 * @param id
	 */
	public void checkEntityId(Long id) {
		if (id == null || id <= 0) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * 检查实体id
	 *
	 * @author qc_zhong
	 */
	public void checkEntityId(Object obj) {
		checkNumber(obj);
		checkEntityId(Long.parseLong(obj.toString().trim()));
	}

	/**
	 * 检查是否为空
	 *
	 * @author qc_zhong
	 */
	public void checkEmpty(Object obj) {
		if (isEmpty(obj)) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * 检查是否为NULL
	 *
	 * @author qc_zhong
	 * @param obj
	 */
	public void checkNull(Object obj) {
		if (obj == null) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * 检查是否为数字
	 *
	 * @author qc_zhong
	 */
	public void checkNumber(Object obj) {
		checkEmpty(obj);
		if (!NumberUtils.isNumber(obj.toString().trim())) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * 检查是否为负数
	 *
	 * @author qc_zhong
	 * @param num
	 */
	public void checkNegative(Number num) {
		checkEmpty(num);
		if (num.doubleValue() < 0) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * 检查是否为负数
	 *
	 * @author qc_zhong
	 * @param obj
	 */
	public void checkNegative(Object obj) {
		checkNumber(obj);
		checkNegative(Double.parseDouble(obj.toString().trim()));
	}

	/**
	 * 检查是否为正数
	 *
	 * @author qc_zhong
	 * @param num
	 */
	public void checkPositive(Number num) {
		checkEmpty(num);
		if (num.doubleValue() <= 0) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * 检查是否为正数
	 *
	 * @author qc_zhong
	 * @param obj
	 */
	public void checkPositive(Object obj) {
		checkNumber(obj);
		checkPositive(Double.parseDouble(obj.toString().trim()));
	}
	
	/**
	 * 根据正则检查字符串格式
	 *
	 * @author qc_zhong
	 * @param str
	 * @param regex
	 */
	public void checkRegex(String str, String regex) {
		if (!str.matches(regex)) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * 判断对象是否为空
	 * 
	 * @author qc_zhong(钟其纯)
	 * @param obj
	 * @return
	 */
	public boolean isEmpty(Object obj) {
		if (obj == null) {
			return true;
		} else if (obj instanceof Collection) {
			return CollectionUtils.isEmpty((Collection<?>) obj);
		} else if (obj instanceof Map) {
			return MapUtils.isEmpty((Map<?, ?>) obj);
		} else if (obj.getClass().isArray()) {
			return ArrayUtils.isEmpty((Object[]) obj);
		} else {
			return obj.toString().trim().length() == 0;
		}
	}

	/**
	 * 判断对象是否不为空
	 *
	 * @author qc_zhong
	 * @param obj
	 * @return
	 */
	public boolean notEmpty(Object obj) {
		return !isEmpty(obj);
	}

	/**
	 * 是否为正数
	 *
	 * @author qc_zhong
	 * @param num
	 * @return
	 */
	public boolean isPositive(Number num) {
		return num == null ? false : num.doubleValue() > 0;
	}
	
	/**
	 * 是否为正数
	 *
	 * @author qc_zhong
	 * @param num
	 * @return
	 */
	public boolean notPositive(Number num) {
		return !isPositive(num);
	}

	/**
	 * 是否为负数
	 *
	 * @author qc_zhong
	 * @param num
	 * @return
	 */
	public boolean isNegative(Number num) {
		return num == null ? false : num.doubleValue() < 0;
	}
	
	/**
	 * 是否为负数
	 *
	 * @author qc_zhong
	 * @param num
	 * @return
	 */
	public boolean notNegative(Number num) {
		return !isNegative(num);
	}
	
	/**
	 * 是否基本数据类型
	 *
	 * @author qc_zhong
	 * @param obj
	 * @return
	 */
	public boolean isPrimitive(Object obj) {
		// get type field
		Field field = null;
		try {
			field = obj.getClass().getField("TYPE");
		} catch (Exception e) {

		}
		if (field == null) {
			return false;
		} else {
			// convert Class
			Class<?> clazz = null;
			try {
				clazz = (Class<?>) field.get(null);
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
			// isPrimitive
			if (clazz == null) {
				return false;
			} else {
				return clazz.isPrimitive();
			}
		}
	}

	/**
	 * 是否基本数据类型
	 *
	 * @author qc_zhong
	 * @param obj
	 * @return
	 */
	public boolean notPrimitive(Object obj) {
		return !isPrimitive(obj);
	}

	/**
	 * 是否为字符串
	 *
	 * @author qc_zhong
	 * @param obj
	 * @return
	 */
	public boolean isString(Object obj) {
		return obj instanceof String;
	}

	/**
	 * 是否为字符串
	 *
	 * @author qc_zhong
	 * @param obj
	 * @return
	 */
	public boolean notString(Object obj) {
		return !isString(obj);
	}
	
	/**
	 * 是否为request
	 *
	 * @author qc_zhong
	 * @param obj
	 * @return
	 */
	public boolean isRequest(Object obj) {
		return obj instanceof ServletRequest;
	}
	
	/**
	 * 是否为request
	 *
	 * @author qc_zhong
	 * @param obj
	 * @return
	 */
	public boolean notRequest(Object obj) {
		return !isRequest(obj);
	}
	
	/**
	 * 是否为response
	 *
	 * @author qc_zhong
	 * @param obj
	 * @return
	 */
	public boolean isResponse(Object obj) {
		return obj instanceof ServletResponse;
	}
	
	/**
	 * 是否为response
	 *
	 * @author qc_zhong
	 * @param obj
	 * @return
	 */
	public boolean notResponse(Object obj) {
		return !isResponse(obj);
	}
	
	/**
	 * 是否为Date
	 *
	 * @author qc_zhong
	 * @param obj
	 * @return
	 */
	public boolean isDate(Object obj) {
		return obj instanceof Date;
	}
	
	/**
	 * 是否为Date
	 *
	 * @author qc_zhong
	 * @param obj
	 * @return
	 */
	public boolean notDate(Object obj) {
		return !isDate(obj);
	}
}