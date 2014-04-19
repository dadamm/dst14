package dst.ass2.util;

import java.util.Date;
import java.util.List;

public class TaskHelperDTO {

	private Long id;
	private String context;
	private Date start;
	private String username;
	private List<String> settings;

	public TaskHelperDTO(Long id, String context, Date start, String username,
			List<String> settings) {
		super();
		this.id = id;
		this.context = context;
		this.start = start;
		this.username = username;
		this.settings = settings;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<String> getSettings() {
		return settings;
	}

	public void setSettings(List<String> settings) {
		this.settings = settings;
	}

	@Override
	public String toString() {
		return "JobHelperDTO [id=" + id + ", context=" + context + ", start="
				+ start + ", username=" + username + ", settings=" + settings + "]";
	}

}
