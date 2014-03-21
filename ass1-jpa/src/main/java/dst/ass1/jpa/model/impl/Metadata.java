package dst.ass1.jpa.model.impl;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

import dst.ass1.jpa.model.IMetadata;

@Entity
public class Metadata implements IMetadata {
	
	@Id
	private Long id;
	
	@Column(name = "context")
	private String context;
	
	@ElementCollection
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
		return this.settings;
	}

	@Override
	public void setSettings(List<String> settings) {
		this.settings = settings;
	}

	@Override
	public void addSetting(String setting) {
		this.settings.add(setting);
	}

}
