package com.myreward.test.scenario;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.myreward.engine.event.error.MetaDataParsingException;
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
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("B").name.equalsIgnoreCase("B"));
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("B").eventCount==1);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("C").eventCount==0);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("B").getReward()==0.0);
				assertFalse(this.getAppInstanceContext().dataSegment.getDataObject("B").isDurationFlagSet());
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("B").isGatekeeperStatusSet());
				assertFalse(this.getAppInstanceContext().dataSegment.getDataObject("B").isPriorityFlagSet());
				assertFalse(this.getAppInstanceContext().dataSegment.getDataObject("B").isRepeatFlagSet());
				assertFalse(this.getAppInstanceContext().dataSegment.getDataObject("B").isShowFlagSet());
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("GA").isEventCompletionFlagSet());
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("GA").eventCount==1);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("GA").getReward()==1.0);
				assertTrue(!this.getAppInstanceContext().dataSegment.getDataObject("C").isEventCompletionFlagSet());
				this.getAppInstanceContext().eventProcessor.process_event(this.createEvent("C", new Date()));
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("C").name.equalsIgnoreCase("C"));
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("B").eventCount==1);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("C").eventCount==1);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("B").getReward()==0.0);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("C").getReward()==0.0);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("GA").isEventCompletionFlagSet());
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("GA").eventCount==2);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("GA").getReward()==2.0);
				assertTrue(!this.getAppInstanceContext().dataSegment.getDataObject("C").isEventCompletionFlagSet());
				assertTrue(!this.getAppInstanceContext().dataSegment.getDataObject("B").isEventCompletionFlagSet());

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
