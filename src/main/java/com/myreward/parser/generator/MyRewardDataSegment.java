package com.myreward.parser.generator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

import com.myreward.engine.app.AppInstanceContext;
import com.myreward.engine.delegate.EventDataObjectDelegate;
import com.myreward.engine.delegate.IRuntimeDelegate;
import com.myreward.parser.metamodel.RepeatMetaModel.RepeatCriteria;
import com.myreward.parser.symbol.Symbol;
import com.myreward.parser.symbol.SymbolTable;

public class MyRewardDataSegment<T> implements Serializable {
	private AppInstanceContext parentContext;
	private Map<Long, Integer> xmapdataSegment = new HashMap<>();
	private List<EventDataObject> dataSegment = new ArrayList<>();
	private IRuntimeDelegate delegate;
	private T delegateT; // New delegate
	public class EventDataObject implements Serializable {
		// 0x0000 0000 0000 0001 - Event Complete
		// 0x0000 0000 0000 0010 - Gatekeeper - 1 for complete, 0 for incomplete
		// 0x0000 0000 0000 0100 - Reward enabled
		// 0x0000 0000 0000 1000 - Show enabled
		// 0x0000 0000 0001 0000 - priority enabled
		// 0x0000 0000 0010 0000 - repeat enabled
		// 0x0000 0000 0100 0000 - duration - 1 within duration, 0 for outside duration
		// 0x0000 0000 1000 0000 - gatekeeper call - 1 when event complete and event count are not changed(0x80)
		public short eventStatus = 0x0020;
		public Integer eventCount = Integer.valueOf(0);
		public Double amount = Double.valueOf(0.0);
		public Integer maxRepeat = Integer.valueOf(0);
		public Integer priority = Integer.valueOf(0);
		public Date nextRepeat;
		public String description;
		public String name;
		public long id;
		public String namespace;
		public long version;
		private IRuntimeDelegate eventDelegate;
		public RepeatCriteria repeatCriteria;
		public int repeatAfter;
		public EventDataObject() {
			
		}
		public EventDataObject(EventDataObject eventDataObject) {
			this.eventStatus = eventDataObject.eventStatus;
			this.eventCount = eventDataObject.eventCount.intValue();
			this.amount = eventDataObject.amount.doubleValue();
			this.maxRepeat = eventDataObject.maxRepeat.intValue();
			this.priority = eventDataObject.priority.intValue();
			this.nextRepeat = eventDataObject.nextRepeat;
			this.description = eventDataObject.description;
			this.name = eventDataObject.name;
			this.id = eventDataObject.id;
			this.namespace = eventDataObject.namespace;
			this.version = eventDataObject.version;
			this.eventDelegate = eventDataObject.eventDelegate;
			this.repeatCriteria = eventDataObject.repeatCriteria;
			this.repeatAfter = eventDataObject.repeatAfter;
		}

		public EventDataObject(Symbol symbol) {
			this.name = symbol.getFullyQualifiedName();
			this.id = symbol.getFullyQualifiedId();
			this.version = symbol.getVersion();
		}
		public void delegate(IRuntimeDelegate eventDelegate) {
			this.eventDelegate = eventDelegate;
			this.eventDelegate.creation(this);
		}
		public String toString() {
			return name+"<<"+id+"<<"+version+"<<"+description+"<<"+eventStatus+"<<"+eventCount+"<<"+amount+"<<"+maxRepeat+"<<"+priority+"<<"+nextRepeat;
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
		public RepeatCriteria getRepeatCriteria() {
			return repeatCriteria;
		}
		public void setRepeatCriteria(RepeatCriteria repeatCriteria) {
			this.repeatCriteria = repeatCriteria;
		}
		public int getRepeatAfter() {
			return repeatAfter;
		}
		public void setRepeatAfter(int repeatAfter) {
			this.repeatAfter = repeatAfter;
		}
	}
	public List<EventDataObject> clone_data_segment() {
		return dataSegment.stream()
						.map(item -> new EventDataObject(item))
						.collect(Collectors.toList());
	}
	private int recursivelyCreateDataSegment(int index, Symbol symbol) {
		if(symbol.childrenList!=null) {
			for(int childIndex=0;childIndex<symbol.childrenList.size();childIndex++) {
				EventDataObject eventDataObject = new EventDataObject(symbol.childrenList.get(childIndex));
				if(this.getDataObject(symbol.childrenList.get(childIndex).getFullyQualifiedId())==null) {
					xmapdataSegment.put(Long.valueOf(symbol.childrenList.get(childIndex).getFullyQualifiedId()), Integer.valueOf(index));
					dataSegment.add(index, eventDataObject);
					index++;
				}
				index = this.recursivelyCreateDataSegment(index, symbol.childrenList.get(childIndex));
			}
		} else {
			EventDataObject eventDataObject = new EventDataObject(symbol);
			if(this.getDataObject(symbol.getFullyQualifiedId())==null) {
				xmapdataSegment.put(Long.valueOf(symbol.getFullyQualifiedId()), Integer.valueOf(index));
				dataSegment.add(index, eventDataObject);
				index++;
			}
			
		}
		return index;
	}
	public void processDataSegment(SymbolTable symbolTable) {
		List<Symbol> symbolList =  symbolTable.getAllSymbol();
		ListIterator<Symbol> symbolIterator = symbolList.listIterator();
		int index=0;
		while(symbolIterator.hasNext()) {
			Symbol symbol = symbolIterator.next();
			index = this.recursivelyCreateDataSegment(index, symbol);
		}
	}
	public EventDataObject getDataObject(long id) {
		if(xmapdataSegment!=null) {
			Integer dataSegmentIndex = xmapdataSegment.get(id);
			if(dataSegmentIndex!=null) {
				if(dataSegment!=null&& dataSegment.size()>dataSegmentIndex.intValue())
					return dataSegment.get(dataSegmentIndex.intValue());
			}
		} 
		return null;
	}
	public EventDataObject getDataObject(String ruleAttrName) {
		int ruleAttrHashcode = ruleAttrName.hashCode();
		return getDataObject(ruleAttrHashcode);
	}
	public void setDataObject(long id, EventDataObject eventDataObject) {
		if(xmapdataSegment!=null) {
			Integer dataSegmentIndex = xmapdataSegment.get(id);
			if(dataSegmentIndex!=null)
				dataSegment.add((int)id, eventDataObject);
		}
	}
	public void printString() {
		dataSegment.forEach(eventObject -> System.out.println(eventObject));
	}
	public EventDataObject search(String id) {
		Integer eventDataObjectIndex = xmapdataSegment.get(Long.valueOf(id));
		return dataSegment.get(eventDataObjectIndex.intValue());
	}
	public IRuntimeDelegate getDelegate() {
		return delegate;
	}
	public void setDelegate(IRuntimeDelegate delegate) {
		this.delegate = delegate;
		if(dataSegment!=null && dataSegment.size()>0)
			dataSegment.forEach(p->p.delegate(delegate));
	}
	public List<EventDataObject> getDataSegment() {
		return dataSegment;
	}
	public void setDataSegment(List<EventDataObject> dataSegment) {
		this.dataSegment = dataSegment;
	}

}
