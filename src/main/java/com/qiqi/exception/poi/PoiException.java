package com.qiqi.exception.poi;

import java.util.List;

import com.qiqi.exception.BaseException;

/**
 * POI异常
 * 
 * @author qc_zhong
 */
public class PoiException extends BaseException {
	private static final long serialVersionUID = 1L;

	public PoiException(String message) {
		super(message);
	}

	public PoiException(String message, String responseCode) {
		super(message, responseCode);
	}

	public PoiException(String message, Ex ex) {
		super(message, ex.name());
	}

	public PoiException(String message, Ex ex, List<Object> args) {
		super(message, ex.name(), args);
	}

	public PoiException(Ex ex) {
		super(ex.getMessage(), ex.name());
	}

	public enum Ex {
		EX_POI(""), //
		EX_POI_TITLE(""), //
		EX_POI_TITLE_ERROR("标题填写错误。"), //
		EX_POI_OLD_WORKBOOK("不支持97之前版的Excel。"), //
		EX_POI_NOT_FOUND("找不到指定的文件。"), //
		EX_POI_TITLE_EMPTY("请填写工作表标题。"), //
		EX_POI_CONTENT_EMPTY("请填写工作表内容。"), //
		EX_POI_INVALID_FORMAT("请上传表格文件。");

		private final String message;

		private Ex(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}
	}
}