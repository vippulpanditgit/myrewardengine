package com.myreward.test.scenario;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.myreward.parser.test.BaseTestCase;
import com.myreward.parser.test.scenario.MetaSubGroupEventNoReward;

public class MetaSubGroupEventNoRewardTestCase extends BaseTestCase {

	@BeforeEach
	public void setUp() throws Exception {
		super.setUp();
	}

	@AfterEach
	public void tearDown() throws Exception {
		super.tearDown();
	}

//	@Test
	public void run_events() {
		try {
			this.getAppInstanceContext().print_data_segment();
			if(this.getAppInstanceContext().isInstanceReady()) {
				this.getAppInstanceContext().eventProcessor.process_event(this.createEvent("I", new Date()));
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("test.GA.GF.I").name.equalsIgnoreCase("test.GA.GF.I"));
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("test.GA.GF.I").eventCount==1);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("test.GA.GF.I").getReward()==0.0);
				assertFalse(this.getAppInstanceContext().dataSegment.getDataObject("test.GA.GF.I").isDurationFlagSet());
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("test.GA.GF.I").isGatekeeperStatusSet());
				assertFalse(this.getAppInstanceContext().dataSegment.getDataObject("test.GA.GF.I").isPriorityFlagSet());
				assertFalse(this.getAppInstanceContext().dataSegment.getDataObject("test.GA.GF.I").isRepeatFlagSet());
				assertFalse(this.getAppInstanceContext().dataSegment.getDataObject("test.GA.GF.I").isShowFlagSet());
				
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("test.GA.GF").eventCount==1);
				System.out.println("***** Next Event");
				this.getAppInstanceContext().eventProcessor.process_event(this.createEvent("E", new Date()));
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("test.GA.GC.E").name.equalsIgnoreCase("test.GA.GC.E"));
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("test.GA.GC.E").eventCount==1);
				this.getAppInstanceContext().print_data_segment();

				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("test.GA.GC").eventCount==1);
				this.getAppInstanceContext().print_data_segment();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String getRule() {
		// TODO Auto-generated method stub
		return MetaSubGroupEventNoReward.metaData;
	}

}
