package com.myreward.parser.util;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import com.myreward.engine.app.AppConstants;

public class InternalLogger {
    /** The Constant MyReward_INFO. */
    private static final String APP_INFO = AppConstants.APP_NAME + ":INFO ";

    /** The Constant MyReward_WARN. */
    private static final String APP_WARN = AppConstants.APP_NAME + ":WARN ";

    /** The Constant MyReward_ERROR. */
    private static final String APP_ERROR = AppConstants.APP_NAME + ":ERROR ";

   /** The info stream. */
    private static PrintStream infoStream = System.out;

    /** The warn stream. */
    private static PrintStream warnStream = System.err;

    /** The error stream. */
    private static PrintStream errorStream = System.err;
    /**
     * private constructor to avoid instantiation of this class
     */
    private InternalLogger(){
    	
    }
    /**
     * Write information in the console.
     * 
     * @param message
     *            the message
     */
    public static void info(final Object message) {
        infoStream.println(APP_INFO + message.toString());
    }

    /**
     * Write information in the console.
     * 
     * @param message
     *            the message
     * 
     * @since 2.3.0
     */
    public static void info(Object... message) {
        StringBuilder builder = new StringBuilder(APP_INFO);
        for (Object object : message) {
            builder.append(object.toString());
        }
        infoStream.println(builder.toString());
    }

    /**
     * Write warn messasge on console.
     * 
     * @param message
     *            the message
     */
    public static void warn(final Object message) {
        warnStream.println(APP_WARN + message.toString());
    }

    /**
     * Write warn messasge on console.
     * 
     * @param message
     *            the message
     * 
     * @since 2.3.0
     */
    public static void warn(Object... message) {
        StringBuilder builder = new StringBuilder(APP_WARN);
        for (Object object : message) {
            builder.append(object.toString());
        }
        warnStream.println(builder.toString());
    }

    /**
     * Write warn message on console with exception.
     * 
     * @param message
     *            the message
     * @param t
     *            the t
     */
    public static void warn(final Object message, final Throwable t) {
        warnStream.println(APP_WARN + message.toString());
        warnStream.println(stackTraceToString(t));
    }

    /**
     * Write error message on console.
     * 
     * @param message
     *            the message
     */
    public static void error(final Object message) {
        errorStream.println(APP_ERROR + message.toString());
    }

    /**
     * Write error message on console.
     * 
     * @param message
     *            the message
     * 
     * @since 2.3.0
     */
    public static void error(Object... message) {
        StringBuilder builder = new StringBuilder(APP_ERROR);
        for (Object object : message) {
            builder.append(object.toString());
        }
        errorStream.println(builder.toString());
    }

    /**
     * Write error messages on console with exception.
     * 
     * @param message
     *            the message
     * @param t
     *            the t
     */
    public static void error(final Object message, final Throwable t) {
        errorStream.println(APP_ERROR + message.toString());
        errorStream.println(stackTraceToString(t));
    }

    /**
     * Convert Stack trace to string.
     * 
     * @param t
     *            the t
     * @return the string
     */
    private static String stackTraceToString(final Throwable t) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }

}
