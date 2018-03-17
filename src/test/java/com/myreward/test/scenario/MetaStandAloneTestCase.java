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
				this.getAppInstanceContext().eventProcessor.process_event(this.createEvent("B", new Date()));
				Assert.assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("B").name.equalsIgnoreCase("B"));
				Assert.assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("B").eventCount==1);
				Assert.assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("C").eventCount==0);
				Assert.assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("B").getReward()==0.0);
				Assert.assertFalse(this.getAppInstanceContext().dataSegment.getDataObject("B").isDurationFlagSet());
				Assert.assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("B").isGatekeeperStatusSet());
				Assert.assertFalse(this.getAppInstanceContext().dataSegment.getDataObject("B").isPriorityFlagSet());
				Assert.assertFalse(this.getAppInstanceContext().dataSegment.getDataObject("B").isRepeatFlagSet());
				Assert.assertFalse(this.getAppInstanceContext().dataSegment.getDataObject("B").isShowFlagSet());
				Assert.assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("GA").isEventCompletionFlagSet());
				Assert.assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("GA").eventCount==1);
				Assert.assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("GA").getReward()==1.0);
				Assert.assertTrue(!this.getAppInstanceContext().dataSegment.getDataObject("C").isEventCompletionFlagSet());
				this.getAppInstanceContext().eventProcessor.process_event(this.createEvent("C", new Date()));
				Assert.assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("C").name.equalsIgnoreCase("C"));
				Assert.assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("B").eventCount==1);
				Assert.assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("C").eventCount==1);
				Assert.assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("B").getReward()==0.0);
				Assert.assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("C").getReward()==0.0);
				Assert.assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("GA").isEventCompletionFlagSet());
				Assert.assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("GA").eventCount==2);
				Assert.assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("GA").getReward()==2.0);
				Assert.assertTrue(!this.getAppInstanceContext().dataSegment.getDataObject("C").isEventCompletionFlagSet());
				Assert.assertTrue(!this.getAppInstanceContext().dataSegment.getDataObject("B").isEventCompletionFlagSet());

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String getRule() {
		// TODO Auto-generated method stub
		return MetaSimpleGroupEvent.metaData;
	}

}
