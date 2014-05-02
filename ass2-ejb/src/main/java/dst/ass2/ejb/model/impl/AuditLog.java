package dst.ass2.ejb.model.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import dst.ass2.ejb.model.IAuditLog;
import dst.ass2.ejb.model.IAuditParameter;

@Entity
public class AuditLog implements IAuditLog {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "method")
	private String method;

	@Column(name = "result")
	private String result;
	
	@Column
	private Date invocationTime;
	
	@OneToMany(mappedBy = "auditLog", targetEntity = AuditParameter.class)
	private List<IAuditParameter> parameters;
	
	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getMethod() {
		return this.method;
	}

	@Override
	public void setMethod(String method) {
		this.method = method;
	}

	@Override
	public String getResult() {
		return this.result;
	}

	@Override
	public void setResult(String result) {
		this.result = result;
	}

	@Override
	public Date getInvocationTime() {
		return this.invocationTime;
	}

	@Override
	public void setInvocationTime(Date invocationTime) {
		this.invocationTime = invocationTime;
	}

	@Override
	public List<IAuditParameter> getParameters() {
		if(parameters == null) {
			return new LinkedList<IAuditParameter>();
		} else return parameters;
	}

	@Override
	public void setParameters(List<IAuditParameter> parameters) {
		this.parameters = parameters;
	}

}
