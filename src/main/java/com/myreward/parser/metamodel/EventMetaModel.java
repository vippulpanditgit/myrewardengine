package com.myreward.parser.metamodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import com.myreward.parser.grammar.MyRewardParser;
import com.myreward.parser.symbol.Symbol;
import com.myreward.parser.symbol.SymbolTable;
import com.myreward.parser.util.DateTimeConvertorUtil;

public class EventMetaModel extends BaseMetaModel {
	public enum EventType {
			EVENT,
			DERIVED_EVENT,
			GATEKEEPER
	}
	private RewardMetaModel rewardMetaModel;
	private ShowMetaModel showMetaModel;
	private RepeatMetaModel repeatMetaModel;
	private GroupMetaModel groupMetaModel;
	private String eventName;
	private EventType eventType;
	private DurationMetaModel durationMetaModel;
	private PriorityMetaModel priorityMetaModel;
	private GatekeeperMetaModel gatekeeperMetaModel;

	private List<String> eventOpCodeList = new ArrayList<String>();
	
	// Only for Standalone events
	private String[] prefixCallStackOpCodeListTemplate = {"ifevt_nm(%s)"};
	private String[] bodyCallStackOpCodeListTemplate = {"call(%s)"};
	private String[] suffixCallStackOpCodeListTemplate = {"return"};
	
	private String[] prefixEventOpCodeListTemplate = {"label:%s", "push_ref(%s)" };
	private String[] suffixEventOpCodeListTemplate = {"store(%s, %d)", "return"};

	// Duration 
	private String[] durationEffectiveDateEventOpCodeListTemplate = {"ifle(%d)", "return"};
	private String[] durationExpirationDateEventOpCodeListTemplate = {"ifge(%d)", "return"};
	
	private String[] derivedEventOpCodeListTemplate = {"call(%s)"};
	private String[] eventOpCodeListTemplate = {"push(%d)"};
	
	public RewardMetaModel getRewardMetaModel() {
		return rewardMetaModel;
	}
	public void setRewardMetaModel(RewardMetaModel rewardMetaModel) {
		this.rewardMetaModel = rewardMetaModel;
	}
	public ShowMetaModel getShowMetaModel() {
		return showMetaModel;
	}
	public void setShowMetaModel(ShowMetaModel showMetaModel) {
		this.showMetaModel = showMetaModel;
	}
	public RepeatMetaModel getRepeatMetaModel() {
		return repeatMetaModel;
	}
	public void setRepeatMetaModel(RepeatMetaModel repeatMetaModel) {
		this.repeatMetaModel = repeatMetaModel;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public EventType getEventType() {
		return eventType;
	}
	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}
	public GroupMetaModel getGroupMetaModel() {
		return groupMetaModel;
	}
	public void setGroupMetaModel(GroupMetaModel groupMetaModel) {
		this.groupMetaModel = groupMetaModel;
	}
	public DurationMetaModel getDuraitonMetaModel() {
		return durationMetaModel;
	}
	public void setDuraitonMetaModel(DurationMetaModel duraitonMetaModel) {
		this.durationMetaModel = duraitonMetaModel;
	}
	public PriorityMetaModel getPriorityMetaModel() {
		return priorityMetaModel;
	}
	public void setPriorityMetaModel(PriorityMetaModel priorityMetaModel) {
		this.priorityMetaModel = priorityMetaModel;
	}
	public GatekeeperMetaModel getGatekeeperMetaModel() {
		return gatekeeperMetaModel;
	}
	public void setGatekeeperMetaModel(GatekeeperMetaModel gatekeeperMetaModel) {
		this.gatekeeperMetaModel = gatekeeperMetaModel;
	}
	@Override
	public String[] build() {
		List<String> groupOpcodeList = new ArrayList<String>();
		Symbol eventSymbol = new Symbol(eventName);
		SymbolTable symbolTable = MyRewardParser.symbolTable;
		eventSymbol = symbolTable.lookup(eventSymbol);
		if(groupMetaModel!=null 
				&& groupMetaModel.eventMetaModelList!=null 
				&& groupMetaModel.eventMetaModelList.size()>0) { // This is a derived event. It is triggered by an action.
//			groupOpcodeList.add(String.format(derivedEventOpCodeListTemplate[0], eventSymbol.getFullyQualifiedId()));
			groupOpcodeList.addAll(Arrays.asList(groupMetaModel.build()));
			if(this.parent instanceof EventMetaModel) {
				EventMetaModel parentEventMetaModel = (EventMetaModel)this.parent;
				Symbol parentEventSymbol = new Symbol(parentEventMetaModel.getEventName());
				parentEventSymbol = symbolTable.lookup(parentEventSymbol);
				parentEventSymbol.callDeclarationList.add(String.valueOf(eventSymbol.getFullyQualifiedId()));
			}  else if(this.parent instanceof GroupMetaModel) {
				GroupMetaModel parentGroupEventMetaModel = (GroupMetaModel)this.parent;
				EventMetaModel parentEventMetaModel = null;
				if(parentGroupEventMetaModel.parent instanceof EventMetaModel) {
					parentEventMetaModel = (EventMetaModel)parentGroupEventMetaModel.parent;
				}
				if(parentEventMetaModel!=null) {
					Symbol parentEventSymbol = new Symbol(parentEventMetaModel.getEventName());
					parentEventSymbol = symbolTable.lookup(parentEventSymbol);
					parentEventSymbol.callDeclarationList.add(String.valueOf(eventSymbol.getFullyQualifiedId()));
				}
			} 
		} else { // This is a standalone event.
			if(this.parent instanceof EventMetaModel) {
				groupOpcodeList.add(String.format(eventOpCodeListTemplate[0], eventSymbol.getFullyQualifiedId()));
				EventMetaModel parentEventMetaModel = (EventMetaModel)this.parent;
				Symbol parentEventSymbol = new Symbol(parentEventMetaModel.getEventName());
				parentEventSymbol = symbolTable.lookup(parentEventSymbol);
				parentEventSymbol.callDeclarationList.add(String.valueOf(eventSymbol.getFullyQualifiedId()));
			}  else if(this.parent instanceof GroupMetaModel) {
				groupOpcodeList.add(String.format(eventOpCodeListTemplate[0], eventSymbol.getFullyQualifiedId()));
				GroupMetaModel parentGroupEventMetaModel = (GroupMetaModel)this.parent;
				EventMetaModel parentEventMetaModel = null;
				if(parentGroupEventMetaModel.parent instanceof EventMetaModel) {
					parentEventMetaModel = (EventMetaModel)parentGroupEventMetaModel.parent;
				}
				if(parentEventMetaModel!=null) {
					Symbol parentEventSymbol = new Symbol(parentEventMetaModel.getEventName());
					parentEventSymbol = symbolTable.lookup(parentEventSymbol);
					parentEventSymbol.callDeclarationList.add(String.valueOf(eventSymbol.getFullyQualifiedId()));
				}
			} 
		}
		return groupOpcodeList.toArray(new String[0]);
	}
	@Override
	public String[] model() {
		if(groupMetaModel!=null 
				&& groupMetaModel.eventMetaModelList!=null 
				&& groupMetaModel.eventMetaModelList.size()>0) {
			return groupMetaModel.model();
		} else {
			Symbol eventSymbol = new Symbol(eventName);
			SymbolTable symbolTable = MyRewardParser.symbolTable;
			eventSymbol = symbolTable.lookup(eventSymbol);
			eventOpCodeList.add(String.format(prefixEventOpCodeListTemplate[0], eventSymbol.getFullyQualifiedId()));
			eventOpCodeList.add(String.format(prefixEventOpCodeListTemplate[1], eventSymbol.getFullyQualifiedId()));
			if(this.durationMetaModel!=null) {
				if(this.durationMetaModel.effectiveDate!=null) {
					eventOpCodeList.add(String.format(this.durationEffectiveDateEventOpCodeListTemplate[0], DateTimeConvertorUtil.toLong(this.durationMetaModel.effectiveDate)));
					eventOpCodeList.add(String.format(this.durationEffectiveDateEventOpCodeListTemplate[1], DateTimeConvertorUtil.toLong(this.durationMetaModel.effectiveDate)));
				}
				if(this.durationMetaModel.expirationDate!=null) {
					eventOpCodeList.add(String.format(this.durationExpirationDateEventOpCodeListTemplate[0], DateTimeConvertorUtil.toLong(this.durationMetaModel.effectiveDate)));
					eventOpCodeList.add(String.format(this.durationExpirationDateEventOpCodeListTemplate[1], DateTimeConvertorUtil.toLong(this.durationMetaModel.effectiveDate)));
					
				}
				
			}
			
			eventOpCodeList.add(String.format(suffixEventOpCodeListTemplate[0], eventSymbol.getFullyQualifiedId(), 1));
			eventOpCodeList.add(String.format(suffixEventOpCodeListTemplate[1], eventSymbol.getFullyQualifiedId()));
	
			return eventOpCodeList.toArray(new String[0]);
		}
	}

