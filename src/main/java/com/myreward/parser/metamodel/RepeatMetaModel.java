package com.myreward.parser.metamodel;

public class RepeatMetaModel extends BaseMetaModel {
	public enum RepeatCriteria {
		WEEKLY,
		MONTHLY,
		YEARLY,
		ACTIVITY_DATE
	}
	public int repeatAfterDay;
	public RepeatCriteria repeatCriteria;
	@Override
	public String[] build() {
		// TODO Auto-generated method stub
		return null;
	}

}
