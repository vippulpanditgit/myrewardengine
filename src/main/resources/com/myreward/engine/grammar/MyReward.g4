grammar MyReward;
options {
    language=Java;
}
@header {
import com.myreward.engine.grammar.Symbol;
import com.myreward.engine.grammar.SymbolTable;
import com.myreward.engine.grammar.MyRewardSymbolTable;
import com.myreward.engine.util.W3CDateFormat;
import java.text.ParseException;
import java.util.*;
import java.lang.*; 
import java.io.*;
import com.myreward.engine.model.*;
import com.myreward.engine.metamodel.*;
import com.myreward.engine.util.*;
}
@members {
//  private static org.slf4j.Logger _logger =
//    org.slf4j.LoggerFactory.getLogger(com.myreward.engine.grammar.MyRewardParser.class);
	private Symbol packageSymbol;
	private Symbol current;
	private SymbolTable symbolTable = new MyRewardSymbolTable();
	private int level = 0;
	
	
	public SymbolTable getSymbolTable() {
		return symbolTable;
	}
}

// Keywords
ANY : 'any';
ALL : 'all';
BETWEEN : 'between';
COMMA : ',';
REPEAT : 'repeat';
SHOW : 'show';
PRIORITY : 'priority';
GATEKEEPER : 'gatekeeper';
EVENT : 'event';
REWARD : 'reward';
IMPORT : 'import';
PACKAGE : 'package';

INT    : [0-9]+;
DOUBLE : [0-9]+'.'[0-9]+;
PI     : 'pi';
E      : 'e';
NL     : '\n';
WS     : [ \t\n\r]+ -> skip;
ID     : [a-zA-Z_][a-zA-Z_0-9.]*;

//1997-07-16T19:20:30.45+01:00
DATE_TIME   : [0-9][0-9][0-9][0-9]'-'[0-9][0-9]'-'[0-9][0-9]'T'[0-2][0-9]':'[0-9][0-9]':'[0-9][0-9]'.'[0-9][0-9]'+'[0-9][0-9]':'[0-9][0-9];

LPAREN : '(';
RPAREN : ')';
LBRACE : '{' {level++;/*System.out.println("level "+level);*/};
RBRACE : '}' {level--;/*System.out.println("level "+level);*/};
LBRACK : '[';
RBRACK : ']';
SEMI : ';';
DOT : '.';
ELLIPSIS : '...';
AT : '@';
COLONCOLON : '::';


// §3.12 Operators

ASSIGN : '=';
GT : '>';
LT : '<';
BANG : '!';
TILDE : '~';
QUESTION : '?';
COLON : ':';
ARROW : '->';
EQUAL : '==';
LE : '<=';
GE : '>=';
NOTEQUAL : '!=';
AND : '&&';
OR : '||';
INC : '++';
DEC : '--';
ADD : '+';
SUB : '-';
MUL : '*';
DIV : '/';
BITAND : '&';
BITOR : '|';
CARET : '^';
MOD : '%';

ADD_ASSIGN : '+=';
SUB_ASSIGN : '-=';
MUL_ASSIGN : '*=';
DIV_ASSIGN : '/=';
AND_ASSIGN : '&=';
OR_ASSIGN : '|=';
XOR_ASSIGN : '^=';
MOD_ASSIGN : '%=';
LSHIFT_ASSIGN : '<<=';
RSHIFT_ASSIGN : '>>=';
URSHIFT_ASSIGN : '>>>=';

COMMENT
    :   '/*' .*? '*/' -> channel(HIDDEN)
    ;

LINE_COMMENT
    :   '//' ~[\r\n]* -> channel(HIDDEN)
    ;

/*
*	Parser Rules
*/
myreward_defs 
	: myreward_def+
	;
myreward_def
	: package_def import_def* event_def+ 
	| package_def event_def+
	;
import_def
	: IMPORT importLib=import_name 
	;
import_name returns [String importSymbolLibrary]
	: importName = ID {
						try {
							MyRewardParser myRewardParser = MyRewardParserUtil.getParsed(TestData.getTestData($importName.getText()));
							Myreward_defsContext fileContext = myRewardParser.myreward_defs(); 
							
							this.getSymbolTable().merge(myRewardParser.getSymbolTable().getAllSymbol());
						} catch(IOException exp){exp.printStackTrace();}
							
					}
	;
