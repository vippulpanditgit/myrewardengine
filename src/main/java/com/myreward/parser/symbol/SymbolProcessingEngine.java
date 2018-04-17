package com.myreward.parser.symbol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class SymbolProcessingEngine {
	public List<Number> dataSegment = new ArrayList<Number>();
	public Map<Integer, Integer> dataSegmentMap = new HashMap<Integer, Integer>();
	
	
	public void process(SymbolTable symbolTable) {
		List<Symbol> symbolList =  symbolTable.getAllSymbol();
		ListIterator<Symbol> symbolIterator = symbolList.listIterator();
		int index=0;
		while(symbolIterator.hasNext()) {
			Symbol symbol = symbolIterator.next();
			dataSegmentMap.put(symbol.getFullyQualifiedId(), Integer.valueOf(index++));
			dataSegment.add(Long.valueOf(0));
		}
	}

}
