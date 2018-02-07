package com.myreward.engine.metamodel;

import java.util.Date;

public class RewardMetaModel extends BaseMetaModel {
	public enum RewardType {
		DOLLAR,
		POINTS
	}
	public Date effectiveDate;
	public Date expirationDate;
	public RewardType rewardType;
	public double rewardAmount;

}
