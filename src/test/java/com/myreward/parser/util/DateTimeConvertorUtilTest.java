/**
 * 
 */
package com.myreward.parser.util;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



/**
 * @author vpandit
 *
 */
public class DateTimeConvertorUtilTest {

	private Date today;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	public void setUp() throws Exception {
		today = new Date();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	public void tearDown() throws Exception {
		today = null;
	}

	/**
	 * Test method for {@link com.myreward.parser.util.DateTimeConvertorUtil#toISO8601UTC(java.util.Date)}.
	 */
	@Test
	public void testToISO8601UTC() {
		String iso860Date = DateTimeConvertorUtil.toISO8601UTC(today);
		Date resultingDate = DateTimeConvertorUtil.fromISO8601UTC(iso860Date);
		assertTrue(today.compareTo(resultingDate)==0);
	}

	/**
	 * Test method for {@link com.myreward.parser.util.DateTimeConvertorUtil#toLong(java.util.Date)}.
	 */
	@Test
	public void testToLong() {
		Long dateInLog = DateTimeConvertorUtil.toLong(today);
		Date reverseEngineeredDate = new Date(dateInLog.longValue());
		assertTrue(today.compareTo(reverseEngineeredDate)==0);
	}

	/**
	 * Test method for {@link com.myreward.parser.util.DateTimeConvertorUtil#fromISO8601UTC(java.lang.String)}.
	 */
	@Test
	public void testFromISO8601UTC() {
		String iso860Date = DateTimeConvertorUtil.toISO8601UTC(today);
		Date resultingDate = DateTimeConvertorUtil.fromISO8601UTC(iso860Date);
		assertTrue(today.compareTo(resultingDate)==0);
	}

}
