package com.myreward.test.scenario;

import static org.junit.Assert.*;

import org.antlr.v4.runtime.RecognitionException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.myreward.engine.event.error.MetaDataParsingException1;
import com.myreward.parser.test.BaseTestCase;
import com.myreward.parser.test.scenario.MetaSimpleGroupEvent;

public class MetaSimpleGroupEventTestCase extends BaseTestCase {

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void test() {
		try {
			getMetaOpCodeProcessor().parse(MetaSimpleGroupEvent.metaData, false);
			getMetaOpCodeProcessor().print_code_segment();
		} catch (RecognitionException | MetaDataParsingException1 e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
