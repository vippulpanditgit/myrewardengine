package com.myreward.parser.test;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.UUID;

import org.antlr.v4.runtime.RecognitionException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.myreward.engine.app.AppContext;
import com.myreward.engine.app.AppInstanceContext;
import com.myreward.engine.event.error.MetaDataParsingException;
import com.myreward.engine.event.processor.EventProcessor;
import com.myreward.engine.event.processor.MetaOpCodeProcessor;
import com.myreward.engine.model.event.EventDO;
import com.myreward.parser.generator.MyRewardDataSegment;

public abstract class BaseTestCase {
	private AppInstanceContext appInstanceContext;
	
	public MetaOpCodeProcessor createMetaOpCodeProcessor(AppInstanceContext appInstanceContext, String rule) throws RecognitionException, MetaDataParsingException {
		MetaOpCodeProcessor metaOpCodeProcessor = new MetaOpCodeProcessor(appInstanceContext);
		metaOpCodeProcessor.initialize();
		metaOpCodeProcessor.parse(rule, false);
		metaOpCodeProcessor.print_code_segment();
		return metaOpCodeProcessor;
	}
	public abstract String getRule();
	@Before
	public void setUp() throws Exception {
        appInstanceContext = new AppInstanceContext();
        AppContext.getInstance().add("oneEvent1", new test().createMetaOpCodeProcessor(appInstanceContext, getRule()));
        appInstanceContext.isDebug = false;
        appInstanceContext.actor = "vippul";
        appInstanceContext.uuid = UUID.randomUUID().toString();
        appInstanceContext.metaOpCodeProcessor =  AppContext.getInstance().get("oneEvent1");
        appInstanceContext.dataSegment = appInstanceContext.metaOpCodeProcessor.createDataSegment();
        appInstanceContext.eventProcessor = appInstanceContext.metaOpCodeProcessor.createEventProcessor(appInstanceContext.metaOpCodeProcessor.create_runtime_opcode_tree(),
        															appInstanceContext.dataSegment);
	}

	@After
	public void tearDown() throws Exception {
	}
	protected EventDO createEvent(String name, Date eventDate) {
		EventDO eventDO = new EventDO();
		eventDO.setActivityName(name);
		eventDO.setActivityDate(eventDate);
		return eventDO;
	}
	public AppInstanceContext getAppInstanceContext() {
		return appInstanceContext;
	}
	public void setAppInstanceContext(AppInstanceContext appInstanceContext) {
		this.appInstanceContext = appInstanceContext;
	}




}
