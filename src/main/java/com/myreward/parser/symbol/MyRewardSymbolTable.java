package com.myreward.parser.symbol;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MyRewardSymbolTable extends SymbolTable {
	private List<Symbol> symbolList;
	
	public MyRewardSymbolTable() {
		if(symbolList==null)
			symbolList = Collections.synchronizedList(new LinkedList<Symbol>());
	}

	@Override
	public void enterScope() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean insertSymbol(Symbol symbol) {
		// Obtaining Iterator
	    Iterator it = symbolList.iterator();
	 
	    // Iterating the list in forward direction
	    while(it.hasNext()){
	    	Symbol symbolValue = (Symbol)it.next();
	    	if(symbolValue.getFullyQualifiedId()==symbol.getFullyQualifiedId()) {
	    		symbol.version = symbolValue.version+1;
	    		return false;
	    	}
	    }
		return symbolList.add(symbol);
	}

	@Override
	public Symbol localLookup(Symbol symbol) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Symbol lookup(Symbol symbol) {
		int hashValue = symbol.getId();
	    Iterator<Symbol> it = symbolList.iterator();

		if(hashValue==0 && symbol.getName()!=null)
			hashValue = symbol.getName().hashCode();
		symbol.setId(hashValue);
		 
	    // Iterating the list in forward direction
	    while(it.hasNext()){
		   	Symbol symbolValue = (Symbol)it.next();
		   	if(symbolValue.getId()==symbol.getId())
		   		return symbolValue;
	    }
	    return null;
	}
	public Symbol lookup(int symbolInt) {
	    Iterator<Symbol> it = symbolList.iterator();
	    // Iterating the list in forward direction
	    while(it.hasNext()){
		    	Symbol symbolValue = (Symbol)it.next();
		    	if(symbolValue.getId()==symbolInt)
		    		return symbolValue;
	    }
	    return null;
	}

	@Override
	public void exitScope() {
		// TODO Auto-generated method stub

	}

	@Override
	public void allocate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void free() {
		symbolList = null;
	}

	@Override
	public Enum getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return symbolList.toString();
	}

	@Override
	public boolean merge(List<Symbol> symbolList) {
		if(symbolList!=null) {
			this.symbolList.addAll(symbolList);
			return true;
		}
		return false;
	}

	@Override
	public List<Symbol> getAllSymbol() {
		return symbolList;
	}

}
