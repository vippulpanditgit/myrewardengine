package com.myreward.engine.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import com.myreward.engine.grammar.MyRewardLexer;
import com.myreward.engine.grammar.MyRewardParser;

public class MyRewardParserUtil {
	public static MyRewardParser getParsed(String eventString) throws IOException {
		InputStream stream = new ByteArrayInputStream(eventString.getBytes(StandardCharsets.UTF_8));
		
		MyRewardLexer myRewardLexer = new MyRewardLexer(CharStreams.fromStream(stream, StandardCharsets.UTF_8));
        MyRewardParser myRewardParser = new MyRewardParser(new CommonTokenStream(myRewardLexer));
        return myRewardParser;

	}

}
