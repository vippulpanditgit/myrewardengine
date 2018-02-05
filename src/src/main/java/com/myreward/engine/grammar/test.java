package com.myreward.engine.grammar;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.antlr.v4.runtime.*;

import com.myreward.engine.grammar.visitor.MyRewardVisitor;

public class test {

	public static void main(String[] args) {
		try {
//			InputStream stream = new ByteArrayInputStream("BIOMETRIC.ALLSOURCES.HEALTHFAIR any(1) {(FILE.OPERATIONS.BIOMETRIC_COMPLETION any(1) from [start_date, end_date] repeat WEEKLY|MONTHLY|YEARLY) or FILE.HOKIT.OFFLINEHEALTHFAIREVENT or FILE.BIO.OFFLINEHEALTHFAIREVENT or FILE.HFAIR.OFFLINEHEALTHFAIREVENT or FILE.MDFRM.OFFLINEHEALTHFAIREVENT} from [start_date, end_date]".getBytes(StandardCharsets.UTF_8));
//			InputStream stream = new ByteArrayInputStream("event(BIOMETRIC.ALLSOURCES.HEALTHFAIR) any".getBytes(StandardCharsets.UTF_8));
			String oneEvent = "event(BIOMETRIC.ALLSOURCES.HEALTHFAIR).reward(1).between('1997-07-16T19:20:30.45+01:00','1997-07-16T19:20:30.45+01:00').repeat(MONTHLY,2).show(true).priority(1)";
			String subEvent = "event(BIOMETRIC.ALLSOURCES.HEALTHFAIR).reward(1).between('1997-07-16T19:20:30.45+01:00','1997-07-16T19:20:30.45+01:00').repeat(MONTHLY,2).show(true).priority(1).any(1){event(FILE.MDFRM.OFFLINEHEALTHFAIREVENT).repeat(ACTIVITY_DATE, 1)}";
			String subEventsOr = "event(BIOMETRIC.ALLSOURCES.HEALTHFAIR).reward(1).between('1997-07-16T19:20:30.45+01:00','1997-07-16T19:20:30.45+01:00').repeat(MONTHLY,2).show(true).priority(1).any(1){event(FILE.MDFRM.OFFLINEHEALTHFAIREVENT).repeat(ACTIVITY_DATE, 1)" + 
					"||event(FILE.HFAIR.OFFLINEHEALTHFAIREVENT).repeat(ACTIVITY_DATE, 1)" + 
					"||event(FILE.BIO.OFFLINEHEALTHFAIREVENT).repeat(ACTIVITY_DATE, 1)" + 
					"||event(FILE.HOKIT.OFFLINEHEALTHFAIREVENT).repeat(ACTIVITY_DATE, 1)}";
			InputStream stream = new ByteArrayInputStream(subEventsOr.getBytes(StandardCharsets.UTF_8));
			
			MyRewardLexer myRewardLexer = new MyRewardLexer(CharStreams.fromStream(stream, StandardCharsets.UTF_8));
	        MyRewardParser myRewardParser = new MyRewardParser(new CommonTokenStream(myRewardLexer));
	        
	        
            MyRewardParser.Myreward_defContext fileContext = myRewardParser.myreward_def(); 
            MyRewardVisitor visitor = new MyRewardVisitor();
            visitor.setSymbolTable(myRewardParser.getSymbolTable());
             visitor.visit(fileContext);   
		} catch(Exception exp) {
			exp.printStackTrace();
		}
/*
		try {
			InputStream stream = new ByteArrayInputStream("{\"name\":\"john\",\"description\":\"This is a test\"}".getBytes(StandardCharsets.UTF_8));
	
			MyRewardLexer myRewardLexer = new MyRewardLexer(CharStreams.fromStream(stream, StandardCharsets.UTF_8));
	        MyRewardParser myRewardParser = new MyRewardParser(new CommonTokenStream(myRewardLexer));

            MyRewardParser.Standalone_eventContext fileContext = myRewardParser.standalone_event();                
            MyRewardVisitor visitor = new MyRewardVisitor(System.out);                
            visitor.visit(fileContext);   
		} catch(Exception exp) {
			exp.printStackTrace();
		}
*/
    }

}
