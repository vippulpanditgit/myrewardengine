package com.myreward.engine.audit;

public enum AuditEventType {
	AUDIT_EVENT_INIT("00000000000", "AUDIT_EVENT_INIT", "Audit initializaiton."),
	AUDIT_EVENT_TERMINATION("99999999999", "AUDIT_EVENT_INIT", "Audit initializaiton.");
	
	private String code;
	private String value;
	private String description;
	AuditEventType(String code, String value, String description) {
		this.code = code;
		this.value = value;
		this.description = description;
	}

}
