package com.myreward.engine.metamodel;

import java.util.Stack;

public class GatekeeperMetaModel extends BaseMetaModel {
	public Stack<String> instructionStack;

	public GatekeeperMetaModel() {
		instructionStack = new Stack<String>();
	}
	public String toString() {
		return instructionStack;
	}


}
