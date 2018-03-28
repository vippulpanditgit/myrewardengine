package com.myreward.parser.metamodel;

import java.util.ArrayList;
import java.util.List;

import com.myreward.parser.grammar.MyRewardParser;
import com.myreward.parser.model.CallStackFunctionModel;
import com.myreward.parser.symbol.Symbol;
import com.myreward.parser.symbol.SymbolTable;

public class RepeatMetaModel extends BaseMetaModel {
	public enum RepeatCriteria {
		WEEKLY(0),
		MONTHLY(1),
		YEARLY(2),
		ACTIVITY_DATE(3);

		int repeatType;
		RepeatCriteria(int p) {
			repeatType = p;
		}
		int repeatType() {
			return repeatType;
		} 
	}
	public int repeatAfter;
	public RepeatCriteria repeatCriteria;
	
	private String[] repeatOpCodeListTemplate = {"lbl_rpt:%s:%s", "desc(\".repeat(%s(%s),%s,%d)\")", "set_rpt_flg(%d)", "set_rpt_typ(%d,%d)", "set_rpt_aft(%d,%d)", "return"};

	@Override
	public String[] build() {
		return null;
	}
	@Override
	public String[] model() {
		List<String> repeatOpCodeList = new ArrayList<String>();
		if(this.parent instanceof EventMetaModel) {
			EventMetaModel parentEventMetaModel = (EventMetaModel)this.parent;
			Symbol eventSymbol = new Symbol(parentEventMetaModel.getEventName());
			eventSymbol.setNamespace(parentEventMetaModel.namespace);
			SymbolTable symbolTable = MyRewardParser.symbolTable;
			eventSymbol = symbolTable.lookup(eventSymbol);
			repeatOpCodeList.add(String.format(repeatOpCodeListTemplate[0], String.valueOf(eventSymbol.getFullyQualifiedId()),String.format(EventMetaModel.overrideTemplate, eventSymbol.version)));
			repeatOpCodeList.add(String.format(repeatOpCodeListTemplate[1], eventSymbol.getName(), eventSymbol.getFullyQualifiedId(), repeatCriteria.name(), repeatAfter));
			repeatOpCodeList.add(String.format(repeatOpCodeListTemplate[2], eventSymbol.getFullyQualifiedId()));
			repeatOpCodeList.add(String.format(repeatOpCodeListTemplate[3], eventSymbol.getFullyQualifiedId(), repeatCriteria.repeatType));
			repeatOpCodeList.add(String.format(repeatOpCodeListTemplate[4], eventSymbol.getFullyQualifiedId(), repeatAfter));
			repeatOpCodeList.add(String.format(repeatOpCodeListTemplate[5], eventSymbol.getFullyQualifiedId()));
		}
		return repeatOpCodeList.toArray(new String[0]);
	}
	@Override
	public void call_stack(CallStackFunctionModel callStackFunctionModel) {
		// TODO Auto-generated method stub
		return;
	}

}
