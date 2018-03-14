package com.myreward.engine.audit;

import java.io.Serializable;
import java.util.Date;

public class Event implements Serializable {
	private Long uuid;
	
	private Date timestamp;
	
	private String meta_client;
	
	public Event() {
        uuid = java.util.UUID.randomUUID().getMostSignificantBits();
        this.timestamp = new Date();
	}

	public Long getUuid() {
		return uuid;
	}

	public void setUuid(Long uuid) {
		this.uuid = uuid;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getMeta_client() {
		return meta_client;
	}

	public void setMeta_client(String meta_client) {
		this.meta_client = meta_client;
	}
}
