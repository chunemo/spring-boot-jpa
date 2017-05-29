package com.qiqi.event;

import org.springframework.context.ApplicationEvent;


/**
 * 事件demo
 * 
 * @author qc_zhong
 */
public class DemoEvent extends ApplicationEvent 
{
	 private static final long serialVersionUID = 1L;
	private String msg; 

	public DemoEvent(Object source) {
		super(source);
	}

	public DemoEvent(Object source, String msg) {
		super(source);
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}