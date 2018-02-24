package com.myreward.engine.model.event;

import java.util.Date;

public class EventDO {
	private String activityName;
	private Date activityDate;
	private Double activityValue1;
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

}
