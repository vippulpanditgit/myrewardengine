package com.myreward.parser.test.scenario;

public class MetaStandAloneEventWithGatekeeper {
	public static String metaData = "package myclient event(A).reward(1).between('2018-01-01T00:00:00.00+01:00','2019-01-01T00:00:00.00+01:00').repeat(MONTHLY,2).show(true).priority(1).gatekeeper(event(KB))";
}
