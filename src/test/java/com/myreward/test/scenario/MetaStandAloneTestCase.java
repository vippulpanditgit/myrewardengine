package com.myreward.test.scenario;

import java.util.Date;

import org.antlr.v4.runtime.RecognitionException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.myreward.engine.event.error.EventProcessingException;
import com.myreward.engine.event.error.MetaDataParsingException;
import com.myreward.engine.event.processor.EventProcessor;
import com.myreward.engine.model.event.EventDO;
import com.myreward.parser.generator.MyRewardDataSegment;
import com.myreward.parser.test.BaseTestCase;
import com.myreward.parser.test.scenario.MetaSimpleGroupEvent;
import com.myreward.parser.test.scenario.MetaStandAloneEvent;

public class MetaStandAloneTestCase extends BaseTestCase {

	@BeforeEach
	public void setUp() throws Exception {
		super.setUp();
	}

	@AfterEach
	public void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void run_events() {
		try {
			this.getAppInstanceContext().print_data_segment();
			if(this.getAppInstanceContext().isInstanceReady()) {
				this.getAppInstanceContext().eventProcessor.process_event(this.createEvent("A", new Date()));
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("A").name.equalsIgnoreCase("A"));
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("A").eventCount==1);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("A").getReward()==1.0);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("A").isDurationFlagSet());
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("A").isGatekeeperStatusSet());
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("A").isPriorityFlagSet());
				assertFalse(this.getAppInstanceContext().dataSegment.getDataObject("A").isRepeatFlagSet());
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("A").isShowFlagSet());

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String getRule() {
		// TODO Auto-generated method stub
		return MetaStandAloneEvent.metaDataDuration;
	}

}
