package com.myreward.parser.metamodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import com.myreward.parser.grammar.MyRewardParser;
import com.myreward.parser.symbol.Symbol;
import com.myreward.parser.symbol.SymbolTable;

public class GatekeeperMetaModel extends BaseMetaModel {
	
	public EventMetaModel eventMetaModel;

	// Gatekeeper source check
	private String[] gatekeeperSourceEventOpCodeListTemplate = {"lbl_gtk:%s:%s", "desc(\".gatekeeper(%s(%s))\")", "if_cmp_flg_set(%d,,+%s)", "set_gtk_flg(%d)", "return"};
	private String[] prefixGatekeeperOpCodesListTemplate = {"lbl_fn:%s:%s"};
	private String[] gatekeeperOpCodesListTemplate = {"inc_cmp_cnt(%s)", "set_cmp_flg(%s)"};
	private String[] suffixGatekeeperOpCodesListTemplate = {"return"};
	public static String overrideTemplate = "%d";

	// Gatekeeper check
//	private String[] gatekeeperEventOpCodeListTemplate = {"label:%s", "if_gtk_flag(%d)", "return"};

	public GatekeeperMetaModel() {
		eventMetaModel = new EventMetaModel();
	}
	@Override
	public String[] build() {
		List<String> gatekeeperOpCodes = new ArrayList<String>();
		Symbol eventSymbol = new Symbol(eventMetaModel.getEventName());
		
		SymbolTable symbolTable = MyRewardParser.symbolTable;
		eventSymbol = symbolTable.lookup(eventSymbol);

		gatekeeperOpCodes.add(String.format(prefixGatekeeperOpCodesListTemplate[0],eventSymbol.getFullyQualifiedId(),String.format(overrideTemplate, eventSymbol.symbolIndex)));
		gatekeeperOpCodes.add(String.format(gatekeeperOpCodesListTemplate[0],eventSymbol.getFullyQualifiedId(),String.format(overrideTemplate, eventSymbol.symbolIndex)));
		gatekeeperOpCodes.add(String.format(gatekeeperOpCodesListTemplate[1],eventSymbol.getFullyQualifiedId(),String.format(overrideTemplate, eventSymbol.symbolIndex)));


		gatekeeperOpCodes.add(String.format(suffixGatekeeperOpCodesListTemplate[0]));
		return gatekeeperOpCodes.toArray(new String[0]);
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
			gatekeeperOpcodes.add(String.format(gatekeeperSourceEventOpCodeListTemplate[0], String.valueOf(gatekeeperTargetSymbol.getFullyQualifiedId()), String.format(EventMetaModel.overrideTemplate, ++gatekeeperSourceSymbol.symbolIndex)));
			gatekeeperOpcodes.add(String.format(gatekeeperSourceEventOpCodeListTemplate[1], gatekeeperSourceSymbol.getName(), gatekeeperSourceSymbol.getFullyQualifiedId()));
			gatekeeperOpcodes.add(String.format(gatekeeperSourceEventOpCodeListTemplate[2], gatekeeperSourceSymbol.getFullyQualifiedId(),2));
			gatekeeperOpcodes.add(String.format(gatekeeperSourceEventOpCodeListTemplate[3], gatekeeperTargetSymbol.getFullyQualifiedId()));
			gatekeeperOpcodes.add(String.format(gatekeeperSourceEventOpCodeListTemplate[4], gatekeeperSourceSymbol.getFullyQualifiedId()));
			gatekeeperOpcodes.addAll(Arrays.asList(eventMetaModel.model()));
		}
		return gatekeeperOpcodes.toArray(new String[0]);
	}
	@Override
	public String[] call_stack() {
		// TODO Auto-generated method stub
		return eventMetaModel.call_stack();
	}


}
