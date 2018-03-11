package com.myreward.engine.event.error;

import com.myreward.engine.model.event.EventDO;
import com.myreward.parser.generator.MyRewardDataSegment;

public class DebugException extends Exception {
	public int opCodeIndex=-1;
	public EventDO eventDO = null;
	public MyRewardDataSegment myRewardDataSegment = null;

}
