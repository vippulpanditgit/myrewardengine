package com.myreward.engine.audit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EventBatch<T extends Event> implements Iterable<T> {

	private List<T> eventList = new ArrayList<T>();
	   public EventBatch() {
	        
	    }
	    
	    public EventBatch(T... events) {
	        addAll(this.eventList, eventList);
	    }
	    
	    private void addAll(List<T> eventList2, List<T> eventList3) {
			// TODO Auto-generated method stub
			
		}

		public EventBatch(List<T> eventList) {
	        this.eventList = eventList;
	    }
	    public void addEvent(final T event) {
	        eventList.add(event);
	    }
	    
	    /**
	     * Remove a event from the batch.
	     *
	     * @param event to remove
	     */
	    public void removeEvent  (final T event) {
	        eventList.remove(event);
	    }

	    /**
	     * Check if the batch is empty.
	     *
	     * @return true if the batch is empty, false otherwise
	     */
	    public boolean isEmpty() {
	        return eventList.isEmpty();
	    }
	    
	    /**
	     * Get the size of the batch.
	     *
	     * @return the size of the batch
	     */
	    public long size() {
	        return eventList.size();
	    }
	    
	    /**
	     * Get the event by index.
	     *
	     * @return the Event
	     */
	    public Event get(int index) {
	        return eventList.get(index);
	    }
	    
	    @Override
	    public Iterator iterator() {
	        return eventList.iterator();
	    }
	    
	    @Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (o == null || getClass() != o.getClass()) return false;

	        EventBatch batch = (EventBatch) o;

	        return eventList.equals(batch.eventList);

	    }

	    @Override
	    public int hashCode() {
	        return eventList.hashCode();
	    }


	    @Override
	    public String toString() {
	        StringBuilder stringBuilder = new StringBuilder();
	        stringBuilder.append("Batch: {");
	        stringBuilder.append("|");
	        for (Event event : eventList) {
	            stringBuilder.append('\t');
	            stringBuilder.append(event);
	            stringBuilder.append("|");
	        }
	        stringBuilder.append('}');
	        return stringBuilder.toString();
	    }

}
