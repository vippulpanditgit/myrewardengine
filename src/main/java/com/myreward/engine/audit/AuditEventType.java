package com.myreward.engine.audit;

public enum AuditEventType {
	AUDIT_EVENT_INIT("0x00000000000", "AUDIT_EVENT_INIT", "Audit initializaiton."),
	AUDIT_EVENT_TERMINATION("0xFFFFFFFFFFF", "AUDIT_EVENT_INIT", "Audit Termination."), 
	AUDIT_EVENT_CREATE_META_DATA_TREE_START("0x00000100000", "AUDIT_EVENT_CREATE_META_DATA_TREE_START", "Creating Meta Data Tree"),
	AUDIT_EVENT_CREATE_META_DATA_TREE_END("0x000001FFFFF", "AUDIT_EVENT_CREATE_META_DATA_TREE_END", "Completed creating Meta Data Tree"), 
	EVENT_PROCESSING_EXCEPTION("0x10000000000", "EVENT_PROCESSING_EXCEPTION", "Exception during event processing"),
	META_DATA_CREATION_EXCEPTION("0x10000000100", "META_DATA_CREATION_EXCEPTION", "Exception during meta data creation"),
	META_DATA_PARSING_EXCEPTION("0x10000000110", "META_DATA_PARSING_EXCEPTION", "Exception during meta data parsing"), 
	SYMBOL_SEARCH_EXCEPTION("0x10000001000", "PARSE_SYMBOL", "Can't find symbol in table."),;
	
	private String code;
	private String value;
	private String description;
	AuditEventType(String code, String value, String description) {
		this.code = code;
		this.value = value;
		this.description = description;
	}

}
