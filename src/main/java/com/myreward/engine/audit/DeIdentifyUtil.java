package com.myreward.engine.audit;

import org.apache.commons.lang3.StringUtils;

public class DeIdentifyUtil {
	   /**
     * private constructor to avoid instantiation of this class
     */
    private DeIdentifyUtil(){
    	
    }

	/**
	 * Deidentify left.
	 *
	 * @param str the str
	 * @param size the size
	 * @return the string
	 * 
	 * @since 2.0.0
	 */
	public static String deidentifyLeft(String str, int size) {
		int repeat;
		if (size > str.length()) {
			repeat = str.length();
		} else {
			repeat = size;
		}
		return StringUtils.overlay(str, StringUtils.repeat('*', repeat), 0, size);
	}

	/**
	 * Deidentify right.
	 *
	 * @param str the str
	 * @param size the size
	 * @return the string
	 * 
	 * @since 2.0.0
	 */
	public static String deidentifyRight(String str, int size) {
		int end = str.length();
		int repeat;
		if (size > str.length()) {
			repeat = str.length();
		} else {
			repeat = size;
		}
		return StringUtils.overlay(str, StringUtils.repeat('*', repeat), end - size, end);
	}

	/**
	 * Deidentify from left.
	 *
	 * @param str the str
	 * @param size the size
	 * @return the string
	 * 
	 * @since 2.0.0
	 */
	public static String deidentifyFromLeft(String str, int size) {
		int end = str.length();
		int repeat;
		if (size > str.length()) {
			repeat = 0;
		} else {
			repeat = str.length() - size;
		}
		return StringUtils.overlay(str, StringUtils.repeat('*', repeat), size, end);
	}

	/**
	 * Deidentify from right.
	 *
	 * @param str the str
	 * @param size the size
	 * @return the string
	 * 
	 * @since 2.0.0
	 */
	public static String deidentifyFromRight(String str, int size) {
		int end = str.length();
		int repeat;
		if (size > str.length()) {
			repeat = str.length();
		} else {
			repeat = end - size;
		}
		return StringUtils.overlay(str, StringUtils.repeat('*', repeat), 0, end - size);
	}

	/**
	 * Deidentify middle.
	 *
	 * @param str the str
	 * @param start the start
	 * @param end the end
	 * @return the string
	 * 
	 * @since 2.0.0
	 */
	public static String deidentifyMiddle(String str, int start, int end) {

		int repeat;
		if (end - start > str.length()) {
			repeat = str.length();
		} else {
			repeat = (str.length()- end) - start;
		}
		return StringUtils.overlay(str, StringUtils.repeat('*', repeat), start, str.length()-end);
	}

	/**
	 * Deidentify edge.
	 *
	 * @param str the str
	 * @param start the start
	 * @param end the end
	 * @return the string
	 * 
	 * @since 2.0.0
	 */
	public static String deidentifyEdge(String str, int start, int end) {
		return deidentifyLeft(deidentifyRight(str, end), start);
	}

	/**
	 * Deidentify.
	 *
	 * @param text the text
	 * @param left the left
	 * @param right the right
	 * @param fromLeft the from left
	 * @param fromRight the from right
	 * @return the string
	 * 
	 * @since 2.0.0
	 */
	public static String deidentify(String text, int left, int right, int fromLeft, int fromRight) {
		if (left == 0 && right == 0 && fromLeft == 0 && fromRight == 0) {
			return StringUtils.repeat('*', text.length());
		} else if (left > 0 && right == 0 && fromLeft == 0 && fromRight == 0) {
			return deidentifyLeft(text, left);
		} else if (left == 0 && right > 0 && fromLeft == 0 && fromRight == 0) {
			return deidentifyRight(text, right);
		}else if (left > 0 && right > 0 && fromLeft == 0 && fromRight == 0) {
			return deidentifyEdge(text, left, right);
		}else if (left == 0 && right == 0 && fromLeft > 0 && fromRight == 0) {
			return deidentifyFromLeft(text, fromLeft);
		}else if (left == 0 && right == 0 && fromLeft == 0 && fromRight > 0) {
			return deidentifyFromRight(text, fromRight);
		}else if (left == 0 && right == 0 && fromLeft > 0 && fromRight > 0) {
			return deidentifyMiddle(text, fromLeft, fromRight);
		}

		return text;
	}

}
