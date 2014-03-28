package dst.ass1.jpa.model.impl;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import dst.ass1.jpa.model.IMetadata;
import dst.ass1.jpa.util.Constants;

@Entity
@Table(name = Constants.T_METADATA)
public class Metadata implements IMetadata {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "context", length = 50)
	private String context;
	
	@ElementCollection
	@JoinColumn(name = Constants.J_METADATA_SETTINGS)
	private List<String> settings;

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getContext() {
		return this.context;
	}

	@Override
	public void setContext(String description) {
		this.context = description;
	}

	@Override
	public List<String> getSettings() {
		if (settings == null) {
			return this.settings = new LinkedList<String>();
		} else {
			return this.settings;
		}
	}

	@Override
	public void setSettings(List<String> settings) {
		this.settings = settings;
	}

	@Override
	public void addSetting(String setting) {
		getSettings().add(setting);
	}

}
