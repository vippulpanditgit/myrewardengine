package com.myreward.engine.metamodel;

import java.util.Date;

public class RewardMetaModel extends BaseMetaModel {
	public enum RewardType {
		DOLLAR,
		POINTS
	}
	private Date effectiveDate;
	private Date expirationDate;
	private RewardType rewardType;

}
