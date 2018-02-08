package com.myreward.engine.metamodel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;

import com.myreward.engine.symbol.Symbol;

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
//	public List<GroupMetaModel> childrenGroupMetaModel=null;
	
	private String[] prefix = {"label:%s", "push_ref(%s)" };
	private String[] suffix = {"store_ref(%s)", "pop_ref(%s)", "return"};
	
	public GroupMetaModel() {
		operationIndex=0;
		instructionStack = new Stack<String>();
	}
	public String[] build() {
		String[] groupOpcodes = null;
		if(eventMetaModelList!=null && eventMetaModelList.size()>0) {
			Iterator<EventMetaModel> eventMetaModelListIterator = eventMetaModelList.listIterator();
			while(eventMetaModelListIterator.hasNext()) {
				EventMetaModel eventMetaModel = eventMetaModelListIterator.next();
				String[] opcodeList = eventMetaModel.build();
				
			}

			
		}
		return groupOpcodes;
	}
	public String toString() {
		return instructionStack+"<<"+operationIndex;
	}

}
