package gov.va.ocp.vetservices.claims.api.model.v1;

public class ClaimInfo {
	private String id;
	private AttributesInfo attributesInfo;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public AttributesInfo getAttributesInfo() {
		return attributesInfo;
	}

	public void setAttributesInfo(AttributesInfo attributesInfo) {
		this.attributesInfo = attributesInfo;
	}
}
