package com.myreward.parser.metamodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.myreward.engine.event.error.ErrorCode;
import com.myreward.engine.event.error.MetaModelException;
import com.myreward.engine.event.error.ReferencedModelException;
import com.myreward.engine.event.error.BuildException;
import com.myreward.parser.generator.MyRewardFunctionXRef;
import com.myreward.parser.grammar.MyRewardParser;
import com.myreward.parser.model.CallStackFunctionModel;
import com.myreward.parser.model.EventFunctionModel;
import com.myreward.parser.model.EventInteractionFunctionModel;
import com.myreward.parser.model.CallStackFunctionModel.EventAttributeType;
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

	// Post event
	private String[] postEventCallForGatekeeperOpCodeListTemplate = {"lbl_fn_post:%s:%s",
													"if_gtk_flg_set(%s)",
													"call_rwd(%s:%s)",
													"return"};
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
	private String getSymbolNamespace(BaseMetaModel baseMetaModel) {
		if(baseMetaModel.parent.metaSymbol!=null) {
			if(baseMetaModel.parent instanceof PackageMetaModel)
				return baseMetaModel.parent.metaSymbol.getName();
			else if(baseMetaModel.parent instanceof GatekeeperMetaModel)
				return baseMetaModel.parent.metaSymbol.getName();
			else 
				return baseMetaModel.parent.metaSymbol.getNamespace()+"."+baseMetaModel.parent.metaSymbol.getName();
		} else if(baseMetaModel.parent instanceof GatekeeperMetaModel)
			return  baseMetaModel.parent.namespace;
		else {
			if(baseMetaModel.parent!=null)
				return this.getSymbolNamespace(baseMetaModel.parent);
			else return null;
		}
		
	}
	private String getPackageName(BaseMetaModel baseMetaModel) {
		if(baseMetaModel.parent.metaSymbol!=null && baseMetaModel.parent instanceof PackageMetaModel)
				return baseMetaModel.parent.metaSymbol.getName();
		else 
			return this.getPackageName(baseMetaModel.parent);
		
	}

	@Override
	public String[] build() throws BuildException {
		List<String> groupOpcodeList = new ArrayList<String>();
		String namespace = this.getSymbolNamespace(this);
		metaSymbol = new Symbol(eventName);
		metaSymbol.setNamespace(namespace);
		metaSymbol.setPackageName(this.getPackageName(this));
		metaSymbol = symbolTable.lookup(metaSymbol);
		if(metaSymbol==null)
			throw new BuildException(ErrorCode.SYMBOL_NOT_FOUND);
		if(groupMetaModel!=null) { // If this event is a group event
			if(groupMetaModel.eventMetaModelList!=null 
				&& groupMetaModel.eventMetaModelList.size()>0) { // This is a derived event. It is triggered by an action.
				if(this.durationMetaModel!=null) {
					groupOpcodeList.add(String.format(this.callDurationOpCodeListTemplate[0], metaSymbol.getFullyQualifiedId()));
					groupOpcodeList.add(String.format(this.callDurationOpCodeListTemplate[1], metaSymbol.getFullyQualifiedId()));
					groupOpcodeList.add(String.format(this.postCallDurationOpCodeListTemplate[0], metaSymbol.getFullyQualifiedId()));
					groupOpcodeList.add(String.format(this.postCallDurationOpCodeListTemplate[1], metaSymbol.getFullyQualifiedId()));
				}
				groupOpcodeList.addAll(Arrays.asList(groupMetaModel.build()));
				if(this.parent instanceof EventMetaModel) {
					EventMetaModel parentEventMetaModel = (EventMetaModel)this.parent;
					Symbol parentEventSymbol = new Symbol(parentEventMetaModel.getEventName());
					parentEventSymbol = symbolTable.lookup(parentEventSymbol);
					parentEventSymbol.callDeclarationList.add(String.valueOf(metaSymbol.getFullyQualifiedId()));
				}  else if(this.parent instanceof GroupMetaModel) {
					GroupMetaModel parentGroupEventMetaModel = (GroupMetaModel)this.parent;
					EventMetaModel parentEventMetaModel = null;
					if(parentGroupEventMetaModel.parent instanceof EventMetaModel) {
						parentEventMetaModel = (EventMetaModel)parentGroupEventMetaModel.parent;
					}
					if(this.repeatMetaModel!=null) {
						for(int index=0;index<this.preRepeatEventOpCodeListTemplate.length;index++)
							groupOpcodeList.add(String.format(preRepeatEventOpCodeListTemplate[index],metaSymbol.getFullyQualifiedId(),String.format(EventMetaModel.overrideTemplate, metaSymbol.version)));					
					}
					if(parentEventMetaModel!=null) {
						Symbol parentEventSymbol = new Symbol(parentEventMetaModel.getEventName());
						parentEventSymbol.setNamespace(parentEventMetaModel.namespace);
						parentEventSymbol = symbolTable.lookup(parentEventSymbol);
						parentEventSymbol.callDeclarationList.add(String.valueOf(metaSymbol.getFullyQualifiedId()));
					}
				}

			}
		} else { // This is a standalone event.
			Symbol symbolReference = symbolTable.getReference(symbolTable.getAllSymbol(), metaSymbol);
			if(symbolReference!=null) {
				symbolReference.setReferenced(true);
				metaSymbol.setReferredSymbol(symbolReference);
			} else if(this.parent instanceof EventMetaModel) {
				groupOpcodeList.add(String.format(eventOpCodeListTemplate[0], metaSymbol.getFullyQualifiedId())+"// "+metaSymbol.getFullyQualifiedName()+" is complete?");
				EventMetaModel parentEventMetaModel = (EventMetaModel)this.parent;
				Symbol parentEventSymbol = new Symbol(parentEventMetaModel.getEventName());
				parentEventSymbol = symbolTable.lookup(parentEventSymbol);
				parentEventSymbol.callDeclarationList.add(String.valueOf(metaSymbol.getFullyQualifiedId()));
			}  else if(this.parent instanceof GroupMetaModel) {
				GroupMetaModel parentGroupEventMetaModel = (GroupMetaModel)this.parent;
				EventMetaModel parentEventMetaModel = null;
				if(parentGroupEventMetaModel.parent instanceof EventMetaModel) {
					parentEventMetaModel = (EventMetaModel)parentGroupEventMetaModel.parent;
				}
				if(parentEventMetaModel!=null) {
					Symbol parentEventSymbol = new Symbol(parentEventMetaModel.getEventName());
					String parentEventNamespace = this.getSymbolNamespace(parentEventMetaModel);
					parentEventSymbol.setNamespace(parentEventNamespace);
					parentEventSymbol = symbolTable.lookup(parentEventSymbol);
					parentEventSymbol.callDeclarationList.add(String.valueOf(metaSymbol.getFullyQualifiedId()));
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
		Symbol metaSymbol = new Symbol(eventName);
 
		BaseMetaModel parentMetaModel = this.parent;
		while(parentMetaModel!=null) {
			if(parentMetaModel instanceof PackageMetaModel
					|| parentMetaModel instanceof EventMetaModel
					|| parentMetaModel instanceof GatekeeperMetaModel) {
				break;
			} else 
				parentMetaModel = parentMetaModel.parent;
		}
		if(parentMetaModel instanceof PackageMetaModel)
			metaSymbol.setNamespace(((PackageMetaModel) parentMetaModel).packageName);
		else if(parentMetaModel instanceof GatekeeperMetaModel)
			metaSymbol.setNamespace(((GatekeeperMetaModel) parentMetaModel).namespace);
		else if(parentMetaModel instanceof EventMetaModel)
			metaSymbol.setNamespace(parentMetaModel.namespace+"."+((EventMetaModel) parentMetaModel).eventName);
		metaSymbol = symbolTable.lookup(metaSymbol);
		this.namespace = metaSymbol.getNamespace();
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
			eventOpCodeList.add(String.format(prefixEventOpCodeListTemplate[0], 
							String.valueOf(metaSymbol.getFullyQualifiedId()),
							String.format(overrideTemplate, metaSymbol.version)));
			MyRewardFunctionXRef.fnXRef.put(String.valueOf(metaSymbol.getFullyQualifiedId())+":"+String.format(overrideTemplate, metaSymbol.version), String.format(prefixEventOpCodeListTemplate[0], metaSymbol.getFullyQualifiedId(),String.format(overrideTemplate, metaSymbol.version)));

			if(this.durationMetaModel!=null) {
				eventOpCodeList.add(String.format(this.callDurationOpCodeListTemplate[0], metaSymbol.getFullyQualifiedId()));
				eventOpCodeList.add(String.format(this.callDurationOpCodeListTemplate[1], metaSymbol.getFullyQualifiedId(), String.format(overrideTemplate, metaSymbol.version)));
				eventOpCodeList.add(String.format(this.postCallDurationOpCodeListTemplate[0], metaSymbol.getFullyQualifiedId(),2));
				eventOpCodeList.add(String.format(this.postCallDurationOpCodeListTemplate[1], metaSymbol.getFullyQualifiedId()));

				eventOpCodeList.add(String.format(this.eventOpCodesListTemplate[0], metaSymbol.getFullyQualifiedId(), metaSymbol.version));
				eventOpCodeList.add(String.format(this.eventOpCodesListTemplate[1], metaSymbol.getFullyQualifiedId(), metaSymbol.version));
				eventOpCodeList.add(String.format(this.eventOpCodesListTemplate[2], metaSymbol.getFullyQualifiedId()));
			}
			if(this.showMetaModel!=null) {
				eventOpCodeList.add(String.format(callShowOpCodeListTemplate[0], metaSymbol.getFullyQualifiedId(), String.format(overrideTemplate, metaSymbol.version)));
			}
			if(this.priorityMetaModel!=null) {
				eventOpCodeList.add(String.format(callPriorityOpCodeListTemplate[0], metaSymbol.getFullyQualifiedId(), String.format(overrideTemplate, metaSymbol.version)));
			}
			if(this.rewardMetaModel!=null) {
				if(this.durationMetaModel==null) { // The increment is already done on duration
					eventOpCodeList.add(String.format(this.eventOpCodesListTemplate[1], metaSymbol.getFullyQualifiedId(), metaSymbol.version));
					eventOpCodeList.add(String.format(this.eventOpCodesListTemplate[2], metaSymbol.getFullyQualifiedId()));
				}
				if(groupMetaModel!=null) {
					if(groupMetaModel.eventMetaModelList==null || groupMetaModel.eventMetaModelList.size()==0) {
							if(groupMetaModel.ordinalMetaModel!=null) {
								eventOpCodeList.add(String.format(callRewardOpCodeListTemplate[0], metaSymbol.getFullyQualifiedId(), groupMetaModel.ordinalMetaModel.ordinal,2));
								eventOpCodeList.add(String.format(callRewardOpCodeListTemplate[1], metaSymbol.getFullyQualifiedId(), String.format(overrideTemplate, metaSymbol.version)));
							}
					}
				} else {
					if(gatekeeperMetaModel==null) // Before calling reward make sure there is no gatekeeper on this event. Otherwise, reward is in gatekeeper.
						eventOpCodeList.add(String.format(callRewardOpCodeListTemplate[1], metaSymbol.getFullyQualifiedId(), String.format(overrideTemplate, metaSymbol.version)));
				}
			}
			if(this.gatekeeperMetaModel!=null) {
				if(this.durationMetaModel==null && this.rewardMetaModel==null) { // The completion increment already done on duration or reward block.
					eventOpCodeList.add(String.format(this.eventOpCodesListTemplate[1], metaSymbol.getFullyQualifiedId(), metaSymbol.version));
					eventOpCodeList.add(String.format(this.eventOpCodesListTemplate[2], metaSymbol.getFullyQualifiedId()));
				}
				eventOpCodeList.add(String.format(this.resetGatekeeperFlag[0], metaSymbol.getFullyQualifiedId()));
				eventOpCodeList.add(String.format(this.gatekeeperConstraintEventOpCodeListTemplate[0], metaSymbol.getFullyQualifiedId(), metaSymbol.version));
				eventOpCodeList.add(String.format(this.gatekeeperConstraintEventOpCodeListTemplate[1], metaSymbol.getFullyQualifiedId()));
				if(this.rewardMetaModel!=null) {
					eventOpCodeList.add(String.format(this.rewardOutcomeEventOpCodeListTemplate[0], metaSymbol.getFullyQualifiedId(),String.format(overrideTemplate, metaSymbol.version)));
				}
			} else {
				
			}
			if(this.gatekeeperMetaModel==null 
					&& this.rewardMetaModel==null
					&& this.durationMetaModel==null) {
				eventOpCodeList.add(String.format(this.eventOpCodesListTemplate[1], metaSymbol.getFullyQualifiedId(), metaSymbol.version));
				eventOpCodeList.add(String.format(this.eventOpCodesListTemplate[2], metaSymbol.getFullyQualifiedId()));
			}
			eventOpCodeList.add(String.format(suffixEventOpCodeListTemplate[0], metaSymbol.getFullyQualifiedId()));

			return eventOpCodeList.toArray(new String[0]);
		}
	}

	@Override
	public void call_stack(CallStackFunctionModel callStackFunctionModel) {
		if(groupMetaModel!=null 
				&& groupMetaModel.eventMetaModelList!=null 
				&& groupMetaModel.eventMetaModelList.size()>0) {
			groupMetaModel.call_stack(callStackFunctionModel);
		} else {	
			Symbol metaSymbol = new Symbol(eventName);
			String namespace = this.namespace!=null?this.namespace:this.getSymbolNamespace(this);
			metaSymbol.setNamespace(namespace);
			metaSymbol.setPackageName(this.packageName);
			metaSymbol = symbolTable.lookup(metaSymbol);
			if(metaSymbol.getReferredSymbol()!=null) {// If this symbol is a reference then don't generate a call stack
				
			} else {
				List<String> callStackOpCodeList = new ArrayList<String>();
				callStackOpCodeList.add(String.format(this.bodyCallStackOpCodeListTemplate[0], metaSymbol.getFullyQualifiedId(), String.valueOf(metaSymbol.version/*--*/))+" // "+metaSymbol.getFullyQualifiedName());			
				int level=0;
				level = this.climbUpTheEventStackTree(this, callStackOpCodeList, level);
				callStackOpCodeList.add(0, String.format(prefixCallStackOpCodeListTemplate[0], eventName, level+3));
				callStackOpCodeList.add(String.format(suffixCallStackOpCodeListTemplate[0], eventName));
				callStackFunctionModel.add(eventName, namespace, callStackOpCodeList.toArray(new String[0]));
				if(this.gatekeeperMetaModel!=null) {
					this.gatekeeperMetaModel.call_stack(callStackFunctionModel);
				}
			}
		}
	}
	private int climbUpTheEventStackTree(BaseMetaModel eventMetaModel, List<String> callStackOpCodeList, int level) {
		if(callStackOpCodeList==null)
			callStackOpCodeList = new ArrayList<String>();
		if(eventMetaModel!=null && eventMetaModel.parent!=null
				&&  eventMetaModel.parent instanceof GroupMetaModel) {
			level++;
			EventMetaModel groupEventMetaModel = (EventMetaModel)eventMetaModel.parent.parent;
			Symbol metaSymbol = new Symbol(groupEventMetaModel.eventName);
			String namespace = groupEventMetaModel.namespace!=null?groupEventMetaModel.namespace:this.getSymbolNamespace(groupEventMetaModel);
			metaSymbol.setNamespace(namespace);
			metaSymbol = symbolTable.lookup(metaSymbol);
			callStackOpCodeList.add(String.format(this.bodyCallStackOpCodeListTemplate[0], metaSymbol.getFullyQualifiedId(),String.format(EventMetaModel.overrideTemplate, metaSymbol.version))+" // "+metaSymbol.getFullyQualifiedName());
			eventMetaModel = eventMetaModel.parent.parent;
			return this.climbUpTheEventStackTree(eventMetaModel, callStackOpCodeList, level);
		}
		if(eventMetaModel!=null && eventMetaModel.parent!=null
				&&  eventMetaModel.parent instanceof GatekeeperMetaModel) {
			level++;
			EventMetaModel groupEventMetaModel = (EventMetaModel)eventMetaModel.parent.parent;
			Symbol metaSymbol = new Symbol(groupEventMetaModel.eventName);
			metaSymbol.setNamespace(this.getSymbolNamespace(eventMetaModel));
			metaSymbol = symbolTable.lookup(metaSymbol);
			callStackOpCodeList.add(String.format(this.bodyCallStackOpCodeListGateKeeperTemplate[0], metaSymbol.getFullyQualifiedId(),String.format(EventMetaModel.overrideTemplate, /*++*/metaSymbol.version))+" // "+metaSymbol.getFullyQualifiedName());
			callStackOpCodeList.add(String.format(this.bodyCallStackOpCodeListGateKeeperTemplate[1], metaSymbol.getFullyQualifiedId(),String.format(EventMetaModel.overrideTemplate, metaSymbol.version)));
			eventMetaModel = eventMetaModel.parent.parent;
			return this.climbUpTheEventStackTree(eventMetaModel, callStackOpCodeList, level);
			
		}
		return level;
	}
	public String toString() {
		return eventName+"<<"+this.namespace;
	}
	private BaseMetaModel getOrigin() {
		BaseMetaModel parentMetaModel = this.parent;
		while(parentMetaModel!=null && !(parentMetaModel instanceof MyRewardMetaModel)) {
			parentMetaModel = parentMetaModel.parent;
		}
		return parentMetaModel;
	}
	@Override
	public void lib_lookup() throws ReferencedModelException {
		if(groupMetaModel!=null 
				&& groupMetaModel.eventMetaModelList!=null 
				&& groupMetaModel.eventMetaModelList.size()>0) {
			groupMetaModel.lib_lookup();
		} else {	
			Symbol metaSymbol = new Symbol(eventName);
			String namespace = "";
			namespace = this.getNamespace(this, namespace);
			metaSymbol.setNamespace(namespace);
			metaSymbol = symbolTable.lookup(metaSymbol);
			Symbol symbolReference = symbolTable.getReference(symbolTable.getAllSymbol(), metaSymbol);
			if(symbolReference!=null) {
				BaseMetaModel myRewardModel = getOrigin();
				try {
					BaseMetaModel refMetaModel = myRewardModel.find(symbolReference);
					
					this.rewardMetaModel = ((EventMetaModel)refMetaModel).getRewardMetaModel();
					this.showMetaModel = ((EventMetaModel)refMetaModel).getShowMetaModel();
					this.repeatMetaModel = ((EventMetaModel)refMetaModel).getRepeatMetaModel();
					this.groupMetaModel = ((EventMetaModel)refMetaModel).getGroupMetaModel();
					this.eventType = ((EventMetaModel)refMetaModel).getEventType();
					this.durationMetaModel = ((EventMetaModel)refMetaModel).getDuraitonMetaModel();
					this.priorityMetaModel = ((EventMetaModel)refMetaModel).getPriorityMetaModel();
					this.gatekeeperMetaModel = ((EventMetaModel)refMetaModel).getGatekeeperMetaModel();
					metaSymbol = null;
					this.lib_lookup();
							
				} catch (MetaModelException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	private String getNamespace(BaseMetaModel baseMetaModel, String namespace) {
		BaseMetaModel parentMetaModel = baseMetaModel.parent;
		while(parentMetaModel!=null) {
			if(parentMetaModel instanceof PackageMetaModel
					|| parentMetaModel instanceof EventMetaModel
					|| parentMetaModel instanceof GatekeeperMetaModel) {
				break;
			} else 
				parentMetaModel = parentMetaModel.parent;
		}
		if(parentMetaModel instanceof PackageMetaModel) {
			if(namespace!=null && namespace.length()>0)
				namespace = ((PackageMetaModel) parentMetaModel).packageName+"."+namespace;
			else
				namespace = ((PackageMetaModel) parentMetaModel).packageName;
			return namespace;
		}else if(parentMetaModel instanceof GatekeeperMetaModel)
			namespace = ((GatekeeperMetaModel) parentMetaModel).namespace+"."+namespace;
		else if(parentMetaModel instanceof EventMetaModel) {
			if(namespace!=null && namespace.length()>0)
				namespace = ((EventMetaModel)parentMetaModel).eventName+"."+namespace;
			else
				namespace = ((EventMetaModel)parentMetaModel).eventName;
		}
		namespace = this.getNamespace(parentMetaModel, namespace);
		return namespace;
	}
	@Override
	public BaseMetaModel find(Symbol symbol) throws MetaModelException {
		if(groupMetaModel!=null 
				&& groupMetaModel.eventMetaModelList!=null 
				&& groupMetaModel.eventMetaModelList.size()>0) {
			String thisNamespace = this.getNamespace(this, "");
			if(symbol.getName().equalsIgnoreCase(this.eventName)
					&& symbol.getNamespace().equalsIgnoreCase(thisNamespace))
				return this;
			BaseMetaModel baseMetaModel = groupMetaModel.find(symbol);
			if(baseMetaModel!=null)
				return baseMetaModel;
		} else {
			String thisNamespace = this.getNamespace(this, "");
			if(symbol.getName().equalsIgnoreCase(this.eventName)
					&& symbol.getNamespace().equalsIgnoreCase(thisNamespace))
				return this;
		}
		return null;
	}
	private String getModelNamespace(BaseMetaModel baseMetaModel, String namespace) {
		BaseMetaModel parentMetaModel = baseMetaModel.parent;
		while(parentMetaModel!=null) {
			if(parentMetaModel instanceof PackageMetaModel
					|| parentMetaModel instanceof EventMetaModel
					|| parentMetaModel instanceof GatekeeperMetaModel) {
				break;
			} else 
				parentMetaModel = parentMetaModel.parent;
		}
		if(parentMetaModel instanceof PackageMetaModel) {
			namespace = ((PackageMetaModel) parentMetaModel).packageName+"."+namespace;
			return namespace;
		}else if(parentMetaModel instanceof GatekeeperMetaModel)
			namespace = ((GatekeeperMetaModel) parentMetaModel).namespace+"."+namespace;
		else if(parentMetaModel instanceof EventMetaModel) {
			if(namespace!=null && namespace.length()>0)
				namespace = ((EventMetaModel)parentMetaModel).eventName+"."+namespace;
			else
				namespace = ((EventMetaModel)parentMetaModel).eventName;
		}
		namespace = this.getNamespace(parentMetaModel, namespace);
		return namespace;
	}

	@Override
	public void model(EventFunctionModel eventFunctionModel) {
		Symbol metaSymbol = new Symbol(eventName);
		 
		BaseMetaModel parentMetaModel = this.parent;
		String namespace = this.getModelNamespace(this, "");
		while(parentMetaModel!=null) {
			if(parentMetaModel instanceof PackageMetaModel
					|| parentMetaModel instanceof EventMetaModel
					|| parentMetaModel instanceof GatekeeperMetaModel) {
				break;
			} else 
				parentMetaModel = parentMetaModel.parent;
		}
		if(parentMetaModel instanceof PackageMetaModel)
			metaSymbol.setNamespace(((PackageMetaModel) parentMetaModel).packageName);
		else if(parentMetaModel instanceof GatekeeperMetaModel)
			metaSymbol.setNamespace(((GatekeeperMetaModel) parentMetaModel).namespace);
		else if(parentMetaModel instanceof EventMetaModel)
			metaSymbol.setNamespace(parentMetaModel.namespace+"."+((EventMetaModel) parentMetaModel).eventName);
		metaSymbol = symbolTable.lookup(metaSymbol);
		this.namespace = metaSymbol.getNamespace();
		if(this.durationMetaModel!=null) {
			durationMetaModel.model(eventFunctionModel);
		}
		if(this.gatekeeperMetaModel!=null) {
			gatekeeperMetaModel.model(eventFunctionModel);
		}
		if(this.rewardMetaModel!=null) {
			rewardMetaModel.model(eventFunctionModel);
		}
		if(this.repeatMetaModel!=null) {
			repeatMetaModel.model(eventFunctionModel);
		}
		if(this.showMetaModel!=null) {
			showMetaModel.model(eventFunctionModel);
		}
		if(this.priorityMetaModel!=null) {
			priorityMetaModel.model(eventFunctionModel);
		}
		if(groupMetaModel!=null 
				&& groupMetaModel.eventMetaModelList!=null 
				&& groupMetaModel.eventMetaModelList.size()>0) {
			groupMetaModel.model(eventFunctionModel);
			return;
		} else {
			eventOpCodeList.add(String.format(prefixEventOpCodeListTemplate[0], 
							String.valueOf(metaSymbol.getFullyQualifiedId()),
							String.format(overrideTemplate, metaSymbol.version)));
			MyRewardFunctionXRef.fnXRef.put(String.valueOf(metaSymbol.getFullyQualifiedId())+":"+String.format(overrideTemplate, metaSymbol.version), String.format(prefixEventOpCodeListTemplate[0], metaSymbol.getFullyQualifiedId(),String.format(overrideTemplate, metaSymbol.version)));

			if(this.durationMetaModel!=null) {
				eventOpCodeList.add(String.format(this.callDurationOpCodeListTemplate[0], metaSymbol.getFullyQualifiedId()));
				eventOpCodeList.add(String.format(this.callDurationOpCodeListTemplate[1], metaSymbol.getFullyQualifiedId(), String.format(overrideTemplate, metaSymbol.version)));
				eventOpCodeList.add(String.format(this.postCallDurationOpCodeListTemplate[0], metaSymbol.getFullyQualifiedId(),2));
				eventOpCodeList.add(String.format(this.postCallDurationOpCodeListTemplate[1], metaSymbol.getFullyQualifiedId()));

				eventOpCodeList.add(String.format(this.eventOpCodesListTemplate[0], metaSymbol.getFullyQualifiedId(), metaSymbol.version));
				eventOpCodeList.add(String.format(this.eventOpCodesListTemplate[1], metaSymbol.getFullyQualifiedId(), metaSymbol.version));
				eventOpCodeList.add(String.format(this.eventOpCodesListTemplate[2], metaSymbol.getFullyQualifiedId()));
			}
			if(this.showMetaModel!=null) {
				eventOpCodeList.add(String.format(callShowOpCodeListTemplate[0], metaSymbol.getFullyQualifiedId(), String.format(overrideTemplate, metaSymbol.version)));
			}
			if(this.priorityMetaModel!=null) {
				eventOpCodeList.add(String.format(callPriorityOpCodeListTemplate[0], metaSymbol.getFullyQualifiedId(), String.format(overrideTemplate, metaSymbol.version)));
			}
			if(this.rewardMetaModel!=null) {
				if(this.durationMetaModel==null) { // The increment is already done on duration
					eventOpCodeList.add(String.format(this.eventOpCodesListTemplate[1], metaSymbol.getFullyQualifiedId(), metaSymbol.version));
					eventOpCodeList.add(String.format(this.eventOpCodesListTemplate[2], metaSymbol.getFullyQualifiedId()));
				}
				if(groupMetaModel!=null) {
					if(groupMetaModel.eventMetaModelList==null || groupMetaModel.eventMetaModelList.size()==0) {
							if(groupMetaModel.ordinalMetaModel!=null) {
								eventOpCodeList.add(String.format(callRewardOpCodeListTemplate[0], metaSymbol.getFullyQualifiedId(), groupMetaModel.ordinalMetaModel.ordinal,2));
								eventOpCodeList.add(String.format(callRewardOpCodeListTemplate[1], metaSymbol.getFullyQualifiedId(), String.format(overrideTemplate, metaSymbol.version)));
							}
					}
				} else {
					if(gatekeeperMetaModel==null) // Before calling reward make sure there is no gatekeeper on this event. Otherwise, reward is in gatekeeper.
						eventOpCodeList.add(String.format(callRewardOpCodeListTemplate[1], metaSymbol.getFullyQualifiedId(), String.format(overrideTemplate, metaSymbol.version)));
				}
			}
			if(this.gatekeeperMetaModel!=null) {
				if(this.durationMetaModel==null && this.rewardMetaModel==null) { // The completion increment already done on duration or reward block.
					eventOpCodeList.add(String.format(this.eventOpCodesListTemplate[1], metaSymbol.getFullyQualifiedId(), metaSymbol.version));
					eventOpCodeList.add(String.format(this.eventOpCodesListTemplate[2], metaSymbol.getFullyQualifiedId()));
				}
				eventOpCodeList.add(String.format(this.resetGatekeeperFlag[0], metaSymbol.getFullyQualifiedId()));
				eventOpCodeList.add(String.format(this.gatekeeperConstraintEventOpCodeListTemplate[0], metaSymbol.getFullyQualifiedId(), metaSymbol.version));
				eventOpCodeList.add(String.format(this.gatekeeperConstraintEventOpCodeListTemplate[1], metaSymbol.getFullyQualifiedId()));
				if(this.rewardMetaModel!=null) {
					eventOpCodeList.add(String.format(this.rewardOutcomeEventOpCodeListTemplate[0], metaSymbol.getFullyQualifiedId(),String.format(overrideTemplate, metaSymbol.version)));
					Symbol gatekeeperSourceSymbol = new Symbol(this.gatekeeperMetaModel.eventMetaModel.eventName);
					gatekeeperSourceSymbol.setNamespace(this.gatekeeperMetaModel.eventMetaModel.namespace);
					
					gatekeeperSourceSymbol = symbolTable.lookup(gatekeeperSourceSymbol);

					List<String> postEventOpCodeList = new ArrayList<>();
					postEventOpCodeList.add(String.format(postEventCallForGatekeeperOpCodeListTemplate[0], gatekeeperSourceSymbol.getFullyQualifiedId(), gatekeeperSourceSymbol.version));
					postEventOpCodeList.add(String.format(postEventCallForGatekeeperOpCodeListTemplate[1], metaSymbol.getFullyQualifiedId(), metaSymbol.version));
					postEventOpCodeList.add(String.format(postEventCallForGatekeeperOpCodeListTemplate[2], metaSymbol.getFullyQualifiedId(), metaSymbol.version));
					postEventOpCodeList.add(String.format(postEventCallForGatekeeperOpCodeListTemplate[3]));
					eventFunctionModel.add(String.format(postEventCallForGatekeeperOpCodeListTemplate[0], 
							gatekeeperSourceSymbol.getFullyQualifiedId(), gatekeeperSourceSymbol.version),
						this.namespace, 
						EventAttributeType.EVENT, 
						postEventOpCodeList.toArray(new String[0]),
						"// "+gatekeeperSourceSymbol.getFullyQualifiedName());
				}
			} else {
				
			}
			if(this.gatekeeperMetaModel==null 
					&& this.rewardMetaModel==null
					&& this.durationMetaModel==null) {
				eventOpCodeList.add(String.format(this.eventOpCodesListTemplate[1], metaSymbol.getFullyQualifiedId(), metaSymbol.version));
				eventOpCodeList.add(String.format(this.eventOpCodesListTemplate[2], metaSymbol.getFullyQualifiedId()));
			}
			eventOpCodeList.add(String.format(suffixEventOpCodeListTemplate[0], metaSymbol.getFullyQualifiedId()));
			eventFunctionModel.add(String.format(prefixEventOpCodeListTemplate[0], 
						String.valueOf(metaSymbol.getFullyQualifiedId()),
						String.format(overrideTemplate, metaSymbol.version)),
					this.namespace, 
					EventAttributeType.EVENT, 
					eventOpCodeList.toArray(new String[0]),
					"// "+metaSymbol.getFullyQualifiedName());
		}
		
	}
	@Override
	public void build(EventInteractionFunctionModel eventInteractionFunctionModel) throws BuildException {
		List<String> groupOpcodeList = new ArrayList<String>();
		String namespace = this.namespace==null?this.getSymbolNamespace(this):this.namespace;
		metaSymbol = new Symbol(eventName);
		metaSymbol.setNamespace(namespace);
		metaSymbol.setPackageName(packageName);
		metaSymbol = symbolTable.lookup(metaSymbol);
		if(metaSymbol==null)
			throw new BuildException(ErrorCode.SYMBOL_NOT_FOUND);
		if(groupMetaModel!=null) { // If this event is a group event
			if(groupMetaModel.eventMetaModelList!=null 
				&& groupMetaModel.eventMetaModelList.size()>0) { // This is a derived event. It is triggered by an action.
				if(this.durationMetaModel!=null) {
					groupOpcodeList.add(String.format(this.callDurationOpCodeListTemplate[0], metaSymbol.getFullyQualifiedId()));
					groupOpcodeList.add(String.format(this.callDurationOpCodeListTemplate[1], metaSymbol.getFullyQualifiedId()));
					groupOpcodeList.add(String.format(this.postCallDurationOpCodeListTemplate[0], metaSymbol.getFullyQualifiedId()));
					groupOpcodeList.add(String.format(this.postCallDurationOpCodeListTemplate[1], metaSymbol.getFullyQualifiedId()));
				}
				groupMetaModel.build(eventInteractionFunctionModel);
				if(this.parent instanceof EventMetaModel) {
					EventMetaModel parentEventMetaModel = (EventMetaModel)this.parent;
					Symbol parentEventSymbol = new Symbol(parentEventMetaModel.getEventName());
					parentEventSymbol = symbolTable.lookup(parentEventSymbol);
					parentEventSymbol.callDeclarationList.add(String.valueOf(metaSymbol.getFullyQualifiedId()));
				}  else if(this.parent instanceof GroupMetaModel) {
					GroupMetaModel parentGroupEventMetaModel = (GroupMetaModel)this.parent;
					EventMetaModel parentEventMetaModel = null;
					if(parentGroupEventMetaModel.parent instanceof EventMetaModel) {
						parentEventMetaModel = (EventMetaModel)parentGroupEventMetaModel.parent;
					}
					if(this.repeatMetaModel!=null) {
						for(int index=0;index<this.preRepeatEventOpCodeListTemplate.length;index++)
							groupOpcodeList.add(String.format(preRepeatEventOpCodeListTemplate[index],metaSymbol.getFullyQualifiedId(),String.format(EventMetaModel.overrideTemplate, metaSymbol.version)));					
					}
					if(parentEventMetaModel!=null) {
						Symbol parentEventSymbol = new Symbol(parentEventMetaModel.getEventName());
						parentEventSymbol.setNamespace(parentEventMetaModel.namespace);
						parentEventSymbol = symbolTable.lookup(parentEventSymbol);
						parentEventSymbol.callDeclarationList.add(String.valueOf(metaSymbol.getFullyQualifiedId()));
					}
				}

			}
		} else { // This is a standalone event.
			Symbol symbolReference = symbolTable.getReference(symbolTable.getAllSymbol(), metaSymbol);
			if(symbolReference!=null) {
				symbolReference.setReferenced(true);
				metaSymbol.setReferredSymbol(symbolReference);
			} else if(this.parent instanceof EventMetaModel) {
				groupOpcodeList.add(String.format(eventOpCodeListTemplate[0], metaSymbol.getFullyQualifiedId())+"// "+metaSymbol.getFullyQualifiedName()+" is complete?");
				EventMetaModel parentEventMetaModel = (EventMetaModel)this.parent;
				Symbol parentEventSymbol = new Symbol(parentEventMetaModel.getEventName());
				parentEventSymbol = symbolTable.lookup(parentEventSymbol);
				parentEventSymbol.callDeclarationList.add(String.valueOf(metaSymbol.getFullyQualifiedId()));
			}  else if(this.parent instanceof GroupMetaModel) {
				GroupMetaModel parentGroupEventMetaModel = (GroupMetaModel)this.parent;
				EventMetaModel parentEventMetaModel = null;
				if(parentGroupEventMetaModel.parent instanceof EventMetaModel) {
					parentEventMetaModel = (EventMetaModel)parentGroupEventMetaModel.parent;
				}
				if(parentEventMetaModel!=null) {
					Symbol parentEventSymbol = new Symbol(parentEventMetaModel.getEventName());
					String parentEventNamespace = parentEventMetaModel!=null?parentEventMetaModel.namespace:this.getSymbolNamespace(parentEventMetaModel);
					parentEventSymbol.setNamespace(parentEventNamespace);
					parentEventSymbol = symbolTable.lookup(parentEventSymbol);
					parentEventSymbol.callDeclarationList.add(String.valueOf(metaSymbol.getFullyQualifiedId()));
				}
			} 
		}
		if(this.gatekeeperMetaModel!=null) {
			this.gatekeeperMetaModel.build(eventInteractionFunctionModel);
		}
		eventInteractionFunctionModel.add(String.format(prefixEventOpCodeListTemplate[0], 
				String.valueOf(metaSymbol.getFullyQualifiedId()),
				String.format(overrideTemplate, metaSymbol.version)),
			this.namespace, 
			EventAttributeType.GROUP, 
			groupOpcodeList.toArray(new String[0]),
			"// "+metaSymbol.getName());

	}
}
