package com.myreward.engine.event.opcode;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.myreward.engine.model.event.EventDO;
import com.myreward.engine.model.event.IfOperationResult;
import com.myreward.engine.model.event.OperationResultDO;
import com.myreward.parser.generator.MyRewardDataSegment;
import com.myreward.parser.generator.MyRewardDataSegment.EventDataObject;

public class IfGatekeeperModel extends IfBaseModel {
	public enum IfGatekeeperType {
		FLAG,
		AMOUNT
	}
	public enum IfGatekeeperAmtType {
		LE("_LE"),
		LT("_LT"),
		EQ("_EQ"),
		GT("_GT"),
		GE("_GE");
		
		private final String value;
		IfGatekeeperAmtType(String value) {
			this.value = value;
		}
		public String value(){
			return this.value;
		}
	}
	public enum IfGatekeeperFlgType {
		SET("_set"),
		NOT_SET("_not_set");
		
		private final String value;
		IfGatekeeperFlgType(String value) {
			this.value = value;
		}
		public String value(){
			return this.value;
		}
	}

	private static String OPCODE_LABEL_FLAG = "if_gtk_flg";
	private static String OPCODE_LABEL_AMOUNT = "if_gtk_amt";
	private static String OPCODE_OPERAND_START = "(";
	private static String OPCODE_OPERAND_END = ")";
	private static String OPERAND_FORMAT_PATTERN = ",";
	private IfGatekeeperType type;
	private IfGatekeeperAmtType amountType;
	private IfGatekeeperFlgType flagType;
	private String name;
	private String amount;
	public static String[] OPCODE_HANDLER = {OPCODE_LABEL_FLAG, OPCODE_LABEL_AMOUNT};

	public IfGatekeeperModel() {
		
	}

	public IfGatekeeperModel(String statement) {
		IfGatekeeperType ifGatekeeperType = getType(statement);
		if(ifGatekeeperType!=null) {
			type = ifGatekeeperType;
			if(type==IfGatekeeperType.AMOUNT) {
				String[] amountOperand = this.parse(OPCODE_LABEL_AMOUNT, OPERAND_FORMAT_PATTERN, statement);
				name = amountOperand[0];
				amount = amountOperand[1];
				if(StringUtils.startsWith(statement, OPCODE_LABEL_AMOUNT+IfGatekeeperAmtType.LE.value)) {
					amountType = IfGatekeeperAmtType.LE;
				} else if(StringUtils.startsWith(statement, OPCODE_LABEL_AMOUNT+IfGatekeeperAmtType.LT.value)) {
					amountType = IfGatekeeperAmtType.LT;
				} else if(StringUtils.startsWith(statement, OPCODE_LABEL_AMOUNT+IfGatekeeperAmtType.EQ.value)) {
					amountType = IfGatekeeperAmtType.EQ;
				} else if(StringUtils.startsWith(statement, OPCODE_LABEL_AMOUNT+IfGatekeeperAmtType.GT.value)) {
					amountType = IfGatekeeperAmtType.GT;
				} else if(StringUtils.startsWith(statement, OPCODE_LABEL_AMOUNT+IfGatekeeperAmtType.GE.value)) {
					amountType = IfGatekeeperAmtType.GE;
				}
			} else if(type==IfGatekeeperType.FLAG) {
				String[] flagOperand = this.parse(OPCODE_LABEL_FLAG, null, statement);
				name = flagOperand[0];
				if(StringUtils.startsWith(statement, OPCODE_LABEL_FLAG+IfGatekeeperFlgType.SET.value)) {
					flagType = IfGatekeeperFlgType.SET;
				} else if(StringUtils.startsWith(statement, OPCODE_LABEL_FLAG+IfGatekeeperFlgType.NOT_SET.value)) {
					flagType = IfGatekeeperFlgType.NOT_SET;
				}
			}
		}
	}
	public IfGatekeeperType getType(String opcode) {
		if(StringUtils.startsWith(opcode, OPCODE_LABEL_FLAG))
			return IfGatekeeperType.FLAG;
		if(StringUtils.startsWith(opcode, OPCODE_LABEL_AMOUNT))
			return IfGatekeeperType.AMOUNT;
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
		if(type==IfGatekeeperType.FLAG) {
			return OPCODE_LABEL_FLAG+flagType.value+OPCODE_OPERAND_START+name+OPCODE_OPERAND_END;
		}
		if(type==IfGatekeeperType.AMOUNT) {
			return OPCODE_LABEL_AMOUNT+amountType.value+OPCODE_OPERAND_START+name+OPERAND_FORMAT_PATTERN+amount+OPCODE_OPERAND_END;
		}
		return null;
	}

	@Override
	public OperationResultDO process(List<OpCodeBaseModel> instructionOpCodes, MyRewardDataSegment myRewardDataSegment,
			EventDO event) {
		OperationResultDO operationResultDO = null;
		if(type==IfGatekeeperType.FLAG) {
			operationResultDO = new IfOperationResult();;
			EventDataObject eventDataObject = myRewardDataSegment.search(name);
			if(eventDataObject!=null) {
				if(eventDataObject.isGatekeeperStatusSet()) {
					((IfOperationResult)operationResultDO).setResult(true);
					((IfOperationResult)operationResultDO).setNextOperationNumber(1);
				} else {
					((IfOperationResult)operationResultDO).setResult(false);
					((IfOperationResult)operationResultDO).setNextOperationNumber(2);
				}
				return operationResultDO;
			}
			operationResultDO.setResult(false);
			return operationResultDO;
		}
		if(type==IfGatekeeperType.AMOUNT) {
			operationResultDO = new IfOperationResult();;
			EventDataObject eventDataObject = myRewardDataSegment.search(name);
			((IfOperationResult)operationResultDO).setResult(false);
			((IfOperationResult)operationResultDO).setNextOperationNumber(-1);
			if(eventDataObject!=null) {
				if(amountType==IfGatekeeperAmtType.LT) {
					if(eventDataObject.getReward() < Double.valueOf(amount)) {
						((IfOperationResult)operationResultDO).setResult(true);
						((IfOperationResult)operationResultDO).setNextOperationNumber(1);
					}
				}
				if(amountType==IfGatekeeperAmtType.LE) {
					if(eventDataObject.getReward() <= Double.valueOf(amount)) {
						((IfOperationResult)operationResultDO).setResult(true);
						((IfOperationResult)operationResultDO).setNextOperationNumber(1);
					}
				}
				if(amountType==IfGatekeeperAmtType.EQ) {
					if(eventDataObject.getReward() == Double.valueOf(amount)) {
						((IfOperationResult)operationResultDO).setResult(true);
						((IfOperationResult)operationResultDO).setNextOperationNumber(1);
					}
				}
				if(amountType==IfGatekeeperAmtType.GE) {
					if(eventDataObject.getReward() >= Double.valueOf(amount)) {
						((IfOperationResult)operationResultDO).setResult(true);
						((IfOperationResult)operationResultDO).setNextOperationNumber(1);
					}
				}
				if(amountType==IfGatekeeperAmtType.GT) {
					if(eventDataObject.getReward() > Double.valueOf(amount)) {
						((IfOperationResult)operationResultDO).setResult(true);
						((IfOperationResult)operationResultDO).setNextOperationNumber(1);
					}
				}
				return operationResultDO;
			}
			return operationResultDO;
		}
		return operationResultDO;
	}
}
