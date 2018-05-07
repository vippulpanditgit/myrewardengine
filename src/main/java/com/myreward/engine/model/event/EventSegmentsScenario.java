package com.myreward.engine.model.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.myreward.engine.app.AppInstanceContext;
import com.myreward.engine.event.error.DebugException;
import com.myreward.engine.event.error.ErrorCode;
import com.myreward.engine.event.error.ScenarioException;

public class EventSegmentsScenario {
	private AppInstanceContext appInstanceContext;
	private List<EventSegments> scenarioList;
	
	public EventSegmentsScenario(AppInstanceContext appInstanceContext) {
		scenarioList = new ArrayList<>();
		this.appInstanceContext = appInstanceContext;
	}
	public Optional<EventSegments> get(String scenarioName) {
		return scenarioList.stream()
					.filter(scenario -> StringUtils.equalsIgnoreCase(scenario.getScenarioName(), scenarioName))
					.findFirst();
	}
	public List<String> getScenarioList() {
		return scenarioList.stream()
					.map(scenario -> scenario.getScenarioName())
					.collect(Collectors.toList());
	}
	public void put(String scenarioName, EventDO eventDO) {
		try {
			this.appInstanceContext.stackSegment.push(eventDO);
			Optional<EventSegments> scenarioOptional = scenarioList.stream()
					.filter(scenario -> StringUtils.equalsIgnoreCase(scenario.getScenarioName(), scenarioName))
					.findFirst();
			scenarioOptional.ifPresent(scenarioItem -> scenarioItem.add(eventDO));
			if(!scenarioOptional.isPresent()) {
				scenarioList.add(new EventSegments(scenarioName, eventDO));
			}
		} catch(NullPointerException nullPointerException) {
			scenarioList.add(new EventSegments(scenarioName, eventDO));
		}
	}
	public void put(EventDO eventDO) {
			put("", eventDO);
	}
	public boolean drop(String scenarioName) {
		if(scenarioName==null)
			scenarioName = "";
		Optional<EventSegments> eventSegmentsOpt = this.get(scenarioName);
		if(eventSegmentsOpt.isPresent())
			return eventSegmentsOpt.get().drop();
		return false;
	}
	public void put(String scenarioName, List<EventSegment> initialize) {
		EventSegments eventSegments = new EventSegments();
		eventSegments.setScenarioName(scenarioName);
		eventSegments.setEventSegmentList(initialize);
		scenarioList.add(eventSegments);
	}
	public boolean process(String scenarioName, List<EventDO> eventDOList) {
		if(scenarioName==null)
			scenarioName = "";
		Optional<EventSegments> eventSegmentsOpt = this.get(scenarioName);
		if(eventSegmentsOpt.isPresent()) {
			eventDOList.stream().forEach(eventDO -> {
				try {
					process(eventDO);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			});
			return true;
		}
		return false;
		
	}

	public void process(String scenario) throws Exception {
		Optional<EventSegments> eventSegmentsScenario = get(scenario);
		EventSegments eventSegments = eventSegmentsScenario.get();
		eventSegments.getEventSegmentList().stream().forEach(eventSegment -> {
			try {
				process(eventSegment);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
	public boolean process(EventDO eventDO) throws Exception {
		appInstanceContext.print_data_segment();
		try {
			if(appInstanceContext.isInstanceReady())
				appInstanceContext.eventProcessor.process_event(eventDO);
		} catch(DebugException debugException) {
			appInstanceContext.eventProcessor.setMyRewardDataSegment(debugException.myRewardDataSegment);
			while(true) {
				try {
					int index = appInstanceContext.eventProcessor.step(debugException.opCodeIndex, debugException.eventDO);
					debugException.opCodeIndex = index;
					if(appInstanceContext.eventProcessor.getInstructionOpCodes().size()-1<=index)
						break;
				} catch(DebugException deepDebugException) {
					debugException.eventDO = deepDebugException.eventDO;
					debugException.myRewardDataSegment = deepDebugException.myRewardDataSegment;
					debugException.opCodeIndex = deepDebugException.opCodeIndex;
				}
			}
		}
		appInstanceContext.print_data_segment();
		return true;
		
	}
	public boolean process(EventSegment eventSegment) throws Exception {
		appInstanceContext.print_data_segment();
		try {
			if(appInstanceContext.isInstanceReady())
				appInstanceContext.eventProcessor.process_event(eventSegment.getEventDO());
		} catch(DebugException debugException) {
			appInstanceContext.eventProcessor.setMyRewardDataSegment(debugException.myRewardDataSegment);
			while(true) {
				try {
					int index = appInstanceContext.eventProcessor.step(debugException.opCodeIndex, debugException.eventDO);
					debugException.opCodeIndex = index;
					if(appInstanceContext.eventProcessor.getInstructionOpCodes().size()-1<=index)
						break;
				} catch(DebugException deepDebugException) {
					debugException.eventDO = deepDebugException.eventDO;
					debugException.myRewardDataSegment = deepDebugException.myRewardDataSegment;
					debugException.opCodeIndex = deepDebugException.opCodeIndex;
				}
			}
		}
		eventSegment.setDataSegment(appInstanceContext.dataSegment.clone_data_segment());
		appInstanceContext.print_data_segment();
		return true;
	}
	public void copyScenario(String from, String to, EventDO until, boolean overrideTo) throws ScenarioException {
		Optional<EventSegments> toEventSegment = this.get(to);
		if(!overrideTo && toEventSegment.isPresent())
			throw new ScenarioException(ErrorCode.OVERRIDE_FALSE_TO_SEGMENT_PRESENT, "Override is not allowed and 'to' Segment is present");
		Optional<EventSegments> fromEventSegmentsOptional = this.get(from);
		if(!fromEventSegmentsOptional.isPresent())
			throw new ScenarioException(ErrorCode.FROM_SEGMENT_NOT_PRESENT, "Nothing to override from.");
		List<EventSegment> fromEventSegmentList = fromEventSegmentsOptional.get().getTillFirstOccurence(until);
		this.put(to, fromEventSegmentList);
		
	}

	public AppInstanceContext getAppInstanceContext() {
		return appInstanceContext;
	}
	public void setAppInstanceContext(AppInstanceContext appInstanceContext) {
		this.appInstanceContext = appInstanceContext;
	}

}
