package com.myreward.parser.metamodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;
import java.util.UUID;

import com.myreward.parser.grammar.MyRewardParser;
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
	
//	private String[] prefixGroupOpCodesListTemplate = {"lbl_fn:%s"};
//	private String[] suffixGroupOpCodesListTemplate = {"store_ref(%s)", "pop_ref(%s)", "return"};
//	private String[] anyLogicGroupOpCodesListTemplate = {"OP_OR", "push_ref(%s)", "ifref_num(%s,%d)", "call_rwd(%s)", "return"};
	private String[] anyLogicGroupOpCodesListTemplate = {"if_cmp_flg_set(%s)","inc_cmp_cnt(%s)","rst_cmp_flg(%s)"};
	private String[] plainAnyLogicGroupOpCodesListTemplate = {"if_cmp_cnt(%s,mod(%d),+%d)","return"};
	private String[] rewardGroupOpCodesListTemplate = {"if_cmp_cnt(%s,mod(%d),+%d)","call_rwd(%s)","return"};
	private String[] allLogicGroupOpCodesListTemplate = {"OP_AND", "push_ref(%s)"};
	
	
	public GroupMetaModel() {
		operationIndex=0;
		instructionStack = new Stack<String>();
		uuid = UUID.randomUUID();

	}
	public String[] build() {
		List<String> groupOpCodes = new ArrayList<String>();
		if(eventMetaModelList!=null && eventMetaModelList.size()>0) {
			Symbol parentEventSymbol = null;
			RewardMetaModel rewardMetaModel = null;
			if(parent instanceof EventMetaModel) {
				EventMetaModel parentEventMetaModel = (EventMetaModel)parent;
				String eventName = parentEventMetaModel.getEventName();
				parentEventSymbol = new Symbol(eventName);
				
				SymbolTable symbolTable = MyRewardParser.symbolTable;
				parentEventSymbol = symbolTable.lookup(parentEventSymbol);
				rewardMetaModel = parentEventMetaModel.getRewardMetaModel();
			}
			Iterator<EventMetaModel> eventMetaModelListIterator = eventMetaModelList.listIterator();
			while(eventMetaModelListIterator.hasNext()) {
				EventMetaModel eventMetaModel = eventMetaModelListIterator.next();
				Symbol eventSymbol = MyRewardParser.symbolTable.lookup(new Symbol(eventMetaModel.getEventName()));
				if(ordinalMetaModel instanceof AnyMetaModel) {
					groupOpCodes.add(String.format(anyLogicGroupOpCodesListTemplate[0], eventSymbol.getFullyQualifiedId()));
					groupOpCodes.add(String.format(anyLogicGroupOpCodesListTemplate[1], parentEventSymbol.getFullyQualifiedId()));
					groupOpCodes.add(String.format(anyLogicGroupOpCodesListTemplate[2], eventSymbol.getFullyQualifiedId()));
					if(rewardMetaModel!=null) {
						groupOpCodes.add(String.format(rewardGroupOpCodesListTemplate[0], parentEventSymbol.getFullyQualifiedId(),ordinalMetaModel.ordinal,1));
						groupOpCodes.add(String.format(rewardGroupOpCodesListTemplate[1], parentEventSymbol.getFullyQualifiedId()));
						groupOpCodes.add(String.format(rewardGroupOpCodesListTemplate[2]));
					} else {
						groupOpCodes.add(String.format(plainAnyLogicGroupOpCodesListTemplate[0], parentEventSymbol.getFullyQualifiedId(),ordinalMetaModel.ordinal,1));
						groupOpCodes.add(String.format(plainAnyLogicGroupOpCodesListTemplate[1]));
					}
				} else if(ordinalMetaModel instanceof AllMetaModel) {
					
				}
			}
		}
/*		if(eventMetaModelList!=null && eventMetaModelList.size()>0) {
			Symbol eventSymbol = null;
			if(parent instanceof EventMetaModel) {
				EventMetaModel eventMetaModel = (EventMetaModel)parent;
				String eventName = eventMetaModel.getEventName();
				eventSymbol = new Symbol(eventName);
				
				SymbolTable symbolTable = MyRewardParser.symbolTable;
				eventSymbol = symbolTable.lookup(eventSymbol);
			}
//			for(int index=0;index<prefixGroupOpCodesListTemplate.length;index++)
//				groupOpcodes.add(String.format(prefixGroupOpCodesListTemplate[index],eventSymbol.getFullyQualifiedId()));
			Iterator<EventMetaModel> eventMetaModelListIterator = eventMetaModelList.listIterator();
			while(eventMetaModelListIterator.hasNext()) {
				EventMetaModel eventMetaModel = eventMetaModelListIterator.next();
				groupOpcodes.addAll(Arrays.asList(eventMetaModel.build()));
				if(ordinalMetaModel instanceof AnyMetaModel) {
					for(int index=0;index<anyLogicGroupOpCodesListTemplate.length;index++) {
						if(index==0)
							groupOpcodes.add(String.format(anyLogicGroupOpCodesListTemplate[index],eventSymbol.getFullyQualifiedId(),1));
						else if(index==1) {
							if(this.parent instanceof EventMetaModel) {
								EventMetaModel parentEventMetaModel = (EventMetaModel)this.parent;
								if(parentEventMetaModel.getRewardMetaModel()!=null) {
									groupOpcodes.add(String.format(anyLogicGroupOpCodesListTemplate[index],eventSymbol.getFullyQualifiedId(),((AnyMetaModel)ordinalMetaModel).ordinal,4));
								} else {
									groupOpcodes.add(String.format(anyLogicGroupOpCodesListTemplate[index],eventSymbol.getFullyQualifiedId(),((AnyMetaModel)ordinalMetaModel).ordinal,3));
									
								}
							}
						} else if(index==3) {
							if(this.parent instanceof EventMetaModel) {
								EventMetaModel parentEventMetaModel = (EventMetaModel)this.parent;
								if(parentEventMetaModel.getRewardMetaModel()!=null) {
									groupOpcodes.add(String.format(anyLogicGroupOpCodesListTemplate[index],eventSymbol.getFullyQualifiedId()));
								}
							}
						} else 
							groupOpcodes.add(String.format(anyLogicGroupOpCodesListTemplate[index],eventSymbol.getFullyQualifiedId()));
					}
				} else if(ordinalMetaModel instanceof AllMetaModel) {
					for(int index=0;index<allLogicGroupOpCodesListTemplate.length;index++)
						groupOpcodes.add(String.format(allLogicGroupOpCodesListTemplate[index],eventSymbol.getFullyQualifiedId()));
				}
				
			}
//			for(int index=0;index<suffixGroupOpCodesListTemplate.length;index++)
//				groupOpcodes.add(String.format(suffixGroupOpCodesListTemplate[index],eventSymbol.getFullyQualifiedId()));

			
		}
*/		return groupOpCodes.toArray(new String[0]);
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
	public String[] call_stack() {
		List<String> groupOpcodeList = new ArrayList<String>();
		if(eventMetaModelList!=null && eventMetaModelList.size()>0) { // events that are part of the group
			ListIterator<EventMetaModel> groupMetaModelListIterator = eventMetaModelList.listIterator();
			while(groupMetaModelListIterator.hasNext()) {
				groupOpcodeList.addAll(Arrays.asList(groupMetaModelListIterator.next().call_stack()));
			}
		} else { // Standalone event with"any"
			
		}
		return groupOpcodeList.toArray(new String[0]);
	}

}