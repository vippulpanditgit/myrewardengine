package com.myreward.engine.metamodel;

import java.util.List;
import java.util.Stack;

public class GroupMetaModel extends BaseMetaModel {
	public enum GROUP_LOGIC {
		ANY,
		ALL
	}
	private List<EventMetaModel> eventMetaModelList;
	public Stack<String> instructionStack;
	public int operationIndex;
	public GROUP_LOGIC logic;
	public int ordinal;
	public List<GroupMetaModel> childrenGroupMetaModel=null;
	
	public GroupMetaModel() {
		operationIndex=0;
		instructionStack = new Stack<String>();
		ordinal = 0;
	}
	
	public String toString() {
		return instructionStack+"<<"+operationIndex;
	}

}
