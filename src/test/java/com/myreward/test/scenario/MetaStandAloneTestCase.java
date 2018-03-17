package com.myreward.test.scenario;

import static org.junit.Assert.*;

import java.util.Date;

import org.antlr.v4.runtime.RecognitionException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.myreward.engine.event.error.EventProcessingException;
import com.myreward.engine.event.error.MetaDataParsingException;
import com.myreward.engine.event.processor.EventProcessor;
import com.myreward.engine.model.event.EventDO;
import com.myreward.parser.generator.MyRewardDataSegment;
import com.myreward.parser.test.BaseTestCase;
import com.myreward.parser.test.scenario.MetaSimpleGroupEvent;
import com.myreward.parser.test.scenario.MetaStandAloneEvent;

public class MetaStandAloneTestCase extends BaseTestCase {

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void run_events() {
		try {
			this.getAppInstanceContext().print_data_segment();
			if(this.getAppInstanceContext().isInstanceReady()) {
				this.getAppInstanceContext().eventProcessor.process_event(this.createEvent("A", new Date()));
				Assert.assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("A").name.equalsIgnoreCase("A"));
				Assert.assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("A").eventCount==1);
				Assert.assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("A").getReward()==1.0);
				Assert.assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("A").isDurationFlagSet());
				Assert.assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("A").isGatekeeperStatusSet());
				Assert.assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("A").isPriorityFlagSet());
				Assert.assertFalse(this.getAppInstanceContext().dataSegment.getDataObject("A").isRepeatFlagSet());
				Assert.assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("A").isShowFlagSet());

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
