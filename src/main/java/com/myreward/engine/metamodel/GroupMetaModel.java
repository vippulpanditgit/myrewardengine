package com.myreward.engine.metamodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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
	public List<GroupMetaModel> childrenGroupMetaModel=null;
	
	public GroupMetaModel() {
		operationIndex=0;
		instructionStack = new Stack<String>();
	}
	
	public String toString() {
		return instructionStack+"<<"+operationIndex;
	}

}
