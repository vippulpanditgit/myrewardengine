package com.myreward.engine.event;

import com.myreward.engine.model.event.EventDO;

public interface EventListener {
	public void handler(EventDO eventDO);
}
