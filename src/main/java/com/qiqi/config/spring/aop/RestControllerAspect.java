package com.qiqi.config.spring.aop;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.qiqi.tool.Parser;
import com.qiqi.tool.bean.Checker;
import com.qiqi.tool.bean.Converter;

/**
 * RestController切面
 * 
 * @author qc_zhong
 *
 */
@Aspect
@Component
@Slf4j
public class RestControllerAspect {
	/**
	 * 切入点：所有RestController
	 *
	 * @author qc_zhong
	 */
	@Pointcut("execution(public * com.qiqi.controller.rest..*.*(..))")
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
		log.info("RestControllerAspect: " + joinPoint.getSignature().getName());
		Object[] args = joinPoint.getArgs();

		Checker checker = Checker.getInstance();
		Converter converter = Converter.getInstance();

		for (int i = 0; i < args.length; i++) {
			if (checker.isEmpty(args[i])) {
				continue;
			}
			// 字符串
			if (checker.isString(args[i])) {
				continue;
			}
			// 日期
			if (checker.isDate(args[i])) {
				continue;
			}
			// http请求
			if (checker.isRequest(args[i])) {
				continue;
			}
			// http响应
			if (checker.isResponse(args[i])) {
				continue;
			}

			// 处理空属性
			converter.emptyAttrToNull(args[i]);
			// 处理前后空格
			converter.trimAttr(args[i]);
			// 日志
			log.info(Parser.parseJson(args[i]));
		}
	}
}
