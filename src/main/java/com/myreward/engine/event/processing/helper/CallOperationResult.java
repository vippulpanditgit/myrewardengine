package com.myreward.engine.event.processing.helper;

public class CallOperationResult extends OperationResultDO {
	private String labelHashValue;
	private String labelVersion;
	public String getLabelHashValue() {
		return labelHashValue;
	}
	public void setLabelHashValue(String labelHashValue) {
		this.labelHashValue = labelHashValue;
	}
	public String getLabelVersion() {
		return labelVersion;
	}
	public void setLabelVersion(String labelVersion) {
		this.labelVersion = labelVersion;
	}

}
