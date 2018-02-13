package com.myreward.parser.metamodel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.myreward.parser.grammar.MyRewardParser;
import com.myreward.parser.symbol.Symbol;
import com.myreward.parser.symbol.SymbolTable;

public class RewardMetaModel extends BaseMetaModel {
	public enum RewardType {
		DOLLAR,
		POINTS
	}
	private String[] rewardOpCodeListTemplate = {"lbl_rwd:%d", "store_rwd_flg(%d)", "store_rwd_amt(%d, %f)", "return"};
	public RewardType rewardType;
	public double rewardAmount;
	@Override
	public String[] build() {
		List<String> rewardOpCodeList = new ArrayList<String>();
		if(this.parent instanceof EventMetaModel) {
			EventMetaModel parentEventMetaModel = (EventMetaModel)this.parent;
			Symbol eventSymbol = new Symbol(parentEventMetaModel.getEventName());
			SymbolTable symbolTable = MyRewardParser.symbolTable;
			eventSymbol = symbolTable.lookup(eventSymbol);
			rewardOpCodeList.add(String.format(rewardOpCodeListTemplate[0], eventSymbol.getFullyQualifiedId()));
			rewardOpCodeList.add(String.format(rewardOpCodeListTemplate[1], eventSymbol.getFullyQualifiedId()));
			rewardOpCodeList.add(String.format(rewardOpCodeListTemplate[2], eventSymbol.getFullyQualifiedId(), rewardAmount));
			rewardOpCodeList.add(String.format(rewardOpCodeListTemplate[3], eventSymbol.getFullyQualifiedId()));
		}
		return rewardOpCodeList.toArray(new String[0]);
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
