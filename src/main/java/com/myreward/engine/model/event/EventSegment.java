package com.myreward.engine.model.event;

import java.util.List;

import com.myreward.parser.generator.MyRewardDataSegment.EventDataObject;

public class EventSegment {
	private EventDO eventDO;
	private List<EventDataObject> dataSegment;
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

}
