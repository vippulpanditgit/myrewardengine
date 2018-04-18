package com.myreward.parser.metamodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.myreward.engine.event.error.BuildException;
import com.myreward.engine.event.error.MetaModelException;
import com.myreward.engine.event.error.ReferencedModelException;
import com.myreward.parser.grammar.MyRewardParser;
import com.myreward.parser.model.CallStackFunctionModel;
import com.myreward.parser.model.EventFunctionModel;
import com.myreward.parser.model.EventInteractionFunctionModel;
import com.myreward.parser.model.CallStackFunctionModel.EventAttributeType;
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
		eventSymbol.setNamespace(namespace);
		
		eventSymbol = symbolTable.lookup(eventSymbol);

		gatekeeperOpCodes.add(String.format(prefixGatekeeperOpCodesListTemplate[0],eventSymbol.getFullyQualifiedId(),String.format(overrideTemplate, eventSymbol.version)));
		gatekeeperOpCodes.add(String.format(gatekeeperOpCodesListTemplate[0],eventSymbol.getFullyQualifiedId(),String.format(overrideTemplate, eventSymbol.version)));
		gatekeeperOpCodes.add(String.format(gatekeeperOpCodesListTemplate[1],eventSymbol.getFullyQualifiedId(),String.format(overrideTemplate, eventSymbol.version)));


		gatekeeperOpCodes.add(String.format(suffixGatekeeperOpCodesListTemplate[0]));
		return gatekeeperOpCodes.toArray(new String[0]);
	}
	@Override
	public String[] model() {
		List<String> gatekeeperOpcodes = new ArrayList<String>();
		String gatekeeperSourceEventName = eventMetaModel.getEventName();
		Symbol gatekeeperSourceSymbol = new Symbol(gatekeeperSourceEventName);
		this.namespace = parent.namespace;
		gatekeeperSourceSymbol.setNamespace(this.namespace);
		gatekeeperSourceSymbol = symbolTable.lookup(gatekeeperSourceSymbol);
		if(this.parent instanceof EventMetaModel) {
			EventMetaModel gatekeeperTargetEvent = (EventMetaModel)this.parent;
			Symbol gatekeeperTargetSymbol = new Symbol(gatekeeperTargetEvent.getEventName());
			gatekeeperTargetSymbol.setNamespace(gatekeeperTargetEvent.namespace);
			gatekeeperTargetSymbol = symbolTable.lookup(gatekeeperTargetSymbol);
			gatekeeperOpcodes.add(String.format(gatekeeperSourceEventOpCodeListTemplate[0], 
					String.valueOf(gatekeeperTargetSymbol.getFullyQualifiedId()), 
					String.format(EventMetaModel.overrideTemplate, /*++*/gatekeeperSourceSymbol.version))+"// "+gatekeeperTargetSymbol.getFullyQualifiedName());
			gatekeeperOpcodes.add(String.format(gatekeeperSourceEventOpCodeListTemplate[1], gatekeeperSourceSymbol.getName(), gatekeeperSourceSymbol.getFullyQualifiedId()));
			gatekeeperOpcodes.add(String.format(gatekeeperSourceEventOpCodeListTemplate[2], gatekeeperSourceSymbol.getFullyQualifiedId(),2));
			gatekeeperOpcodes.add(String.format(gatekeeperSourceEventOpCodeListTemplate[3], gatekeeperTargetSymbol.getFullyQualifiedId()));
			gatekeeperOpcodes.add(String.format(gatekeeperSourceEventOpCodeListTemplate[4], gatekeeperSourceSymbol.getFullyQualifiedId()));
			gatekeeperOpcodes.addAll(Arrays.asList(eventMetaModel.model()));
		}
		return gatekeeperOpcodes.toArray(new String[0]);
	}
	@Override
	public void call_stack(CallStackFunctionModel callStackFunctionModel) {
		eventMetaModel.call_stack(callStackFunctionModel);
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
	@Override
	public void model(EventFunctionModel eventFunctionModel) {
		List<String> gatekeeperOpcodes = new ArrayList<String>();
		String gatekeeperSourceEventName = eventMetaModel.getEventName();
		Symbol gatekeeperSourceSymbol = new Symbol(gatekeeperSourceEventName);
		this.namespace = parent.namespace;
		gatekeeperSourceSymbol.setNamespace(this.namespace);
		gatekeeperSourceSymbol = symbolTable.lookup(gatekeeperSourceSymbol);
		if(this.parent instanceof EventMetaModel) {
			EventMetaModel gatekeeperTargetEvent = (EventMetaModel)this.parent;
			Symbol gatekeeperTargetSymbol = new Symbol(gatekeeperTargetEvent.getEventName());
			gatekeeperTargetSymbol.setNamespace(gatekeeperTargetEvent.namespace);
			gatekeeperTargetSymbol = symbolTable.lookup(gatekeeperTargetSymbol);
			gatekeeperOpcodes.add(String.format(gatekeeperSourceEventOpCodeListTemplate[0], String.valueOf(gatekeeperTargetSymbol.getFullyQualifiedId()), String.format(EventMetaModel.overrideTemplate, /*++*/gatekeeperSourceSymbol.version)));
			gatekeeperOpcodes.add(String.format(gatekeeperSourceEventOpCodeListTemplate[1], gatekeeperSourceSymbol.getName(), gatekeeperSourceSymbol.getFullyQualifiedId()));
			gatekeeperOpcodes.add(String.format(gatekeeperSourceEventOpCodeListTemplate[2], gatekeeperSourceSymbol.getFullyQualifiedId(),2));
			gatekeeperOpcodes.add(String.format(gatekeeperSourceEventOpCodeListTemplate[3], gatekeeperTargetSymbol.getFullyQualifiedId()));
			gatekeeperOpcodes.add(String.format(gatekeeperSourceEventOpCodeListTemplate[4], gatekeeperSourceSymbol.getFullyQualifiedId()));
//			gatekeeperOpcodes.addAll(Arrays.asList(eventMetaModel.model()));
			eventFunctionModel.add(String.format(gatekeeperSourceEventOpCodeListTemplate[0], String.valueOf(gatekeeperTargetSymbol.getFullyQualifiedId()), String.format(EventMetaModel.overrideTemplate, /*++*/gatekeeperSourceSymbol.version)),
											this.namespace, 
											EventAttributeType.GATEKEEPER, 
											gatekeeperOpcodes.toArray(new String[0]),
											"// "+gatekeeperTargetSymbol.getFullyQualifiedName());
		}
	}
	@Override
	public void build(EventInteractionFunctionModel eventInteractionFunctionModel) throws BuildException {
		List<String> gatekeeperOpCodes = new ArrayList<String>();
		Symbol eventSymbol = new Symbol(eventMetaModel.getEventName());
		eventSymbol.setNamespace(namespace);
		
		eventSymbol = symbolTable.lookup(eventSymbol);
		gatekeeperOpCodes.add(String.format(prefixGatekeeperOpCodesListTemplate[0],eventSymbol.getFullyQualifiedId(),String.format(overrideTemplate, eventSymbol.version)));
		gatekeeperOpCodes.add(String.format(gatekeeperOpCodesListTemplate[0],eventSymbol.getFullyQualifiedId(),String.format(overrideTemplate, eventSymbol.version)));
		gatekeeperOpCodes.add(String.format(gatekeeperOpCodesListTemplate[1],eventSymbol.getFullyQualifiedId(),String.format(overrideTemplate, eventSymbol.version)));
		gatekeeperOpCodes.add(String.format(suffixGatekeeperOpCodesListTemplate[0]));
		eventInteractionFunctionModel.add(String.format(prefixGatekeeperOpCodesListTemplate[0],eventSymbol.getFullyQualifiedId(),String.format(overrideTemplate, eventSymbol.version)),
				this.namespace, 
				EventAttributeType.GATEKEEPER, 
				gatekeeperOpCodes.toArray(new String[0]));
		
	}


}
