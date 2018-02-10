package com.myreward.parser.generated;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.myreward.parser.symbol.Symbol;
import com.myreward.parser.symbol.SymbolTable;

public class MyRewardCodeGenerator {
	private Map<Integer, Integer> xmapdataSegment = new HashMap<Integer, Integer>();
	private List<Long> dataSegment = new ArrayList<Long>();
	private List<String> codeSegment = new ArrayList<String>();

	public List<Long> getDataSegment() {
		return dataSegment;
	}
	public void setDataSegment(List<Long> dataSegment) {
		this.dataSegment = dataSegment;
	}
	public List<String> getCodeSegment() {
		return codeSegment;
	}
	public void setCodeSegment(List<String> codeSegment) {
		this.codeSegment = codeSegment;
	}
	public Map<Integer, Integer> getXmapdataSegment() {
		return xmapdataSegment;
	}
	public void setXmapdataSegment(Map<Integer, Integer> xmapdataSegment) {
		this.xmapdataSegment = xmapdataSegment;
	}
	public void processDataSegment(SymbolTable symbolTable) {
		List<Symbol> symbolList =  symbolTable.getAllSymbol();
		ListIterator<Symbol> symbolIterator = symbolList.listIterator();
		int index=0;
		while(symbolIterator.hasNext()) {
			Symbol symbol = symbolIterator.next();
			xmapdataSegment.put(symbol.getFullyQualifiedId(), Integer.valueOf(index++));
			dataSegment.add(Long.valueOf(0));
		}
	}
}
