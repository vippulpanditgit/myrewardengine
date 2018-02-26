package com.myreward.engine.event.opcode;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.myreward.engine.event.opcode.IfDurationModel.IfDurationType;
import com.myreward.engine.model.event.EventDO;
import com.myreward.engine.model.event.IfOperationResult;
import com.myreward.engine.model.event.OperationResultDO;
import com.myreward.engine.model.event.StatementOperationResult;
import com.myreward.parser.generator.MyRewardDataSegment;
import com.myreward.parser.generator.MyRewardDataSegment.EventDataObject;

public class IfEventModel extends IfBaseModel {
	public enum IfCompletionType {
		FLAG,
		AMOUNT,
		EVENT,
		DATE
	}
	public enum IfCompletionAmtType {
		LE("_LE"),
		LT("_LT"),
		EQ("_EQ"),
		GT("_GT"),
		GE("_GE");
		
		private final String value;
		IfCompletionAmtType(String value) {
			this.value = value;
		}
		public String value(){
			return this.value;
		}
	}
	public enum IfCompletionDtType {
		LE("_LE"),
		LT("_LT"),
		EQ("_EQ"),
		GT("_GT"),
		GE("_GE");
		
		private final String value;
		IfCompletionDtType(String value) {
			this.value = value;
		}
		public String value(){
			return this.value;
		}
	}
	public enum IfCompletionFlgType {
		SET("_set"),
		NOT_SET("_not_set");
		
		private final String value;
		IfCompletionFlgType(String value) {
			this.value = value;
		}
		public String value(){
			return this.value;
		}
	}

	private static String OPCODE_LABEL_FLAG = "if_cmp_flg";
	private static String OPCODE_LABEL_AMOUNT = "if_cmp_cnt";
	private static String OPCODE_LABEL_EVENT = "if_evt_nm";
	private static String OPCODE_LABEL_DATE = "if_evt_dt";
	
	private static String OPCODE_OPERAND_START = "(";
	private static String OPCODE_OPERAND_END = ")";
	private static String OPERAND_FORMAT_PATTERN = ",";
	private IfCompletionType type;
	private IfCompletionAmtType amountType;
	private IfCompletionFlgType flagType;
	private IfCompletionDtType dtType;

	private String name;
	private String amount;
	private String eventName;
	private int gotoLine;
	private Date date;
	public static String[] OPCODE_HANDLER = {OPCODE_LABEL_FLAG, OPCODE_LABEL_AMOUNT, OPCODE_LABEL_EVENT, OPCODE_LABEL_DATE};

	public IfEventModel() {
		
	}

