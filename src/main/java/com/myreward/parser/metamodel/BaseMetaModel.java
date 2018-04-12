package com.myreward.parser.metamodel;

import java.util.UUID;

import com.myreward.engine.event.error.BuildException;
import com.myreward.engine.event.error.MetaModelException;
import com.myreward.engine.event.error.ReferencedModelException;
import com.myreward.parser.model.CallStackFunctionModel;
import com.myreward.parser.model.EventFunctionModel;
import com.myreward.parser.symbol.Symbol;
import com.myreward.parser.symbol.SymbolTable;

public abstract class BaseMetaModel {
	public String[] opcodes;
	public SymbolTable symbolTable;
	public BaseMetaModel parent;
	public UUID uuid;
	public String namespace;
	public Symbol metaSymbol;
	public String packageName;
	
	public abstract String[] build() throws BuildException;
	public abstract String[] model();
	public abstract void call_stack(CallStackFunctionModel callStackFunctionModel);
	public abstract void lib_lookup() throws ReferencedModelException;
	public abstract BaseMetaModel find(Symbol symbol) throws MetaModelException;
	public abstract void model(EventFunctionModel eventFunctionModel);
}
