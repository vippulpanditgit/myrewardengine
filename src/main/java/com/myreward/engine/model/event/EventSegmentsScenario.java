package com.myreward.engine.model.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class EventSegmentsScenario {
	private List<EventSegments> scenarioList;
	
	public EventSegmentsScenario() {
		scenarioList = new ArrayList<>();
	}
	public Optional<EventSegments> get(String scenarioName) {
		return scenarioList.stream()
					.filter(scenario -> StringUtils.equalsIgnoreCase(scenario.getScenarioName(), scenarioName))
					.findFirst();
	}
	public void put(String scenarioName, EventDO eventDO) {
		try {
			scenarioList.stream()
					.filter(scenario -> StringUtils.equalsIgnoreCase(scenario.getScenarioName(), scenarioName))
					.findFirst()
					.ifPresent(scenario -> {
						scenario.add(eventDO);
					});
		} catch(NullPointerException nullPointerException) {
			scenarioList.add(new EventSegments(scenarioName, eventDO));
		}
	}

}
