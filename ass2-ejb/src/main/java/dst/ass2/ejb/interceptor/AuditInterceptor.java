package dst.ass2.ejb.interceptor;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import dst.ass2.ejb.model.IAuditLog;
import dst.ass2.ejb.model.IAuditParameter;
import dst.ass2.ejb.model.impl.AuditLog;
import dst.ass2.ejb.model.impl.AuditParameter;

public class AuditInterceptor {
	
	@PersistenceContext private EntityManager entityManager;

	@AroundInvoke
	public Object auditInterceptor(InvocationContext ctx) throws Exception {
		IAuditLog auditLog = new AuditLog();
		auditLog.setInvocationTime(new Date());
		auditLog.setMethod(ctx.getMethod().getName());
		
		Object[] parameters = ctx.getParameters();
		if (parameters != null) {
			List<IAuditParameter> auditParameters = new LinkedList<IAuditParameter>();
			for (int i = 0; i < parameters.length; i++) {
				IAuditParameter auditParameter = new AuditParameter();
				auditParameter.setAuditLog(auditLog);
				auditParameter.setParameterIndex(i);
				auditParameter.setType(parameters[i] == null ? "null" : parameters[i].getClass().toString());
				auditParameter.setValue(parameters[i] == null ? "null": parameters[i].toString());
				auditParameters.add(auditParameter);
//				entityManager.persist(auditParameter);
			}
			auditLog.setParameters(auditParameters);
		}
		Object result = null;
		
		try {
			result = ctx.proceed();
			auditLog.setResult(result == null ? "null" : result.toString());
			return result;
		} catch (Exception e) {
			auditLog.setResult(e.toString());
			throw e;
		} finally {
			entityManager.persist(auditLog);
			for(IAuditParameter p : auditLog.getParameters()) {
				entityManager.persist(p);
			}
			entityManager.flush();
		}
	}

}
