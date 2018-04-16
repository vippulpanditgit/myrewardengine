package com.myreward.test.scenario;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.myreward.parser.test.BaseTestCase;
import com.myreward.parser.test.scenario.MetaSameEventMultipleGroupsNoReward;

public class MetaSameEventMultipleGroupsNoRewqrdTestCase extends BaseTestCase {
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
				this.getAppInstanceContext().print_data_segment();
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaSameEventMultipleGroupsNoReward.GC.A").name.equalsIgnoreCase("MetaSameEventMultipleGroupsNoReward.GC.A"));
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaSameEventMultipleGroupsNoReward.GAA.A").eventCount==1);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaSameEventMultipleGroupsNoReward.GA.A").eventCount==1);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaSameEventMultipleGroupsNoReward.GC.A").eventCount==1);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaSameEventMultipleGroupsNoReward.GA").eventCount==1);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaSameEventMultipleGroupsNoReward.GAA").eventCount==1);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaSameEventMultipleGroupsNoReward.GC.A").getReward()==0.0);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaSameEventMultipleGroupsNoReward.GA").isEventCompletionFlagSet());
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaSameEventMultipleGroupsNoReward.GAA").isEventCompletionFlagSet());
				assertFalse(this.getAppInstanceContext().dataSegment.getDataObject("MetaSameEventMultipleGroupsNoReward.GC").isEventCompletionFlagSet());
				System.out.println("***** Next Event");
				this.getAppInstanceContext().eventProcessor.process_event(this.createEvent("C", new Date()));
				this.getAppInstanceContext().print_data_segment();
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaSameEventMultipleGroupsNoReward.GAA.A").eventCount==1);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaSameEventMultipleGroupsNoReward.GA.A").eventCount==1);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaSameEventMultipleGroupsNoReward.GC.A").eventCount==1);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaSameEventMultipleGroupsNoReward.GA").eventCount==1);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaSameEventMultipleGroupsNoReward.GAA").eventCount==1);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaSameEventMultipleGroupsNoReward.GC.C").eventCount==1);
				System.out.println("***** Next Event");
				this.getAppInstanceContext().eventProcessor.process_event(this.createEvent("C", new Date()));
				this.getAppInstanceContext().print_data_segment();
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaSameEventMultipleGroupsNoReward.GAA.A").eventCount==1);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaSameEventMultipleGroupsNoReward.GA.A").eventCount==1);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaSameEventMultipleGroupsNoReward.GC.A").eventCount==1);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaSameEventMultipleGroupsNoReward.GA").eventCount==1);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaSameEventMultipleGroupsNoReward.GAA").eventCount==1);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaSameEventMultipleGroupsNoReward.GC").eventCount==3);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaSameEventMultipleGroupsNoReward.GC.C").eventCount==2);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaSameEventMultipleGroupsNoReward.GA").isEventCompletionFlagSet());
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaSameEventMultipleGroupsNoReward.GAA").isEventCompletionFlagSet());
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("MetaSameEventMultipleGroupsNoReward.GC").isEventCompletionFlagSet());

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String getRule() {
		// TODO Auto-generated method stub
		return MetaSameEventMultipleGroupsNoReward.metaData;
	}

}
