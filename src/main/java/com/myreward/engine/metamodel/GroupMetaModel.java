package com.myreward.engine.metamodel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;

import com.myreward.engine.grammar.MyRewardParser;
import com.myreward.engine.symbol.Symbol;
import com.myreward.engine.symbol.SymbolTable;

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
	
	private String[] prefixGroupOpCodesListTemplate = {"label:%s", "push_ref(%s)" };
	private String[] suffixGroupOpCodesListTemplate = {"store_ref(%s)", "pop_ref(%s)", "return"};
	private String[] anyLogicGroupOpCodesListTemplate = {"OP_OR", "push_ref(%s)", "ifref_num(%s,%s)", "return"};
	private String[] allLogicGroupOpCodesListTemplate = {"OP_AND", "push_ref(%s)"};
	
	
	public GroupMetaModel() {
		operationIndex=0;
		instructionStack = new Stack<String>();
	}
	public String[] build() {
		List<String> groupOpcodes = new ArrayList<String>();
		if(eventMetaModelList!=null && eventMetaModelList.size()>0) {
			Symbol eventSymbol = null;
			if(parent instanceof EventMetaModel) {
				EventMetaModel eventMetaModel = (EventMetaModel)parent;
				String eventName = eventMetaModel.getEventName();
				eventSymbol = new Symbol(eventName);
				
				SymbolTable symbolTable = MyRewardParser.symbolTable;
				eventSymbol = symbolTable.lookup(eventSymbol);
			}
			Iterator<EventMetaModel> eventMetaModelListIterator = eventMetaModelList.listIterator();
			while(eventMetaModelListIterator.hasNext()) {
				EventMetaModel eventMetaModel = eventMetaModelListIterator.next();
				String[] opcodeList = eventMetaModel.build();
				
			}

			
		}
		return groupOpcodes.toArray(new String[0]);
	}
	public String toString() {
		return instructionStack+"<<"+operationIndex;
	}

}
