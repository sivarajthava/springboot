package com.rabo.customer.statement.logging;

import org.apache.tomcat.util.buf.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Logs the method enter and exit details for trace purpose
 * 
 * @author Sivaraj
 *
 */
@Aspect
@Component
@Slf4j
public class ReportLoggingAspect {

	@Around("execution(* com.rabo.customer.statement.controller..*(..)))")
	public Object profileControllerMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		return aspectLog(proceedingJoinPoint);
	}

	@Around("execution(* com.rabo.customer.statement.service..*(..)))")
	public Object profileServiceMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		return aspectLog(proceedingJoinPoint);
	}

	@Around("execution(* com.rabo.customer.statement.business..*(..)))")
	public Object profileBusinessMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		return aspectLog(proceedingJoinPoint);
	}

	private Object aspectLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
		// Get intercepted method details
		String className = methodSignature.getDeclaringType().getCanonicalName();
		String methodName = methodSignature.getName();
		String[] parameterNames = methodSignature.getParameterNames();
		// Log method before enter
		log.info("Execution of " + className + "." + methodName + "(" + StringUtils.join(parameterNames) + ") - Begin");
		Object result = proceedingJoinPoint.proceed();
		// Log method after completion of execution
		log.info("Execution  of " + className + "." + methodName + "(" + StringUtils.join(parameterNames) + ") - End");
		return result;
	}
}
