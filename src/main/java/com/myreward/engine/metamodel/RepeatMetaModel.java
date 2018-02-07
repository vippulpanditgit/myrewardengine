package com.myreward.engine.metamodel;

public class RepeatMetaModel extends BaseMetaModel {
	public enum RepeatCriteria {
		WEEKLY,
		MONTHLY,
		YEARLY,
		ACTIVITY_DATE
	}
	public int repeatAfterDay;
	public RepeatCriteria repeatCriteria;

}