	@Override
	public String[] call_stack() {
		if(groupMetaModel!=null 
				&& groupMetaModel.eventMetaModelList!=null 
				&& groupMetaModel.eventMetaModelList.size()>0) {
			return groupMetaModel.call_stack();
		} else {	
			Symbol eventSymbol = new Symbol(eventName);
			SymbolTable symbolTable = MyRewardParser.symbolTable;
			eventSymbol = symbolTable.lookup(eventSymbol);
			List<String> callStackOpCodeList = new ArrayList<String>();
			callStackOpCodeList.add(String.format(prefixCallStackOpCodeListTemplate[0], eventName));
			callStackOpCodeList.add(String.format(this.bodyCallStackOpCodeListTemplate[0], eventSymbol.getFullyQualifiedId()));
			this.climbUpTheEventStackTree(this, callStackOpCodeList);
			callStackOpCodeList.add(String.format(suffixCallStackOpCodeListTemplate[0], eventName));
			return callStackOpCodeList.toArray(new String[0]);
		}
	}
	private void climbUpTheEventStackTree(BaseMetaModel eventMetaModel, List<String> callStackOpCodeList) {
		if(callStackOpCodeList==null)
			callStackOpCodeList = new ArrayList<String>();
		if(eventMetaModel!=null && eventMetaModel.parent!=null
				&&  eventMetaModel.parent instanceof GroupMetaModel) {
			EventMetaModel groupEventMetaModel = (EventMetaModel)eventMetaModel.parent.parent;
			Symbol eventSymbol = new Symbol(groupEventMetaModel.eventName);
			SymbolTable symbolTable = MyRewardParser.symbolTable;
			eventSymbol = symbolTable.lookup(eventSymbol);
			callStackOpCodeList.add(String.format(this.bodyCallStackOpCodeListTemplate[0], eventSymbol.getFullyQualifiedId()));
			eventMetaModel = eventMetaModel.parent.parent;
			this.climbUpTheEventStackTree(eventMetaModel, callStackOpCodeList);
		}
		
		
		
	}

}
