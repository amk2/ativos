package com.projtec.slingcontrol.infra;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class LogAspect
{
	  Logger LOG = LoggerFactory.getLogger(LogAspect.class);
	  
	  @AfterThrowing(pointcut="execution(* com.projtec.slingcontrol.gerencia.*.*(..))" , throwing="ex"  )
	  public void doLogError(RuntimeException ex) 
	  {  
		  LOG.error(ex.getMessage() , ex);    
	  }	  
	  
	  
	  @Before("execution(* com.projtec.slingcontrol.gerencia.*.*(..))")
	  public void logJoinPoint(JoinPoint joinPoint) 
	  {
			  LOG.info("Join point kind : "
		  + joinPoint.getKind());
			  LOG.info("Signature declaring type : "
		  + joinPoint.getSignature().getDeclaringTypeName());
			  LOG.info("Signature name : "
		  + joinPoint.getSignature().getName());
			  LOG.info("Arguments : "
		  + Arrays.toString(joinPoint.getArgs()));
			  LOG.info("Target class : "
		  + joinPoint.getTarget().getClass().getName());
			  LOG.info("This class : "
		  + joinPoint.getThis().getClass().getName());
	  }
	  
//	  @Around("within(com.projtec.slingcontrol.persistencia..*)")
//	  public Object doLogDAO(ProceedingJoinPoint pjp) throws Throwable {
//	   		  
//	    //Object retVal = pjp.proceed();
//	    String nomeClasse =  pjp.getSignature().getName() ;
//	    String nomeMetodo = pjp.getTarget().getClass().getName() ;
//	    LOG.info("<INICIO>Executando o metodo: " + nomeClasse  +"->" + nomeMetodo );
//	    Object val= pjp.proceed();
//	    LOG.info("<FIM> Executando o metodo: "  + nomeClasse  +"->" + nomeMetodo );
//	    return val;
//	    
//	  }

	  
	 


	


}
