package com.qiqi.scedule;

import org.springframework.stereotype.Component;

@Component
public class DemoTask {
	/**
	 * 隔固定时间执行
	 *
	 * @author qc_zhong
	 */
	//@Scheduled(fixedRate = 5000)
	public void fixedTask() {
		System.out.println("task");
	}

	/**
	 * 指定时间执行
	 *
	 * @author qc_zhong
	 */
	//@Scheduled(cron = "0 0 1 * * ?")
	public void cronTask() {

	}
}