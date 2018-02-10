package com.myreward.parser.symbol;

import java.io.Serializable;
import java.util.List;

public abstract class SymbolTable  implements Serializable {
	// the table's identifier
	private String id;
	// table's name. This is the name of the class if the table is for a class, the name of the
	// function if the table is for a function, or 'top' if the table is global
	private String name;
	// The line number of the first line in the block this table represents.
	private int lineNumber;
	// True if the locals in this table can be optimized
	private boolean isOptimized;
	// True if the block is nested class or function
	private boolean isNested;
	// True if the block has nested namespaces within it
	private boolean hasChildren;
	
	public abstract void enterScope();
	public abstract boolean insertSymbol(Symbol symbol);
	public abstract Symbol localLookup(Symbol symbol);
	public abstract Symbol lookup(Symbol symbol);
	public abstract Symbol lookup(int id);
	public abstract void exitScope();
	public abstract String toString();
	public abstract boolean merge(List<Symbol> symbolList);
	public abstract List<Symbol> getAllSymbol();
	
	// Allocate a new empty symbol table
	public abstract void allocate(); 
	// Free all enteries storage of symbol table
	public abstract void free();
	
	// Return the type of the symbol table. Possible values are "class", "module" and "function"
	public abstract Enum getType();
	// Return the table's identifier
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	public boolean isOptimized() {
		return isOptimized;
	}
	public void setOptimized(boolean isOptimized) {
		this.isOptimized = isOptimized;
	}
	public boolean isNested() {
		return isNested;
	}
	public void setNested(boolean isNested) {
		this.isNested = isNested;
	}
	public boolean isHasChildren() {
		return hasChildren;
	}
	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}
	public void setId(String id) {
		this.id = id;
	}
}
