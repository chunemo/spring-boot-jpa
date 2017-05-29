package com.qiqi.tool.poi;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.OldExcelFormatException;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.qiqi.exception.poi.PoiException;
import com.qiqi.exception.poi.PoiException.Ex;
import com.qiqi.tool.bean.Checker;

/**
 * POI工具类
 * 
 * @author qc_zhong
 */
public class PoiUtil {
	private static PoiUtil instance;
	private static final String NODE = ".";
	private static final String LEFT_SQUARE = "[";
	private static final String RIGHT_SQUARE = "]";
	private static final String SPLIT_SQUARE_COMMA = "[\\[,]";

	private PoiUtil() {

	}

	public static PoiUtil getInstance() {
		if (instance == null) {
			synchronized (PoiUtil.class) {
				if (instance == null) {
					instance = new PoiUtil();
				}
			}
		}
		return instance;
	}

	/**
	 * 对象列表->字符串数组列表 <br>
	 * 适用于级联属性列表连续排列的情况
	 *
	 * @author qc_zhong
	 * @param elements
	 * @param properties
	 * @param castMap
	 * @return
	 */
	public <T> List<String[]> buildContents(List<T> elements, List<String> properties, Map<String, List<String>> cascadeMap, Map<String, CastConverter> castMap) {
		List<String[]> list = new ArrayList<String[]>();

		if (elements != null && elements.size() > 0) {
			if (properties != null && properties.size() > 0) {
				for (int elementIndex = 0; elementIndex < elements.size(); elementIndex++) {
					T t = elements.get(elementIndex);

					List<String> content = buildContent(t, properties, cascadeMap, castMap);
					list.add(content.toArray(new String[] {}));
				}
			}
		}

		return list;
	}

