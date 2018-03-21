package com.myreward.parser.generator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

import com.myreward.engine.app.AppInstanceContext;
import com.myreward.engine.delegate.EventDataObjectDelegate;
import com.myreward.engine.delegate.IRuntimeDelegate;
import com.myreward.parser.symbol.Symbol;
import com.myreward.parser.symbol.SymbolTable;

public class MyRewardDataSegment implements Serializable {
	private AppInstanceContext parentContext;
	private MultiValuedMap<Integer, Integer> xmapdataSegment = new ArrayListValuedHashMap<Integer, Integer>();
	private List<EventDataObject> dataSegment = new ArrayList<EventDataObject>();
	private IRuntimeDelegate delegate;
	public class EventDataObject implements Serializable {
		// 0x0000 0000 0000 0001 - Event Complete
		// 0x0000 0000 0000 0010 - Gatekeeper - 1 for complete, 0 for incomplete
		// 0x0000 0000 0000 0100 - Reward enabled
		// 0x0000 0000 0000 1000 - Show enabled
		// 0x0000 0000 0001 0000 - priority enabled
		// 0x0000 0000 0010 0000 - repeat enabled
		// 0x0000 0000 0100 0000 - duration - 1 within duration, 0 for outside duration
		// 0x0000 0000 1000 0000 - gatekeeper call - 1 when event complete and event count are not changed(0x80)
		public short eventStatus = 0x02;
		public Integer eventCount = Integer.valueOf(0);
		public Double amount = Double.valueOf(0.0);
		public Integer maxRepeat = Integer.valueOf(0);
		public Integer priority = Integer.valueOf(0);
		public Date nextRpeat;
		public String description;
		public String name;
		public long id;
		public long version;
		private IRuntimeDelegate eventDelegate;
		
		public void delegate(IRuntimeDelegate eventDelegate) {
			this.eventDelegate = eventDelegate;
			this.eventDelegate.creation(this);
		}
		public String toString() {
			return name+"<<"+id+"<<"+description+"<<"+eventStatus+"<<"+eventCount+"<<"+amount+"<<"+maxRepeat+"<<"+priority+"<<"+nextRpeat;
		}
		public void setEventCompletionFlag() {
			eventStatus |= 0x01;
			if(this.eventDelegate!=null)
				this.eventDelegate.changed(this, EventDataObjectDelegate.trace(Thread.currentThread().getStackTrace()));
		}
		public void resetEventCompletionFlag() {
			eventStatus &= 0xfe;
			if(this.eventDelegate!=null)
				this.eventDelegate.changed(this, EventDataObjectDelegate.trace(Thread.currentThread().getStackTrace()));

		}
		public boolean isEventCompletionFlagSet() {
			if((eventStatus & 0x01)==0x01)
				return true;
			return false;
		}
		public void resetGatekeeperStatus() {
			eventStatus &= 0xfd;
			if(this.eventDelegate!=null)
				this.eventDelegate.changed(this, EventDataObjectDelegate.trace(Thread.currentThread().getStackTrace()));
		}
		public void setGatekeeperStatus() {
			eventStatus |= 0x02;
			if(this.eventDelegate!=null)
				this.eventDelegate.changed(this, EventDataObjectDelegate.trace(Thread.currentThread().getStackTrace()));
		}
		public boolean isGatekeeperStatusSet() {
			if((eventStatus & 0x02)==0x02)
				return true;
			return false;
		}
		public void resetRewardStatus() {
			eventStatus &= 0xfb;
			if(this.eventDelegate!=null)
				this.eventDelegate.changed(this, EventDataObjectDelegate.trace(Thread.currentThread().getStackTrace()));
		}
		public void setRewardStatus() {
			eventStatus |= 0x04;
			if(this.eventDelegate!=null)
				this.eventDelegate.changed(this, EventDataObjectDelegate.trace(Thread.currentThread().getStackTrace()));
		}
		public boolean isRewardStatusSet() {
			if((eventStatus & 0x04)==0x04)
				return true;
			return false;
		}
		public void setShowFlag() {
			eventStatus |= 0x08;
			if(this.eventDelegate!=null)
				this.eventDelegate.changed(this, EventDataObjectDelegate.trace(Thread.currentThread().getStackTrace()));
		}
		public boolean isShowFlagSet() {
			if((eventStatus & 0x08)==0x08)
				return true;
			return false;
		}
		public void setPriorityFlag() {
			eventStatus |=0x10;
			if(this.eventDelegate!=null)
				this.eventDelegate.changed(this, EventDataObjectDelegate.trace(Thread.currentThread().getStackTrace()));
		}
		public boolean isPriorityFlagSet() {
			if((eventStatus & 0x10)==0x10)
				return true;
			return false;
		}

		public void setRepeatFlag() {
			eventStatus |= 0x20;
			if(this.eventDelegate!=null)
				this.eventDelegate.changed(this, EventDataObjectDelegate.trace(Thread.currentThread().getStackTrace()));
		}
		public boolean isRepeatFlagSet() {
			if((eventStatus & 0x20)==0x20)
				return true;
			return false;
		}

		public void setDurationFlag() {
			eventStatus |= 0x40;
			if(this.eventDelegate!=null)
				this.eventDelegate.changed(this, EventDataObjectDelegate.trace(Thread.currentThread().getStackTrace()));
		}
		public boolean isDurationFlagSet() {
			if((eventStatus & 0x40)==0x40)
				return true;
			return false;
		}

