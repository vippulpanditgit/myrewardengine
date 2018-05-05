package com.myreward.parser.test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.antlr.v4.runtime.RecognitionException;
import org.apache.commons.lang3.time.DateUtils;

import com.myreward.engine.app.AppContext;
import com.myreward.engine.app.AppInstanceContext;
import com.myreward.engine.app.User;
import com.myreward.engine.event.error.BuildException;
import com.myreward.engine.event.error.DebugException;
import com.myreward.engine.event.error.MetaDataParsingException;
import com.myreward.engine.event.error.ReferencedModelException;
import com.myreward.engine.event.listener.PrintEventProcessorListener;
import com.myreward.engine.event.opcode.processing.OpCodeBaseModel;
import com.myreward.engine.event.processor.MetaOpCodeProcessor;
import com.myreward.engine.model.event.EventDO;
import com.myreward.parser.grammar.MyRewardParser;
import com.myreward.parser.symbol.MyRewardSymbolTable;
import com.myreward.parser.util.EventProcessingUtil;
import com.myreward.parser.util.FileProcessingUtil;

public class test {

	public MetaOpCodeProcessor createMetaOpCodeProcessor(AppInstanceContext appInstanceContext, String rule) throws RecognitionException, MetaDataParsingException, BuildException, ReferencedModelException {
		MetaOpCodeProcessor metaOpCodeProcessor = new MetaOpCodeProcessor(appInstanceContext);
		metaOpCodeProcessor.initialize();
		metaOpCodeProcessor.parse(rule, false);
		appInstanceContext.symbolTable.print_symbol_table();
		metaOpCodeProcessor.print_code_segment();
		return metaOpCodeProcessor;
	}
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
			String pseudo_2_groups = "package test event(GA).any(1) {event(B), event(C)}.reward(1) event(GD).any(1){event(E), event(F)}.reward(1)";
			String pseudo2 = "package test event(GA).any(1) {event(B).any(1), event(C)}.between('1997-07-16T19:20:30.45+01:00','1997-07-16T19:20:30.45+01:00').reward(100,1000)";
			String pseudo_standalone = "package test event(A).any(1).reward(1).repeat(WEEKLY,2)";
			String pseudo_between_repeat = "package test event(A).any(1).between('1997-07-16T19:20:30.45+01:00','1997-07-16T19:20:30.45+01:00').reward(1).repeat(WEEKLY,2)";
			String pseudo_between = "package test event(A).any(1).between('1997-07-16T19:20:30.45+01:00','1997-07-16T19:20:30.45+01:00')";
			String pseudo_gatekeeper = "package test event(GA).any(1) {event(B), event(C)}.show(true).priority(1) event(GD).any(1){event(E), event(F)}.gatekeeper(event(GA))";
			String pseudo_standalone_gatekeeper = "package test event(A).any(1).show(true).priority(1) event(B).gatekeeper(event(A)).reward(100)";
			String pseudo_group = "package test event(GA).any(1) {event(B), event(C)}.reward(1)";
			String pseudo_group_repeat = "package test event(GA).any(1) {event(B), event(C)}.reward(1).repeat(WEEKLY,2)";
			
			AppInstanceContext appInstanceContext = new AppInstanceContext();
			String ruleFileName = "src/main/resources/test/MetaStandAloneWithShowPriorityDurationGatekeeper";
			String hashValue = String.valueOf(ruleFileName.hashCode());
            AppContext.getInstance().add(hashValue, 
            		new test().createMetaOpCodeProcessor(appInstanceContext, FileProcessingUtil.readFile(ruleFileName)));
            appInstanceContext.isDebug = true;
            appInstanceContext.actor = new User();
            appInstanceContext.uuid = UUID.randomUUID().toString();
            appInstanceContext.metaOpCodeProcessor =  AppContext.getInstance().get(hashValue);
            if(appInstanceContext.metaOpCodeProcessor!=null) {
	            appInstanceContext.dataSegment = appInstanceContext.metaOpCodeProcessor.createDataSegment();
	            List<OpCodeBaseModel> runtime_opcode_tree = appInstanceContext.metaOpCodeProcessor.create_runtime_opcode_tree();
	            runtime_opcode_tree.stream().forEach(model -> model.registerProcessingListener(new PrintEventProcessorListener()));
	            appInstanceContext.eventProcessor = appInstanceContext.metaOpCodeProcessor.createEventProcessor(runtime_opcode_tree,
	            															appInstanceContext.dataSegment);

	            EventDO eventDO = new EventDO();
		    	eventDO.setActivityName("A");
		    	eventDO.setActivityDate(DateUtils.addDays(new Date(), -2));
		    	appInstanceContext.eventSegmentsScenario.put("test", eventDO);
		    	eventDO = new EventDO();
		    	eventDO.setActivityName("A");
		    	eventDO.setActivityDate(DateUtils.addDays(new Date(), -1));
		    	appInstanceContext.eventSegmentsScenario.put("test", eventDO);
		    	System.out.println("**** "+eventDO.getActivityName());
		    	appInstanceContext.eventSegmentsScenario.process("test");
//		    	appInstanceContext.stackSegment.add(eventDO);
//		    	EventProcessingUtil.processEvent(appInstanceContext, eventDO);
/*		    	eventDO.setActivityName("A");
		    	eventDO.setActivityDate(DateUtils.addDays(new Date(), -1));
		    	appInstanceContext.eventSegmentsScenario.put(eventDO);
		    	System.out.println("**** "+eventDO.getActivityName());
		    	appInstanceContext.stackSegment.add(eventDO);
		    	EventProcessingUtil.processEvent(appInstanceContext, eventDO);
		    	eventDO.setActivityName("KB");
		    	eventDO.setActivityDate(new Date());
		    	appInstanceContext.eventSegmentsScenario.put(eventDO);
		    	System.out.println("**** "+eventDO.getActivityName());
		    	appInstanceContext.stackSegment.add(eventDO);
		    		
		    	EventProcessingUtil.processEvent(appInstanceContext, eventDO);
*/            } else {
            	System.out.println("Hash value not found! "+hashValue);
            }
		} catch(Exception exp) {
			exp.printStackTrace();
		}

    }

}
