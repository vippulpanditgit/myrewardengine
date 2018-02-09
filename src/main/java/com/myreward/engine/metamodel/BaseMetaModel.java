package com.myreward.engine.metamodel;

import com.myreward.engine.symbol.SymbolTable;

public abstract class BaseMetaModel {
	public String[] opcodes;
	public SymbolTable symbolTable;
	public BaseMetaModel parent;
	public abstract String[] build();

}