	public IfEventModel(String statement) {
		IfCompletionType ifCompletionType = getType(statement);
		if(ifCompletionType!=null) {
			type = ifCompletionType;
			if(type==IfCompletionType.AMOUNT) {
				String[] amountOperand = this.parse(OPCODE_LABEL_AMOUNT, OPERAND_FORMAT_PATTERN, statement);
				name = amountOperand[0];
				amount = amountOperand[1];
				if(StringUtils.startsWith(statement, OPCODE_LABEL_AMOUNT+IfCompletionAmtType.LE.value)) {
					amountType = IfCompletionAmtType.LE;
				} else if(StringUtils.startsWith(statement, OPCODE_LABEL_AMOUNT+IfCompletionAmtType.LT.value)) {
					amountType = IfCompletionAmtType.LT;
				} else if(StringUtils.startsWith(statement, OPCODE_LABEL_AMOUNT+IfCompletionAmtType.EQ.value)) {
					amountType = IfCompletionAmtType.EQ;
				} else if(StringUtils.startsWith(statement, OPCODE_LABEL_AMOUNT+IfCompletionAmtType.GT.value)) {
					amountType = IfCompletionAmtType.GT;
				} else if(StringUtils.startsWith(statement, OPCODE_LABEL_AMOUNT+IfCompletionAmtType.GE.value)) {
					amountType = IfCompletionAmtType.GE;
				}
			} else if(type==IfCompletionType.DATE) {
				String[] dateOperand = this.parse(OPCODE_LABEL_DATE, OPERAND_FORMAT_PATTERN, statement);
				String longDate = dateOperand[0].trim();
				date = new Date(Long.parseLong(longDate));
				if(StringUtils.startsWith(statement, OPCODE_LABEL_DATE+IfCompletionDtType.LE.value)) {
					dtType = IfCompletionDtType.LE;
				} else if(StringUtils.startsWith(statement, OPCODE_LABEL_DATE+IfCompletionDtType.LT.value)) {
					dtType = IfCompletionDtType.LT;
				} else if(StringUtils.startsWith(statement, OPCODE_LABEL_DATE+IfCompletionDtType.EQ.value)) {
					dtType = IfCompletionDtType.EQ;
				} else if(StringUtils.startsWith(statement, OPCODE_LABEL_DATE+IfCompletionDtType.GT.value)) {
					dtType = IfCompletionDtType.GT;
				} else if(StringUtils.startsWith(statement, OPCODE_LABEL_DATE+IfCompletionDtType.GE.value)) {
					dtType = IfCompletionDtType.GE;
				}
			} else if(type==IfCompletionType.FLAG) {
				String[] flagOperand = this.parse(OPCODE_LABEL_FLAG, null, statement);
				if(flagOperand.length>0)
					if(StringUtils.contains(flagOperand[0], OPERAND_FORMAT_PATTERN)) {
						flagOperand = StringUtils.split(flagOperand[0], OPERAND_FORMAT_PATTERN);
						if(flagOperand.length==1)
							name = flagOperand[0];
						if(flagOperand.length==2) {
							name = flagOperand[0];
							gotoLine = Integer.valueOf(flagOperand[1]);
						}
						if(flagOperand.length==3) {
							name = flagOperand[0];
							gotoLine = Integer.valueOf(flagOperand[2]);
						}
						
					} else 
						name = flagOperand[0];
				if(StringUtils.startsWith(statement, OPCODE_LABEL_FLAG+IfCompletionFlgType.SET.value)) {
					flagType = IfCompletionFlgType.SET;
				} else if(StringUtils.startsWith(statement, OPCODE_LABEL_FLAG+IfCompletionFlgType.NOT_SET.value)) {
					flagType = IfCompletionFlgType.NOT_SET;
				}
			} else if(type==IfCompletionType.EVENT) {
				String[] eventOperand = this.parse(OPCODE_LABEL_EVENT, OPERAND_FORMAT_PATTERN, statement);
				eventName = eventOperand[0];
				gotoLine = Integer.valueOf(eventOperand[1]).intValue();
			}
		}
	}
	public IfCompletionType getType(String opcode) {
		if(StringUtils.startsWith(opcode, OPCODE_LABEL_FLAG))
			return IfCompletionType.FLAG;
		if(StringUtils.startsWith(opcode, OPCODE_LABEL_AMOUNT))
			return IfCompletionType.AMOUNT;
		if(StringUtils.startsWith(opcode, OPCODE_LABEL_EVENT))
			return IfCompletionType.EVENT;
		if(StringUtils.startsWith(opcode, OPCODE_LABEL_DATE))
			return IfCompletionType.DATE;
		return null;
	}
	public String[] parse(String opcodeLabelFlag, String operandSeparator, String value) {
		if(!StringUtils.startsWith(value, opcodeLabelFlag))
			return null;
		String operandValue = StringUtils.substringAfter(value, OPCODE_OPERAND_START);
		operandValue = StringUtils.substringBeforeLast(operandValue, OPCODE_OPERAND_END);
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
		if(type==IfCompletionType.FLAG) {
			return OPCODE_LABEL_FLAG+flagType.value+OPCODE_OPERAND_START+name+OPCODE_OPERAND_END;
		}
		if(type==IfCompletionType.AMOUNT) {
			return OPCODE_LABEL_AMOUNT+amountType.value+OPCODE_OPERAND_START+name+OPERAND_FORMAT_PATTERN+amount+OPCODE_OPERAND_END;
		}
		if(type==IfCompletionType.EVENT) {
			return OPCODE_LABEL_AMOUNT+amountType.value+OPCODE_OPERAND_START+name+OPERAND_FORMAT_PATTERN+amount+OPCODE_OPERAND_END;
		}
		if(type==IfCompletionType.DATE) {
			return OPCODE_LABEL_AMOUNT+amountType.value+OPCODE_OPERAND_START+name+OPERAND_FORMAT_PATTERN+amount+OPCODE_OPERAND_END;
		}
		return null;
	}

	@Override
	public OperationResultDO process(List<OpCodeBaseModel> instructionOpCodes, MyRewardDataSegment myRewardDataSegment, EventDO event) {
		OperationResultDO operationResultDO = null;
		if(type==IfCompletionType.FLAG) {
			operationResultDO = new IfOperationResult();;
			EventDataObject eventDataObject = myRewardDataSegment.search(name);
			if(eventDataObject!=null) {
				if(eventDataObject.isEventCompletionFlagSet()) {
					((IfOperationResult)operationResultDO).setResult(true);
					((IfOperationResult)operationResultDO).setNextOperationNumber(1);
				} else {
					((IfOperationResult)operationResultDO).setResult(true);
					((IfOperationResult)operationResultDO).setNextOperationNumber(gotoLine);
				}
				return operationResultDO;
			}
			operationResultDO.setResult(false);
			return operationResultDO;

		}
		if(type==IfCompletionType.AMOUNT) {
		}
		if(type==IfCompletionType.EVENT) {
			if(event.isValid()) {
				if(StringUtils.equalsAnyIgnoreCase(event.getActivityName(), eventName)) {
					operationResultDO = new IfOperationResult();
					((IfOperationResult)operationResultDO).setResult(true);
					((IfOperationResult)operationResultDO).setNextOperationNumber(1);
				} else {
					operationResultDO = new IfOperationResult();
					((IfOperationResult)operationResultDO).setResult(false);
					((IfOperationResult)operationResultDO).setNextOperationNumber(gotoLine);
				}
			}
		}
		if(type==IfCompletionType.DATE) {
		}
		return operationResultDO;	
	}
}
