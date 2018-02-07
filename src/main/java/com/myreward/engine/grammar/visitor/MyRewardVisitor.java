package com.myreward.engine.grammar.visitor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import com.myreward.engine.grammar.MyRewardBaseVisitor;
import com.myreward.engine.grammar.MyRewardParser.All_defContext;
import com.myreward.engine.grammar.MyRewardParser.Any_defContext;
import com.myreward.engine.grammar.MyRewardParser.Between_defContext;
import com.myreward.engine.grammar.MyRewardParser.EventGroupingContext;
import com.myreward.engine.grammar.MyRewardParser.Event_defContext;
import com.myreward.engine.grammar.MyRewardParser.Event_modifier_defContext;
import com.myreward.engine.grammar.MyRewardParser.Event_nameContext;
import com.myreward.engine.grammar.MyRewardParser.Gatekeeper_defContext;
import com.myreward.engine.grammar.MyRewardParser.Group_defContext;
import com.myreward.engine.grammar.MyRewardParser.Import_defContext;
import com.myreward.engine.grammar.MyRewardParser.Import_nameContext;
import com.myreward.engine.grammar.MyRewardParser.Myreward_defContext;
import com.myreward.engine.grammar.MyRewardParser.Myreward_defsContext;
import com.myreward.engine.grammar.MyRewardParser.Package_defContext;
import com.myreward.engine.grammar.MyRewardParser.Package_nameContext;
import com.myreward.engine.grammar.MyRewardParser.Priority_defContext;
import com.myreward.engine.grammar.MyRewardParser.Repeat_defContext;
import com.myreward.engine.grammar.MyRewardParser.Repeat_frequency_defContext;
import com.myreward.engine.grammar.MyRewardParser.Reward_defContext;
import com.myreward.engine.grammar.MyRewardParser.Reward_quantity_defContext;
import com.myreward.engine.grammar.MyRewardParser.Sequence_defContext;
import com.myreward.engine.grammar.MyRewardParser.Show_defContext;
import com.myreward.engine.grammar.Symbol;
import com.myreward.engine.grammar.Symbol.SymbolType;
import com.myreward.engine.grammar.SymbolTable;
import com.myreward.engine.metamodel.BaseMetaModel;
import com.myreward.engine.metamodel.GatekeeperMetaModel;
import com.myreward.engine.metamodel.GroupMetaModel;

public class MyRewardVisitor extends MyRewardBaseVisitor<BaseMetaModel> {
	private SymbolTable symbolTable;

	private boolean isInGroup = false;
	private Hashtable<String, Object> dataSegmentList = new Hashtable<String, Object>();
	private String groupOperation = null;
	private int groupEventId = 0;
	private int currentEventId;
	private int childIndex=0;
	public List<GroupMetaModel> groupMetaModelList = new ArrayList<GroupMetaModel>();
	public List<GatekeeperMetaModel> gatekeeperMetaModelList = new ArrayList<GatekeeperMetaModel>();
	public Symbol packageSymbol = null;

	public SymbolTable getSymbolTable() {
		return symbolTable;
	}

