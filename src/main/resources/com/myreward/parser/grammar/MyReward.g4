grammar MyReward;
options {
    language=Java;
}
@header {
import java.text.ParseException;
import java.util.*;
import java.lang.*; 
import java.io.*;
import com.myreward.parser.model.*;
import com.myreward.parser.symbol.*;
import com.myreward.parser.metamodel.*;
import com.myreward.parser.util.*;
import com.myreward.engine.app.AppInstanceContext;
import com.myreward.engine.event.processor.MetaOpCodeProcessor;
import com.myreward.engine.event.error.BuildException;
import com.myreward.engine.event.error.MetaDataParsingException;
import com.myreward.engine.event.error.ReferencedModelException;

}
@members {
//  private static org.slf4j.Logger _logger =
//    org.slf4j.LoggerFactory.getLogger(com.myreward.parser.grammar.MyRewardParser.class);
	private Symbol packageSymbol;
	private Symbol current;
	public SymbolTable symbolTable = new MyRewardSymbolTable();
	private int level = 0;
	public MetaOpCodeProcessor metaOpCodeProcessor;
	private Stack<Symbol> symbolStack = new Stack<>();
	
	
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
	: myRewardDef=myreward_def+ {
		}
	;
myreward_def returns [MyRewardMetaModel myRewardMetaModel]
	: packageDef=package_def (import_def{$packageDef.packageMetaModel.packageMetaModelList.add($import_def.importMetaModel);
										$import_def.importMetaModel.parent = $packageDef.packageMetaModel;})* 
										(eventDef=event_def {
					// Event Handling										
					$eventDef.eventMetaModel.parent = $packageDef.packageMetaModel;
					$packageDef.packageMetaModel.packageMetaModelList.add($eventDef.eventMetaModel);
					$myRewardMetaModel = new MyRewardMetaModel();
					$myRewardMetaModel.symbolTable = symbolTable;
					$packageDef.packageMetaModel.parent = $myRewardMetaModel;
					$myRewardMetaModel.myRewardMetaModelList.add($packageDef.packageMetaModel);
				}) +
	;
import_def returns [ImportMetaModel importMetaModel]
	: IMPORT importLib=import_name {
					$importMetaModel = new ImportMetaModel();
					$importMetaModel.symbolTable = symbolTable;
					$importMetaModel.importMetaModelList.add($importLib.importSymbolLibrary);
				} SEMI
	;
	catch[RecognitionException e] { throw e; }
import_name returns [String importSymbolLibrary]
	: importName = ID {
						if(metaOpCodeProcessor.captureToProcessList(FileProcessingUtil.getDefaultFilePath()+((Import_nameContext)_localctx).importName.getText())) {
							((Import_nameContext)_localctx).importSymbolLibrary =  ((Import_nameContext)_localctx).importName.getText();
						}
					}
	;
package_def returns [PackageMetaModel packageMetaModel]
	: PACKAGE packageName=package_name {
						$packageMetaModel = new PackageMetaModel();
						$packageMetaModel.symbolTable = symbolTable;
						$packageMetaModel.packageName = $packageName.packageNameElement;
					} SEMI
	;
package_name returns [String packageNameElement]
	: packageName = ID {
						packageSymbol = new Symbol();
						packageSymbol.setType(Symbol.SymbolType.PACKAGE);
						packageSymbol.setName($packageName.getText());
						symbolTable.insertSymbol(packageSymbol);
						packageSymbol.setNamespace(null);
						current = packageSymbol;
						symbolStack.push(current);
						
						level++;
						$packageNameElement = $packageName.getText();
					}
	;		
event_def returns [EventMetaModel eventMetaModel]
	: EVENT LPAREN eventName=event_name {
						if($eventMetaModel==null)
							$eventMetaModel = new EventMetaModel();
						$eventMetaModel.symbolTable = symbolTable;
						$eventMetaModel.setEventName($eventName.eventName);
						$eventMetaModel.packageName = packageSymbol.getName();
						$eventMetaModel.setEventType(EventMetaModel.EventType.EVENT);
				 } RPAREN (modifier=event_modifier_def {
				 	$modifier.modifierMetaModel.parent = $eventMetaModel;
				 	$modifier.modifierMetaModel.namespace = $eventMetaModel.namespace;
				 	$modifier.modifierMetaModel.symbolTable = symbolTable;
					if($modifier.modifierMetaModel instanceof GroupMetaModel){
						$eventMetaModel.setGroupMetaModel((GroupMetaModel)$modifier.modifierMetaModel);
					} else if($modifier.modifierMetaModel instanceof ShowMetaModel){
						$eventMetaModel.setShowMetaModel((ShowMetaModel)$modifier.modifierMetaModel);
					} else if($modifier.modifierMetaModel instanceof RewardMetaModel){
						$eventMetaModel.setRewardMetaModel((RewardMetaModel)$modifier.modifierMetaModel);
					} else if($modifier.modifierMetaModel instanceof RepeatMetaModel){
						$eventMetaModel.setRepeatMetaModel((RepeatMetaModel)$modifier.modifierMetaModel);
					} else if($modifier.modifierMetaModel instanceof DurationMetaModel){
						$eventMetaModel.setDuraitonMetaModel((DurationMetaModel)$modifier.modifierMetaModel);
					} else if($modifier.modifierMetaModel instanceof PriorityMetaModel){
						$eventMetaModel.setPriorityMetaModel((PriorityMetaModel)$modifier.modifierMetaModel);
					} else if($modifier.modifierMetaModel instanceof GatekeeperMetaModel){
						$eventMetaModel.setGatekeeperMetaModel((GatekeeperMetaModel)$modifier.modifierMetaModel);
					} else {
						$eventMetaModel.setEventType(EventMetaModel.EventType.EVENT);
					}
				})*
	;
event_modifier_def returns [BaseMetaModel modifierMetaModel]
	: DOT rewardDef=reward_def {
						$rewardDef.rewardMetaModel.parent = $modifierMetaModel;
						$rewardDef.rewardMetaModel.packageName = packageSymbol.getName();
						$modifierMetaModel = $rewardDef.rewardMetaModel;
					}
	| DOT groupDef=group_def	{
						$groupDef.groupDefMetaModel.parent = $modifierMetaModel;
						$groupDef.groupDefMetaModel.packageName = packageSymbol.getName();
						$modifierMetaModel = $groupDef.groupDefMetaModel;
					}
	| DOT durationDef=between_def {
						$durationDef.durationMetaModel.parent = $modifierMetaModel;
						$durationDef.durationMetaModel.packageName = packageSymbol.getName();
						$modifierMetaModel = $durationDef.durationMetaModel;
					}
	| DOT repeatDef=repeat_def {
						$repeatDef.repeatMetaModel.parent = $modifierMetaModel;
						$repeatDef.repeatMetaModel.packageName = packageSymbol.getName();
						$modifierMetaModel = $repeatDef.repeatMetaModel;
					}
	| DOT showDef=show_def {
						$showDef.showMetaModel.parent = $modifierMetaModel;
						$showDef.showMetaModel.packageName = packageSymbol.getName();
						$modifierMetaModel = $showDef.showMetaModel;
					}
	| DOT priorityDef=priority_def {
						$priorityDef.priorityMetaModel.parent = $modifierMetaModel;
						$priorityDef.priorityMetaModel.packageName = packageSymbol.getName();
						$modifierMetaModel = $priorityDef.priorityMetaModel;
					}
	| DOT gatekeeperDef=gatekeeper_def {
						$gatekeeperDef.gatekeeperMetaModel.parent = $modifierMetaModel;
						$gatekeeperDef.gatekeeperMetaModel.packageName = packageSymbol.getName();
						$modifierMetaModel = $gatekeeperDef.gatekeeperMetaModel;
					}
	;
event_name returns [String eventName]
	: eventNameElement=ID {
				Symbol storedCurrent = current; 
				current = new Symbol();
				current.setName($eventNameElement.getText());
				current.setLevel(level);
				current.setType(Symbol.SymbolType.EVENT);
				current.setPackageName(packageSymbol.getName());
				current.setContainer(packageSymbol);
				symbolStack.push(current);
				if(storedCurrent==null || level==0) {
					symbolTable.insertSymbol(current);
				} else {
					storedCurrent.addChild(current);
				}
				$eventName = $eventNameElement.getText();
		}
	;
group_def returns [GroupMetaModel groupDefMetaModel]
	: groupAnyLogic=any_def LBRACE{level++;} groupEventDef = group_events_def RBRACE {
																level--;
																$groupEventDef.groupEventsDefMetaModel.parent = $groupDefMetaModel;
																$groupDefMetaModel = $groupEventDef.groupEventsDefMetaModel;	
																$groupDefMetaModel.ordinalMetaModel = $groupAnyLogic.anyMetaModel;
																$groupDefMetaModel.packageName = packageSymbol.getName();
															}
	| groupAllLogic=all_def LBRACE{level++;} groupEventDef = group_events_def RBRACE {
																level--;
																$groupAllLogic.groupModel.parent = $groupDefMetaModel;
																$groupDefMetaModel = $groupAllLogic.groupModel;
																$groupDefMetaModel.eventMetaModelList = $groupEventDef.groupEventsDefMetaModel.eventMetaModelList;
																$groupDefMetaModel.packageName = packageSymbol.getName();
															}
	| groupAnyLogic=any_def {	
																
																$groupDefMetaModel = new GroupMetaModel();
																$groupDefMetaModel.symbolTable = symbolTable;
																$groupDefMetaModel.ordinalMetaModel = $groupAnyLogic.anyMetaModel;
																$groupDefMetaModel.packageName = packageSymbol.getName();
																current.setType(Symbol.SymbolType.EVENT);
															}
	| groupAllLogic=all_def	{									$groupAllLogic.groupModel.parent = $groupDefMetaModel;
																$groupDefMetaModel = $groupAllLogic.groupModel;
																$groupDefMetaModel.packageName = packageSymbol.getName();
																current.setType(Symbol.SymbolType.EVENT);
															}													
	;
group_events_def returns [GroupMetaModel groupEventsDefMetaModel]
	@init {$groupEventsDefMetaModel = new GroupMetaModel();
			$groupEventsDefMetaModel.symbolTable = symbolTable;}
	: eventDef=event_def {			$eventDef.eventMetaModel.parent = $groupEventsDefMetaModel;
									$groupEventsDefMetaModel.eventMetaModelList.add($eventDef.eventMetaModel);
						} (COMMA eventDefNext=event_def {
										$eventDefNext.eventMetaModel.parent = $groupEventsDefMetaModel;
										$groupEventsDefMetaModel.eventMetaModelList.add($eventDefNext.eventMetaModel);
									})* 	
	;
all_def returns [GroupMetaModel groupModel]
	: ALL	{ if($groupModel==null) $groupModel = new GroupMetaModel();
									$groupModel.symbolTable = symbolTable;
									$groupModel.logic = GroupMetaModel.GROUP_LOGIC.ALL;
									current.setType(Symbol.SymbolType.DERIVED_EVENT);
									}
	;
any_def returns [AnyMetaModel anyMetaModel]
	@init {$anyMetaModel = new AnyMetaModel();}
	: ANY ordinal = sequence_def { $anyMetaModel.ordinal = $ordinal.value;
									current.setType(Symbol.SymbolType.DERIVED_EVENT);
									}
	| ANY 						{ $anyMetaModel.ordinal = 1;
									current.setType(Symbol.SymbolType.DERIVED_EVENT);
									}
	;
sequence_def returns [int value]
	: LPAREN intValue=INT RPAREN {$value = Integer.parseInt($intValue.getText());}
	| intValue=INT				{$value = Integer.parseInt($intValue.getText());}
	;
show_def returns [ShowMetaModel showMetaModel]
	: SHOW LPAREN 'true' RPAREN  {current.setShow(true);
								$showMetaModel = new ShowMetaModel();
								$showMetaModel.symbolTable = symbolTable;
								$showMetaModel.isShow = true;
							}
	| SHOW LPAREN 'false' RPAREN {current.setShow(false);
								$showMetaModel = new ShowMetaModel();
								$showMetaModel.symbolTable = symbolTable;
								$showMetaModel.isShow = false;
							}
	;
priority_def returns [PriorityMetaModel priorityMetaModel]
	@init {$priorityMetaModel = new PriorityMetaModel();}
	: PRIORITY sequenceDef=sequence_def {
								current.setPriority($sequenceDef.value);
								$priorityMetaModel.priority = $sequenceDef.value;
							}
	;
between_def returns [DurationMetaModel durationMetaModel]
	@init {$durationMetaModel = new DurationMetaModel();}
	: BETWEEN LPAREN '\'' effectiveDate=DATE_TIME '\'' COMMA '\'' expirationDate=DATE_TIME '\'' RPAREN {
									W3CDateFormat format = new W3CDateFormat(W3CDateFormat.Pattern.AUTO);
									Date effectiveDate=null;
									Date expirationDate=null;
									try {
										effectiveDate = format.parse($effectiveDate.getText());
										expirationDate = format.parse($expirationDate.getText());
										$durationMetaModel.effectiveDate = format.parse($effectiveDate.getText());
										$durationMetaModel.expirationDate = format.parse($expirationDate.getText());
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
										$durationMetaModel.effectiveDate = format.parse($startDate.getText());
									} catch (ParseException e) {e.printStackTrace();}
								}
	;
repeat_def returns [RepeatMetaModel repeatMetaModel]
	: REPEAT LPAREN repeatOnFrequency=repeat_frequency_def RPAREN {
								$repeatMetaModel = $repeatOnFrequency.repeatMetaModel;
							}
	| REPEAT LPAREN repeatOnFrequency=repeat_frequency_def COMMA timeFrame=INT RPAREN {
								$repeatMetaModel = $repeatOnFrequency.repeatMetaModel;
								$repeatMetaModel.repeatAfter = Integer.valueOf($timeFrame.getText());
							}
	;
repeat_frequency_def returns [RepeatMetaModel repeatMetaModel]
	@init {$repeatMetaModel = new RepeatMetaModel();}
	: repeatFrequency='WEEKLY'  {$repeatMetaModel.repeatCriteria = RepeatMetaModel.RepeatCriteria.WEEKLY;}
	| repeatFrequency='MONTHLY' {$repeatMetaModel.repeatCriteria = RepeatMetaModel.RepeatCriteria.MONTHLY;}
	| repeatFrequency='YEARLY'  {$repeatMetaModel.repeatCriteria = RepeatMetaModel.RepeatCriteria.YEARLY;}
	| repeatFrequency='ACTIVITY_DATE' {$repeatMetaModel.repeatCriteria = RepeatMetaModel.RepeatCriteria.ACTIVITY_DATE;}
	;
gatekeeper_def returns [GatekeeperMetaModel gatekeeperMetaModel]
	@init{$gatekeeperMetaModel = new GatekeeperMetaModel();} 
	: GATEKEEPER LPAREN eventDef=event_def RPAREN {
									$gatekeeperMetaModel.eventMetaModel = $eventDef.eventMetaModel;
									$eventDef.eventMetaModel.parent = $gatekeeperMetaModel;
									symbolStack.pop();
								}
	;
reward_def returns [RewardMetaModel rewardMetaModel]
	: REWARD LPAREN rewardQuantityDef=reward_quantity_def{
									$rewardMetaModel = $rewardQuantityDef.rewardMetaModel;								
								}  
								(COMMA maxRewardAmount=DOUBLE {
									$rewardMetaModel.maxRewardAmount = Double.parseDouble($maxRewardAmount.getText());
									}
									| COMMA maxRewardAmount=INT {
									$rewardMetaModel.maxRewardAmount = Double.parseDouble($maxRewardAmount.getText());
									})* RPAREN
									
	;
reward_quantity_def returns [RewardMetaModel rewardMetaModel]
	: rewardQuantity=DOUBLE {
							double rewardAmount = 0;
							Reward reward = new Reward();
							try {
								rewardAmount = Double.parseDouble($rewardQuantity.getText());
								reward.setAmount(rewardAmount);
							}catch(Exception exp){exp.printStackTrace();}
							current.setReward(reward);
							$rewardMetaModel = new RewardMetaModel();
							$rewardMetaModel.symbolTable = symbolTable;
							$rewardMetaModel.rewardAmount = rewardAmount;
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
							$rewardMetaModel = new RewardMetaModel();
							$rewardMetaModel.symbolTable = symbolTable;
							$rewardMetaModel.rewardAmount = rewardAmount;
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

