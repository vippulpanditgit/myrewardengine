package com.myreward.test.scenario;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.UUID;

import org.antlr.v4.runtime.RecognitionException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.myreward.engine.app.AppContext;
import com.myreward.engine.app.AppInstanceContext;
import com.myreward.engine.event.error.MetaDataParsingException;
import com.myreward.engine.event.processor.MetaOpCodeProcessor;
import com.myreward.parser.test.BaseTestCase;
import com.myreward.parser.test.test;
import com.myreward.parser.test.scenario.MetaSameEventMultipleGroupsNoReward;
import com.myreward.parser.test.scenario.MetaSubGroupEventNoReward;

public class MetaSameEventMultipleGroupsNoRewqrdTestCase extends BaseTestCase {
	
	public MetaOpCodeProcessor createMetaOpCodeProcessor(AppInstanceContext appInstanceContext, String rule) throws RecognitionException, MetaDataParsingException {
		MetaOpCodeProcessor metaOpCodeProcessor = new MetaOpCodeProcessor(appInstanceContext);
		metaOpCodeProcessor.initialize();
		assertNotNull(metaOpCodeProcessor);
		metaOpCodeProcessor.parse(rule, false);
		metaOpCodeProcessor.print_code_segment();
		return metaOpCodeProcessor;
	}

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
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("test.GC.A").name.equalsIgnoreCase("test.GC.A"));
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("test.GAA.A").eventCount==1);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("test.GA.A").eventCount==1);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("test.GC.A").eventCount==1);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("test.GA").eventCount==1);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("test.GAA").eventCount==1);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("test.GC.A").getReward()==0.0);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("test.GA").isEventCompletionFlagSet());
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("test.GAA").isEventCompletionFlagSet());
				assertFalse(this.getAppInstanceContext().dataSegment.getDataObject("test.GC").isEventCompletionFlagSet());
				System.out.println("***** Next Event");
				this.getAppInstanceContext().eventProcessor.process_event(this.createEvent("C", new Date()));
				this.getAppInstanceContext().print_data_segment();
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("test.GAA.A").eventCount==1);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("test.GA.A").eventCount==1);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("test.GC.A").eventCount==1);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("test.GA").eventCount==1);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("test.GAA").eventCount==1);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("test.GC.C").eventCount==1);
				System.out.println("***** Next Event");
				this.getAppInstanceContext().eventProcessor.process_event(this.createEvent("C", new Date()));
				this.getAppInstanceContext().print_data_segment();
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("test.GAA.A").eventCount==1);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("test.GA.A").eventCount==1);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("test.GC.A").eventCount==1);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("test.GA").eventCount==1);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("test.GAA").eventCount==1);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("test.GC").eventCount==3);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("test.GC.C").eventCount==2);
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("test.GA").isEventCompletionFlagSet());
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("test.GAA").isEventCompletionFlagSet());
				assertTrue(this.getAppInstanceContext().dataSegment.getDataObject("test.GC").isEventCompletionFlagSet());

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
