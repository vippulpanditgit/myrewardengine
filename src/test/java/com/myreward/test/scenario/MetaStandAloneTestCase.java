package com.myreward.test.scenario;

import static org.junit.Assert.*;

import java.util.Date;

import org.antlr.v4.runtime.RecognitionException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.myreward.engine.event.error.EventProcessingException;
import com.myreward.engine.event.error.MetaDataParsingException1;
import com.myreward.engine.event.processor.EventProcessor;
import com.myreward.engine.model.event.EventDO;
import com.myreward.parser.generator.MyRewardDataSegment;
import com.myreward.parser.test.BaseTestCase;
import com.myreward.parser.test.scenario.MetaSimpleGroupEvent;
import com.myreward.parser.test.scenario.MetaStandAloneEvent;

public class MetaStandAloneTestCase extends BaseTestCase {
	private EventProcessor eventProcessor;

	@Before
	public void setUp() throws Exception {
		super.setUp();
		getMetaOpCodeProcessor().parse(MetaStandAloneEvent.metaDataDuration, false);
        eventProcessor = new EventProcessor(getMetaOpCodeProcessor());
        eventProcessor.setMyRewardDataSegment(getMetaOpCodeProcessor().createDataSegment());
        eventProcessor.create_meta_tree();
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}
	@Test
	public void test() {
		try {
//			getMetaOpCodeProcessor().print_code_segment();
            eventProcessor.process_event(createEvent("A", new Date()));
//           eventProcessor.getMyRewardDataSegment().printString();
//            assertTrue(eventProcessor.getMyRewardDataSegment().getDataObject("A".hashCode()).eventCount.compareTo(Integer.valueOf(1))==1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
