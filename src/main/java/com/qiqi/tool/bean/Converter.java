package com.qiqi.tool.bean;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.qiqi.enums.CaseType;
import com.qiqi.tool.BeanUtil;
import com.qiqi.tool.ReflectionUtils;

/**
 * 转换器
 * 
 * @author qc_zhong
 */
public class Converter {
	private static Converter instance = new Converter();

	private Converter() {

	}

	public static Converter getInstance() {
		if (instance == null) {
			synchronized (Converter.class) {
				if (instance == null) {
					instance = new Converter();
				}
			}
		}
		return instance;
	}

	/**
	 * 源对象转换成目标对象
	 */
	public <S, D> void convert(S source, D dest) {
		if (source != null && dest != null) {
			try {
				BeanUtils.copyProperties(source, dest);
			} catch (Exception ex) {
				throw new RuntimeException("Error copying properties of : " + source.getClass().getName(), ex);
			}
		}
	}

	/**
	 * 指定目标Class，将源对象转换成目对象
	 */
	public <S, D> D convert(S source, Class<D> clazz) {
		D dest = null;
		if (source != null) {
			try {
				dest = clazz.newInstance();
				BeanUtils.copyProperties(source, dest);
			} catch (Exception ex) {
				throw new RuntimeException("Error copying properties of : " + source.getClass().getName(), ex);
			}
		}
		return dest;
	}

	/**
	 * 提供忽略属性，将源对象转换成目标对象
	 */
	public <S, D> void convertExclude(S source, D dest, String... ignoreProperties) {
		if (source != null && dest != null) {
			try {
				BeanUtils.copyProperties(source, dest, ignoreProperties);
			} catch (Exception ex) {
				throw new RuntimeException("Error copying properties of : " + source.getClass().getName(), ex);
			}
		}
	}

	/**
	 * 提供忽略属性，将源对象转换成目标对象
	 */
	public <S, D> void convertExclude(S source, D dest, Collection<String> ignoreProperties) {
		String[] temps = ignoreProperties.toArray(new String[] {});
		convertExclude(source, dest, temps);
	}

	/**
	 * 提供忽略属性和目标class，将源对象转换成目标对象
	 */
	public <S, D> D convertExclude(S source, Class<D> clazz, String... ignoreProperties) {
		D d = null;
		if (source != null) {
			try {
				d = clazz.newInstance();
				BeanUtils.copyProperties(source, d, ignoreProperties);
			} catch (Exception ex) {
				throw new RuntimeException("Error copying properties of : " + source.getClass().getName(), ex);
			}
		}
		return d;
	}

	/**
	 * 提供忽略属性和目标class，将源对象转换成目标对象
	 */
	public <S, D> D convertExclude(S source, Class<D> clazz, Collection<String> ignoreProperties) {
		String[] temps = ignoreProperties.toArray(new String[] {});
		return convertExclude(source, clazz, temps);
	}

	/**
	 * 提供指定属性，将源对象转换成目标对象
	 */
	public <S, D> void convertInclude(S source, D dest, String... properties) {
		if (source != null && dest != null) {
			try {
				BeanUtil.copyProperties(source, dest, properties);
			} catch (Exception ex) {
				throw new RuntimeException("Error copying properties of : " + source.getClass().getName(), ex);
			}
		}
	}

	/**
	 * 提供指定属性，将源对象转换成目标对象
	 */
	public <S, D> void convertInclude(S source, D dest, Collection<String> properties) {
		String[] temps = properties.toArray(new String[] {});
		convertInclude(source, dest, temps);
	}

	/**
	 * 提供指定属性和目标class，将源对象转换成目标对象
	 */
	public <S, D> D convertInclude(S source, Class<D> clazz, String... properties) {
		D dest = null;
		if (source != null) {
			try {
				dest = clazz.newInstance();
				BeanUtil.copyProperties(source, dest, properties);
			} catch (Exception ex) {
				throw new RuntimeException("Error copying properties of : " + source.getClass().getName(), ex);
			}
		}
		return dest;
	}

	/**
	 * 提供指定属性和目标class，将源对象转换成目标对象
	 */
	public <S, D> D convertInclude(S source, Class<D> clazz, Collection<String> properties) {
		String[] temps = properties.toArray(new String[] {});
		return convertInclude(source, clazz, temps);
	}

	/**
	 * 源集合转换成目标集合
	 */
	public <S, D> List<D> converts(Collection<S> sourceList, Class<D> clazz) {
		List<D> destList = new ArrayList<D>();

		if (sourceList != null && sourceList.size() > 0 && clazz != null) {
			for (Iterator<S> it = sourceList.iterator(); it.hasNext();) {
				D d = convert(it.next(), clazz);
				// add
				destList.add(d);
			}
		}

		return destList;
	}

	/**
	 * 提供忽略属性和目标class, 源集合转换成目标集合
	 */
	public <S, D> List<D> convertsExclude(Collection<S> sourceList, Class<D> clazz, String... ignoreProperties) {
		List<D> destList = new ArrayList<D>();

		if (sourceList != null && sourceList.size() > 0 && clazz != null) {
			for (Iterator<S> it = sourceList.iterator(); it.hasNext();) {
				D d = convertExclude(it.next(), clazz, ignoreProperties);
				// add
				destList.add(d);
			}
		}

		return destList;
	}

