package com.myreward.engine.event.opcode.processing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.myreward.engine.event.listener.IEventProcessorListener;
import com.myreward.engine.event.processing.helper.OperationResultDO;
import com.myreward.engine.model.event.EventDO;
import com.myreward.parser.generator.MyRewardDataSegment;

public  class OpCodeBaseModel implements Serializable {
	private List<IEventProcessorListener> listeners = new ArrayList<>();
	
	public  String[] getOpcodes() {
		return null;};

	public OperationResultDO process(List<OpCodeBaseModel> instructionOpCodes, MyRewardDataSegment myRewardDataSegment, EventDO event)  throws Exception {
		return null;};
	public void registerProcessingListener(IEventProcessorListener listener) {
		this.listeners.add(listener);
	}
	public void unregisterProcessingListener(IEventProcessorListener listener) {
		this.listeners.remove(listener);
	}
	   public void notifyProcessingListeners(String opcode) {
	        // Notify each of the listeners in the list of registered listeners
	        this.listeners.forEach(listener -> listener.onExecute(opcode));
	    }
}
