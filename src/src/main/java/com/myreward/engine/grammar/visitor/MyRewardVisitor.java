package com.myreward.engine.grammar.visitor;

import java.util.Stack;

import com.myreward.engine.grammar.MyRewardBaseVisitor;
import com.myreward.engine.grammar.MyRewardParser.All_defContext;
import com.myreward.engine.grammar.MyRewardParser.Any_defContext;
import com.myreward.engine.grammar.MyRewardParser.Between_defContext;
import com.myreward.engine.grammar.MyRewardParser.EventGroupingContext;
import com.myreward.engine.grammar.MyRewardParser.Event_defContext;
import com.myreward.engine.grammar.MyRewardParser.Event_modifier_defContext;
import com.myreward.engine.grammar.MyRewardParser.Event_nameContext;
import com.myreward.engine.grammar.MyRewardParser.Gatekeeper_defContext;
import com.myreward.engine.grammar.MyRewardParser.Group_defContext;
import com.myreward.engine.grammar.MyRewardParser.LogicalAndContext;
import com.myreward.engine.grammar.MyRewardParser.LogicalOrContext;
import com.myreward.engine.grammar.MyRewardParser.Myreward_defContext;
import com.myreward.engine.grammar.MyRewardParser.Priority_defContext;
import com.myreward.engine.grammar.MyRewardParser.Repeat_defContext;
import com.myreward.engine.grammar.MyRewardParser.Repeat_frequency_defContext;
import com.myreward.engine.grammar.MyRewardParser.Reward_defContext;
import com.myreward.engine.grammar.MyRewardParser.Reward_quantity_defContext;
import com.myreward.engine.grammar.MyRewardParser.Sequence_defContext;
import com.myreward.engine.grammar.MyRewardParser.Show_defContext;
import com.myreward.engine.grammar.Symbol;
import com.myreward.engine.grammar.SymbolTable;

public class MyRewardVisitor extends MyRewardBaseVisitor<String> {
	private SymbolTable symbolTable;

	private boolean isInGroup = false;
	private Stack<String> dataStack = new Stack<String>();
	private Stack<String> appStack = new Stack<String>();
	private Stack<String> instructionStack = new Stack<String>();

	public SymbolTable getSymbolTable() {
		return symbolTable;
	}

	public void setSymbolTable(SymbolTable symbolTable) {
		this.symbolTable = symbolTable;
	}

	@Override
	public String visitRepeat_def(Repeat_defContext ctx) {
		// TODO Auto-generated method stub

		return visitChildren(ctx);
	}

	@Override
	public String visitEvent_def(Event_defContext ctx) {
		// TODO Auto-generated method stub
		for(int index=0; index < ctx.getChildCount();index++) {
			if(ctx.getChild(index) instanceof Event_nameContext) {
				Event_nameContext eventNameContext = (Event_nameContext)ctx.getChild(index);
				String eventName = eventNameContext.eventName.getText();
				Symbol eventNameSymbol = new Symbol(eventName);
				eventNameSymbol = this.getSymbolTable().lookup(eventNameSymbol);
				if(isInGroup) {
					System.out.println(eventNameSymbol);
					dataStack.push("push("+eventNameSymbol.getId()+")");
					if(!dataStack.isEmpty()
							&& dataStack.size()==2
							&& !instructionStack.isEmpty()
							&& instructionStack.size()==1) {
						appStack.push(dataStack.pop());
						appStack.push(dataStack.pop());
						appStack.push(instructionStack.pop());
						dataStack.push("push(output)");
					}
				}
				
			}
		}
		return visitChildren(ctx);
	}

	@Override
	public String visitEvent_name(Event_nameContext ctx) {
		// TODO Auto-generated method stub

		return visitChildren(ctx);
	}

	@Override
	public String visitGroup_def(Group_defContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}

	@Override
	public String visitAll_def(All_defContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}

	@Override
	public String visitAny_def(Any_defContext ctx) {
		// TODO Auto-generated method stub
		
		return visitChildren(ctx);
	}

	@Override
	public String visitSequence_def(Sequence_defContext ctx) {
		// TODO Auto-generated method stub
		
		return visitChildren(ctx);
	}

	@Override
	public String visitPriority_def(Priority_defContext ctx) {
		// TODO Auto-generated method stub

		return visitChildren(ctx);
	}

	@Override
	public String visitBetween_def(Between_defContext ctx) {
		// TODO Auto-generated method stub

		return visitChildren(ctx);
	}

	@Override
	public String visitGatekeeper_def(Gatekeeper_defContext ctx) {
		// TODO Auto-generated method stub

		return visitChildren(ctx);
	}

	@Override
	public String visitReward_def(Reward_defContext ctx) {
		// TODO Auto-generated method stub

		return visitChildren(ctx);
	}

	@Override
	public String visitShow_def(Show_defContext ctx) {
		// TODO Auto-generated method stub

		return visitChildren(ctx);
	}

	@Override
	public String visitEvent_modifier_def(Event_modifier_defContext ctx) {
		// TODO Auto-generated method stub

		return visitChildren(ctx);
	}

	@Override
	public String visitLogicalOr(LogicalOrContext ctx) {
		// TODO Auto-generated method stub
		instructionStack.push("OR()");
		return visitChildren(ctx);
	}

	@Override
	public String visitLogicalAnd(LogicalAndContext ctx) {
		// TODO Auto-generated method stub

		return visitChildren(ctx);
	}

	@Override
	public String visitRepeat_frequency_def(Repeat_frequency_defContext ctx) {
		// TODO Auto-generated method stub

		return visitChildren(ctx);
	}

	@Override
	public String visitReward_quantity_def(Reward_quantity_defContext ctx) {
		// TODO Auto-generated method stub

		return visitChildren(ctx);
	}

	@Override
	public String visitMyreward_def(Myreward_defContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}

	@Override
	public String visitEventGrouping(EventGroupingContext ctx) {
		isInGroup = true;
		// TODO Auto-generated method stub
		appStack.push("{");
		appStack.push("push(local_stored_register_xyz)");
		String result = visitChildren(ctx);
		appStack.push("store(local_stored_register_xyz)");
		appStack.push("pop(local_stored_register_xyz)");
		appStack.push("}");
		isInGroup = false;
		return result;
	}


}
