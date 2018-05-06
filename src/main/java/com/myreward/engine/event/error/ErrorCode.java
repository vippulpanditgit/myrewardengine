package com.myreward.engine.event.error;

public enum ErrorCode {
	FUNCTION_NOT_FOUND, 
	EVENT_METADATA_NOT_PRESET, 
	LABEL_MAIN_NOT_FOUND, GENERAL_PARSING_EXCEPTION, NO_PCODE_GENERATED,  //There is no rule in the system to generate the PCODE
	DATASEGMENT_NOT_INITIALIZED, SYMBOL_NOT_FOUND, OVERRIDE_FALSE_TO_SEGMENT_PRESENT, FROM_SEGMENT_NOT_PRESENT, //Data segment has not been initialized.
	

}
