package com.myreward.parser.test;

import java.util.Date;
import java.util.UUID;

import org.antlr.v4.runtime.RecognitionException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import com.myreward.engine.app.AppContext;
import com.myreward.engine.app.AppInstanceContext;
import com.myreward.engine.app.User;
import com.myreward.engine.event.error.BuildException;
import com.myreward.engine.event.error.MetaDataParsingException;
import com.myreward.engine.event.error.ReferencedModelException;
import com.myreward.engine.event.processor.EventProcessor;
import com.myreward.engine.event.processor.MetaOpCodeProcessor;
import com.myreward.engine.model.event.EventDO;
import com.myreward.parser.generator.MyRewardDataSegment;

public abstract class BaseTestCase {
	protected AppInstanceContext appInstanceContext;
	
	public MetaOpCodeProcessor createMetaOpCodeProcessor(AppInstanceContext appInstanceContext, String rule) throws RecognitionException, MetaDataParsingException, BuildException, ReferencedModelException {
		MetaOpCodeProcessor metaOpCodeProcessor = new MetaOpCodeProcessor(appInstanceContext);
		metaOpCodeProcessor.initialize();
		metaOpCodeProcessor.parse(rule, true);
		metaOpCodeProcessor.print_code_segment();
		return metaOpCodeProcessor;
	}
	public abstract String getRule();
	public void setUp() throws Exception {
        appInstanceContext = new AppInstanceContext();
        AppContext.getInstance().add(String.valueOf(getRule().hashCode()), this.createMetaOpCodeProcessor(appInstanceContext, getRule()));
        appInstanceContext.isDebug = false;
        appInstanceContext.actor = new User();
        appInstanceContext.uuid = UUID.randomUUID().toString();
        appInstanceContext.metaOpCodeProcessor =  AppContext.getInstance().get(String.valueOf(getRule().hashCode()));
        appInstanceContext.dataSegment = appInstanceContext.metaOpCodeProcessor.createDataSegment();
        appInstanceContext.eventProcessor = appInstanceContext.metaOpCodeProcessor.createEventProcessor(appInstanceContext.metaOpCodeProcessor.create_runtime_opcode_tree(),
        															appInstanceContext.dataSegment);
	}

	public void tearDown() throws Exception {
		AppContext.getInstance().reset();
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
