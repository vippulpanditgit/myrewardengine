package com.myreward.engine.model.event;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EventSegments {
	private String scenarioName;
	private List<EventSegment> eventSegmentList=null;
	
	public EventSegments() {
		if(eventSegmentList==null)
			eventSegmentList = new ArrayList<>();
	}
	public EventSegments(String scenarioName, EventDO eventDO) {
		if(eventSegmentList==null)
			eventSegmentList = new ArrayList<>();
		this.scenarioName = scenarioName;
		this.add(eventDO);
	}
	public List<EventSegment> search(EventDO eventDO) {
		return eventSegmentList.stream()
					.filter(event -> event.getEventDO().equals(eventDO))
					.collect(Collectors.toList());
	}
	public void add(EventDO eventDO) {
		eventSegmentList.add(new EventSegment(eventDO));
	}
	public List<EventSegment> getTillFirstOccurence(EventDO eventDO) {
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
	public String getScenarioName() {
		return scenarioName;
	}
	public void setScenarioName(String scenarioName) {
		this.scenarioName = scenarioName;
	}
	public List<EventSegment> getEventSegmentList() {
		return eventSegmentList;
	}
	public void setEventSegmentList(List<EventSegment> eventSegmentList) {
		this.eventSegmentList = eventSegmentList;
	}
}
