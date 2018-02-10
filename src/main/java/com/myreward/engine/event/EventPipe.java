package com.myreward.engine.event;

import java.util.List;

import com.myreward.parser.model.event.EventDO;

public class EventPipe {
	private String uuid;
	private String type;
	private String name;
	private List<EventDO> eventList;

	public List<EventDO> getEventList() {
		return eventList;
	}

	public void setEventList(List<EventDO> eventList) {
		this.eventList = eventList;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
