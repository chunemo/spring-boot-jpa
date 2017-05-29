package com.qiqi.config.spring.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.qiqi.tool.bean.Checker;

/**
 * service切面
 * 
 * @author qc_zhong(钟其纯)
 */
@Aspect
@Component
public class ServiceAspect {
	/**
	 * 切入点：所有Service
	 *
	 * @author qc_zhong
	 */
	@Pointcut("execution(public * com.qiqi.service..*.*(..))")
	public void argmentHandle() {
	}

	/**
	 * 前置通知 <br>
	 * 统一处理空属性 <br>
	 * 统一处理前后空格
	 *
	 * @author qc_zhong
	 * @param joinPoint
	 */
	@Before("argmentHandle()")
	public void before(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		for (int i = 0; i < args.length; i++) {
			// 处理空属性
			Checker.getInstance().checkEmpty(args[i]);
		}
	}
}