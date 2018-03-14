package com.myreward.engine.audit;

import java.util.Hashtable;

public class AuditEvent extends Event {
	private String actor;
	private String origin;
	private String tag;
	private Enum<AuditEventType> eventType;
	private Hashtable<String, String> additionalEventInfo;

}
