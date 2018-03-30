package com.myreward.parser.metamodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;
import java.util.UUID;

import com.myreward.parser.grammar.MyRewardParser;
import com.myreward.parser.model.CallStackFunctionModel;
import com.myreward.parser.model.Marker;
import com.myreward.parser.symbol.Symbol;
import com.myreward.parser.symbol.SymbolTable;

public class GroupMetaModel extends BaseMetaModel {
	public enum GROUP_LOGIC {
		ANY,
		ALL
	}
	public List<EventMetaModel> eventMetaModelList = new ArrayList<EventMetaModel>();
	public Stack<String> instructionStack;
	public int operationIndex;
	public GROUP_LOGIC logic;
	public OrdinalMetaModel ordinalMetaModel;
	public static String overrideTemplate = "%d";
	
//	private String[] prefixGroupOpCodesListTemplate = {"lbl_fn:%s"};
//	private String[] suffixGroupOpCodesListTemplate = {"set_ref(%s)", "pop_ref(%s)", "return"};
//	private String[] anyLogicGroupOpCodesListTemplate = {"OP_OR", "push_ref(%s)", "ifref_num(%s,%d)", "call_rwd(%s)", "return"};
	private String[] anyLogicGroupOpCodesListTemplate = {"if_cmp_flg_set(%s,,+%d)","inc_cmp_cnt(%s)","set_cmp_flg(%s)","reset_cmp_flg(%s)"};
	private String[] plainAnyLogicGroupOpCodesListTemplate = {"if_cmp_cnt(%s,mod(%d),+%d)","set_cmp_flg(%s)","return"};
	private String[] rewardGroupOpCodesListTemplate = {"if_cmp_cnt(%s,mod(%d),+%d)","set_cmp_flg(%s)","call_rwd(%s:%s)","return"};
	private String[] allLogicGroupOpCodesListTemplate = {"OP_AND", "push_ref(%s)"};
	private String[] preRepeatEventOpCodeListTemplate = {"if_rpt_flg_not_set(%s)", "call_rpt(%s:%s)", "if_rpt_flg_set(%s)", "if_evt_dt_lt(%s)", "set_rpt_dt(%s)"};
	
	private String[] prefixGroupOpCodesListTemplate = {"lbl_fn:%s:%s"};
	private String[] suffixGroupOpCodesListTemplate = {"return"};

	public GroupMetaModel() {
		operationIndex=0;
		instructionStack = new Stack<String>();
		uuid = UUID.randomUUID();

	}
	private String getSymbolNamespace(BaseMetaModel baseMetaModel) {
		if(baseMetaModel.parent.metaSymbol!=null) {
			if(baseMetaModel.parent instanceof PackageMetaModel)
				return baseMetaModel.parent.metaSymbol.getName();
			else 
				return baseMetaModel.parent.metaSymbol.getNamespace()+"."+baseMetaModel.parent.metaSymbol.getName();
		} else
			return this.getSymbolNamespace(baseMetaModel.parent);
	}

