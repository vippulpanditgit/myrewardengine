package com.myreward.engine.metamodel;

public class RepeatMetaModel extends BaseMetaModel {
	public enum RepeatCriteria {
		ACTIVITY_DATE
	}
	private int repeatAfterDay;
	private RepeatCriteria repeatCriteria;

}
