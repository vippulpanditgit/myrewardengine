package com.myreward.parser.util;

import com.myreward.engine.app.AppInstanceContext;
import com.myreward.engine.event.error.DebugException;
import com.myreward.engine.model.event.EventDO;

public class EventProcessingUtil {
	public static void processEvent(AppInstanceContext appInstanceContext, EventDO eventDO) throws Exception {
		appInstanceContext.print_data_segment();
		try {
			if(appInstanceContext.isInstanceReady())
				appInstanceContext.eventProcessor.process_event(eventDO);
		} catch(DebugException debugException) {
			appInstanceContext.eventProcessor.setMyRewardDataSegment(debugException.myRewardDataSegment);
			while(true) {
				try {
					int index = appInstanceContext.eventProcessor.step(debugException.opCodeIndex, debugException.eventDO);
					debugException.opCodeIndex = index;
					if(appInstanceContext.eventProcessor.getInstructionOpCodes().size()-1<=index)
						break;
				} catch(DebugException deepDebugException) {
					debugException.eventDO = deepDebugException.eventDO;
					debugException.myRewardDataSegment = deepDebugException.myRewardDataSegment;
					debugException.opCodeIndex = deepDebugException.opCodeIndex;
				}
			}
		}
		appInstanceContext.print_data_segment();
	}
	public static void processEvent(AppInstanceContext appInstanceContext) throws Exception {
		appInstanceContext.print_data_segment();
		try {
			EventDO eventDO = (EventDO)appInstanceContext.stackSegment.peek();
			if(appInstanceContext.isInstanceReady())
				appInstanceContext.eventProcessor.process_event(eventDO);
		} catch(DebugException debugException) {
			appInstanceContext.eventProcessor.setMyRewardDataSegment(debugException.myRewardDataSegment);
			while(true) {
				try {
					int index = appInstanceContext.eventProcessor.step(debugException.opCodeIndex, debugException.eventDO);
					debugException.opCodeIndex = index;
					if(appInstanceContext.eventProcessor.getInstructionOpCodes().size()-1<=index)
						break;
				} catch(DebugException deepDebugException) {
					debugException.eventDO = deepDebugException.eventDO;
					debugException.myRewardDataSegment = deepDebugException.myRewardDataSegment;
					debugException.opCodeIndex = deepDebugException.opCodeIndex;
				}
			}
		}
		appInstanceContext.print_data_segment();
	}
}