	public String[] build() {
		List<String> groupOpCodes = new ArrayList<String>();
		if(eventMetaModelList!=null && eventMetaModelList.size()>0) {
			Symbol parentEventSymbol = null;
			RewardMetaModel rewardMetaModel = null;
			if(parent instanceof EventMetaModel) {
				EventMetaModel parentEventMetaModel = (EventMetaModel)parent;
				String namespace = this.getSymbolNamespace(parentEventMetaModel);

				String eventName = parentEventMetaModel.getEventName();
				parentEventSymbol = new Symbol(eventName);
				parentEventSymbol.setNamespace(namespace);
				SymbolTable symbolTable = MyRewardParser.symbolTable;
				parentEventSymbol = symbolTable.lookup(parentEventSymbol);
				rewardMetaModel = parentEventMetaModel.getRewardMetaModel();
				// Create a lbl for function for the group
				groupOpCodes.add(String.format(prefixGroupOpCodesListTemplate[0],parentEventSymbol.getFullyQualifiedId(),String.format(overrideTemplate, parentEventSymbol.version)));
			}
			Iterator<EventMetaModel> eventMetaModelListIterator = eventMetaModelList.listIterator();
			while(eventMetaModelListIterator.hasNext()) {
				EventMetaModel eventMetaModel = eventMetaModelListIterator.next();
				Symbol lookupEvent = new Symbol(eventMetaModel.getEventName());
				String namespace = this.getSymbolNamespace(eventMetaModel);
				lookupEvent.setNamespace(namespace);
				Symbol eventSymbol = MyRewardParser.symbolTable.lookup(lookupEvent);
				if(ordinalMetaModel instanceof AnyMetaModel) {
					int anyGroupIndex = groupOpCodes.size();
					groupOpCodes.add(String.format(anyLogicGroupOpCodesListTemplate[0], eventSymbol.getFullyQualifiedId(),rewardMetaModel!=null?8:6));
					groupOpCodes.add(String.format(anyLogicGroupOpCodesListTemplate[1], parentEventSymbol.getFullyQualifiedId()));
					groupOpCodes.add(String.format(anyLogicGroupOpCodesListTemplate[2], parentEventSymbol.getFullyQualifiedId()));
					groupOpCodes.add(String.format(anyLogicGroupOpCodesListTemplate[3], eventSymbol.getFullyQualifiedId()));
					if(rewardMetaModel!=null) {
						groupOpCodes.add(String.format(rewardGroupOpCodesListTemplate[0], parentEventSymbol.getFullyQualifiedId(),ordinalMetaModel.ordinal,4));
						groupOpCodes.add(String.format(rewardGroupOpCodesListTemplate[1], parentEventSymbol.getFullyQualifiedId()));						
						groupOpCodes.add(String.format(rewardGroupOpCodesListTemplate[2], parentEventSymbol.getFullyQualifiedId(),String.format(overrideTemplate, parentEventSymbol.version)));
						groupOpCodes.add(String.format(rewardGroupOpCodesListTemplate[3]));
					} else {
						groupOpCodes.add(String.format(plainAnyLogicGroupOpCodesListTemplate[0], parentEventSymbol.getFullyQualifiedId(),ordinalMetaModel.ordinal,3));
						groupOpCodes.add(String.format(plainAnyLogicGroupOpCodesListTemplate[1], parentEventSymbol.getFullyQualifiedId()));						
						groupOpCodes.add(String.format(plainAnyLogicGroupOpCodesListTemplate[2]));
					}
					groupOpCodes.remove(anyGroupIndex);
					groupOpCodes.add(anyGroupIndex, String.format(anyLogicGroupOpCodesListTemplate[0], eventSymbol.getFullyQualifiedId(),groupOpCodes.size()+1-anyGroupIndex));
				} else if(ordinalMetaModel instanceof AllMetaModel) {
					
				}
			}
			groupOpCodes.add(String.format(suffixGroupOpCodesListTemplate[0]));
			eventMetaModelListIterator = eventMetaModelList.listIterator();
			while(eventMetaModelListIterator.hasNext()) {
				EventMetaModel eventMetaModel = eventMetaModelListIterator.next();
				groupOpCodes.addAll(Arrays.asList(eventMetaModel.build()));
			}
		}
		return groupOpCodes.toArray(new String[0]);
	}
	public String toString() {
		return instructionStack+"<<"+operationIndex;
	}
	@Override
	public String[] model() {
		List<String> groupOpcodeList = new ArrayList<String>();
		ListIterator<EventMetaModel> groupMetaModelListIterator = eventMetaModelList.listIterator();
		while(groupMetaModelListIterator.hasNext()) {
			groupOpcodeList.addAll(Arrays.asList(groupMetaModelListIterator.next().model()));
		}
		return groupOpcodeList.toArray(new String[0]);
	}
	@Override
	public void call_stack(CallStackFunctionModel callStackFunctionModel) {
		if(eventMetaModelList!=null && eventMetaModelList.size()>0) { // events that are part of the group
			ListIterator<EventMetaModel> groupMetaModelListIterator = eventMetaModelList.listIterator();
			while(groupMetaModelListIterator.hasNext()) {
				groupMetaModelListIterator.next().call_stack(callStackFunctionModel);
			}
		} else { // Standalone event with"any"
			
		}
	}

}
