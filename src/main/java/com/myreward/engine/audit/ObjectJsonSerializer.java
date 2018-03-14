package com.myreward.engine.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

public class ObjectJsonSerializer implements IObjectSerializer {

	public final static byte[] toJson(Object object, DeIdentify deidentify) throws JsonProcessingException {
		if (isPrimitive(object)) {
			Object deidentifiedObj = deidentifyObject(object, deidentify);
			String primitiveValue = String.valueOf(deidentifiedObj);
			if (object instanceof String || object instanceof Character || !object.equals(deidentifiedObj)) {
				primitiveValue = '"' + primitiveValue + '"';
			}
			return primitiveValue.getBytes();
		}
		return ObjectJsonSerializer.serialize(object);
	}
	public final static Object deidentifyObject(Object object, DeIdentify deidentify) {
		if (object == null || deidentify == null) {
			return object;
		}
		return DeIdentifyUtil.deidentify(String.valueOf(object),
				deidentify.left(), deidentify.right(),
				deidentify.fromleft(), deidentify.fromRight());
	}


	public final static byte[] serialize(Object object) throws JsonProcessingException {
		final ObjectMapper mapper = new ObjectMapper();
		final ObjectWriter w = mapper.writer();
		// enable one feature, disable another
		byte[] json = w
		  .with(SerializationFeature.INDENT_OUTPUT)
		  .without(SerializationFeature.FAIL_ON_EMPTY_BEANS)
		  .writeValueAsBytes(object);
		return json;
	}
    /** 
      * Checks if is primitive. 
      * 
      * @param object        the object 
      * @return true, if is primitive 
      */ 
     public final static boolean isPrimitive(Object object) { 
         if (object instanceof String || object instanceof Number || object instanceof Boolean 
                 || object instanceof Character) { 
             return true; 
         } 
         return false; 
     } 

}
