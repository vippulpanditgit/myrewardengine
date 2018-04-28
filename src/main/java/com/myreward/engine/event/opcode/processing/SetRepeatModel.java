package com.myreward.engine.event.opcode.processing;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.myreward.engine.model.event.EventDO;
import com.myreward.engine.model.event.OperationResultDO;
import com.myreward.engine.model.event.StatementOperationResult;
import com.myreward.parser.generator.MyRewardDataSegment;
import com.myreward.parser.generator.MyRewardDataSegment.EventDataObject;
import com.myreward.parser.metamodel.RepeatMetaModel.RepeatCriteria;


public class SetRepeatModel extends SetBaseModel {
	public enum StoreRepeatType {
		FLAG,
		AMOUNT,
		TYPE,
		DATE
	}
	private static String OPCODE_LABEL_FLAG = "set_rpt_flg";
	private static String OPCODE_LABEL_AMOUNT = "set_rpt_aft";
	private static String OPCODE_LABEL_TYPE = "set_rpt_typ";
	private static String OPCODE_LABEL_DATE = "set_rpt_dt";

	private static String OPCODE_OPERAND_START = "(";
	private static String OPCODE_OPERAND_END = ")";
	private static String OPERAND_FORMAT_PATTERN = ",";
	private StoreRepeatType type;
	private String name;
	private String amount;
	private String after;
	private String date;
	public static String[] OPCODE_HANDLER = {OPCODE_LABEL_FLAG, OPCODE_LABEL_AMOUNT, OPCODE_LABEL_TYPE, OPCODE_LABEL_DATE};

	public SetRepeatModel() {
	}

	public SetRepeatModel(String statement) {
		StoreRepeatType storePriorityType = getType(statement);
		if(storePriorityType!=null) {
			type = storePriorityType;
			if(type==StoreRepeatType.AMOUNT) {
				String[] amountOperand = this.parse(OPCODE_LABEL_AMOUNT, OPERAND_FORMAT_PATTERN, statement);
				name = amountOperand[0];
				amount = amountOperand[1];
			} else if(type==StoreRepeatType.FLAG) {
				String[] amountOperand = this.parse(OPCODE_LABEL_FLAG, null, statement);
				name = amountOperand[0];
			} else if(type==StoreRepeatType.TYPE) {
				String[] amountOperand = this.parse(OPCODE_LABEL_TYPE, OPERAND_FORMAT_PATTERN, statement);
				name = amountOperand[0];
				after = amountOperand[1];
			} else if(type==StoreRepeatType.DATE) {
				String[] amountOperand = this.parse(OPCODE_LABEL_DATE, null, statement);
				name = amountOperand[0];
			}
		}
	}
	public StoreRepeatType getType(String opcode) {
		if(StringUtils.startsWith(opcode, OPCODE_LABEL_FLAG))
			return StoreRepeatType.FLAG;
		if(StringUtils.startsWith(opcode, OPCODE_LABEL_AMOUNT))
			return StoreRepeatType.AMOUNT;
		if(StringUtils.startsWith(opcode, OPCODE_LABEL_TYPE))
			return StoreRepeatType.TYPE;
		if(StringUtils.startsWith(opcode, OPCODE_LABEL_DATE))
			return StoreRepeatType.DATE;
		return null;
	}
	public String[] parse(String opcodeLabelFlag, String operandSeparator, String value) {
		if(!StringUtils.startsWith(value, opcodeLabelFlag))
			return null;
		String operandValue = StringUtils.substringBetween(value, OPCODE_OPERAND_START, OPCODE_OPERAND_END);
		if(operandSeparator!=null) {
			Pattern pattern = Pattern.compile(operandSeparator);
			String[] functionCallParameter = pattern.split(operandValue);
			return functionCallParameter;
		} else {
			return new String[]{operandValue};
		}
	}
	@Override
	public String[] getOpcodes() {
		return OPCODE_HANDLER;
	}
	public String toString() {
		String value = "";
		
		switch(type) {
			case FLAG:
				value= (OPCODE_LABEL_FLAG+OPCODE_OPERAND_START+name+OPCODE_OPERAND_END);
				break;
			case TYPE:
				value = (OPCODE_LABEL_TYPE+OPCODE_OPERAND_START+name+OPERAND_FORMAT_PATTERN+after+OPCODE_OPERAND_END);
				break;
			case AMOUNT:
				value = (OPCODE_LABEL_AMOUNT+OPCODE_OPERAND_START+name+OPERAND_FORMAT_PATTERN+amount+OPCODE_OPERAND_END);
				break;
			case DATE:
				value = (OPCODE_LABEL_DATE+OPCODE_OPERAND_START+name+OPERAND_FORMAT_PATTERN+date+OPCODE_OPERAND_END);
				break;
		}
		return value;
	}

	@Override
	public OperationResultDO process(List<OpCodeBaseModel> instructionOpCodes, MyRewardDataSegment myRewardDataSegment,
			EventDO eventDO) {
		OperationResultDO operationResultDO = new StatementOperationResult();;
		EventDataObject eventDataObject = myRewardDataSegment.search(name);
		if(type==StoreRepeatType.FLAG) {
			if(eventDataObject!=null) {
				eventDataObject.setRepeatFlag();
				operationResultDO.setResult(true);
				return operationResultDO;
			}
			operationResultDO.setResult(false);
		} else if(type==StoreRepeatType.TYPE){
			if(eventDataObject!=null) {
				RepeatCriteria repeatCriteria = RepeatCriteria.ACTIVITY_DATE;
				if(after.equalsIgnoreCase("0"))
					repeatCriteria = RepeatCriteria.WEEKLY;
				if(after.equalsIgnoreCase("3"))
					repeatCriteria = RepeatCriteria.ACTIVITY_DATE;
				if(after.equalsIgnoreCase("1"))
					repeatCriteria = RepeatCriteria.MONTHLY;
				if(after.equalsIgnoreCase("2"))
					repeatCriteria = RepeatCriteria.YEARLY;
				
				eventDataObject.setRepeatCriteria(repeatCriteria);
				operationResultDO.setResult(true);
				return operationResultDO;
			}

		} else if(type==StoreRepeatType.AMOUNT){
			if(eventDataObject!=null) {
				eventDataObject.setRepeatAfter(Integer.valueOf(amount));
				operationResultDO.setResult(true);
				return operationResultDO;
			}
		} else if(type==StoreRepeatType.DATE){
			Date activityDate = eventDO.getActivityDate();
			if(activityDate==null)
				activityDate = new Date();
			if(amount==null)
				amount = String.valueOf(eventDataObject.repeatAfter);
			if(eventDataObject.getRepeatCriteria()==RepeatCriteria.WEEKLY) {
				eventDataObject.nextRepeat = DateUtils.addWeeks(activityDate, Integer.valueOf(amount));
			} else if(eventDataObject.getRepeatCriteria()==RepeatCriteria.ACTIVITY_DATE) {
				eventDataObject.nextRepeat = DateUtils.addDays(activityDate, Integer.valueOf(amount));
			} else if(eventDataObject.getRepeatCriteria()==RepeatCriteria.MONTHLY) {
				eventDataObject.nextRepeat = DateUtils.addMonths(activityDate, Integer.valueOf(amount));
			} else if(eventDataObject.getRepeatCriteria()==RepeatCriteria.YEARLY) {
				eventDataObject.nextRepeat = DateUtils.addYears(activityDate, Integer.valueOf(amount));				
			}  
		}
		return operationResultDO;
	}

}