	/**
	 * 提供忽略属性和目标class, 源集合转换成目标集合
	 */
	public <S, D> List<D> convertsExclude(Collection<S> sourceList, Class<D> clazz, Collection<String> ignoreProperties) {
		String[] temps = ignoreProperties.toArray(new String[] {});
		return convertsExclude(sourceList, clazz, temps);
	}

	/**
	 * 提供属性和目标class, 源集合转换成目标集合
	 */
	public <S, D> List<D> convertsInclude(Collection<S> sourceList, Class<D> clazz, String... properties) {
		List<D> destList = new ArrayList<D>();

		if (sourceList != null && sourceList.size() > 0 && clazz != null) {
			for (Iterator<S> it = sourceList.iterator(); it.hasNext();) {
				D d = convertInclude(it.next(), clazz, properties);
				// add
				destList.add(d);
			}
		}

		return destList;
	}

	/**
	 * 提供属性和目标class, 源集合转换成目标集合
	 */
	public <S, D> List<D> convertsInclude(Collection<S> es, Class<D> clazz, Collection<String> properties) {
		String[] temps = properties.toArray(new String[] {});
		return convertsInclude(es, clazz, temps);
	}

	/**
	 * 将对象的空属性赋值为null
	 * 
	 * @author qc_zhong
	 * @param obj
	 */
	public void emptyAttrToNull(Object obj) {
		// 获取检查器
		Checker checker = Checker.getInstance();

		// 不为空且不为基本数据类型
		if (obj != null && checker.notPrimitive(obj)) {
			BeanInfo bi = null;
			try {
				bi = Introspector.getBeanInfo(obj.getClass(), Object.class);
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}

			// 获取属性数组
			PropertyDescriptor[] pd = bi.getPropertyDescriptors();
			for (PropertyDescriptor propertyDescriptor : pd) {
				String name = propertyDescriptor.getName();

				Field field = ReflectionUtils.getDeclaredField(obj, name);
				if (field != null) {
					if (!Modifier.isPublic(field.getModifiers())) {
						field.setAccessible(true);
					}

					// 获取属性值
					try {
						Object val = field.get(obj);
						if (val != null && checker.isEmpty(val)) {
							field.set(obj, null);
						}
					} catch (Exception e) {
						System.err.println(e.getMessage());
					}
				}
			}
		}
	}

	/**
	 * 移除将对象中String属性的前后空格
	 * 
	 * @author qc_zhong(钟其纯)
	 * 
	 * @param obj
	 */
	public void trimAttr(Object obj) {
		// 获取检查器
		Checker checker = Checker.getInstance();

		// 不为空且不为基本数据类型

		if (obj != null && checker.notPrimitive(obj)) {
			BeanInfo bi = null;
			try {
				bi = Introspector.getBeanInfo(obj.getClass(), Object.class);
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}

			// 获取属性数组
			PropertyDescriptor[] pd = bi.getPropertyDescriptors();
			for (PropertyDescriptor propertyDescriptor : pd) {
				String name = propertyDescriptor.getName();

				Field field = ReflectionUtils.getDeclaredField(obj, name);
				if (field != null) {
					if (!Modifier.isPublic(field.getModifiers())) {
						field.setAccessible(true);
					}

					// 获取属性值
					try {
						Object val = field.get(obj);
						if (val != null && checker.isString(val) && checker.notEmpty(val)) {
							field.set(obj, val.toString().trim());
						}
					} catch (Exception e) {
						System.err.println(e.getMessage());
					}
				}
			}
		}
	}

	/**
	 * 将对象指定的字符串属性进行大小写转换 <br>
	 * 
	 * @author qc_zhong
	 * @param obj
	 * @param attrs
	 * @param caseType
	 */
	public void attrToCase(Object obj, List<String> attrs, CaseType caseType) {
		// 验参
		Checker checker = Checker.getInstance();
		if (checker.isEmpty(obj) && checker.isEmpty(attrs) && checker.isEmpty(caseType)) {
			return;
		}

		for (int i = 0; i < attrs.size(); i++) {
			// 获取field
			Field field = ReflectionUtils.getDeclaredField(obj, attrs.get(i));
			if (field != null) {
				// 设置可修改
				if (!Modifier.isPublic(field.getModifiers())) {
					field.setAccessible(true);
				}

				try {
					Object val = field.get(obj);
					if (val != null && checker.isString(val)) {
						String str = val.toString();
						if (!str.isEmpty()) {
							switch (caseType) {
							case UPPER:
								// 大写
								field.set(obj, str.toUpperCase());
								break;
							case LOWER:
								// 小写
								field.set(obj, str.toLowerCase());
								break;
							case INITCAP:
								// 首字母大写
								String temp = str.toLowerCase();
								StringBuilder sb = new StringBuilder(temp.length());
								sb.append(Character.toUpperCase(temp.charAt(0)));
								sb.append(temp.substring(1));
								field.set(obj, sb.toString());
								break;
							}
						}
					}
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}
	}
}