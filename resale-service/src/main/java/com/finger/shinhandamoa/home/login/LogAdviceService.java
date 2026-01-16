package com.finger.shinhandamoa.home.login;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author  by puki
 * @date    2018. 3. 30.
 * @desc    최초생성
 * @version 
 * 
 */
@Component
@Aspect
public class LogAdviceService {

	private static final Logger logger = LoggerFactory.getLogger(LogAdviceService.class);
	
	@Around("execution(* com.finger.shinhandamoa..*Controller.*(..))"
			//+ " or execution(* com.finger.shinhandamoa..*Impl.*(..))"
			+ " or execution(* com.finger.shinhandamoa..*Impl.*(..))")
	public Object logPrint(ProceedingJoinPoint joinPoint) throws Throwable {
		
		Object result = joinPoint.proceed();
		String type = joinPoint.getSignature().getDeclaringTypeName();
		String name = "";
	
		if(type.indexOf("Controller") > -1) {
			name = "Controller \t: ";
		} else if(type.indexOf("Service") > -1) {
			name = "ServiceImpl \t: ";
		} else if(type.indexOf("DAO") > -1) {
			name = "Dao \t: ";
		}
		
//		logger.info(name + type + "." + joinPoint.getSignature().getName());
		
		return result;
	}
	
}