		public void resetDurationFlag() {
			eventStatus &= 0xbf;
			if(this.eventDelegate!=null)
				this.eventDelegate.changed(this, EventDataObjectDelegate.trace(Thread.currentThread().getStackTrace()));
		}
		public void setGatekeeperRelatedFlag() {
			eventStatus |= 0x80;
			if(this.eventDelegate!=null)
				this.eventDelegate.changed(this, EventDataObjectDelegate.trace(Thread.currentThread().getStackTrace()));
		}
		public boolean isGatekeeperRelatedFlagSet() {
			if((eventStatus & 0x80)==0x80)
				return true;
			return false;
		}

		public void resetGatekeeperRelatedFlag() {
			eventStatus &= 0x7f;
			if(this.eventDelegate!=null)
				this.eventDelegate.changed(this, EventDataObjectDelegate.trace(Thread.currentThread().getStackTrace()));
		}

		public int increaseCount() {
			eventCount = Integer.valueOf(eventCount.intValue() + 1);
			if(this.eventDelegate!=null)
				this.eventDelegate.changed(this, EventDataObjectDelegate.trace(Thread.currentThread().getStackTrace()));
			return eventCount.intValue();
		}
		public int resetCount() {
			eventCount = Integer.valueOf(0);
			if(this.eventDelegate!=null)
				this.eventDelegate.changed(this, EventDataObjectDelegate.trace(Thread.currentThread().getStackTrace()));
			return eventCount.intValue();
		}
		public double setReward(double rewardAmount) {
			amount = Double.valueOf(rewardAmount);
			if(this.eventDelegate!=null)
				this.eventDelegate.changed(this, EventDataObjectDelegate.trace(Thread.currentThread().getStackTrace()));
			return amount.doubleValue();
		}
		public double getReward(){
			return amount.doubleValue();
		}
		public void setMaxRepeat(int maxRepeat) {
			maxRepeat = Integer.valueOf(maxRepeat);
			if(this.eventDelegate!=null)
				this.eventDelegate.changed(this, EventDataObjectDelegate.trace(Thread.currentThread().getStackTrace()));
		}
		public int getMaxRepeat() {
			return maxRepeat.intValue();
		}
		public void setPriority(int priority) {
			priority = Integer.valueOf(priority);
			if(this.eventDelegate!=null)
				this.eventDelegate.changed(this, EventDataObjectDelegate.trace(Thread.currentThread().getStackTrace()));
		}
		public int getPriority() {
			return priority.intValue();
		}
		public void resetPriorityFlag() {
			eventStatus &= 0xef;
			if(this.eventDelegate!=null)
				this.eventDelegate.changed(this, EventDataObjectDelegate.trace(Thread.currentThread().getStackTrace()));
		}
		public void resetShowFlag() {
			eventStatus &= 0xf7;
			if(this.eventDelegate!=null)
				this.eventDelegate.changed(this, EventDataObjectDelegate.trace(Thread.currentThread().getStackTrace()));
		}
	}
	public void processDataSegment(SymbolTable symbolTable) {
		List<Symbol> symbolList =  symbolTable.getAllSymbol();
		ListIterator<Symbol> symbolIterator = symbolList.listIterator();
		int index=0;
		while(symbolIterator.hasNext()) {
			Symbol symbol = symbolIterator.next();
			if(symbol.alias!=null) {
				while(symbol!=null) {
					EventDataObject eventDataObject = new EventDataObject();
					eventDataObject.name = symbol.getFullyQualifiedName();
					eventDataObject.id = symbol.getFullyQualifiedId();
					eventDataObject.version = symbol.getVersion();
					xmapdataSegment.put(symbol.getFullyQualifiedId(), Integer.valueOf(index++));
					dataSegment.add(eventDataObject);
					symbol = symbol.alias;
				}
			} else {
				EventDataObject eventDataObject = new EventDataObject();
				eventDataObject.name = symbol.getFullyQualifiedName();
				eventDataObject.id = symbol.getFullyQualifiedId();
				eventDataObject.version = symbol.getVersion();
				xmapdataSegment.put(symbol.getFullyQualifiedId(), Integer.valueOf(index++));
				dataSegment.add(eventDataObject);
				
			}
		}
	}
	public EventDataObject getDataObject(int id) {
		if(xmapdataSegment!=null) {
			Integer dataSegmentIndex = xmapdataSegment.get(id);
			return dataSegment.get(dataSegmentIndex.intValue());
		} return null;
	}
	public EventDataObject getDataObject(String ruleAttrName) {
		int ruleAttrHashcode = ruleAttrName.hashCode();
		return getDataObject(ruleAttrHashcode);
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
	public EventDataObject search(String id) {
		Integer eventDataObjectIndex = xmapdataSegment.get(Integer.valueOf(id));
		if(eventDataObjectIndex!=null) {
			return dataSegment.get(eventDataObjectIndex.intValue());
		}
		return null;
	}
	public IRuntimeDelegate getDelegate() {
		return delegate;
	}
	public void setDelegate(IRuntimeDelegate delegate) {
		this.delegate = delegate;
		if(dataSegment!=null && dataSegment.size()>0)
			dataSegment.forEach(p->p.delegate(delegate));
	}

}
