package com.myreward.parser.metamodel;

import java.util.ArrayList;
import java.util.List;

import com.myreward.parser.grammar.MyRewardParser;
import com.myreward.parser.model.CallStackFunctionModel;
import com.myreward.parser.symbol.Symbol;
import com.myreward.parser.symbol.SymbolTable;

public class ShowMetaModel extends BaseMetaModel {
	public boolean isShow;

	private String[] showOpCodeListTemplate = {"lbl_shw:%s:%s", "desc(\".show(%s(%s),%s)\")", "set_shw_flg(%d)", "return"};

	@Override
	public String[] build() {
		return null;
	}

	@Override
	public String[] model() {
		List<String> showOpCodeList = new ArrayList<String>();
		if(this.parent instanceof EventMetaModel) {
			EventMetaModel parentEventMetaModel = (EventMetaModel)this.parent;
			Symbol eventSymbol = new Symbol(parentEventMetaModel.getEventName());
			eventSymbol.setNamespace(parentEventMetaModel.namespace);
			SymbolTable symbolTable = MyRewardParser.symbolTable;
			eventSymbol = symbolTable.lookup(eventSymbol);
			showOpCodeList.add(String.format(showOpCodeListTemplate[0], String.valueOf(eventSymbol.getFullyQualifiedId()),String.format(EventMetaModel.overrideTemplate, eventSymbol.version)));
			showOpCodeList.add(String.format(showOpCodeListTemplate[1], eventSymbol.getName(), eventSymbol.getFullyQualifiedId(), "true"));
			showOpCodeList.add(String.format(showOpCodeListTemplate[2], eventSymbol.getFullyQualifiedId()));
			showOpCodeList.add(String.format(showOpCodeListTemplate[3], eventSymbol.getFullyQualifiedId()));
		}
		return showOpCodeList.toArray(new String[0]);
	}

	@Override
	public void call_stack(CallStackFunctionModel callStackFunctionModel) {
		// TODO Auto-generated method stub
		return;
	}

}