	public void setSymbolTable(SymbolTable symbolTable) {
		this.symbolTable = symbolTable;
	}
	private void serializeDataSegment(String filename) {
		FileOutputStream output;
		ObjectOutputStream os;
		try {
	        output = new FileOutputStream("outSer.ser");
	        os = new ObjectOutputStream(output);
	        os.writeObject(dataSegmentList);
	        os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private HashMap<String, Object> deserializeDataSegment(String filename) {
		FileInputStream input;
        ObjectInputStream ois;
		try {
			input = new FileInputStream(filename);
			ois = new ObjectInputStream(input);
			HashMap<String, Object> hm= new HashMap<String, Object>((Hashtable<String, Object>)ois.readObject());
			ois.close();
			return hm;
		} catch (IOException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			
		}
		return null;
	}
	@Override
	public BaseMetaModel visitRepeat_def(Repeat_defContext ctx) {
		// TODO Auto-generated method stub

		return visitChildren(ctx);
	}

	@Override
	public BaseMetaModel visitEvent_def(Event_defContext ctx) {
		BaseMetaModel response = visitChildren(ctx);

		return response;
	}

	@Override
	public BaseMetaModel visitEvent_name(Event_nameContext ctx) {
		String eventName = ctx.getText();
		Symbol eventNameSymbol = new Symbol(eventName);
		if(packageSymbol!=null)
			eventNameSymbol.setContainer(packageSymbol);
		eventNameSymbol = this.getSymbolTable().lookup(eventNameSymbol);
		currentEventId = eventNameSymbol.getFullyQualifiedId();
		if(ctx.getParent()!=null && ctx.getParent() instanceof Event_defContext) {
			if(ctx.getParent().getParent()!=null && ctx.getParent().getParent() instanceof EventGroupingContext) {
				if(ctx.getParent().getParent().getParent()!=null && ctx.getParent().getParent().getParent() instanceof Group_defContext) {
					Group_defContext groupDefContext = (Group_defContext)ctx.getParent().getParent().getParent();
					if(eventNameSymbol.getType()== SymbolType.DERIVED_EVENT)
						groupDefContext.groupDefMetaModel.instructionStack.add("call("+eventNameSymbol.getFullyQualifiedId()+")");
					else 
						groupDefContext.groupDefMetaModel.instructionStack.add("push("+eventNameSymbol.getFullyQualifiedId()+")");
					groupDefContext.groupDefMetaModel.operationIndex++;
					if(groupDefContext.groupDefMetaModel.operationIndex<2) {
						if(groupDefContext.groupDefMetaModel.logic==GroupMetaModel.GROUP_LOGIC.ANY) {
							groupDefContext.groupDefMetaModel.instructionStack.add("OP_OR");
							groupDefContext.groupDefMetaModel.instructionStack.add("push_ref("+groupEventId+")");
							groupDefContext.groupDefMetaModel.instructionStack.add("ifref_num("+groupEventId+","+groupDefContext.groupDefMetaModel.ordinal+")");
							groupDefContext.groupDefMetaModel.instructionStack.add("return");
							groupDefContext.groupDefMetaModel.operationIndex = 0;
						} else if(groupDefContext.groupDefMetaModel.logic==GroupMetaModel.GROUP_LOGIC.ALL) {
							groupDefContext.groupDefMetaModel.instructionStack.add("OP_AND");
							groupDefContext.groupDefMetaModel.instructionStack.add("push_ref("+groupEventId+")");
							groupDefContext.groupDefMetaModel.operationIndex = 0;
						}

					}
				}
			}
		}
//		System.out.println(eventNameSymbol.toString());
		return visitChildren(ctx);
	}

	@Override
	public BaseMetaModel visitGroup_def(Group_defContext ctx) {
		isInGroup = true;
		childIndex++;
		groupEventId = currentEventId;
//		ctx.groupDefMetaModel.instructionStack.push("call("+groupEventId+")");
		BaseMetaModel response = visitChildren(ctx);
		childIndex--;
		isInGroup = false;
		return response;
	}

	@Override
	public BaseMetaModel visitAll_def(All_defContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public BaseMetaModel visitAny_def(Any_defContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public BaseMetaModel visitSequence_def(Sequence_defContext ctx) {
		// TODO Auto-generated method stub
		
		return visitChildren(ctx);
	}

	@Override
	public BaseMetaModel visitPriority_def(Priority_defContext ctx) {
		// TODO Auto-generated method stub

		return visitChildren(ctx);
	}

	@Override
	public BaseMetaModel visitBetween_def(Between_defContext ctx) {
		// TODO Auto-generated method stub

		return visitChildren(ctx);
	}


	@Override
	public BaseMetaModel visitReward_def(Reward_defContext ctx) {
		// TODO Auto-generated method stub

		return visitChildren(ctx);
	}

	@Override
	public BaseMetaModel visitShow_def(Show_defContext ctx) {
		// TODO Auto-generated method stub

		return visitChildren(ctx);
	}

	@Override
	public BaseMetaModel visitEvent_modifier_def(Event_modifier_defContext ctx) {
		// TODO Auto-generated method stub

		return visitChildren(ctx);
	}

	@Override
	public BaseMetaModel visitRepeat_frequency_def(Repeat_frequency_defContext ctx) {
		// TODO Auto-generated method stub

		return visitChildren(ctx);
	}

	@Override
	public BaseMetaModel visitReward_quantity_def(Reward_quantity_defContext ctx) {
		// TODO Auto-generated method stub

		return visitChildren(ctx);
	}

	@Override
	public BaseMetaModel visitMyreward_def(Myreward_defContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}

	@Override
	public BaseMetaModel visitEventGrouping(EventGroupingContext ctx) {
		Group_defContext groupDefContext = null;
		if(ctx.getParent() instanceof Group_defContext)
			groupDefContext = (Group_defContext)ctx.getParent();
		dataSegmentList.put("local_stored_register_"+childIndex, new Integer(0x0));
		int groupEventId = currentEventId;
		groupDefContext.groupDefMetaModel.instructionStack.push("label:"+groupEventId);
		groupDefContext.groupDefMetaModel.instructionStack.push("push_ref("+groupEventId+")");
		BaseMetaModel response = visitChildren(ctx);
		groupDefContext.groupDefMetaModel.instructionStack.push("store_ref("+groupEventId+")");
		groupDefContext.groupDefMetaModel.instructionStack.push("pop_ref("+groupEventId+")");
		groupDefContext.groupDefMetaModel.instructionStack.push("return");
		groupMetaModelList.add(groupDefContext.groupDefMetaModel);
		return response;
	}

	@Override
	public BaseMetaModel visitMyreward_defs(Myreward_defsContext ctx) {
		BaseMetaModel response = visitChildren(ctx);
		
		return response;
	}

	@Override
	public BaseMetaModel visitImport_def(Import_defContext ctx) {
		BaseMetaModel response = visitChildren(ctx);
		
		return response;
	}

	@Override
	public BaseMetaModel visitImport_name(Import_nameContext ctx) {
		BaseMetaModel response = visitChildren(ctx);
		
		return response;
	}

	@Override
	public BaseMetaModel visitPackage_def(Package_defContext ctx) {
		BaseMetaModel response = visitChildren(ctx);
		
		return response;
	}

	@Override
	public BaseMetaModel visitPackage_name(Package_nameContext ctx) {
		packageSymbol = new Symbol();
		packageSymbol.setType(Symbol.SymbolType.PACKAGE);
		packageSymbol.setName(ctx.getText());
		BaseMetaModel response = visitChildren(ctx);
		
		return response;
	}


	@Override
	public BaseMetaModel visitGatekeeper_def(Gatekeeper_defContext ctx) {
/*		if(ctx.getChildCount()>0) {
			for(int index=0;index< ctx.getChildCount();index++) {
				if(ctx.getChild(index) instanceof Event_defContext) {
					if(ctx.getChild(index).getChildCount()>0) {
						for(int eventNameContextIndex=0;eventNameContextIndex< ctx.getChild(index).getChildCount();eventNameContextIndex++) {
							if(ctx.getChild(index).getChild(eventNameContextIndex) instanceof Event_nameContext) {
								Event_nameContext eventNameContext = (Event_nameContext)ctx.getChild(index).getChild(eventNameContextIndex);
								Symbol gatekeeperSymbol = new Symbol();
								gatekeeperSymbol.setName(eventNameContext.getText());
								gatekeeperSymbol = this.symbolTable.lookup(gatekeeperSymbol);
								gatekeeperSymbol.setType(Symbol.SymbolType.GATEKEEPER);
								GatekeeperMetaModel gatekeeperMetaModel = ctx.gateKeeperMetaModel;
								
								
								dataSegmentList.put("local_stored_register_"+childIndex, new Integer(0x0));
								int groupEventId = currentEventId;
								groupDefContext.groupDefMetaModel.instructionStack.push("label:"+groupEventId);
								groupDefContext.groupDefMetaModel.instructionStack.push("push_ref("+groupEventId+")");
								groupDefContext.groupDefMetaModel.instructionStack.push("store_ref("+groupEventId+")");
								groupDefContext.groupDefMetaModel.instructionStack.push("pop_ref("+groupEventId+")");
								groupDefContext.groupDefMetaModel.instructionStack.push("return");
								gatekeeperMetaModelList.add(groupDefContext.groupDefMetaModel);
								
							}
						}
					}
					
				}
			}
		}*/
		BaseMetaModel response = visitChildren(ctx);
		
		return response;
	}


}
