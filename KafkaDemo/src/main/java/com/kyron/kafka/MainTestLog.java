package com.kyron.kafka;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 * Test writing log to console and file. See resources/log4j2.properties 
 * for log file name and location. See pom.xml for dependencies.
 */
public class MainTestLog {

    private static final Logger LOGGER = LogManager.getLogger(MainTestLog.class.getName());
    
    public static void main(String[] args) 
    {
        LOGGER.debug("Debug Message Logged !!!");
        LOGGER.info("Info Message Logged !!!");
        LOGGER.error("Error Message Logged !!!", new NullPointerException("NullError"));
    }

}
