package dst.ass2.ejb.model.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import dst.ass2.ejb.model.IAuditLog;
import dst.ass2.ejb.model.IAuditParameter;

@Entity
public class AuditParameter implements IAuditParameter {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "parameterindex")
	private Integer parameterIndex;
	
	// TODO add length property
	@Column(name = "type")
	private String type;

	// TODO add length property
	@Column(name = "value")
	private String value;
	
	@ManyToOne(targetEntity = AuditLog.class)
	@JoinColumn(name = "auditlog_id")
	private IAuditLog auditLog;

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Integer getParameterIndex() {
		return this.parameterIndex;
	}

	@Override
	public void setParameterIndex(Integer parameterIndex) {
		this.parameterIndex = parameterIndex;
	}

	@Override
	public String getType() {
		return this.type;
	}

	@Override
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String getValue() {
		return this.value;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public IAuditLog getAuditLog() {
		return this.auditLog;
	}

	@Override
	public void setAuditLog(IAuditLog auditLog) {
		this.auditLog = auditLog;
	}

}
