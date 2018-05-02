package com.myreward.engine.model.event;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;

public class EventDO {
	private String activityName;
	private Date activityDate;
	private Map<String, String> attributeMap;
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public Date getActivityDate() {
		return activityDate;
	}
	public void setActivityDate(Date activityDate) {
		this.activityDate = activityDate;
	}
	
	public boolean isValid() {
		if(activityName!=null
				&& activityDate!=null)
			return true;
		return false;
	}
	public boolean equals(EventDO eventDO) {
		return this.getActivityName().equalsIgnoreCase(eventDO.getActivityName())
				&& DateUtils.isSameDay(this.getActivityDate(), eventDO.getActivityDate());
	}

}
