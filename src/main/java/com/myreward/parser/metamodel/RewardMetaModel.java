package com.myreward.parser.metamodel;

import java.util.Date;

public class RewardMetaModel extends BaseMetaModel {
	public enum RewardType {
		DOLLAR,
		POINTS
	}
	private String[] setRewardOpCodeListTemplate = {"lbl_rwd:%d", "store_rwd_flg(%d)", "return"};
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
	@Override
	public String[] call_stack() {
		// TODO Auto-generated method stub
		return null;
	}

}
