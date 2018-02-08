package com.myreward.engine.metamodel;

import java.util.Stack;

public class GatekeeperMetaModel extends BaseMetaModel {
	public Stack<String> instructionStack;
	public EventMetaModel eventMetaModel;

	public GatekeeperMetaModel() {
		instructionStack = new Stack<String>();
		eventMetaModel = new EventMetaModel();
	}
	public String toString() {
		return instructionStack.toString();
	}
	@Override
	public String[] build() {
		// TODO Auto-generated method stub
		return null;
	}


}
