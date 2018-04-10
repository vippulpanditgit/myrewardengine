package com.myreward.parser.test;

import java.util.Date;
import java.util.UUID;

import org.antlr.v4.runtime.RecognitionException;

import com.myreward.engine.app.AppContext;
import com.myreward.engine.app.AppInstanceContext;
import com.myreward.engine.event.error.BuildException;
import com.myreward.engine.event.error.DebugException;
import com.myreward.engine.event.error.MetaDataParsingException;
import com.myreward.engine.event.error.ReferencedModelException;
import com.myreward.engine.event.processor.MetaOpCodeProcessor;
import com.myreward.engine.model.event.EventDO;
import com.myreward.parser.grammar.MyRewardParser;

public class test {

	public MetaOpCodeProcessor createMetaOpCodeProcessor(AppInstanceContext appInstanceContext, String rule) throws RecognitionException, MetaDataParsingException, BuildException, ReferencedModelException {
		MetaOpCodeProcessor metaOpCodeProcessor = new MetaOpCodeProcessor(appInstanceContext);
		metaOpCodeProcessor.initialize();
		metaOpCodeProcessor.parse(rule, false);
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
			String event_time_based = "package test event(A).between('1997-07-16T19:20:30.45+01:00','1997-07-16T19:20:30.45+01:00').reward(1) event(A).between('1997-07-16T19:20:30.45+01:00','1997-07-16T19:20:30.45+01:00').reward(10)";
			String event_between_different = "package test event(A).any(1).between('1997-07-16T19:20:30.45+01:00','2000-07-16T19:20:30.45+01:00').reward(1) event(A).any(1).between('2000-07-17T19:20:30.45+01:00','2017-07-16T19:20:30.45+01:00').reward(2)";
			String reward_metadata = "package global "
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
			String event_2_groups_triggered_by_same_event = "package test event(GA).any(1){event(A), event(B)} event(GAA).any(1){event(A), event(C)}";
			String event_2_groups_triggered_by_2_same_event = "package test event(GA).any(1){event(A), event(B)} event(GAA).any(1){event(A), event(B)} event(GC).any(3) {event(A), event(C)}";
			String pseudo = "package test event(GA).any(10) {event(B),event(GC).any(3){event(D), event(E)}, event(GF).any(1) {event(H), event(I)}}"
					+ " package test1 event(GA).any(10) {event(B),event(GC).any(3){event(D), event(E)}, event(GF).any(1) {event(H), event(I)}}";
			String pseudo1 = "package test event(GA).any(10) {event(B),event(GC).any(3){event(D), event(E)}, event(GF).any(1) {event(H), event(I)}}"
					+ " event(GH).any(1){event(GF), event(H)}";

			AppInstanceContext appInstanceContext = new AppInstanceContext();
            AppContext.getInstance().add("test_event_hash", 
            		new test().createMetaOpCodeProcessor(appInstanceContext, pseudo1));
            appInstanceContext.isDebug = true;
            appInstanceContext.actor = "vippul";
            appInstanceContext.uuid = UUID.randomUUID().toString();
            appInstanceContext.metaOpCodeProcessor =  AppContext.getInstance().get("test_event_hash");
            appInstanceContext.dataSegment = appInstanceContext.metaOpCodeProcessor.createDataSegment();
            appInstanceContext.eventProcessor = appInstanceContext.metaOpCodeProcessor.createEventProcessor(appInstanceContext.metaOpCodeProcessor.create_runtime_opcode_tree(),
            															appInstanceContext.dataSegment);
	    		EventDO eventDO = new EventDO();
	    		eventDO.setActivityName("E");
	    		eventDO.setActivityDate(new Date());
	    		processEvent(appInstanceContext, eventDO);
	   // 		processEvent(appInstanceContext, eventDO);
	   // 		processEvent(appInstanceContext, eventDO);

		} catch(Exception exp) {
			exp.printStackTrace();
		}

    }
	private static void processEvent(AppInstanceContext appInstanceContext, EventDO eventDO) throws Exception {
		appInstanceContext.print_data_segment();
		try {
			if(appInstanceContext.isInstanceReady())
				appInstanceContext.eventProcessor.process_event(eventDO);
		} catch(DebugException debugException) {
			appInstanceContext.eventProcessor.setMyRewardDataSegment(debugException.myRewardDataSegment);
			while(true) {
				try {
					int index = appInstanceContext.eventProcessor.step(debugException.opCodeIndex, debugException.eventDO);
					debugException.opCodeIndex = index;
					if(appInstanceContext.eventProcessor.getInstructionOpCodes().size()-1<=index)
						break;
				} catch(DebugException deepDebugException) {
					debugException.eventDO = deepDebugException.eventDO;
					debugException.myRewardDataSegment = deepDebugException.myRewardDataSegment;
					debugException.opCodeIndex = deepDebugException.opCodeIndex;
				}
			}
		}
//    			byte[] json = ObjectJsonSerializer.toJson(appInstanceContext.eventProcessor.getMyRewardDataSegment().getDataObject(66), null);
//    			System.out.println(new String(json));
			appInstanceContext.print_data_segment();
	}

}
