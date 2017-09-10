package com.itechart.app.logging;

import org.apache.log4j.Logger;

/**
 * defines apps logger, that uses log4j library
 */
public class AppLogger {

    /** logger object from log4j library */
    private static final Logger logger = Logger.getLogger(AppLogger.class);

    /**
     * private constructor of class, which implement non-instantiation
     */
    private AppLogger(){}

    public static void debug(final String message){
        logger.debug(message);
    }

    public static void error(final String message){
        logger.error(message);
    }

    public static void fatal(final String message){
        logger.fatal(message);
    }

    public static void info(final String message){
        logger.info(message);
    }

    public static void warn(final String message){
        logger.warn(message);
    }

    public static void trace(final String message){
        logger.trace(message);
    }
}
