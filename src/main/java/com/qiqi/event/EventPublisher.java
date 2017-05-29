package com.qiqi.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

/**
 * 事件发布器
 * 
 * @author qc_zhong
 */
@Component
public class EventPublisher {
	@Autowired
	private ApplicationContext applicationContext;

	public void publish(ApplicationEvent event) {
		applicationContext.publishEvent(event);
	}
}