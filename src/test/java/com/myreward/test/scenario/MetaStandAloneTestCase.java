package com.myreward.test.scenario;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.myreward.parser.test.BaseTestCase;
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
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaStandAloneEvent1.A").name.equalsIgnoreCase("MetaStandAloneEvent1.A"));
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaStandAloneEvent1.A").eventCount==1);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaStandAloneEvent1.A").getReward()==1.0);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaStandAloneEvent1.A").isDurationFlagSet());
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaStandAloneEvent1.A").isGatekeeperStatusSet());
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaStandAloneEvent1.A").isPriorityFlagSet());
				assertFalse(this.getAppInstanceContext().dataSegment.getDataObject("MetaStandAloneEvent1.A").isRepeatFlagSet());
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaStandAloneEvent1.A").isShowFlagSet());

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
