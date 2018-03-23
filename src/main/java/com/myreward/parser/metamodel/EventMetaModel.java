package com.myreward.parser.metamodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.myreward.parser.generator.MyRewardFunctionXRef;
import com.myreward.parser.grammar.MyRewardParser;
import com.myreward.parser.symbol.Symbol;
import com.myreward.parser.symbol.SymbolTable;

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
	public static String overrideTemplate = "%d";
	
	// Only for Standalone events
	private String[] prefixCallStackOpCodeListTemplate = {"if_evt_nm(%s,%d)"};
	private String[] bodyCallStackOpCodeListTemplate = {"call_fn(%s:%s)"};
	private String[] bodyCallStackOpCodeListGateKeeperTemplate = {"set_gtk_rel_call(%s:%s)", "call_fn(%s:%s)"};
	private String[] suffixCallStackOpCodeListTemplate = {"return"};
	
	private String[] prefixEventOpCodeListTemplate = {"lbl_fn:%s:%s"};
	private String[] eventOpCodesListTemplate = {"if_gtk_rel_call_not_set(%s,,+3)", "inc_cmp_cnt(%s)", "set_cmp_flg(%s)"};

	private String[] gatekeeperConstraintEventOpCodeListTemplate = {"call_gtk(%s:%s)", "if_gtk_flg_set(%d)"};
	private String[] rewardOutcomeEventOpCodeListTemplate = {"call_rwd(%s:%s)"}; 
	private String[] suffixEventOpCodeListTemplate = {"return"};
	private String[] preRepeatEventOpCodeListTemplate = {"if_rpt_flg_not_set(%s)", "call_rpt(%s:%s)", "if_rpt_flg_set(%s)", "if_evt_dt_lt(%s)", "set_rpt_dt(%s)"};
	private String[] resetGatekeeperFlag = {"reset_gtk_flg(%s)"};
	// Calling outcome 
	private String[] callShowOpCodeListTemplate = {"call_shw(%s:%s)"};
	private String[] callPriorityOpCodeListTemplate = {"call_pri(%s:%s)"};
	private String[] callRewardOpCodeListTemplate = {"if_cmp_cnt(%s,mod(%d),+%d)","call_rwd(%s:%s)"};
	private String[] callDurationOpCodeListTemplate = {"reset_dur_flg(%d)", "call_dur(%s:%s)"};
	private String[] postCallDurationOpCodeListTemplate = {"if_dur_flg_not_set(%s,+%d)", "return"};

	private String[] derivedEventOpCodeListTemplate = {"call(%s)"};
	private String[] eventOpCodeListTemplate = {"if_cmp_flg_set(%d)"};
	
	public EventMetaModel() {
		uuid = UUID.randomUUID();
	}
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
		if(groupMetaModel!=null) {
			if(groupMetaModel.eventMetaModelList!=null 
				&& groupMetaModel.eventMetaModelList.size()>0) { // This is a derived event. It is triggered by an action.
				if(this.durationMetaModel!=null) {
					groupOpcodeList.add(String.format(this.callDurationOpCodeListTemplate[0], eventSymbol.getFullyQualifiedId()));
					groupOpcodeList.add(String.format(this.callDurationOpCodeListTemplate[1], eventSymbol.getFullyQualifiedId()));
					groupOpcodeList.add(String.format(this.postCallDurationOpCodeListTemplate[0], eventSymbol.getFullyQualifiedId()));
					groupOpcodeList.add(String.format(this.postCallDurationOpCodeListTemplate[1], eventSymbol.getFullyQualifiedId()));
				}
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
					if(this.repeatMetaModel!=null) {
						for(int index=0;index<this.preRepeatEventOpCodeListTemplate.length;index++)
							groupOpcodeList.add(String.format(preRepeatEventOpCodeListTemplate[index],eventSymbol.getFullyQualifiedId(),String.format(EventMetaModel.overrideTemplate, eventSymbol.version)));					
					}
					if(parentEventMetaModel!=null) {
						Symbol parentEventSymbol = new Symbol(parentEventMetaModel.getEventName());
						parentEventSymbol = symbolTable.lookup(parentEventSymbol);
						parentEventSymbol.callDeclarationList.add(String.valueOf(eventSymbol.getFullyQualifiedId()));
					}
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
		if(this.gatekeeperMetaModel!=null) {
			groupOpcodeList.addAll(Arrays.asList(this.gatekeeperMetaModel.build()));
		}
		return groupOpcodeList.toArray(new String[0]);
	}
	@Override
	public String[] model() {
		Symbol eventSymbol = new Symbol(eventName);
		eventSymbol.setNamespace(this.parent.namespace);
		SymbolTable symbolTable = MyRewardParser.symbolTable;
		eventSymbol = symbolTable.lookup(eventSymbol);
//		++eventSymbol.version;
		
		if(this.durationMetaModel!=null) {
			eventOpCodeList.addAll(Arrays.asList(durationMetaModel.model()));
		}
		if(this.gatekeeperMetaModel!=null) {
			eventOpCodeList.addAll(Arrays.asList(gatekeeperMetaModel.model()));
		}
		if(this.rewardMetaModel!=null) {
			eventOpCodeList.addAll(Arrays.asList(rewardMetaModel.model()));
		}
		if(this.repeatMetaModel!=null) {
			eventOpCodeList.addAll(Arrays.asList(repeatMetaModel.model()));
		}
		if(this.showMetaModel!=null) {
			eventOpCodeList.addAll(Arrays.asList(showMetaModel.model()));
		}
		if(this.priorityMetaModel!=null) {
			eventOpCodeList.addAll(Arrays.asList(priorityMetaModel.model()));
		}
		if(groupMetaModel!=null 
				&& groupMetaModel.eventMetaModelList!=null 
				&& groupMetaModel.eventMetaModelList.size()>0) {
			eventOpCodeList.addAll(Arrays.asList(groupMetaModel.model()));
			return eventOpCodeList.toArray(new String[0]);
		} else {
//			if(MyRewardFunctionXRef.fnXRef.get(String.valueOf(eventSymbol.getFullyQualifiedId()))==null) {
			eventOpCodeList.add(String.format(prefixEventOpCodeListTemplate[0], 
							String.valueOf(eventSymbol.getFullyQualifiedId()),
							String.format(overrideTemplate, eventSymbol.version)));
			MyRewardFunctionXRef.fnXRef.put(String.valueOf(eventSymbol.getFullyQualifiedId())+":"+String.format(overrideTemplate, eventSymbol.version), String.format(prefixEventOpCodeListTemplate[0], eventSymbol.getFullyQualifiedId(),String.format(overrideTemplate, eventSymbol.version)));
//			} else {
//				eventOpCodeList.add(String.format(prefixEventOpCodeListTemplate[0], String.valueOf(eventSymbol.getFullyQualifiedId()),String.format(overrideTemplate, eventSymbol.symbolIndex)));
//				MyRewardFunctionXRef.fnXRef.put(String.valueOf(eventSymbol.getFullyQualifiedId())+":"+String.format(overrideTemplate, eventSymbol.symbolIndex), String.format(prefixEventOpCodeListTemplate[0], eventSymbol.getFullyQualifiedId(),String.format(overrideTemplate, eventSymbol.symbolIndex)));
//			}

			if(this.durationMetaModel!=null) {
				eventOpCodeList.add(String.format(this.callDurationOpCodeListTemplate[0], eventSymbol.getFullyQualifiedId()));
				eventOpCodeList.add(String.format(this.callDurationOpCodeListTemplate[1], eventSymbol.getFullyQualifiedId(), String.format(overrideTemplate, eventSymbol.version)));
				eventOpCodeList.add(String.format(this.postCallDurationOpCodeListTemplate[0], eventSymbol.getFullyQualifiedId(),2));
				eventOpCodeList.add(String.format(this.postCallDurationOpCodeListTemplate[1], eventSymbol.getFullyQualifiedId()));

				eventOpCodeList.add(String.format(this.eventOpCodesListTemplate[0], eventSymbol.getFullyQualifiedId(), eventSymbol.version));
				eventOpCodeList.add(String.format(this.eventOpCodesListTemplate[1], eventSymbol.getFullyQualifiedId(), eventSymbol.version));
				eventOpCodeList.add(String.format(this.eventOpCodesListTemplate[2], eventSymbol.getFullyQualifiedId()));
			}
			if(this.showMetaModel!=null) {
				eventOpCodeList.add(String.format(callShowOpCodeListTemplate[0], eventSymbol.getFullyQualifiedId(), String.format(overrideTemplate, eventSymbol.version)));
			}
			if(this.priorityMetaModel!=null) {
				eventOpCodeList.add(String.format(callPriorityOpCodeListTemplate[0], eventSymbol.getFullyQualifiedId(), String.format(overrideTemplate, eventSymbol.version)));
			}
			if(this.rewardMetaModel!=null) {
				if(this.durationMetaModel==null) { // The increment is already done on duration
					eventOpCodeList.add(String.format(this.eventOpCodesListTemplate[1], eventSymbol.getFullyQualifiedId(), eventSymbol.version));
					eventOpCodeList.add(String.format(this.eventOpCodesListTemplate[2], eventSymbol.getFullyQualifiedId()));
				}
				if(groupMetaModel!=null) {
					if(groupMetaModel.eventMetaModelList==null || groupMetaModel.eventMetaModelList.size()==0) {
							if(groupMetaModel.ordinalMetaModel!=null) {
								eventOpCodeList.add(String.format(callRewardOpCodeListTemplate[0], eventSymbol.getFullyQualifiedId(), groupMetaModel.ordinalMetaModel.ordinal,2));
								eventOpCodeList.add(String.format(callRewardOpCodeListTemplate[1], eventSymbol.getFullyQualifiedId(), String.format(overrideTemplate, eventSymbol.version)));
							}
					}
				} else {
					if(gatekeeperMetaModel==null) // Before calling reward make sure there is no gatekeeper on this event. Otherwise, reward is in gatekeeper.
						eventOpCodeList.add(String.format(callRewardOpCodeListTemplate[1], eventSymbol.getFullyQualifiedId(), String.format(overrideTemplate, eventSymbol.version)));
				}
			}
			if(this.gatekeeperMetaModel!=null) {
				if(this.durationMetaModel==null && this.rewardMetaModel==null) { // The completion increment already done on duration or reward block.
					eventOpCodeList.add(String.format(this.eventOpCodesListTemplate[1], eventSymbol.getFullyQualifiedId(), eventSymbol.version));
					eventOpCodeList.add(String.format(this.eventOpCodesListTemplate[2], eventSymbol.getFullyQualifiedId()));
				}
				eventOpCodeList.add(String.format(this.resetGatekeeperFlag[0], eventSymbol.getFullyQualifiedId()));
				eventOpCodeList.add(String.format(this.gatekeeperConstraintEventOpCodeListTemplate[0], eventSymbol.getFullyQualifiedId(), eventSymbol.version));
				eventOpCodeList.add(String.format(this.gatekeeperConstraintEventOpCodeListTemplate[1], eventSymbol.getFullyQualifiedId()));
				if(this.rewardMetaModel!=null) {
					eventOpCodeList.add(String.format(this.rewardOutcomeEventOpCodeListTemplate[0], eventSymbol.getFullyQualifiedId(),String.format(overrideTemplate, eventSymbol.version)));
				}
			} else {
				
			}
			if(this.gatekeeperMetaModel==null 
					&& this.rewardMetaModel==null
					&& this.durationMetaModel==null) {
				eventOpCodeList.add(String.format(this.eventOpCodesListTemplate[1], eventSymbol.getFullyQualifiedId(), eventSymbol.version));
				eventOpCodeList.add(String.format(this.eventOpCodesListTemplate[2], eventSymbol.getFullyQualifiedId()));
			}
			eventOpCodeList.add(String.format(suffixEventOpCodeListTemplate[0], eventSymbol.getFullyQualifiedId()));
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
			callStackOpCodeList.add(String.format(this.bodyCallStackOpCodeListTemplate[0], eventSymbol.getFullyQualifiedId(), String.valueOf(eventSymbol.version--)));			
			int level=0;
			level = this.climbUpTheEventStackTree(this, callStackOpCodeList, level);
			callStackOpCodeList.add(0, String.format(prefixCallStackOpCodeListTemplate[0], eventName, level+3));
			callStackOpCodeList.add(String.format(suffixCallStackOpCodeListTemplate[0], eventName));
			if(this.gatekeeperMetaModel!=null) {
				callStackOpCodeList.addAll(Arrays.asList(this.gatekeeperMetaModel.call_stack()));
			}
			return callStackOpCodeList.toArray(new String[0]);
		}
	}
	private int climbUpTheEventStackTree(BaseMetaModel eventMetaModel, List<String> callStackOpCodeList, int level) {
		if(callStackOpCodeList==null)
			callStackOpCodeList = new ArrayList<String>();
		if(eventMetaModel!=null && eventMetaModel.parent!=null
				&&  eventMetaModel.parent instanceof GroupMetaModel) {
			level++;
			EventMetaModel groupEventMetaModel = (EventMetaModel)eventMetaModel.parent.parent;
			Symbol eventSymbol = new Symbol(groupEventMetaModel.eventName);
			SymbolTable symbolTable = MyRewardParser.symbolTable;
			eventSymbol = symbolTable.lookup(eventSymbol);
			callStackOpCodeList.add(String.format(this.bodyCallStackOpCodeListTemplate[0], eventSymbol.getFullyQualifiedId(),String.format(EventMetaModel.overrideTemplate, eventSymbol.version)));
			eventMetaModel = eventMetaModel.parent.parent;
			return this.climbUpTheEventStackTree(eventMetaModel, callStackOpCodeList, level);
		}
		if(eventMetaModel!=null && eventMetaModel.parent!=null
				&&  eventMetaModel.parent instanceof GatekeeperMetaModel) {
			level++;
			EventMetaModel groupEventMetaModel = (EventMetaModel)eventMetaModel.parent.parent;
			Symbol eventSymbol = new Symbol(groupEventMetaModel.eventName);
			SymbolTable symbolTable = MyRewardParser.symbolTable;
			eventSymbol = symbolTable.lookup(eventSymbol);
			callStackOpCodeList.add(String.format(this.bodyCallStackOpCodeListGateKeeperTemplate[0], eventSymbol.getFullyQualifiedId(),String.format(EventMetaModel.overrideTemplate, ++eventSymbol.version)));
			callStackOpCodeList.add(String.format(this.bodyCallStackOpCodeListGateKeeperTemplate[1], eventSymbol.getFullyQualifiedId(),String.format(EventMetaModel.overrideTemplate, eventSymbol.version)));
			eventMetaModel = eventMetaModel.parent.parent;
			return this.climbUpTheEventStackTree(eventMetaModel, callStackOpCodeList, level);
			
		}
		return level;
	}

}
