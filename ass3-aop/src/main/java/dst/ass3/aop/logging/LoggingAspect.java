package dst.ass3.aop.logging;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class LoggingAspect {
	
	private String getLogMessage(String classname, String text) {
		StringBuilder sb = new StringBuilder();
		sb.append("Plugin ");
		sb.append(classname);
		sb.append(" ");
		sb.append(text);
		return sb.toString();
	}
	
	private Logger getLogger(Object object) throws IllegalArgumentException, IllegalAccessException {
		Class<?> clazz = object.getClass();
		for(Field field : clazz.getFields()) {
			if(Logger.class.isAssignableFrom(field.getType())) {
				Logger logger = (Logger) field.get(object);
				logger.setLevel(Level.INFO);
				return logger;
			}
		}
		return null;
	}
	
	@Pointcut("execution(* dst.ass3.aop.IPluginExecutable.execute(..)) && !@annotation(Invisible)")
	public void procCall() {
		
	}
	
	@Before("procCall()")
	public void beforeExecution(JoinPoint joinPoint) {
		Logger logger = null;
		String message = getLogMessage(joinPoint.getTarget().getClass().getCanonicalName(), " started to execute");
		try {
			logger = getLogger(joinPoint.getTarget());
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(logger == null) {
			System.out.println(message);
		} else {
			logger.info(message);
		}
	}
	
	@After("procCall()")
	public void afterExecution(JoinPoint joinPoint) {
		Logger logger = null;
		String message = getLogMessage(joinPoint.getTarget().getClass().getCanonicalName(), " is finished");
		try {
			logger = getLogger(joinPoint.getTarget());
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(logger == null) {
			System.out.println(message);
		} else {
			logger.info(message);
		}
	}
	
}
