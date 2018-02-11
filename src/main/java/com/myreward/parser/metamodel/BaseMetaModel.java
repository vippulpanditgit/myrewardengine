package com.myreward.parser.metamodel;

import com.myreward.parser.symbol.SymbolTable;

public abstract class BaseMetaModel {
	public String[] opcodes;
	public SymbolTable symbolTable;
	public BaseMetaModel parent;
	public abstract String[] build();
	public abstract String[] model();
	public abstract String[] call_stack();

}
