package com.myreward.engine.audit;

import java.util.Hashtable;

public class AuditEvent extends Event {
	private String actor;
	private String origin;
	private String tag;
	private Enum<AuditEventType> eventType;
	private String jsonObject;
	private Hashtable<String, String> additionalEventInfo;
	public AuditEvent(final String actor, final Enum<AuditEventType> eventType, final String jsonObject) {
		this.actor = actor;
		this.eventType = eventType;
		this.setJsonObject(jsonObject);
	}
	public AuditEvent(final String actor, final String origin, final Enum<AuditEventType> eventType, final String jsonObject) {
		this.actor = actor;
		this.origin = origin;
		this.eventType = eventType;
		this.setJsonObject(jsonObject);
	}
  	public String getActor() {
		return actor;
	}
	public void setActor(String actor) {
		this.actor = actor;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public Enum<AuditEventType> getEventType() {
		return eventType;
	}
	public void setEventType(Enum<AuditEventType> eventType) {
		this.eventType = eventType;
	}
	public Hashtable<String, String> getAdditionalEventInfo() {
		return additionalEventInfo;
	}
	public void setAdditionalEventInfo(Hashtable<String, String> additionalEventInfo) {
		this.additionalEventInfo = additionalEventInfo;
	}
	public String getJsonObject() {
		return jsonObject;
	}
	public void setJsonObject(String jsonObject) {
		this.jsonObject = jsonObject;
	}

}
