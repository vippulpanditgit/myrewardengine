package com.myreward.parser.test;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.myreward.engine.event.processor.EventProcessor;
import com.myreward.engine.event.processor.MetaOpCodeProcessor;
import com.myreward.engine.model.event.EventDO;
import com.myreward.parser.generator.MyRewardDataSegment;

public class BaseTestCase {
	private MetaOpCodeProcessor metaOpCodeProcessor;
	@Before
	public void setUp() throws Exception {
		setMetaOpCodeProcessor(new MetaOpCodeProcessor());
		getMetaOpCodeProcessor().initialize();
	}

	@After
	public void tearDown() throws Exception {
		getMetaOpCodeProcessor().cleanUp();
	}
	protected EventDO createEvent(String name, Date eventDate) {
		EventDO eventDO = new EventDO();
		eventDO.setActivityName(name);
		eventDO.setActivityDate(eventDate);
		return eventDO;
	}

	public MetaOpCodeProcessor getMetaOpCodeProcessor() {
		return metaOpCodeProcessor;
	}

	public void setMetaOpCodeProcessor(MetaOpCodeProcessor metaOpCodeProcessor) {
		this.metaOpCodeProcessor = metaOpCodeProcessor;
	}


}
