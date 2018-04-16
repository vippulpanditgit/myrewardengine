package com.myreward.parser.test.scenario;

public class MetaStandAloneWithShowPriorityDurationGatekeeper {
	public static String metaData = "package MetaStandAloneWithShowPriorityDurationGatekeeper; event(A).between('2000-07-17T19:20:30.45+01:00','2018-07-16T19:20:30.45+01:00').reward(10,100).repeat(WEEKLY,2).show(true).priority(1).gatekeeper(event(KB))";

}
