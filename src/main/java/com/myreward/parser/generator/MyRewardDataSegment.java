package com.myreward.parser.generator;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.myreward.parser.symbol.Symbol;
import com.myreward.parser.symbol.SymbolTable;

public class MyRewardDataSegment {
	private Map<Integer, Integer> xmapdataSegment = new HashMap<Integer, Integer>();
	private List<EventDataObject> dataSegment = new ArrayList<EventDataObject>();
	public class EventDataObject {
		// 0x0000 0000 0000 0001 - Event Complete
		// 0x0000 0000 0000 0010 - Gatekeeper - 1 for complete, 0 for incomplete
		// 0x0000 0000 0000 0100 - Reward enabled
		// 0x0000 0000 0000 1000 - Show enabled
		// 0x0000 0000 0001 0000 - priority enabled
		// 0x0000 0000 0010 0000 - repeat enabled
		// 0x0000 0000 0100 0000 - duration - 1 within duration, 0 for outside duration
		public short eventStatus = 0x02;
		public Integer eventCount = Integer.valueOf(0);
		public Double amount = Double.valueOf(0.0);
		public Integer maxRepeat = Integer.valueOf(0);
		public Integer priority = Integer.valueOf(0);
		public Date nextRpeat;
		public String description;
		public String name;
		public long id;
		
		public String toString() {
			return name+"<<"+id+"<<"+description+"<<"+eventStatus+"<<"+eventCount+"<<"+amount+"<<"+maxRepeat+"<<"+priority;
		}
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
		public void setShowFlag() {
			eventStatus |= 0x08;
		}
		public void setPriorityFlag() {
			eventStatus |=0x10;
		}
		public void setRepeatFlag() {
			eventStatus |= 0x20;
		}
		public void setDurationFlag() {
			eventStatus |= 0x40;
		}
		public void resetDurationFlag() {
			eventStatus &= 0xbf;
		}
		public int increaseCount() {
			eventCount = Integer.valueOf(eventCount.intValue() + 1);
			return eventCount.intValue();
		}
		public int resetCount() {
			eventCount = Integer.valueOf(0);
			return eventCount.intValue();
		}
		public double setReward(double rewardAmount) {
			amount = Double.valueOf(rewardAmount);
			return amount.doubleValue();
		}
		public void setMaxRepeat(int maxRepeat) {
			maxRepeat = Integer.valueOf(maxRepeat);
		}
		public int getMaxRepeat() {
			return maxRepeat.intValue();
		}
		public void setPriority(int priority) {
			priority = Integer.valueOf(priority);
		}
		public int getPriority() {
			return priority.intValue();
		}
	}
	public void processDataSegment(SymbolTable symbolTable) {
		List<Symbol> symbolList =  symbolTable.getAllSymbol();
		ListIterator<Symbol> symbolIterator = symbolList.listIterator();
		int index=0;
		while(symbolIterator.hasNext()) {
			Symbol symbol = symbolIterator.next();
			EventDataObject eventDataObject = new EventDataObject();
			eventDataObject.name = symbol.getFullyQualifiedName();
			eventDataObject.id = symbol.getFullyQualifiedId();
			
			xmapdataSegment.put(symbol.getFullyQualifiedId(), Integer.valueOf(index++));
			dataSegment.add(eventDataObject);
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
	public void printString() {
		dataSegment.forEach(eventObject -> System.out.println(eventObject));
	}

}
