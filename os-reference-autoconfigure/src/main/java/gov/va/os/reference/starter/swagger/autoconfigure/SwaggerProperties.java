package gov.va.os.reference.starter.swagger.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by vgadda on 8/3/17.
 */

@ConfigurationProperties(prefix = "reference.swagger")
public class SwaggerProperties {

	private boolean enabled = true;

	private String securePaths = "[Api secure paths via reference.swagger.securePaths]";

	private String groupName = "[Api Group Name via reference.swagger.groupName]";

	private String title = "[Api title via 'reference.swagger.title']";

	private String description = "[Api description via 'reference.swagger.description']";

	private String version = "[Api version via 'reference.swagger.version']";

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getSecurePaths() {
		return securePaths;
	}

	public void setSecurePaths(String securePaths) {
		this.securePaths = securePaths;
	}
}
