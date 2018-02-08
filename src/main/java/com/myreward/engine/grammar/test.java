package com.myreward.engine.grammar;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.antlr.v4.runtime.*;

import com.myreward.engine.grammar.MyRewardParser.Myreward_defsContext;
import com.myreward.engine.grammar.visitor.MyRewardVisitor;
import com.myreward.engine.util.MyRewardParserUtil;

public class test {

	public static void main(String[] args) {
		try {
//			InputStream stream = new ByteArrayInputStream("BIOMETRIC.ALLSOURCES.HEALTHFAIR any(1) {(FILE.OPERATIONS.BIOMETRIC_COMPLETION any(1) from [start_date, end_date] repeat WEEKLY|MONTHLY|YEARLY) or FILE.HOKIT.OFFLINEHEALTHFAIREVENT or FILE.BIO.OFFLINEHEALTHFAIREVENT or FILE.HFAIR.OFFLINEHEALTHFAIREVENT or FILE.MDFRM.OFFLINEHEALTHFAIREVENT} from [start_date, end_date]".getBytes(StandardCharsets.UTF_8));
//			InputStream stream = new ByteArrayInputStream("event(BIOMETRIC.ALLSOURCES.HEALTHFAIR) any".getBytes(StandardCharsets.UTF_8));
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
			MyRewardParser myRewardParser = MyRewardParserUtil.getParsed(subEventWithSubEvent);
	        
            Myreward_defsContext fileContext = myRewardParser.myreward_defs(); 
            MyRewardVisitor visitor = new MyRewardVisitor();
            visitor.setSymbolTable(myRewardParser.getSymbolTable());
             visitor.visit(fileContext);   
             System.out.println(visitor.groupMetaModelList);
             System.out.println(visitor.getSymbolTable());

		} catch(Exception exp) {
			exp.printStackTrace();
		}

    }

}
