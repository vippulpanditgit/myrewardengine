package com.myreward.parser.symbol;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.myreward.parser.symbol.Symbol.SymbolType;

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

	private Symbol lookup(List<Symbol> childrenList, Symbol sourceSymbol) {
		for(int index=0; index<childrenList.size();index++) {
			Symbol targetSymbol = childrenList.get(index);
			if(targetSymbol.getFullyQualifiedId()==sourceSymbol.getFullyQualifiedId())
				return targetSymbol;
			else if(targetSymbol.childrenList!=null) {
				Symbol targetChildSymbol =  lookup(targetSymbol.childrenList, sourceSymbol);
				if(targetChildSymbol!=null)
					return targetChildSymbol;
			}
		}
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
		   	if(symbolValue.childrenList!=null) {
		   		Symbol targetValue = this.lookup(symbolValue.childrenList, symbol);
		   		if(targetValue!=null)
		   			return targetValue;
		   	}
		   	
		   	if(symbolValue.getFullyQualifiedId()==symbol.getFullyQualifiedId())
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
	public boolean isReference(List<Symbol> symbolDictionary, Symbol refSym) {
		int hashValue = refSym.getId();
	    Iterator<Symbol> it = symbolDictionary.iterator();

		if(hashValue==0 && refSym.getName()!=null)
			hashValue = refSym.getName().hashCode();
		refSym.setId(hashValue);
		 
	    // Iterating the list in forward direction
	    while(it.hasNext()){
		   	Symbol symbolValue = (Symbol)it.next();
		   	if(symbolValue.childrenList!=null) {
		   		if(this.isReference(symbolValue.childrenList, refSym))
		   			return true;
		   	}
		   	if(StringUtils.equalsIgnoreCase(symbolValue.getPackageName(), refSym.getPackageName())
		   			&& StringUtils.equalsIgnoreCase(symbolValue.getName(), refSym.getName())
		   			&& symbolValue.getType()==SymbolType.DERIVED_EVENT)
		   		return true;
	    }
	    return false;		
	}
	public Symbol getReference(List<Symbol> symbolDictionary, Symbol refSym) {
		int hashValue = refSym.getId();
	    Iterator<Symbol> it = symbolDictionary.iterator();

		if(hashValue==0 && refSym.getName()!=null)
			hashValue = refSym.getName().hashCode();
		refSym.setId(hashValue);
		 
	    // Iterating the list in forward direction
	    while(it.hasNext()){
		   	Symbol symbolValue = (Symbol)it.next();
		   	if(symbolValue.childrenList!=null) {
		   		Symbol referenceSymbol = this.getReference(symbolValue.childrenList, refSym);
		   		if(referenceSymbol!=null)
		   			return referenceSymbol;
		   	}
		   	if(StringUtils.equalsIgnoreCase(symbolValue.getPackageName(), refSym.getPackageName())
		   			&& StringUtils.equalsIgnoreCase(symbolValue.getName(), refSym.getName())
		   			&& symbolValue.getType()==SymbolType.DERIVED_EVENT)
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

	@Override
	public void print_symbol_table() {
		System.out.println(symbolList);
	}

}