package_def
	: PACKAGE package_name
	;
package_name returns [Symbol packageSymbol]
	: packageName = ID {
						$packageSymbol = new Symbol();
						$packageSymbol.setType(Symbol.SymbolType.PACKAGE);
						$packageSymbol.setName($packageName.getText());
						symbolTable.insertSymbol($packageSymbol);
						packageSymbol = $packageSymbol;
					}
	;		
event_def returns [EventMetaModel eventMetaModel]
	: EVENT LPAREN eventName=event_name RPAREN (modifier=event_modifier_def)* {
					$eventMetaModel = new EventMetaModel();
					$eventMetaModel.setEventName($eventName.eventName);
					if($modifier.modifierMetaModel instanceof EventGroupingMetaModel){
						$eventMetaModel.setEventType(EventMetaModel.EventType.DERIVED_EVENT);
					} else {
						$eventMetaModel.setEventType(EventMetaModel.EventType.EVENT);
					}
					
					
					
				}
	;
event_modifier_def returns [BaseMetaModel modifierMetaModel]
	: DOT reward_def {
						$modifierMetaModel = new RewardMetaModel();
					}
	| DOT group_def	{
						$modifierMetaModel = new EventGroupingMetaModel();
					}
	| DOT between_def {
						$modifierMetaModel = new DurationMetaModel();
					}
	| DOT repeat_def {
						$modifierMetaModel = new RepeatMetaModel();
					}
	| DOT show_def {
						$modifierMetaModel = new ShowMetaModel();
					}
	| DOT priority_def {
						$modifierMetaModel = new PriorityMetaModel();
					}
	| DOT gatekeeper_def {
						$modifierMetaModel = new GatekeeperMetaModel();
					}
	;
event_name returns [String eventName]
	: eventNameElement=ID { current = new Symbol();
				current.setName($eventNameElement.getText());
				current.setLevel(level);
				current.setType(Symbol.SymbolType.EVENT);
				current.setContainer(packageSymbol);
				symbolTable.insertSymbol(current);
				$eventName = $eventNameElement.getText();
				}
	;
group_def returns [GroupMetaModel groupDefMetaModel]
	: groupAnyLogic=any_def LBRACE groupEvent = group_events_def RBRACE {	
																$groupDefMetaModel = $groupAnyLogic.groupModel;
															}
	| groupAllLogic=all_def LBRACE groupEvent = group_events_def RBRACE {
																$groupDefMetaModel = $groupAllLogic.groupModel;
															}
	| groupAnyLogic=any_def {	
																$groupDefMetaModel = $groupAnyLogic.groupModel;
																current.setType(Symbol.SymbolType.EVENT);
															}
	| groupAllLogic=all_def	{	
																$groupDefMetaModel = $groupAllLogic.groupModel;
																current.setType(Symbol.SymbolType.EVENT);
															}													
	;
group_events_def returns [GroupMetaModel groupEventsDefMetaModel]
	: event_def (COMMA event_def)* 	# eventGrouping
	;
all_def returns [GroupMetaModel groupModel]
	: ALL	{ if($groupModel==null) $groupModel = new GroupMetaModel();
									$groupModel.logic = GroupMetaModel.GROUP_LOGIC.ALL;
									current.setType(Symbol.SymbolType.DERIVED_EVENT);
									}
	;
any_def returns [GroupMetaModel groupModel]
	: ANY ordinal = sequence_def { if($groupModel==null) $groupModel = new GroupMetaModel();
									$groupModel.logic = GroupMetaModel.GROUP_LOGIC.ANY;
									$groupModel.ordinal = $ordinal.value;
									current.setType(Symbol.SymbolType.DERIVED_EVENT);
									}
	| ANY { if($groupModel==null) $groupModel = new GroupMetaModel();
									$groupModel.logic = GroupMetaModel.GROUP_LOGIC.ANY;
									$groupModel.ordinal = 1;
									current.setType(Symbol.SymbolType.DERIVED_EVENT);
									}
	;
