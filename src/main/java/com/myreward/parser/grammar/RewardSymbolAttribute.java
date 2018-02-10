package com.myreward.parser.grammar;

import java.io.Serializable;
import java.util.Date;

import com.myreward.parser.model.RepeatOn;
import com.myreward.parser.model.Reward;

public class RewardSymbolAttribute implements Serializable {
	private boolean isShow;
	private Date effectiveDate;
	private Date expirationDate;
	private RepeatOn repeatOn;
	private Reward reward;
	private int priority;
	public boolean isShow() {
		return isShow;
	}
	public void setShow(boolean isShow) {
		this.isShow = isShow;
	}
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public Date getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	public RepeatOn getRepeatOn() {
		return repeatOn;
	}
	public void setRepeatOn(RepeatOn repeatOn) {
		this.repeatOn = repeatOn;
	}
	public Reward getReward() {
		return reward;
	}
	public void setReward(Reward reward) {
		this.reward = reward;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public String toString() {
		return effectiveDate+"<<"+expirationDate+"<<"+isShow+"<<"+repeatOn+"<<"+reward+"<<"+priority;
	}
}
