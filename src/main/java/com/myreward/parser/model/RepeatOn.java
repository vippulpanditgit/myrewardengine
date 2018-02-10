package com.myreward.parser.model;

import java.io.Serializable;

public class RepeatOn implements Serializable {
	public enum EnumRepeatOn {
		DAILY,
		MONTHLY,
		WEEKLY,
		YEARLY,
		ACTIVITY_DATE
	}
	private int repeatOnAfter;
	private EnumRepeatOn repeatOn;
	public int getRepeatOnAfter() {
		return repeatOnAfter;
	}
	public void setRepeatOnAfter(int repeatOnAfter) {
		this.repeatOnAfter = repeatOnAfter;
	}
	public EnumRepeatOn getRepeatOn() {
		return repeatOn;
	}
	public void setRepeatOn(EnumRepeatOn repeatOn) {
		this.repeatOn = repeatOn;
	}
	public String toString() {
		return repeatOn+"<<"+repeatOnAfter;
	}
}