	/**
	 * 对象->字符串列表 <br>
	 * 适用于级联属性列表连续排列的情况
	 *
	 * @author qc_zhong
	 * @param t
	 * @param properties
	 * @param cascadeMap
	 * @param castMap
	 * @return
	 */
	public <T> List<String> buildContent(T t, List<String> properties, Map<String, List<String>> cascadeMap, Map<String, CastConverter> castMap) {
		List<String> content = new ArrayList<String>();
		if (notEmpty(properties)) {
			if (notEmpty(t)) {
				// 对象
				BeanInfo bi = null;
				try {
					bi = Introspector.getBeanInfo(t.getClass(), Object.class);
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}

				PropertyDescriptor[] pd = bi.getPropertyDescriptors();
				// to map
				Map<String, PropertyDescriptor> propertyMap = new HashMap<String, PropertyDescriptor>();
				for (int i = 0; i < pd.length; i++) {
					propertyMap.put(pd[i].getName(), pd[i]);
				}

				for (Iterator<String> it = properties.iterator(); it.hasNext();) {
					String name = it.next();
					PropertyDescriptor propertyDescriptor = propertyMap.get(name);

					// getter
					Method readMethod = propertyDescriptor == null ? null : propertyDescriptor.getReadMethod();
					if (readMethod != null) {
						if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
							readMethod.setAccessible(true);
						}
						// 获取属性值
						Object val = null;
						try {
							val = readMethod.invoke(t);
						} catch (Exception e) {
							System.err.println(e.getMessage());
						}
						// get cascade properties
						List<String> props = cascadeMap == null ? null : cascadeMap.get(name);
						// resolve val
						if (props == null) {
							// get cast converter
							CastConverter cast = castMap == null ? null : castMap.get(name);
							if (cast != null) {
								// 使用指定类型转换器
								content.add(notEmpty(val) ? cast.cast(val) : "");
							} else {
								// 使用String类转换
								content.add(notEmpty(val) ? String.valueOf(val) : "");
							}
						} else {
							// 级联递归
							content.addAll(buildContent(val, props, cascadeMap, castMap));
						}
					}
				}
			} else {
				// 空对象，根据属性列表填空字符串
				for (int i = 0; i < properties.size(); i++) {
					content.add("");
				}
			}
		} else {
			// 属性列表为空
			content.add(t == null ? "" : String.valueOf(t));
		}
		return content;
	}

	/**
	 * 对象列表->字符串数组列表 <br>
	 * 属性列表支持级联形式，eg: user.name<br>
	 * 适用于级联属性列表不连续排列的情况 <br>
	 * 注意: 保证级联对象不为空，以保证产生的结果数据一致
	 *
	 * @author qc_zhong
	 * @param elements
	 * @param properties
	 * @param castMap
	 * @return
	 */
	public <T> List<String[]> buildContents(List<T> elements, List<String> properties, Map<String, CastConverter> castMap) {
		List<String[]> list = new ArrayList<String[]>();

		if (elements != null && elements.size() > 0) {
			if (properties != null && properties.size() > 0) {
				for (int elementIndex = 0; elementIndex < elements.size(); elementIndex++) {
					T t = elements.get(elementIndex);

					List<String> content = buildContent(t, properties, castMap);
					list.add(content.toArray(new String[] {}));
				}
			}
		}

		return list;
	}
	
	/**
	 * 对象列表->字符串数组列表 <br>
	 * 属性列表支持级联形式，eg: user.name<br>
	 * 适用于级联属性列表不连续排列的情况 <br>
	 * 注意: 保证级联对象不为空，以保证产生的结果数据一致
	 *
	 * @author qc_zhong
	 * @param contents
	 * @param elements
	 * @param properties
	 * @param castMap
	 */
	public <T> void buildContents(List<String[]> contents, List<T> elements, List<String> properties, Map<String, CastConverter> castMap) {
		if (isEmpty(contents)) {
			contents = new ArrayList<String[]>();
		}

		if (elements != null && elements.size() > 0) {
			if (properties != null && properties.size() > 0) {
				for (int elementIndex = 0; elementIndex < elements.size(); elementIndex++) {
					T t = elements.get(elementIndex);

					List<String> content = buildContent(t, properties, castMap);
					contents.add(content.toArray(new String[] {}));
				}
			}
		}
	}

	/**
	 * 对象->字符串列表 <br>
	 * 属性列表支持级联形式，eg: user.name, user[loginName, name]<br>
	 * 注意: 保证级联对象不为空，以保证产生的结果数据一致
	 *
	 * @author qc_zhong
	 * @param t
	 * @param properties
	 * @param castMap
	 * @return
	 */
	public <T> List<String> buildContent(T t, List<String> properties, Map<String, CastConverter> castMap) {
		List<String> content = new ArrayList<String>();
		if (notEmpty(properties)) {
			if (notEmpty(t)) {
				// 对象
				BeanInfo bi = null;
				try {
					bi = Introspector.getBeanInfo(t.getClass(), Object.class);
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
				PropertyDescriptor[] pd = bi.getPropertyDescriptors();
				// to map
				Map<String, PropertyDescriptor> propertyMap = new HashMap<String, PropertyDescriptor>();
				for (int i = 0; i < pd.length; i++) {
					propertyMap.put(pd[i].getName(), pd[i]);
				}
				for (Iterator<String> it = properties.iterator(); it.hasNext();) {
					String name = it.next();
					// 级联处理
					List<String> props = null; // new properties
					boolean flag = false; // 级联标志
					if (name.indexOf(NODE) > 0) {
						int index = name.indexOf(NODE);
						// 剩余部分
						String other = name.substring(index + 1, name.length());
						// 更新属性名
						name = name.substring(0, index); // 主属性名
						// 标志级联
						flag = true;
						// 构建新属性列表
						props = Arrays.asList(other);
					} else if (name.indexOf(LEFT_SQUARE) > 0) {
						// replace
						name = name.replace(RIGHT_SQUARE, "");
						// split
						String[] arr = name.split(SPLIT_SQUARE_COMMA);
						// 更新属性名
						name = arr[0];
						// 标志级联
						flag = true;
						// 构建新属性列表
						props = new ArrayList<String>();
						for (int i = 1; i < arr.length; i++) {
							props.add(arr[i].trim());
						}
					}
					// getter
					PropertyDescriptor propertyDescriptor = propertyMap.get(name);
					Method readMethod = propertyDescriptor == null ? null : propertyDescriptor.getReadMethod();
					if (readMethod != null) {
						if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
							readMethod.setAccessible(true);
						}
						// 获取属性值
						Object val = null;
						try {
							val = readMethod.invoke(t);
						} catch (Exception e) {
							System.err.println(e.getMessage());
						}
						// add to content
						if (flag) {
							// 级联递归
							content.addAll(buildContent(val, props, castMap));
						} else {
							// get cast converter
							CastConverter cast = castMap == null ? null : castMap.get(name);
							if (cast != null) {
								// 使用指定类型转换器
								content.add(notEmpty(val) ? cast.cast(val) : "");
							} else {
								// 使用String类转换
								content.add(notEmpty(val) ? String.valueOf(val) : "");
							}
						}
					}
				}
			} else {
				// 空对象，根据属性列表填空字符串
				content.addAll(fill(properties));
			}
		} else {
			// 属性列表为空
			content.add("");
		}
		return content;
	}
	
	/**
	 * 填充空值 <br>
	 * 可保证中间级联属性为空时数据的完整性
	 *
	 * @author qc_zhong
	 * @param properties
	 * @return
	 */
	private List<String> fill(List<String> properties) {
		List<String> contents = new ArrayList<String>();
		if (notEmpty(properties)) {
			for (int index = 0; index < properties.size(); index++) {
				String property = properties.get(index);
				boolean flag = false; // 级联标志
				List<String> props = null; // new properties
				if (property.indexOf(NODE) > 0) {
					int nodeindex = property.indexOf(NODE);
					// 剩余部分
					String other = property.substring(nodeindex + 1, property.length());
					// 更新属性名
					property = property.substring(0, nodeindex); // 主属性名
					// 标志级联
					flag = true;
					// 构建新属性列表
					props = Arrays.asList(other);
				} else if (property.indexOf(LEFT_SQUARE) > 0) {
					// replace
					property = property.replace(RIGHT_SQUARE, "");
					// split
					String[] arr = property.split(SPLIT_SQUARE_COMMA);
					// 更新属性名
					property = arr[0];
					// 标志级联
					flag = true;
					// 构建新属性列表
					props = new ArrayList<String>();
					for (int newIndex = 1; newIndex < arr.length; newIndex++) {
						props.add(arr[newIndex].trim());
					}
				}
				if (flag) {
					contents.addAll(fill(props));
				} else {
					contents.add("");
				}
			}
		}
		return contents;
	}

	/**
	 * put, 支持级联
	 *
	 * @author qc_zhong
	 * @param json
	 * @param property
	 * @param val
	 */
	private void put(JSONObject json, String property, Object val) {
		if (json != null && notEmpty(property)) {
			int index = property.indexOf(NODE);
			if (index > 0) {
				String name = property.substring(0, index); // 主属性名
				String other = property.substring(index + 1, property.length()); // 剩余部分
				Object obj = json.get(name);
				if (notEmpty(name) && notEmpty(other)) {
					if (obj == null) {
						// 新增json
						JSONObject jsonobj = new JSONObject();
						json.put(name, jsonobj);
						put(jsonobj, other, val);
					} else {
						// 原json上继续添加
						if (obj instanceof JSONObject) {
							JSONObject jsonobj = (JSONObject) obj;
							json.put(name, jsonobj);
							// 递归
							put(jsonobj, other, val);
						}
					}
				}
			} else {
				// 不含.
				// 直接为当前json添加属性
				json.put(property, val);
			}
		}
	}
	
	/**
	 * get, 支持级联
	 *
	 * @author qc_zhong
	 * @param json
	 * @param property
	 * @return
	 */
	private Object get(JSONObject json, String property) {
		if (json != null && notEmpty(property)) {
			int index = property.indexOf(NODE);
			if (index > 0) {
				String name = property.substring(0, index); // 主属性名
				String other = property.substring(index + 1, property.length()); // 剩余部分
				Object obj = json.get(name);
				if (obj != null && obj instanceof JSONObject && notEmpty(other)) {
					return get((JSONObject) obj, other);
				}
			} else {
				// 不含.
				// 直接取出值
				return json.get(property);
			}
		}
		return null;
	}
	
	/**
	 * 获取取合并单元格的第一个单元格
	 *
	 * @author qc_zhong
	 * @param sheet
	 * @param row
	 * @param column
	 * @return
	 */
	public Cell getMergedCell(Sheet sheet, int row, int column) {
		int mergeCount = sheet.getNumMergedRegions();
		for (int i = 0; i < mergeCount; i++) {
			CellRangeAddress mergedRegion = sheet.getMergedRegion(i);
			int firstColumn = mergedRegion.getFirstColumn();
			int lastColumn = mergedRegion.getLastColumn();
			int firstRow = mergedRegion.getFirstRow();
			int lastRow = mergedRegion.getLastRow();

			if (row >= firstRow && row <= lastRow) {
				if (column >= firstColumn && column <= lastColumn) {
					Row fRow = sheet.getRow(firstRow);
					return fRow.getCell(firstColumn);
				}
			}
		}

		return null;
	}
	
	/**
	 * 合并单元格
	 * 
	 * @param sheet
	 * @param firstRow
	 * @param lastRow
	 * @param firstCol
	 * @param lastCol
	 */
	public void mergeRegion(Sheet sheet, int firstRow, int lastRow, int firstCol, int lastCol) {
		sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
	}

	/**
	 * 创建工作薄
	 *
	 * @author qc_zhong
	 * @param title
	 * @param contents
	 * @return
	 */
	public Workbook buildWorkbook(String title, List<String[]> contents) {
		// 声明一个工作薄
		Workbook workbook = new HSSFWorkbook();
		// 生成一个表格
		Sheet sheet = workbook.createSheet(title);
		// 生成一个样式
		CellStyle style = workbook.createCellStyle();
		// 写入内容
		if (contents != null && contents.size() > 0) {
			for (int rowIndex = 0; rowIndex < contents.size(); rowIndex++) {
				Row row = sheet.createRow(rowIndex);
				String[] content = contents.get(rowIndex);

				for (int columnIndex = 0; columnIndex < content.length; columnIndex++) {
					Cell cell = row.createCell(columnIndex);
					cell.setCellStyle(style);
					HSSFRichTextString text = new HSSFRichTextString(content[columnIndex]);
					cell.setCellValue(text);
				}
			}
		}
		// return
		return workbook;
	}

	/**
	 * 添加表格
	 *
	 * @author qc_zhong
	 * @param workbook
	 * @param title
	 * @param contents
	 */
	public void addSheet(Workbook workbook, String title, List<String[]> contents) {
		// 生成一个表格
		Sheet sheet = workbook.createSheet(title);
		// 生成一个样式
		CellStyle style = workbook.createCellStyle();
		// 写入内容
		if (contents != null && contents.size() > 0) {
			for (int rowIndex = 0; rowIndex < contents.size(); rowIndex++) {
				Row row = sheet.createRow(rowIndex);
				String[] content = contents.get(rowIndex);

				for (int columnIndex = 0; columnIndex < content.length; columnIndex++) {
					Cell cell = row.createCell(columnIndex);
					cell.setCellStyle(style);
					HSSFRichTextString text = new HSSFRichTextString(content[columnIndex]);
					cell.setCellValue(text);
				}
			}
		}
	}

	/**
	 * 逐行解析表格 <br>
	 * 支持级联属性, eg: user.name
	 *
	 * @author qc_zhong
	 * @param is
	 * @param sheetName
	 * @param properties
	 * @return
	 * @throws Exception
	 */
	public List<JSONObject> parseExcelByRow(InputStream is, String sheetName, List<String> properties) throws Exception {
		List<JSONObject> jsons = new ArrayList<JSONObject>();

		if (notEmpty(properties)) {
			Workbook workbook = null;
			try {
				workbook = WorkbookFactory.create(is);
			} catch (OldExcelFormatException e) {
				// The supplied spreadsheet seems to be Excel 5.0/7.0 (BIFF5) format.
				Ex ex = Ex.EX_POI_OLD_WORKBOOK;
				throw new PoiException(ex);
			} catch (InvalidFormatException e) {
				// Your InputStream was neither an OLE2 stream, nor an OOXML stream
				Ex ex = Ex.EX_POI_INVALID_FORMAT;
				throw new PoiException(ex);
			}
			Sheet sheet = null;
			if (workbook != null) {
				if (notEmpty(sheetName)) {
					// 获取指定表名的工作表
					sheet = workbook.getSheet(sheetName);
				} else {
					// 获取第一张工作表
					sheet = workbook.getSheetAt(0);
				}
			}
			if (sheet != null) {
				// 遍历工作行
				int rows = sheet.getLastRowNum();
				for (int rowIndex = 0; rowIndex <= rows; rowIndex++) {
					// get row
					Row row = sheet.getRow(rowIndex);
					// new json
					JSONObject json = new JSONObject();
					// 遍历单元格
					short cells = row.getLastCellNum();
					for (int columnIndex = 0; columnIndex < cells; columnIndex++) {
						Cell cell = row.getCell(columnIndex);
						// get property
						String property = null;
						if (columnIndex < properties.size()) {
							// 读取合并单元格的第一个单元格
							Cell mergedCell = getMergedCell(sheet, cell.getRowIndex(), cell.getColumnIndex());
							// 如果当前单元格是合并单元格, 重置为合并单元格的第一个单元格
							cell = mergedCell == null ? cell : mergedCell;
							
							property = properties.get(columnIndex);
							if (property.indexOf(NODE) > 0) {
								// 级联属性
								cell.setCellType(CellType.STRING);
								String val = cell.getStringCellValue();
								put(json, property, val == null ? "" : val.trim());
							} else {
								// fill json
								cell.setCellType(CellType.STRING);
								String val = cell.getStringCellValue();
								json.put(property, val == null ? "" : val.trim());
							}
						} else {
							break;
						}
					}
					// fill json
					// add
					jsons.add(json);
				}
			}
			// 关闭工作薄
			if (workbook != null) {
				try {
					workbook.close();
				} catch (Exception e) {
					System.err.println(workbook);
				}
			}
		}
		// return
		return jsons;
	}
	
	/**
	 * JSON列表->Bean列表<br>
	 * 支持级联属性, eg: user.name
	 *
	 * @author qc_zhong
	 * @param clazz
	 * @param jsons
	 * @param properties
	 * @param castMap
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> parseJsons(Class<T> clazz, List<JSONObject> jsons, List<String> properties, Map<String, CastConverter> castMap) throws Exception {
		List<T> list = new ArrayList<T>();
		if (notEmpty(jsons)) {
			// title
			JSONObject title = jsons.get(0);
			// cast convert special property
			// check special property format
			// strat from index 1
			for (int jsonIndex = 1; jsonIndex < jsons.size(); jsonIndex++) {
				JSONObject json = jsons.get(jsonIndex);
				// 特定属性使用指定类型转换器
				if (notEmpty(castMap)) {
					for (Iterator<Entry<String, CastConverter>> it = castMap.entrySet().iterator(); it.hasNext();) {
						Entry<String, CastConverter> entry = it.next();
						String key = entry.getKey();
						CastConverter cast = castMap.get(key);
						if (cast != null) {
							// get val
							Object val = get(json, key);
							// cast
							if (val != null) {
								Object obj = cast.cast(val.toString());
								json.put(key, obj);
							}
						}
					}
				}
				// check format
				if (notEmpty(properties)) {
					for (int checkIndex = 0; checkIndex < properties.size(); checkIndex++) {
						// get property
						String key = properties.get(checkIndex);
						// get val
						Object val = get(json, key);
						// check
						if (isEmpty(val)) {
							// 异常参数
							List<Object> args = new ArrayList<Object>();
							args.add((jsonIndex + 1)); // 第n行
							args.add(title.get(key)); // 标题
							// message
							StringBuilder msg = new StringBuilder();
							msg.append("第");
							msg.append((jsonIndex + 1));
							msg.append("行的");
							msg.append(title.get(key));
							msg.append("格式不正确。");
							// throw
							throw new PoiException(msg.toString(), Ex.EX_POI, args);
						}
					}
				}
				// parse json
				String jsonString = JSON.toJSONString(json);
				list.add(JSON.parseObject(jsonString, clazz));
			}
		}
		// return
		return list;
	}
	
	/**
	 * 检查工作表标题、内容是否为空
	 *
	 * @author qc_zhong
	 * @param jsons
	 */
	public void check(List<JSONObject> jsons) {
		if (isEmpty(jsons)) {
			// 空表
			Ex ex = Ex.EX_POI_TITLE_EMPTY;
			throw new PoiException(ex);
		}
		if (jsons.size() == 1) {
			// 内容为空
			Ex ex = Ex.EX_POI_CONTENT_EMPTY;
			throw new PoiException(ex);
		}
	}

	/**
	 * 检查标题行
	 *
	 * @author qc_zhong
	 * @param title
	 * @param titleMap
	 */
	public void checkTitle(JSONObject title, Map<String, String> titleMap) {
		if (title != null) {
			if (notEmpty(titleMap)) {
				for (Iterator<Entry<String, String>> it = titleMap.entrySet().iterator(); it.hasNext();) {
					Entry<String, String> entry = it.next();
					String key = entry.getKey();
					Object val = title.get(key);
					if (isEmpty(val)) {
						// 异常参数
						List<Object> args = new ArrayList<Object>();
						args.add(entry.getValue()); // 标题
						// message
						StringBuilder msg = new StringBuilder();
						msg.append("请填写标题");
						msg.append(entry.getValue());
						// throw
						throw new PoiException(msg.toString(), Ex.EX_POI_TITLE, args);
					}
					// 检查标题内容是否一致
					if (entry.getValue() != null && !entry.getValue().equals(val.toString().trim())) {
						// 异常参数
						List<Object> args = new ArrayList<Object>();
						args.add(val.toString());
						args.add(entry.getValue()); // 标题
						// message
						StringBuilder msg = new StringBuilder();
						msg.append("标题填写错误：");
						msg.append(val.toString());
						msg.append("/");
						msg.append(entry.getValue());
						// throw
						throw new PoiException(msg.toString(), Ex.EX_POI_TITLE_ERROR, args);
					}
				}
			}
		}
	}
	
	/**
	 * 检查内容是否为空
	 *
	 * @author qc_zhong
	 * @param jsons
	 * @param properties
	 */
	public void checkEmpty(List<JSONObject> jsons, List<String> properties) {
		if (notEmpty(jsons) && notEmpty(properties)) {
			// title
			JSONObject title = jsons.get(0);
			// cast convert special property
			// check special property format
			// strat from index 1
			for (int jsonIndex = 1; jsonIndex < jsons.size(); jsonIndex++) {
				JSONObject json = jsons.get(jsonIndex);
				// check empty
				for (int checkIndex = 0; checkIndex < properties.size(); checkIndex++) {
					// get property
					String key = properties.get(checkIndex);
					// get val
					Object val = get(json, key);
					// check
					if (isEmpty(val)) {
						// 异常参数
						List<Object> args = new ArrayList<Object>();
						args.add((jsonIndex + 1)); // 第n行
						args.add(title.get(key)); // 标题
						// message
						StringBuilder msg = new StringBuilder();
						msg.append("请填写第");
						msg.append((jsonIndex + 1));
						msg.append("行");
						msg.append(title.get(key));
						msg.append("。");
						// throw
						throw new PoiException(msg.toString(), Ex.EX_POI_TITLE_ERROR, args);
					}
				}
			}
		}
	}

	private boolean isEmpty(Object obj) {
		return Checker.getInstance().isEmpty(obj);
	}

	private boolean notEmpty(Object obj) {
		return Checker.getInstance().notEmpty(obj);
	}
}