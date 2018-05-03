package com.myreward.engine.model.event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.myreward.parser.generator.MyRewardDataSegment.EventDataObject;

public class EventSegment {
	private String version;
	private String receivedDateTime;
	private EventDO eventDO;
	private List<EventDataObject> dataSegment;
	public EventSegment() {
		if(dataSegment==null)
			dataSegment = new ArrayList<>();
	}
	public EventSegment(EventDO eventDO) {
		this.set(eventDO);
	}
	public EventSegment(String version, EventDO eventDO) {
		this.set(version, eventDO);
	}
	public EventSegment(String version, String receivedDateTime, EventDO eventDO) {
		this.set(version, receivedDateTime, eventDO);
	}
	public EventDO getEventDO() {
		return eventDO;
	}
	public void setEventDO(EventDO eventDO) {
		this.eventDO = eventDO;
	}
	public List<EventDataObject> getDataSegment() {
		return dataSegment;
	}
	public void setDataSegment(List<EventDataObject> dataSegment) {
		this.dataSegment = dataSegment;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public void set(EventDO eventDO) {
		if(dataSegment==null)
			dataSegment = new ArrayList<>();
		this.eventDO = eventDO;
		this.receivedDateTime = DateFormatUtils.formatUTC(new Date(), "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	}
	public void set(String version, EventDO eventDO) {
		if(dataSegment==null)
			dataSegment = new ArrayList<>();
		this.eventDO = eventDO;
		this.version = version;
		this.receivedDateTime = DateFormatUtils.formatUTC(new Date(), "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	}
	public void set(String version, String receivedDateTime, EventDO eventDO) {
		if(dataSegment==null)
			dataSegment = new ArrayList<>();
		this.eventDO = eventDO;
		this.version = version;
		this.receivedDateTime = receivedDateTime;
	}

}
