package com.myreward.parser.generated;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.myreward.parser.symbol.Symbol;
import com.myreward.parser.symbol.SymbolTable;

public class MyRewardPCodeGenerator {
	private Map<Integer, Integer> xmapdataSegment = new HashMap<Integer, Integer>();
	private List<EventDataObject> dataSegment = new ArrayList<EventDataObject>();
	private List<String> codeSegment = new ArrayList<String>();

	public List<EventDataObject> getDataSegment() {
		return dataSegment;
	}
	public void setDataSegment(List<EventDataObject> dataSegment) {
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
	public class EventDataObject {
		public byte eventStatus = 0x02;
		public Integer eventCount = new Integer(0);
		public Double amount = new Double(0.0);
		
		public void setEventCompletionStatus() {
			eventStatus |= 0x01;
		}
		public void resetGatekeeperStatus() {
			eventStatus &= 0xfd;
		}
		public void setGatekeeperStatus() {
			eventStatus |= 0x02;
		}
		public void resetRewardStatus() {
			eventStatus &= 0xfb;
		}
		public void setRewardStatus() {
			eventStatus |= 0x04;
		}
	}
	/*
	 * byte 
	 * 	bit 0 - event completion
	 * 	bit 1 - gatekeeper status(default is true)
	 * 	bit 2 - reward status
	 * Integer - completion count
	 * Double - reward amount 
	 */
	public void processDataSegment(SymbolTable symbolTable) {
		List<Symbol> symbolList =  symbolTable.getAllSymbol();
		ListIterator<Symbol> symbolIterator = symbolList.listIterator();
		int index=0;
		while(symbolIterator.hasNext()) {
			Symbol symbol = symbolIterator.next();
			xmapdataSegment.put(symbol.getFullyQualifiedId(), Integer.valueOf(index++));
			dataSegment.add(new EventDataObject());
		}
	}
	public EventDataObject getDataObject(int id) {
		if(xmapdataSegment!=null) {
			Integer dataSegmentIndex = xmapdataSegment.get(id);
			return dataSegment.get(dataSegmentIndex.intValue());
		} return null;
	}
	public void setDataObject(int id, EventDataObject eventDataObject) {
		if(xmapdataSegment!=null) {
			Integer dataSegmentIndex = xmapdataSegment.get(id);
			dataSegment.remove(dataSegmentIndex.intValue());
			dataSegment.add(dataSegmentIndex.intValue(), eventDataObject);
		}
	}
}
