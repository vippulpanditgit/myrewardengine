package com.myreward.parser.metamodel;

import java.util.ArrayList;
import java.util.List;

import com.myreward.engine.event.error.MetaModelException;
import com.myreward.engine.event.error.ReferencedModelException;
import com.myreward.parser.grammar.MyRewardParser;
import com.myreward.parser.model.CallStackFunctionModel;
import com.myreward.parser.symbol.Symbol;
import com.myreward.parser.symbol.SymbolTable;

public class RewardMetaModel extends BaseMetaModel {
	public enum RewardType {
		DOLLAR,
		POINTS
	}
	private String[] rewardOpCodeListTemplate = {"lbl_rwd:%s:%s", "desc(\".reward(%s(%s),%2.2f,%2.2f)\")", "if_dur_flg_not_set(%d)", "return", "if_cmp_flg_not_set(%d)", "return", "set_rwd_flg(%d)", "if_rwd_amt_le(%d,%f)", "add_rwd_amt(%d,%f)", "return", "return"};
	public RewardType rewardType;
	public double rewardAmount;
	public double maxRewardAmount = -1.00;
	@Override
	public String[] build() {
		return null;
	}
	@Override
	public String[] model() {
		List<String> rewardOpCodeList = new ArrayList<String>();
		if(this.parent instanceof EventMetaModel) {
			EventMetaModel parentEventMetaModel = (EventMetaModel)this.parent;
			Symbol eventSymbol = new Symbol(parentEventMetaModel.getEventName());
			eventSymbol.setNamespace(parentEventMetaModel.namespace);
			SymbolTable symbolTable = MyRewardParser.symbolTable;
			eventSymbol = symbolTable.lookup(eventSymbol);
			rewardOpCodeList.add(String.format(rewardOpCodeListTemplate[0], String.valueOf(eventSymbol.getFullyQualifiedId()),String.format(EventMetaModel.overrideTemplate, eventSymbol.version)));
			rewardOpCodeList.add(String.format(rewardOpCodeListTemplate[1], eventSymbol.getName(), String.valueOf(eventSymbol.getFullyQualifiedId()),rewardAmount, maxRewardAmount));
			if(parentEventMetaModel.getDuraitonMetaModel()!=null) {
				rewardOpCodeList.add(String.format(rewardOpCodeListTemplate[2], eventSymbol.getFullyQualifiedId()));
				rewardOpCodeList.add(String.format(rewardOpCodeListTemplate[3], eventSymbol.getFullyQualifiedId()));
			}
			rewardOpCodeList.add(String.format(rewardOpCodeListTemplate[4], eventSymbol.getFullyQualifiedId()));
			rewardOpCodeList.add(String.format(rewardOpCodeListTemplate[5], eventSymbol.getFullyQualifiedId()));
			rewardOpCodeList.add(String.format(rewardOpCodeListTemplate[6], eventSymbol.getFullyQualifiedId()));
			if(maxRewardAmount > 0)
				rewardOpCodeList.add(String.format(rewardOpCodeListTemplate[7], eventSymbol.getFullyQualifiedId(), maxRewardAmount));
			rewardOpCodeList.add(String.format(rewardOpCodeListTemplate[8], eventSymbol.getFullyQualifiedId(), rewardAmount));
			rewardOpCodeList.add(String.format(rewardOpCodeListTemplate[9], eventSymbol.getFullyQualifiedId()));
			if(maxRewardAmount > 0)
				rewardOpCodeList.add(String.format(rewardOpCodeListTemplate[10], eventSymbol.getFullyQualifiedId()));
		}
		return rewardOpCodeList.toArray(new String[0]);
	}
	@Override
	public void call_stack(CallStackFunctionModel callStackFunctionModel) {
	}
	@Override
	public void lib_lookup() throws ReferencedModelException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public BaseMetaModel find(Symbol symbol) throws MetaModelException {
		// TODO Auto-generated method stub
		return null;
	}

}
