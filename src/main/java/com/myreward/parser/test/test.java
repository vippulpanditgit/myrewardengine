package com.myreward.parser.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.antlr.v4.runtime.*;

import com.myreward.engine.event.processor.EventProcessor;
import com.myreward.engine.event.processor.MetaOpCodeProcessor;
import com.myreward.parser.generator.MyRewardDataSegment;
import com.myreward.parser.generator.MyRewardFunctionXRef;
import com.myreward.parser.generator.MyRewardPCodeGenerator;
import com.myreward.parser.grammar.MyRewardParser;
import com.myreward.parser.grammar.MyRewardParser.Myreward_defsContext;
import com.myreward.parser.grammar.visitor.MyRewardVisitor;
import com.myreward.parser.symbol.SymbolProcessingEngine;
import com.myreward.parser.util.MyRewardParserUtil;
import com.myreward.parser.util.RuntimeLib;

public class test {

	public static void main(String[] args) {
		try {
			String oneEventWithGatekeeper = "package myclient event(BIOMETRIC.ALLSOURCES.HEALTHFAIR).reward(1).between('1997-07-16T19:20:30.45+01:00','1997-07-16T19:20:30.45+01:00').repeat(MONTHLY,2).show(true).priority(1).gatekeeper(event(FILE.MDFRM.OFFLINEHEALTHFAIREVENT))";
			String oneEvent = "package myclient event(BIOMETRIC.ALLSOURCES.HEALTHFAIR).reward(1).between('1997-07-16T19:20:30.45+01:00','1997-07-16T19:20:30.45+01:00').repeat(MONTHLY,2).show(true).priority(1)";
			String subEvent = "package myclient event(BIOMETRIC.ALLSOURCES.HEALTHFAIR).reward(1).between('1997-07-16T19:20:30.45+01:00','1997-07-16T19:20:30.45+01:00').repeat(MONTHLY,2).show(true).priority(1).any(1){event(FILE.MDFRM.OFFLINEHEALTHFAIREVENT).repeat(ACTIVITY_DATE, 1)}";
			String subEvents2Or = "package myclient event(BIOMETRIC.ALLSOURCES.HEALTHFAIR).reward(1).between('1997-07-16T19:20:30.45+01:00','1997-07-16T19:20:30.45+01:00').repeat(MONTHLY,2).show(true).priority(1).any(1){event(FILE.MDFRM.OFFLINEHEALTHFAIREVENT).repeat(ACTIVITY_DATE, 1)" + 
					",event(FILE.HOKIT.OFFLINEHEALTHFAIREVENT).repeat(ACTIVITY_DATE, 1)}";
			String subEvents4Or = "package myclient event(BIOMETRIC.ALLSOURCES.HEALTHFAIR).reward(1).between('1997-07-16T19:20:30.45+01:00','1997-07-16T19:20:30.45+01:00').repeat(MONTHLY,2).show(true).priority(1).any(1){event(FILE.MDFRM.OFFLINEHEALTHFAIREVENT).repeat(ACTIVITY_DATE, 1)" + 
					",event(FILE.HFAIR.OFFLINEHEALTHFAIREVENT).repeat(ACTIVITY_DATE, 1)" + 
					",event(FILE.BIO.OFFLINEHEALTHFAIREVENT).repeat(ACTIVITY_DATE, 1)" + 
					",event(FILE.HOKIT.OFFLINEHEALTHFAIREVENT).repeat(ACTIVITY_DATE, 1)}";
			String subEvents2 = "package myclient event(BIOMETRIC.ALLSOURCES.HEALTHFAIR).reward(1).between('1997-07-16T19:20:30.45+01:00','1997-07-16T19:20:30.45+01:00').repeat(MONTHLY,2).show(true).priority(1).any(1){event(FILE.MDFRM.OFFLINEHEALTHFAIREVENT).repeat(ACTIVITY_DATE,1),event(FILE.HFAIR.OFFLINEHEALTHFAIREVENT).repeat(ACTIVITY_DATE,1),event(FILE.BIO.OFFLINEHEALTHFAIREVENT).repeat(ACTIVITY_DATE,1),event(FILE.HOKIT.OFFLINEHEALTHFAIREVENT).repeat(ACTIVITY_DATE, 1)}"
					+ " event(INCENTIVES.GROUP.PHYSICAL).reward(1).repeat(MONTHLY,2).show(true).priority(1).any(1){event(WELLNESS.EVISOR.PHYSICAL.PREADJ).repeat(ACTIVITY_DATE,1),event(WELLNESS.EVISOR.PHYSICAL.ADJ).repeat(ACTIVITY_DATE,1),event(WELLNESS.FILE.PHYSICAL).repeat(ACTIVITY_DATE,1),event(FILE.OPERATIONS.SCREENING.PHYSICAL).repeat(ACTIVITY_DATE, 1)}";
			String subEventWithSubEvent = "package global "
					+ "event(WELLNESS.GROUP.TWC.EVISOR).any(1) {"
						+ "event(WELLNESS.GROUP.TWC_DIABETESLIFESTYLE),"
						+ "event(WELLNESS.GROUP.TWC_WEIGHTMANAGEMENT).any(1) {"
							+ "event(WELLNESS.GROUP.TWC_DIABETESLIFESTYLE2).any(1) {"
								+ "event(WELLNESS.EVISOR_TWC_ICUE.DIABETESLIFESTYLE)"
							+ "},"
							+ "event(WELLNESS.GROUP.TWC_NUTRITION).any(1) {"
								+ "event(WELLNESS.EVISOR_TWC_ICUE.NUTRITION)"
							+ "}"
						+ "}"
					+ "}";
			String subEventWithSubEvent2 = "package myclient "
										+ "import global "
										+ "event(WELLNESS.GROUP.TWC.EVISOR).any(1)"
											+ "{event(WELLNESS.GROUP.TWC_DIABETESLIFESTYLE),"
											+ "event(WELLNESS.GROUP.TWC_WEIGHTMANAGEMENT).any(1)"
												+ "{event(WELLNESS.GROUP.TWC_DIABETESLIFESTYLE).any(1)"
													+ "{event(WELLNESS.EVISOR_TWC_ICUE.DIABETESLIFESTYLE),"
													+ "event(WELLNESS.EVISOR_TWC.DIABETESLIFESTYLE),"
													+ "event(FILE.EVISOR_EXCEPT.DIABETESLIFESTYLE),"
													+ "event(WELLNESS.EVISOR_TWC_ICUE.DIABETESLIFESTYLE.MAIL),"
													+ "event(FILE.OPERATIONS.TWC_DIABETESLIFESTYLE)},"
												+ "event(WELLNESS.GROUP.TWC_NUTRITION).any(1){"
													+ "event(WELLNESS.EVISOR_TWC_ICUE.NUTRITION),"
													+ "event(WELLNESS.EVISOR_TWC.NUTRITION),"
													+ "event(WELLNESS.EVISOR_TWC_ICUE.NUTRITION.MAIL)"
												+ "}"
											+ "}"
										+ "}";
			String pseudo = "package test event(GA).any(10) {event(B),event(GC).any(3){event(D), event(E)}, event(GF).any(1) {event(H), event(I)}}";
			String pseudo_2_groups = "package test event(GA).any(1) {event(B), event(C)}.reward(1) event(GD).any(1){event(E), event(F)}.reward(1)";
			String pseudo2 = "package test event(GA).any(1) {event(B).any(1), event(C)}.between('1997-07-16T19:20:30.45+01:00','1997-07-16T19:20:30.45+01:00').reward(100,1000)";
			String pseudo_standalone = "package test event(A).any(1).reward(1).repeat(WEEKLY,2)";
			String pseudo_between_repeat = "package test event(A).any(1).between('1997-07-16T19:20:30.45+01:00','1997-07-16T19:20:30.45+01:00').reward(1).repeat(WEEKLY,2)";
			String pseudo_between = "package test event(A).any(1).between('1997-07-16T19:20:30.45+01:00','1997-07-16T19:20:30.45+01:00')";
			String pseudo_gatekeeper = "package test event(GA).any(1) {event(B), event(C)}.show(true).priority(1) event(GD).any(1){event(E), event(F)}.gatekeeper(event(GA))";
			String pseudo_standalone_gatekeeper = "package test event(A).any(1).show(true).priority(1) event(B).gatekeeper(event(A)).reward(100)";
			String pseudo_group = "package test event(GA).any(1) {event(B), event(C)}.reward(1)";
			String pseudo_group_repeat = "package test event(GA).any(1) {event(B), event(C)}.reward(1).repeat(WEEKLY,2)";
			String event_time_based = "package test event(A).between('1997-07-16T19:20:30.45+01:00','1997-07-16T19:20:30.45+01:00').reward(1) event(A).between('1997-07-16T19:20:30.45+01:00','1997-07-16T19:20:30.45+01:00').reward(10)";
			String event_between_different = "package test event(A).any(1).between('1997-07-16T19:20:30.45+01:00','2000-07-16T19:20:30.45+01:00').reward(1) event(A).any(1).between('2000-07-17T19:20:30.45+01:00','2017-07-16T19:20:30.45+01:00').reward(2)";
			String testEvent = "package global "
					+ "event(GA).any(2){"
						+ "event(B),"
						+ "event(GC).any(1){"
							+ "event(GD).any(1){"
								+ "event(E)"
							+ "},"
							+ "event(GF).any(1){"
								+ "event(H).reward(10,100)"
							+ "}"
						+ "}"
					+ "}.reward(100,1000)";

			
			String oneEvent1 = "package global event(H).between('2000-07-17T19:20:30.45+01:00','2018-07-16T19:20:30.45+01:00').reward(10,100).repeat(WEEKLY,2).show(true).priority(1).gatekeeper(event(B))";
			MetaOpCodeProcessor metaOpCodeProcessor = new MetaOpCodeProcessor();
            EventProcessor eventProcessor = new EventProcessor(metaOpCodeProcessor);
			metaOpCodeProcessor.initialize();
			metaOpCodeProcessor.parse(testEvent);
            MyRewardDataSegment myRewardDataSegment = eventProcessor.createDataSegment();
            eventProcessor.setMyRewardDataSegment(myRewardDataSegment);
            
            eventProcessor.preprocess();
            if(!eventProcessor.run()) {
            		System.out.println("This is not working.");
            }
            
            System.out.println("Test "+eventProcessor.getInstructionOpCodes().size());
 		} catch(Exception exp) {
			exp.printStackTrace();
		}

    }

}
