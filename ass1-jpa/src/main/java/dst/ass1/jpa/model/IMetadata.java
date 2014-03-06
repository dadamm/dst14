package dst.ass1.jpa.model;

import java.util.List;

public interface IMetadata {
	
	public Long getId();

	public void setId(Long id);

	public String getContext();

	public void setContext(String description);

	public List<String> getSettings();

	public void setSettings(List<String> settings);

	public void addSetting(String setting);

}
