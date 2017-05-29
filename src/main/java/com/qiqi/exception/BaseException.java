package com.qiqi.exception;

import java.util.List;

/**
 * 公共异常类
 * 
 * @author qc_zhong(钟其纯)
 *
 */
public class BaseException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private String i18nCode;
	
	private List<Object> args;

	public BaseException(String message) {
		super(message);
	}

	public BaseException(String message, String i18nCode) {
		super(message);
		this.i18nCode = i18nCode;
	}
	
	public BaseException(String message, String i18nCode, List<Object> args) {
		super(message);
		this.i18nCode = i18nCode;
		this.args = args;
	}

	public BaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public BaseException(Throwable cause) {
		super(cause);
	}

	protected BaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public BaseException(Ex ex) {
		super(ex.getMessage());
		this.i18nCode = ex.name();
	}

	public String getI18nCode() {
		return i18nCode;
	}

	public void setI18nCode(String i18nCode) {
		this.i18nCode = i18nCode;
	}

	public List<Object> getArgs() {
		return args;
	}

	public void setArgs(List<Object> args) {
		this.args = args;
	}

	public enum Ex {
		// TODO 以下CODE需要写在国际化资源文件中
		EX_RETRY("稍候请重试"), //
		EX_FAIL("操作失败"), //
		EX_MODIFY_FAIL("修改失败"), //
		EX_ACTIVATE_FAIL("启用失败"), //
		EX_CANCEL_FAIL("禁用失败");

		private final String message;

		private Ex(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}
	}
}
