package com.myreward.engine.model.event;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EventSegments {
	private List<EventSegment> eventSegmentList=null;
	
	public EventSegments() {
		if(eventSegmentList==null)
			eventSegmentList = new ArrayList<>();
	}
	public List<EventSegment> search(EventDO eventDO) {
		return eventSegmentList.stream()
					.filter(event -> event.getEventDO().equals(eventDO))
					.collect(Collectors.toList());
	}
	public List<EventSegment> getFirstElement(EventDO eventDO) {
		List<EventSegment> eventSegmentList=null;
		for(EventSegment eventSegment : this.eventSegmentList) {
			if(eventSegment.getEventDO().equals(eventDO)) {
				if(eventSegmentList==null)
					eventSegmentList = new ArrayList<>();
				eventSegmentList.add(eventSegment);
				return eventSegmentList;
			} else {
				if(eventSegmentList==null)
					eventSegmentList = new ArrayList<>();
				eventSegmentList.add(eventSegment);
				
			}
		}
		return eventSegmentList;
	}
}
