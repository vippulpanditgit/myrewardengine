package com.myreward.parser.metamodel;

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
	@Override
	public String[] build() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String[] model() {
		// TODO Auto-generated method stub
		return null;
	}

}