sequence_def returns [int value]
	: LPAREN intValue=INT RPAREN {$value = Integer.parseInt($intValue.getText());}
	| intValue=INT				{$value = Integer.parseInt($intValue.getText());}
	;
show_def
	: SHOW LPAREN 'true' RPAREN  {current.setShow(true);}
	| SHOW LPAREN 'false' RPAREN {current.setShow(false);}
	;
priority_def
	: PRIORITY sequenceDef=sequence_def {current.setPriority($sequenceDef.value);}
	;
between_def
	: BETWEEN LPAREN '\'' effectiveDate=DATE_TIME '\'' COMMA '\'' expirationDate=DATE_TIME '\'' RPAREN {
									W3CDateFormat format = new W3CDateFormat(W3CDateFormat.Pattern.AUTO);
									Date effectiveDate=null;
									Date expirationDate=null;
									try {
										effectiveDate = format.parse($effectiveDate.getText());
										expirationDate = format.parse($expirationDate.getText());
										current.setEffectiveDate(effectiveDate);
										current.setExpirationDate(expirationDate);
									} catch (ParseException e) {e.printStackTrace();}
								}
	| BETWEEN LPAREN '\'' startDate=DATE_TIME '\'' RPAREN {
									W3CDateFormat format = new W3CDateFormat(W3CDateFormat.Pattern.AUTO);
									Date startDate=null;
									Date endDate=null;
									try {
										startDate = format.parse($startDate.getText());
									} catch (ParseException e) {e.printStackTrace();}
									//System.out.println(startDate+","+endDate);
								}
	;
repeat_def
	: REPEAT LPAREN repeatOnFrequency=repeat_frequency_def RPAREN {
								RepeatOn repeatOn = new RepeatOn();
								repeatOn.setRepeatOn($repeatOnFrequency.repeatOn);
								current.setRepeatOn(repeatOn);
							}
	| REPEAT LPAREN repeatOnFrequency=repeat_frequency_def COMMA timeFrame=INT RPAREN {
								RepeatOn repeatOn = new RepeatOn();
								repeatOn.setRepeatOn($repeatOnFrequency.repeatOn);
								repeatOn.setRepeatOnAfter(Integer.parseInt($timeFrame.getText()));
								current.setRepeatOn(repeatOn);
							}
	;
repeat_frequency_def returns [RepeatOn.EnumRepeatOn repeatOn]
	: repeatFrequency='WEEKLY'  {$repeatOn = RepeatOn.EnumRepeatOn.WEEKLY;}
	| repeatFrequency='MONTHLY' {$repeatOn = RepeatOn.EnumRepeatOn.MONTHLY;}
	| repeatFrequency='YEARLY'  {$repeatOn = RepeatOn.EnumRepeatOn.YEARLY;}
	| repeatFrequency='ACTIVITY_DATE' {$repeatOn = RepeatOn.EnumRepeatOn.ACTIVITY_DATE;}
	;
gatekeeper_def returns [GatekeeperMetaModel gateKeeperMetaModel]
	: GATEKEEPER LPAREN eventDef=event_def RPAREN {
									$gateKeeperMetaModel = new GatekeeperMetaModel();
									
								}
	;
reward_def
	: REWARD LPAREN reward_quantity_def RPAREN
	;
reward_quantity_def
	: rewardQuantity=DOUBLE {
							double rewardAmount = 0;
							Reward reward = new Reward();
							try {
								rewardAmount = Double.parseDouble($rewardQuantity.getText());
								reward.setAmount(rewardAmount);
							}catch(Exception exp){exp.printStackTrace();}
							current.setReward(reward);
						}
	| rewardQuantity='true'
	| rewardQuantity='false'
	| rewardQuantity=INT {double rewardAmount = 0;
							Reward reward = new Reward();
							try {
								rewardAmount = Double.parseDouble($rewardQuantity.getText());
								reward.setAmount(rewardAmount);
							}catch(Exception exp){exp.printStackTrace();}
							current.setReward(reward);
						}
	;
STRING
   : '"' (ESC | ~ ["\\])* '"' 
   ;
fragment ESC
   : '\\' (["\\/bfnrt] | UNICODE)
   ;
fragment UNICODE
   : 'u' HEX HEX HEX HEX
   ;
fragment HEX
   : [0-9a-fA-F]
   ;

