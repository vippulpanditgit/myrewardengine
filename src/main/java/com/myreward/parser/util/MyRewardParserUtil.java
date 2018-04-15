package com.myreward.parser.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import com.myreward.engine.event.processor.MetaOpCodeProcessor;
import com.myreward.parser.grammar.MyRewardLexer;
import com.myreward.parser.grammar.MyRewardParser;

public class MyRewardParserUtil {
	public static MyRewardParser getParsed(MetaOpCodeProcessor metaOpCodeProcessor, String eventString) throws IOException {
		InputStream stream = new ByteArrayInputStream(eventString.getBytes(StandardCharsets.UTF_8));
		
		MyRewardLexer myRewardLexer = new MyRewardLexer(CharStreams.fromStream(stream, StandardCharsets.UTF_8));
        MyRewardParser myRewardParser = new MyRewardParser(new CommonTokenStream(myRewardLexer));
        myRewardParser.metaOpCodeProcessor = metaOpCodeProcessor;
        return myRewardParser;

	}

}
