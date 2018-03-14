package com.myreward.engine.audit;

import com.myreward.parser.util.StopWatch;

public final class AuditContext {
		
	
	/** The audit output stream. */ 
 	private static IAuditOutputStream<AuditEvent> auditStream; 

	private AuditContext() {
		
	}
	final static void init() {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
	}

}
