package com.myreward.parser.metamodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import com.myreward.parser.grammar.MyRewardParser;
import com.myreward.parser.symbol.Symbol;
import com.myreward.parser.symbol.SymbolTable;

public class GatekeeperMetaModel extends BaseMetaModel {
	
	public EventMetaModel eventMetaModel;

	// Gatekeeper source check
	private String[] gatekeeperSourceEventOpCodeListTemplate = {"lbl_gtk:%d", "if_cmp_flag(%d)", "store_gtk_flg(%d)", "return"};
	
	// Gatekeeper check
	private String[] gatekeeperEventOpCodeListTemplate = {"label:%s", "if_gtk_flag(%d)", "return"};

	public GatekeeperMetaModel() {
		eventMetaModel = new EventMetaModel();
	}
	@Override
	public String[] build() {
		return null;
	}
	@Override
	public String[] model() {
		List<String> gatekeeperOpcodes = new ArrayList<String>();
		String gatekeeperSourceEventName = eventMetaModel.getEventName();
		Symbol gatekeeperSourceSymbol = new Symbol(gatekeeperSourceEventName);
		
		SymbolTable symbolTable = MyRewardParser.symbolTable;
		gatekeeperSourceSymbol = symbolTable.lookup(gatekeeperSourceSymbol);
		if(this.parent instanceof EventMetaModel) {
			EventMetaModel gatekeeperTargetEvent = (EventMetaModel)this.parent;
			Symbol gatekeeperTargetSymbol = new Symbol(gatekeeperTargetEvent.getEventName());
			gatekeeperTargetSymbol = symbolTable.lookup(gatekeeperTargetSymbol);
			gatekeeperOpcodes.add(String.format(gatekeeperSourceEventOpCodeListTemplate[0], gatekeeperTargetSymbol.getFullyQualifiedId()));
			gatekeeperOpcodes.add(String.format(gatekeeperSourceEventOpCodeListTemplate[1], gatekeeperSourceSymbol.getFullyQualifiedId()));
			gatekeeperOpcodes.add(String.format(gatekeeperSourceEventOpCodeListTemplate[2], gatekeeperTargetSymbol.getFullyQualifiedId()));
			gatekeeperOpcodes.add(String.format(gatekeeperSourceEventOpCodeListTemplate[3], gatekeeperSourceSymbol.getFullyQualifiedId()));
		}
		return gatekeeperOpcodes.toArray(new String[0]);
	}
	@Override
	public String[] call_stack() {
		// TODO Auto-generated method stub
		return null;
	}


}
