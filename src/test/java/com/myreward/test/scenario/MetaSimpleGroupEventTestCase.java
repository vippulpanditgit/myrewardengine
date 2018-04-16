package com.myreward.test.scenario;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.myreward.parser.test.BaseTestCase;
import com.myreward.parser.test.scenario.MetaSimpleGroupEvent;

public class MetaSimpleGroupEventTestCase extends BaseTestCase {

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
				this.getAppInstanceContext().eventProcessor.process_event(this.createEvent("B", new Date()));
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaSimpleGroupEvent.GA.B").name.equalsIgnoreCase("MetaSimpleGroupEvent.GA.B"));
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaSimpleGroupEvent.GA.B").eventCount==1);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaSimpleGroupEvent.GA.C").eventCount==0);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaSimpleGroupEvent.GA.B").getReward()==0.0);
				assertFalse(this.getAppInstanceContext().dataSegment.getDataObject("MetaSimpleGroupEvent.GA.B").isDurationFlagSet());
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaSimpleGroupEvent.GA.B").isGatekeeperStatusSet());
				assertFalse(this.getAppInstanceContext().dataSegment.getDataObject("MetaSimpleGroupEvent.GA.B").isPriorityFlagSet());
				assertFalse(this.getAppInstanceContext().dataSegment.getDataObject("MetaSimpleGroupEvent.GA.B").isRepeatFlagSet());
				assertFalse(this.getAppInstanceContext().dataSegment.getDataObject("MetaSimpleGroupEvent.GA.B").isShowFlagSet());
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaSimpleGroupEvent.GA").isEventCompletionFlagSet());
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaSimpleGroupEvent.GA").eventCount==1);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaSimpleGroupEvent.GA").getReward()==1.0);
				assertTrue(!this.getAppInstanceContext().dataSegment.getDataObject("MetaSimpleGroupEvent.GA.C").isEventCompletionFlagSet());
				System.out.println("***** Next Event");
				this.getAppInstanceContext().eventProcessor.process_event(this.createEvent("C", new Date()));
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaSimpleGroupEvent.GA.C").name.equalsIgnoreCase("MetaSimpleGroupEvent.GA.C"));
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaSimpleGroupEvent.GA.B").eventCount==1);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaSimpleGroupEvent.GA.C").eventCount==1);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaSimpleGroupEvent.GA.B").getReward()==0.0);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaSimpleGroupEvent.GA.C").getReward()==0.0);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaSimpleGroupEvent.GA").isEventCompletionFlagSet());
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaSimpleGroupEvent.GA").eventCount==2);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaSimpleGroupEvent.GA").getReward()==2.0);
				assertTrue(!this.getAppInstanceContext().dataSegment.getDataObject("MetaSimpleGroupEvent.GA.C").isEventCompletionFlagSet());
				assertTrue(!this.getAppInstanceContext().dataSegment.getDataObject("MetaSimpleGroupEvent.GA.B").isEventCompletionFlagSet());
				this.getAppInstanceContext().print_data_segment();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String getRule() {
		return MetaSimpleGroupEvent.metaData;
	}

}
